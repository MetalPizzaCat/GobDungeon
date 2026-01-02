package org.goblynn.gobdungeon.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.collections.plusAssign
import kotlin.random.Random
import kotlin.text.get
import kotlin.text.toInt
import kotlin.times

/**
 * Class responsible for handling
 */
class GameManager(level: Levels) {
    var currentMovementSpeed by mutableIntStateOf(1)
        private set

    var currentLevel by mutableStateOf(level)
        private set

    var currentLootDrop by mutableStateOf<LootDrop?>(null)
        private set

    var playerIsStunned by mutableStateOf(false)
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
    var targetDistance by mutableIntStateOf(level.distance)
        private set

    /**
     * Current state of the game
     */
    var currentState by mutableStateOf(GameState.WALKING)


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

    /**
     * Begin combat with provided character. Changes state to fighting
     * @param newEnemy The enemy to fight
     */
    fun startCombat(newEnemy: Character) {
        currentState = GameState.FIGHTING
        enemy = Fighter(newEnemy, onDamage = { displayFighterMessage(it) })
    }

    fun log(msg: String) {
        actionLog.add(msg)
    }

    fun displayFighterMessage(text: String) {
        actionLog.add(text)
    }

    fun runEnemyLogic() {
        if (enemy.isStunned) {
            log("${enemy.character.characterName} is stunned")
            return
        }
        if (enemy.castableSpells.isNotEmpty() && Random.nextFloat() < enemy.character.chanceOfUsingSpells) {
            applySpell(enemy, player, enemy.castableSpells.random())
        } else {
            val damage = enemy.attack(player)
            if (damage != null) {
                log("${enemy.character.characterName} ${enemy.currentWeapon.attackMessageVerb} ${player.character.characterName} for $damage")
            } else {
                log("${enemy.character.characterName} missed!")
            }
        }
    }

    fun endTurn() {
        enemy.advanceEffects()
        if (enemy.dead) {
            log("Defeated ${enemy.character.characterName}")
            currentState = GameState.VICTORY
            currentLootDrop = if (Random.nextFloat() < enemy.character.chanceToDropLoot) {
                enemy.character.lootTable.random()
            } else {
                null
            }
            return
        }
        runEnemyLogic()
        player.advanceEffects()
        playerIsStunned = player.isStunned
    }

    fun endCombat() {
        if (currentState == GameState.VICTORY) {
            if (facingBoss) {
                advanceToNextStage()
            }
            currentState = GameState.WALKING
        }
    }

    fun executeRandomEvents() {
        if (Random.nextFloat() <= currentLevel.chanceOfEnemy) {
            startCombat(currentLevel.enemies.random())
        } else if (Random.nextFloat() <= currentLevel.chanceOfSkip) {
            currentState = GameState.CLIMBING
        } else if (Random.nextFloat() <= 0.1f) {
            val item = currentLevel.findableItems.random()
            player.inventory.add(item)
            log("Found ${item.displayName}")
        }
    }

    fun advanceToNextStage() {
        facingBoss = false
        currentDistance = 0
        targetDistance = (targetDistance * currentLevel.distanceIncrease).toInt()
        currentStage += 1
        player.rest()
    }

    fun jumpAhead() {
        val dist = Random.nextInt(150, 200)
        advanceDistance(dist)
        log("Skipped ${dist}m!")
        currentState = GameState.WALKING
    }

    fun advanceDistance(value: Int) {
        currentDistance = (currentDistance + value).coerceAtMost(targetDistance)
        log("Moved by ${currentMovementSpeed}m")
        if (currentDistance == targetDistance) {
            startCombat(currentLevel.enemies[currentStage])
            facingBoss = true
        }
    }
}