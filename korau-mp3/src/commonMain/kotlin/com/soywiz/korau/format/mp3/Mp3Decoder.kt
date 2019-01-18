package com.soywiz.korau.format.mp3

import com.soywiz.korau.format.*
import com.soywiz.korau.format.mp3.lame.*
import com.soywiz.korau.sound.*
import com.soywiz.korio.lang.*
import com.soywiz.korio.stream.*

//@Keep
open class MP3Decoder : MP3Base() {
    companion object : MP3Decoder()

    override suspend fun decodeStream(data: AsyncStream): AudioStream? {
        val lame = Lame(warningProcessor = null)
        lame.audio.initInFile(lame.flags, data, FrameSkip())

        lame.parser.mp3InputData.totalFrames = lame.parser.mp3InputData.numSamples / lame.parser.mp3InputData.frameSize

        assert(lame.flags.inNumChannels in 1..2)

        val buffer = Array(2) { FloatArray(1152) }
        return AudioStream.generator(lame.flags.inSampleRate, lame.flags.inNumChannels) {
            val flags = lame.flags
            val iread = lame.audio.get_audio16(flags, buffer)
            if (iread > 0) {
                val mp3InputData = lame.parser.mp3InputData
                val framesDecodedCounter = mp3InputData.framesDecodedCounter + iread / mp3InputData.frameSize
                mp3InputData.framesDecodedCounter = framesDecodedCounter

                for (channel in 0 until flags.inNumChannels) {
                    for (i in 0 until iread) {
                        val sample = buffer[channel][i].toInt() and 0xffff
                        this.write(channel, sample.toShort())
                    }
                }
                true
            } else {
                false
            }
        }
    }
}

fun AudioFormats.registerMp3Decoder(): AudioFormats = this.apply { register(MP3Decoder) }
