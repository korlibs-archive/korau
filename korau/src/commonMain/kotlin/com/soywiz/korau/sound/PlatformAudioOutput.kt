package com.soywiz.korau.sound

open class PlatformAudioOutput(freq: Int) {
	open val availableSamples: Int = 0
	open suspend fun add(samples: AudioSamples, offset: Int = 0, size: Int = samples.totalSamples) = Unit
	suspend fun add(data: AudioData) = add(data.samples, 0, data.totalSamples)
	open fun start() = Unit
	open fun stop() = Unit
}


