package org.goblynn.gobdungeon.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import kotlin.random.Random

class Player : Fighter(Character.PLAYER) {
    var hunger by mutableIntStateOf(100)
        private set

    fun passTime() {
        if (health < character.health && !dead) {
            if (hunger > 0) {
                hunger = 0.coerceAtLeast(hunger - Random.nextInt(1, 4))
                receiveDamage(-Random.nextInt(1, 3))
            }
        }

    }
}