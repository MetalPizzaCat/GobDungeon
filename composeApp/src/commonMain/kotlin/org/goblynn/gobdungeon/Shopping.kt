package org.goblynn.gobdungeon

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.goblynn.gobdungeon.game.Item
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ShopMenu(
    shopItems: Map<Item, Int>,
    onItemPurchased: (item: Item, price: Int) -> Unit,
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Column(modifier.verticalScroll(rememberScrollState())) {
            shopItems.forEach { (item, price) ->
                InventoryItem(item, onItemUsed = { onItemPurchased(item, price) }) {
                    Text("Buy for $price gold")
                }
            }
        }
        Button(onClick = { onFinished() }) {
            Text("Exit")
        }
    }
}

@Preview
@Composable
fun ShopPreview() {
    ShopMenu(mapOf(Item.SMALL_RATION to 20), { i, p -> }, {})
}