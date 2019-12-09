package com.soywiz.korau.sound.impl.jna

import com.soywiz.klock.*
import com.soywiz.korau.format.*
import com.soywiz.korau.sound.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.*
import com.soywiz.korio.file.std.*
import com.soywiz.korio.util.*
import com.soywiz.korio.util.encoding.*
import com.sun.jna.*
import kotlinx.coroutines.*
import java.io.*
import java.nio.*
import kotlin.coroutines.*

internal inline fun <T> runCatchingAl(block: () -> T): T? {
    val result = runCatching { block() }
    if (result.isFailure) {
        result.exceptionOrNull()?.printStackTrace()
    }
    return result.getOrNull()
}

@Suppress("unused")
interface AL : Library {
    fun alDopplerFactor(value: Float)
    fun alDopplerVelocity(value: Float)
    fun alSpeedOfSound(value: Float)
    fun alDistanceModel(distanceModel: Int)
    fun alEnable(capability: Int)
    fun alDisable(capability: Int)
    fun alIsEnabled(capability: Int): Boolean
    fun alGetString(param: Int): String
    fun alGetBooleanv(param: Int, values: BooleanArray)
    fun alGetIntegerv(param: Int, values: IntArray)
    fun alGetFloatv(param: Int, values: FloatArray)
    fun alGetDoublev(param: Int, values: DoubleArray)
    fun alGetBoolean(param: Int): Boolean
    fun alGetInteger(param: Int): Int
    fun alGetFloat(param: Int): Float
    fun alGetDouble(param: Int): Double
    fun alGetError(): Int
    fun alIsExtensionPresent(extname: String): Boolean
    fun alGetProcAddress(fname: String): Pointer
    fun alGetEnumValue(ename: String): Int
    fun alListenerf(param: Int, value: Float)
    fun alListener3f(param: Int, value1: Float, value2: Float, value3: Float)
    fun alListenerfv(param: Int, values: FloatArray)
    fun alListeneri(param: Int, value: Int)
    fun alListener3i(param: Int, value1: Int, value2: Int, value3: Int)
    fun alListeneriv(param: Int, values: IntArray)
    fun alGetListenerf(param: Int, value: FloatArray)
    fun alGetListener3f(param: Int, value1: FloatArray, value2: FloatArray, value3: FloatArray)
    fun alGetListenerfv(param: Int, values: FloatArray)
    fun alGetListeneri(param: Int, value: IntArray)
    fun alGetListener3i(param: Int, value1: IntArray, value2: IntArray, value3: IntArray)
    fun alGetListeneriv(param: Int, values: IntArray)
    fun alGenSources(n: Int, sources: IntArray)
    fun alDeleteSources(n: Int, sources: IntArray)
    fun alIsSource(source: Int): Boolean
    fun alSourcef(source: Int, param: Int, value: Float)
    fun alSource3f(source: Int, param: Int, value1: Float, value2: Float, value3: Float)
    fun alSourcefv(source: Int, param: Int, values: FloatArray)
    fun alSourcei(source: Int, param: Int, value: Int)
    fun alSource3i(source: Int, param: Int, value1: Int, value2: Int, value3: Int)
    fun alSourceiv(source: Int, param: Int, values: IntArray)
    fun alGetSourcef(source: Int, param: Int, value: FloatArray)
    fun alGetSource3f(source: Int, param: Int, value1: FloatArray, value2: FloatArray, value3: FloatArray)
    fun alGetSourcefv(source: Int, param: Int, values: FloatArray)
    fun alGetSourcei(source: Int, param: Int, value: IntArray)
    fun alGetSource3i(source: Int, param: Int, value1: IntArray, value2: IntArray, value3: IntArray)
    fun alGetSourceiv(source: Int, param: Int, values: IntArray)
    fun alSourcePlayv(n: Int, sources: IntArray)
    fun alSourceStopv(n: Int, sources: IntArray)
    fun alSourceRewindv(n: Int, sources: IntArray)
    fun alSourcePausev(n: Int, sources: IntArray)
    fun alSourcePlay(source: Int)
    fun alSourceStop(source: Int)
    fun alSourceRewind(source: Int)
    fun alSourcePause(source: Int)
    fun alSourceQueueBuffers(source: Int, nb: Int, buffers: IntArray)
    fun alSourceUnqueueBuffers(source: Int, nb: Int, buffers: IntArray)
    fun alGenBuffers(n: Int, buffers: IntArray)
    fun alDeleteBuffers(n: Int, buffers: IntArray)
    fun alIsBuffer(buffer: Int): Boolean
    fun alBufferData(buffer: Int, format: Int, data: Buffer?, size: Int, freq: Int)
    fun alBufferf(buffer: Int, param: Int, value: Float)
    fun alBuffer3f(buffer: Int, param: Int, value1: Float, value2: Float, value3: Float)
    fun alBufferfv(buffer: Int, param: Int, values: FloatArray)
    fun alBufferi(buffer: Int, param: Int, value: Int)
    fun alBuffer3i(buffer: Int, param: Int, value1: Int, value2: Int, value3: Int)
    fun alBufferiv(buffer: Int, param: Int, values: IntArray)
    fun alGetBufferf(buffer: Int, param: Int, value: FloatArray)
    fun alGetBuffer3f(buffer: Int, param: Int, value1: FloatArray, value2: FloatArray, value3: FloatArray)
    fun alGetBufferfv(buffer: Int, param: Int, values: FloatArray)
    fun alGetBufferi(buffer: Int, param: Int, value: IntArray)
    fun alGetBuffer3i(buffer: Int, param: Int, value1: IntArray, value2: IntArray, value3: IntArray)
    fun alGetBufferiv(buffer: Int, param: Int, values: IntArray)

