package com.soywiz.korau.format

import com.soywiz.korio.async.syncTest
import com.soywiz.korio.stream.openAsync
import com.soywiz.korio.vfs.ResourcesVfs
import org.junit.Assert
import org.junit.Test

class DecodeTest {
    @Test
    fun wav() = syncTest {
        val wavContents = ResourcesVfs["wav1.wav"].read()
        val wavData = AudioFormats.decode(wavContents.openAsync())!!

        Assert.assertEquals("AudioData(rate=44100, channels=1, samples=22050)", "$wavData")
        val wavContentsGen = AudioFormats.encodeToByteArray(wavData, "out.wav")

        Assert.assertArrayEquals(wavContents, wavContentsGen)
    }
}