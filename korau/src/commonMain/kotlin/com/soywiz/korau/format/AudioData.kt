package com.soywiz.korau.format

import com.soywiz.klock.*
import com.soywiz.kmem.*
import com.soywiz.korau.sound.*
import com.soywiz.korio.file.*
import com.soywiz.korio.lang.*
import kotlin.math.*

class AudioData(
    val rate: Int,
    val channels: Int,
    val samples: ShortArray
) {
    val numSamples get() = samples.size / channels
    val totalTime: TimeSpan get() = timeAtSample(numSamples)
    fun timeAtSample(sample: Int) = ((sample).toDouble() / rate.toDouble()).seconds

    fun convertTo(rate: Int = 44100, channels: Int = 2): AudioData {
        TODO()
    }

    fun toStream() = object : AudioStream(rate, channels) {
        var cursor = 0
        override var finished: Boolean = false

        override suspend fun read(out: ShortArray, offset: Int, length: Int): Int {
            val available = samples.size - cursor
            val toread = min(available, length)
            if (toread > 0) arraycopy(samples, cursor, out, offset, toread)
            if (toread <= 0) finished = true
            return toread
        }
    }

    override fun toString(): String = "AudioData(rate=$rate, channels=$channels, samples=${samples.size})"
}

suspend fun AudioData.toNativeSound() = nativeSoundProvider.createSound(this)

suspend fun AudioData.playAndWait() = this.toNativeSound().play()

suspend fun VfsFile.readAudioData(formats: AudioFormats = defaultAudioFormats) =
    this.openUse2 { formats.decode(this) ?: invalidOp("Can't decode audio file ${this@readAudioData}") }