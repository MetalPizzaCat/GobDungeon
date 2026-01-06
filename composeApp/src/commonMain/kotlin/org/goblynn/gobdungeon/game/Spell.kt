package org.goblynn.gobdungeon.game

enum class Spell(
    val displayName: String,
    val cost: Int,
    val bonusDamage: Int,
    val effects: Map<Effect, Int>
) {
    POISON_STINGER(
        "Poison stinger",
        cost = 2,
        bonusDamage = 5,
        effects = mapOf(Effect.POISON to 1)
    ),
    VOLLEY_OF_STINGS(
        "Volley of stings",
        cost = 4,
        bonusDamage = 10,
        effects = mapOf(Effect.POISON to 3)
    )
}