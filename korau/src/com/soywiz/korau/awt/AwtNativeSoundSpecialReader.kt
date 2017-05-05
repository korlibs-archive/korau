package com.soywiz.korau.awt

import com.soywiz.korau.format.AudioData
import com.soywiz.korau.format.readAudioData
import com.soywiz.korau.format.toNativeSound
import com.soywiz.korau.sound.NativeSound
import com.soywiz.korau.sound.nativeSoundProvider
import com.soywiz.korau.sound.readNativeSound
import com.soywiz.korio.vfs.LocalVfs
import com.soywiz.korio.vfs.Vfs
import com.soywiz.korio.vfs.VfsSpecialReader

class AwtNativeSoundSpecialReader : VfsSpecialReader<NativeSound>(NativeSound::class.java) {
	suspend override fun readSpecial(vfs: Vfs, path: String): NativeSound = try {
		when (vfs) {
			is LocalVfs -> nativeSoundProvider.createSound(vfs[path])
			else -> nativeSoundProvider.createSound(vfs[path])
		}
	} catch (e: Throwable) {
		e.printStackTrace()
		nativeSoundProvider.createSound(AudioData(44100, 2, shortArrayOf()))
	}
}