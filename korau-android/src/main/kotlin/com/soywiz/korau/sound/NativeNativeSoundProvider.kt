package com.soywiz.korau.sound

import com.soywiz.korau.AndroidNativeSoundProvider

actual object NativeNativeSoundProvider {
	actual val instance: NativeSoundProvider by lazy { AndroidNativeSoundProvider() }
}
