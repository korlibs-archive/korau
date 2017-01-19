@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package com.soywiz.korau.format

import com.soywiz.korio.async.await
import com.soywiz.korio.error.invalidOp
import com.soywiz.korio.stream.*

class WAV : AudioFormat() {
	data class Chunk(val type: String, val data: AsyncStream)

	suspend override fun tryReadInfo(data: AsyncStream): Info? = try {
		parse(data)
	} catch (e: Throwable) {
		null
	}

	suspend fun parse(data: AsyncStream): Info {
		var formatTag: Int = -1
		var channels: Int = 2
		var samplesPerSec: Int = 0
		var avgBytesPerSec: Long = 0L
		var blockAlign: Int = 0
		var bitsPerSample: Int = 0
		var dataSize = 0L

		riff(data) {
			val (type, d) = this
			when (type) {
				"fmt " -> {
					formatTag = d.readS16_le()
					channels = d.readS16_le()
					samplesPerSec = d.readS32_le()
					avgBytesPerSec = d.readU32_le()
					blockAlign = d.readS16_le()
					bitsPerSample = d.readS16_le()
				}
				"data" -> {
					dataSize += d.getLength()
				}
				else -> Unit
			}
		}
		if (formatTag < 0) invalidOp("Couldn't find RIFF 'fmt ' chunk")

		return Info(
			lengthInMicroseconds = (dataSize * 1000 * 1000) / avgBytesPerSec,
			channels = channels
		)
	}

	suspend fun riff(data: AsyncStream, handler: suspend Chunk.() -> Unit) {
		val s2 = data.clone()
		val magic = s2.readString(4)
		val length = s2.readS32_le()
		val magic2 = s2.readString(4)
		if (magic != "RIFF") invalidOp("Not a RIFF file")
		if (magic2 != "WAVE") invalidOp("Not a RIFF + WAVE file")
		val s = s2.readStream(length - 4)
		while (!s.eof()) {
			val type = s.readString(4)
			val size = s.readS32_le()
			val d = s.readStream(size)
			handler.await(Chunk(type, d))
		}
	}
}
