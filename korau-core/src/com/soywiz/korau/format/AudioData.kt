package com.soywiz.korau.format

import com.soywiz.korau.sound.nativeSoundProvider
import com.soywiz.korio.error.invalidOp
import com.soywiz.korio.vfs.VfsFile

class AudioData(
        val rate: Int,
        val channels: Int,
        val samples: ShortArray
) {
    val seconds: Double get() = (samples.size / channels).toDouble() / rate.toDouble()

    fun convertTo(rate: Int = 44100, channels: Int = 2): AudioData {
        TODO()
    }

    fun toStream() = object : AudioStream(rate, channels) {
        var cursor = 0
        suspend override fun read(out: ShortArray, offset: Int, length: Int): Int {
            val available = samples.size - cursor
            val toread = Math.min(available, length)
            if (toread > 0) System.arraycopy(samples, cursor, out, offset, toread)
            return toread
        }
    }

    override fun toString(): String = "AudioData(rate=$rate, channels=$channels, samples=${samples.size})"
}

suspend fun AudioData.play() = nativeSoundProvider.createSound(this).play()

suspend fun VfsFile.readAudioData() = this.openUse { AudioFormats.decode(this) ?: invalidOp("Can't decode audio file ${this@readAudioData}") }