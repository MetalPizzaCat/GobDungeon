package org.goblynn.gobdungeon.game

enum class Character(
    val characterName: String,
    val health: Int,
    val strength: Int,
    val dexterity: Int,
    val luck: Int,
    val charisma: Int,
    val defaultWeapon: Weapon,
    val chanceOfUsingSpells: Float,
    val spells: List<Spell>
) {
    PLAYER("You", 100, 5, 5, 5, 5, defaultWeapon = Weapon.FIST, 0f, emptyList()),
    BEE("Swarm of evil bees", 50, 4, 6, 3, 1, defaultWeapon = Weapon.STINGER, .2f, emptyList())
}