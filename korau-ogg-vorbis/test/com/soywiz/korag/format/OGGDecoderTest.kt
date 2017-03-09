package com.soywiz.korag.format

import com.soywiz.korau.format.AudioFormats
import com.soywiz.korau.format.readAudioData
import com.soywiz.korio.async.syncTest
import com.soywiz.korio.vfs.ResourcesVfs
import org.junit.Assert
import org.junit.Test

class OGGDecoderTest {
    @Test
    fun testDecodeWav() = syncTest {
        val expected = ResourcesVfs["ogg1.ogg.wav"].readAudioData()
        val output = ResourcesVfs["ogg1.ogg"].readAudioData()

        val expectedBytes = AudioFormats.encodeToByteArray(expected, "out.wav")
        val outputBytes = AudioFormats.encodeToByteArray(output, "out.wav")

        //output.play()
        //expected.play()
        Assert.assertArrayEquals(expectedBytes, outputBytes)
    }
}