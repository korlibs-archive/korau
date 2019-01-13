package com.soywiz.korau.sound

import com.soywiz.kds.*
import com.soywiz.korau.format.*
import com.soywiz.korio.stream.*
import kotlinx.cinterop.*

open class NativeAudioDecoder(val data: AsyncStream, val maxSamples: Int) {
    val scope = Arena()

    var closed = false

    val frameData = ByteArray(16 * 1024)
    val samplesData = ShortArray(maxSamples)
    val dataBuffer = ByteArrayDeque(14)
    val samplesBuffer = ShortArrayDeque()


    open fun init() {
    }

    data class DecodeInfo(
        var samplesDecoded: Int = 0,
        var frameBytes: Int = 0,
        var nchannels: Int = 0,
        var hz: Int = 0,
        var totalLengthInSamples: Long? = null
    )

    private val info = DecodeInfo(0, 0, 0, 0)

    val nchannels: Int get() = info.nchannels
    val hz: Int get() = info.hz
    val totalLengthInSamples: Long? get() = info.totalLengthInSamples


    suspend fun decodeFrame() {
        var n = 0
        while (samplesBuffer.availableRead == 0) {
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
                        samplesBuffer.write(samplesData, 0, info.samplesDecoded)
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
        scope.clear()
    }

    suspend fun createAudioStream(): AudioStream? {
        decodeFrame()

        if (nchannels == 0) {
            return null
        }

        return object : AudioStream(hz, nchannels) {
            override suspend fun read(out: ShortArray, offset: Int, length: Int): Int {
                if (closed) return -1

                if (samplesBuffer.availableRead == 0) {
                    decodeFrame()
                }
                val result = samplesBuffer.read(out, offset, length)
                if (result <= 0) {
                    close()
                }
                //println("   AudioStream.read -> result=$result")
                return result
            }

            override fun close() {
                if (!closed) {
                    closed = true
                    scope.clear()
                }
            }
        }
    }
}