    companion object {
        const val AL_NONE = 0
        const val AL_FALSE = 0
        const val AL_TRUE = 1
        const val AL_SOURCE_RELATIVE = 0x202
        const val AL_CONE_INNER_ANGLE = 0x1001
        const val AL_CONE_OUTER_ANGLE = 0x1002
        const val AL_PITCH = 0x1003
        const val AL_POSITION = 0x1004
        const val AL_DIRECTION = 0x1005
        const val AL_VELOCITY = 0x1006
        const val AL_LOOPING = 0x1007
        const val AL_BUFFER = 0x1009
        const val AL_GAIN = 0x100A
        const val AL_MIN_GAIN = 0x100D
        const val AL_MAX_GAIN = 0x100E
        const val AL_ORIENTATION = 0x100F
        const val AL_SOURCE_STATE = 0x1010
        const val AL_INITIAL = 0x1011
        const val AL_PLAYING = 0x1012
        const val AL_PAUSED = 0x1013
        const val AL_STOPPED = 0x1014
        const val AL_BUFFERS_QUEUED = 0x1015
        const val AL_BUFFERS_PROCESSED = 0x1016
        const val AL_REFERENCE_DISTANCE = 0x1020
        const val AL_ROLLOFF_FACTOR = 0x1021
        const val AL_CONE_OUTER_GAIN = 0x1022
        const val AL_MAX_DISTANCE = 0x1023
        const val AL_SEC_OFFSET = 0x1024
        const val AL_SAMPLE_OFFSET = 0x1025
        const val AL_BYTE_OFFSET = 0x1026
        const val AL_SOURCE_TYPE = 0x1027
        const val AL_STATIC = 0x1028
        const val AL_STREAMING = 0x1029
        const val AL_UNDETERMINED = 0x1030
        const val AL_FORMAT_MONO8 = 0x1100
        const val AL_FORMAT_MONO16 = 0x1101
        const val AL_FORMAT_STEREO8 = 0x1102
        const val AL_FORMAT_STEREO16 = 0x1103
        const val AL_FREQUENCY = 0x2001
        const val AL_BITS = 0x2002
        const val AL_CHANNELS = 0x2003
        const val AL_SIZE = 0x2004
        const val AL_UNUSED = 0x2010
        const val AL_PENDING = 0x2011
        const val AL_PROCESSED = 0x2012
        const val AL_NO_ERROR = 0
        const val AL_INVALID_NAME = 0xA001
        const val AL_INVALID_ENUM = 0xA002
        const val AL_INVALID_VALUE = 0xA003
        const val AL_INVALID_OPERATION = 0xA004
        const val AL_OUT_OF_MEMORY = 0xA005
        const val AL_VENDOR = 0xB001
        const val AL_VERSION = 0xB002
        const val AL_RENDERER = 0xB003
        const val AL_EXTENSIONS = 0xB004
        const val AL_DOPPLER_FACTOR = 0xC000
        const val AL_DOPPLER_VELOCITY = 0xC001
        const val AL_SPEED_OF_SOUND = 0xC003
        const val AL_DISTANCE_MODEL = 0xD000
        const val AL_INVERSE_DISTANCE = 0xD001
        const val AL_INVERSE_DISTANCE_CLAMPED = 0xD002
        const val AL_LINEAR_DISTANCE = 0xD003
        const val AL_LINEAR_DISTANCE_CLAMPED = 0xD004
        const val AL_EXPONENT_DISTANCE = 0xD005
        const val AL_EXPONENT_DISTANCE_CLAMPED = 0xD006
    }
}

