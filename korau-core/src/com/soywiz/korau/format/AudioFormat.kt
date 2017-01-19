@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package com.soywiz.korau.format

import com.soywiz.korio.stream.AsyncStream
import com.soywiz.korio.util.Extra
import com.soywiz.korio.vfs.VfsFile
import java.util.*

open class AudioFormat {
	data class Info(
		var lengthInMicroseconds: Long = 0L,
		var channels: Int = 2
	) : Extra by Extra.Mixin() {
		val msLength = lengthInMicroseconds / 1000L
		val length = lengthInMicroseconds.toDouble() / 1_000_000.0
	}

	suspend open fun tryReadInfo(data: AsyncStream): Info? = null
}

object AudioFormats : AudioFormat() {
	val formats = ServiceLoader.load(AudioFormat::class.java).toList()

	suspend override fun tryReadInfo(data: AsyncStream): Info? {
		for (format in formats) {
			try {
				return format.tryReadInfo(data) ?: continue
			} catch (e: Throwable) {
			}
		}
		return null
	}
}

suspend fun VfsFile.readSoundInfo() = this.openUse { AudioFormats.tryReadInfo(this) }