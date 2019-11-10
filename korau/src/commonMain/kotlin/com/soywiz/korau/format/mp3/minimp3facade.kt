package com.soywiz.korau.format.mp3

import com.soywiz.kds.*
import com.soywiz.kds.iterators.*
import com.soywiz.korau.format.*
import com.soywiz.korau.sound.*
import com.soywiz.korio.annotations.*
import com.soywiz.korio.stream.*

@Keep
open class MP3Decoder : NativeMp3DecoderFormat() {
    companion object : MP3Decoder()
}

open class NativeMp3DecoderFormat : AudioFormat("mp3") {
    companion object : NativeMp3DecoderFormat() {
    }

    override suspend fun tryReadInfo(data: AsyncStream): Info? {
        return MP3.tryReadInfo(data)
    }

    private val programPool = Pool(1) { MiniMp3(1 * 1024 * 1024) }

    override suspend fun decodeStream(data: AsyncStream): AudioStream? {
        //println(programPool.itemsInPool)
        val program = programPool.alloc()

        //println("program.HEAP_PTR: ${program.HEAP_PTR}")
        //println("program.STACK_PTR: ${program.STACK_PTR}")
        //println("program.mallocCount: ${program.mallocCount}")

        return object : NativeAudioDecoder(program, data, MiniMp3.MINIMP3_MAX_SAMPLES_PER_FRAME) {
            val mp3d = scope.allocBytes<mp3dec_t>(mp3dec_t.SIZE_BYTES)

            override fun init() {
                program.mp3dec_init(mp3d)
            }

            override fun close() {
                super.close()
                //println("FREED")
                programPool.free(program)
            }

            override fun decodeFrameBase(
                samplesDataPtr: CPointer<ShortVar>,
                frameDataPtr: CPointer<ByteVar>,
                frameSize: Int,
                out: DecodeInfo
            ) {
                program.apply {
                    program.stackFrame {
                        val info = CPointer<mp3dec_frame_info_t>(alloca(mp3dec_frame_info_t.SIZE_BYTES).ptr)
                        val infov = mp3dec_frame_info_t(info.ptr)
                        out.samplesDecoded = program.mp3dec_decode_frame(
                            mp3d,
                            frameDataPtr as CPointer<UByte>, frameSize,
                            samplesDataPtr,
                            info
                        )
                        out.frameBytes = infov.frame_bytes
                        out.hz = infov.hz
                        out.nchannels = infov.channels
                    }
                }
            }
        }.createAudioStream()
    }

    override suspend fun encode(data: AudioData, out: AsyncOutputStream, filename: String) {
        super.encode(data, out, filename)
    }

    override fun toString(): String = "NativeMp3DecoderFormat"
}

internal typealias ShortVar = Short
internal typealias IntVar = Int
internal typealias ByteVar = Byte

internal class Arena(val runtime: AbstractRuntime) {
    val pointers = intArrayListOf()

    fun <T> allocBytes(size: Int): CPointer<T> {
        val ptr = runtime.malloc(size).ptr
        pointers.add(ptr)
        return CPointer(ptr)
    }

    fun clear() {
        pointers.fastForEach {
            runtime.free(CPointer<Any>(it))
        }
        pointers.clear()
    }
}

internal fun AbstractRuntime.write(ptr: CPointer<*>, data: ByteArray) = run { for (n in 0 until data.size) sb(ptr.ptr + n, data[n]) }
internal fun AbstractRuntime.read(ptr: CPointer<*>, data: ByteArray) = run { for (n in 0 until data.size) data[n] = lb(ptr.ptr + n) }

internal fun AbstractRuntime.write(ptr: CPointer<*>, data: ShortArray) = run { for (n in 0 until data.size) sh(ptr.ptr + n * 2, data[n]) }
internal fun AbstractRuntime.read(ptr: CPointer<*>, data: ShortArray) = run { for (n in 0 until data.size) data[n] = lh(ptr.ptr + n * 2) }

internal open class NativeAudioDecoder(internal val runtime: AbstractRuntime, val data: AsyncStream, val maxSamples: Int, val maxChannels: Int = 2) {
    var closed = false

    val scope = Arena(runtime)

    internal val frameDataPtr = scope.allocBytes<Byte>(16 * 1024)
    internal val samplesDataPtr = scope.allocBytes<Short>(maxSamples * 2)

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

            if (dataBuffer.availableRead < 16 * 1024) {
                val temp = ByteArray(16 * 1024)
                val tempRead = data.read(temp)
                dataBuffer.write(temp, 0, tempRead)
            }
            val frameSize = dataBuffer.read(frameData)

            runtime.write(frameDataPtr, frameData)
            decodeFrameBase(samplesDataPtr, frameDataPtr, frameSize, info)
            runtime.read(samplesDataPtr, samplesData)
            dataBuffer.writeHead(frameData, info.frameBytes, frameSize - info.frameBytes)
            samplesBuffers.writeInterleaved(samplesData, 0, info.samplesDecoded, channels = info.nchannels)
            n++
            if (n >= 16) break
        }
    }

    // Must set: samplesDecoded, nchannels, hz and consumedBytes
    internal open fun decodeFrameBase(
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
            override suspend fun read(out: AudioSamples, offset: Int, length: Int): Int {
                if (closed) return -1

                if (samplesBuffers.availableRead == 0) {
                    decodeFrame()
                }
                val result = samplesBuffers.read(out, offset, length)
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
                    this@NativeAudioDecoder.close()
                }
            }
        }
    }
}
