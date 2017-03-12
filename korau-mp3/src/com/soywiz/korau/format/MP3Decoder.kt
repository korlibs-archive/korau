package com.soywiz.korau.format

import com.soywiz.korio.stream.AsyncStream
import net.sourceforge.lame.mp3.FrameSkip
import net.sourceforge.lame.mp3.GetAudio
import net.sourceforge.lame.mp3.Lame
import net.sourceforge.lame.util.AsyncStreamToRandomReader

class MP3Decoder : MP3() {
    suspend override fun decodeStream(data: AsyncStream): AudioStream? {
        val lame = Lame()
        lame.flags.isWriteId3tagAutomatic = false
        lame.initParams()

        lame.parser.inputFormat = GetAudio.SoundFileFormat.sf_mp3

        val frameSkip = FrameSkip()

        lame.audio.initInFile(lame.flags, AsyncStreamToRandomReader(data), frameSkip)

        var skipStart = 0
        var skipEnd = 0

        if (lame.parser.silent < 10)
            System.out.printf("\rinput:  %s%s(%g kHz, %d channel%s, ", data,
                    if (data.getLength() > 26) "\n\t" else "  ", lame.flags
                    .inSampleRate / 1e3, lame.flags
                    .inNumChannels, if (lame.flags
                    .inNumChannels != 1)
                "s"
            else
                "")

        if (frameSkip.encoderDelay > -1 || frameSkip.encoderPadding > -1) {
            if (frameSkip.encoderDelay > -1)
                skipStart = frameSkip.encoderDelay + 528 + 1
            if (frameSkip.encoderPadding > -1)
                skipEnd = frameSkip.encoderPadding - (528 + 1)
        } else {
            skipStart = lame.flags.encoderDelay + 528 + 1
        }
        System.out.printf("MPEG-%d%s Layer %s", 2 - lame.flags
                .mpegVersion,
                if (lame.flags.outSampleRate < 16000) ".5" else "", "III")

        System.out.printf(")\noutput: (16 bit, Microsoft WAVE)\n")

        if (skipStart > 0)
            System.out.printf(
                    "skipping initial %d samples (encoder+decoder delay)\n",
                    skipStart)
        if (skipEnd > 0)
            System.out
                    .printf("skipping final %d samples (encoder padding-decoder delay)\n",
                            skipEnd)

        val totalFrames = lame.parser.mp3InputData
                .numSamples / lame.parser.mp3InputData.frameSize
        lame.parser.mp3InputData.totalFrames = totalFrames

        assert(lame.flags.inNumChannels >= 1 && lame.flags
                .inNumChannels <= 2)

        println(lame.flags.inNumChannels)

        return AudioStream.generator(lame.flags.inSampleRate, lame.flags.inNumChannels) {
            val buffer = Array(2) { FloatArray(1152) }
            val flags = lame.flags
            val iread = lame.audio.get_audio16(flags, buffer)
            if (iread > 0) {
                var opos = 0
                val out = ShortArray(iread * flags.inNumChannels)
                val mp3InputData = lame.parser.mp3InputData
                val framesDecodedCounter = mp3InputData
                        .framesDecodedCounter + iread / mp3InputData.frameSize
                mp3InputData.framesDecodedCounter = framesDecodedCounter


                for (i in 0 until iread) {
                    var sample = buffer[0][i].toInt() and 0xffff
                    out[opos++] = sample.toShort()
                    if (flags.inNumChannels == 2) {
                        sample = buffer[1][i].toInt() and 0xffff
                        out[opos++] = sample.toShort()
                    }
                }
                out
            } else {
                null
            }
        }
    }
}