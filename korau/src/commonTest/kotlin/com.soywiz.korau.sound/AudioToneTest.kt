package com.soywiz.korau.sound

import com.soywiz.klock.*
import kotlin.test.*

class AudioToneTest {
    @Test
    fun test() {
        val data = AudioTone.generate(1.seconds, 10000.0)
        assertEquals(44100, data.totalSamples)
        assertEquals(1, data.samples.data.size)
        assertEquals(44100, data.samples.data[0].size)
    }
}