@Suppress("unused")
interface ALC : Library {
    fun alcCreateContext(device: Pointer, attrlist: IntArray?): Pointer;
    fun alcMakeContextCurrent(context: Pointer): Boolean
    fun alcProcessContext(context: Pointer);
    fun alcSuspendContext(context: Pointer);
    fun alcDestroyContext(context: Pointer);
    fun alcGetCurrentContext(): Pointer
    fun alcGetContextsDevice(context: Pointer): Pointer
    fun alcOpenDevice(devicename: String?): Pointer
    fun alcCloseDevice(device: Pointer): Boolean
    fun alcGetError(device: Pointer): Int
    fun alcIsExtensionPresent(device: Pointer, extname: String): Boolean
    fun alcGetProcAddress(device: Pointer, funcname: String): Pointer
    fun alcGetEnumValue(device: Pointer, enumname: String): Int
    fun alcGetString(device: Pointer, param: Int): String
    fun alcGetIntegerv(device: Pointer, param: Int, size: Int, values: IntArray);
    fun alcCaptureOpenDevice(devicename: String, frequency: Int, format: Int, buffersize: Int): Pointer
    fun alcCaptureCloseDevice(device: Pointer): Boolean
    fun alcCaptureStart(device: Pointer);
    fun alcCaptureStop(device: Pointer);
    fun alcCaptureSamples(device: Pointer, buffer: Buffer, samples: Int);

    companion object {
        const val ALC_FALSE = 0
        const val ALC_TRUE = 1
        const val ALC_FREQUENCY = 0x1007
        const val ALC_REFRESH = 0x1008
        const val ALC_SYNC = 0x1009
        const val ALC_MONO_SOURCES = 0x1010
        const val ALC_STEREO_SOURCES = 0x1011
        const val ALC_NO_ERROR = 0
        const val ALC_INVALID_DEVICE = 0xA001
        const val ALC_INVALID_CONTEXT = 0xA002
        const val ALC_INVALID_ENUM = 0xA003
        const val ALC_INVALID_VALUE = 0xA004
        const val ALC_OUT_OF_MEMORY = 0xA005
        const val ALC_MAJOR_VERSION = 0x1000
        const val ALC_MINOR_VERSION = 0x1001
        const val ALC_ATTRIBUTES_SIZE = 0x1002
        const val ALC_ALL_ATTRIBUTES = 0x1003
        const val ALC_DEFAULT_DEVICE_SPECIFIER = 0x1004
        const val ALC_DEVICE_SPECIFIER = 0x1005
        const val ALC_EXTENSIONS = 0x1006
        const val ALC_EXT_CAPTURE = 1
        const val ALC_CAPTURE_DEVICE_SPECIFIER = 0x310
        const val ALC_CAPTURE_DEFAULT_DEVICE_SPECIFIER = 0x311
        const val ALC_CAPTURE_SAMPLES = 0x312
        const val ALC_ENUMERATE_ALL_EXT = 1
        const val ALC_DEFAULT_ALL_DEVICES_SPECIFIER = 0x1012
        const val ALC_ALL_DEVICES_SPECIFIER = 0x1013
    }
}

private val arch by lazy { System.getProperty("os.arch").toLowerCase() }
private val alClassLoader by lazy { JnaOpenALNativeSoundProvider::class.java.classLoader }
private fun getNativeFile(path: String): ByteArray = alClassLoader.getResource(path)?.readBytes() ?: error("Can't find '$path'")
private fun getNativeFileLocalPath(path: String): String {
    val tempFile = File.createTempFile("libopenal_", ".${File(path).extension}")
    tempFile.writeBytes(getNativeFile(path))
    return tempFile.absolutePath
}

