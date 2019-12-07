package com.soywiz.korau.format.mp3

import com.soywiz.kds.*
import com.soywiz.korau.format.*
import com.soywiz.korau.sound.*
import com.soywiz.korio.stream.*

open class MP3Decoder() : AudioFormat("mp3") {
    companion object : MP3Decoder()

    override suspend fun tryReadInfo(data: AsyncStream): Info? {
        return MP3.tryReadInfo(data)
    }

    //private val programPool = Pool(1) { MiniMp3(1 * 1024 * 1024) }

    override suspend fun decodeStream(data: AsyncStream): AudioStream? = MP3DecodeStream(data)

    override suspend fun encode(data: AudioData, out: AsyncOutputStream, filename: String) {
        super.encode(data, out, filename)
    }

    override fun toString(): String = "NativeMp3DecoderFormat"
}

expect suspend fun MP3DecodeStream(data: AsyncStream): AudioStream?
