package com.soywiz.korau.sound

import com.soywiz.korau.format.*
import com.soywiz.korau.sound.impl.jna.*
import com.soywiz.korau.sound.impl.jogamp.*
import java.util.*

internal val nativeAudioFormats = AudioFormats().register(
    ServiceLoader.load(com.soywiz.korau.format.AudioFormat::class.java).toList()
)

actual val nativeSoundProvider: NativeSoundProvider by lazy { JnaOpenALNativeSoundProvider() }
//actual val nativeSoundProvider: NativeSoundProvider by lazy { JogampNativeSoundProvider() }
//actual val nativeSoundProvider: NativeSoundProvider by lazy { AwtNativeSoundProvider() }
