package org.goblynn.gobdungeon.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.random.Random

/**
 * Class responsible for handling
 */
class GameManager(level: Levels, val onPlayerDied: () -> Unit) {
    var currentMovementSpeed by mutableIntStateOf(1)
        private set

    var currentLevel by mutableStateOf(level)
        private set

    var currentLootDrop by mutableStateOf<LootDrop?>(null)
        private set

    var playerIsStunned by mutableStateOf(false)
        private set

    val shoppingOptions = mutableStateMapOf<Item, Int>()

    val canUseItems: Boolean
        get() = currentState == GameState.WALKING || currentState == GameState.FIGHTING

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

    val hasReachedTheEnd: Boolean
        get() = currentDistance >= targetDistance

    /**
     * Current state of the game
     */
    var currentState by mutableStateOf(GameState.WALKING)


    var enemy by mutableStateOf<Fighter>(
        Fighter(Character.BEE, onDamage = { displayFighterMessage(it) })
    )
        private set

    var player by mutableStateOf(
        Player(
            onDamage = { displayFighterMessage(it) },
            level.startingItems
        )
    )
        private set

    /**
     * Log of all events that have occurred since starting the game
     */
    val actionLog = mutableStateListOf<String>()

    var facingBoss by mutableStateOf(false)
        private set

    fun beginShopping() {
        if (currentState != GameState.WALKING || currentLevel.shopOptions.isEmpty()) {
            return
        }
        shoppingOptions.clear()
        val itemCount = Random.nextInt(1, currentLevel.shopOptions.size)
        for (i in 0..<itemCount) {
            val opt = currentLevel.shopOptions.random()
            shoppingOptions[opt.item] = Random.nextInt(opt.minPrice, opt.maxPrice)
        }
        currentState = GameState.SHOPPING
    }

    fun stopShopping() {
        currentState = GameState.WALKING
    }

    /**
     * Remove shopping option for this item with given price
     * @param item item
     * @param price specific price of the item
     */
    fun removeShoppingOption(item: Item, price: Int) {
        shoppingOptions.remove(item)
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
        if (enemy.isDead) {
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
        if (player.isDead) {
            onPlayerDied()
            return
        }
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

    /**
     * Execute random event that could occur while player is walking around
     */
    fun executeRandomEvents() {
        if (Random.nextFloat() <= currentLevel.chanceOfEnemy) {
            startCombat(currentLevel.enemies.random())
        } else if (Random.nextFloat() <= currentLevel.chanceOfSkip) {
            currentState = GameState.CLIMBING
        } else if (Random.nextFloat() <= currentLevel.chanceOfShop) {
            beginShopping()
        } else if (Random.nextFloat() <= 0.1f) {
            val item = currentLevel.findableItems.random()
            player.inventory.add(item)
            log("Found ${item.displayName}")
        }
    }

    /**
     * Begin the boss battle that occurs at the end of the level
     */
    fun beginBossBattle() {
        startCombat(currentLevel.endBosses[currentStage])
        facingBoss = true

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

    }
}