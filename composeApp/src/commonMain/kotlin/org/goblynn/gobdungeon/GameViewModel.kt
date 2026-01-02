package org.goblynn.gobdungeon

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.goblynn.gobdungeon.game.Character
import org.goblynn.gobdungeon.game.Fighter
import org.goblynn.gobdungeon.game.GameManager
import org.goblynn.gobdungeon.game.GameState
import org.goblynn.gobdungeon.game.Item
import org.goblynn.gobdungeon.game.Levels
import org.goblynn.gobdungeon.game.Player
import kotlin.random.Random

class GameViewModel : ViewModel() {

    var game by mutableStateOf<GameManager?>(null)
        private set


    /**
     * Start a new game with current level. All values are reset to 0
     * @param level level to enter
     */
    fun enterLevel(level: Levels) {
        game = GameManager(level)
    }

    fun playerAttackMelee() {
        game?.let { game ->
            val damage = game.player.attack(game.enemy)
            if (damage != null) {
                game.log("${game.player.character.characterName} ${game.player.currentWeapon.attackMessageVerb} ${game.enemy.character.characterName} for $damage")
            } else {
                game.log("${game.player.character.characterName} missed!")
            }
            game.endTurn()
        }
    }


    /**
     * Use item as player. If item is weapon every stat is ignored
     */
    fun useItem(item: Item) {
        game?.player?.useItem(item)
    }

    fun acceptVictory() {
       game?.endCombat()
    }

    fun winSkip() {
        game?.jumpAhead()
    }

    fun looseSkip() {
        game!!.currentState = GameState.WALKING
    }


    fun makeStep() {
        game?.let { game ->
            game.advanceDistance(game.currentMovementSpeed)
            game.player.passTime()
            game.executeRandomEvents()
        }
    }

    fun skipTurn() {
        if (game == null) {
            return
        }
        game!!.log("Skipped turn")
        game!!.endTurn()
    }
}