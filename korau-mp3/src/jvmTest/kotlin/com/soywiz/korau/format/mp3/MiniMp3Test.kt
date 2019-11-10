package com.soywiz.korau.format.mp3

import com.soywiz.korau.format.*
import com.soywiz.korau.sound.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.std.*
import kotlin.test.*

//#define MINIMP3_NO_SIMD 1
//#define MINIMP3_IMPLEMENTATION 1
//
//#pragma module_name MiniMp3
//#pragma package_name com.soywiz.korau.format.mp3

class MiniMp3Test {
    @Test
    //@Ignore
    fun testMiniMp3() = suspendTest {
        for (n in 0 until 100) {
            val output = resourcesVfs["mp31.mp3"].readAudioData(AudioFormats().register(MP3Decoder))
        }
    }

    @Test
    //@Ignore
    fun testLame() = suspendTest {
        for (n in 0 until 100) {
            val output = resourcesVfs["mp31.mp3"].readAudioData(AudioFormats().register(MP3DecoderLame))
        }
    }
}
