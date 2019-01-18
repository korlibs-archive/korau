package com.soywiz.korau.sound

import com.soywiz.klock.*
import com.soywiz.korau.format.*
import com.soywiz.korio.async.*
import com.soywiz.korio.stream.*
import java.io.*
import java.util.*
import javax.sound.sampled.*
import javax.sound.sampled.AudioFormat

private val nativeSoundFormats = AudioFormats().register(
    ServiceLoader.load(com.soywiz.korau.format.AudioFormat::class.java).toList()
)

actual val nativeSoundProvider: NativeSoundProvider by lazy { AwtNativeSoundProvider() }

// AudioSystem.getMixerInfo()
val mixer by lazy { AudioSystem.getMixer(null) }

class AwtNativeSoundProvider : NativeSoundProvider() {
    override fun init() {
        // warming and preparing
        mixer.mixerInfo
        val af = AudioFormat(44100f, 16, 2, true, false)
        val info = DataLine.Info(SourceDataLine::class.java, af)
        val line = AudioSystem.getLine(info) as SourceDataLine
        line.open(af, 4096)
        line.start()
        line.write(ByteArray(4), 0, 4)
        line.drain()
        line.stop()
        line.close()
    }

    override fun createAudioStream(freq: Int): PlatformAudioOutput = JvmPlatformAudioOutput(freq)

    override suspend fun createSound(data: ByteArray, streaming: Boolean): NativeSound {
        val audioData = try {
            nativeSoundFormats.decode(data.openAsync()) ?: AudioData.DUMMY
        } catch (e: Throwable) {
            e.printStackTrace()
            AudioData.DUMMY
        }
        return AwtNativeSound(audioData, audioData.toWav()).init()
    }

    override suspend fun createSound(data: AudioData, formats: AudioFormats, streaming: Boolean): NativeSound {
        return AwtNativeSound(data, data.toWav())
    }
}

class AwtNativeSound(val audioData: AudioData, val data: ByteArray) : NativeSound() {
    override var length: TimeSpan = 0.milliseconds

    suspend fun init(): AwtNativeSound {
        executeInWorkerJVM {
            val sound = AudioSystem.getAudioInputStream(ByteArrayInputStream(data))
            length = (sound.frameLength * 1000.0 / sound.format.frameRate.toDouble()).toLong().milliseconds
        }
        return this
    }

    override suspend fun decode(): AudioData = audioData

    override fun play(): NativeSoundChannel {
        return object : NativeSoundChannel(this) {
            val clip = AudioSystem.getClip(mixer.mixerInfo)
            val jsound = AudioSystem.getAudioInputStream(ByteArrayInputStream(data))
            //val len = clip.microsecondLength.toDouble().microseconds
            val len = audioData.totalTime

            override val current: TimeSpan get() = clip.microsecondPosition.toDouble().microseconds
            override val total: TimeSpan get() = len
            var stopped = false
            //override val playing: Boolean get() = !stopped && current < total
            override val playing: Boolean get() = !stopped

            //override var pitch: Double = 1.0
            //    set(value) {
            //        field = value
            //        //(clip.getControl(FloatControl.Type.SAMPLE_RATE) as FloatControl).value = (audioData.rate * pitch).toFloat()
            //    }

            override fun stop() {
                clip.stop()
                stopped = true
            }

            init {
                if (len == 0.seconds) {
                    stopped = true
                } else {
                    clip.open(jsound)
                    clip.addLineListener(object : LineListener {
                        override fun update(event: LineEvent) {
                            when (event.type) {
                                LineEvent.Type.STOP, LineEvent.Type.CLOSE -> {
                                    event.line.close()
                                    clip.removeLineListener(this)
                                    stop()
                                }
                            }

                        }
                    })
                    clip.start()
                }
            }
        }
    }
}
