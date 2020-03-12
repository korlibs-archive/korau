package com.soywiz.korau.format.mp3

import com.soywiz.korau.sound.*
import com.soywiz.korio.stream.*

actual suspend fun MP3DecodeStream(data: AsyncStream): AudioStream? = NativeMp3DecoderAudioFormat.decodeStream(data)
