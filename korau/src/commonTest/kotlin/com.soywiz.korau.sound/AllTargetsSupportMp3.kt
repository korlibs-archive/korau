package com.soywiz.korau.sound

import com.soywiz.korio.async.*
import com.soywiz.korio.file.std.*
import com.soywiz.korio.util.*
import kotlin.test.*

class AllTargetsSupportMp3 {
    @Test
    fun testDecode() = suspendTest {
        if (nativeSoundProvider.target == "android") return@suspendTest
        if (OS.isJsNodeJs) return@suspendTest

        val data = resourcesVfs["mp31.mp3"].readNativeSound().decode()
    }
}