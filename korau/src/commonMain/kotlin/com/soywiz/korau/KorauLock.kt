package com.soywiz.korau

// @TODO: Expose Lock in Korio!
class KorauLock {
    inline operator fun <T> invoke(callback: () -> T): T = callback()
}
