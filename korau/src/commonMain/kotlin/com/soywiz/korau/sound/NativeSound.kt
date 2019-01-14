package com.soywiz.korau.sound

import com.soywiz.klock.*
import com.soywiz.korau.format.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.*
import kotlinx.coroutines.*
import kotlin.coroutines.*

expect val nativeSoundProvider: NativeSoundProvider

open class NativeSoundProvider {
	private var initialized = false

	open fun initOnce() {
		if (!initialized) {
			initialized = true
			init()
		}
	}

	open fun createAudioStream(freq: Int = 44100): NativeAudioStream = NativeAudioStream(freq)

	protected open fun init(): Unit = Unit

	open suspend fun createSound(data: ByteArray, streaming: Boolean = false): NativeSound = object : NativeSound() {
		override suspend fun decode(): AudioData {
			return AudioData(44100, 2, shortArrayOf())
		}

		override fun play(): NativeSoundChannel = object : NativeSoundChannel(this) {
			override fun stop() {
			}
		}
	}

	open suspend fun createSound(vfs: Vfs, path: String, streaming: Boolean = false): NativeSound =
		createSound(vfs.file(path).read(), streaming)

	suspend fun createSound(file: FinalVfsFile, streaming: Boolean = false): NativeSound =
		createSound(file.vfs, file.path, streaming)

	suspend fun createSound(file: VfsFile, streaming: Boolean = false): NativeSound =
		createSound(file.getUnderlyingUnscapedFile(), streaming)

	open suspend fun createSound(
		data: com.soywiz.korau.format.AudioData,
		formats: AudioFormats = defaultAudioFormats,
		streaming: Boolean = false
	): NativeSound {
		return createSound(WAV.encodeToByteArray(data), streaming)
	}

	suspend fun playAndWait(stream: BaseAudioStream, bufferSeconds: Double = 0.1): Unit {
		val nas = nativeSoundProvider.createAudioStream()
		try {
			val temp = ShortArray(1024)
			val nchannels = 2
			val minBuf = (stream.rate * nchannels * bufferSeconds).toInt()
			nas.start()
			while (!stream.finished) {
				val read = stream.read(temp, 0, temp.size)
				nas.addSamples(temp, 0, read)
				while (nas.availableSamples in minBuf..minBuf * 2) delay(4.milliseconds) // 100ms of buffering, and 1s as much
			}
		} catch (e: CancellationException) {
			nas.stop()
		}
	}
}

class DummyNativeSoundProvider : NativeSoundProvider()

class DummyNativeSoundChannel(sound: NativeSound, val data: AudioData? = null) : NativeSoundChannel(sound) {
	private var timeStart = DateTime.now()
	override val current: TimeSpan get() = DateTime.now() - timeStart
	override val total: TimeSpan get() = data?.totalTime ?: 0.seconds

	override fun stop() {
		timeStart = DateTime.now() + total
	}
}

abstract class NativeSoundChannel(val sound: NativeSound) {
	private val startTime = DateTime.now()
	open var volume = 1.0
	open var pitch = 1.0
	open val current: TimeSpan get() = DateTime.now() - startTime
	open val total: TimeSpan get() = sound.length
	open val playing get() = current < total
	abstract fun stop(): Unit
}

suspend fun NativeSoundChannel.await(progress: NativeSoundChannel.(current: TimeSpan, total: TimeSpan) -> Unit = { current, total -> }) {
	try {
		while (playing) {
			progress(current, total)
			delay(4.milliseconds)
		}
		progress(total, total)
	} catch (e: CancellationException) {
		stop()
	}
}

abstract class NativeSound {
	open val length: TimeSpan = 0.seconds
	abstract suspend fun decode(): AudioData
	abstract fun play(): NativeSoundChannel
}

suspend fun NativeSound.playAndWait(progress: NativeSoundChannel.(current: TimeSpan, total: TimeSpan) -> Unit = { current, total -> }): Unit =
	play().await(progress)

suspend fun VfsFile.readNativeSound(streaming: Boolean = false) = nativeSoundProvider.createSound(this, streaming)
suspend fun VfsFile.readNativeSoundOptimized(streaming: Boolean = false) =
	nativeSoundProvider.createSound(this, streaming)
