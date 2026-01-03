package org.goblynn.gobdungeon

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.goblynn.gobdungeon.game.Levels
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    viewModel: GameViewModel = viewModel { GameViewModel() }
) {
    MaterialTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            AnimatedVisibility(viewModel.game == null && viewModel.endGameStats == null) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Levels.entries.forEach { level ->
                        Button(onClick = { viewModel.enterLevel(level) }) {
                            Text(level.dungeonLevelName)
                        }
                    }
                }
            }
            AnimatedVisibility(viewModel.endGameStats != null) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text("You lost!")
                    Button(onClick = { viewModel.acceptEndGameMenu() }) {
                        Text("Return to level select")
                    }

                }
            }
            AnimatedVisibility(viewModel.game != null) {
                Game(modifier = Modifier.fillMaxSize())
            }

        }
    }
}

