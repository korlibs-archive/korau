package com.soywiz.korau.sound

import com.soywiz.korio.async.*
import com.soywiz.korio.file.std.*
import com.soywiz.korio.util.*
import kotlin.test.*

class AllTargetsSupportMp3 {
    @Test
    fun testDecode() = suspendTest({ !OS.isJs && !OS.isAndroid }) {
        val data = resourcesVfs["mp31.mp3"].readSound().decode()
    }
}
