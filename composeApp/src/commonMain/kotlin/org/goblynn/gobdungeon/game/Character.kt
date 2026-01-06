package org.goblynn.gobdungeon.game

enum class Character(
    val characterName: String,
    val health: Int,
    val arcana: Int,
    val strength: Int,
    val dexterity: Int,
    val luck: Int,
    val charisma: Int,
    val defaultWeapon: Weapon,
    val chanceOfUsingSpells: Float,
    val spells: List<Spell>,
    val chanceToDropLoot: Float,
    val lootTable: List<LootDrop>
) {
    PLAYER(
        characterName = "You",
        health = 100,
        arcana = 10,
        strength = 5,
        dexterity = 5,
        luck = 5,
        charisma = 5,
        defaultWeapon = Weapon.FIST,
        chanceOfUsingSpells = 0f,
        spells = emptyList(),
        chanceToDropLoot = 0f,
        lootTable = emptyList()
    ),
    BEE(
        characterName = "Swarm of evil bees",
        health = 50,
        arcana = 4,
        strength = 3,
        dexterity = 6,
        luck = 3,
        charisma = 1,
        defaultWeapon = Weapon.STINGER,
        chanceOfUsingSpells = 1.2f,
        spells = listOf(
            Spell.POISON_STINGER
        ),
        chanceToDropLoot = 0.3f,
        lootTable = listOf(LootDrop(0, 10, listOf(Item.BOTTLE_OF_HONEY)))
    ),
    SUPER_BEE(
        characterName = "Swarm of magical super bees",
        health = 150,
        arcana = 10,
        strength = 4,
        dexterity = 7,
        luck = 3,
        charisma = 1,
        defaultWeapon = Weapon.STINGER,
        chanceOfUsingSpells = .2f,
        spells = listOf(
            Spell.POISON_STINGER,
            Spell.VOLLEY_OF_STINGS
        ),
        chanceToDropLoot = 1f,
        lootTable = listOf(LootDrop(50, 150, listOf(Item.BOTTLE_OF_HONEY, Item.STING_BLADE)))
    )
}