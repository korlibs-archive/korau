package com.soywiz.korau.format

import com.soywiz.korio.async.syncTest
import com.soywiz.korio.stream.openAsync
import com.soywiz.korio.vfs.LocalVfs
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

    @Test
    fun wav24() = syncTest {
        val wavContents = ResourcesVfs["wav24.wav"].read()
        val wavData = AudioFormats.decode(wavContents.openAsync())!!

        Assert.assertEquals("AudioData(rate=48000, channels=1, samples=4120)", "$wavData")
        val wavContentsGen = AudioFormats.encodeToByteArray(wavData, "out.wav")

        //LocalVfs("c:/temp/lol.wav").write(wavContentsGen)
        //Assert.assertArrayEquals(wavContents, wavContentsGen)
    }
}