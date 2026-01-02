package org.goblynn.gobdungeon

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.goblynn.gobdungeon.game.Fighter
import org.goblynn.gobdungeon.game.GameState
import kotlin.math.floor
import kotlin.random.Random

@Composable
fun StreetView(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = viewModel { GameViewModel() }
) {
    Column(modifier) {
        Text(viewModel.game!!.currentDistance.toString())
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
    AnimatedContent(viewModel.game!!.playerIsStunned) { state ->
        if (!state) {
            Column {
                FlowRow(modifier) {
                    FighterStats(viewModel.game!!.enemy)
                    Button(onClick = { viewModel.playerAttackMelee() }) {
                        Text("Melee")
                    }
                    Button(onClick = {}) {
                        Text("Magic")
                    }
                    Button(onClick = { showSpecialMenu = !showSpecialMenu }) {
                        Text("Special")
                    }

                }
                AnimatedVisibility(showSpecialMenu) {
                    Text("Special goes here")
                }
            }
        } else {
            Button(onClick = { viewModel.skipTurn() }) {
                Text("Stunned!")
            }
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
fun ClimbView(
    player: Fighter,
    onSuccess: () -> Unit,
    onFail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var successChanceBonus by remember { mutableIntStateOf(0) }
    var escapeChanceBonus by remember { mutableIntStateOf(0) }
    Column(modifier) {
        Button(onClick = {
            val roll = Random.nextInt(
                1,
                20
            ) + successChanceBonus + player.character.dexterity / 2
            if (roll > 16) {
                onSuccess()
            } else {

                successChanceBonus += Random.nextInt(2, 6)
            }
        }) {
            Text("Keep going(${((4 + successChanceBonus + player.character.dexterity / 2) / 20f * 100f).toInt()}%)")
        }
        Button(onClick = {
            if (Random.nextInt(
                    1,
                    20
                ) + escapeChanceBonus + player.character.dexterity / 2 > 10
            ) {
                onFail()
            } else {
                escapeChanceBonus += Random.nextInt(0, 3)
            }
        }) {
            Text("Turn back(${((10 + escapeChanceBonus + player.character.dexterity / 2) / 20f * 100f).toInt()}%)")
        }
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
        viewModel.game?.let { game ->
            FighterStats(
                game.player,
                Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
            ) {
                Row {
                    Text("Hunger")
                    LinearProgressIndicator(progress = { game.player.hunger / 100f })
                }
            }
            AnimatedContent(
                targetState = game.currentState,
                label = "Action"
            ) { state ->
                when (state) {
                    GameState.WALKING -> StreetView(modifier = Modifier.align(Alignment.CenterHorizontally))
                    GameState.FIGHTING -> CombatView(modifier = Modifier.align(Alignment.CenterHorizontally))
                    GameState.CLIMBING -> ClimbView(
                        game.player,
                        onSuccess = { viewModel.winSkip() },
                        onFail = { viewModel.looseSkip() },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )

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
                    game.player.inventory.forEach {
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
                    viewModel.game?.actionLog?.takeLast(
                        if (showLongerLog) {
                            50
                        } else {
                            10
                        }
                    )?.forEach { msg ->
                        Text(msg)
                    }
                }
            }
        }
    }
}