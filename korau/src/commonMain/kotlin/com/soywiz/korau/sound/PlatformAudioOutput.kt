package com.soywiz.korau.sound

import com.soywiz.klock.*
import com.soywiz.korio.async.*
import com.soywiz.korio.lang.*

open class PlatformAudioOutput(freq: Int) : Disposable {
	open val availableSamples: Int = 0
    open var pitch: Double = 1.0
    open var volume: Double = 1.0
    open var panning: Double = 0.0
	open suspend fun add(samples: AudioSamples, offset: Int = 0, size: Int = samples.totalSamples) {
        delay(100.milliseconds)
    }
	suspend fun add(data: AudioData) = add(data.samples, 0, data.totalSamples)
	open fun start() = Unit
	open fun stop() = Unit
    override fun dispose() = Unit
}


