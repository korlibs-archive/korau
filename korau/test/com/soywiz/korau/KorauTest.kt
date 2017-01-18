package com.soywiz.korau

import com.soywiz.korau.sound.readSound
import com.soywiz.korio.async.sync
import com.soywiz.korio.vfs.ResourcesVfs
import org.junit.Test

class KorauTest {
	@Test
	fun name(): Unit = sync<Unit> {
		val sound = ResourcesVfs["wav1.wav"].readSound()
		//val p0 = spawn {
		//	sleep(0)
		//	sound.play()
		//}
//
		//val p1 = spawn {
		//	sleep(250)
		//	sound.play()
		//}
		//p0.await()
		//p1.await()
	}
}