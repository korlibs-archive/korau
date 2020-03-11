package com.soywiz.korau.format.mp3

import com.soywiz.korau.format.*
import com.soywiz.korau.sound.*
import com.soywiz.korio.stream.*

open class MP3Decoder() : AudioFormat("mp3") {
    companion object : MP3Decoder()

    override suspend fun tryReadInfo(data: AsyncStream, props: AudioDecodingProps): Info? = MP3.tryReadInfo(data, props)
    override suspend fun decodeStream(data: AsyncStream, props: AudioDecodingProps): AudioStream? = MP3DecodeStream(data)
    override fun toString(): String = "NativeMp3DecoderFormat"
}

expect suspend fun MP3DecodeStream(data: AsyncStream): AudioStream?
