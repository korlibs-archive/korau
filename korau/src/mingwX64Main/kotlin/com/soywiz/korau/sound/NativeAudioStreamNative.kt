package com.soywiz.korau.sound

import com.soywiz.klock.*
import com.soywiz.kmem.*
import com.soywiz.korau.format.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.*
import kotlinx.cinterop.*
import kotlin.coroutines.*
import kotlinx.coroutines.*
import platform.windows.*

val nativeAudioFormats = AudioFormats().register(
    WAV, NativeMp3DecoderFormat, NativeOggVorbisDecoderFormat
)

actual val nativeSoundProvider: NativeSoundProvider = object : NativeSoundProvider() {
    override fun initOnce() {
        super.initOnce()
    }

    override fun createAudioStream(freq: Int): PlatformAudioOutput {
        return super.createAudioStream(freq)
    }

    override fun init() {
        super.init()
    }

    override suspend fun createSound(data: ByteArray, streaming: Boolean): NativeSound {
        return Win32NativeSoundNoStream(CoroutineScope(coroutineContext), nativeAudioFormats.decode(data))
    }

    override suspend fun createSound(vfs: Vfs, path: String, streaming: Boolean): NativeSound {
        return super.createSound(vfs, path, streaming)
    }

    override suspend fun createSound(data: AudioData, formats: AudioFormats, streaming: Boolean): NativeSound {
        return super.createSound(data, formats, streaming)
    }
}

class Win32NativeSoundNoStream(val coroutineScope: CoroutineScope, val data: AudioData?) : NativeSound() {
    override suspend fun decode(): AudioData = data ?: AudioData.DUMMY

    override fun play(): NativeSoundChannel {
        val data = data ?: return DummyNativeSoundChannel(this)
        val scope = Arena()
        val hWaveOut = scope.alloc<HWAVEOUTVar>()
        val samplesPin = data.samples.pin()
        val hdr = scope.alloc<WAVEHDR>().apply {
            this.lpData = samplesPin.addressOf(0).reinterpret()
            this.dwBufferLength = (data.samples.size * 2).convert()
            this.dwFlags = 0.convert()

            //this.dwBytesRecorded = 0.convert()
            //this.dwUser = 0.convert()
            //this.dwLoops = 0.convert()
            //this.lpNext = 0.convert()
        }
        memScoped {
            val format = alloc<WAVEFORMATEX>().apply {
                this.cbSize = WAVEFORMATEX.size.convert()
                this.wFormatTag = WAVE_FORMAT_PCM.convert()
                this.nSamplesPerSec =  data.rate.convert()
                this.nChannels = data.channels.convert()
                this.nBlockAlign = (data.channels * 2).convert()
                this.wBitsPerSample = 16.convert()
            }
            val res = waveOutOpen(hWaveOut.ptr, WAVE_MAPPER, format.ptr, 0.convert(), 0.convert(), CALLBACK_NULL)
            //println(res)
            val resPrepare = waveOutPrepareHeader(hWaveOut.value, hdr.ptr, WAVEHDR.size.convert())
            //println(resPrepare)
            val resOut = waveOutWrite(hWaveOut.value, hdr.ptr, WAVEHDR.size.convert())
            //println(resOut)
        }
        var stopped = false
        val channel = object : NativeSoundChannel(this) {
            override var pitch: Double = 1.0
                set(value) {
                    field = value
                    val intPart = value.toInt()
                    val divPart = field % 1.0
                    waveOutSetPitch(hWaveOut.value, ((intPart shl 16) or (divPart * 0xFFFF).toInt()).convert())
                }
            override var volume: Double = 1.0
                set(value) {
                    field = value
                    waveOutSetVolume(hWaveOut.value, (value.clamp(0.0, 1.0) * 0xFFFF).toInt().convert())
                }

            val currentSamples: Int
                get() = memScoped {
                    val time = alloc<mmtime_tag>().apply {
                        wType = TIME_SAMPLES.convert()
                    }
                    waveOutGetPosition(hWaveOut.value, time.ptr, MMTIME.size.convert())
                    time.u.sample.toInt()
                }

            override val current: TimeSpan get() = (currentSamples.toDouble() / data.rate).seconds
            override val total: TimeSpan get() = data.totalTime
            override val playing: Boolean
                get() = !stopped && super.playing

            override fun stop() {
                if (!stopped) {
                    //println("stop")
                    stopped = true
                    val res = waveOutClose(hWaveOut.value)
                    //println(res)
                    scope.clear()
                    samplesPin.unpin()
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