package com.soywiz.korau.format

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

    override fun toString(): String = "AudioData(rate=$rate, channels=$channels, samples=${samples.size})"
}

suspend fun VfsFile.readAudioData() = this.openUse { AudioFormats.decode(this) ?: invalidOp("Can't decode audio file ${this@readAudioData}") }