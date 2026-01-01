package org.goblynn.gobdungeon

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.goblynn.gobdungeon.game.Character
import org.goblynn.gobdungeon.game.Fighter
import org.goblynn.gobdungeon.game.GameState
import org.goblynn.gobdungeon.game.Item
import org.goblynn.gobdungeon.game.Levels
import org.goblynn.gobdungeon.game.Player
import org.goblynn.gobdungeon.game.Spell
import kotlin.random.Random

class GameViewModel : ViewModel() {

    var currentMovementSpeed by mutableIntStateOf(1)
        private set
    var currentLevel by mutableStateOf(Levels.ENTRANCE)
        private set

    /**
     * Current stage of the level. Depending on how high the value is different events might occur
     */
    var currentStage by mutableIntStateOf(0)
        private set

    var currentDistance by mutableIntStateOf(0)
        private set

    /**
     * How far has the player has to go before reaching a boss and having a short rest
     */
    var targetDistance by mutableIntStateOf(500)
        private set

    /**
     * Current state of the game
     */
    var currentState by mutableStateOf(GameState.WALKING)
        private set

    var enemy by mutableStateOf<Fighter>(
        Fighter(Character.BEE, onDamage = { displayFighterMessage(it) })
    )
        private set

    var player by mutableStateOf(Player(onDamage = { displayFighterMessage(it) }))
        private set

    /**
     * Log of all events that have occurred since starting the game
     */
    val actionLog = mutableStateListOf<String>()

    var facingBoss: Boolean = false
        private set


    fun displayFighterMessage(text: String) {
        actionLog.add(text)
    }

    /**
     * Begin combat with provided character. Changes state to fighting
     * @param newEnemy The enemy to fight
     */
    fun startCombat(newEnemy: Character) {
        currentState = GameState.FIGHTING
        enemy = Fighter(newEnemy, onDamage = { displayFighterMessage(it) })
    }


    fun playerAttackMelee() {
        val damage = player.attack(enemy)
        if (damage != null) {
            actionLog.add("${player.character.characterName} ${player.currentWeapon.attackMessageVerb} ${enemy.character.characterName} for $damage")
        } else {
            actionLog.add("${player.character.characterName} missed!")
        }
        endTurn()
    }

    /**
     * Roll the die on applying a spell
     * @param caster who is casting the spell
     * @param target who is the target
     * @param spell what spell to apply
     */
    fun applySpell(caster: Fighter, target: Fighter, spell: Spell): Boolean {
        if (Random.nextInt(0, 20) + caster.character.charisma > 8) {
            val damage = Random.nextInt(
                caster.character.charisma,
                2 * caster.character.charisma
            ) + spell.bonusDamage
            caster.useArcana(spell.cost)
            target.receiveDamage(damage)
            for ((effect, duration) in spell.effects) {
                target.addEffect(effect, duration)
            }
            actionLog.add("${target.character.characterName} casts ${spell.displayName} and deals $damage damage ")
            return true
        } else {
            actionLog.add("${caster.character.characterName} failed to cast spell!")
            return false
        }
    }

    fun runEnemyLogic() {
        if (enemy.isStunned) {
            actionLog.add("${enemy.character.characterName} is stunned")
            return
        }
        if (enemy.castableSpells.isNotEmpty() && Random.nextFloat() < enemy.character.chanceOfUsingSpells) {
            applySpell(enemy, player, enemy.castableSpells.random())
        } else {
            val damage = enemy.attack(player)
            if (damage != null) {
                actionLog.add("${enemy.character.characterName} ${enemy.currentWeapon.attackMessageVerb} ${player.character.characterName} for $damage")
            } else {
                actionLog.add("${enemy.character.characterName} missed!")
            }
        }
    }

    fun endTurn() {
        enemy.advanceEffects()
        if (enemy.dead) {
            actionLog.add("Defeated ${enemy.character.characterName}")
            currentState = GameState.VICTORY
            return
        }
        runEnemyLogic()

        if (player.isStunned) {
            player.advanceEffects()
            actionLog.add("${player.character.characterName} is stunned!")
            endTurn()
        } else {
            player.advanceEffects()
        }
    }

    /**
     * Use item as player. If item is weapon every stat is ignored
     */
    fun useItem(item: Item) {
        if (item.weapon != null) {
            player.currentWeapon = item.weapon
            return
        }
        player.restoreHunger(item.hungerRestore)
        player.receiveDamage(-item.healthRestore)
        for ((e, d) in item.effect) {
            player.addEffect(e, d)
        }
        player.removeItem(item)
    }

    fun acceptVictory() {
        if (currentState == GameState.VICTORY) {
            if (facingBoss) {
                facingBoss = false
                currentDistance = 0
                targetDistance = (targetDistance * currentLevel.distanceIncrease).toInt()
                currentStage += 1
                player.rest()
            }
            currentState = GameState.WALKING
        }
    }

    fun winSkip() {
        val dist = Random.nextInt(150, 200)
        advanceDistance(dist)
        actionLog.add("Skipped ${dist}m!")
        currentState = GameState.WALKING
    }

    fun looseSkip() {
        currentState = GameState.WALKING
    }

    fun advanceDistance(value: Int) {
        currentDistance = (currentDistance + value).coerceAtMost(targetDistance)
        if (currentDistance == targetDistance) {
            startCombat(currentLevel.enemies[currentStage])
            facingBoss = true
        }
    }

    fun makeStep() {
        currentDistance += currentMovementSpeed
        player.passTime()
        actionLog.add("Moved by ${currentMovementSpeed}m")
        if (Random.nextFloat() <= currentLevel.chanceOfEnemy) {
            startCombat(currentLevel.enemies.random())
        } else if (Random.nextFloat() <= currentLevel.chanceOfSkip) {
            currentState = GameState.CLIMBING
        }
    }
}