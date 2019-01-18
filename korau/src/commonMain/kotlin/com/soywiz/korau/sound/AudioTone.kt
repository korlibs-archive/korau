package com.soywiz.korau.sound

import com.soywiz.klock.*
import com.soywiz.korau.internal.*
import kotlin.math.*

object AudioTone {
    fun generate(length: TimeSpan, freq: Double, rate: Int = 44100): AudioData {
        val nsamples = (rate * length.seconds).toInt()
        val samples = AudioSamples(1, nsamples)
        val scale = freq / nsamples.toDouble()
        for (n in 0 until nsamples) {
            val ratio = n.toDouble() / nsamples.toDouble()
            val sample = cos(ratio * PI * scale)
            val shortSample = SampleConvert.floatToShort(sample.toFloat())
            samples[0, n] = shortSample
        }
        return AudioData(rate, samples)
    }
}