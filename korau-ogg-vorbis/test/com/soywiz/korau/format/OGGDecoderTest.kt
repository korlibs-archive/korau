package com.soywiz.korau.format

import com.soywiz.korio.async.syncTest
import com.soywiz.korio.vfs.LocalVfs
import com.soywiz.korio.vfs.ResourcesVfs
import org.junit.Assert
import org.junit.Test

class OGGDecoderTest {
    @Test
    fun testDecodeWav() = syncTest {
        //ResourcesVfs["ogg1.ogg"].readAudioData().play()
        //ResourcesVfs["ogg1.ogg"].readAudioStream()!!.play()
        val expected = ResourcesVfs["ogg1.ogg.wav"].readAudioData()
        val output = ResourcesVfs["ogg1.ogg"].readAudioData()

        val expectedBytes = AudioFormats.encodeToByteArray(expected, "out.wav")
        val outputBytes = AudioFormats.encodeToByteArray(output, "out.wav")

        //output.play()
        //expected.play()
        Assert.assertArrayEquals(expectedBytes, outputBytes)

        //LocalVfs("c:/temp/test.ogg").readAudioStream()!!.play()
    }
}