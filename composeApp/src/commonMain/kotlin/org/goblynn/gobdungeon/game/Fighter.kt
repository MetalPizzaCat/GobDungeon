package org.goblynn.gobdungeon.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.random.Random


open class Fighter(val character: Character) {
    var health by mutableIntStateOf(character.health)
        private set

    var dead by mutableStateOf(false)
        private set

    var currentWeapon by mutableStateOf(character.defaultWeapon)

    val inventory = mutableStateListOf<Item>()

    fun receiveDamage(damage: Int) {
        health -= damage
        if (health < 0) {
            dead = true
        }
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
