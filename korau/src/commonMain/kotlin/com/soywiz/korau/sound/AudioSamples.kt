package com.soywiz.korau.sound

import com.soywiz.kmem.*

interface IAudioSamples {
    val channels: Int
    val totalSamples: Int
    val size get() = totalSamples
    fun isEmpty() = size == 0
    fun isNotEmpty() = size != 0
    operator fun get(channel: Int, sample: Int): Short
    operator fun set(channel: Int, sample: Int, value: Short): Unit
    fun getFloat(channel: Int, sample: Int): Float = this[channel, sample].toFloat() / Short.MAX_VALUE.toFloat()
    fun setFloat(channel: Int, sample: Int, value: Float) = run { this[channel, sample] = (value.clamp(-1f, +1f) * Short.MAX_VALUE).toShort() }
}

class AudioSamples(override val channels: Int, override val totalSamples: Int) : IAudioSamples {
    val data = Array(channels) { ShortArray(totalSamples) }

    operator fun get(channel: Int): ShortArray = data[channel]

    override operator fun get(channel: Int, sample: Int): Short = data[channel][sample]
    override operator fun set(channel: Int, sample: Int, value: Short) = run { data[channel][sample] = value }
}

class AudioSamplesInterleaved(override val channels: Int, override val totalSamples: Int) : IAudioSamples {
    val data = ShortArray(totalSamples * channels)

    private fun index(channel: Int, sample: Int) = (sample * channels) + channel
    override operator fun get(channel: Int, sample: Int): Short = data[index(channel, sample)]
    override operator fun set(channel: Int, sample: Int, value: Short) = run { data[index(channel, sample)] = value }
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
