package com.soywiz.korau.format.mp3

import com.soywiz.kmem.*
import com.soywiz.korau.sound.*
import com.soywiz.korio.stream.*

suspend fun createJavaMp3DecoderStream(s: AsyncStream): AudioStream {
    return createJavaMp3DecoderStream(s.readAll())
}

// @TODO: Use AsyncStream and read frame chunks
suspend fun createJavaMp3DecoderStream(idata: ByteArray): AudioStream {
    var data = JavaMp3Decoder.init(idata) ?: error("Not an mp3 file")
    val samples = ShortArray(data.samplesBuffer.size / 2)
    val deque = AudioSamplesDeque(data.nchannels)
    var samplesPos = 0L

    fun decodeSamples() {
        for (n in samples.indices) samples[n] = data.samplesBuffer.readU16LE(n * 2).toShort()
    }

    return object : AudioStream(data.frequency, data.nchannels) {
        override var finished: Boolean = false
        override var currentPositionInSamples: Long
            get() = samplesPos
            set(value) {
                if (value != 0L) error("Only can restart the stream")
                data = JavaMp3Decoder.init(idata) ?: error("Not an mp3 file")
                finished = false
            }

        override suspend fun read(out: AudioSamples, offset: Int, length: Int): Int {
            if (deque.availableRead < length) {
                if (!finished && JavaMp3Decoder.decodeFrame(data)) {
                    decodeSamples()
                    deque.writeInterleaved(samples, 0)
                } else {
                    finished = true
                }
            }
            return deque.read(out, offset, length).also {
                samplesPos += length
            }
        }

        override suspend fun clone(): AudioStream = createJavaMp3DecoderStream(idata)
    }
}
