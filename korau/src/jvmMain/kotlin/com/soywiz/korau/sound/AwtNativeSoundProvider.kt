package com.soywiz.korau.sound

import com.jogamp.openal.AL
import com.jogamp.openal.ALFactory
import com.jogamp.openal.util.ALut
import com.soywiz.klock.TimeSpan
import com.soywiz.korau.format.AudioFormats
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.Vfs
import java.nio.ShortBuffer
import java.util.*
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.*
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext


private val nativeAudioFormats = AudioFormats().register(
    ServiceLoader.load(com.soywiz.korau.format.AudioFormat::class.java).toList()
)

val openalNativeSoundProvider: OpenALNativeSoundProvider by lazy { OpenALNativeSoundProvider() }
actual val nativeSoundProvider: NativeSoundProvider get() = openalNativeSoundProvider

val al by lazy {
    ALFactory.getAL().also { al ->
    //val error = al.alGetError()
    //if (error != AL.AL_NO_ERROR) error("Error initializing OpenAL ${error.shex}")
} }

/*
val alc by lazy {
    ALFactory.getALC().also { alc ->
        //val error = alc.alcGetError()
        //if (error != AL.AL_NO_ERROR) error("Error initializing OpenAL ${error.shex}")
    } }

private val device by lazy { alc.alcOpenDevice(null).also {
    println("alc.alcOpenDevice: $it")
} }
private val context by lazy { alc.alcCreateContext(device, null).also {
    println("alc.alcCreateContext: $it with device=$device")
} }
*/

fun checkAlErrors() {
//    val error = al.alGetError()
//    if (error != AL.AL_NO_ERROR) error("OpenAL error ${error.shex}")
}

class OpenALNativeSoundProvider : NativeSoundProvider() {
    init {
        //println("ALut.alutInit: ${Thread.currentThread()}")
        ALut.alutInit()
        //alc.alcMakeContextCurrent(context)
        al.alListener3f(AL.AL_POSITION, 0f, 0f, 1.0f)
        checkAlErrors()
        al.alListener3f(AL.AL_VELOCITY, 0f, 0f, 0f)
        checkAlErrors()
        al.alListenerfv(AL.AL_ORIENTATION, floatArrayOf(0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f), 0)
        checkAlErrors()
    }

    override suspend fun createSound(data: ByteArray, streaming: Boolean): NativeSound {
        return OpenALNativeSoundNoStream(coroutineContext, nativeAudioFormats.decode(data))
    }

    override suspend fun createSound(vfs: Vfs, path: String, streaming: Boolean): NativeSound {
        return super.createSound(vfs, path, streaming)
    }

    override suspend fun createSound(data: AudioData, formats: AudioFormats, streaming: Boolean): NativeSound {
        return super.createSound(data, formats, streaming)
    }
}

// https://ffainelli.github.io/openal-example/
class OpenALNativeSoundNoStream(val coroutineContext: CoroutineContext, val data: AudioData?) : NativeSound() {
    override suspend fun decode(): AudioData = data ?: AudioData.DUMMY

    override fun play(): NativeSoundChannel {
        //if (openalNativeSoundProvider.device == null || openalNativeSoundProvider.context == null) return DummyNativeSoundChannel(this, data)
        //println("OpenALNativeSoundNoStream.play : $data")
        //alc.alcMakeContextCurrent(context)
        val data = data ?: return DummyNativeSoundChannel(this)

        val buffer = alGenBuffer()
        alBufferData(buffer, data)

        val source = alGenSource()
        al.alSourcef(source, AL.AL_PITCH, 1f)
        al.alSourcef(source, AL.AL_GAIN, 1f)
        al.alSource3f(source, AL.AL_POSITION, 0f, 0f, 0f)
        al.alSource3f(source, AL.AL_VELOCITY, 0f, 0f, 0f)
        al.alSourcei(source, AL.AL_LOOPING, AL.AL_FALSE)
        al.alSourcei(source, AL.AL_BUFFER, buffer)
        checkAlErrors()

        al.alSourcePlay(source)
        checkAlErrors()

        var stopped = false

        val channel = object : NativeSoundChannel(this) {
            val totalSamples get() = data.totalSamples
            val currentSampleOffset get() = alGetSourcei(source, AL.AL_SAMPLE_OFFSET)

            override var volume: Double
                get() = run { alGetSourcef(source, AL.AL_GAIN).toDouble() }
                set(value) = run { al.alSourcef(source, AL.AL_GAIN, value.toFloat()) }
            override var pitch: Double
                get() = run { alGetSourcef(source, AL.AL_PITCH).toDouble() }
                set(value) = run { al.alSourcef(source, AL.AL_PITCH, value.toFloat()) }
            override var panning: Double = 0.0
                set(value) = run {
                    field = value
                    al.alSource3f(source, AL.AL_POSITION, panning.toFloat(), 0f, 0f)
                }

            override val current: TimeSpan get() = data.timeAtSample(currentSampleOffset)
            override val total: TimeSpan get() = data.totalTime
            override val playing: Boolean get() {
                val result = alGetSourceState(source) == AL.AL_PLAYING
                checkAlErrors()
                return result
            }

            override fun stop() {
                if (!stopped) {
                    stopped = true
                    alDeleteSource(source)
                    alDeleteBuffer(buffer)
                }
            }
        }
        launchImmediately(coroutineContext[ContinuationInterceptor.Key] ?: coroutineContext) {
            try {
                do {
                    delay(1L)
                } while (channel.playing)
            } catch (e: Throwable) {
                e.printStackTrace()
            } finally {
                channel.stop()
            }
        }
        return channel

    }
}

