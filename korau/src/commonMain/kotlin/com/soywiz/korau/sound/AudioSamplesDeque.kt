package com.soywiz.korau.sound

import com.soywiz.kds.*
import kotlin.math.*

class AudioSamplesDeque(val channels: Int) {
    val buffer = Array(channels) { ShortArrayDeque() }
    val availableRead get() = buffer[0].availableRead
    val maxOutSamples: Int get() = buffer.map { it.availableRead }.max() ?: 0

    private val temp = ShortArray(1)

    // Individual samples
    fun read(channel: Int): Short = buffer[channel].read(temp, 0, 1).let { temp[0] }

    fun write(channel: Int, sample: Short) = run { buffer[channel].write(temp.also { temp[0] = sample }, 0, 1) }

    // Write samples
    fun write(samples: AudioSamples, offset: Int = 0, len: Int = samples.size - offset) {
        for (channel in 0 until samples.channels) write(channel, samples[channel], offset, len)
    }

    fun write(samples: AudioSamplesInterleaved, offset: Int = 0, len: Int = samples.size - offset) {
        writeInterleaved(samples.data, offset, len, samples.channels)
    }

    fun write(samples: IAudioSamples, offset: Int = 0, len: Int = samples.size - offset) {
        when (samples) {
            is AudioSamples -> write(samples, offset, len)
            is AudioSamplesInterleaved -> write(samples, offset, len)
            else -> for (c in 0 until samples.channels) for (n in 0 until len) write(c, samples[c, offset + n])
        }
    }

    // Write raw
    fun write(channel: Int, data: ShortArray, offset: Int = 0, len: Int = data.size - offset) {
        buffer[channel].write(data, offset, len)
    }

    // @TODO: Important to optimize!
    fun writeInterleaved(data: ShortArray, offset: Int, len: Int = data.size - offset, channels: Int = this.channels) {
        for (n in 0 until len) {
            val channel = n % channels
            write(channel, data[offset + n])
        }
    }

    fun read(out: AudioSamples, offset: Int = 0, len: Int = out.totalSamples - offset): Int {
        val result = min(len, availableRead)
        for (channel in 0 until out.channels) this.buffer[channel].read(out[channel], offset, len)
        return result
    }

    fun read(out: AudioSamplesInterleaved, offset: Int = 0, len: Int = out.totalSamples - offset): Int {
        val result = min(len, availableRead)
        for (channel in 0 until out.channels) for (n in 0 until len) out[channel, offset + n] = this.read(channel)
        return result
    }

    fun read(out: IAudioSamples, offset: Int = 0, len: Int = out.totalSamples - offset): Int {
        val result = min(len, availableRead)
        when (out) {
            is AudioSamples -> read(out, offset, len)
            is AudioSamplesInterleaved -> read(out, offset, len)
            else -> for (c in 0 until out.channels) for (n in 0 until len) out[c, offset + n] = this.read(c)
        }
        return result
    }
}
