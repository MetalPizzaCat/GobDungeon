package org.goblynn.gobdungeon.game

/**
 * List of all items that are present in the game, depending on which values are set in the item
 * different actions will be taken. For example setting weapon to anything but null will allow
 * this weapon to be equipped
 *
 * @property displayName name to display to user
 * @property description info to display to player
 * @property healthRestore how much health to restore when consuming the item
 * @property hungerRestore how much hunger to restore when consuming the item
 * @property weapon
 * @property effect effects to apply upon item consumption
 */
enum class Item(
    val displayName: String,
    val description: String,
    val healthRestore: Int,
    val hungerRestore: Int,
    val weapon: Weapon?,
    val effect: Map<Effect, Int>
) {
    SMALL_RATION(
        displayName = "Small ration",
        description = "Basic ration to restore some hunger",
        healthRestore = 5,
        hungerRestore = 30,
        weapon = null,
        effect = emptyMap()
    ),
    BIG_RATION(
        displayName = "Big ration",
        description = "Basic ration to restore some hunger",
        healthRestore = 10,
        hungerRestore = 80,
        weapon = null,
        effect = emptyMap()
    ),
    HEALTH_POTION(
        displayName = "Health potion",
        description = "Cures some minor wounds",
        healthRestore = 25,
        hungerRestore = 0,
        weapon = null,
        effect = emptyMap()
    ),
    GREATER_HEALTH_POTION(
        displayName = "Greater Health potion",
        description = "Basic ration to restore some hunger",
        healthRestore = 60,
        hungerRestore = 0,
        weapon = null,
        effect = emptyMap()
    ),
    MEGA_HEALTH_POTION(
        displayName = "Super Health potion",
        description = "Fully heals you",
        healthRestore = 100,
        hungerRestore = 0,
        weapon = null,
        effect = emptyMap()
    ),
    BOTTLE_OF_HONEY(
        displayName = "Bottle of honey",
        description = "Helps with hunger and illnesses",
        healthRestore = 10,
        hungerRestore = 50,
        weapon = null,
        effect = emptyMap()
    ),
    DAGGER(
        displayName = "Dagger",
        description = "Small but sharp dagger",
        healthRestore = 0,
        hungerRestore = 0,
        weapon = Weapon.DAGGER,
        effect = emptyMap()
    ),
    SHORTSWORD(
        displayName = "Shortsword",
        description = "A decent blade",
        healthRestore = 0,
        hungerRestore = 0,
        weapon = Weapon.SHORTSWORD,
        effect = emptyMap()
    ),
    GREATSWORD(
        displayName = "Greatsword",
        description = "A big sword for killing things",
        healthRestore = 0,
        hungerRestore = 0,
        weapon = Weapon.GREATSWORD,
        effect = emptyMap()
    ),
    BATTLEAXE(
        displayName = "Battleaxe",
        description = "A big sword for killing things",
        healthRestore = 0,
        hungerRestore = 0,
        weapon = Weapon.BATTLEAXE,
        effect = emptyMap()
    ),
    STING_BLADE(
        displayName = "The string",
        description = "Strange sharp and pointy blade",
        healthRestore = 0,
        hungerRestore = 0,
        weapon = Weapon.STINGER_BLADE,
        effect = emptyMap()
    )
}