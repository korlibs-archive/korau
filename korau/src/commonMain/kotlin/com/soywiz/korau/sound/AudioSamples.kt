package com.soywiz.korau.sound

import com.soywiz.kmem.*

class AudioSamples(val channels: Int, val totalSamples: Int) {
    val size get() = totalSamples
    val data = Array(channels) { ShortArray(totalSamples) }

    operator fun get(channel: Int): ShortArray = data[channel]
    operator fun get(channel: Int, sample: Int): Short = data[channel][sample]

    operator fun set(channel: Int, sample: Int, value: Short) = run { data[channel][sample] = value }
}

fun AudioSamples.copyOfRange(start: Int, end: Int): AudioSamples {
    val out = AudioSamples(channels, end - start)
    for (n in 0 until channels) {
        arraycopy(this[n], start, out[n], 0, end - start)
    }
    return out
}

fun AudioSamples.interleaved(out: ShortArray = ShortArray(totalSamples * channels)): ShortArray {
    var m = 0
    for (n in 0 until totalSamples) for (c in 0 until channels) out[m++] = this[c, n]
    return out
}
