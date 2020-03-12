package com.soywiz.korau.sound

import com.soywiz.korau.format.*
import com.soywiz.korio.async.*
import kotlinx.cinterop.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import platform.windows.*
import kotlin.coroutines.*

actual val nativeSoundProvider: NativeSoundProvider = NativeNativeSoundProvider

object NativeNativeSoundProvider : NativeSoundProvider() {
    override val audioFormats: AudioFormats = AudioFormats(WAV, NativeMp3DecoderFormat, NativeOggVorbisDecoderFormat)

    override fun createAudioStream(coroutineContext: CoroutineContext, freq: Int): PlatformAudioOutput {
        var channel: Channel<ShortArray>? = null
        var job: Job? = null
        val nchannels = 2

        return object : PlatformAudioOutput(coroutineContext, freq) {
            override val availableSamples: Int get() = TODO()
            override var pitch: Double = 1.0
            override var volume: Double = 1.0
            override var panning: Double = 0.0

            override suspend fun add(samples: AudioSamples, offset: Int, size: Int) {
                channel?.send(samples.interleaved().data)
            }

            override fun start() {
                job?.cancel()
                channel?.close()
                channel = Channel<ShortArray>(Channel.UNLIMITED)
                job = launchImmediately(Dispatchers.Unconfined) {
                    memScoped {
                        val samplesInterleaved = AudioSamplesInterleaved(2, 4096)
                        val scope = Arena()
                        val hWaveOut = scope.alloc<HWAVEOUTVar>()
                        samplesInterleaved.data.usePinned { samplesPin ->
                            val hdr = scope.alloc<WAVEHDR>().apply {
                                this.lpData = samplesPin.addressOf(0).reinterpret()
                                this.dwBufferLength = (samplesInterleaved.data.size * 2).convert()
                                this.dwFlags = 0.convert()

                                //this.dwBytesRecorded = 0.convert()
                                //this.dwUser = 0.convert()
                                //this.dwLoops = 0.convert()
                            }
                            val format = alloc<WAVEFORMATEX>().apply {
                                this.cbSize = WAVEFORMATEX.size.convert()
                                this.wFormatTag = WAVE_FORMAT_PCM.convert()
                                this.nSamplesPerSec = freq.convert()
                                this.nChannels = nchannels.convert()
                                this.nBlockAlign = (nchannels * 2).convert()
                                this.wBitsPerSample = 16.convert()
                            }
                            val res = waveOutOpen(hWaveOut.ptr, WAVE_MAPPER, format.ptr, 0.convert(), 0.convert(), CALLBACK_NULL)
                            //println(res)
                            //println(resPrepare)
                            val deque = AudioSamplesDeque(nchannels)
                            while (true) {
                                while (deque.availableRead < 16 * 1024) {
                                    val chunk = channel!!.receiveOrNull() ?: break
                                    deque.write(AudioSamplesInterleaved(nchannels, chunk.size / 2, chunk))
                                }
                                deque.read(samplesInterleaved)
                                val resPrepare = waveOutPrepareHeader(hWaveOut.value, hdr.ptr, WAVEHDR.size.convert())
                                val resOut = waveOutWrite(hWaveOut.value, hdr.ptr, WAVEHDR.size.convert())
                            }
                            //println(resOut)
                        }
                    }
                }
            }

            override fun stop() {
                job?.cancel()
                channel?.close()
                channel = null
                job = null
            }
        }
    }
}

/*
class Win32NativeSoundNoStream(val coroutineContext: CoroutineContext, val data: AudioData?) : NativeSound() {
    override suspend fun decode(): AudioData = data ?: AudioData.DUMMY

    override fun play(params: PlaybackParameters): NativeSoundChannel {
        val data = data ?: return DummyNativeSoundChannel(this)
        val scope = Arena()
        val hWaveOut = scope.alloc<HWAVEOUTVar>()
        val samplesPin = data.samplesInterleaved.data.pin()
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

            override var current: TimeSpan
                get() = (currentSamples.toDouble() / data.rate).seconds
                set(value) = seekingNotSupported()
            override val total: TimeSpan get() = data.totalTime
            override val playing: Boolean
                get() = !stopped && super.playing

            override fun stop() {
                if (!stopped) {
                    //println("stop")
                    stopped = true
                    waveOutReset(hWaveOut.value)
                    val res = waveOutClose(hWaveOut.value)
                    waveOutUnprepareHeader(hWaveOut.value, hdr.ptr, WAVEHDR.size.convert())
                    //println(res)
                    scope.clear()
                    samplesPin.unpin()
                }
            }
        }.also {
            it.copySoundPropsFrom(params)
        }
        launchImmediately(coroutineContext[ContinuationInterceptor.Key] ?: coroutineContext) {
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
*/
