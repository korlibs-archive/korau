package com.soywiz.korau.format

open class AudioBuffer {
    var buffer = ShortArray(0)
    var bufferlen = 0

    fun ensure(len: Int) {
        if (this.bufferlen + len > buffer.size) {
            buffer = buffer.copyOf((this.bufferlen + len) * 2)
        }
    }

    fun write(data: ShortArray, offset: Int, len: Int) {
        if (len <= 0) return
        ensure(len)
        System.arraycopy(data, offset, this.buffer, this.bufferlen, len)
        this.bufferlen += len
    }

    fun toShortArray() = buffer.copyOf(bufferlen)
}