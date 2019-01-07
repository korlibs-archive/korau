package com.soywiz.korau.sound

import com.soywiz.klock.*
import com.soywiz.korau.format.*
import kotlin.math.*

object AudioTone {
    fun generate(length: TimeSpan, freq: Double, rate: Int = 44100): AudioData {
        val samples = ShortArray((rate * length.seconds).toInt() * 2)
        val nsamples = samples.size / 2
        val scale = freq / nsamples.toDouble()
        for (n in 0 until nsamples) {
            val ratio = n.toDouble() / nsamples.toDouble()
            val sample = cos(ratio * PI * scale)
            val shortSample = (sample * Short.MAX_VALUE).toShort()
            samples[n * 2 + 0] = shortSample
            samples[n * 2 + 1] = shortSample
        }
        return AudioData(rate, 2, samples)
    }
}