package com.soywiz.korau.sound

import com.soywiz.klock.*
import com.soywiz.korau.format.*
import com.soywiz.korio.file.*
import kotlinx.cinterop.*
import platform.AppKit.*
import platform.Foundation.*

actual val nativeSoundProvider: NativeSoundProvider = object : NativeSoundProvider() {
    override fun createAudioStream(freq: Int): NativeAudioStream {
        return super.createAudioStream(freq)
    }

    override suspend fun createSound(data: ByteArray, streaming: Boolean): NativeSound {
        return NSSoundNativeSound(NSSound(data.toNSData()))
    }

    override suspend fun play(stream: BaseAudioStream, bufferSeconds: Double) {
        super.play(stream, bufferSeconds)
    }
}

class NSSoundNativeSound(val sound: NSSound) : NativeSound() {
    override fun play(): NativeSoundChannel {
        val ssound = sound.copy() as NSSound
        ssound.play()
        return object : NativeSoundChannel(this) {
            override var volume: Double
                get() = ssound.volume.toDouble()
                set(value) { ssound.volume = value.toFloat() }
            override val current: TimeSpan get() = ssound.currentTime.seconds

            override val total: TimeSpan get() = ssound.duration.seconds
            override val playing: Boolean get() = ssound.playing

            override fun stop() {
                ssound.stop()
            }
        }
    }
}

private fun ByteArray.toNSData(): NSData {
    val data = this
    return data.usePinned { dataPin ->
        NSData.dataWithBytes(dataPin.addressOf(0), data.size.convert())
    }
}
