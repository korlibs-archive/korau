package com.soywiz.korau.format

import com.soywiz.korio.async.syncTest
import com.soywiz.korio.vfs.LocalVfs
import com.soywiz.korio.vfs.ResourcesVfs
import org.junit.Assert
import org.junit.Test

class MP3DecoderTest {
    // http://mpgedit.org/mpgedit/testdata/mpegdata.html
    @Test
    fun testDecodeWav() = syncTest {

        val output = ResourcesVfs["mp31.mp3"].readAudioData()
        val outputBytes = AudioFormats.encodeToByteArray(output, "out.wav")

        //output.play()
        //expected.play()

        //LocalVfs("c:/temp/mp31.mp3.wav").write(outputBytes)

        val expected = ResourcesVfs["mp31.mp3.wav"].readAudioData()
        val expectedBytes = AudioFormats.encodeToByteArray(expected, "out.wav")

        Assert.assertArrayEquals(expectedBytes, outputBytes)

        //LocalVfs("c:/temp/test.mp3").readAudioStream()!!.play()
        //LocalVfs("c:/temp/test3.mp3").readAudioStream()!!.play()
        //ResourcesVfs["fl1.mp1"].readAudioStream()!!.play()
        //ResourcesVfs["fl4.mp1"].readAudioStream()!!.play()
        //ResourcesVfs["fl5.mp1"].readAudioStream()!!.play()
        //ResourcesVfs["fl10.mp2"].readAudioStream()!!.play()
        //ResourcesVfs["fl13.mp2"].readAudioStream()!!.play()
        //ResourcesVfs["fl14.mp2"].readAudioStream()!!.play()
        //ResourcesVfs["fl16.mp2"].readAudioStream()!!.play()
        ResourcesVfs["mp31_joint_stereo_vbr.mp3"].readAudioStream()!!.play()
        LocalVfs("c:/temp/test2.mp3").readAudioStream()!!.play()
    }
}