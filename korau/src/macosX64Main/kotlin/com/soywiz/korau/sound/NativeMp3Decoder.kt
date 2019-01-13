package com.soywiz.korau.sound

import com.soywiz.kds.*
import com.soywiz.korau.format.*
import com.soywiz.korio.stream.*
import kotlinx.cinterop.*
import minimp3.*

class NativeMp3DecoderFormat : AudioFormat("mp3") {
    override suspend fun tryReadInfo(data: AsyncStream): Info? {
        return super.tryReadInfo(data)
    }

    override suspend fun decodeStream(data: AsyncStream): AudioStream? {
        val scope = Arena()

        val mp3d = scope.alloc<mp3dec_t>()
        var closed = false

        val frameData = ByteArray(16 * 1024)
        val samplesData = ShortArray(MINIMP3_MAX_SAMPLES_PER_FRAME)
        val dataBuffer = ByteArrayDeque(14)
        val samplesBuffer = ShortArrayDeque()

        var nchannels = 0
        var hz = 44100

        mp3dec_init(mp3d.ptr)

        suspend fun decodeFrame() {
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
                        val info = alloc<mp3dec_frame_info_t>()
                        val samples = mp3dec_decode_frame(
                            mp3d.ptr,
                            frameDataPtr.reinterpret(), frameSize,
                            samplesDataPtr,
                            info.ptr
                        )
                        dataBuffer.writeHead(frameData, info.frame_bytes, frameSize - info.frame_bytes)
                        //println("DECODED FRAME: samples=$samples, frame_bytes=${info.frame_bytes}, channels=${info.channels}, bitrate_kbps=${info.bitrate_kbps}, hz=${info.hz}, layer=${info.layer}")

                        if (nchannels == 0) {
                            nchannels = info.channels
                            hz = info.hz
                        }
                        samplesBuffer.write(samplesData, 0, samples)
                    }
                }
            }
        }

        decodeFrame()

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

    override suspend fun encode(data: AudioData, out: AsyncOutputStream, filename: String) {
        super.encode(data, out, filename)
    }

    override fun toString(): String = "NativeMp3DecoderFormat"
}
