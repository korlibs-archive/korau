package com.soywiz.korau.sound

import com.soywiz.korau.format.*
import com.soywiz.korau.format.mp3.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.std.*
import kotlin.test.*

class SoftMp3DecoderTest {
    @Test
    //@Ignore
    fun testMiniMp3() = suspendTest {
        for (n in 0 until 100) {
            val output = resourcesVfs["mp31.mp3"].readAudioData(AudioFormats().register(MP3Decoder))
        }
    }
}
