package com.soywiz.korau.js

import com.jtransc.js.*
import com.soywiz.korau.sound.NativeSound
import com.soywiz.korau.sound.NativeSoundProvider
import com.soywiz.korio.async.suspendCancellableCoroutine
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
	//private val once = Once()

	override var lengthInMs: Long = 0L

	suspend override fun init() {
		initInternal()
		lengthInMs = (audio["duration"].toDouble() * 1000L).toLong()
	}


	suspend fun initInternal() = suspendCancellableCoroutine<Unit> { c ->
		var ok: JsDynamic? = null
		var error: JsDynamic? = null

		fun removeEventListeners() {
			audio.call("removeEventListener", "canplaythrough", ok)
			audio.call("removeEventListener", "error", error)
			audio.call("removeEventListener", "abort", error)
		}

		ok = jsFunction<Unit> {
			removeEventListeners()
			c.resume(Unit)

		}
		error = jsFunction<Unit> {
			removeEventListeners()
			c.resume(Unit)
		}

		audio.call("addEventListener", "canplaythrough", ok)
		audio.call("addEventListener", "error", error)
		audio.call("addEventListener", "abort", error)

		c.onCancel {
			audio.call("stop")
		}
	}

	suspend override fun play() = suspendCancellableCoroutine<Unit> { c ->
		var done: JsDynamic? = null

		fun removeEventListeners() {
			audio.call("removeEventListener", "ended", done)
			audio.call("removeEventListener", "pause", done)
			audio.call("removeEventListener", "stalled", done)
			audio.call("removeEventListener", "error", done)
		}

		done = jsFunction<Unit> {
			removeEventListeners()
			c.resume(Unit)
		}

		audio.call("addEventListener", "ended", done)
		audio.call("addEventListener", "pause", done)
		audio.call("addEventListener", "stalled", done)
		audio.call("addEventListener", "error", done)
		audio.call("play")

		c.onCancel {
			audio.call("stop")
		}
	}
}