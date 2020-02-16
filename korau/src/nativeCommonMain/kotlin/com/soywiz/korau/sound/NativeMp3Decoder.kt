package com.soywiz.korau.sound

import com.soywiz.korau.format.*
import com.soywiz.korio.stream.*
import kotlinx.cinterop.*
import minimp3.*

object NativeMp3DecoderFormat : AudioFormat("mp3") {
    override suspend fun tryReadInfo(data: AsyncStream): Info?
        = MP3.tryReadInfo(data)

    override suspend fun decodeStream(data: AsyncStream): AudioStream? {
        return object : NativeAudioDecoder(data, MINIMP3_MAX_SAMPLES_PER_FRAME) {
            val mp3d = scope.alloc<mp3dec_t>()

            override fun init() {
                mp3dec_init(mp3d.ptr)
            }

            override fun decodeFrameBase(
                samplesDataPtr: CPointer<ShortVar>,
                frameDataPtr: CPointer<ByteVar>,
                frameSize: Int,
                out: NativeAudioDecoder.DecodeInfo
            ) {
                memScoped {
                    val info = alloc<mp3dec_frame_info_t>()
                    out.samplesDecoded = mp3dec_decode_frame(
                        mp3d.ptr,
                        frameDataPtr.reinterpret(), frameSize,
                        samplesDataPtr,
                        info.ptr
                    )
                    out.frameBytes = info.frame_bytes
                    out.hz = info.hz
                    out.nchannels = info.channels
                }
            }
        }.createAudioStream()
    }

    override suspend fun encode(data: AudioData, out: AsyncOutputStream, filename: String) {
        super.encode(data, out, filename)
    }

    override fun toString(): String = "NativeMp3DecoderFormat"
}
