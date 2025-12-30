package org.goblynn.gobdungeon

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform