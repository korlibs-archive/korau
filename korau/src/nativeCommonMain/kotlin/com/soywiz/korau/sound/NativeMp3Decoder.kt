package com.soywiz.korau.sound

import com.soywiz.korau.format.*
import com.soywiz.korio.stream.*
import kotlinx.cinterop.*
import minimp3.*

object NativeMp3DecoderAudioFormat : AudioFormat("mp3") {
    override suspend fun tryReadInfo(data: AsyncStream, props: AudioDecodingProps): Info?
        = MP3.tryReadInfo(data, props)

    class Mp3AudioDecoder(data: AsyncStream, val props: AudioDecodingProps) : NativeAudioDecoder(data, MINIMP3_MAX_SAMPLES_PER_FRAME) {
        val mp3d = scope.alloc<mp3dec_t>()

        override fun init() {
            mp3dec_init(mp3d.ptr)
        }

        override fun decodeFrameBase(
            samplesDataPtr: CPointer<ShortVar>,
            frameDataPtr: CPointer<ByteVar>,
            frameSize: Int,
            out: DecodeInfo
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

        private var mp3SeekingTable: MP3Base.SeekingTable? = null
        suspend fun getSeekingTable(): MP3Base.SeekingTable {
            if (mp3SeekingTable == null) mp3SeekingTable = MP3Base.Parser(data).getSeekingTable()
            return mp3SeekingTable!!
        }

        override suspend fun totalSamples(): Long? = getSeekingTable().lengthSamples

        override suspend fun seekSamples(sample: Long) {
            dataBuffer.clear()
            samplesBuffers.clear()
            data.position = getSeekingTable().locateSample(sample)
        }

        override fun clone(): NativeAudioDecoder = Mp3AudioDecoder(data.duplicate(), props)
    }

    override suspend fun decodeStream(data: AsyncStream, props: AudioDecodingProps): AudioStream? = Mp3AudioDecoder(data, props).createAudioStream()

    override fun toString(): String = "NativeMp3DecoderFormat"
}
