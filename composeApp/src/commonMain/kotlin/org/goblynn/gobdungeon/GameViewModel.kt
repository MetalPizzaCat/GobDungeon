package org.goblynn.gobdungeon

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.goblynn.gobdungeon.game.EndGameStats
import org.goblynn.gobdungeon.game.GameManager
import org.goblynn.gobdungeon.game.GameState
import org.goblynn.gobdungeon.game.Item
import org.goblynn.gobdungeon.game.Levels

class GameViewModel : ViewModel() {

    var game by mutableStateOf<GameManager?>(null)
        private set

    var endGameStats by mutableStateOf<EndGameStats?>(null)
        private set


    /**
     * Start a new game with current level. All values are reset to 0
     * @param level level to enter
     */
    fun enterLevel(level: Levels) {
        game = GameManager(level, onPlayerDied = { playerDied() })
    }

    fun playerDied() {
        if (game != null) {
            endGameStats = EndGameStats(game!!.currentDistance, game!!.currentStage)
            game = null
        }
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

    fun acceptEndGameMenu() {
        endGameStats = null
    }


    /**
     * Try to buy a given item at a given price
     * @param item item to buy
     * @param price price to buy at
     * @return True if player has enough money and game is active, false otherwise
     */
    fun tryBuyItem(item: Item, price: Int): Boolean {
        val player = game?.player ?: return false
        if (player.gold < price) {
            return false
        }
        player.addItem(item)
        player.spendGold(price)
        game?.removeShoppingOption(item, price)
        return true
    }

    fun stopShopping() {
        game?.stopShopping()
    }

    /**
     * Use item as player. If item is weapon every stat is ignored
     */
    fun useItem(item: Item) {
        if (game?.canUseItems ?: false) {
            game?.player?.useItem(item)
        }
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
            if (game.hasReachedTheEnd) {
                game.beginBossBattle()
            } else {
                game.executeRandomEvents()
            }
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