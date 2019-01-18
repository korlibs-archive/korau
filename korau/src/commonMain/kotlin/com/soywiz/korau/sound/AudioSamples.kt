package com.soywiz.korau.sound

import com.soywiz.kmem.*
import com.soywiz.korau.internal.*

interface IAudioSamples {
    val channels: Int
    val totalSamples: Int
    val size get() = totalSamples
    fun isEmpty() = size == 0
    fun isNotEmpty() = size != 0
    operator fun get(channel: Int, sample: Int): Short
    operator fun set(channel: Int, sample: Int, value: Short): Unit
    fun getFloat(channel: Int, sample: Int): Float = SampleConvert.shortToFloat(this[channel, sample])
    fun setFloat(channel: Int, sample: Int, value: Float) = run { this[channel, sample] = SampleConvert.floatToShort(value) }
}

class AudioSamples(override val channels: Int, override val totalSamples: Int) : IAudioSamples {
    val data = Array(channels) { ShortArray(totalSamples) }

    operator fun get(channel: Int): ShortArray = data[channel]

    override operator fun get(channel: Int, sample: Int): Short = data[channel][sample]
    override operator fun set(channel: Int, sample: Int, value: Short) = run { data[channel][sample] = value }

    override fun hashCode(): Int = channels + totalSamples * 32 + data.contentDeepHashCode() * 64
    override fun equals(other: Any?): Boolean = (other is AudioSamples) && this.channels == other.channels && this.totalSamples == other.totalSamples && this.data.contentDeepEquals(other.data)

    override fun toString(): String = "AudioSamples(channels=$channels, totalSamples=$totalSamples)"
}

class AudioSamplesInterleaved(override val channels: Int, override val totalSamples: Int) : IAudioSamples {
    val data = ShortArray(totalSamples * channels)

    private fun index(channel: Int, sample: Int) = (sample * channels) + channel
    override operator fun get(channel: Int, sample: Int): Short = data[index(channel, sample)]
    override operator fun set(channel: Int, sample: Int, value: Short) = run { data[index(channel, sample)] = value }

    override fun toString(): String = "AudioSamplesInterleaved(channels=$channels, totalSamples=$totalSamples)"
}

fun AudioSamples.copyOfRange(start: Int, end: Int): AudioSamples {
    val out = AudioSamples(channels, end - start)
    for (n in 0 until channels) {
        arraycopy(this[n], start, out[n], 0, end - start)
    }
    return out
}

fun IAudioSamples.interleaved(out: AudioSamplesInterleaved = AudioSamplesInterleaved(channels, totalSamples)): AudioSamplesInterleaved {
    var m = 0
    for (n in 0 until totalSamples) for (c in 0 until channels) out.data[m++] = this[c, n]
    return out
}

fun IAudioSamples.separated(out: AudioSamples = AudioSamples(channels, totalSamples)): AudioSamples {
    for (n in 0 until totalSamples) for (c in 0 until channels) out[c, n] = this[c, n]
    return out
}
