package com.soywiz.korau.sound

import android.media.*
import com.soywiz.klock.*
import com.soywiz.kds.*
import com.soywiz.korio.file.*
import com.soywiz.korio.file.std.*
import com.soywiz.korio.util.encoding.*

class AndroidNativeSoundProvider : NativeSoundProvider() {
	val mediaPlayerPool = Pool(reset = {
		it.setOnCompletionListener(null)
		it.reset()
	}) { MediaPlayer() }

	fun getDurationInMs(url: String): Int {
		return mediaPlayerPool.alloc { mp ->
			mp.setDataSource(url)
			mp.prepare()
			mp.duration
		}
	}

	override suspend fun createSound(data: ByteArray, streaming: Boolean): NativeSound =
		AndroidNativeSound(this, "data:audio/mp3;base64," + Base64.encode(data))
	//suspend override fun createSound(file: VfsFile): NativeSound {
	//}

	override suspend fun createSound(vfs: Vfs, path: String, streaming: Boolean): NativeSound {
		return try {
			when (vfs) {
				is LocalVfs -> AndroidNativeSound(this, path)
				else -> super.createSound(vfs, path, streaming)
			}
		} catch (e: Throwable) {
			e.printStackTrace()
			nativeSoundProvider.createSound(AudioData(44100, AudioSamples(2, 0)))
		}
	}
}

class AndroidNativeSound(val prov: AndroidNativeSoundProvider, val url: String) : NativeSound() {
	//override val lengthInMs: Long by lazy { prov.getDurationInMs(url).toLong() }
	override val length: TimeSpan get() = prov.getDurationInMs(url).milliseconds

	override suspend fun decode(): AudioData {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun play(): NativeSoundChannel {
		var mp: MediaPlayer? = prov.mediaPlayerPool.alloc()
		return object : NativeSoundChannel(this) {
			override val current: TimeSpan = mp?.currentPosition?.toDouble()?.milliseconds ?: 0.milliseconds
			override val total: TimeSpan = mp?.duration?.toDouble()?.milliseconds ?: 0.milliseconds
			override var playing: Boolean = true

			override fun stop() {
				playing = false
				if (mp != null) prov.mediaPlayerPool.free(mp!!)
				mp = null
			}

			init {
				mp?.setDataSource(url)
				mp?.setOnCompletionListener {
					stop()
				}
				mp?.prepare()
				mp?.start()
			}
		}
	}
}