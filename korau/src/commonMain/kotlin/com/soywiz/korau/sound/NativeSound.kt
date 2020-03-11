package com.soywiz.korau.sound

import com.soywiz.klock.*
import com.soywiz.korau.format.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.*
import com.soywiz.korio.util.*
import kotlinx.coroutines.*
import kotlin.coroutines.*

expect val nativeSoundProvider: NativeSoundProvider

open class NativeSoundProvider {
	open val target: String = "unknown"

	private var initialized = false

	open fun initOnce() {
		if (!initialized) {
			initialized = true
			init()
		}
	}

	open fun createAudioStream(freq: Int = 44100): PlatformAudioOutput = PlatformAudioOutput(freq)

	protected open fun init(): Unit = Unit

	open suspend fun createSound(data: ByteArray, streaming: Boolean = false): NativeSound = object : NativeSound() {
		override suspend fun decode(): AudioData = AudioData.DUMMY
		override fun play(controller: PlaybackController): NativeSoundChannel = object : NativeSoundChannel(this) {
			override fun stop() = Unit
		}
	}

    open val audioFormats = AudioFormats(WAV)

    open suspend fun createSound(vfs: Vfs, path: String, streaming: Boolean = false): NativeSound {
        return if (streaming) {
            val stream = vfs.file(path).open()
            createStreamingSound(audioFormats.decodeStream(stream) ?: error("Can't open sound for streaming")) {
                stream.close()
            }
        } else {
            createSound(vfs.file(path).read(), streaming)
        }
    }

	suspend fun createSound(file: FinalVfsFile, streaming: Boolean = false): NativeSound =
		createSound(file.vfs, file.path, streaming)

	suspend fun createSound(file: VfsFile, streaming: Boolean = false): NativeSound =
		createSound(file.getUnderlyingUnscapedFile(), streaming)

	open suspend fun createSound(
		data: AudioData,
		formats: AudioFormats = defaultAudioFormats,
		streaming: Boolean = false
	): NativeSound {
		return createSound(WAV.encodeToByteArray(data), streaming)
	}

    open suspend fun createStreamingSound(stream: AudioStream, bufferSeconds: Double = 0.1, closeStream: Boolean = false, onComplete: suspend () -> Unit = {}): NativeSound {
        println("STREAM.RATE:" + stream.rate)
        println("STREAM.CHANNELS:" + stream.channels)
        val coroutineContext = coroutineContext
        return object : NativeSound() {
            val nativeSound = this
            override val length: TimeSpan get() = stream.totalLength
            override suspend fun decode(): AudioData = stream.toData()
            override fun play(controller: PlaybackController): NativeSoundChannel {
                val nas = createAudioStream(stream.rate)
                var playing = true
                val job = launchImmediately(coroutineContext) {
                    playing = true
                    //println("STREAM.START")
                    try {
                        val temp = AudioSamples(stream.channels, 1024)
                        val nchannels = 2
                        val minBuf = (stream.rate * nchannels * bufferSeconds).toInt()
                        nas.start()
                        while (controller.mustPlay()) {
                            while (!stream.finished) {
                                //println("STREAM")
                                val read = stream.read(temp, 0, temp.totalSamples)
                                nas.add(temp, 0, read)
                                while (nas.availableSamples in minBuf..minBuf * 2) {
                                    delay(4.milliseconds) // 100ms of buffering, and 1s as much
                                    //println("STREAM.WAIT: ${nas.availableSamples}")
                                }
                            }
                            stream.currentPositionInSamples = 0L
                        }
                    } catch (e: CancellationException) {
                        nas.stop()
                    } finally {
                        //println("STREAM.STOP")
                        if (closeStream) {
                            stream.close()
                        }
                        playing = false
                        onComplete()
                    }
                }
                fun close() {
                    job.cancel()
                }
                return object : NativeSoundChannel(nativeSound) {
                    override var volume: Double by nas::volume.redirected()
                    override var pitch: Double by nas::pitch.redirected()
                    override var panning: Double by nas::panning.redirected()
                    override val current: TimeSpan get() = super.current
                    override val total: TimeSpan get() = stream.totalLength
                    override val playing: Boolean get() = playing
                    override fun stop() = close()
                }
            }
        }
    }

    suspend fun playAndWait(stream: AudioStream, bufferSeconds: Double = 0.1) {
        createStreamingSound(stream, bufferSeconds).playAndWait()
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

interface SoundProps {
    var volume: Double
    var pitch: Double
    var panning: Double
}

abstract class NativeSoundChannel(val sound: NativeSound) : SoundProps {
	private val startTime = DateTime.now()
	override var volume = 1.0
	override var pitch = 1.0
	override var panning = 0.0 // -1.0 left, +1.0 right
	open val current: TimeSpan get() = DateTime.now() - startTime
	open val total: TimeSpan get() = sound.length
	open val playing get() = current < total
    open fun reset(): Unit = TODO()
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

abstract class NativeSound : SoundProps {
    override var volume: Double = 1.0
    override var panning: Double = 0.0
    override var pitch: Double = 1.0
	open val length: TimeSpan = 0.seconds
	abstract suspend fun decode(): AudioData
	open fun play(controller: PlaybackController): NativeSoundChannel = TODO()
    fun play(times: PlaybackTimes): NativeSoundChannel = play(times.controller())
    fun playForever(): NativeSoundChannel = play(infinitePlaybackTimes)
    open fun play(): NativeSoundChannel = play(1.playbackTimes)
}

interface PlaybackController {
    fun mustPlay(): Boolean
}

val infinitePlaybackTimes get() = PlaybackTimes.INFINITE
inline val Int.playbackTimes get() = PlaybackTimes(this)

inline class PlaybackTimes(val count: Int) {
    companion object {
        val ZERO = PlaybackTimes(0)
        val ONE = PlaybackTimes(1)
        val INFINITE = PlaybackTimes(-1)
    }
    val hasMore get() = this != ZERO
    val oneLess get() = if (this == INFINITE) INFINITE else PlaybackTimes(count - 1)
    fun controller(): PlaybackController {
        var count = this
        return object : PlaybackController {
            override fun mustPlay(): Boolean = count.hasMore.also {
                count = count.oneLess
            }
        }
    }
}

suspend fun NativeSound.toData(): AudioData = decode()
suspend fun NativeSound.toStream(): AudioStream = decode().toStream()

suspend fun NativeSound.playAndWait(controller: PlaybackController, progress: NativeSoundChannel.(current: TimeSpan, total: TimeSpan) -> Unit = { current, total -> }): Unit =
    play(controller).await(progress)

suspend fun NativeSound.playAndWait(times: PlaybackTimes, progress: NativeSoundChannel.(current: TimeSpan, total: TimeSpan) -> Unit = { current, total -> }): Unit =
    play(times).await(progress)

suspend fun NativeSound.playAndWait(progress: NativeSoundChannel.(current: TimeSpan, total: TimeSpan) -> Unit = { current, total -> }): Unit =
	play().await(progress)

suspend fun VfsFile.readNativeMusic() = readNativeSound(streaming = true)
suspend fun VfsFile.readNativeSound(streaming: Boolean = false) = nativeSoundProvider.createSound(this, streaming)

suspend fun ByteArray.readNativeSound(streaming: Boolean = false) = nativeSoundProvider.createSound(this, streaming)
suspend fun ByteArray.readNativeMusic() = readNativeSound(streaming = true)

@Deprecated("", ReplaceWith("readNativeSound(streaming)"))
suspend fun VfsFile.readNativeSoundOptimized(streaming: Boolean = false) = readNativeSound(streaming)

