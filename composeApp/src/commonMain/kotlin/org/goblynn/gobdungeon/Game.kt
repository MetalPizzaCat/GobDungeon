package org.goblynn.gobdungeon

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.goblynn.gobdungeon.game.Fighter
import org.goblynn.gobdungeon.game.GameState
import java.awt.Button

@Composable
fun StreetView(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = viewModel { GameViewModel() }
) {
    Column(modifier) {
        Text(viewModel.currentDistance.toString())
        Button(onClick = { viewModel.makeStep() }) {
            Text("Move forward")
        }
    }
}

@Composable
fun CombatView(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = viewModel { GameViewModel() }
) {
    var showSpecialMenu by remember { mutableStateOf(false) }
    Column(modifier) {
        FighterStats(viewModel.enemy)
        Button(onClick = { viewModel.playerAttackMelee() }) {
            Text("Melee")
        }
        Button(onClick = {}) {
            Text("Magic")
        }
        Button(onClick = { showSpecialMenu = !showSpecialMenu }) {
            Text("Special")
        }
        AnimatedVisibility(showSpecialMenu) {
            Text("Special goes here")
        }
    }
}

@Composable
fun FighterStats(
    fighter: Fighter,
    modifier: Modifier = Modifier,
    additional: @Composable () -> Unit = {}
) {
    Column(modifier) {
        Text(fighter.character.characterName)
        Row {
            Text("Health")
            LinearProgressIndicator(progress = { fighter.health.toFloat() / fighter.character.health })
        }
        additional()
    }
}

@Composable
fun Game(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = viewModel { GameViewModel() }
) {
    var showLog by remember { mutableStateOf(true) }
    var showLongerLog by remember { mutableStateOf(false) }
    var showInventory by remember { mutableStateOf(false) }
    Column(modifier.fillMaxWidth()) {
        FighterStats(
            viewModel.player,
            Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
        ) {
            Row {
                Text("Hunger")
                LinearProgressIndicator(progress = { viewModel.player.hunger / 100f })
            }
        }
        AnimatedContent(
            targetState = viewModel.currentState,
            label = "Action"
        ) { state ->
            when (state) {
                GameState.WALKING -> StreetView(modifier = Modifier.align(Alignment.CenterHorizontally))
                GameState.FIGHTING -> CombatView(modifier = Modifier.align(Alignment.CenterHorizontally))
                GameState.CLIMBING -> TODO()
                GameState.STEALING -> TODO()
                GameState.CHOICE -> TODO()
                GameState.VICTORY -> {
                    Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text("You won!")
                        Button(onClick = { viewModel.acceptVictory() }) {
                            Text("Continue")
                        }
                    }
                }

                GameState.DEFEAT -> TODO()
            }
        }
        Button(onClick = { showInventory = !showInventory }) {
            Text("Inventory")
        }
        AnimatedVisibility(showInventory) {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                viewModel.player.inventory.forEach {
                    Button(onClick = {}) {
                        Text(it.displayName)
                    }
                }
            }
        }
        Text(
            "Log:",
            Modifier.fillMaxWidth().align(Alignment.CenterHorizontally).combinedClickable(
                onClick = { showLog = !showLog },
                onLongClick = { showLog = true; showLongerLog = !showLongerLog }
            ),
        )
        AnimatedVisibility(showLog) {
            Column(
                Modifier.fillMaxWidth().align(Alignment.CenterHorizontally).verticalScroll(
                    rememberScrollState()
                )
            ) {
                viewModel.actionLog.takeLast(
                    if (showLongerLog) {
                        50
                    } else {
                        10
                    }
                ).forEach { msg ->
                    Text(msg)
                }
            }
        }
    }
}