private val tempF = FloatArray(1)
private val tempI = IntArray(1)
private fun alGetSourcef(source: Int, param: Int): Float = tempF.apply { al.alGetSourcef(source, param, this, 0) }[0]
private fun alGetSourcei(source: Int, param: Int): Int = tempI.apply { al.alGetSourcei(source, param, this, 0) }[0]
private fun alGetSourceState(source: Int): Int = alGetSourcei(source, AL.AL_SOURCE_STATE)

private fun alBufferData(buffer: Int, data: AudioData) {
    val samples = data.samplesInterleaved.data

    val bufferData = ShortBuffer.wrap(samples)
    //val bufferData = ByteBuffer.allocateDirect(samples.size * 2).order(ByteOrder.nativeOrder())
    //bufferData.asShortBuffer().put(samples)

    al.alBufferData(
        buffer,
        if (data.channels == 1) AL.AL_FORMAT_MONO16 else AL.AL_FORMAT_STEREO16,
        if (samples.isNotEmpty()) bufferData else null,
        samples.size * 2,
        data.rate
    )
    checkAlErrors()
}

private fun alGenBuffer(): Int = tempI.apply { al.alGenBuffers(1, this, 0) }[0]
private fun alGenSource(): Int = tempI.apply { al.alGenSources(1, this, 0) }[0]
private fun alDeleteBuffer(buffer: Int): Unit = al.alDeleteBuffers(1, tempI.also { it[0] = buffer }, 0)
private fun alDeleteSource(buffer: Int): Unit = al.alDeleteSources(1, tempI.also { it[0] = buffer }, 0)

/*
class JOALNativeSoundProvider : NativeSoundProvider() {
    override val target: String = "joal"

    override fun createAudioStream(freq: Int): PlatformAudioOutput = JvmPlatformAudioOutput(freq)

    override suspend fun createSound(data: ByteArray, streaming: Boolean): NativeSound {
        val audioData = try {
            nativeSoundFormats.decode(data.openAsync()) ?: AudioData.DUMMY
        } catch (e: Throwable) {
            e.printStackTrace()
            AudioData.DUMMY
        }
        return ALNativeSound(audioData, audioData.toWav())
    }

    override suspend fun createSound(data: AudioData, formats: AudioFormats, streaming: Boolean): NativeSound {
        return ALNativeSound(data, data.toWav())
    }
}

class ALNativeSound(val audioData: AudioData, val data: ByteArray) : NativeSound() {
    override suspend fun decode(): AudioData = audioData

    override fun play(): NativeSoundChannel = object : NativeSoundChannel(this) {
        private var running = true

        private val buffer = IntArray(1).apply {
            al.alGenBuffers(1, this, 0)
        }
        private val source = IntArray(1).apply {
            al.alGenSources(1, this, 0)
        }

        override fun stop() {
            if (running) {
                running = false
                al.alDeleteBuffers(1, buffer, 0)
                al.alDeleteSources(1, source, 0)
                buffer[0] = 0
                source[0] = 0
            }
        }

        init {
            al.alBufferData(buffer[0], if (audioData.channels == 2) AL.AL_FORMAT_STEREO16 else AL.AL_FORMAT_STEREO8, ByteBuffer.wrap(data), data.size, audioData.rate)
            al.alsource
            al.alSourcePlay(source[0])
        }
    }
}
*/

