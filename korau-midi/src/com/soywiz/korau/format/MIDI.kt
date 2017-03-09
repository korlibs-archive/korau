package com.soywiz.korau.format

import com.soywiz.korio.stream.AsyncStream

class MIDI : AudioFormat() {
    suspend override fun tryReadInfo(data: AsyncStream): Info? {
        return null
    }
}