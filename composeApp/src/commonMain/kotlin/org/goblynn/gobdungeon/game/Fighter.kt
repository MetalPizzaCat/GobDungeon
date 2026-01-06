package org.goblynn.gobdungeon.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.random.Random


/**
 * Base class for objects that are involved in the fight
 * @param character base character data for the fighter
 * @param onDamage callback function that can be used to add text to action log
 */
open class Fighter(
    val character: Character,
    val onDamage: (info: String) -> Unit,
    startingInventory: List<Item> = emptyList()
) {
    var health by mutableIntStateOf(character.health)
        protected set

    /**
     * How many arcana points character has. These are used to limit spell usage
     */
    var arcana by mutableIntStateOf(character.arcana)
        protected set

    val isDead: Boolean
        get() = health <= 0

    var currentWeapon by mutableStateOf(character.defaultWeapon)

    val inventory = mutableStateListOf<Item>(*startingInventory.toTypedArray())

    val effects = mutableStateMapOf<Effect, Int>()

    /**
     * Spells that character can actually cast in current state
     */
    val castableSpells: List<Spell>
        get() = character.spells.filter { it.cost <= arcana }

    /**
     * Check if fighter has stun effect applied
     */
    val isStunned: Boolean
        get() = effects.contains(Effect.STUN)

    /**
     * Remove first instance of the given item from the inventory. If no item is present nothing happens
     * @param item item to remove
     */
    fun removeItem(item: Item) {
        inventory.remove(item)
    }

    fun addItem(item: Item) {
        inventory.add(item)
    }

    fun useArcana(amount: Int) {
        arcana = (arcana - amount).coerceAtLeast(0)
    }

    /**
     * Try to add an effect. If effect is already present and its duration is lower it is set to the new one
     * @param effect effect to add to fighter
     * @param duration how long should effect last for
     */
    fun addEffect(effect: Effect, duration: Int) {
        if (!effects.contains(effect) || effects[effect]!! < duration) {
            effects[effect] = duration
        }
    }


    fun removeIllnessEffects() {
        effects.remove(Effect.POISON)
    }

    /**
     * Apple effects and decrease their timers
     */
    fun advanceEffects() {
        val effectsToRemove = HashSet<Effect>()
        for ((effect, duration) in effects) {
            when (effect) {
                Effect.STUN -> {/*stun obviously does nothing*/
                }

                Effect.POISON -> {
                    val damage = Random.nextInt(6)
                    receiveDamage(damage)
                    onDamage("${character.characterName} received $damage poison damage")
                }

                Effect.FIRE -> {
                    val damage = Random.nextInt(4)
                    receiveDamage(damage)
                    onDamage("${character.characterName} received $damage fire damage")
                }
            }
            effects[effect] = duration - 1
            if (effects[effect]!! <= 0) {
                effectsToRemove.add(effect)
            }
        }
        for (effect in effectsToRemove) {
            effects.remove(effect)
        }
    }

    fun receiveDamage(damage: Int) {
        health = (health - damage).coerceAtLeast(0)
    }

    fun attack(target: Fighter): Int? {
        if (Random.nextInt(0, 6) + target.character.dexterity > 5) {
            val damage = character.strength +
                    Random.nextInt(1, 6) +
                    Random.nextInt(
                        currentWeapon.damageBonusMin,
                        currentWeapon.damageBonusMax
                    )
            target.receiveDamage(damage)
            return damage
        }
        return null
    }
}
