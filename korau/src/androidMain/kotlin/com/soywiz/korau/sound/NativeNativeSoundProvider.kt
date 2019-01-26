package com.soywiz.korau.sound

actual val nativeSoundProvider: NativeSoundProvider by lazy { AndroidNativeSoundProvider() }

