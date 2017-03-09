package com.soywiz.korau.format

import com.soywiz.korio.vfs.VfsFile

open class AudioStream(
        val rate: Int,
        val channels: Int
) {
    suspend open fun read(out: ShortArray, offset: Int, length: Int): Int {
        return 0
    }
}

suspend fun VfsFile.readAudioStream() = AudioFormats.decodeStream(this.open())

suspend fun VfsFile.writeAudio(data: AudioData) = this.openUse(com.soywiz.korio.vfs.VfsOpenMode.CREATE_OR_TRUNCATE) { AudioFormats.encode(data, this, this@writeAudio.basename) }