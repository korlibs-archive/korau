package com.soywiz.korau.format

import com.soywiz.kds.*
import com.soywiz.kmem.*
import com.soywiz.korau.internal.*
import com.soywiz.korau.sound.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.*
import com.soywiz.korio.lang.*
import com.soywiz.korio.stream.*
import kotlin.math.*

interface BaseAudioStream : Closeable {
    val rate: Int
    val channels: Int
    val finished: Boolean
    suspend fun read(out: ShortArray, offset: Int, length: Int): Int
}

suspend fun BaseAudioStream.toData(maxSamples: Int = Int.MAX_VALUE): AudioData {
    val temp = ShortArray(1024)
    var totalSize = 0L
    val buffers = arrayListOf<ShortArray>()
    while (!finished) {
        val read = read(temp, 0, temp.size)
        if (read <= 0) break
        totalSize += read
        if (totalSize >= maxSamples) break
        buffers += temp.copyOf(read)
    }
    return AudioData(rate, channels, buffers.combine())
}

open class AudioStream(
    override val rate: Int,
    override val channels: Int
) : BaseAudioStream {
    override val finished = false

    override suspend fun read(out: ShortArray, offset: Int, length: Int): Int {
        return 0
    }

    override fun close() {
    }

    suspend fun toData(): AudioData {
        val out = AudioBuffer()
        val buffer = ShortArray(1024)
        while (true) {
            val read = read(buffer, 0, buffer.size)
            if (read <= 0) break
            out.write(buffer, 0, read)
        }
        close()
        return AudioData(rate, channels, out.toShortArray())
    }

    companion object {
        fun generator(rate: Int, channels: Int, gen: suspend () -> ShortArray?): AudioStream {
            return object : AudioStream(rate, channels) {
                var chunk: ShortArray = shortArrayOf()
                var pos = 0
                val available get() = chunk.size - pos
                val chunks = Deque<ShortArray>()
                override val finished: Boolean = false

                override suspend fun read(out: ShortArray, offset: Int, length: Int): Int {
                    while (available <= 0) {
                        if (chunks.isEmpty()) chunks += gen() ?: return 0
                        chunk = chunks.removeFirst()
                        pos = 0
                    }
                    val read = min(length, available)
                    arraycopy(chunk, pos, out, offset, read)
                    pos += read
                    return read
                }
            }
        }
    }
}

suspend fun BaseAudioStream.playAndWait(bufferSeconds: Double = 0.1) = nativeSoundProvider.playAndWait(this, bufferSeconds)

suspend fun VfsFile.readAudioStream(formats: AudioFormats = defaultAudioFormats) = formats.decodeStream(this.open())

suspend fun VfsFile.writeAudio(data: AudioData, formats: AudioFormats = defaultAudioFormats) =
    this.openUse2(VfsOpenMode.CREATE_OR_TRUNCATE) {
        formats.encode(data, this, this@writeAudio.baseName)
    }

// @TODO: Problem with Kotlin.JS. Fails in runtime returning kotlin.Unit.
// @TODO: BUG in Kotlin.JS. Fails in runtime returning kotlin.Unit.
/*
suspend inline fun <T> VfsFile.openUse2(
	mode: VfsOpenMode = VfsOpenMode.READ,
	noinline callback: suspend AsyncStream.() -> T
): T {
	return open(mode).use { callback.await(this) }
}
*/
// @TODO: BUG in Kotlin.JS. Fails in runtime returning kotlin.Unit.
/*
suspend inline fun <T> VfsFile.openUse2(
	mode: VfsOpenMode = VfsOpenMode.READ,
	noinline callback: suspend AsyncStream.() -> T
): T {
	//return open(mode).use { callback.await(this) }
	val s = open(mode)
	try {
		return callback.await(s)
	} finally {
		s.close()
	}
}
*/

// @TODO: Works in Kotlin.JS
suspend fun <T> VfsFile.openUse2(
    mode: VfsOpenMode = VfsOpenMode.READ,
    callback: suspend AsyncStream.() -> T
): T {
    //return open(mode).use { callback.await(this) }
    val s = open(mode)
    try {
        return callback(s)
    } finally {
        s.close()
    }
}
