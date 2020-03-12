package com.soywiz.korau.sound

import com.soywiz.kds.*
import com.soywiz.korio.stream.*
import kotlinx.cinterop.*

open class NativeAudioDecoder(val data: AsyncStream, val maxSamples: Int, val maxChannels: Int = 2) {
    val scope = Arena()

    var closed = false

    val frameData = ByteArray(16 * 1024)
    val samplesData = ShortArray(maxSamples)
    val dataBuffer = ByteArrayDeque(14)
    val samplesBuffers = AudioSamplesDeque(maxChannels)

    open fun init() {
    }

    data class DecodeInfo(
        var samplesDecoded: Int = 0,
        var frameBytes: Int = 0,
        var nchannels: Int = 0,
        var hz: Int = 0,
        var totalLengthInSamples: Long? = null
    )

    private val info = DecodeInfo()

    val nchannels: Int get() = info.nchannels
    val hz: Int get() = info.hz
    val totalLengthInSamples: Long? get() = info.totalLengthInSamples


    suspend fun decodeFrame() {
        var n = 0
        while (samplesBuffers.availableRead == 0) {
            memScoped {
                if (dataBuffer.availableRead < 16 * 1024) {
                    val temp = ByteArray(16 * 1024)
                    val tempRead = data.read(temp)
                    dataBuffer.write(temp, 0, tempRead)
                }
                val frameSize = dataBuffer.read(frameData)

                samplesData.usePinned {
                    val samplesDataPtr = it.addressOf(0)
                    frameData.usePinned {
                        val frameDataPtr = it.addressOf(0)
                        decodeFrameBase(samplesDataPtr, frameDataPtr, frameSize, info)
                        dataBuffer.writeHead(frameData, info.frameBytes, frameSize - info.frameBytes)
                        samplesBuffers.writeInterleaved(samplesData, 0, info.samplesDecoded * info.nchannels, channels = info.nchannels)
                    }
                }
            }
            n++
            if (n >= 16) break
        }
    }

    // Must set: samplesDecoded, nchannels, hz and consumedBytes
    protected open fun decodeFrameBase(
        samplesDataPtr: CPointer<ShortVar>,
        frameDataPtr: CPointer<ByteVar>,
        frameSize: Int,
        out: DecodeInfo
    ) {

    }

    open fun close() {
        if (!closed) {
            closed = true
            scope.clear()
        }
    }

    open suspend fun totalSamples(): Long? {
        return null
    }

    open suspend fun seekSamples(sample: Long) {
    }

    open fun clone(): NativeAudioDecoder {
        println("NativeAudioDecoder.clone not implemented")
        return this
    }

    suspend fun createAudioStream(): AudioStream? {
        decodeFrame()

        if (nchannels == 0) {
            return null
        }

        val totalSamples = totalSamples()

        return object : AudioStream(hz, nchannels) {
            var readSamples = 0L
            var seekPosition = -1L

            override val finished: Boolean get() = closed
            override val totalLengthInSamples: Long? get() = totalSamples
            override var currentPositionInSamples: Long
                get() = readSamples
                set(value) {
                    readSamples = value
                    seekPosition = value
                    closed = false
                    dataBuffer.clear()
                    samplesBuffers.clear()
                }

            override suspend fun clone(): AudioStream = this@NativeAudioDecoder.clone().createAudioStream()!!

            override suspend fun read(out: AudioSamples, offset: Int, length: Int): Int {
                if (seekPosition >= 0L) {
                    seekSamples(seekPosition)
                    seekPosition = -1L
                }

                if (closed) return -1

                if (samplesBuffers.availableRead == 0) {
                    decodeFrame()
                }
                val result = samplesBuffers.read(out, offset, length)
                if (result <= 0) {
                    close()
                }
                readSamples += result
                //println("   AudioStream.read -> result=$result")
                return result
            }

            override fun close() {
                this@NativeAudioDecoder.close()
            }
        }
    }
}
