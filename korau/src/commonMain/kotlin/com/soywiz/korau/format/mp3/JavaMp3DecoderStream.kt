package com.soywiz.korau.format.mp3

import com.soywiz.kds.*
import com.soywiz.kmem.*
import com.soywiz.korau.sound.*
import fr.delthas.javamp3.*

fun JavaMp3DecoderToAudioData(data: ByteArray): AudioData {
    val shorts = ShortArrayDeque()
    val data = JavaMp3Decoder.init(data) ?: error("Not an mp3 file")
    val samples = ShortArray(data.samplesBuffer.size / 2)
    while (JavaMp3Decoder.decodeFrame(data)) {
        for (n in samples.indices) samples[n] = data.samplesBuffer.readU16LE(n * 2).toShort()
        shorts.write(samples)
    }

    val samples2 = AudioSamplesInterleaved(data.nchannels, shorts.availableRead)
    shorts.read(samples2.data)

    return AudioData(data.frequency, samples2.separated())
}

suspend fun createJavaMp3DecoderStream(idata: ByteArray): AudioStream {
    var data = JavaMp3Decoder.init(idata) ?: error("Not an mp3 file")
    val samples = ShortArray(data.samplesBuffer.size / 2)
    val deque = AudioSamplesDeque(data.nchannels)
    var samplesPos = 0L

    fun decodeSamples() {
        for (n in samples.indices) samples[n] = data.samplesBuffer.readU16LE(n * 2).toShort()
    }

    return object : AudioStream(data.frequency, data.nchannels) {
        override var currentPositionInSamples: Long
            get() = samplesPos
            set(value) {
                if (value != 0L) error("Only can restart the stream")
                data = JavaMp3Decoder.init(idata) ?: error("Not an mp3 file")
            }

        override suspend fun read(out: AudioSamples, offset: Int, length: Int): Int {
            if (deque.availableRead < length) {
                JavaMp3Decoder.decodeFrame(data)
                decodeSamples()
                deque.writeInterleaved(samples, 0)
            }
            return deque.read(out, offset, length).also {
                samplesPos += length
            }
        }

        override suspend fun clone(): AudioStream = createJavaMp3DecoderStream(idata)
    }
}

/*

class JavaMp3DecoderStream : AudioStream(0, 0) {
    override suspend fun read(out: AudioSamples, offset: Int, length: Int): Int {
        return super.read(out, offset, length)
    }
}
*/
