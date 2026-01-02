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
    STING_BLADE(
        displayName = "The string",
        description = "Strange sharp and pointy blade",
        healthRestore = 0,
        hungerRestore = 0,
        weapon = Weapon.STINGER_BLADE,
        effect = emptyMap()
    )
}