package com.soywiz.korau.format.mp3

import com.soywiz.kds.*
import com.soywiz.kds.iterators.*
import com.soywiz.korau.format.*
import com.soywiz.korau.sound.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.std.*
import com.soywiz.korio.stream.*
import kotlin.test.*

typealias ShortVar = Short
typealias IntVar = Int
typealias ByteVar = Byte

class Arena(val runtime: Runtime) {
    val pointers = intArrayListOf()

    fun <T> allocBytes(size: Int): CPointer<T> {
        val ptr = runtime.alloca(size).ptr
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

fun Runtime.memScoped(callback: Arena.() -> Unit) {
    val arena = Arena(this) // @TODO: Pooling
    try {
        callback(arena)
    } finally {
        arena.clear()
    }
}

fun Runtime.write(ptr: CPointer<*>, data: ByteArray) = run { for (n in 0 until data.size) sb(ptr.ptr + n, data[n]) }
fun Runtime.read(ptr: CPointer<*>, data: ByteArray) = run { for (n in 0 until data.size) data[n] = lb(ptr.ptr + n) }

fun Runtime.write(ptr: CPointer<*>, data: ShortArray) = run { for (n in 0 until data.size) sh(ptr.ptr + n * 2, data[n]) }
fun Runtime.read(ptr: CPointer<*>, data: ShortArray) = run { for (n in 0 until data.size) data[n] = lh(ptr.ptr + n * 2) }

open class NativeAudioDecoder(val runtime: Runtime, val data: AsyncStream, val maxSamples: Int, val maxChannels: Int = 2) {
    var closed = false

    val scope = Arena(runtime)

    val frameDataPtr = scope.allocBytes<Byte>(16 * 1024)
    val samplesDataPtr = scope.allocBytes<Short>(maxSamples * 2)

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
                }
            }
        }
    }
}

object NativeMp3DecoderFormat : AudioFormat("mp3") {
    override suspend fun tryReadInfo(data: AsyncStream): Info? {
        return MP3.tryReadInfo(data)
    }

    private const val MINIMP3_MAX_SAMPLES_PER_FRAME = (1152*2)

    override suspend fun decodeStream(data: AsyncStream): AudioStream? {
        val program = Program()
        return object : NativeAudioDecoder(program, data, MINIMP3_MAX_SAMPLES_PER_FRAME) {
            val mp3d = scope.allocBytes<Program.mp3dec_t>(Program.mp3dec_t.SIZE_BYTES)

            override fun init() {
                program.mp3dec_init(mp3d)
            }

            override fun decodeFrameBase(
                samplesDataPtr: CPointer<ShortVar>,
                frameDataPtr: CPointer<ByteVar>,
                frameSize: Int,
                out: NativeAudioDecoder.DecodeInfo
            ) {
                program.apply {
                    program.memScoped {
                        val info = allocBytes<Program.mp3dec_frame_info_t>(Program.mp3dec_frame_info_t.SIZE_BYTES)
                        val infov = Program.mp3dec_frame_info_t(info.ptr)
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


class MiniMp3Test {
    @Test
    @Ignore
    fun test() = suspendTest {
        val output = resourcesVfs["mp31.mp3"].readAudioData(AudioFormats().register(NativeMp3DecoderFormat))
    }
}
