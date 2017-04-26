package com.soywiz.korau.js

import com.jtransc.js.*
import com.soywiz.korau.sound.NativeSound
import com.soywiz.korau.sound.NativeSoundProvider
import com.soywiz.korio.coroutine.korioSuspendCoroutine
import com.soywiz.korio.inject.AsyncDependency
import com.soywiz.korio.util.Once

object HtmlNativeSoundProviderImpl {
	suspend fun createSound(data: ByteArray): NativeSound {
		// @TODO: This would produce leaks since no revokeObjectURL is called.
		val blob = jsNew("Blob", jsArray(data), jsObject("type" to "audio/mp3"))
		val blobURL = global["URL"].call("createObjectURL", blob)


		//return createFromUrl(blobURL.toJavaString())

		try {
			return createFromUrl(blobURL.toJavaString())
		} finally {
			global["URL"].call("revokeObjectURL", blobURL)
		}
	}

	suspend fun createFromUrl(url: String) = HtmlNativeSound(url).apply { init() }
}

class HtmlNativeSoundProvider : NativeSoundProvider() {
	override suspend fun createSound(data: ByteArray): NativeSound = HtmlNativeSoundProviderImpl.createSound(data)
}

class HtmlNativeSound(val url: String) : NativeSound(), AsyncDependency {
	val audio = jsNew("Audio", url)
	val loadOnce = Once()

	suspend override fun init() = korioSuspendCoroutine<Unit> { c ->
		val resumeCallback = jsFunction<Unit> {
			loadOnce {
				c.resume(Unit)
			}
		}

		audio.call("addEventListener", "canplaythrough", resumeCallback)
		window.call("setTimeout", resumeCallback, 1000)
	}

	suspend override fun play() {
		audio.call("play")
	}
}