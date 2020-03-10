package com.soywiz.korau.format.mp3

import com.soywiz.kds.*
import com.soywiz.klock.*
import com.soywiz.korau.format.*
import com.soywiz.korau.sound.*
import com.soywiz.korio.stream.*

actual suspend fun MP3DecodeStream(data: AsyncStream): AudioStream? {
    return createJavaMp3DecoderStream(data.readAll())
}
