package com.soywiz.korau.format

import com.soywiz.korio.async.sync
import com.soywiz.korio.vfs.ResourcesVfs
import org.junit.Assert
import org.junit.Test

class AudioFormatTest {
	@Test
	fun wav() = sync {
		Assert.assertEquals(
			"Info(lengthInMicroseconds=500000, channels=1)",
			ResourcesVfs["wav1.wav"].readSoundInfo().toString()
		)
		Assert.assertEquals(
			"Info(lengthInMicroseconds=500000, channels=1)",
			ResourcesVfs["wav2.wav"].readSoundInfo().toString()
		)
	}

	@Test
	fun ogg() = sync {
		Assert.assertEquals(
			"Info(lengthInMicroseconds=500000, channels=1)",
			ResourcesVfs["ogg1.ogg"].readSoundInfo().toString()
		)
	}

	@Test
	fun mp3() = sync {
		Assert.assertEquals(
			"Info(lengthInMicroseconds=546625, channels=1)",
			ResourcesVfs["mp31.mp3"].readSoundInfo().toString()
		)
	}
}