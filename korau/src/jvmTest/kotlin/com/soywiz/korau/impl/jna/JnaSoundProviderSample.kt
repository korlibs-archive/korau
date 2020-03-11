package com.soywiz.korau.impl.jna

import com.soywiz.klock.*
import com.soywiz.korau.format.*
import com.soywiz.korau.sound.*
import com.soywiz.korio.file.std.*
import kotlinx.coroutines.*

object JnaSoundProviderSample {
    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking {
            //val data = resourcesVfs["monkey_drama.mp3"].readNativeMusic()
            val group = NativeSoundChannelGroup(volume = 0.2)
            val info = resourcesVfs["mp31_joint_stereo_vbr.mp3"].readSoundInfo()
            println(info)
            //val data = resourcesVfs["mp31_joint_stereo_vbr.mp3"].readNativeMusic()
            val data = resourcesVfs["mp31_joint_stereo_vbr.mp3"].readNativeMusic()
            //val data = resourcesVfs["monkey_drama.mp3"].readNativeMusic()
            //val data = resourcesVfs["mp31_joint_stereo_vbr.mp3"].readNativeSound()

            //println(data.length)
            //val result = data.playForever().attachTo(group)
            val result = data.play(2.playbackTimes).attachTo(group)
            //group.volume = 0.2

            group.pitch = 1.5
            for (n in -10 .. +10) {
                group.panning = n.toDouble() / 10.0
                println(group.panning)
                com.soywiz.korio.async.delay(0.1.seconds)
            }
            println("Waiting...")
            group.await()
            println("Stop...")
            //com.soywiz.korio.async.delay(1.seconds)
            group.stop()
        }
    }
}
