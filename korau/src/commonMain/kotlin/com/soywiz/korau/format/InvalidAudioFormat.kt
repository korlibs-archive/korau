package com.soywiz.korau.format

open class InvalidAudioFormat(message: String) : RuntimeException(message)

fun invalidAudioFormat(message: String = "invalid audio format"): Nothing = throw InvalidAudioFormat(message)
