package com.soywiz.korau.format.ogg.jcraft

class Lock {
    inline operator fun <T> invoke(callback: () -> T): T = callback()
}