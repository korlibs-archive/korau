package com.soywiz.korau.sound

import com.soywiz.kds.*
import com.soywiz.korio.async.*
import com.soywiz.korio.lang.*
import kotlin.coroutines.*
import kotlinx.coroutines.*

actual val nativeSoundProvider: NativeSoundProvider by lazy { DummyNativeSoundProvider() }
