package org.goblynn.gobdungeon.game

enum class Levels(
    val dungeonLevelName: String,
    val distance: Int,
    val distanceIncrease: Float,
    val chanceOfEnemy: Float,
    val endBoss: Character,
    val enemies: List<Character>
) {
    ENTRANCE("Entrance", 250, distanceIncrease = 1.5f, 0.1f, Character.BEE, listOf(Character.BEE))
}