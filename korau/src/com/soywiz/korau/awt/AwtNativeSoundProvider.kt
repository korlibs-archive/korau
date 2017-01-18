package com.soywiz.korau.awt

import com.soywiz.korau.sound.NativeSound
import com.soywiz.korau.sound.NativeSoundProvider
import com.soywiz.korio.async.suspendCoroutineEL
import java.io.ByteArrayInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.DataLine
import javax.sound.sampled.LineEvent

class AwtNativeSoundProvider : NativeSoundProvider() {
	override val priority: Int = 1000

	override fun createSound(data: ByteArray): NativeSound {
		return AwtNativeSound(data)
	}
}

class AwtNativeSound(val data: ByteArray) : NativeSound() {
	suspend override fun play(): Unit = suspendCoroutineEL { c ->
		val sound = AudioSystem.getAudioInputStream(ByteArrayInputStream(data))
		val info = DataLine.Info(Clip::class.java, sound.format)
		val clip = AudioSystem.getLine(info) as Clip
		clip.open(sound)
		clip.addLineListener { event ->
			if (event.type === LineEvent.Type.STOP) {
				event.line.close()
				c.resume(Unit)
			}
		}
		clip.start()
	}
}