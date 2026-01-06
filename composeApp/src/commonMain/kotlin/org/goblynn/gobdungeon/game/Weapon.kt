package org.goblynn.gobdungeon.game

/**
 * List of all weapons present in the game
 *
 * @param displayName Name to show to player
 * @param damageBonusMin Minimum bonus damage
 * @param damageBonusMax Maximum bonus damage
 * @param attackMessageVerb What verb to use in the action log following `"p1 VERB p2 for N damage"` structure
 */
enum class Weapon(
    val displayName: String,
    val damageBonusMin: Int,
    val damageBonusMax: Int,
    val attackMessageVerb: String
) {
    /**
     * "hidden" weapon that can be set as default for characters
     */
    FIST("Fist", damageBonusMin = 0, damageBonusMax = 1, attackMessageVerb = "hit"),
    STINGER("Stinger", damageBonusMin = 0, damageBonusMax = 1, attackMessageVerb = "stung"),
    STINGER_BLADE("The string", damageBonusMin = 10, damageBonusMax = 20, attackMessageVerb = "stabbed"),
    SHORTSWORD("Shortsword", damageBonusMin = 5, damageBonusMax = 15, attackMessageVerb = "hit"),
    GREATSWORD("Greatsword", damageBonusMin = 10, damageBonusMax = 30, attackMessageVerb = "hit"),
    BATTLEAXE("Battleaxe", damageBonusMin = 7, damageBonusMax = 18, attackMessageVerb = "stabbed"),
    DAGGER("Dagger", damageBonusMin = 2, damageBonusMax = 4, attackMessageVerb = "stabbed")
}