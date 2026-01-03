package org.goblynn.gobdungeon.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import kotlin.random.Random

class Player(onDamage: (info: String) -> Unit, startingInventory: List<Item>) :
    Fighter(Character.PLAYER, onDamage, startingInventory) {
    /**
     * Property describing how full player is. If above 0 player will slowly heal
     */
    var hunger by mutableIntStateOf(100)
        private set

    /**
     * Equivalent to long rest in Baldur's gate 3. Restore health and similar stats to full
     */
    fun rest() {
        hunger = 100
        health = character.health
    }

    fun restoreHunger(amount: Int) {
        hunger = (hunger + amount).coerceAtMost(100)
    }

    val spells = mutableStateListOf<Spell>()

    /**
     * Try to add a new spell to spell list, if spell is already present it will not be added
     * @param spell spell to add
     */
    fun addSpell(spell: Spell) {
        if (!spells.contains(spell)) {
            spells.add(spell)
        }
    }

    /**
     * Use item
     */
    fun useItem(item: Item) {
        if (item.weapon != null) {
            currentWeapon = item.weapon
            return
        }
        restoreHunger(item.hungerRestore)
        receiveDamage(-item.healthRestore)
        for ((e, d) in item.effect) {
            addEffect(e, d)
        }
        when (item) {
            Item.BOTTLE_OF_HONEY -> {
                removeIllnessEffects()
            }

            else -> {}
        }
        removeItem(item)
    }

    /**
     * Calculate all logic related to advancing time, such as heal from food or damage from poison
     */
    fun passTime() {
        if (health < character.health && !isDead) {
            if (hunger > 0) {
                hunger = (hunger - Random.nextInt(1, 4)).coerceAtLeast(0)
                receiveDamage(-Random.nextInt(1, 3))
            }
        }

    }
}