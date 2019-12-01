package com.soywiz.korau.sound

import com.soywiz.korau.format.*
import com.soywiz.korau.sound.impl.*
import java.util.*

internal val nativeAudioFormats = AudioFormats().register(
    ServiceLoader.load(com.soywiz.korau.format.AudioFormat::class.java).toList()
)

actual val nativeSoundProvider: NativeSoundProvider by lazy { OpenALNativeSoundProvider() }
//actual val nativeSoundProvider: NativeSoundProvider by lazy { AwtNativeSoundProvider() }
