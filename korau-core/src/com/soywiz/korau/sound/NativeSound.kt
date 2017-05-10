package com.soywiz.korau.sound

import com.soywiz.korau.format.AudioFormats
import com.soywiz.korau.format.AudioStream
import com.soywiz.korio.inject.AsyncDependency
import com.soywiz.korio.service.Services
import com.soywiz.korio.vfs.VfsFile

val nativeSoundProvider by lazy { Services.load(NativeSoundProvider::class.java) }

open class NativeSoundProvider : Services.Impl(), AsyncDependency {
	override suspend fun init(): Unit = Unit

	open suspend fun createSound(data: ByteArray): NativeSound = NativeSound()

	open suspend fun createSound(file: VfsFile): NativeSound = createSound(file.read())

	suspend open fun createSound(data: com.soywiz.korau.format.AudioData): NativeSound {
		return createSound(AudioFormats.encodeToByteArray(data))
	}

	suspend open fun play(stream: AudioStream): Unit = Unit
}

class DummyNativeSoundProvider : NativeSoundProvider() {
	override val priority = Int.MAX_VALUE - 1000
}

open class NativeSound {
	open val lengthInMs: Long = 0L

	suspend open fun play(): Unit {
	}
}

suspend fun VfsFile.readNativeSound() = nativeSoundProvider.createSound(this)
suspend fun VfsFile.readNativeSoundOptimized() = this.readSpecial<NativeSound>()