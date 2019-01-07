package com.soywiz.korau.sound

import com.soywiz.korau.format.*

open class NativeAudioStream(freq: Int) {
	open val availableSamples: Int = 0
	open suspend fun addSamples(samples: ShortArray, offset: Int, size: Int) = Unit
	open fun start() = Unit
	open fun stop() = Unit
}

suspend fun NativeAudioStream.add(data: AudioData) {
	addSamples(data.samples, 0, data.samples.size)
}

// @TODO: kotlin-js BUG: https://youtrack.jetbrains.com/issue/KT-25210
//fun NativeAudioStream(): NativeAudioStream = NativeAudioStream(44100)

suspend fun NativeAudioStream.addSamples(samples: ShortArray): Unit = addSamples(samples, 0, samples.size)
