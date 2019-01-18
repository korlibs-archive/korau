package com.soywiz.korau.sound

import com.soywiz.klock.*
import com.soywiz.korau.format.*
import com.soywiz.korio.file.*
import com.soywiz.korio.lang.*
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
        fun generator(rate: Int, channels: Int, generateChunk: suspend AudioSamplesDeque.(step: Int) -> Boolean): AudioStream =
            object : AudioStream(rate, channels) {
                val deque = AudioSamplesDeque(channels)
                val availableRead get() = deque.availableRead
                override var finished: Boolean = false
                private var step: Int = 0

                override suspend fun read(out: AudioSamples, offset: Int, length: Int): Int {
                    if (finished && availableRead <= 0) return -1
                    while (availableRead <= 0) {
                        if (!generateChunk(deque, step++)) {
                            finished = true
                            break
                        }
                    }
                    val read = min(length, availableRead)
                    deque.read(out, offset, read)
                    return read
                }
            }
    }
}

suspend fun AudioStream.toData(maxSamples: Int = Int.MAX_VALUE): AudioData {
    val out = AudioSamplesDeque(channels)
    val buffer = AudioSamples(channels, 1024)
    try {
        while (!finished) {
            val read = read(buffer, 0, buffer.totalSamples)
            if (read <= 0) break
            out.write(buffer, 0, read)
            if (out.availableRead >= maxSamples) break
        }
    } finally {
        close()
    }

    val maxOutSamples = out.availableReadMax

    return AudioData(rate, AudioSamples(channels, maxOutSamples).apply { out.read(this) })
}


suspend fun AudioStream.playAndWait(bufferSeconds: Double = 0.1) = nativeSoundProvider.playAndWait(this, bufferSeconds)

suspend fun VfsFile.readAudioStream(formats: AudioFormats = defaultAudioFormats) = formats.decodeStream(this.open())

suspend fun VfsFile.writeAudio(data: AudioData, formats: AudioFormats = defaultAudioFormats) =
    this.openUse(VfsOpenMode.CREATE_OR_TRUNCATE) {
        formats.encode(data, this, this@writeAudio.baseName)
    }
