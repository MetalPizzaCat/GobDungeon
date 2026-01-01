package org.goblynn.gobdungeon.game

enum class Levels(
    val dungeonLevelName: String,
    val distance: Int,
    val distanceIncrease: Float,
    val chanceOfEnemy: Float,
    val chanceOfSkip: Float,
    val endBosses: List<Character>,
    val enemies: List<Character>
) {
    ENTRANCE(
        dungeonLevelName = "Entrance",
        distance = 250,
        distanceIncrease = 1.5f,
        chanceOfEnemy = 0.1f,
        chanceOfSkip = 0.1f,
        endBosses = listOf(Character.SUPER_BEE),
        enemies = listOf(Character.BEE)
    )
}