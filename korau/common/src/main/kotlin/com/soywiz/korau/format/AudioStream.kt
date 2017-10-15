package com.soywiz.korau.format

import com.soywiz.korau.sound.nativeSoundProvider
import com.soywiz.korio.ds.LinkedList
import com.soywiz.korio.typedarray.copyRangeTo
import com.soywiz.korio.vfs.VfsFile
import kotlin.math.min

open class AudioStream(
	val rate: Int,
	val channels: Int
) {
	suspend open fun read(out: ShortArray, offset: Int, length: Int): Int {
		return 0
	}

	suspend fun toData(): AudioData {
		val out = AudioBuffer()
		val buffer = ShortArray(1024)
		while (true) {
			val read = read(buffer, 0, buffer.size)
			if (read <= 0) break
			out.write(buffer, 0, read)
		}
		return AudioData(rate, channels, out.toShortArray())
	}

	companion object {
		fun generator(rate: Int, channels: Int, gen: suspend () -> ShortArray?): AudioStream {
			return object : AudioStream(rate, channels) {
				var chunk: ShortArray = shortArrayOf()
				var pos = 0
				val available get() = chunk.size - pos
				val chunks = LinkedList<ShortArray>()

				suspend override fun read(out: ShortArray, offset: Int, length: Int): Int {
					while (available <= 0) {
						if (chunks.isEmpty()) chunks += gen() ?: return 0
						chunk = chunks.removeFirst()
						pos = 0
					}
					val read = min(length, available)
					chunk.copyRangeTo(pos, out, offset, read)
					pos += read
					return read
				}
			}
		}
	}
}

suspend fun AudioStream.play() = nativeSoundProvider.play(this)

suspend fun VfsFile.readAudioStream(formats: AudioFormats = defaultAudioFormats) = formats.decodeStream(this.open())

suspend fun VfsFile.writeAudio(data: AudioData, formats: AudioFormats = defaultAudioFormats) = this.openUse(com.soywiz.korio.vfs.VfsOpenMode.CREATE_OR_TRUNCATE) { formats.encode(data, this, this@writeAudio.basename) }