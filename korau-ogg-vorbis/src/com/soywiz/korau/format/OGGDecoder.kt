package com.soywiz.korau.format

import com.soywiz.korio.stream.AsyncStream

class OGGDecoder : OGG() {
    suspend override fun decodeStream(data: AsyncStream): AudioStream? {
        return object : AudioStream(44100, 1) {
            suspend override fun read(out: ShortArray, offset: Int, length: Int): Int {
                return 0
            }
        }
    }
}