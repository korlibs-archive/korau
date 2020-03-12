package com.soywiz.korau.sound

import com.soywiz.klock.*
import com.soywiz.kmem.*
import com.soywiz.korau.format.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.*
import kotlinx.cinterop.*
import kotlinx.coroutines.*
import platform.AVFoundation.*
import platform.Foundation.*
import kotlin.coroutines.*

expect fun appleInitAudio()

val nativeAudioFormats = AudioFormats(WAV, NativeMp3DecoderFormat, NativeOggVorbisDecoderFormat)

class AvFoundationNativeSoundProvider : NativeSoundProvider() {
    init {
        appleInitAudio()
    }

    override suspend fun createSound(data: ByteArray, streaming: Boolean): NativeSound {
        return AVFoundationNativeSoundNoStream(CoroutineScope(coroutineContext), nativeAudioFormats.decode(data))
    }

    override suspend fun createAudioStream(freq: Int): PlatformAudioOutput {
        return super.createAudioStream(freq)
    }
}

val avFoundationNativeSoundProvider: AvFoundationNativeSoundProvider by lazy {
    AvFoundationNativeSoundProvider()
}
actual val nativeSoundProvider: NativeSoundProvider get() = avFoundationNativeSoundProvider

private fun ByteArray.toNSData(): NSData {
    val array = this
    return memScoped {
        array.usePinned { arrayPin ->
            NSData.dataWithBytes(arrayPin.startAddressOf, array.size.convert())
        }
    }
}

// https://ffainelli.github.io/openal-example/
class AVFoundationNativeSoundNoStream(val coroutineScope: CoroutineScope, val data: AudioData?) : NativeSound() {
    override suspend fun decode(): AudioData = data ?: AudioData.DUMMY

    override fun play(): NativeSoundChannel {
        val data = data ?: return DummyNativeSoundChannel(this)

        val player = memScoped {
            val error = alloc<ObjCObjectVar<NSError?>>().ptr
            AVAudioPlayer(data.toWav().toNSData(), error)
        }
        player.play()

        var stopped = false

        val channel = object : NativeSoundChannel(this) {
            override var volume: Double
                get() = run { player.volume.toDouble() }
                set(value) = run { player.setVolume(value.toFloat()) }
            override var pitch: Double
                get() = 1.0
                set(value) = run { }
            override var panning: Double
                get() = player.pan.toDouble()
                set(value) = run {
                    player.pan = value.toFloat()
                }

            override val current: TimeSpan get() = player.currentTime.seconds
            override val total: TimeSpan get() = data.totalTime
            override val playing: Boolean get() = player.playing

            override fun stop() {
                if (!stopped) {
                    stopped = true
                    player.stop()
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
