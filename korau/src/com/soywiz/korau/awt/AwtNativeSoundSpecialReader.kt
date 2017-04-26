package com.soywiz.korau.awt

import com.soywiz.korau.sound.NativeSound
import com.soywiz.korio.vfs.LocalVfs
import com.soywiz.korio.vfs.Vfs
import com.soywiz.korio.vfs.VfsSpecialReader

class AwtNativeSoundSpecialReader : VfsSpecialReader<NativeSound>(NativeSound::class.java) {
	suspend override fun readSpecial(vfs: Vfs, path: String): NativeSound = when (vfs) {
		is LocalVfs -> AwtNativeSound(vfs[path].readBytes())
		else -> AwtNativeSound(vfs[path].readBytes())
	}
}