val nativeOpenALLibraryPath: String? by lazy {
    println("Initializing OpenAL: arch=$arch, OS.rawName=${OS.rawName}")
    val result = when {
        OS.isMac -> {
            //getNativeFileLocalPath("natives/macosx64/libopenal.dylib")
            "OpenAL" // Mac already includes the OpenAL library
        }
        OS.isLinux -> {
            when {
                arch.contains("arm") -> getNativeFileLocalPath("natives/linuxarm/libopenal.so")
                arch.contains("64") -> getNativeFileLocalPath("natives/linuxx64/libopenal.so")
                else -> getNativeFileLocalPath("natives/linuxx86/libopenal.so")
            }
        }
        OS.isWindows -> {
            when {
                arch.contains("64") -> getNativeFileLocalPath("natives/winx64/soft_oal.dll")
                else -> getNativeFileLocalPath("natives/winx86/soft_oal.dll")
            }
        }
        else -> {
            println("  - Unknown/Unsupported OS")
            null
        }
    }
    println("   -> $result")
    result
}

val al: AL? by lazy {
    runCatchingAl {
        Native.load(nativeOpenALLibraryPath, AL::class.java)
    }
}

val alc: ALC? by lazy {
    runCatchingAl {
        Native.load(nativeOpenALLibraryPath, ALC::class.java)
    }
}

class JnaOpenALNativeSoundProvider : NativeSoundProvider() {
    val device = alc?.alcOpenDevice(null)
    val context = device?.let { alc?.alcCreateContext(device, null) }

    init {
        doInit()
    }

    fun makeCurrent() {
        context?.let { alc?.alcMakeContextCurrent(it) }

    }

    private fun doInit() {
        //println("ALut.alutInit: ${Thread.currentThread()}")
        makeCurrent()

        al?.alListener3f(AL.AL_POSITION, 0f, 0f, 1.0f)
        checkAlErrors()
        al?.alListener3f(AL.AL_VELOCITY, 0f, 0f, 0f)
        checkAlErrors()
        //al?.alListenerfv(AL.AL_ORIENTATION, floatArrayOf(0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f), 0)
        al?.alListenerfv(AL.AL_ORIENTATION, floatArrayOf(0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f))
        checkAlErrors()
    }

    override suspend fun createSound(data: ByteArray, streaming: Boolean): NativeSound =
        OpenALNativeSoundNoStream(this, coroutineContext, nativeAudioFormats.decode(data))

    override suspend fun createSound(vfs: Vfs, path: String, streaming: Boolean): NativeSound {
        return super.createSound(vfs, path, streaming)
    }

    override suspend fun createSound(data: AudioData, formats: AudioFormats, streaming: Boolean): NativeSound {
        return super.createSound(data, formats, streaming)
    }
}

// https://ffainelli.github.io/openal-example/
class OpenALNativeSoundNoStream(val provider: JnaOpenALNativeSoundProvider, val coroutineContext: CoroutineContext, val data: AudioData?) : NativeSound() {
    override suspend fun decode(): AudioData = data ?: AudioData.DUMMY

    override fun play(): NativeSoundChannel {
        //if (openalNativeSoundProvider.device == null || openalNativeSoundProvider.context == null) return DummyNativeSoundChannel(this, data)
        //println("OpenALNativeSoundNoStream.play : $data")
        val data = data ?: return DummyNativeSoundChannel(this)

        provider.makeCurrent()

        val buffer = alGenBuffer()
        alBufferData(buffer, data)

        val source = alGenSource()
        al?.alSourcef(source, AL.AL_PITCH, 1f)
        al?.alSourcef(source, AL.AL_GAIN, 1f)
        al?.alSource3f(source, AL.AL_POSITION, 0f, 0f, 0f)
        al?.alSource3f(source, AL.AL_VELOCITY, 0f, 0f, 0f)
        al?.alSourcei(source, AL.AL_LOOPING, AL.AL_FALSE)
        al?.alSourcei(source, AL.AL_BUFFER, buffer)
        checkAlErrors()

        al?.alSourcePlay(source)
        checkAlErrors()

        var stopped = false

        val channel = object : NativeSoundChannel(this) {
            val totalSamples get() = data.totalSamples
            val currentSampleOffset get() = alGetSourcei(source, AL.AL_SAMPLE_OFFSET)

            override var volume: Double
                get() = run { alGetSourcef(source, AL.AL_GAIN).toDouble() }
                set(value) = run { al?.alSourcef(source, AL.AL_GAIN, value.toFloat()) }
            override var pitch: Double
                get() = run { alGetSourcef(source, AL.AL_PITCH).toDouble() }
                set(value) = run { al?.alSourcef(source, AL.AL_PITCH, value.toFloat()) }
            override var panning: Double = 0.0
                set(value) = run {
                    field = value
                    al?.alSource3f(source, AL.AL_POSITION, panning.toFloat(), 0f, 0f)
                }

            override val current: TimeSpan get() = data.timeAtSample(currentSampleOffset)
            override val total: TimeSpan get() = data.totalTime
            override val playing: Boolean
                get() {
                    val result = alGetSourceState(source) == AL.AL_PLAYING
                    checkAlErrors()
                    return result
                }

            override fun stop() {
                if (!stopped) {
                    stopped = true
                    alDeleteSource(source)
                    alDeleteBuffer(buffer)
                }
            }
        }
        launchImmediately(coroutineContext[ContinuationInterceptor] ?: coroutineContext) {
            try {
                do {
                    delay(1L)
                } while (channel.playing)
            } catch (e: Throwable) {
                e.printStackTrace()
            } finally {
                channel.stop()
            }
        }
        return channel
    }
}

