package com.soywiz.korau.sound

import com.soywiz.klock.*
import com.soywiz.klock.min
import com.soywiz.kmem.*
import com.soywiz.korau.format.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.*
import kotlinx.cinterop.*
import kotlinx.coroutines.*
import platform.AVFoundation.*
import platform.CoreAudioTypes.*
import platform.Foundation.*
import platform.darwin.*
import kotlin.coroutines.*
import kotlin.math.*

actual val nativeSoundProvider: NativeSoundProvider get() = avFoundationNativeSoundProvider
val engine by lazy {
    appleInitAudio()
    AVAudioEngine()
}
val avFoundationNativeSoundProvider: AvFoundationNativeSoundProvider by lazy { AvFoundationNativeSoundProvider() }

expect fun appleInitAudio()

class AvFoundationNativeSoundProvider : NativeSoundProvider() {
    val engine = com.soywiz.korau.sound.engine
    val mainMixer = engine.mainMixerNode
    val output = engine.outputNode
    val outputFormat = output.inputFormatForBus(0.convert())
    val sampleRate = outputFormat.sampleRate.toFloat()
    init {
        engine.connect(mainMixer, to = output, format = outputFormat)
        memScoped {
            val error = alloc<ObjCObjectVar<NSError?>>().ptr
            val result = engine.startAndReturnError(error)
            println("engine.start: $result, $error")
        }
    }

    override val audioFormats: AudioFormats = AudioFormats(WAV, com.soywiz.korau.format.mp3.PureJavaMp3DecoderAudioFormat, NativeOggVorbisDecoderFormat)

    //override suspend fun createSound(data: ByteArray, streaming: Boolean, props: AudioDecodingProps): NativeSound = AVFoundationNativeSoundNoStream(CoroutineScope(coroutineContext), audioFormats.decode(data))

    internal val audioOutputs = mutableSetOf<AVFoundationPlatformAudioOutput>()
    override fun createAudioStream(coroutineContext: CoroutineContext, freq: Int): PlatformAudioOutput {
        return AVFoundationPlatformAudioOutput(this, coroutineContext, freq)
    }
}

class AVFoundationPlatformAudioOutput(val provider: AvFoundationNativeSoundProvider, coroutineContext: CoroutineContext, freq: Int) : PlatformAudioOutput(coroutineContext, freq) {
    init {
        provider.audioOutputs += this
    }
    val inputFormat = AVAudioFormat(
        commonFormat = AVAudioPCMFormatFloat32,
        sampleRate = freq.toDouble(),
        channels = 2.convert(),
        interleaved = false
    )
    val nchannels = 2
    val deque = AudioSamplesDeque(nchannels)

    val gen: (CPointer<BooleanVar>?, CPointer<AudioTimeStamp>?, AVAudioFrameCount, CPointer<AudioBufferList>?) -> platform.darwin.OSStatus = { _, _, frameCount, audioBufferList ->
        println("AVFoundationPlatformAudioOutput.gen")
        val channels = audioBufferList!!.pointed.mBuffers
        val nchannels = audioBufferList!!.pointed.mNumberBuffers
        val available = min(deque.availableRead, frameCount.toInt())
        for (c in 0 until nchannels.toInt()) {
            val channelData = channels[c].mData!!.reinterpret<FloatVar>()
            for (n in 0 until available) {
                channelData[n] = deque.readFloat(c)
            }
        }
        noErr.convert()
    }

    val srcNode: AVAudioSourceNode = AVAudioSourceNode(gen)

    init {
        println("AVFoundationPlatformAudioOutput")
    }

    var totalSamples = 0L

    override val availableSamples: Int get() = deque.availableRead

    override var pitch: Double = 1.0
        set(value) = run { field = value }.also { }
    override var volume: Double = 1.0
        set(value) = run { field = value }.also { srcNode.setVolume(value.toFloat() )}
    override var panning: Double = 0.0
        set(value) = run { field = value }.also { srcNode.setPan(value.toFloat() )}


    override suspend fun add(samples: AudioSamples, offset: Int, size: Int) {
        deque.write(samples, offset, size)
        totalSamples += samples.totalSamples
        println("AVFoundationPlatformAudioOutput.add")
    }


    override fun start() {
        println("AVFoundationPlatformAudioOutput.start")
        provider.engine.attachNode(srcNode)
        provider.engine.connect(srcNode, to = provider.mainMixer, format = inputFormat)
        //platform.CoreFoundation.CFRunLoopRun()
    }

    override fun stop() {
        println("AVFoundationPlatformAudioOutput.stop")
        provider.audioOutputs -= this
        provider.engine.detachNode(srcNode)
        provider.engine.disconnectNodeInput(srcNode)
    }

    override fun dispose() {
        println("AVFoundationPlatformAudioOutput.dispose")
        stop()
    }
}

/*
private fun ByteArray.toNSData(): NSData {
    val array = this
    return memScoped {
        array.usePinned { arrayPin ->
            NSData.dataWithBytes(arrayPin.startAddressOf, array.size.convert())
        }
    }
}

// https://ffainelli.github.io/openal-example/
class AVFoundationNativeSoundNoStream(val coroutineScope: CoroutineScope, val data: AudioData?) : NativeSound() {
    override suspend fun decode(): AudioData = data ?: AudioData.DUMMY

    override fun play(params: PlaybackParameters): NativeSoundChannel {
        val data = data ?: return DummyNativeSoundChannel(this)

        val player = memScoped {
            val error = alloc<ObjCObjectVar<NSError?>>().ptr
            AVAudioPlayer(data.toWav().toNSData(), error)
        }
        player.play()

        var stopped = false

        val channel = object : NativeSoundChannel(this) {
            override var volume: Double
                get() = run { player.volume.toDouble() }
                set(value) = run { player.setVolume(value.toFloat()) }
            override var pitch: Double
                get() = 1.0
                set(value) = run { }
            override var panning: Double
                get() = player.pan.toDouble()
                set(value) = run {
                    player.pan = value.toFloat()
                }

            override var current: TimeSpan
                get() = player.currentTime.seconds
                set(value) = run { }
            override val total: TimeSpan get() = data.totalTime
            override val playing: Boolean get() = player.playing

            override fun stop() {
                if (!stopped) {
                    stopped = true
                    player.stop()
                }
            }
        }
        coroutineScope.launchImmediately {
            try {
                while (channel.playing) {
                    //println("${channel.current}/${channel.total}")
                    delay(1L)
                }
            } finally {
                channel.stop()
            }
        }
        return channel

    }
}
*/
