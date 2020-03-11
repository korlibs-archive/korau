package com.soywiz.korau.error

class SeekingNotSupported : Exception()
fun seekingNotSupported(): Nothing = throw SeekingNotSupported()