private val tempF = FloatArray(1)
private val tempI = IntArray(1)
//private fun alGetSourcef(source: Int, param: Int): Float = tempF.apply { al?.alGetSourcef(source, param, this, 0) }[0]
//private fun alGetSourcei(source: Int, param: Int): Int = tempI.apply { al?.alGetSourcei(source, param, this, 0) }[0]

private fun alGetSourcef(source: Int, param: Int): Float = tempF.apply { al?.alGetSourcef(source, param, this) }[0]
private fun alGetSourcei(source: Int, param: Int): Int = tempI.apply { al?.alGetSourcei(source, param, this) }[0]
private fun alGetSourceState(source: Int): Int = alGetSourcei(source, AL.AL_SOURCE_STATE)

private fun alBufferData(buffer: Int, data: AudioData) {
    val samples = data.samplesInterleaved.data

    val bufferData = ShortBuffer.wrap(samples)
    //val bufferData = ByteBuffer.allocateDirect(samples.size * 2).order(ByteOrder.nativeOrder())
    //bufferData.asShortBuffer().put(samples)

    al?.alBufferData(
        buffer,
        if (data.channels == 1) AL.AL_FORMAT_MONO16 else AL.AL_FORMAT_STEREO16,
        if (samples.isNotEmpty()) bufferData else null,
        samples.size * 2,
        data.rate
    )
    checkAlErrors()
}

private fun alGenBuffer(): Int = tempI.apply { al?.alGenBuffers(1, this) }[0]
private fun alGenSource(): Int = tempI.apply { al?.alGenSources(1, this) }[0]
private fun alDeleteBuffer(buffer: Int): Unit = run { al?.alDeleteBuffers(1, tempI.also { it[0] = buffer }) }
private fun alDeleteSource(buffer: Int): Unit = run { al?.alDeleteSources(1, tempI.also { it[0] = buffer }) }

//private fun alGenBuffer(): Int = tempI.apply { al?.alGenBuffers(1, this, 0) }[0]
//private fun alGenSource(): Int = tempI.apply { al?.alGenSources(1, this, 0) }[0]
//private fun alDeleteBuffer(buffer: Int): Unit = run { al?.alDeleteBuffers(1, tempI.also { it[0] = buffer }, 0) }
//private fun alDeleteSource(buffer: Int): Unit = run { al?.alDeleteSources(1, tempI.also { it[0] = buffer }, 0) }

/*
val alc by lazy {
    ALFactory.getALC().also { alc ->
        //val error = alc.alcGetError()
        //if (error != AL.AL_NO_ERROR) error("Error initializing OpenAL ${error.shex}")
    } }

private val device by lazy { alc.alcOpenDevice(null).also {
    println("alc.alcOpenDevice: $it")
} }
private val context by lazy { alc.alcCreateContext(device, null).also {
    println("alc.alcCreateContext: $it with device=$device")
} }
*/

fun checkAlErrors() {
//    val error = al.alGetError()
//    if (error != AL.AL_NO_ERROR) error("OpenAL error ${error.shex}")
}