/*
actual val nativeSoundProvider: NativeSoundProvider by lazy { AwtNativeSoundProvider() }
// AudioSystem.getMixerInfo()
val mixer by lazy { AudioSystem.getMixer(null) }

class AwtNativeSoundProvider : NativeSoundProvider() {
    override fun init() {
        // warming and preparing
        mixer.mixerInfo
        val af = AudioFormat(44100f, 16, 2, true, false)
        val info = DataLine.Info(SourceDataLine::class.java, af)
        val line = AudioSystem.getLine(info) as SourceDataLine
        line.open(af, 4096)
        line.start()
        line.write(ByteArray(4), 0, 4)
        line.drain()
        line.stop()
        line.close()
    }

    override fun createAudioStream(freq: Int): PlatformAudioOutput = JvmPlatformAudioOutput(freq)

    override suspend fun createSound(data: ByteArray, streaming: Boolean): NativeSound {
        val audioData = try {
            nativeSoundFormats.decode(data.openAsync()) ?: AudioData.DUMMY
        } catch (e: Throwable) {
            e.printStackTrace()
            AudioData.DUMMY
        }
        return AwtNativeSound(audioData, audioData.toWav()).init()
    }

    override suspend fun createSound(data: AudioData, formats: AudioFormats, streaming: Boolean): NativeSound {
        return AwtNativeSound(data, data.toWav())
    }
}

class AwtNativeSound(val audioData: AudioData, val data: ByteArray) : NativeSound() {
    override var length: TimeSpan = 0.milliseconds
    val format by lazy { AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioData.rate.toFloat(), 16, audioData.channels, 1024, 1024.toFloat(), false) }
    //val jsound by lazy { AudioSystem.getAudioInputStream(ByteArrayInputStream(data)) }

    suspend fun init(): AwtNativeSound {
        executeInWorkerJVM {
            val sound = AudioSystem.getAudioInputStream(ByteArrayInputStream(data))
            length = (sound.frameLength * 1000.0 / sound.format.frameRate.toDouble()).toLong().milliseconds
        }
        return this
    }

    override suspend fun decode(): AudioData = audioData

    class PooledClip {
        companion object {
            private val pool = Pool({ it.stopped = false }) { PooledClip() }
            fun play(channel: NativeSoundChannel): PooledClip {
                //val clip = pool.alloc()
                val clip = PooledClip()
                clip.play(channel)
                return clip
            }
        }

        val lineListener: LineListener = object : LineListener {
            override fun update(event: LineEvent) {
                when (event.type) {
                    LineEvent.Type.STOP, LineEvent.Type.CLOSE -> {
                        event.line.close()
                        clip.removeLineListener(this)
                        stop()
                    }
                }

            }
        }

        val clip = AudioSystem.getClip(mixer.mixerInfo).apply {
            addLineListener(lineListener)
        }
        var stopped = false
        val current: TimeSpan get() = clip.microsecondPosition.toDouble().microseconds

        private var channel: NativeSoundChannel? = null

        fun stop() {
            if (!stopped) {
                stopped = true
                channel?.stop()
                channel = null
                clip.stop()
                //clip.close()
                //pool.free(this)
            }
        }

        fun play(channel: NativeSoundChannel) {
            this.channel = channel
            val sound = channel.sound as AwtNativeSound

            if (sound.audioData.totalTime == 0.seconds) {
                stop()
            } else {
                val data = sound.data
                val time = measureTime {
                    clip.open(sound.format, data, 0, data.size)
                }
                //println("Opening clip time: $time")
                clip.start()
            }
        }
    }

    override fun play(): NativeSoundChannel {
        return object : NativeSoundChannel(this) {
            //val len = clip.microsecondLength.toDouble().microseconds
            val len = audioData.totalTime

            override val current: TimeSpan get() = clip?.current ?: 0.milliseconds
            override val total: TimeSpan get() = len
            //override val playing: Boolean get() = !stopped && current < total
            override val playing: Boolean get() = clip != null

            //override var pitch: Double = 1.0
            //    set(value) {
            //        field = value
            //        //(clip.getControl(FloatControl.Type.SAMPLE_RATE) as FloatControl).value = (audioData.rate * pitch).toFloat()
            //    }

            override fun stop() {
                clip?.stop()
                clip = null
            }

            var clip: PooledClip? = PooledClip.play(this)
        }
    }
}
*/
