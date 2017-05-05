package com.soywiz.korau

import com.soywiz.korau.format.readAudioData
import com.soywiz.korio.async.syncTest
import com.soywiz.korio.vfs.ResourcesVfs
import org.junit.Test

class KorauTest {
	@Test
	fun name(): Unit = syncTest {
		val sound = ResourcesVfs["wav1.wav"].readAudioData()
		//sleep(0)
		//sound.play()
	}
}