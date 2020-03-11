package com.soywiz.korau.sound

import com.soywiz.klock.*
import com.soywiz.kmem.*
import com.soywiz.korau.error.*
import com.soywiz.korau.format.*
import com.soywiz.korau.internal.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.*
import com.soywiz.korio.file.std.*
import com.soywiz.korio.lang.*
import kotlin.coroutines.*

class HtmlNativeSoundProvider : NativeSoundProvider() {
	override fun initOnce() {
	}

	override fun createAudioStream(freq: Int): PlatformAudioOutput = JsPlatformAudioOutput(freq)

	override suspend fun createSound(data: ByteArray, streaming: Boolean, props: AudioDecodingProps): NativeSound {
		return AudioBufferNativeSound(HtmlSimpleSound.loadSound(data))
		/*
		return if (streaming) {
			createTemporalURLForData(data, "audio/mp3") { url ->
				MediaNativeSound(url)
			}
			//return MediaNativeSound(createURLForData(data, "audio/mp3")) // @TODO: Leak
			//return MediaNativeSound(createBase64URLForData(data, "audio/mp3"))
		} else {
			AudioBufferNativeSound(HtmlSimpleSound.loadSound(data))
		}
		*/
	}

	override suspend fun createSound(vfs: Vfs, path: String, streaming: Boolean, props: AudioDecodingProps): NativeSound = when (vfs) {
		is LocalVfs, is UrlVfs -> {
			val rpath = when (vfs) {
				is LocalVfs -> path
				is UrlVfs -> vfs.getFullUrl(path)
				else -> invalidOp
			}
			//if (streaming) {
			//	MediaNativeSound(rpath)
			//} else {
			//	AudioBufferNativeSound(HtmlSimpleSound.loadSound(rpath))
			//}
			AudioBufferNativeSound(HtmlSimpleSound.loadSound(rpath))
		}
		else -> {
			super.createSound(vfs, path)
		}
	}
}

/*
class MediaNativeSound private constructor(
	val context: CoroutineContext,
	val url: String,
	override val length: TimeSpan
) : NativeSound() {
	companion object {
		suspend operator fun invoke(url: String): NativeSound {
			//val audio = document.createElement("audio").unsafeCast<HTMLAudioElement>()
			//audio.autoplay = false
			//audio.src = url
			return MediaNativeSound(coroutineContext, url, 100.milliseconds)
			//val audio = document.createElement("audio").unsafeCast<HTMLAudioElement>()
			//audio.autoplay = false
			//audio.src = url
			//log.trace { "CREATE SOUND FROM URL: $url" }
			//
			//suspendCancellableCoroutine<Unit> { c ->
			//	var ok: ((Event) -> Unit)? = null
			//	var error: ((Event) -> Unit)? = null
			//
			//	fun removeEventListeners() {
			//		audio.removeEventListener("canplaythrough", ok)
			//		audio.removeEventListener("error", error)
			//		audio.removeEventListener("abort", error)
			//	}
			//
			//	ok = {
			//		log.trace { "OK" }
			//		removeEventListeners()
			//		c.resume(Unit)
			//
			//	}
			//	error = {
			//		log.trace { "ERROR" }
			//		removeEventListeners()
			//		c.resume(Unit)
			//	}
			//
			//	audio.addEventListener("canplaythrough", ok)
			//	audio.addEventListener("error", error)
			//	audio.addEventListener("abort", error)
			//}
			//log.trace { "DURATION_MS: ${(audio.duration * 1000).toLong()}" }
			//return MediaNativeSound(url, (audio.duration * 1000).toLong())
		}
	}

	override suspend fun decode(): AudioData {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun play(): NativeSoundChannel {
		return object : NativeSoundChannel(this) {
			val bufferPromise = asyncImmediately(context) {
				if (HtmlSimpleSound.unlocked) HtmlSimpleSound.loadSoundBuffer(url) else null
			}
			val channelPromise = asyncImmediately(context) {
				val buffer = bufferPromise.await()
				if (buffer != null) HtmlSimpleSound.playSoundBuffer(buffer) else null
			}

			override fun stop() {
				launchImmediately(context) {
					val res = bufferPromise.await()
					if (res != null) HtmlSimpleSound.stopSoundBuffer(res)
				}
			}
		}
	}
}
*/

class AudioBufferNativeSound(val buffer: AudioBuffer?) : NativeSound() {
	override val length: TimeSpan = ((buffer?.duration) ?: 0.0).seconds

	override suspend fun decode(): AudioData = if (buffer == null) {
		AudioData.DUMMY
	} else {
		val nchannels = buffer.numberOfChannels
		val nsamples = buffer.length
		val data = AudioSamples(nchannels, nsamples)
		var m = 0
		for (c in 0 until nchannels) {
			val channelF = buffer.getChannelData(c)
			for (n in 0 until nsamples) {
				data[c][m++] = SampleConvert.floatToShort(channelF[n])
			}
		}
		AudioData(buffer.sampleRate, data)
	}

	override fun play(params: PlaybackParameters): NativeSoundChannel {
		return object : NativeSoundChannel(this) {
			val channel = if (buffer != null) HtmlSimpleSound.playSound(buffer, controller) else null

			override var volume: Double
				get() = channel?.gain?.gain?.value ?: 1.0
				set(value) { channel?.gain?.gain?.value = value}
			override var pitch: Double
				get() = super.pitch
				set(value) {}
			override var panning: Double
				get() = channel?.panning ?: 0.0
				set(value) { channel?.panning = value }
			override var current: TimeSpan
                get() = channel?.currentTime?.seconds ?: 0.seconds
                set(value) = seekingNotSupported()
			override val total: TimeSpan = buffer?.duration?.seconds ?: 0.seconds
			override val playing: Boolean get() = current < total

			override fun stop(): Unit = run { channel?.stop() }
		}
	}
}

private suspend fun soundProgress(
	totalTime: Double,
	timeProvider: () -> Double,
	progress: (Double, Double) -> Unit,
	startTime: Double = timeProvider()
) {
	while (true) {
		val now = timeProvider()
		val elapsed = now - startTime
		if (elapsed >= totalTime) break
		progress(elapsed, totalTime)
		delay(4.milliseconds)
	}
	progress(totalTime, totalTime)
}
