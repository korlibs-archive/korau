package com.soywiz.korau.sound

import com.soywiz.klock.*
import com.soywiz.korau.format.*
import com.soywiz.korio.lang.*
import com.soywiz.korio.stream.*
import kotlinx.cinterop.*
import stb_vorbis.*

object NativeOggVorbisDecoderFormat : AudioFormat("ogg") {
    override suspend fun tryReadInfo(data: AsyncStream): Info? =
        decodeStream(data)?.use { Info(it.totalLength.microseconds.microseconds, it.channels) }

    override suspend fun decodeStream(data: AsyncStream): AudioStream? {
        return object : NativeAudioDecoder(data, 16 * 1024) {
            override fun init() {
            }

            private var vorbis: CPointer<stb_vorbis>? = null

            override fun decodeFrameBase(
                samplesDataPtr: CPointer<ShortVar>,
                frameDataPtr: CPointer<ByteVar>,
                frameSize: Int,
                out: NativeAudioDecoder.DecodeInfo
            ) {
                memScoped {
                    if (vorbis == null) {
                        val consumed = alloc<IntVar>()
                        val error = alloc<IntVar>()
                        val alloc = alloc<stb_vorbis_alloc>()
                        vorbis = stb_vorbis_open_pushdata(
                            frameDataPtr.reinterpret(),
                            frameSize,
                            consumed.ptr,
                            error.ptr,
                            alloc.ptr
                        )
                        //println("vorbis: $vorbis")
                        //println("consumed: ${consumed.value}")
                        //println("error: ${error.value}")

                        out.samplesDecoded = 0
                        out.frameBytes = consumed.value
                        out.totalLengthInSamples = stb_vorbis_stream_length_in_samples(vorbis).toLong()

                        stb_vorbis_get_info(vorbis).useContents {
                            out.nchannels = this.channels.toInt()
                            out.hz = sample_rate.toInt()
                            //println("info.channels: ${this.channels}")
                            //println("info.max_frame_size: ${this.max_frame_size}")
                            //println("info.sample_rate: ${this.sample_rate}")
                            //println("info.setup_memory_required: ${this.setup_memory_required}")
                            //println("info.setup_temp_memory_required: ${this.setup_temp_memory_required}")
                            //println("info.temp_memory_required: ${this.temp_memory_required}")
                            //Unit
                        }
                    } else {
                        val nchannels = alloc<IntVar>()
                        val nsamples = alloc<IntVar>()
                        val output = alloc<CPointerVar<CPointerVar<FloatVar>>>()
                        val consumed = stb_vorbis_decode_frame_pushdata(
                            vorbis,
                            frameDataPtr.reinterpret(),
                            frameSize,
                            nchannels.ptr,
                            output.ptr,
                            nsamples.ptr
                        )
                        val outputPtr = output.value
                        val samples = nsamples.value
                        val channels = nchannels.value

                        //println("stb_vorbis_decode_frame_pushdata: frameSize=$frameSize, samples=$samples, channels=$channels, consumed=$consumed")

                        var m = 0
                        for (n in 0 until samples) {
                            for (channel in 0 until channels) {
                                samplesDataPtr[m++] = (outputPtr!![channel]!![n] * Short.MAX_VALUE).toShort()
                            }
                        }
                        out.frameBytes = consumed
                        out.samplesDecoded = nsamples.value.toInt() * nchannels.value.toInt()
                    }
                }
            }

            override fun close() {
                super.close()
                if (vorbis != null) {
                    stb_vorbis_close(vorbis)
                    vorbis = null
                }
            }
        }.createAudioStream()
    }

    override suspend fun encode(data: AudioData, out: AsyncOutputStream, filename: String) {
        super.encode(data, out, filename)
    }

    override fun toString(): String = "NativeOggVorbisDecoderFormat"
}
