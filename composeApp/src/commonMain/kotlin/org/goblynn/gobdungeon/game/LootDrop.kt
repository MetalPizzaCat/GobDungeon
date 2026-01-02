package org.goblynn.gobdungeon.game

/**
 * Object describing what items drop from an event
 * @property minMoney minium amount of money that player can get
 * @property maxMoney maximum amount of money player can get
 * @property items all items player will get
 */
data class LootDrop(val minMoney: Int, val maxMoney: Int, val items: List<Item>)
