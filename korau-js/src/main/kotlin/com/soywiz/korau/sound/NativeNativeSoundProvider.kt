package com.soywiz.korau.sound

import com.soywiz.korau.HtmlNativeSoundProvider

actual object NativeNativeSoundProvider {
	actual val instance: NativeSoundProvider by lazy { HtmlNativeSoundProvider() }
}
