package com.soywiz.korau.sound

import com.soywiz.kds.*
import com.soywiz.klock.*
import com.soywiz.korau.format.*
import com.soywiz.korio.file.*
import com.soywiz.korio.lang.*
import com.soywiz.korio.stream.*
import kotlin.math.*

open class AudioStream(
    val rate: Int,
    val channels: Int
) : Closeable {
    open val finished = false
    val totalLengthInSamples: Long? = null
    val totalLength get() = ((totalLengthInSamples ?: 0L).toDouble() / rate.toDouble()).seconds
    open suspend fun read(out: AudioSamples, offset: Int, length: Int): Int = 0
    override fun close() = Unit

    companion object {
        fun generator(rate: Int, channels: Int, generateChunk: suspend Array<ShortArrayDeque>.(step: Int) -> Boolean): AudioStream =
            object : AudioStream(rate, channels) {
                val chunks = Array(channels) { ShortArrayDeque() }
                val available get() = chunks[0].availableRead
                override var finished: Boolean = false
                private var step: Int = 0

                override suspend fun read(out: AudioSamples, offset: Int, length: Int): Int {
                    while (available <= 0) {
                        if (finished) return -1
                        if (!generateChunk(chunks, step++)) {
                            finished = true
                            break
                        }
                    }
                    val read = min(length, available)
                    for (n in 0 until channels) chunks[n].read(out[n], offset, read)
                    return read
                }
            }
    }
}

suspend fun AudioStream.toData(maxSamples: Int = Int.MAX_VALUE): AudioData {
    val out = Array(channels) { ShortArrayDeque() }
    val buffer = AudioSamples(channels, 1024)
    try {
        while (!finished) {
            val read = read(buffer, 0, buffer.totalSamples)
            if (read <= 0) break
            for (n in 0 until channels) {
                out[n].write(buffer[n], 0, read)
            }
            if (out[0].availableRead >= maxSamples) break
        }
    } finally {
        close()
    }

    val maxOutSamples = out.map { it.availableRead }.max() ?: 0

    return AudioData(rate, AudioSamples(channels, maxOutSamples).apply {
        for (n in 0 until channels) out[n].read(this.data[n])
    })
}


suspend fun AudioStream.playAndWait(bufferSeconds: Double = 0.1) = nativeSoundProvider.playAndWait(this, bufferSeconds)

suspend fun VfsFile.readAudioStream(formats: AudioFormats = defaultAudioFormats) = formats.decodeStream(this.open())

suspend fun VfsFile.writeAudio(data: AudioData, formats: AudioFormats = defaultAudioFormats) =
    this.openUse(VfsOpenMode.CREATE_OR_TRUNCATE) {
        formats.encode(data, this, this@writeAudio.baseName)
    }
