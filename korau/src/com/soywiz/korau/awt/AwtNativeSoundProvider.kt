package com.soywiz.korau.awt

import com.soywiz.korau.format.*
import com.soywiz.korau.sound.NativeSound
import com.soywiz.korau.sound.NativeSoundProvider
import com.soywiz.korio.async.*
import com.soywiz.korio.coroutine.Continuation
import com.soywiz.korio.coroutine.korioSuspendCoroutine
import com.soywiz.korio.stream.openAsync
import java.io.ByteArrayInputStream
import javax.sound.sampled.*
import javax.sound.sampled.AudioFormat

class AwtNativeSoundProvider : NativeSoundProvider() {
	override val priority: Int = 1000

	override suspend fun createSound(data: ByteArray): NativeSound {
		try {
			return AwtNativeSound((AudioFormats.decode(data.openAsync()) ?: AudioData(44100, 2, shortArrayOf())).toWav()).init()
		}catch (e: Throwable) {
			e.printStackTrace()
			return AwtNativeSound(AudioData(44100, 2, shortArrayOf()).toWav()).init()
		}
		//return AwtNativeSound(data)
	}

	suspend override fun play(stream: AudioStream): Unit = suspendCancellableCoroutine { c ->
		spawn(c.context) {
			executeInNewThread {
				val af = AudioFormat(stream.rate.toFloat(), 16, stream.channels, true, false)
				val info = DataLine.Info(SourceDataLine::class.java, af)
				val line = AudioSystem.getLine(info) as SourceDataLine

				line.open(af, 4096)
				line.start()

				val sdata = ShortArray(1024)
				val bdata = ByteArray(sdata.size * 2)
				//var writtenLength = 0L

				while (!c.cancelled) {
					//while (true) {
					//println(c.cancelled)
					//println(line.microsecondPosition)
					//println("" + line.longFramePosition + "/" + writtenLength + "/" + cancelled)
					val read = stream.read(sdata, 0, sdata.size)
					if (read <= 0) break
					var m = 0
					for (n in 0 until read) {
						val s = sdata[n].toInt()
						bdata[m++] = ((s ushr 0) and 0xFF).toByte()
						bdata[m++] = ((s ushr 8) and 0xFF).toByte()
					}
					//println(line.available())
					line.write(bdata, 0, m)
					//writtenLength += read / stream.channels
				}
				line.drain()
				line.stop()
				line.close()
				c.resume(Unit)
			}
		}
	}
}

class AwtNativeSound(val data: ByteArray) : NativeSound() {
	override var lengthInMs: Long = 0L

	suspend fun init(): AwtNativeSound {
		executeInWorker {
			val sound = AudioSystem.getAudioInputStream(ByteArrayInputStream(data))
			lengthInMs = (sound.frameLength * 1000.0 / sound.format.frameRate.toDouble()).toLong()
		}
		return this
	}

	suspend override fun play(): Unit = suspendCancellableCoroutine { c ->
		Thread {
			val sound = AudioSystem.getAudioInputStream(ByteArrayInputStream(data))
			val info = DataLine.Info(Clip::class.java, sound.format)
			val clip = AudioSystem.getLine(info) as Clip
			clip.open(sound)

			clip.addLineListener(MyLineListener(clip, c))
			clip.start()
			c.onCancel {
				clip.stop()
			}
		}.apply {
			isDaemon = true
		}.start()
	}

	private class MyLineListener(val clip: Clip, val c: Continuation<Unit>) : LineListener {
		override fun update(event: LineEvent) {
			when (event.type) {
				LineEvent.Type.STOP, LineEvent.Type.CLOSE -> {
					event.line.close()
					clip.removeLineListener(this)
					c.resume(Unit)
				}
			}
		}
	}
}