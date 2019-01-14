package com.soywiz.korau.sound

import com.soywiz.klock.TimeSpan
import com.soywiz.klock.microseconds
import com.soywiz.klock.milliseconds
import com.soywiz.korau.format.*
import com.soywiz.korau.sound.internal.jvm.mp3.*
import com.soywiz.korio.async.*
import com.soywiz.korio.stream.openAsync
import java.io.ByteArrayInputStream
import javax.sound.sampled.*
import javax.sound.sampled.AudioFormat

private val nativeSoundFormats = AudioFormats().register(
    WAV, MP3Decoder
)

actual val nativeSoundProvider: NativeSoundProvider by lazy { AwtNativeSoundProvider() }

class AwtNativeSoundProvider : NativeSoundProvider() {
    override fun init() {
        AudioSystem.getMixerInfo()

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

    override fun createAudioStream(freq: Int): NativeAudioStream = JvmNativeAudioStream(freq)

    override suspend fun createSound(data: ByteArray, streaming: Boolean): NativeSound {
        val data = try {
            nativeSoundFormats.decode(data.openAsync()) ?: AudioData(44100, 2, shortArrayOf())
        } catch (e: Throwable) {
            e.printStackTrace()
            AudioData(44100, 2, shortArrayOf())
        }
        return AwtNativeSound(data, data.toWav()).init()
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
            val sound2 = AudioSystem.getAudioInputStream(ByteArrayInputStream(data))
            val info = DataLine.Info(Clip::class.java, sound2.format)
            val clip = AudioSystem.getLine(info) as Clip
            val len = clip.microsecondLength.toDouble().microseconds
            override val current: TimeSpan get() = clip.microsecondPosition.toDouble().microseconds
            override val total: TimeSpan get() = len
            override var playing: Boolean = true

            override fun stop() {
                clip.stop()
                playing = false
            }

            init {
                clip.open(sound2)
                clip.addLineListener(MyLineListener(clip) {
                    stop()
                })
                clip.start()
            }
        }
    }

    private class MyLineListener(val clip: Clip, val complete: () -> Unit) : LineListener {
        override fun update(event: LineEvent) {
            when (event.type) {
                LineEvent.Type.STOP, LineEvent.Type.CLOSE -> {
                    event.line.close()
                    clip.removeLineListener(this)
                    complete()
                }
            }
        }
    }
}