package org.goblynn.gobdungeon.game

enum class Levels(
    val dungeonLevelName: String,
    val distance: Int,
    val distanceIncrease: Float,
    val chanceOfEnemy: Float,
    val chanceOfSkip: Float,
    val chanceOfShop: Float,
    val endBosses: List<Character>,
    val enemies: List<Character>,
    val findableItems: List<Item>,
    val shopOptions: List<ShopItem>,
    val startingItems: List<Item>
) {
    ENTRANCE(
        dungeonLevelName = "Entrance",
        distance = 250,
        distanceIncrease = 1.5f,
        chanceOfEnemy = 0.05f,
        chanceOfSkip = 0.1f,
        chanceOfShop = 0.02f,
        endBosses = listOf(Character.SUPER_BEE),
        enemies = listOf(Character.BEE),
        findableItems = listOf(Item.SMALL_RATION, Item.DAGGER),
        shopOptions = listOf(
            ShopItem(Item.GREATER_HEALTH_POTION, 30, 100),
            ShopItem(Item.BATTLEAXE, 200, 400),
            ShopItem(Item.HEALTH_POTION, 20, 50),
            ShopItem(Item.SMALL_RATION, 10, 50),
            ShopItem(Item.BIG_RATION, 30, 80),
        ),
        startingItems = listOf(Item.SMALL_RATION, Item.SMALL_RATION, Item.SMALL_RATION)
    )
}