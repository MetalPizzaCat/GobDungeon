package org.goblynn.gobdungeon.game

/**
 * List of all items that are present in the game, depending on which values are set in the item
 * different actions will be taken. For example setting weapon to anything but null will allow
 * this weapon to be equipped
 *
 * @property displayName name to display to user
 * @property healthRestore how much health to restore when consuming the item
 * @property hungerRestore how much hunger to restore when consuming the item
 * @property weapon
 */
enum class Item(
    val displayName: String,
    val healthRestore: Int,
    val hungerRestore: Int,
    val weapon: Weapon?
) {
    SMALL_RATION("Small ration", healthRestore = 5, hungerRestore = 30, weapon = null),
    DAGGER("Dagger", healthRestore = 0, hungerRestore = 0, weapon = Weapon.DAGGER)
}