package com.soywiz.korau.format

import com.soywiz.korio.stream.AsyncStream

class MIDI : AudioFormat("mid", "midi") {
    suspend override fun tryReadInfo(data: AsyncStream): Info? {
        return null
    }
}