package com.soywiz.korau.sound

import com.soywiz.klock.*
import com.soywiz.kmem.*
import com.soywiz.korau.format.*
import com.soywiz.korio.file.*
import com.soywiz.korio.lang.*
import kotlin.math.*

class AudioData(
    val rate: Int,
    val data: AudioSamples
) {
    companion object {
        val DUMMY by lazy { AudioData(44100, AudioSamples(2, 0)) }
    }

    val channels get() = data.channels
    val totalSamples get() = data.totalSamples
    val totalTime: TimeSpan get() = timeAtSample(totalSamples)
    fun timeAtSample(sample: Int) = ((sample).toDouble() / rate.toDouble()).seconds

    operator fun get(channel: Int): ShortArray = data.data[channel]
    operator fun get(channel: Int, sample: Int): Short = data.data[channel][sample]

    override fun toString(): String = "AudioData(rate=$rate, channels=$channels, samples=$totalSamples)"
}

enum class AudioConversionQuality { FAST }

fun AudioData.convertTo(rate: Int = 44100, channels: Int = 2, quality: AudioConversionQuality = AudioConversionQuality.FAST): AudioData {
    TODO()
}


fun AudioData.toStream(): AudioStream = object : AudioStream(rate, channels) {
    var cursor = 0
    override var finished: Boolean = false

    override suspend fun read(out: AudioSamples, offset: Int, length: Int): Int {
        val available = data.totalSamples - cursor
        val toread = min(available, length)
        if (toread > 0) {
            for (n in 0 until channels) {
                arraycopy(data[n], cursor, out[n], offset, toread)
            }
        }
        if (toread <= 0) finished = true
        return toread
    }
}


suspend fun AudioData.toNativeSound() = nativeSoundProvider.createSound(this)

suspend fun AudioData.playAndWait() = this.toNativeSound().play()

suspend fun VfsFile.readAudioData(formats: AudioFormats = defaultAudioFormats) =
    this.openUse { formats.decode(this) ?: invalidOp("Can't decode audio file ${this@readAudioData}") }