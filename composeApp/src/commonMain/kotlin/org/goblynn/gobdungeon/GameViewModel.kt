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
import kotlin.random.Random

class GameViewModel : ViewModel() {

    var currentMovementSpeed by mutableIntStateOf(1)
        private set
    var currentLevel by mutableStateOf(Levels.ENTRANCE)
        private set

    var currentDistance by mutableIntStateOf(0)
        private set

    var targetDistance by mutableIntStateOf(500)
        private set
    var currentState by mutableStateOf(GameState.WALKING)
        private set

    var enemy by mutableStateOf<Fighter>(Fighter(Character.BEE))
        private set

    var player by mutableStateOf(Player())
        private set

    val actionLog = mutableStateListOf<String>()


    fun startCombat(newEnemy: Character) {
        currentState = GameState.FIGHTING
        enemy = Fighter(newEnemy)
    }

    fun runEnemyTurn() {
        if (currentState != GameState.FIGHTING) {
            return
        }
    }

    fun writeAttackToActionLog(target: Fighter, attacker: Fighter, action: String) {
        actionLog.add("${attacker.character.characterName} $action ${target.character.characterName}")
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

    fun runEnemyLogic() {
        if (enemy.character.spells.isNotEmpty() && Random.nextFloat() < enemy.character.chanceOfUsingSpells) {
            // no spells yet
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
        if (enemy.dead) {
            actionLog.add("Defeated ${enemy.character.characterName}")
            currentState = GameState.VICTORY
        }
        runEnemyLogic()
    }

    fun useItem(item : Item){
        if(item.weapon != null){
            player.currentWeapon = item.weapon
            return
        }

    }

    fun acceptVictory() {
        if (currentState == GameState.VICTORY) {
            currentState = GameState.WALKING
        }
    }

    fun makeStep() {
        currentDistance += currentMovementSpeed
        player.passTime()
        actionLog.add("Moved by ${currentMovementSpeed}m")
        if (Random.nextFloat() <= currentLevel.chanceOfEnemy) {
            startCombat(currentLevel.enemies.random())
        }
    }
}