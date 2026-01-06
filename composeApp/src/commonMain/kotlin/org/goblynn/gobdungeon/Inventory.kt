package org.goblynn.gobdungeon

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.goblynn.gobdungeon.game.Item
import org.goblynn.gobdungeon.game.Weapon
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Inventory(
    inventory: List<Item>,
    onItemUse: (item: Item) -> Unit,
    modifier: Modifier = Modifier,
    showInventoryByDefault: Boolean = false
) {
    var showInventory by remember { mutableStateOf(showInventoryByDefault) }

    Column(modifier.fillMaxWidth()) {
        Button(
            onClick = { showInventory = !showInventory },
            Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Inventory")
        }
        AnimatedVisibility(showInventory) {
            ElevatedCard(Modifier.padding(5.dp)) {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    inventory.forEach {
                        InventoryItem(it, onItemUsed = { onItemUse(it) }) {
                            Text(
                                if (it.weapon != null) {
                                    "Equip"
                                } else {
                                    "Consume"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InventoryItem(
    item: Item,
    onItemUsed: () -> Unit,
    modifier: Modifier = Modifier,
    useButtonLabel: @Composable (RowScope.() -> Unit) = { Text("Equip") },
) {
    ElevatedCard(modifier.fillMaxWidth().padding(5.dp)) {
        Column(modifier.fillMaxWidth().padding(5.dp)) {
            Text(item.displayName, fontSize = 24.sp, fontStyle = FontStyle.Italic)
            Text(item.description)
            if (item.weapon != null) {
                WeaponDisplay(item.weapon)
            }
            Button(onClick = { onItemUsed() }) {
                useButtonLabel()
            }
        }
    }
}

@Composable
fun WeaponDisplay(weapon: Weapon, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text("Damage: ${weapon.damageBonusMin}-${weapon.damageBonusMax}")
    }
}


@Preview
@Composable
fun InventoryPreview() {
    Inventory(
        listOf(Item.SMALL_RATION, Item.BOTTLE_OF_HONEY, Item.DAGGER, Item.STING_BLADE),
        {}, showInventoryByDefault = true
    )
}