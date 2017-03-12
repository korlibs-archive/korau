package com.soywiz.korau.format

import com.soywiz.korio.stream.AsyncStream
import net.sourceforge.lame.mp3.FrameSkip
import net.sourceforge.lame.mp3.GetAudio
import net.sourceforge.lame.mp3.Lame
import java.io.RandomAccessFile

class MP3Decoder : MP3() {
    suspend override fun decodeStream(data: AsyncStream): AudioStream? {
        //val lame = Lame()
        //lame.flags.isWriteId3tagAutomatic = false
        //lame.initParams()
//
        //lame.parser.inputFormat = GetAudio.SoundFileFormat.sf_mp3
//
        //val frameSkip = FrameSkip()
//
        //lame.audio.initInFile(lame.flags, mp3File, frameSkip)
        //lame.audio.initInFile()
//
        //val out = object : AudioStream() {
//
        //}

        return super.decodeStream(data)
    }
}