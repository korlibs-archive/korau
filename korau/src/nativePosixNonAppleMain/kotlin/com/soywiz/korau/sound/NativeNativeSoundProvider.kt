package com.soywiz.korau.sound

import com.soywiz.klock.*
import com.soywiz.korau.format.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.*
import kotlinx.cinterop.*
import kotlinx.coroutines.*
import platform.OpenAL.*
import kotlin.coroutines.*

val nativeAudioFormats = AudioFormats(WAV, NativeMp3DecoderFormat, NativeOggVorbisDecoderFormat)

class OpenALNativeSoundProvider : NativeSoundProvider() {
    val device = alcOpenDevice(null)
    //val device: CPointer<ALCdevice>? = null
    val context = device?.let { alcCreateContext(it, null).also {
        alcMakeContextCurrent(it)
        memScoped {
            alListener3f(AL_POSITION, 0f, 0f, 1.0f)
            alListener3f(AL_VELOCITY, 0f, 0f, 0f)
            val listenerOri = floatArrayOf(0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f)
            listenerOri.usePinned {
                alListenerfv(AL_ORIENTATION, it.addressOf(0))
            }
        }
    } }

    override suspend fun createSound(data: ByteArray, streaming: Boolean): NativeSound {
        return OpenALNativeSoundNoStream(CoroutineScope(coroutineContext), nativeAudioFormats.decode(data))
    }

    override suspend fun createSound(vfs: Vfs, path: String, streaming: Boolean): NativeSound {
        return super.createSound(vfs, path, streaming)
    }

    override suspend fun createSound(data: AudioData, formats: AudioFormats, streaming: Boolean): NativeSound {
        return super.createSound(data, formats, streaming)
    }
}

val openalNativeSoundProvider: OpenALNativeSoundProvider by lazy { OpenALNativeSoundProvider() }
actual val nativeSoundProvider: NativeSoundProvider get() = openalNativeSoundProvider

// https://ffainelli.github.io/openal-example/
class OpenALNativeSoundNoStream(val coroutineScope: CoroutineScope, val data: AudioData?) : NativeSound() {
    override suspend fun decode(): AudioData = data ?: AudioData.DUMMY

    override fun play(): NativeSoundChannel {
        if (openalNativeSoundProvider.device == null || openalNativeSoundProvider.context == null) return DummyNativeSoundChannel(this, data)
        val data = data ?: return DummyNativeSoundChannel(this)

        val buffer = alGenBuffer()
        alBufferData(buffer, data)

        val source = alGenSource()
        alSourcef(source, AL_PITCH, 1f)
        alSourcef(source, AL_GAIN, 1f)
        alSource3f(source, AL_POSITION, 0f, 0f, 0f)
        alSource3f(source, AL_VELOCITY, 0f, 0f, 0f)
        alSourcei(source, AL_LOOPING, AL_FALSE)

        alSourcei(source, AL_BUFFER, buffer.convert())

        alSourcePlay(source)

        var stopped = false

        val channel = object : NativeSoundChannel(this) {
            val totalSamples get() = data.totalSamples
            val currentSampleOffset get() = alGetSourcei(source, AL_SAMPLE_OFFSET)

            override var volume: Double
                get() = run { alGetSourcef(source, AL_GAIN).toDouble() }
                set(value) = run { alSourcef(source, AL_GAIN, value.toFloat()) }
            override var pitch: Double
                get() = run { alGetSourcef(source, AL_PITCH).toDouble() }
                set(value) = run { alSourcef(source, AL_PITCH, value.toFloat()) }
            override var panning: Double = 0.0
                set(value) = run {
                    field = value
                    alSource3f(source, AL_POSITION, panning.toFloat(), 0f, 0f)
                }

            override val current: TimeSpan get() = data.timeAtSample(currentSampleOffset)
            override val total: TimeSpan get() = data.totalTime
            override val playing: Boolean get() = alGetSourceState(source) == AL_PLAYING

            override fun stop() {
                if (!stopped) {
                    stopped = true
                    alDeleteSource(source)
                    alDeleteBuffer(buffer)
                }
            }
        }
        coroutineScope.launchImmediately {
            try {
                while (channel.playing) {
                    //println("${channel.current}/${channel.total}")
                    delay(1L)
                }
            } finally {
                channel.stop()
            }
        }
        return channel

    }
}

private fun alGetSourcef(source: ALuint, param: ALenum): ALfloat =
    memScoped { alloc<ALfloatVar>().also { alGetSourcef(source, param, it.ptr) }.value }

private fun alGetSourcei(source: ALuint, param: ALenum): ALint =
    memScoped { alloc<ALintVar>().also { alGetSourcei(source, param, it.ptr) }.value }

private fun alGetSourceState(source: ALuint): ALint = alGetSourcei(source, AL_SOURCE_STATE)

private fun alBufferData(buffer: ALuint, data: AudioData) {
    val samples = data.samplesInterleaved.data
    samples.usePinned { pin ->
        alBufferData(
            buffer,
            if (data.channels == 1) AL_FORMAT_MONO16 else AL_FORMAT_STEREO16,
            if (samples.isNotEmpty()) pin.addressOf(0) else null,
            samples.size * 2,
            data.rate.convert()
        )
    }
}

private fun alGenBuffer(): ALuint = memScoped { alloc<ALuintVar>().apply { alGenBuffers(1, this.ptr) }.value }
private fun alDeleteBuffer(buffer: ALuint): Unit =
    run { memScoped { alloc<ALuintVar>().apply { this.value = buffer }.apply { alDeleteBuffers(1, this.ptr) } } }

private fun alGenSource(): ALuint = memScoped { alloc<ALuintVar>().apply { alGenSources(1, this.ptr) }.value }
private fun alDeleteSource(buffer: ALuint): Unit =
    run { memScoped { alloc<ALuintVar>().apply { this.value = buffer }.apply { alDeleteSources(1, this.ptr) } } }
