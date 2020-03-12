package com.soywiz.korau.sound

import android.annotation.*
import android.media.*
import android.os.*

actual val nativeSoundProvider: NativeSoundProvider by lazy { AndroidNativeSoundProvider() }

class AndroidNativeSoundProvider : NativeSoundProvider() {
	override val target: String = "android"

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun createAudioStream(freq: Int): PlatformAudioOutput {
        open class MyThread(val block: MyThread.() -> Unit) : Thread() {
            var running = true
            override fun run() = block()
        }

        val deque = AudioSamplesDeque(2)
        var thread: MyThread? = null

		return object : PlatformAudioOutput(44100) {
            override val availableSamples: Int get() = deque.availableRead

            override var pitch: Double = 1.0
            override var volume: Double = 1.0
            override var panning: Double = 0.0

            override suspend fun add(samples: AudioSamples, offset: Int, size: Int) {
                deque.write(samples, offset, size)
            }

            override fun start(): Unit {
                val props: SoundProps = this
                thread?.running = false
                thread = MyThread {
                    val mp = MediaPlayer()
                    try {
                        val at = AudioTrack(
                            AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build(),
                            AudioFormat.Builder().setChannelMask(AudioFormat.CHANNEL_IN_STEREO).setSampleRate(freq).setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                                .build(),
                            AudioTrack.MODE_STREAM,
                            2 * 2 * 1024,
                            mp.audioSessionId
                        )
                        try {
                            val temp = AudioSamplesInterleaved(2, 1024)
                            while (running) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    at.playbackParams.speed = props.pitch.toFloat()
                                    //at.playbackParams.pitch = props.pitch.toFloat()
                                }
                                at.setVolume(props.volume.toFloat())
                                val readCount = deque.read(temp)
                                at.write(temp.data, 0, readCount)
                            }
                        } finally {
                            at.stop()
                        }
                    } finally {

                        mp.stop()
                    }
                }.also { it.start() }
            }
            override fun stop() = run { thread?.running = false }
        }
	}

    /*
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

	override suspend fun createSound(data: ByteArray, streaming: Boolean, props: AudioDecodingProps): NativeSound =
		AndroidNativeSound(this, "data:audio/mp3;base64," + Base64.encode(data))
	//suspend override fun createSound(file: VfsFile): NativeSound {
	//}

	override suspend fun createSound(vfs: Vfs, path: String, streaming: Boolean, props: AudioDecodingProps): NativeSound {
		return try {
			when (vfs) {
				is LocalVfs -> AndroidNativeSound(this, path)
				else -> super.createSound(vfs, path, streaming, props)
			}
		} catch (e: Throwable) {
			e.printStackTrace()
			nativeSoundProvider.createSound(AudioData(44100, AudioSamples(2, 0)))
		}
	}
    */
}

/*
class AndroidNativeSound(val prov: AndroidNativeSoundProvider, val url: String) : NativeSound() {
	override val length: TimeSpan by lazy { prov.getDurationInMs(url).milliseconds }

	override suspend fun decode(): AudioData {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun play(params: PlaybackParameters): NativeSoundChannel {
		var mp: MediaPlayer? = prov.mediaPlayerPool.alloc().apply {
			setDataSource(url)
			prepare()

		}
		return object : NativeSoundChannel(this) {
			override var current: TimeSpan
                get() = mp?.currentPosition?.toDouble()?.milliseconds ?: 0.milliseconds
                set(value) = run { TODO() }
			override val total: TimeSpan by lazy { mp?.duration?.toDouble()?.milliseconds ?: 0.milliseconds }
			override var playing: Boolean = true; private set

			override fun stop() {
				playing = false
				if (mp != null) prov.mediaPlayerPool.free(mp!!)
				mp = null
			}

			init {
				mp?.setOnCompletionListener {
					stop()
				}
				mp?.start()
			}
		}
	}
}
*/
