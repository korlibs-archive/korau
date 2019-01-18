/*
 *      LAME MP3 encoding engine
 *
 *      Copyright (c) 1999-2000 Mark Taylor
 *      Copyright (c) 2000-2005 Takehiro Tominaga
 *      Copyright (c) 2000-2005 Robert Hegemann
 *      Copyright (c) 2000-2005 Gabriel Bouvigne
 *      Copyright (c) 2000-2004 Alexander Leidinger
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

/* $Id: Lame.java,v 1.44 2012/03/23 10:02:29 kenchis Exp $ */

package com.soywiz.korau.format.mp3.lame

import com.soywiz.kmem.*
import com.soywiz.korio.lang.*
import com.soywiz.korio.stream.*
import kotlin.math.*

class Lame(val warningProcessor: ((String) -> Unit)?) {
	val flags = LameGlobalFlags()
	val vbr = VBRTag()
	val parser = Parse()
	val intf = Interface(vbr, warningProcessor)
	val mpg = MPGLib(intf)
	val audio = GetAudio(parser, mpg)
}

class Common(val warningProcessor: ((String) -> Unit)?) {
	companion object {
		val tabsel_123 = arrayOf(
			arrayOf(
				intArrayOf(0, 32, 64, 96, 128, 160, 192, 224, 256, 288, 320, 352, 384, 416, 448),
				intArrayOf(0, 32, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 256, 320, 384),
				intArrayOf(0, 32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 256, 320)
			),
			arrayOf(
				intArrayOf(0, 32, 48, 56, 64, 80, 96, 112, 128, 144, 160, 176, 192, 224, 256),
				intArrayOf(0, 8, 16, 24, 32, 40, 48, 56, 64, 80, 96, 112, 128, 144, 160),
				intArrayOf(0, 8, 16, 24, 32, 40, 48, 56, 64, 80, 96, 112, 128, 144, 160)
			)
		)

		val freqs = intArrayOf(44100, 48000, 32000, 22050, 24000, 16000, 11025, 12000, 8000)
		private val MAX_INPUT_FRAMESIZE = 4096
	}

	var muls = Array(27) { FloatArray(64) }

	fun head_check(head: Long, check_layer: Int): Boolean {
		/* bits 13-14 = layer 3 */
		val nLayer = (4 - (head shr 17 and 3)).toInt()

		if (head and 0xffe00000L != 0xffe00000L) return false /* syncword */
		if (nLayer == 4) return false
		if (check_layer > 0 && nLayer != check_layer) return false
		if (head shr 12 and 0xf == 0xfL) return false/* bits 16,17,18,19 = 1111 invalid bitrate */
		if (head shr 10 and 0x3 == 0x3L) return false /* bits 20,21 = 11 invalid sampling freq */
		if (head and 0x3 == 0x2L) return false/* invalid emphasis */

		return true
	}

	/**
	 * decode a header and write the information into the frame structure
	 */
	fun decode_header(fr: Frame, newhead: Long): Int {

		if (newhead and (1 shl 20) != 0L) {
			fr.lsf = if (newhead and (1 shl 19) != 0L) 0x0 else 0x1
			fr.mpeg25 = false
		} else {
			fr.lsf = 1
			fr.mpeg25 = true
		}

		fr.lay = (4 - (newhead shr 17 and 3)).toInt()
		if (newhead shr 10 and 0x3 == 0x3L) throw RuntimeException("Stream error")
		if (fr.mpeg25) {
			fr.sampling_frequency = (6 + (newhead shr 10 and 0x3)).toInt()
		} else {
			fr.sampling_frequency = ((newhead shr 10 and 0x3) + fr.lsf * 3).toInt()
		}

		fr.error_protection = newhead shr 16 and 0x1 == 0L

		if (fr.mpeg25) fr.bitrate_index = (newhead shr 12 and 0xf).toInt() /* allow Bitrate change for 2.5 ... */

		fr.bitrate_index = (newhead shr 12 and 0xf).toInt()
		fr.padding = (newhead shr 9 and 0x1).toInt()
		fr.extension = (newhead shr 8 and 0x1).toInt()
		fr.mode = (newhead shr 6 and 0x3).toInt()
		fr.mode_ext = (newhead shr 4 and 0x3).toInt()
		fr.copyright = (newhead shr 3 and 0x1).toInt()
		fr.original = (newhead shr 2 and 0x1).toInt()
		fr.emphasis = (newhead and 0x3).toInt()

		fr.stereo = if (fr.mode == MPG123.MPG_MD_MONO) 1 else 2

		when (fr.lay) {
			1 -> {
				fr.framesize = tabsel_123[fr.lsf][0][fr.bitrate_index] * 12000
				fr.framesize /= freqs[fr.sampling_frequency]
				fr.framesize = (fr.framesize + fr.padding shl 2) - 4
				fr.down_sample = 0
				fr.down_sample_sblimit = MPG123.SBLIMIT shr fr.down_sample
			}

			2 -> {
				fr.framesize = tabsel_123[fr.lsf][1][fr.bitrate_index] * 144000
				fr.framesize /= freqs[fr.sampling_frequency]
				fr.framesize += fr.padding - 4
				fr.down_sample = 0
				fr.down_sample_sblimit = MPG123.SBLIMIT shr fr.down_sample
			}

			3 -> {
				if (fr.framesize > MAX_INPUT_FRAMESIZE) {
					warningProcessor?.invoke("Frame size too big.")
					fr.framesize = MAX_INPUT_FRAMESIZE
					return 0
				}

				if (fr.bitrate_index == 0)
					fr.framesize = 0
				else {
					fr.framesize = tabsel_123[fr.lsf][2][fr.bitrate_index] * 144000
					fr.framesize /= freqs[fr.sampling_frequency] shl fr.lsf
					fr.framesize = fr.framesize + fr.padding - 4
				}
			}
			else -> {
				warningProcessor?.invoke("Sorry, layer ${fr.lay} not supported")
				return 0
			}
		}
		/* print_header(fr); */

		return 1
	}

	fun getbits(mp: MPGLib.mpstr_tag, number_of_bits: Int): Int {
		var rval: Long

		if (number_of_bits <= 0 || null == mp.wordpointer) return 0


		rval = (mp.wordpointer[mp.wordpointerPos + 0].unsigned).toLong()
		rval = rval shl 8
		rval = rval or (mp.wordpointer[mp.wordpointerPos + 1].unsigned).toLong()
		rval = rval shl 8
		rval = rval or (mp.wordpointer[mp.wordpointerPos + 2].unsigned).toLong()
		rval = (rval shl mp.bitindex)
		rval = rval and 0xffffffL

		mp.bitindex += number_of_bits

		rval = rval shr (24 - number_of_bits)

		mp.wordpointerPos += mp.bitindex shr 3
		mp.bitindex = mp.bitindex and 7

		return rval.toInt()
	}

	fun getbits_fast(mp: MPGLib.mpstr_tag, number_of_bits: Int): Int {
		var rval: Long

		rval = (mp.wordpointer[mp.wordpointerPos + 0].unsigned).toLong()
		rval = rval shl 8
		rval = rval or (mp.wordpointer[mp.wordpointerPos + 1].unsigned).toLong()
		rval = rval shl mp.bitindex
		rval = rval and 0xffffL
		mp.bitindex += number_of_bits

		rval = rval shr (16 - number_of_bits)

		mp.wordpointerPos += mp.bitindex shr 3
		mp.bitindex = mp.bitindex and 7

		return rval.toInt()
	}

	fun set_pointer(mp: MPGLib.mpstr_tag, backstep: Int): Int {
		if (mp.fsizeold < 0 && backstep > 0) {
			warningProcessor?.invoke("hip: Can't step back $backstep bytes!")
			return MPGLib.MP3_ERR
		}
		val bsbufold = mp.bsspace[1 - mp.bsnum]
		val bsbufoldPos = 512
		mp.wordpointerPos -= backstep
		if (backstep != 0)
			arraycopy(bsbufold, bsbufoldPos + mp.fsizeold - backstep, mp.wordpointer, mp.wordpointerPos, backstep)
		mp.bitindex = 0
		return MPGLib.MP3_OK
	}
}

class DCT64 {

	fun dct64_1(
		out0: FloatArray, out0Pos: Int, out1: FloatArray, out1Pos: Int,
		b1: FloatArray, b2: Int, samples: FloatArray, samplesPos: Int, pnts: Array<FloatArray>
	) {
		run {
			val costab = pnts[0]

			//for (int n = 0; n < 16; n++) {
			//    b1[n] = samples[samplesPos + n] + samples[samplesPos + 0x1F - n];
			//    b1[0x1F - n] = (samples[samplesPos + n] - samples[samplesPos + 0x1F - n]) * costab[n];
			//}

			b1[0x00] = samples[samplesPos + 0x00] + samples[samplesPos + 0x1F]
			b1[0x1F] = (samples[samplesPos + 0x00] - samples[samplesPos + 0x1F]) * costab[0x0]

			b1[0x01] = samples[samplesPos + 0x01] + samples[samplesPos + 0x1E]
			b1[0x1E] = (samples[samplesPos + 0x01] - samples[samplesPos + 0x1E]) * costab[0x1]

			b1[0x02] = samples[samplesPos + 0x02] + samples[samplesPos + 0x1D]
			b1[0x1D] = (samples[samplesPos + 0x02] - samples[samplesPos + 0x1D]) * costab[0x2]

			b1[0x03] = samples[samplesPos + 0x03] + samples[samplesPos + 0x1C]
			b1[0x1C] = (samples[samplesPos + 0x03] - samples[samplesPos + 0x1C]) * costab[0x3]

			b1[0x04] = samples[samplesPos + 0x04] + samples[samplesPos + 0x1B]
			b1[0x1B] = (samples[samplesPos + 0x04] - samples[samplesPos + 0x1B]) * costab[0x4]

			b1[0x05] = samples[samplesPos + 0x05] + samples[samplesPos + 0x1A]
			b1[0x1A] = (samples[samplesPos + 0x05] - samples[samplesPos + 0x1A]) * costab[0x5]

			b1[0x06] = samples[samplesPos + 0x06] + samples[samplesPos + 0x19]
			b1[0x19] = (samples[samplesPos + 0x06] - samples[samplesPos + 0x19]) * costab[0x6]

			b1[0x07] = samples[samplesPos + 0x07] + samples[samplesPos + 0x18]
			b1[0x18] = (samples[samplesPos + 0x07] - samples[samplesPos + 0x18]) * costab[0x7]

			b1[0x08] = samples[samplesPos + 0x08] + samples[samplesPos + 0x17]
			b1[0x17] = (samples[samplesPos + 0x08] - samples[samplesPos + 0x17]) * costab[0x8]

			b1[0x09] = samples[samplesPos + 0x09] + samples[samplesPos + 0x16]
			b1[0x16] = (samples[samplesPos + 0x09] - samples[samplesPos + 0x16]) * costab[0x9]

			b1[0x0A] = samples[samplesPos + 0x0A] + samples[samplesPos + 0x15]
			b1[0x15] = (samples[samplesPos + 0x0A] - samples[samplesPos + 0x15]) * costab[0xA]

			b1[0x0B] = samples[samplesPos + 0x0B] + samples[samplesPos + 0x14]
			b1[0x14] = (samples[samplesPos + 0x0B] - samples[samplesPos + 0x14]) * costab[0xB]

			b1[0x0C] = samples[samplesPos + 0x0C] + samples[samplesPos + 0x13]
			b1[0x13] = (samples[samplesPos + 0x0C] - samples[samplesPos + 0x13]) * costab[0xC]

			b1[0x0D] = samples[samplesPos + 0x0D] + samples[samplesPos + 0x12]
			b1[0x12] = (samples[samplesPos + 0x0D] - samples[samplesPos + 0x12]) * costab[0xD]

			b1[0x0E] = samples[samplesPos + 0x0E] + samples[samplesPos + 0x11]
			b1[0x11] = (samples[samplesPos + 0x0E] - samples[samplesPos + 0x11]) * costab[0xE]

			b1[0x0F] = samples[samplesPos + 0x0F] + samples[samplesPos + 0x10]
			b1[0x10] = (samples[samplesPos + 0x0F] - samples[samplesPos + 0x10]) * costab[0xF]
		}

		run {
			val costab = pnts[1]

			b1[b2 + 0x00] = b1[0x00] + b1[0x0F]
			b1[b2 + 0x0F] = (b1[0x00] - b1[0x0F]) * costab[0]
			b1[b2 + 0x01] = b1[0x01] + b1[0x0E]
			b1[b2 + 0x0E] = (b1[0x01] - b1[0x0E]) * costab[1]
			b1[b2 + 0x02] = b1[0x02] + b1[0x0D]
			b1[b2 + 0x0D] = (b1[0x02] - b1[0x0D]) * costab[2]
			b1[b2 + 0x03] = b1[0x03] + b1[0x0C]
			b1[b2 + 0x0C] = (b1[0x03] - b1[0x0C]) * costab[3]
			b1[b2 + 0x04] = b1[0x04] + b1[0x0B]
			b1[b2 + 0x0B] = (b1[0x04] - b1[0x0B]) * costab[4]
			b1[b2 + 0x05] = b1[0x05] + b1[0x0A]
			b1[b2 + 0x0A] = (b1[0x05] - b1[0x0A]) * costab[5]
			b1[b2 + 0x06] = b1[0x06] + b1[0x09]
			b1[b2 + 0x09] = (b1[0x06] - b1[0x09]) * costab[6]
			b1[b2 + 0x07] = b1[0x07] + b1[0x08]
			b1[b2 + 0x08] = (b1[0x07] - b1[0x08]) * costab[7]

			b1[b2 + 0x10] = b1[0x10] + b1[0x1F]
			b1[b2 + 0x1F] = (b1[0x1F] - b1[0x10]) * costab[0]
			b1[b2 + 0x11] = b1[0x11] + b1[0x1E]
			b1[b2 + 0x1E] = (b1[0x1E] - b1[0x11]) * costab[1]
			b1[b2 + 0x12] = b1[0x12] + b1[0x1D]
			b1[b2 + 0x1D] = (b1[0x1D] - b1[0x12]) * costab[2]
			b1[b2 + 0x13] = b1[0x13] + b1[0x1C]
			b1[b2 + 0x1C] = (b1[0x1C] - b1[0x13]) * costab[3]
			b1[b2 + 0x14] = b1[0x14] + b1[0x1B]
			b1[b2 + 0x1B] = (b1[0x1B] - b1[0x14]) * costab[4]
			b1[b2 + 0x15] = b1[0x15] + b1[0x1A]
			b1[b2 + 0x1A] = (b1[0x1A] - b1[0x15]) * costab[5]
			b1[b2 + 0x16] = b1[0x16] + b1[0x19]
			b1[b2 + 0x19] = (b1[0x19] - b1[0x16]) * costab[6]
			b1[b2 + 0x17] = b1[0x17] + b1[0x18]
			b1[b2 + 0x18] = (b1[0x18] - b1[0x17]) * costab[7]
		}

		run {
			val costab = pnts[2]

			b1[0x00] = b1[b2 + 0x00] + b1[b2 + 0x07]
			b1[0x07] = (b1[b2 + 0x00] - b1[b2 + 0x07]) * costab[0]
			b1[0x01] = b1[b2 + 0x01] + b1[b2 + 0x06]
			b1[0x06] = (b1[b2 + 0x01] - b1[b2 + 0x06]) * costab[1]
			b1[0x02] = b1[b2 + 0x02] + b1[b2 + 0x05]
			b1[0x05] = (b1[b2 + 0x02] - b1[b2 + 0x05]) * costab[2]
			b1[0x03] = b1[b2 + 0x03] + b1[b2 + 0x04]
			b1[0x04] = (b1[b2 + 0x03] - b1[b2 + 0x04]) * costab[3]

			b1[0x08] = b1[b2 + 0x08] + b1[b2 + 0x0F]
			b1[0x0F] = (b1[b2 + 0x0F] - b1[b2 + 0x08]) * costab[0]
			b1[0x09] = b1[b2 + 0x09] + b1[b2 + 0x0E]
			b1[0x0E] = (b1[b2 + 0x0E] - b1[b2 + 0x09]) * costab[1]
			b1[0x0A] = b1[b2 + 0x0A] + b1[b2 + 0x0D]
			b1[0x0D] = (b1[b2 + 0x0D] - b1[b2 + 0x0A]) * costab[2]
			b1[0x0B] = b1[b2 + 0x0B] + b1[b2 + 0x0C]
			b1[0x0C] = (b1[b2 + 0x0C] - b1[b2 + 0x0B]) * costab[3]

			b1[0x10] = b1[b2 + 0x10] + b1[b2 + 0x17]
			b1[0x17] = (b1[b2 + 0x10] - b1[b2 + 0x17]) * costab[0]
			b1[0x11] = b1[b2 + 0x11] + b1[b2 + 0x16]
			b1[0x16] = (b1[b2 + 0x11] - b1[b2 + 0x16]) * costab[1]
			b1[0x12] = b1[b2 + 0x12] + b1[b2 + 0x15]
			b1[0x15] = (b1[b2 + 0x12] - b1[b2 + 0x15]) * costab[2]
			b1[0x13] = b1[b2 + 0x13] + b1[b2 + 0x14]
			b1[0x14] = (b1[b2 + 0x13] - b1[b2 + 0x14]) * costab[3]

			b1[0x18] = b1[b2 + 0x18] + b1[b2 + 0x1F]
			b1[0x1F] = (b1[b2 + 0x1F] - b1[b2 + 0x18]) * costab[0]
			b1[0x19] = b1[b2 + 0x19] + b1[b2 + 0x1E]
			b1[0x1E] = (b1[b2 + 0x1E] - b1[b2 + 0x19]) * costab[1]
			b1[0x1A] = b1[b2 + 0x1A] + b1[b2 + 0x1D]
			b1[0x1D] = (b1[b2 + 0x1D] - b1[b2 + 0x1A]) * costab[2]
			b1[0x1B] = b1[b2 + 0x1B] + b1[b2 + 0x1C]
			b1[0x1C] = (b1[b2 + 0x1C] - b1[b2 + 0x1B]) * costab[3]
		}

		run {
			val cos0 = pnts[3][0]
			val cos1 = pnts[3][1]

			b1[b2 + 0x00] = b1[0x00] + b1[0x03]
			b1[b2 + 0x03] = (b1[0x00] - b1[0x03]) * cos0
			b1[b2 + 0x01] = b1[0x01] + b1[0x02]
			b1[b2 + 0x02] = (b1[0x01] - b1[0x02]) * cos1

			b1[b2 + 0x04] = b1[0x04] + b1[0x07]
			b1[b2 + 0x07] = (b1[0x07] - b1[0x04]) * cos0
			b1[b2 + 0x05] = b1[0x05] + b1[0x06]
			b1[b2 + 0x06] = (b1[0x06] - b1[0x05]) * cos1

			b1[b2 + 0x08] = b1[0x08] + b1[0x0B]
			b1[b2 + 0x0B] = (b1[0x08] - b1[0x0B]) * cos0
			b1[b2 + 0x09] = b1[0x09] + b1[0x0A]
			b1[b2 + 0x0A] = (b1[0x09] - b1[0x0A]) * cos1

			b1[b2 + 0x0C] = b1[0x0C] + b1[0x0F]
			b1[b2 + 0x0F] = (b1[0x0F] - b1[0x0C]) * cos0
			b1[b2 + 0x0D] = b1[0x0D] + b1[0x0E]
			b1[b2 + 0x0E] = (b1[0x0E] - b1[0x0D]) * cos1

			b1[b2 + 0x10] = b1[0x10] + b1[0x13]
			b1[b2 + 0x13] = (b1[0x10] - b1[0x13]) * cos0
			b1[b2 + 0x11] = b1[0x11] + b1[0x12]
			b1[b2 + 0x12] = (b1[0x11] - b1[0x12]) * cos1

			b1[b2 + 0x14] = b1[0x14] + b1[0x17]
			b1[b2 + 0x17] = (b1[0x17] - b1[0x14]) * cos0
			b1[b2 + 0x15] = b1[0x15] + b1[0x16]
			b1[b2 + 0x16] = (b1[0x16] - b1[0x15]) * cos1

			b1[b2 + 0x18] = b1[0x18] + b1[0x1B]
			b1[b2 + 0x1B] = (b1[0x18] - b1[0x1B]) * cos0
			b1[b2 + 0x19] = b1[0x19] + b1[0x1A]
			b1[b2 + 0x1A] = (b1[0x19] - b1[0x1A]) * cos1

			b1[b2 + 0x1C] = b1[0x1C] + b1[0x1F]
			b1[b2 + 0x1F] = (b1[0x1F] - b1[0x1C]) * cos0
			b1[b2 + 0x1D] = b1[0x1D] + b1[0x1E]
			b1[b2 + 0x1E] = (b1[0x1E] - b1[0x1D]) * cos1
		}

		run {
			val cos0 = pnts[4][0]

			b1[0x00] = b1[b2 + 0x00] + b1[b2 + 0x01]
			b1[0x01] = (b1[b2 + 0x00] - b1[b2 + 0x01]) * cos0
			b1[0x02] = b1[b2 + 0x02] + b1[b2 + 0x03]
			b1[0x03] = (b1[b2 + 0x03] - b1[b2 + 0x02]) * cos0
			b1[0x02] += b1[0x03]

			b1[0x04] = b1[b2 + 0x04] + b1[b2 + 0x05]
			b1[0x05] = (b1[b2 + 0x04] - b1[b2 + 0x05]) * cos0
			b1[0x06] = b1[b2 + 0x06] + b1[b2 + 0x07]
			b1[0x07] = (b1[b2 + 0x07] - b1[b2 + 0x06]) * cos0
			b1[0x06] += b1[0x07]
			b1[0x04] += b1[0x06]
			b1[0x06] += b1[0x05]
			b1[0x05] += b1[0x07]

			b1[0x08] = b1[b2 + 0x08] + b1[b2 + 0x09]
			b1[0x09] = (b1[b2 + 0x08] - b1[b2 + 0x09]) * cos0
			b1[0x0A] = b1[b2 + 0x0A] + b1[b2 + 0x0B]
			b1[0x0B] = (b1[b2 + 0x0B] - b1[b2 + 0x0A]) * cos0
			b1[0x0A] += b1[0x0B]

			b1[0x0C] = b1[b2 + 0x0C] + b1[b2 + 0x0D]
			b1[0x0D] = (b1[b2 + 0x0C] - b1[b2 + 0x0D]) * cos0
			b1[0x0E] = b1[b2 + 0x0E] + b1[b2 + 0x0F]
			b1[0x0F] = (b1[b2 + 0x0F] - b1[b2 + 0x0E]) * cos0
			b1[0x0E] += b1[0x0F]
			b1[0x0C] += b1[0x0E]
			b1[0x0E] += b1[0x0D]
			b1[0x0D] += b1[0x0F]

			b1[0x10] = b1[b2 + 0x10] + b1[b2 + 0x11]
			b1[0x11] = (b1[b2 + 0x10] - b1[b2 + 0x11]) * cos0
			b1[0x12] = b1[b2 + 0x12] + b1[b2 + 0x13]
			b1[0x13] = (b1[b2 + 0x13] - b1[b2 + 0x12]) * cos0
			b1[0x12] += b1[0x13]

			b1[0x14] = b1[b2 + 0x14] + b1[b2 + 0x15]
			b1[0x15] = (b1[b2 + 0x14] - b1[b2 + 0x15]) * cos0
			b1[0x16] = b1[b2 + 0x16] + b1[b2 + 0x17]
			b1[0x17] = (b1[b2 + 0x17] - b1[b2 + 0x16]) * cos0
			b1[0x16] += b1[0x17]
			b1[0x14] += b1[0x16]
			b1[0x16] += b1[0x15]
			b1[0x15] += b1[0x17]

			b1[0x18] = b1[b2 + 0x18] + b1[b2 + 0x19]
			b1[0x19] = (b1[b2 + 0x18] - b1[b2 + 0x19]) * cos0
			b1[0x1A] = b1[b2 + 0x1A] + b1[b2 + 0x1B]
			b1[0x1B] = (b1[b2 + 0x1B] - b1[b2 + 0x1A]) * cos0
			b1[0x1A] += b1[0x1B]

			b1[0x1C] = b1[b2 + 0x1C] + b1[b2 + 0x1D]
			b1[0x1D] = (b1[b2 + 0x1C] - b1[b2 + 0x1D]) * cos0
			b1[0x1E] = b1[b2 + 0x1E] + b1[b2 + 0x1F]
			b1[0x1F] = (b1[b2 + 0x1F] - b1[b2 + 0x1E]) * cos0
			b1[0x1E] += b1[0x1F]
			b1[0x1C] += b1[0x1E]
			b1[0x1E] += b1[0x1D]
			b1[0x1D] += b1[0x1F]
		}

		out0[out0Pos + 0x10 * 16] = b1[0x00]
		out0[out0Pos + 0x10 * 12] = b1[0x04]
		out0[out0Pos + 0x10 * 8] = b1[0x02]
		out0[out0Pos + 0x10 * 4] = b1[0x06]
		out0[out0Pos + 0x10 * 0] = b1[0x01]

		out1[out1Pos + 0x10 * 0] = b1[0x01]
		out1[out1Pos + 0x10 * 4] = b1[0x05]
		out1[out1Pos + 0x10 * 8] = b1[0x03]
		out1[out1Pos + 0x10 * 12] = b1[0x07]

		b1[0x08] += b1[0x0C]
		out0[out0Pos + 0x10 * 14] = b1[0x08]
		b1[0x0C] += b1[0x0a]
		out0[out0Pos + 0x10 * 10] = b1[0x0C]
		b1[0x0A] += b1[0x0E]
		out0[out0Pos + 0x10 * 6] = b1[0x0A]
		b1[0x0E] += b1[0x09]
		out0[out0Pos + 0x10 * 2] = b1[0x0E]
		b1[0x09] += b1[0x0D]
		out1[out1Pos + 0x10 * 2] = b1[0x09]
		b1[0x0D] += b1[0x0B]
		out1[out1Pos + 0x10 * 6] = b1[0x0D]
		b1[0x0B] += b1[0x0F]
		out1[out1Pos + 0x10 * 10] = b1[0x0B]
		out1[out1Pos + 0x10 * 14] = b1[0x0F]

		b1[0x18] += b1[0x1C]
		out0[out0Pos + 0x10 * 15] = b1[0x10] + b1[0x18]
		out0[out0Pos + 0x10 * 13] = b1[0x18] + b1[0x14]
		b1[0x1C] += b1[0x1a]
		out0[out0Pos + 0x10 * 11] = b1[0x14] + b1[0x1C]
		out0[out0Pos + 0x10 * 9] = b1[0x1C] + b1[0x12]
		b1[0x1A] += b1[0x1E]
		out0[out0Pos + 0x10 * 7] = b1[0x12] + b1[0x1A]
		out0[out0Pos + 0x10 * 5] = b1[0x1A] + b1[0x16]
		b1[0x1E] += b1[0x19]
		out0[out0Pos + 0x10 * 3] = b1[0x16] + b1[0x1E]
		out0[out0Pos + 0x10 * 1] = b1[0x1E] + b1[0x11]
		b1[0x19] += b1[0x1D]
		out1[out1Pos + 0x10 * 1] = b1[0x11] + b1[0x19]
		out1[out1Pos + 0x10 * 3] = b1[0x19] + b1[0x15]
		b1[0x1D] += b1[0x1B]
		out1[out1Pos + 0x10 * 5] = b1[0x15] + b1[0x1D]
		out1[out1Pos + 0x10 * 7] = b1[0x1D] + b1[0x13]
		b1[0x1B] += b1[0x1F]
		out1[out1Pos + 0x10 * 9] = b1[0x13] + b1[0x1B]
		out1[out1Pos + 0x10 * 11] = b1[0x1B] + b1[0x17]
		out1[out1Pos + 0x10 * 13] = b1[0x17] + b1[0x1F]
		out1[out1Pos + 0x10 * 15] = b1[0x1F]
	}

}

class Decode {

	private val tab = TabInit
	private val dct64 = DCT64()

	private fun writeSampleClipped(sum: Float, clip: Int, out: FloatArray, outPos: Int): Int {
		if (sum > 32767.0) {
			out[outPos] = 32767f
			return clip + 1
		} else if (sum < -32768.0) {
			out[outPos] = -32768f
			return clip + 1
		} else {
			out[outPos] = (if (sum > 0) sum + 0.5 else sum - 0.5).toInt().toFloat()
			return clip
		}
	}

	private fun writeSampleUnclipped(sum: Float, out: FloatArray, outPos: Int) {
		out[outPos] = sum
	}

	fun synth1to1mono(
		mp: MPGLib.mpstr_tag,
		bandPtr: FloatArray,
		bandPos: Int,
		out: FloatArray,
		pnt: MPGLib.ProcessedBytes
	): Int {
		val samples = FloatArray(64)

		val clip = synth_1to1(mp, bandPtr, bandPos, 0, samples, MPGLib.ProcessedBytes())

		var i = 0
		while (i < samples.size) {
			out[pnt.pb++] = samples[i]
			i += 2
		}
		return clip
	}

	fun synth1to1monoUnclipped(
		mp: MPGLib.mpstr_tag,
		bandPtr: FloatArray,
		bandPos: Int,
		out: FloatArray,
		pnt: MPGLib.ProcessedBytes
	) {
		val samples = FloatArray(64)

		synth_1to1_unclipped(mp, bandPtr, bandPos, 0, samples, MPGLib.ProcessedBytes())

		var i = 0
		while (i < samples.size) {
			out[pnt.pb++] = samples[i]
			i += 2
		}
	}

	fun synth_1to1(
		mp: MPGLib.mpstr_tag,
		bandPtr: FloatArray,
		bandPos: Int,
		ch: Int,
		out: FloatArray,
		pnt: MPGLib.ProcessedBytes
	): Int {
		val b0: FloatArray
		var clip = 0
		val bo1: Int

		if (0 == ch) {
			mp.synth_bo--
			mp.synth_bo = mp.synth_bo and 0xf
		} else {
			pnt.pb++
		}

		if (mp.synth_bo and 0x1 != 0) {
			b0 = mp.synth_buffs[ch][0]
			bo1 = mp.synth_bo
			val bufs = FloatArray(0x40)
			dct64.dct64_1(
				mp.synth_buffs[ch][1],
				mp.synth_bo + 1 and 0xf,
				mp.synth_buffs[ch][0],
				mp.synth_bo,
				bufs,
				0x20,
				bandPtr,
				bandPos,
				TabInit.pnts
			)
		} else {
			b0 = mp.synth_buffs[ch][1]
			bo1 = mp.synth_bo + 1
			val bufs = FloatArray(0x40)
			dct64.dct64_1(
				mp.synth_buffs[ch][0],
				mp.synth_bo,
				mp.synth_buffs[ch][1],
				mp.synth_bo + 1,
				bufs,
				0x20,
				bandPtr,
				bandPos,
				TabInit.pnts
			)
		}

		run {
			var window = 16 - bo1

			var b0Pos = 0
			run {
				var j = 16
				while (j != 0) {
					var sum = 0f
					sum += TabInit.decwin[window + 0x0] * b0[b0Pos + 0x0]
					sum -= TabInit.decwin[window + 0x1] * b0[b0Pos + 0x1]
					sum += TabInit.decwin[window + 0x2] * b0[b0Pos + 0x2]
					sum -= TabInit.decwin[window + 0x3] * b0[b0Pos + 0x3]
					sum += TabInit.decwin[window + 0x4] * b0[b0Pos + 0x4]
					sum -= TabInit.decwin[window + 0x5] * b0[b0Pos + 0x5]
					sum += TabInit.decwin[window + 0x6] * b0[b0Pos + 0x6]
					sum -= TabInit.decwin[window + 0x7] * b0[b0Pos + 0x7]
					sum += TabInit.decwin[window + 0x8] * b0[b0Pos + 0x8]
					sum -= TabInit.decwin[window + 0x9] * b0[b0Pos + 0x9]
					sum += TabInit.decwin[window + 0xA] * b0[b0Pos + 0xA]
					sum -= TabInit.decwin[window + 0xB] * b0[b0Pos + 0xB]
					sum += TabInit.decwin[window + 0xC] * b0[b0Pos + 0xC]
					sum -= TabInit.decwin[window + 0xD] * b0[b0Pos + 0xD]
					sum += TabInit.decwin[window + 0xE] * b0[b0Pos + 0xE]
					sum -= TabInit.decwin[window + 0xF] * b0[b0Pos + 0xF]
					clip = writeSampleClipped(sum, clip, out, pnt.pb)
					j--
					b0Pos += 0x10
					window += 0x20
					pnt.pb += 2
				}
			}

			run {
				var sum = 0f
				sum += TabInit.decwin[window + 0x0] * b0[b0Pos + 0x0]
				sum += TabInit.decwin[window + 0x2] * b0[b0Pos + 0x2]
				sum += TabInit.decwin[window + 0x4] * b0[b0Pos + 0x4]
				sum += TabInit.decwin[window + 0x6] * b0[b0Pos + 0x6]
				sum += TabInit.decwin[window + 0x8] * b0[b0Pos + 0x8]
				sum += TabInit.decwin[window + 0xA] * b0[b0Pos + 0xA]
				sum += TabInit.decwin[window + 0xC] * b0[b0Pos + 0xC]
				sum += TabInit.decwin[window + 0xE] * b0[b0Pos + 0xE]
				clip = writeSampleClipped(sum, clip, out, pnt.pb)
				b0Pos -= 0x10
				window -= 0x20
				pnt.pb += 2
			}
			window += bo1 shl 1

			var j = 15
			while (j != 0) {
				var sum: Float = 0f
				sum -= TabInit.decwin[window + -0x1] * b0[b0Pos + 0x0]
				sum -= TabInit.decwin[window + -0x2] * b0[b0Pos + 0x1]
				sum -= TabInit.decwin[window + -0x3] * b0[b0Pos + 0x2]
				sum -= TabInit.decwin[window + -0x4] * b0[b0Pos + 0x3]
				sum -= TabInit.decwin[window + -0x5] * b0[b0Pos + 0x4]
				sum -= TabInit.decwin[window + -0x6] * b0[b0Pos + 0x5]
				sum -= TabInit.decwin[window + -0x7] * b0[b0Pos + 0x6]
				sum -= TabInit.decwin[window + -0x8] * b0[b0Pos + 0x7]
				sum -= TabInit.decwin[window + -0x9] * b0[b0Pos + 0x8]
				sum -= TabInit.decwin[window + -0xA] * b0[b0Pos + 0x9]
				sum -= TabInit.decwin[window + -0xB] * b0[b0Pos + 0xA]
				sum -= TabInit.decwin[window + -0xC] * b0[b0Pos + 0xB]
				sum -= TabInit.decwin[window + -0xD] * b0[b0Pos + 0xC]
				sum -= TabInit.decwin[window + -0xE] * b0[b0Pos + 0xD]
				sum -= TabInit.decwin[window + -0xF] * b0[b0Pos + 0xE]
				sum -= TabInit.decwin[window + -0x0] * b0[b0Pos + 0xF]

				clip = writeSampleClipped(sum, clip, out, pnt.pb)
				j--
				b0Pos -= 0x10
				window -= 0x20
				pnt.pb += 2
			}
		}
		if (ch == 1) {
			pnt.pb--
		}
		return clip
	}

	fun synth_1to1_unclipped(
		mp: MPGLib.mpstr_tag,
		bandPtr: FloatArray,
		bandPos: Int,
		ch: Int,
		out: FloatArray,
		pnt: MPGLib.ProcessedBytes
	) {
		val b0: FloatArray
		val bo1: Int

		if (0 == ch) {
			mp.synth_bo--
			mp.synth_bo = mp.synth_bo and 0xf
		} else {
			pnt.pb++
		}

		if (mp.synth_bo and 0x1 != 0) {
			b0 = mp.synth_buffs[ch][0]
			bo1 = mp.synth_bo
			val bufs = FloatArray(0x40)
			dct64.dct64_1(
				mp.synth_buffs[ch][1],
				mp.synth_bo + 1 and 0xf,
				mp.synth_buffs[ch][0],
				mp.synth_bo,
				bufs,
				0x20,
				bandPtr,
				bandPos,
				TabInit.pnts
			)
		} else {
			b0 = mp.synth_buffs[ch][1]
			bo1 = mp.synth_bo + 1
			val bufs = FloatArray(0x40)
			dct64.dct64_1(
				mp.synth_buffs[ch][0],
				mp.synth_bo,
				mp.synth_buffs[ch][1],
				mp.synth_bo + 1,
				bufs,
				0x20,
				bandPtr,
				bandPos,
				TabInit.pnts
			)
		}

		run {
			var window = 16 - bo1

			var b0Pos = 0
			run {
				var j = 16
				while (j != 0) {
					var sum: Float
					sum = TabInit.decwin[window + 0x0] * b0[b0Pos + 0x0]
					sum -= TabInit.decwin[window + 0x1] * b0[b0Pos + 0x1]
					sum += TabInit.decwin[window + 0x2] * b0[b0Pos + 0x2]
					sum -= TabInit.decwin[window + 0x3] * b0[b0Pos + 0x3]
					sum += TabInit.decwin[window + 0x4] * b0[b0Pos + 0x4]
					sum -= TabInit.decwin[window + 0x5] * b0[b0Pos + 0x5]
					sum += TabInit.decwin[window + 0x6] * b0[b0Pos + 0x6]
					sum -= TabInit.decwin[window + 0x7] * b0[b0Pos + 0x7]
					sum += TabInit.decwin[window + 0x8] * b0[b0Pos + 0x8]
					sum -= TabInit.decwin[window + 0x9] * b0[b0Pos + 0x9]
					sum += TabInit.decwin[window + 0xA] * b0[b0Pos + 0xA]
					sum -= TabInit.decwin[window + 0xB] * b0[b0Pos + 0xB]
					sum += TabInit.decwin[window + 0xC] * b0[b0Pos + 0xC]
					sum -= TabInit.decwin[window + 0xD] * b0[b0Pos + 0xD]
					sum += TabInit.decwin[window + 0xE] * b0[b0Pos + 0xE]
					sum -= TabInit.decwin[window + 0xF] * b0[b0Pos + 0xF]
					writeSampleUnclipped(sum, out, pnt.pb)
					j--
					b0Pos += 0x10
					window += 0x20
					pnt.pb += 2
				}
			}

			run {
				var sum: Float
				sum = TabInit.decwin[window + 0x0] * b0[b0Pos + 0x0]
				sum += TabInit.decwin[window + 0x2] * b0[b0Pos + 0x2]
				sum += TabInit.decwin[window + 0x4] * b0[b0Pos + 0x4]
				sum += TabInit.decwin[window + 0x6] * b0[b0Pos + 0x6]
				sum += TabInit.decwin[window + 0x8] * b0[b0Pos + 0x8]
				sum += TabInit.decwin[window + 0xA] * b0[b0Pos + 0xA]
				sum += TabInit.decwin[window + 0xC] * b0[b0Pos + 0xC]
				sum += TabInit.decwin[window + 0xE] * b0[b0Pos + 0xE]
				writeSampleUnclipped(sum, out, pnt.pb)
				b0Pos -= 0x10
				window -= 0x20
				pnt.pb += 2
			}
			window += bo1 shl 1

			var j = 15
			while (j != 0) {
				var sum: Float
				sum = -TabInit.decwin[window + -0x1] * b0[b0Pos + 0x0]
				sum -= TabInit.decwin[window + -0x2] * b0[b0Pos + 0x1]
				sum -= TabInit.decwin[window + -0x3] * b0[b0Pos + 0x2]
				sum -= TabInit.decwin[window + -0x4] * b0[b0Pos + 0x3]
				sum -= TabInit.decwin[window + -0x5] * b0[b0Pos + 0x4]
				sum -= TabInit.decwin[window + -0x6] * b0[b0Pos + 0x5]
				sum -= TabInit.decwin[window + -0x7] * b0[b0Pos + 0x6]
				sum -= TabInit.decwin[window + -0x8] * b0[b0Pos + 0x7]
				sum -= TabInit.decwin[window + -0x9] * b0[b0Pos + 0x8]
				sum -= TabInit.decwin[window + -0xA] * b0[b0Pos + 0x9]
				sum -= TabInit.decwin[window + -0xB] * b0[b0Pos + 0xA]
				sum -= TabInit.decwin[window + -0xC] * b0[b0Pos + 0xB]
				sum -= TabInit.decwin[window + -0xD] * b0[b0Pos + 0xC]
				sum -= TabInit.decwin[window + -0xE] * b0[b0Pos + 0xD]
				sum -= TabInit.decwin[window + -0xF] * b0[b0Pos + 0xE]
				sum -= TabInit.decwin[window + -0x0] * b0[b0Pos + 0xF]

				writeSampleUnclipped(sum, out, pnt.pb)
				j--
				b0Pos -= 0x10
				window -= 0x20
				pnt.pb += 2
			}
		}
		if (ch == 1) {
			pnt.pb--
		}
	}
}

class Frame {
	var stereo: Int = 0
	var jsbound: Int = 0
	var single: Int = 0
	var lsf: Int = 0
	var mpeg25: Boolean = false
	var lay: Int = 0
	var error_protection: Boolean = false
	var bitrate_index: Int = 0
	var sampling_frequency: Int = 0
	var padding: Int = 0
	var extension: Int = 0
	var mode: Int = 0
	var mode_ext: Int = 0
	var copyright: Int = 0
	var original: Int = 0
	var emphasis: Int = 0
	var framesize: Int = 0
	var II_sblimit: Int = 0
	var alloc: Array<L2Tables.al_table2>? = null
	var down_sample_sblimit: Int = 0
	var down_sample: Int = 0
}

class FrameSkip {
	var encoderDelay = -1
	var encoderPadding = -1
}

class GetAudio(internal var parse: Parse, internal var mpg: MPGLib) {

	/* AIFF Definitions */
	lateinit private var musicin: AsyncStream
	private var hip: MPGLib.mpstr_tag = MPGLib.mpstr_tag()

	suspend fun initInFile(gfp: LameGlobalFlags, inPath: AsyncStream, enc: FrameSkip) {
		try {
			musicin = OpenSndFile(gfp, inPath, enc)
		} catch (e: IOException) {
			e.printStackTrace()
		}

	}

	suspend fun get_audio16(gfp: LameGlobalFlags, buffer: Array<FloatArray>): Int {
		return get_audio_common(gfp, null, buffer)
	}

	private suspend fun get_audio_common(
		gfp: LameGlobalFlags,
		buffer: Array<FloatArray>?,
		buffer16: Array<FloatArray>
	): Int {
		val num_channels = gfp.inNumChannels
		val buf_tmp16 = Array(2) { FloatArray(1152) }
		val samples_read: Int

		samples_read = read_samples_mp3(gfp, musicin, if (buffer != null) buf_tmp16 else buffer16)
		if (samples_read < 0) return samples_read

		if (buffer != null) {
			run {
				var i = samples_read
				while (--i >= 0) {
					val value = buf_tmp16[0][i].toInt()
					buffer[0][i] = (value shl 16).toFloat()
				}
			}
			if (num_channels == 2) {
				var i = samples_read
				while (--i >= 0) {
					val value = buf_tmp16[1][i].toInt()
					buffer[1][i] = (value shl 16).toFloat()
				}
			} else if (num_channels == 1) {
				buffer[1].fill(0f, 0, samples_read)
			} else {
				throw RuntimeException("Channels must be 1 or 2")
			}
		}

		return samples_read
	}

	internal suspend fun read_samples_mp3(
		gfp: LameGlobalFlags,
		musicin: AsyncStream,
		mpg123pcm: Array<FloatArray>
	): Int {
		val out: Int

		out = lame_decode_fromfile(musicin, mpg123pcm[0], mpg123pcm[1], parse.mp3InputData)

		if (out < 0) {
			mpg123pcm[0].fill(0.toShort().toFloat())
			mpg123pcm[1].fill(0.toShort().toFloat())
			return 0
		}

		if (gfp.inNumChannels != parse.mp3InputData.stereo)
			throw RuntimeException("number of channels has changed")
		if (gfp.inSampleRate != parse.mp3InputData.samplerate)
			throw RuntimeException("sample frequency has changed")
		return out
	}

	//@Throws(IOException::class)
	suspend private fun OpenSndFile(gfp: LameGlobalFlags, musicin2: AsyncStream, enc: FrameSkip): AsyncStream {

		/* set the defaults from info in case we cannot determine them from file */
		gfp.num_samples = -1

		musicin = musicin2

		if (-1 == lame_decode_initfile(musicin, parse.mp3InputData, enc)) {
			throw RuntimeException("Error reading headers in mp3 input file $musicin2.")
		}
		gfp.inNumChannels = parse.mp3InputData.stereo
		gfp.inSampleRate = parse.mp3InputData.samplerate
		gfp.num_samples = parse.mp3InputData.numSamples

		if (gfp.num_samples == -1) {

			val flen = musicin2.getLength()
			if (flen >= 0L) {
				if (parse.mp3InputData.bitrate > 0) {
					val totalseconds = flen.toDouble() * 8.0 / (1000.0 * parse.mp3InputData.bitrate)
					val tmp_num_samples = (totalseconds * gfp.inSampleRate).toInt()

					gfp.num_samples = tmp_num_samples
					parse.mp3InputData.numSamples = tmp_num_samples
				}
			}
		}
		return musicin
	}

	private fun check_aid(header: ByteArray): Boolean {
		return header.size >= 4 && header[0] == 'A'.toByte() && header[1] == 'i'.toByte() && header[2] == 'D'.toByte() && header[3].toInt() == 1
	}

	private fun is_syncword_mp123(headerptr: ByteArray): Boolean {
		val p = 0

		if (headerptr[p + 0].unsigned and 0xFF != 0xFF) return false /* first 8 bits must be '1' */
		if (headerptr[p + 1].unsigned and 0xE0 != 0xE0) return false /* next 3 bits are also */
		if (headerptr[p + 1].unsigned and 0x18 == 0x08) return false /* no MPEG-1, -2 or -2.5 */

		parse.layer = when (headerptr[p + 1].toInt() and 0x06) {
			0x02 -> 3
			0x04 -> 2
			0x06 -> 1
			else -> return false // illegal layer
		}
		if (headerptr[p + 1].unsigned and 0x06 == 0x00) return false /* no Layer I, II and III */
		if (headerptr[p + 2].unsigned and 0xF0 == 0xF0) return false /* bad bitrate */
		if (headerptr[p + 2].unsigned and 0x0C == 0x0C) return false /* no sample frequency with (32,44.1,48)/(1,2,4) */
		if (headerptr[p + 1].unsigned and 0x18 == 0x18 && headerptr[p + 1].unsigned and 0x06 == 0x04 && abl2[headerptr[p + 2].unsigned shr 4].toInt() and (1 shl (headerptr[p + 3].unsigned shr 6)) != 0)
			return false
		if (headerptr[p + 3].unsigned and 3 == 2) return false /* reserved enphasis mode */
		return true
	}

	private suspend fun lame_decode_initfile(fd: AsyncStream, mp3data: MP3Data, enc: FrameSkip): Int {
		val buf = ByteArray(100)
		val pcm_l = FloatArray(1152)
		val pcm_r = FloatArray(1152)
		var freeformat = false

		mpg.hip_decode_exit(hip)
		hip = mpg.hip_decode_init()

		var len = 4
		try {
			fd.readExact(buf, 0, len)
		} catch (e: IOException) {
			e.printStackTrace()
			return -1 /* failed */
		}

		if (buf[0] == 'I'.toByte() && buf[1] == 'D'.toByte() && buf[2] == '3'.toByte()) {
			//System.out.println("ID3v2 found. Be aware that the ID3 tag is currently lost when transcoding.");
			len = 6
			try {
				fd.readExact(buf, 0, len)
			} catch (e: IOException) {
				e.printStackTrace()
				return -1 /* failed */
			}

			buf[2] = (buf[2].toInt() and 127).toByte()
			buf[3] = (buf[3].toInt() and 127).toByte()
			buf[4] = (buf[4].toInt() and 127).toByte()
			buf[5] = (buf[5].toInt() and 127).toByte()
			len = (((buf[2].unsigned shl 7) + buf[3] shl 7) + buf[4] shl 7) + buf[5]
			try {
				fd.skip(len)
			} catch (e: IOException) {
				e.printStackTrace()
				return -1 /* failed */
			}

			len = 4
			try {
				fd.readExact(buf, 0, len)
			} catch (e: IOException) {
				e.printStackTrace()
				return -1 /* failed */
			}

		}
		if (check_aid(buf)) {
			try {
				fd.readExact(buf, 0, 2)
			} catch (e: IOException) {
				e.printStackTrace()
				return -1 /* failed */
			}

			val aid_header = (buf[0].unsigned) + 256 * (buf[1].unsigned)
			//System.out.printf("Album ID found.  length=%d \n", aid_header);
			/* skip rest of AID, except for 6 bytes we have already read */
			try {
				fd.skip(aid_header - 6)
			} catch (e: IOException) {
				e.printStackTrace()
				return -1 /* failed */
			}

			/* read 4 more bytes to set up buffer for MP3 header check */
			try {
				fd.readExact(buf, 0, len)
			} catch (e: IOException) {
				e.printStackTrace()
				return -1 /* failed */
			}

		}
		len = 4
		while (!is_syncword_mp123(buf)) {
			var i: Int
			i = 0
			while (i < len - 1) {
				buf[i] = buf[i + 1]
				i++
			}
			try {
				fd.readExact(buf, len - 1, 1)
			} catch (e: IOException) {
				e.printStackTrace()
				return -1 /* failed */
			}

		}

		if (buf[2].unsigned and 0xf0 == 0) {
			//System.out.println("Input file is freeformat.");
			freeformat = true
		}

		var ret = mpg.hip_decode1_headers(hip, buf, len, pcm_l, pcm_r, mp3data, enc)
		if (ret == -1) return -1

		while (!mp3data.header_parsed) {
			try {
				fd.readExact(buf, 0, buf.size)
			} catch (e: IOException) {
				e.printStackTrace()
				return -1 /* failed */
			}

			ret = mpg.hip_decode1_headers(hip, buf, buf.size, pcm_l, pcm_r, mp3data, enc)
			if (ret == -1) return -1
		}

		if (mp3data.bitrate == 0 && !freeformat) return lame_decode_initfile(fd, mp3data, enc)
		if (mp3data.totalFrames <= 0) mp3data.numSamples = -1

		return 0
	}

	suspend private fun lame_decode_fromfile(
		fd: AsyncStream,
		pcm_l: FloatArray,
		pcm_r: FloatArray,
		mp3data: MP3Data
	): Int {
		var len = 0
		val buf = ByteArray(1024)

		/* first see if we still have data buffered in the decoder: */
		var ret = mpg.hip_decode1_headers(hip, buf, len, pcm_l, pcm_r, mp3data, FrameSkip())
		if (ret != 0) return ret

		/* read until we get a valid output frame */
		while (true) {
			try {
				len = fd.read(buf, 0, 1024)
			} catch (e: IOException) {
				e.printStackTrace()
				return -1
			}

			if (len <= 0) {
				/* we are done reading the file, but check for buffered data */
				ret = mpg.hip_decode1_headers(hip, buf, 0, pcm_l, pcm_r, mp3data, FrameSkip())
				if (ret <= 0) {
					mpg.hip_decode_exit(hip)
					/* release mp3decoder memory */
					return -1 /* done with file */
				}
				break
			}

			ret = mpg.hip_decode1_headers(hip, buf, len, pcm_l, pcm_r, mp3data, FrameSkip())
			if (ret == -1) {
				mpg.hip_decode_exit(hip)
				/* release mp3decoder memory */
				return -1
			}
			if (ret > 0)
				break
		}
		return ret
	}

	companion object {
		private val abl2 = charArrayOf(
			0.toChar(),
			7.toChar(),
			7.toChar(),
			7.toChar(),
			0.toChar(),
			7.toChar(),
			0.toChar(),
			0.toChar(),
			0.toChar(),
			0.toChar(),
			0.toChar(),
			8.toChar(),
			8.toChar(),
			8.toChar(),
			8.toChar(),
			8.toChar()
		)
	}
}

object Huffman {
	private val tab0 = shortArrayOf(0)
	private val tab1 = shortArrayOf(-5, -3, -1, 17, 1, 16, 0)
	private val tab2 = shortArrayOf(-15, -11, -9, -5, -3, -1, 34, 2, 18, -1, 33, 32, 17, -1, 1, 16, 0)
	private val tab3 = shortArrayOf(-13, -11, -9, -5, -3, -1, 34, 2, 18, -1, 33, 32, 16, 17, -1, 1, 0)
	private val tab5 = shortArrayOf(-29, -25, -23, -15, -7, -5, -3, -1, 51, 35, 50, 49, -3, -1, 19, 3, -1, 48, 34, -3, -1, 18, 33, -1, 2, 32, 17, -1, 1, 16, 0)
	private val tab6 = shortArrayOf(-25, -19, -13, -9, -5, -3, -1, 51, 3, 35, -1, 50, 48, -1, 19, 49, -3, -1, 34, 2, 18, -3, -1, 33, 32, 1, -1, 17, -1, 16, 0)
	private val tab7 = shortArrayOf(-69, -65, -57, -39, -29, -17, -11, -7, -3, -1, 85, 69, -1, 84, 83, -1, 53, 68, -3, -1, 37, 82, 21, -5, -1, 81, -1, 5, 52, -1, 80, -1, 67, 51, -5, -3, -1, 36, 66, 20, -1, 65, 64, -11, -7, -3, -1, 4, 35, -1, 50, 3, -1, 19, 49, -3, -1, 48, 34, 18, -5, -1, 33, -1, 2, 32, 17, -1, 1, 16, 0)
	private val tab8 = shortArrayOf(-65, -63, -59, -45, -31, -19, -13, -7, -5, -3, -1, 85, 84, 69, 83, -3, -1, 53, 68, 37, -3, -1, 82, 5, 21, -5, -1, 81, -1, 52, 67, -3, -1, 80, 51, 36, -5, -3, -1, 66, 20, 65, -3, -1, 4, 64, -1, 35, 50, -9, -7, -3, -1, 19, 49, -1, 3, 48, 34, -1, 2, 32, -1, 18, 33, 17, -3, -1, 1, 16, 0)
	private val tab9 = shortArrayOf(-63, -53, -41, -29, -19, -11, -5, -3, -1, 85, 69, 53, -1, 83, -1, 84, 5, -3, -1, 68, 37, -1, 82, 21, -3, -1, 81, 52, -1, 67, -1, 80, 4, -7, -3, -1, 36, 66, -1, 51, 64, -1, 20, 65, -5, -3, -1, 35, 50, 19, -1, 49, -1, 3, 48, -5, -3, -1, 34, 2, 18, -1, 33, 32, -3, -1, 17, 1, -1, 16, 0)
	private val tab10 = shortArrayOf(-125, -121, -111, -83, -55, -35, -21, -13, -7, -3, -1, 119, 103, -1, 118, 87, -3, -1, 117, 102, 71, -3, -1, 116, 86, -1, 101, 55, -9, -3, -1, 115, 70, -3, -1, 85, 84, 99, -1, 39, 114, -11, -5, -3, -1, 100, 7, 112, -1, 98, -1, 69, 53, -5, -1, 6, -1, 83, 68, 23, -17, -5, -1, 113, -1, 54, 38, -5, -3, -1, 37, 82, 21, -1, 81, -1, 52, 67, -3, -1, 22, 97, -1, 96, -1, 5, 80, -19, -11, -7, -3, -1, 36, 66, -1, 51, 4, -1, 20, 65, -3, -1, 64, 35, -1, 50, 3, -3, -1, 19, 49, -1, 48, 34, -7, -3, -1, 18, 33, -1, 2, 32, 17, -1, 1, 16, 0)
	private val tab11 = shortArrayOf(-121, -113, -89, -59, -43, -27, -17, -7, -3, -1, 119, 103, -1, 118, 117, -3, -1, 102, 71, -1, 116, -1, 87, 85, -5, -3, -1, 86, 101, 55, -1, 115, 70, -9, -7, -3, -1, 69, 84, -1, 53, 83, 39, -1, 114, -1, 100, 7, -5, -1, 113, -1, 23, 112, -3, -1, 54, 99, -1, 96, -1, 68, 37, -13, -7, -5, -3, -1, 82, 5, 21, 98, -3, -1, 38, 6, 22, -5, -1, 97, -1, 81, 52, -5, -1, 80, -1, 67, 51, -1, 36, 66, -15, -11, -7, -3, -1, 20, 65, -1, 4, 64, -1, 35, 50, -1, 19, 49, -5, -3, -1, 3, 48, 34, 33, -5, -1, 18, -1, 2, 32, 17, -3, -1, 1, 16, 0)
	private val tab12 = shortArrayOf(-115, -99, -73, -45, -27, -17, -9, -5, -3, -1, 119, 103, 118, -1, 87, 117, -3, -1, 102, 71, -1, 116, 101, -3, -1, 86, 55, -3, -1, 115, 85, 39, -7, -3, -1, 114, 70, -1, 100, 23, -5, -1, 113, -1, 7, 112, -1, 54, 99, -13, -9, -3, -1, 69, 84, -1, 68, -1, 6, 5, -1, 38, 98, -5, -1, 97, -1, 22, 96, -3, -1, 53, 83, -1, 37, 82, -17, -7, -3, -1, 21, 81, -1, 52, 67, -5, -3, -1, 80, 4, 36, -1, 66, 20, -3, -1, 51, 65, -1, 35, 50, -11, -7, -5, -3, -1, 64, 3, 48, 19, -1, 49, 34, -1, 18, 33, -7, -5, -3, -1, 2, 32, 0, 17, -1, 1, 16)
	private val tab13 = shortArrayOf(-509, -503, -475, -405, -333, -265, -205, -153, -115, -83, -53, -35, -21, -13, -9, -7, -5, -3, -1, 254, 252, 253, 237, 255, -1, 239, 223, -3, -1, 238, 207, -1, 222, 191, -9, -3, -1, 251, 206, -1, 220, -1, 175, 233, -1, 236, 221, -9, -5, -3, -1, 250, 205, 190, -1, 235, 159, -3, -1, 249, 234, -1, 189, 219, -17, -9, -3, -1, 143, 248, -1, 204, -1, 174, 158, -5, -1, 142, -1, 127, 126, 247, -5, -1, 218, -1, 173, 188, -3, -1, 203, 246, 111, -15, -7, -3, -1, 232, 95, -1, 157, 217, -3, -1, 245, 231, -1, 172, 187, -9, -3, -1, 79, 244, -3, -1, 202, 230, 243, -1, 63, -1, 141, 216, -21, -9, -3, -1, 47, 242, -3, -1, 110, 156, 15, -5, -3, -1, 201, 94, 171, -3, -1, 125, 215, 78, -11, -5, -3, -1, 200, 214, 62, -1, 185, -1, 155, 170, -1, 31, 241, -23, -13, -5, -1, 240, -1, 186, 229, -3, -1, 228, 140, -1, 109, 227, -5, -1, 226, -1, 46, 14, -1, 30, 225, -15, -7, -3, -1, 224, 93, -1, 213, 124, -3, -1, 199, 77, -1, 139, 184, -7, -3, -1, 212, 154, -1, 169, 108, -1, 198, 61, -37, -21, -9, -5, -3, -1, 211, 123, 45, -1, 210, 29, -5, -1, 183, -1, 92, 197, -3, -1, 153, 122, 195, -7, -5, -3, -1, 167, 151, 75, 209, -3, -1, 13, 208, -1, 138, 168, -11, -7, -3, -1, 76, 196, -1, 107, 182, -1, 60, 44, -3, -1, 194, 91, -3, -1, 181, 137, 28, -43, -23, -11, -5, -1, 193, -1, 152, 12, -1, 192, -1, 180, 106, -5, -3, -1, 166, 121, 59, -1, 179, -1, 136, 90, -11, -5, -1, 43, -1, 165, 105, -1, 164, -1, 120, 135, -5, -1, 148, -1, 119, 118, 178, -11, -3, -1, 27, 177, -3, -1, 11, 176, -1, 150, 74, -7, -3, -1, 58, 163, -1, 89, 149, -1, 42, 162, -47, -23, -9, -3, -1, 26, 161, -3, -1, 10, 104, 160, -5, -3, -1, 134, 73, 147, -3, -1, 57, 88, -1, 133, 103, -9, -3, -1, 41, 146, -3, -1, 87, 117, 56, -5, -1, 131, -1, 102, 71, -3, -1, 116, 86, -1, 101, 115, -11, -3, -1, 25, 145, -3, -1, 9, 144, -1, 72, 132, -7, -5, -1, 114, -1, 70, 100, 40, -1, 130, 24, -41, -27, -11, -5, -3, -1, 55, 39, 23, -1, 113, -1, 85, 7, -7, -3, -1, 112, 54, -1, 99, 69, -3, -1, 84, 38, -1, 98, 53, -5, -1, 129, -1, 8, 128, -3, -1, 22, 97, -1, 6, 96, -13, -9, -5, -3, -1, 83, 68, 37, -1, 82, 5, -1, 21, 81, -7, -3, -1, 52, 67, -1, 80, 36, -3, -1, 66, 51, 20, -19, -11, -5, -1, 65, -1, 4, 64, -3, -1, 35, 50, 19, -3, -1, 49, 3, -1, 48, 34, -3, -1, 18, 33, -1, 2, 32, -3, -1, 17, 1, 16, 0)
	private val tab15 = shortArrayOf(-495, -445, -355, -263, -183, -115, -77, -43, -27, -13, -7, -3, -1, 255, 239, -1, 254, 223, -1, 238, -1, 253, 207, -7, -3, -1, 252, 222, -1, 237, 191, -1, 251, -1, 206, 236, -7, -3, -1, 221, 175, -1, 250, 190, -3, -1, 235, 205, -1, 220, 159, -15, -7, -3, -1, 249, 234, -1, 189, 219, -3, -1, 143, 248, -1, 204, 158, -7, -3, -1, 233, 127, -1, 247, 173, -3, -1, 218, 188, -1, 111, -1, 174, 15, -19, -11, -3, -1, 203, 246, -3, -1, 142, 232, -1, 95, 157, -3, -1, 245, 126, -1, 231, 172, -9, -3, -1, 202, 187, -3, -1, 217, 141, 79, -3, -1, 244, 63, -1, 243, 216, -33, -17, -9, -3, -1, 230, 47, -1, 242, -1, 110, 240, -3, -1, 31, 241, -1, 156, 201, -7, -3, -1, 94, 171, -1, 186, 229, -3, -1, 125, 215, -1, 78, 228, -15, -7, -3, -1, 140, 200, -1, 62, 109, -3, -1, 214, 227, -1, 155, 185, -7, -3, -1, 46, 170, -1, 226, 30, -5, -1, 225, -1, 14, 224, -1, 93, 213, -45, -25, -13, -7, -3, -1, 124, 199, -1, 77, 139, -1, 212, -1, 184, 154, -7, -3, -1, 169, 108, -1, 198, 61, -1, 211, 210, -9, -5, -3, -1, 45, 13, 29, -1, 123, 183, -5, -1, 209, -1, 92, 208, -1, 197, 138, -17, -7, -3, -1, 168, 76, -1, 196, 107, -5, -1, 182, -1, 153, 12, -1, 60, 195, -9, -3, -1, 122, 167, -1, 166, -1, 192, 11, -1, 194, -1, 44, 91, -55, -29, -15, -7, -3, -1, 181, 28, -1, 137, 152, -3, -1, 193, 75, -1, 180, 106, -5, -3, -1, 59, 121, 179, -3, -1, 151, 136, -1, 43, 90, -11, -5, -1, 178, -1, 165, 27, -1, 177, -1, 176, 105, -7, -3, -1, 150, 74, -1, 164, 120, -3, -1, 135, 58, 163, -17, -7, -3, -1, 89, 149, -1, 42, 162, -3, -1, 26, 161, -3, -1, 10, 160, 104, -7, -3, -1, 134, 73, -1, 148, 57, -5, -1, 147, -1, 119, 9, -1, 88, 133, -53, -29, -13, -7, -3, -1, 41, 103, -1, 118, 146, -1, 145, -1, 25, 144, -7, -3, -1, 72, 132, -1, 87, 117, -3, -1, 56, 131, -1, 102, 71, -7, -3, -1, 40, 130, -1, 24, 129, -7, -3, -1, 116, 8, -1, 128, 86, -3, -1, 101, 55, -1, 115, 70, -17, -7, -3, -1, 39, 114, -1, 100, 23, -3, -1, 85, 113, -3, -1, 7, 112, 54, -7, -3, -1, 99, 69, -1, 84, 38, -3, -1, 98, 22, -3, -1, 6, 96, 53, -33, -19, -9, -5, -1, 97, -1, 83, 68, -1, 37, 82, -3, -1, 21, 81, -3, -1, 5, 80, 52, -7, -3, -1, 67, 36, -1, 66, 51, -1, 65, -1, 20, 4, -9, -3, -1, 35, 50, -3, -1, 64, 3, 19, -3, -1, 49, 48, 34, -9, -7, -3, -1, 18, 33, -1, 2, 32, 17, -3, -1, 1, 16, 0)
	private val tab16 = shortArrayOf(
		-509,
		-503,
		-461,
		-323,
		-103,
		-37,
		-27,
		-15,
		-7,
		-3,
		-1,
		239,
		254,
		-1,
		223,
		253,
		-3,
		-1,
		207,
		252,
		-1,
		191,
		251,
		-5,
		-1,
		175,
		-1,
		250,
		159,
		-3,
		-1,
		249,
		248,
		143,
		-7,
		-3,
		-1,
		127,
		247,
		-1,
		111,
		246,
		255,
		-9,
		-5,
		-3,
		-1,
		95,
		245,
		79,
		-1,
		244,
		243,
		-53,
		-1,
		240,
		-1,
		63,
		-29,
		-19,
		-13,
		-7,
		-5,
		-1,
		206,
		-1,
		236,
		221,
		222,
		-1,
		233,
		-1,
		234,
		217,
		-1,
		238,
		-1,
		237,
		235,
		-3,
		-1,
		190,
		205,
		-3,
		-1,
		220,
		219,
		174,
		-11,
		-5,
		-1,
		204,
		-1,
		173,
		218,
		-3,
		-1,
		126,
		172,
		202,
		-5,
		-3,
		-1,
		201,
		125,
		94,
		189,
		242,
		-93,
		-5,
		-3,
		-1,
		47,
		15,
		31,
		-1,
		241,
		-49,
		-25,
		-13,
		-5,
		-1,
		158,
		-1,
		188,
		203,
		-3,
		-1,
		142,
		232,
		-1,
		157,
		231,
		-7,
		-3,
		-1,
		187,
		141,
		-1,
		216,
		110,
		-1,
		230,
		156,
		-13,
		-7,
		-3,
		-1,
		171,
		186,
		-1,
		229,
		215,
		-1,
		78,
		-1,
		228,
		140,
		-3,
		-1,
		200,
		62,
		-1,
		109,
		-1,
		214,
		155,
		-19,
		-11,
		-5,
		-3,
		-1,
		185,
		170,
		225,
		-1,
		212,
		-1,
		184,
		169,
		-5,
		-1,
		123,
		-1,
		183,
		208,
		227,
		-7,
		-3,
		-1,
		14,
		224,
		-1,
		93,
		213,
		-3,
		-1,
		124,
		199,
		-1,
		77,
		139,
		-75,
		-45,
		-27,
		-13,
		-7,
		-3,
		-1,
		154,
		108,
		-1,
		198,
		61,
		-3,
		-1,
		92,
		197,
		13,
		-7,
		-3,
		-1,
		138,
		168,
		-1,
		153,
		76,
		-3,
		-1,
		182,
		122,
		60,
		-11,
		-5,
		-3,
		-1,
		91,
		137,
		28,
		-1,
		192,
		-1,
		152,
		121,
		-1,
		226,
		-1,
		46,
		30,
		-15,
		-7,
		-3,
		-1,
		211,
		45,
		-1,
		210,
		209,
		-5,
		-1,
		59,
		-1,
		151,
		136,
		29,
		-7,
		-3,
		-1,
		196,
		107,
		-1,
		195,
		167,
		-1,
		44,
		-1,
		194,
		181,
		-23,
		-13,
		-7,
		-3,
		-1,
		193,
		12,
		-1,
		75,
		180,
		-3,
		-1,
		106,
		166,
		179,
		-5,
		-3,
		-1,
		90,
		165,
		43,
		-1,
		178,
		27,
		-13,
		-5,
		-1,
		177,
		-1,
		11,
		176,
		-3,
		-1,
		105,
		150,
		-1,
		74,
		164,
		-5,
		-3,
		-1,
		120,
		135,
		163,
		-3,
		-1,
		58,
		89,
		42,
		-97,
		-57,
		-33,
		-19,
		-11,
		-5,
		-3,
		-1,
		149,
		104,
		161,
		-3,
		-1,
		134,
		119,
		148,
		-5,
		-3,
		-1,
		73,
		87,
		103,
		162,
		-5,
		-1,
		26,
		-1,
		10,
		160,
		-3,
		-1,
		57,
		147,
		-1,
		88,
		133,
		-9,
		-3,
		-1,
		41,
		146,
		-3,
		-1,
		118,
		9,
		25,
		-5,
		-1,
		145,
		-1,
		144,
		72,
		-3,
		-1,
		132,
		117,
		-1,
		56,
		131,
		-21,
		-11,
		-5,
		-3,
		-1,
		102,
		40,
		130,
		-3,
		-1,
		71,
		116,
		24,
		-3,
		-1,
		129,
		128,
		-3,
		-1,
		8,
		86,
		55,
		-9,
		-5,
		-1,
		115,
		-1,
		101,
		70,
		-1,
		39,
		114,
		-5,
		-3,
		-1,
		100,
		85,
		7,
		23,
		-23,
		-13,
		-5,
		-1,
		113,
		-1,
		112,
		54,
		-3,
		-1,
		99,
		69,
		-1,
		84,
		38,
		-3,
		-1,
		98,
		22,
		-1,
		97,
		-1,
		6,
		96,
		-9,
		-5,
		-1,
		83,
		-1,
		53,
		68,
		-1,
		37,
		82,
		-1,
		81,
		-1,
		21,
		5,
		-33,
		-23,
		-13,
		-7,
		-3,
		-1,
		52,
		67,
		-1,
		80,
		36,
		-3,
		-1,
		66,
		51,
		20,
		-5,
		-1,
		65,
		-1,
		4,
		64,
		-1,
		35,
		50,
		-3,
		-1,
		19,
		49,
		-3,
		-1,
		3,
		48,
		34,
		-3,
		-1,
		18,
		33,
		-1,
		2,
		32,
		-3,
		-1,
		17,
		1,
		16,
		0
	)
	private val tab24 = shortArrayOf(
		-451,
		-117,
		-43,
		-25,
		-15,
		-7,
		-3,
		-1,
		239,
		254,
		-1,
		223,
		253,
		-3,
		-1,
		207,
		252,
		-1,
		191,
		251,
		-5,
		-1,
		250,
		-1,
		175,
		159,
		-1,
		249,
		248,
		-9,
		-5,
		-3,
		-1,
		143,
		127,
		247,
		-1,
		111,
		246,
		-3,
		-1,
		95,
		245,
		-1,
		79,
		244,
		-71,
		-7,
		-3,
		-1,
		63,
		243,
		-1,
		47,
		242,
		-5,
		-1,
		241,
		-1,
		31,
		240,
		-25,
		-9,
		-1,
		15,
		-3,
		-1,
		238,
		222,
		-1,
		237,
		206,
		-7,
		-3,
		-1,
		236,
		221,
		-1,
		190,
		235,
		-3,
		-1,
		205,
		220,
		-1,
		174,
		234,
		-15,
		-7,
		-3,
		-1,
		189,
		219,
		-1,
		204,
		158,
		-3,
		-1,
		233,
		173,
		-1,
		218,
		188,
		-7,
		-3,
		-1,
		203,
		142,
		-1,
		232,
		157,
		-3,
		-1,
		217,
		126,
		-1,
		231,
		172,
		255,
		-235,
		-143,
		-77,
		-45,
		-25,
		-15,
		-7,
		-3,
		-1,
		202,
		187,
		-1,
		141,
		216,
		-5,
		-3,
		-1,
		14,
		224,
		13,
		230,
		-5,
		-3,
		-1,
		110,
		156,
		201,
		-1,
		94,
		186,
		-9,
		-5,
		-1,
		229,
		-1,
		171,
		125,
		-1,
		215,
		228,
		-3,
		-1,
		140,
		200,
		-3,
		-1,
		78,
		46,
		62,
		-15,
		-7,
		-3,
		-1,
		109,
		214,
		-1,
		227,
		155,
		-3,
		-1,
		185,
		170,
		-1,
		226,
		30,
		-7,
		-3,
		-1,
		225,
		93,
		-1,
		213,
		124,
		-3,
		-1,
		199,
		77,
		-1,
		139,
		184,
		-31,
		-15,
		-7,
		-3,
		-1,
		212,
		154,
		-1,
		169,
		108,
		-3,
		-1,
		198,
		61,
		-1,
		211,
		45,
		-7,
		-3,
		-1,
		210,
		29,
		-1,
		123,
		183,
		-3,
		-1,
		209,
		92,
		-1,
		197,
		138,
		-17,
		-7,
		-3,
		-1,
		168,
		153,
		-1,
		76,
		196,
		-3,
		-1,
		107,
		182,
		-3,
		-1,
		208,
		12,
		60,
		-7,
		-3,
		-1,
		195,
		122,
		-1,
		167,
		44,
		-3,
		-1,
		194,
		91,
		-1,
		181,
		28,
		-57,
		-35,
		-19,
		-7,
		-3,
		-1,
		137,
		152,
		-1,
		193,
		75,
		-5,
		-3,
		-1,
		192,
		11,
		59,
		-3,
		-1,
		176,
		10,
		26,
		-5,
		-1,
		180,
		-1,
		106,
		166,
		-3,
		-1,
		121,
		151,
		-3,
		-1,
		160,
		9,
		144,
		-9,
		-3,
		-1,
		179,
		136,
		-3,
		-1,
		43,
		90,
		178,
		-7,
		-3,
		-1,
		165,
		27,
		-1,
		177,
		105,
		-1,
		150,
		164,
		-17,
		-9,
		-5,
		-3,
		-1,
		74,
		120,
		135,
		-1,
		58,
		163,
		-3,
		-1,
		89,
		149,
		-1,
		42,
		162,
		-7,
		-3,
		-1,
		161,
		104,
		-1,
		134,
		119,
		-3,
		-1,
		73,
		148,
		-1,
		57,
		147,
		-63,
		-31,
		-15,
		-7,
		-3,
		-1,
		88,
		133,
		-1,
		41,
		103,
		-3,
		-1,
		118,
		146,
		-1,
		25,
		145,
		-7,
		-3,
		-1,
		72,
		132,
		-1,
		87,
		117,
		-3,
		-1,
		56,
		131,
		-1,
		102,
		40,
		-17,
		-7,
		-3,
		-1,
		130,
		24,
		-1,
		71,
		116,
		-5,
		-1,
		129,
		-1,
		8,
		128,
		-1,
		86,
		101,
		-7,
		-5,
		-1,
		23,
		-1,
		7,
		112,
		115,
		-3,
		-1,
		55,
		39,
		114,
		-15,
		-7,
		-3,
		-1,
		70,
		100,
		-1,
		85,
		113,
		-3,
		-1,
		54,
		99,
		-1,
		69,
		84,
		-7,
		-3,
		-1,
		38,
		98,
		-1,
		22,
		97,
		-5,
		-3,
		-1,
		6,
		96,
		53,
		-1,
		83,
		68,
		-51,
		-37,
		-23,
		-15,
		-9,
		-3,
		-1,
		37,
		82,
		-1,
		21,
		-1,
		5,
		80,
		-1,
		81,
		-1,
		52,
		67,
		-3,
		-1,
		36,
		66,
		-1,
		51,
		20,
		-9,
		-5,
		-1,
		65,
		-1,
		4,
		64,
		-1,
		35,
		50,
		-1,
		19,
		49,
		-7,
		-5,
		-3,
		-1,
		3,
		48,
		34,
		18,
		-1,
		33,
		-1,
		2,
		32,
		-3,
		-1,
		17,
		1,
		-1,
		16,
		0
	)

	val ht = arrayOf(
		newhuff(/* 0 */0, tab0),
		newhuff(/* 2 */0, tab1),
		newhuff(/* 3 */0, tab2),
		newhuff(/* 3 */0, tab3),
		newhuff(/* 0 */0, tab0),
		newhuff(/* 4 */0, tab5),
		newhuff(/* 4 */0, tab6),
		newhuff(/* 6 */0, tab7),
		newhuff(/* 6 */0, tab8),
		newhuff(/* 6 */0, tab9),
		newhuff(/* 8 */0, tab10),
		newhuff(/* 8 */0, tab11),
		newhuff(/* 8 */0, tab12),
		newhuff(/* 16 */0, tab13),
		newhuff(/* 0 */0, tab0),
		newhuff(/* 16 */0, tab15),
		newhuff(/* 16 */1, tab16),
		newhuff(/* 16 */2, tab16),
		newhuff(/* 16 */3, tab16),
		newhuff(/* 16 */4, tab16),
		newhuff(/* 16 */6, tab16),
		newhuff(/* 16 */8, tab16),
		newhuff(/* 16 */10, tab16),
		newhuff(/* 16 */13, tab16),
		newhuff(/* 16 */4, tab24),
		newhuff(/* 16 */5, tab24),
		newhuff(/* 16 */6, tab24),
		newhuff(/* 16 */7, tab24),
		newhuff(/* 16 */8, tab24),
		newhuff(/* 16 */9, tab24),
		newhuff(/* 16 */11, tab24),
		newhuff(/* 16 */13, tab24)
	)
	private val tab_c0 = shortArrayOf(-29, -21, -13, -7, -3, -1, 11, 15, -1, 13, 14, -3, -1, 7, 5, 9, -3, -1, 6, 3, -1, 10, 12, -3, -1, 2, 1, -1, 4, 8, 0)
	private val tab_c1 = shortArrayOf(-15, -7, -3, -1, 15, 14, -1, 13, 12, -3, -1, 11, 10, -1, 9, 8, -7, -3, -1, 7, 6, -1, 5, 4, -3, -1, 3, 2, -1, 1, 0)
	val htc = arrayOf(newhuff(/* 1 , 1 , */0, tab_c0), newhuff(/* 1 , 1 , */0, tab_c1))

	data class newhuff(val linbits: Int, val table: ShortArray)
}

class Interface(private val vbr: VBRTag, private val warningProcessor: ((String) -> Unit)?) {
	companion object {
		/* number of bytes needed by GetVbrTag to parse header */
		const val XING_HEADER_SIZE = 194
	}

	protected var decode = Decode()
	private val common = Common(warningProcessor)
	private val layer1 = Layer1(common, decode)
	private val layer2 = Layer2(common)
	private val layer3 = Layer3(common)

	fun InitMP3(): MPGLib.mpstr_tag {
		val mp = MPGLib.mpstr_tag()

		mp.framesize = 0
		mp.num_frames = 0
		mp.enc_delay = -1
		mp.enc_padding = -1
		mp.vbr_header = false
		mp.header_parsed = false
		mp.side_parsed = false
		mp.data_parsed = false
		mp.free_format = false
		mp.old_free_format = false
		mp.ssize = 0
		mp.dsize = 0
		mp.fsizeold = -1
		mp.bsize = 0
		mp.list = ArrayList<MPGLib.buf>()
		mp.fr.single = -1
		mp.bsnum = 0
		mp.wordpointer = mp.bsspace[mp.bsnum]
		mp.wordpointerPos = 512
		mp.bitindex = 0
		mp.synth_bo = 1
		mp.sync_bitstream = true

		layer3.init_layer3(MPG123.SBLIMIT)

		layer2.init_layer2()

		return mp
	}

	fun ExitMP3(mp: MPGLib.mpstr_tag) {
		mp.list.clear()
	}

	fun addbuf(mp: MPGLib.mpstr_tag, buf: ByteArray, bufPos: Int, size: Int): MPGLib.buf? {
		val nbuf = MPGLib.buf()
		nbuf.pnt = ByteArray(size)
		nbuf.size = size
		arraycopy(buf, bufPos, nbuf.pnt, 0, size)
		nbuf.pos = 0
		mp.list.add(nbuf)
		mp.bsize += size

		return nbuf
	}

	fun remove_buf(mp: MPGLib.mpstr_tag) {
		mp.list.removeAt(0)
	}

	fun read_buf_byte(mp: MPGLib.mpstr_tag): Int {
		val b: Int

		var pos: Int

		pos = mp.list[0].pos
		while (pos >= mp.list[0].size) {
			remove_buf(mp)
			if (null == mp.list[0]) {
				throw RuntimeException(
					"hip: Fatal error! tried to read past mp buffer"
				)
			}
			pos = mp.list[0].pos
		}

		b = mp.list[0].pnt[pos].unsigned
		mp.bsize--
		mp.list[0].pos++

		return b
	}

	fun read_head(mp: MPGLib.mpstr_tag) {
		var head: Long

		head = read_buf_byte(mp).toLong()
		head = head shl 8
		head = head or read_buf_byte(mp).toLong()
		head = head shl 8
		head = head or read_buf_byte(mp).toLong()
		head = head shl 8
		head = head or read_buf_byte(mp).toLong()

		mp.header = head
	}

	fun copy_mp(mp: MPGLib.mpstr_tag, size: Int, ptr: ByteArray, ptrPos: Int) {
		var len = 0

		while (len < size && mp.list[0] != null) {
			val nlen: Int
			val blen = mp.list[0].size - mp.list[0].pos
			if (size - len <= blen) {
				nlen = size - len
			} else {
				nlen = blen
			}
			arraycopy(mp.list[0].pnt, mp.list[0].pos, ptr, ptrPos + len, nlen)
			len += nlen
			mp.list[0].pos += nlen
			mp.bsize -= nlen
			if (mp.list[0].pos == mp.list[0].size) {
				remove_buf(mp)
			}
		}
	}

	/*
	 * traverse mp data structure without changing it (just like sync_buffer)
	 * pull out Xing bytes call vbr header check code from LAME if we find a
	 * header, parse it and also compute the VBR header size if no header, do
	 * nothing.
	 *
	 * bytes = number of bytes before MPEG header. skip this many bytes before
	 * starting to read return value: number of bytes in VBR header, including
	 * syncword
	 */
	internal fun check_vbr_header(mp: MPGLib.mpstr_tag, bytes: Int): Int {
		var i: Int
		var pos: Int
		var l = 0
		var buf = mp.list[l]
		val xing = ByteArray(XING_HEADER_SIZE)

		pos = buf.pos
		/* skip to valid header */
		i = 0
		while (i < bytes) {
			while (pos >= buf.size) {
				if (++l == mp.list.size)
					return -1 /* fatal error */
				buf = mp.list[l]
				pos = buf.pos
			}
			++pos
			++i
		}
		/* now read header */
		i = 0
		while (i < XING_HEADER_SIZE) {
			while (pos >= buf.size) {
				if (++l == mp.list.size)
					return -1 /* fatal error */
				buf = mp.list[l]
				pos = buf.pos
			}
			xing[i] = buf.pnt[pos]
			++pos
			++i
		}

		/* check first bytes for Xing header */
		val pTagData = vbr.getVbrTag(xing)
		mp.vbr_header = pTagData != null
		if (mp.vbr_header) {
			mp.num_frames = pTagData!!.frames
			mp.enc_delay = pTagData.encDelay
			mp.enc_padding = pTagData.encPadding

			if (pTagData.headersize < 1)
				return 1
			return pTagData.headersize
		}
		return 0
	}

	fun sync_buffer(mp: MPGLib.mpstr_tag, free_match: Boolean): Int {
		/*
		 * traverse mp structure without modifying pointers, looking for a frame
		 * valid header. if free_format, valid header must also have the same
		 * samplerate. return number of bytes in mp, before the header return -1
		 * if header is not found
		 */
		val b = intArrayOf(0, 0, 0, 0)
		var i: Int
		var pos: Int
		var h: Boolean
		var l = 0
		if (mp.list.size == 0)
			return -1
		var buf = mp.list[l]

		pos = buf.pos
		i = 0
		while (i < mp.bsize) {
			/* get 4 bytes */

			b[0] = b[1]
			b[1] = b[2]
			b[2] = b[3]
			while (pos >= buf.size) {
				buf = mp.list[++l]
				pos = buf.pos
			}
			b[3] = buf.pnt[pos].unsigned
			++pos

			if (i >= 3) {
				val fr = mp.fr
				var head: Long

				head = b[0].toLong()
				head = head shl 8
				head = head or b[1].toLong()
				head = head shl 8
				head = head or b[2].toLong()
				head = head shl 8
				head = head or b[3].toLong()
				h = common.head_check(head, fr.lay)

				if (h && free_match) {
					/* just to be even more thorough, match the sample rate */
					val mode: Int
					val stereo: Int
					val sampling_frequency: Int
					val lsf: Int
					val mpeg25: Boolean

					if (head and (1 shl 20) != 0L) {
						lsf = if (head and (1 shl 19) != 0L) 0x0 else 0x1
						mpeg25 = false
					} else {
						lsf = 1
						mpeg25 = true
					}

					mode = (head shr 6 and 0x3).toInt()
					stereo = if (mode == MPG123.MPG_MD_MONO) 1 else 2

					if (mpeg25)
						sampling_frequency = (6 + (head shr 10 and 0x3)).toInt()
					else
						sampling_frequency = ((head shr 10 and 0x3) + lsf * 3).toInt()
					h = stereo == fr.stereo && lsf == fr.lsf
							&& mpeg25 == fr.mpeg25 && sampling_frequency == fr.sampling_frequency
				}

				if (h) {
					return i - 3
				}
			}
			i++
		}
		return -1
	}

	internal fun decodeMP3_clipchoice(
		mp: MPGLib.mpstr_tag, `in`: ByteArray?, inPos: Int, isize: Int,
		out: FloatArray, done: MPGLib.ProcessedBytes, synth: ISynth
	): Int {
		var i: Int
		var iret: Int
		var bits: Int
		var bytes: Int

		if (`in` != null && isize != 0 && addbuf(mp, `in`, inPos, isize) == null)
			return MPGLib.MP3_ERR

		/* First decode header */
		if (!mp.header_parsed) {

			if (mp.fsizeold == -1 || mp.sync_bitstream) {
				val vbrbytes: Int
				mp.sync_bitstream = false

				/* This is the very first call. sync with anything */
				/* bytes= number of bytes before header */
				bytes = sync_buffer(mp, false)

				/* now look for Xing VBR header */
				if (mp.bsize >= bytes + XING_HEADER_SIZE) {
					/* vbrbytes = number of bytes in entire vbr header */
					vbrbytes = check_vbr_header(mp, bytes)
				} else {
					/* not enough data to look for Xing header */
					return MPGLib.MP3_NEED_MORE
				}

				if (mp.vbr_header) {
					/* do we have enough data to parse entire Xing header? */
					if (bytes + vbrbytes > mp.bsize) {
						return MPGLib.MP3_NEED_MORE
					}

					/*
					 * read in Xing header. Buffer data in case it is used by a
					 * non zero main_data_begin for the next frame, but
					 * otherwise dont decode Xing header
					 */
					i = 0
					while (i < vbrbytes + bytes) {
						read_buf_byte(mp)
						++i
					}
					/* now we need to find another syncword */
					/* just return and make user send in more data */

					return MPGLib.MP3_NEED_MORE
				}
			} else {
				/* match channels, samplerate, etc, when syncing */
				bytes = sync_buffer(mp, true)
			}

			/* buffer now synchronized */
			if (bytes < 0) {
				/* fprintf(stderr,"hip: need more bytes %d\n", bytes); */
				return MPGLib.MP3_NEED_MORE
			}
			if (bytes > 0) {
				/*
				 * there were some extra bytes in front of header. bitstream
				 * problem, but we are now resynced should try to buffer
				 * previous data in case new frame has nonzero main_data_begin,
				 * but we need to make sure we do not overflow buffer
				 */
				var size: Int
				warningProcessor?.invoke("hip: bitstream problem, resyncing skipping $bytes bytes...")
				mp.old_free_format = false

				/* FIXME: correct ??? */
				mp.sync_bitstream = true

				/* skip some bytes, buffer the rest */
				size = mp.wordpointerPos - 512

				if (size > MPG123.MAXFRAMESIZE) {
					/*
					 * wordpointer buffer is trashed. probably cant recover, but
					 * try anyway
					 */
					warningProcessor?.invoke("hip: wordpointer trashed.  size=$size (${MPG123.MAXFRAMESIZE})  bytes=$bytes")
					size = 0
					mp.wordpointer = mp.bsspace[mp.bsnum]
					mp.wordpointerPos = 512
				}

				/*
				 * buffer contains 'size' data right now we want to add 'bytes'
				 * worth of data, but do not exceed MAXFRAMESIZE, so we through
				 * away 'i' bytes
				 */
				i = size + bytes - MPG123.MAXFRAMESIZE
				while (i > 0) {
					--bytes
					read_buf_byte(mp)
					--i
				}

				copy_mp(mp, bytes, mp.wordpointer, mp.wordpointerPos)
				mp.fsizeold += bytes
			}

			read_head(mp)
			common.decode_header(mp.fr, mp.header)
			mp.header_parsed = true
			mp.framesize = mp.fr.framesize
			mp.free_format = mp.framesize == 0

			if (mp.fr.lsf != 0)
				mp.ssize = if (mp.fr.stereo == 1) 9 else 17
			else
				mp.ssize = if (mp.fr.stereo == 1) 17 else 32
			if (mp.fr.error_protection)
				mp.ssize += 2

			mp.bsnum = 1 - mp.bsnum /* toggle buffer */
			mp.wordpointer = mp.bsspace[mp.bsnum]
			mp.wordpointerPos = 512
			mp.bitindex = 0

			/* for very first header, never parse rest of data */
			if (mp.fsizeold == -1) {
				return MPGLib.MP3_NEED_MORE
			}
		} /* end of header parsing block */

		/* now decode side information */
		if (!mp.side_parsed) {

			/* Layer 3 only */
			if (mp.fr.lay == 3) {
				if (mp.bsize < mp.ssize)
					return MPGLib.MP3_NEED_MORE

				copy_mp(mp, mp.ssize, mp.wordpointer, mp.wordpointerPos)

				if (mp.fr.error_protection)
					common.getbits(mp, 16)
				bits = layer3.do_layer3_sideinfo(mp)
				/* bits = actual number of bits needed to parse this frame */
				/* can be negative, if all bits needed are in the reservoir */
				if (bits < 0)
					bits = 0

				/* read just as many bytes as necessary before decoding */
				mp.dsize = (bits + 7) / 8

				/* this will force mpglib to read entire frame before decoding */
				/* mp.dsize= mp.framesize - mp.ssize; */

			} else {
				/* Layers 1 and 2 */

				/* check if there is enough input data */
				if (mp.fr.framesize > mp.bsize)
					return MPGLib.MP3_NEED_MORE

				/*
				 * takes care that the right amount of data is copied into
				 * wordpointer
				 */
				mp.dsize = mp.fr.framesize
				mp.ssize = 0
			}

			mp.side_parsed = true
		}

		/* now decode main data */
		iret = MPGLib.MP3_NEED_MORE
		if (!mp.data_parsed) {
			if (mp.dsize > mp.bsize) {
				return MPGLib.MP3_NEED_MORE
			}

			copy_mp(mp, mp.dsize, mp.wordpointer, mp.wordpointerPos)

			done.pb = 0

			/* do_layer3(&mp.fr,(unsigned char *) out,done); */
			when (mp.fr.lay) {
				1 -> {
					if (mp.fr.error_protection)
						common.getbits(mp, 16)

					layer1.do_layer1(mp, out, done)
				}

				2 -> {
					if (mp.fr.error_protection)
						common.getbits(mp, 16)

					layer2.do_layer2<Any>(mp, out, done, synth)
				}

				3 -> layer3.do_layer3(mp, out, done, synth)
				else -> warningProcessor?.invoke("hip: invalid layer ${mp.fr.lay}")
			}

			mp.wordpointer = mp.bsspace[mp.bsnum]
			mp.wordpointerPos = 512 + mp.ssize + mp.dsize

			mp.data_parsed = true
			iret = MPGLib.MP3_OK
		}

		/*
		 * remaining bits are ancillary data, or reservoir for next frame If
		 * free format, scan stream looking for next frame to determine
		 * mp.framesize
		 */
		if (mp.free_format) {
			if (mp.old_free_format) {
				/* free format. bitrate must not vary */
				mp.framesize = mp.fsizeold_nopadding + mp.fr.padding
			} else {
				bytes = sync_buffer(mp, true)
				if (bytes < 0)
					return iret
				mp.framesize = bytes + mp.ssize + mp.dsize
				mp.fsizeold_nopadding = mp.framesize - mp.fr.padding
			}
		}

		/* buffer the ancillary data and reservoir for next frame */
		bytes = mp.framesize - (mp.ssize + mp.dsize)
		if (bytes > mp.bsize) {
			return iret
		}

		if (bytes > 0) {
			val size: Int
			copy_mp(mp, bytes, mp.wordpointer, mp.wordpointerPos)
			mp.wordpointerPos += bytes

			size = mp.wordpointerPos - 512
			if (size > MPG123.MAXFRAMESIZE) {
				warningProcessor?.invoke("hip: fatal error.  MAXFRAMESIZE not large enough.")
			}

		}

		/* the above frame is completely parsed. start looking for next frame */
		mp.fsizeold = mp.framesize
		mp.old_free_format = mp.free_format
		mp.framesize = 0
		mp.header_parsed = false
		mp.side_parsed = false
		mp.data_parsed = false

		return iret
	}

	fun decodeMP3(
		mp: MPGLib.mpstr_tag, `in`: ByteArray, bufferPos: Int, isize: Int,
		out: FloatArray, osize: Int, done: MPGLib.ProcessedBytes
	): Int {
		if (osize < 2304) {
			warningProcessor?.invoke("hip: Insufficient memory for decoding buffer $osize")
			return MPGLib.MP3_ERR
		}

		/* passing pointers to the functions which clip the samples */
		val synth = object : ISynth {

			override fun synth_1to1_mono_ptr(
				mp: MPGLib.mpstr_tag, `in`: FloatArray, inPos: Int,
				out: FloatArray, p: MPGLib.ProcessedBytes
			): Int {
				return decode.synth1to1mono(mp, `in`, inPos, out, p)
			}

			override fun synth_1to1_ptr(
				mp: MPGLib.mpstr_tag, `in`: FloatArray, inPos: Int,
				i: Int, out: FloatArray, p: MPGLib.ProcessedBytes
			): Int {
				return decode.synth_1to1(mp, `in`, inPos, i, out, p)
			}

		}
		return decodeMP3_clipchoice(mp, `in`, bufferPos, isize, out, done, synth)
	}

	fun decodeMP3_unclipped(
		mp: MPGLib.mpstr_tag, `in`: ByteArray, bufferPos: Int,
		isize: Int, out: FloatArray, osize: Int, done: MPGLib.ProcessedBytes
	): Int {
		/*
		 * we forbid input with more than 1152 samples per channel for output in
		 * unclipped mode
		 */
		if (osize < 1152 * 2) {
			warningProcessor?.invoke("hip: out space too small for unclipped mode")
			return MPGLib.MP3_ERR
		}

		val synth = object : ISynth {

			override fun synth_1to1_mono_ptr(
				mp: MPGLib.mpstr_tag, `in`: FloatArray, inPos: Int,
				out: FloatArray, p: MPGLib.ProcessedBytes
			): Int {
				decode.synth1to1monoUnclipped(mp, `in`, inPos, out, p)
				return 0
			}

			override fun synth_1to1_ptr(
				mp: MPGLib.mpstr_tag, `in`: FloatArray, inPos: Int,
				i: Int, out: FloatArray, p: MPGLib.ProcessedBytes
			): Int {
				decode.synth_1to1_unclipped(mp, `in`, inPos, i, out, p)
				return 0
			}

		}
		/* passing pointers to the functions which don't clip the samples */
		return decodeMP3_clipchoice(mp, `in`, bufferPos, isize, out, done, synth)
	}

	interface ISynth {
		fun synth_1to1_mono_ptr(mp: MPGLib.mpstr_tag, `in`: FloatArray, inPos: Int, out: FloatArray, p: MPGLib.ProcessedBytes): Int

		fun synth_1to1_ptr(mp: MPGLib.mpstr_tag, `in`: FloatArray, inPos: Int, i: Int, out: FloatArray, p: MPGLib.ProcessedBytes): Int
	}
}

object L2Tables {
	val alloc_0 = arrayOf(
		al_table2(4, 0), al_table2(5, 3), al_table2(3, -3), al_table2(4, -7), al_table2(5, -15), al_table2(6, -31), al_table2(7, -63), al_table2(8, -127), al_table2(9, -255), al_table2(10, -511), al_table2(11, -1023), al_table2(12, -2047), al_table2(13, -4095), al_table2(14, -8191), al_table2(15, -16383), al_table2(16, -32767),
		al_table2(4, 0), al_table2(5, 3), al_table2(3, -3), al_table2(4, -7), al_table2(5, -15), al_table2(6, -31), al_table2(7, -63), al_table2(8, -127), al_table2(9, -255), al_table2(10, -511), al_table2(11, -1023), al_table2(12, -2047), al_table2(13, -4095), al_table2(14, -8191), al_table2(15, -16383), al_table2(16, -32767),
		al_table2(4, 0), al_table2(5, 3), al_table2(3, -3), al_table2(4, -7), al_table2(5, -15), al_table2(6, -31), al_table2(7, -63), al_table2(8, -127), al_table2(9, -255), al_table2(10, -511), al_table2(11, -1023), al_table2(12, -2047), al_table2(13, -4095), al_table2(14, -8191), al_table2(15, -16383), al_table2(16, -32767),
		al_table2(4, 0), al_table2(5, 3), al_table2(7, 5), al_table2(3, -3), al_table2(10, 9), al_table2(4, -7), al_table2(5, -15), al_table2(6, -31), al_table2(7, -63), al_table2(8, -127), al_table2(9, -255), al_table2(10, -511), al_table2(11, -1023), al_table2(12, -2047), al_table2(13, -4095), al_table2(16, -32767),
		al_table2(4, 0), al_table2(5, 3), al_table2(7, 5), al_table2(3, -3), al_table2(10, 9), al_table2(4, -7), al_table2(5, -15), al_table2(6, -31), al_table2(7, -63), al_table2(8, -127), al_table2(9, -255), al_table2(10, -511), al_table2(11, -1023), al_table2(12, -2047), al_table2(13, -4095), al_table2(16, -32767),
		al_table2(4, 0), al_table2(5, 3), al_table2(7, 5), al_table2(3, -3), al_table2(10, 9), al_table2(4, -7), al_table2(5, -15), al_table2(6, -31), al_table2(7, -63), al_table2(8, -127), al_table2(9, -255), al_table2(10, -511), al_table2(11, -1023), al_table2(12, -2047), al_table2(13, -4095), al_table2(16, -32767),
		al_table2(4, 0), al_table2(5, 3), al_table2(7, 5), al_table2(3, -3), al_table2(10, 9), al_table2(4, -7), al_table2(5, -15), al_table2(6, -31), al_table2(7, -63), al_table2(8, -127), al_table2(9, -255), al_table2(10, -511), al_table2(11, -1023), al_table2(12, -2047), al_table2(13, -4095), al_table2(16, -32767),
		al_table2(4, 0), al_table2(5, 3), al_table2(7, 5), al_table2(3, -3), al_table2(10, 9), al_table2(4, -7), al_table2(5, -15), al_table2(6, -31), al_table2(7, -63), al_table2(8, -127), al_table2(9, -255), al_table2(10, -511), al_table2(11, -1023), al_table2(12, -2047), al_table2(13, -4095), al_table2(16, -32767),
		al_table2(4, 0), al_table2(5, 3), al_table2(7, 5), al_table2(3, -3), al_table2(10, 9), al_table2(4, -7), al_table2(5, -15), al_table2(6, -31), al_table2(7, -63), al_table2(8, -127), al_table2(9, -255), al_table2(10, -511), al_table2(11, -1023), al_table2(12, -2047), al_table2(13, -4095), al_table2(16, -32767),
		al_table2(4, 0), al_table2(5, 3), al_table2(7, 5), al_table2(3, -3), al_table2(10, 9), al_table2(4, -7), al_table2(5, -15), al_table2(6, -31), al_table2(7, -63), al_table2(8, -127), al_table2(9, -255), al_table2(10, -511), al_table2(11, -1023), al_table2(12, -2047), al_table2(13, -4095), al_table2(16, -32767),
		al_table2(4, 0), al_table2(5, 3), al_table2(7, 5), al_table2(3, -3), al_table2(10, 9), al_table2(4, -7), al_table2(5, -15), al_table2(6, -31), al_table2(7, -63), al_table2(8, -127), al_table2(9, -255), al_table2(10, -511), al_table2(11, -1023), al_table2(12, -2047), al_table2(13, -4095), al_table2(16, -32767),
		al_table2(3, 0), al_table2(5, 3), al_table2(7, 5), al_table2(3, -3), al_table2(10, 9), al_table2(4, -7), al_table2(5, -15), al_table2(16, -32767),
		al_table2(3, 0), al_table2(5, 3), al_table2(7, 5), al_table2(3, -3), al_table2(10, 9), al_table2(4, -7), al_table2(5, -15), al_table2(16, -32767),
		al_table2(3, 0), al_table2(5, 3), al_table2(7, 5), al_table2(3, -3), al_table2(10, 9), al_table2(4, -7), al_table2(5, -15), al_table2(16, -32767),
		al_table2(3, 0), al_table2(5, 3), al_table2(7, 5), al_table2(3, -3), al_table2(10, 9), al_table2(4, -7), al_table2(5, -15), al_table2(16, -32767),
		al_table2(3, 0), al_table2(5, 3), al_table2(7, 5), al_table2(3, -3), al_table2(10, 9), al_table2(4, -7), al_table2(5, -15), al_table2(16, -32767),
		al_table2(3, 0), al_table2(5, 3), al_table2(7, 5), al_table2(3, -3), al_table2(10, 9), al_table2(4, -7), al_table2(5, -15), al_table2(16, -32767),
		al_table2(3, 0), al_table2(5, 3), al_table2(7, 5), al_table2(3, -3), al_table2(10, 9), al_table2(4, -7), al_table2(5, -15), al_table2(16, -32767),
		al_table2(3, 0), al_table2(5, 3), al_table2(7, 5), al_table2(3, -3), al_table2(10, 9), al_table2(4, -7), al_table2(5, -15), al_table2(16, -32767),
		al_table2(3, 0), al_table2(5, 3), al_table2(7, 5), al_table2(3, -3), al_table2(10, 9), al_table2(4, -7), al_table2(5, -15), al_table2(16, -32767),
		al_table2(3, 0), al_table2(5, 3), al_table2(7, 5), al_table2(3, -3), al_table2(10, 9), al_table2(4, -7), al_table2(5, -15), al_table2(16, -32767),
		al_table2(3, 0), al_table2(5, 3), al_table2(7, 5), al_table2(3, -3), al_table2(10, 9), al_table2(4, -7), al_table2(5, -15), al_table2(16, -32767),
		al_table2(3, 0), al_table2(5, 3), al_table2(7, 5), al_table2(3, -3), al_table2(10, 9), al_table2(4, -7), al_table2(5, -15), al_table2(16, -32767),
		al_table2(2, 0), al_table2(5, 3), al_table2(7, 5), al_table2(16, -32767),
		al_table2(2, 0), al_table2(5, 3), al_table2(7, 5), al_table2(16, -32767),
		al_table2(2, 0), al_table2(5, 3), al_table2(7, 5), al_table2(16, -32767),
		al_table2(2, 0), al_table2(5, 3), al_table2(7, 5), al_table2(16, -32767)
	)
	val alloc_1 = arrayOf(
		al_table2(4, 0),
		al_table2(5, 3),
		al_table2(3, -3),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(8, -127),
		al_table2(9, -255),
		al_table2(10, -511),
		al_table2(11, -1023),
		al_table2(12, -2047),
		al_table2(13, -4095),
		al_table2(14, -8191),
		al_table2(15, -16383),
		al_table2(16, -32767),
		al_table2(4, 0),
		al_table2(5, 3),
		al_table2(3, -3),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(8, -127),
		al_table2(9, -255),
		al_table2(10, -511),
		al_table2(11, -1023),
		al_table2(12, -2047),
		al_table2(13, -4095),
		al_table2(14, -8191),
		al_table2(15, -16383),
		al_table2(16, -32767),
		al_table2(4, 0),
		al_table2(5, 3),
		al_table2(3, -3),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(8, -127),
		al_table2(9, -255),
		al_table2(10, -511),
		al_table2(11, -1023),
		al_table2(12, -2047),
		al_table2(13, -4095),
		al_table2(14, -8191),
		al_table2(15, -16383),
		al_table2(16, -32767),
		al_table2(4, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(3, -3),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(8, -127),
		al_table2(9, -255),
		al_table2(10, -511),
		al_table2(11, -1023),
		al_table2(12, -2047),
		al_table2(13, -4095),
		al_table2(16, -32767),
		al_table2(4, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(3, -3),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(8, -127),
		al_table2(9, -255),
		al_table2(10, -511),
		al_table2(11, -1023),
		al_table2(12, -2047),
		al_table2(13, -4095),
		al_table2(16, -32767),
		al_table2(4, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(3, -3),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(8, -127),
		al_table2(9, -255),
		al_table2(10, -511),
		al_table2(11, -1023),
		al_table2(12, -2047),
		al_table2(13, -4095),
		al_table2(16, -32767),
		al_table2(4, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(3, -3),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(8, -127),
		al_table2(9, -255),
		al_table2(10, -511),
		al_table2(11, -1023),
		al_table2(12, -2047),
		al_table2(13, -4095),
		al_table2(16, -32767),
		al_table2(4, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(3, -3),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(8, -127),
		al_table2(9, -255),
		al_table2(10, -511),
		al_table2(11, -1023),
		al_table2(12, -2047),
		al_table2(13, -4095),
		al_table2(16, -32767),
		al_table2(4, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(3, -3),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(8, -127),
		al_table2(9, -255),
		al_table2(10, -511),
		al_table2(11, -1023),
		al_table2(12, -2047),
		al_table2(13, -4095),
		al_table2(16, -32767),
		al_table2(4, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(3, -3),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(8, -127),
		al_table2(9, -255),
		al_table2(10, -511),
		al_table2(11, -1023),
		al_table2(12, -2047),
		al_table2(13, -4095),
		al_table2(16, -32767),
		al_table2(4, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(3, -3),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(8, -127),
		al_table2(9, -255),
		al_table2(10, -511),
		al_table2(11, -1023),
		al_table2(12, -2047),
		al_table2(13, -4095),
		al_table2(16, -32767),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(3, -3),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(16, -32767),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(3, -3),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(16, -32767),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(3, -3),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(16, -32767),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(3, -3),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(16, -32767),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(3, -3),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(16, -32767),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(3, -3),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(16, -32767),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(3, -3),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(16, -32767),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(3, -3),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(16, -32767),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(3, -3),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(16, -32767),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(3, -3),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(16, -32767),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(3, -3),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(16, -32767),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(3, -3),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(16, -32767),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(16, -32767),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(16, -32767),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(16, -32767),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(16, -32767),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(16, -32767),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(16, -32767),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(16, -32767)
	)
	val alloc_2 = arrayOf(
		al_table2(4, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(8, -127),
		al_table2(9, -255),
		al_table2(10, -511),
		al_table2(11, -1023),
		al_table2(12, -2047),
		al_table2(13, -4095),
		al_table2(14, -8191),
		al_table2(15, -16383),
		al_table2(4, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(8, -127),
		al_table2(9, -255),
		al_table2(10, -511),
		al_table2(11, -1023),
		al_table2(12, -2047),
		al_table2(13, -4095),
		al_table2(14, -8191),
		al_table2(15, -16383),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63)
	)
	val alloc_3 = arrayOf(
		al_table2(4, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(8, -127),
		al_table2(9, -255),
		al_table2(10, -511),
		al_table2(11, -1023),
		al_table2(12, -2047),
		al_table2(13, -4095),
		al_table2(14, -8191),
		al_table2(15, -16383),
		al_table2(4, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(8, -127),
		al_table2(9, -255),
		al_table2(10, -511),
		al_table2(11, -1023),
		al_table2(12, -2047),
		al_table2(13, -4095),
		al_table2(14, -8191),
		al_table2(15, -16383),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63)
	)
	val alloc_4 = arrayOf(
		al_table2(4, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(3, -3),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(8, -127),
		al_table2(9, -255),
		al_table2(10, -511),
		al_table2(11, -1023),
		al_table2(12, -2047),
		al_table2(13, -4095),
		al_table2(14, -8191),
		al_table2(4, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(3, -3),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(8, -127),
		al_table2(9, -255),
		al_table2(10, -511),
		al_table2(11, -1023),
		al_table2(12, -2047),
		al_table2(13, -4095),
		al_table2(14, -8191),
		al_table2(4, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(3, -3),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(8, -127),
		al_table2(9, -255),
		al_table2(10, -511),
		al_table2(11, -1023),
		al_table2(12, -2047),
		al_table2(13, -4095),
		al_table2(14, -8191),
		al_table2(4, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(3, -3),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(8, -127),
		al_table2(9, -255),
		al_table2(10, -511),
		al_table2(11, -1023),
		al_table2(12, -2047),
		al_table2(13, -4095),
		al_table2(14, -8191),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(3, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(4, -7),
		al_table2(5, -15),
		al_table2(6, -31),
		al_table2(7, -63),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9),
		al_table2(2, 0),
		al_table2(5, 3),
		al_table2(7, 5),
		al_table2(10, 9)
	)

	data class al_table2(val bits: Short, val d: Short)
}

/**
 * Control Parameters set by User. These parameters are here for backwards
 * compatibility with the old, non-shared lib API. Please use the
 * lame_set_variablename() functions below

 * @author Ken
 */
class LameGlobalFlags {
	var num_samples = -1
	var inNumChannels = 2
	var inSampleRate = 44100
}

class Layer1(private val common: Common, private val decode: Decode) {

	private fun I_step_one(mp: MPGLib.mpstr_tag, balloc: IntArray, scale_index: IntArray, fr: Frame) {
		var ba = 0
		var sca = 0

		assert(fr.stereo == 1 || fr.stereo == 2)
		if (fr.stereo == 2) {
			var i: Int
			val jsbound = fr.jsbound
			i = 0
			while (i < jsbound) {
				balloc[ba++] = common.getbits(mp, 4)
				balloc[ba++] = common.getbits(mp, 4)
				i++
			}
			i = jsbound
			while (i < MPG123.SBLIMIT) {
				balloc[ba++] = common.getbits(mp, 4)
				i++
			}

			ba = 0

			i = 0
			while (i < jsbound) {
				if (balloc[ba]++ != 0) scale_index[sca++] = common.getbits(mp, 6)
				if (balloc[ba++] != 0) scale_index[sca++] = common.getbits(mp, 6)
				i++
			}
			i = jsbound
			while (i < MPG123.SBLIMIT) {
				if (balloc[ba++] != 0) {
					scale_index[sca++] = common.getbits(mp, 6)
					scale_index[sca++] = common.getbits(mp, 6)
				}
				i++
			}
		} else {
			var i: Int
			i = 0
			while (i < MPG123.SBLIMIT) {
				balloc[ba++] = common.getbits(mp, 4)
				i++
			}
			ba = 0
			i = 0
			while (i < MPG123.SBLIMIT) {
				if (balloc[ba++] != 0) scale_index[sca++] = common.getbits(mp, 6)
				i++
			}
		}
	}

	private fun I_step_two(
		mp: MPGLib.mpstr_tag,
		fraction: Array<FloatArray>,
		balloc: IntArray,
		scale_index: IntArray,
		fr: Frame
	) {
		var i: Int
		var n: Int
		val smpb = IntArray(2 * MPG123.SBLIMIT) /* values: 0-65535 */
		var sample: Int
		var ba = 0
		var sca = 0

		assert(fr.stereo == 1 || fr.stereo == 2)
		if (fr.stereo == 2) {
			val jsbound = fr.jsbound
			var f0 = 0
			var f1 = 0
			ba = 0
			sample = 0
			i = 0
			while (i < jsbound) {
				n = balloc[ba++]
				if (n != 0) smpb[sample++] = common.getbits(mp, n + 1)
				n = balloc[ba++]
				if (n != 0) smpb[sample++] = common.getbits(mp, n + 1)
				i++
			}
			i = jsbound
			while (i < MPG123.SBLIMIT) {
				n = balloc[ba++]
				if (n != 0) smpb[sample++] = common.getbits(mp, n + 1)
				i++
			}
			ba = 0
			sample = 0
			i = 0
			while (i < jsbound) {
				n = balloc[ba++]
				if (n != 0) {
					fraction[0][f0++] = ((-1 shl n) + smpb[sample++] + 1).toFloat() *
							common.muls[n + 1][scale_index[sca++]]
				} else {
					fraction[0][f0++] = 0.0f
				}
				n = balloc[ba++]
				if (n != 0) {
					fraction[1][f1++] = ((-1 shl n) + smpb[sample++] + 1).toFloat() *
							common.muls[n + 1][scale_index[sca++]]
				} else {
					fraction[1][f1++] = 0.0f
				}
				i++
			}
			i = jsbound
			while (i < MPG123.SBLIMIT) {
				n = balloc[ba++]
				if (n != 0) {
					val samp = ((-1 shl n) + smpb[sample++] + 1).toFloat()
					fraction[0][f0++] = samp * common.muls[n + 1][scale_index[sca++]]
					fraction[1][f1++] = samp * common.muls[n + 1][scale_index[sca++]]
				} else {
					fraction[0][f0++] = 0.0f
					fraction[1][f1++] = 0.0f
				}
				i++
			}
			i = fr.down_sample_sblimit
			while (i < 32) {
				fraction[0][i] = 0.0f
				fraction[1][i] = 0.0f
				i++
			}
		} else {
			var f0 = 0
			ba = 0
			sample = 0
			i = 0
			while (i < MPG123.SBLIMIT) {
				n = balloc[ba++]
				if (n != 0) smpb[sample++] = common.getbits(mp, n + 1)
				i++
			}
			ba = 0
			sample = 0
			i = 0
			while (i < MPG123.SBLIMIT) {
				n = balloc[ba++]
				if (n != 0) {
					fraction[0][f0++] = ((-1 shl n) + smpb[sample++] + 1).toFloat() *
							common.muls[n + 1][scale_index[sca++]]
				} else {
					fraction[0][f0++] = 0.0f
				}
				i++
			}
			i = fr.down_sample_sblimit
			while (i < 32) {
				fraction[0][i] = 0.0f
				i++
			}
		}
	}

	fun do_layer1(mp: MPGLib.mpstr_tag, pcm_sample: FloatArray, pcm_point: MPGLib.ProcessedBytes): Int {
		var clip = 0
		val balloc = IntArray(2 * MPG123.SBLIMIT)
		val scale_index = IntArray(2 * MPG123.SBLIMIT)
		val fraction = Array(2) { FloatArray(MPG123.SBLIMIT) }
		val fr = mp.fr
		val stereo = fr.stereo
		var single = fr.single

		fr.jsbound = if (fr.mode == MPG123.MPG_MD_JOINT_STEREO) (fr.mode_ext shl 2) + 4 else 32

		if (stereo == 1 || single == 3) single = 0

		I_step_one(mp, balloc, scale_index, fr)

		var i = 0
		while (i < MPG123.SCALE_BLOCK) {
			I_step_two(mp, fraction, balloc, scale_index, fr)

			if (single >= 0) {
				clip += decode.synth1to1mono(mp, fraction[single], 0, pcm_sample, pcm_point)
			} else {
				val p1 = MPGLib.ProcessedBytes()
				p1.pb = pcm_point.pb
				clip += decode.synth_1to1(mp, fraction[0], 0, 0, pcm_sample, p1)
				clip += decode.synth_1to1(mp, fraction[1], 0, 1, pcm_sample, pcm_point)
			}
			i++
		}

		return clip
	}
}

class Layer2(private val common: Common) {
	private val nul_tab = intArrayOf()
	private val grp_3tab = IntArray(32 * 3) /* used: 27 */
	private val grp_5tab = IntArray(128 * 3) /* used: 125 */
	private val grp_9tab = IntArray(1024 * 3) /* used: 729 */
	private val tables = arrayOf(grp_3tab, grp_5tab, grp_9tab)
	private val table =
		arrayOf<IntArray>(nul_tab, nul_tab, nul_tab, grp_3tab, nul_tab, grp_5tab, nul_tab, nul_tab, nul_tab, grp_9tab)
	private val base =
		arrayOf(intArrayOf(1, 0, 2), intArrayOf(17, 18, 0, 19, 20), intArrayOf(21, 1, 22, 23, 0, 24, 25, 2, 26))
	private val tablen = intArrayOf(3, 5, 9)
	private val tables2 =
		arrayOf(L2Tables.alloc_0, L2Tables.alloc_1, L2Tables.alloc_2, L2Tables.alloc_3, L2Tables.alloc_4)
	private val sblims = intArrayOf(27, 30, 8, 12, 30)
	private var itable: Int = 0
	private val scfsi_buf = IntArray(64)

	fun init_layer2() {
		for (i in 0..2) {
			itable = 0
			val len = tablen[i]
			for (j in 0 until len) {
				for (k in 0 until len) {
					for (l in 0 until len) {
						tables[i][itable++] = base[i][l]
						tables[i][itable++] = base[i][k]
						tables[i][itable++] = base[i][j]
					}
				}
			}
		}

		for (k in 0..26) {
			val m = mulmul[k]
			val table = common.muls[k]
			var tablePos = 0
			var j = 3
			var i = 0
			while (i < 63) {
				table[tablePos++] = (m * 2.0.pow(j.toDouble() / 3.0)).toFloat()
				i++
				j--
			}
			table[tablePos++] = 0.0f
		}
	}

	private fun II_step_one(mp: MPGLib.mpstr_tag, bit_alloc: IntArray, scale: IntArray, fr: Frame) {
		var scalePos = 0
		val stereo = fr.stereo - 1
		val sblimit = fr.II_sblimit
		val jsbound = fr.jsbound
		val sblimit2 = fr.II_sblimit shl stereo
		var alloc1 = 0
		var i: Int
		var scfsi: Int
		var bita: Int
		var sc: Int
		var step: Int

		bita = 0
		if (stereo != 0) {
			i = jsbound
			while (i != 0) {
				step = fr.alloc!![alloc1].bits.toInt()
				bit_alloc[bita++] = common.getbits(mp, step).toChar().toInt()
				bit_alloc[bita++] = common.getbits(mp, step).toChar().toInt()
				i--
				alloc1 += 1 shl step
			}
			i = sblimit - jsbound
			while (i != 0) {
				step = fr.alloc!![alloc1].bits.toInt()
				bit_alloc[bita + 0] = common.getbits(mp, step).toChar().toInt()
				bit_alloc[bita + 1] = bit_alloc[bita + 0]
				bita += 2
				i--
				alloc1 += 1 shl step
			}
			bita = 0
			scfsi = 0
			i = sblimit2
			while (i != 0) {
				if (bit_alloc[bita++] != 0) {
					scfsi_buf[scfsi++] = common.getbits_fast(mp, 2).toChar().toInt()
				}
				i--
			}
		} else { /* mono */

			i = sblimit
			while (i != 0) {
				step = fr.alloc!![alloc1].bits.toInt()
				bit_alloc[bita++] = common.getbits(mp, step).toChar().toInt()
				i--
				alloc1 += 1 shl step
			}
			bita = 0
			scfsi = 0
			i = sblimit
			while (i != 0) {
				if (bit_alloc[bita++] != 0) {
					scfsi_buf[scfsi++] = common.getbits_fast(mp, 2).toChar().toInt()
				}
				i--
			}
		}

		bita = 0
		scfsi = 0
		i = sblimit2
		while (i != 0) {
			if (bit_alloc[bita++] != 0)
				when (scfsi_buf[scfsi++]) {
					0 -> {
						scale[scalePos++] = common.getbits_fast(mp, 6)
						scale[scalePos++] = common.getbits_fast(mp, 6)
						scale[scalePos++] = common.getbits_fast(mp, 6)
					}
					1 -> {
						sc = common.getbits_fast(mp, 6)
						scale[scalePos++] = sc
						scale[scalePos++] = sc
						scale[scalePos++] = common.getbits_fast(mp, 6)
					}
					2 -> {
						sc = common.getbits_fast(mp, 6)
						scale[scalePos++] = sc
						scale[scalePos++] = sc
						scale[scalePos++] = sc
					}
					else /* case 3 */ -> {
						scale[scalePos++] = common.getbits_fast(mp, 6)
						sc = common.getbits_fast(mp, 6)
						scale[scalePos++] = sc
						scale[scalePos++] = sc
					}
				}
			i--
		}

	}

	private fun II_step_two(
		mp: MPGLib.mpstr_tag, bit_alloc: IntArray,
		fraction: Array<Array<FloatArray>>, scale: IntArray, fr: Frame,
		x1: Int
	) {
		var scalePos = 0
		var k: Int
		var ba: Int
		val stereo = fr.stereo
		val sblimit = fr.II_sblimit
		val jsbound = fr.jsbound
		var alloc2: Int
		var alloc1 = 0
		var bita = 0
		var d1: Int
		var step: Int

		run {
			var i = 0
			while (i < jsbound) {
				step = fr.alloc!![alloc1].bits.toInt()
				for (j in 0 until stereo) {
					ba = bit_alloc[bita++]
					if (ba != 0) {
						alloc2 = alloc1 + ba
						k = fr.alloc!![alloc2].bits.toInt()
						d1 = fr.alloc!![alloc2].d.toInt();
						if (d1 < 0) {
							val cm = common.muls[k][scale[scalePos + x1]]
							fraction[j][0][i] = (common.getbits(mp, k) + d1).toFloat() * cm
							fraction[j][1][i] = (common.getbits(mp, k) + d1).toFloat() * cm
							fraction[j][2][i] = (common.getbits(mp, k) + d1).toFloat() * cm
						} else {
							val idx: Int
							var tab: Int
							val m = scale[scalePos + x1]
							idx = common.getbits(mp, k)
							tab = idx + idx + idx
							fraction[j][0][i] = common.muls[table[d1][tab++]][m]
							fraction[j][1][i] = common.muls[table[d1][tab++]][m]
							fraction[j][2][i] = common.muls[table[d1][tab]][m]
						}
						scalePos += 3
					} else {
						fraction[j][0][i] = 0.0f
						fraction[j][1][i] = 0.0f
						fraction[j][2][i] = 0.0f
					}
				}
				i++
				alloc1 += 1 shl step
			}
		}

		run {
			var i = jsbound
			while (i < sblimit) {
				step = fr.alloc!![alloc1].bits.toInt()
				bita++ /* channel 1 and channel 2 bitalloc are the same */
				ba = bit_alloc[bita++]
				if (ba != 0) {
					alloc2 = alloc1 + ba
					k = fr.alloc!![alloc2].bits.toInt()
					d1 = fr.alloc!![alloc2].d.toInt()
					if (d1 < 0) {
						var cm: Float
						cm = common.muls[k][scale[scalePos + x1 + 3]]
						fraction[0][0][i] = (common.getbits(mp, k) + d1).toFloat()
						fraction[0][1][i] = (common.getbits(mp, k) + d1).toFloat()
						fraction[0][2][i] = (common.getbits(mp, k) + d1).toFloat()

						fraction[1][0][i] = fraction[0][0][i] * cm
						fraction[1][1][i] = fraction[0][1][i] * cm
						fraction[1][2][i] = fraction[0][2][i] * cm
						cm = common.muls[k][scale[scalePos + x1]]
						fraction[0][0][i] *= cm
						fraction[0][1][i] *= cm
						fraction[0][2][i] *= cm
					} else {
						val idx: Int
						var tab: Int
						val m1: Int
						val m2: Int
						m1 = scale[scalePos + x1]
						m2 = scale[scalePos + x1 + 3]
						idx = common.getbits(mp, k).toInt()
						tab = idx + idx + idx
						fraction[0][0][i] = common.muls[table[d1][tab]][m1]
						fraction[1][0][i] = common.muls[table[d1][tab++]][m2]
						fraction[0][1][i] = common.muls[table[d1][tab]][m1]
						fraction[1][1][i] = common.muls[table[d1][tab++]][m2]
						fraction[0][2][i] = common.muls[table[d1][tab]][m1]
						fraction[1][2][i] = common.muls[table[d1][tab]][m2]
					}
					scalePos += 6
				} else {
					fraction[0][0][i] = 0.0f
					fraction[0][1][i] = 0.0f
					fraction[0][2][i] = 0.0f
					fraction[1][0][i] = 0.0f
					fraction[1][1][i] = 0.0f
					fraction[1][2][i] = 0.0f
				}
				i++
				alloc1 += 1 shl step
			}
		}

		for (i in sblimit until MPG123.SBLIMIT) {
			for (j in 0 until stereo) {
				fraction[j][0][i] = 0.0f
				fraction[j][1][i] = 0.0f
				fraction[j][2][i] = 0.0f
			}
		}

	}

	private fun II_select_table(fr: Frame) {
		val table: Int
		val sblim: Int

		if (fr.lsf != 0) {
			table = 4
		} else {
			table = translate[fr.sampling_frequency][2 - fr.stereo][fr.bitrate_index]
		}
		sblim = sblims[table]

		fr.alloc = tables2[table]
		fr.II_sblimit = sblim
	}

	fun <T> do_layer2(mp: MPGLib.mpstr_tag, pcm_sample: FloatArray, pcm_point: MPGLib.ProcessedBytes, synth: Interface.ISynth): Int {
		var clip = 0
		var i: Int
		var j: Int
		val fraction = Array(2) { Array(4) { FloatArray(MPG123.SBLIMIT) } }
		val bit_alloc = IntArray(64)
		val scale = IntArray(192)
		val fr = mp.fr
		val stereo = fr.stereo
		var single = fr.single

		II_select_table(fr)
		fr.jsbound = if (fr.mode == MPG123.MPG_MD_JOINT_STEREO) (fr.mode_ext shl 2) + 4 else fr.II_sblimit

		if (stereo == 1 || single == 3) single = 0

		II_step_one(mp, bit_alloc, scale, fr)

		i = 0
		while (i < MPG123.SCALE_BLOCK) {
			II_step_two(mp, bit_alloc, fraction, scale, fr, i shr 2)
			j = 0
			while (j < 3) {
				if (single >= 0) {
					clip += synth.synth_1to1_mono_ptr(mp, fraction[single][j], 0, pcm_sample, pcm_point)
				} else {
					val p1 = MPGLib.ProcessedBytes()
					p1.pb = pcm_point.pb
					clip += synth.synth_1to1_ptr(mp, fraction[0][j], 0, 0, pcm_sample, p1)
					clip += synth.synth_1to1_ptr(mp, fraction[1][j], 0, 1, pcm_sample, pcm_point)
				}
				j++
			}
			i++
		}

		return clip
	}

	companion object {

		private val mulmul = doubleArrayOf(
			0.0,
			-2.0 / 3.0,
			2.0 / 3.0,
			2.0 / 7.0,
			2.0 / 15.0,
			2.0 / 31.0,
			2.0 / 63.0,
			2.0 / 127.0,
			2.0 / 255.0,
			2.0 / 511.0,
			2.0 / 1023.0,
			2.0 / 2047.0,
			2.0 / 4095.0,
			2.0 / 8191.0,
			2.0 / 16383.0,
			2.0 / 32767.0,
			2.0 / 65535.0,
			-4.0 / 5.0,
			-2.0 / 5.0,
			2.0 / 5.0,
			4.0 / 5.0,
			-8.0 / 9.0,
			-4.0 / 9.0,
			-2.0 / 9.0,
			2.0 / 9.0,
			4.0 / 9.0,
			8.0 / 9.0
		)
		private val translate = arrayOf(
			arrayOf(
				intArrayOf(0, 2, 2, 2, 2, 2, 2, 0, 0, 0, 1, 1, 1, 1, 1, 0),
				intArrayOf(0, 2, 2, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0)
			),
			arrayOf(
				intArrayOf(0, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0),
				intArrayOf(0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
			),
			arrayOf(
				intArrayOf(0, 3, 3, 3, 3, 3, 3, 0, 0, 0, 1, 1, 1, 1, 1, 0),
				intArrayOf(0, 3, 3, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0)
			)
		)
	}
}


@Suppress("UNUSED_CHANGED_VALUE")
class Layer3(private val common: Common) {
	private val ispow = FloatArray(8207)
	private val aa_ca = FloatArray(8)
	private val aa_cs = FloatArray(8)
	private val COS1 = Array(12) { FloatArray(6) }
	private val win = Array(4) { FloatArray(36) }
	private val win1 = Array(4) { FloatArray(36) }
	private val gainpow2 = FloatArray(256 + 118 + 4)
	private val COS9 = FloatArray(9)
	private var COS6_1: Float = 0.toFloat()
	private var COS6_2: Float = 0.toFloat()
	private val tfcos36 = FloatArray(9)
	private val tfcos12 = FloatArray(3)
	private val longLimit = Array(9) { IntArray(23) }
	private val shortLimit = Array(9) { IntArray(14) }
	private val mapbuf0 = Array(9) { IntArray(152) }
	private val mapbuf1 = Array(9) { IntArray(156) }
	private val mapbuf2 = Array(9) { IntArray(44) }
	private val map = Array<Array<IntArray>>(9) { Array<IntArray>(3) { intArrayOf() } }
	private val mapend = Array(9) { IntArray(3) }
	private val n_slen2 = IntArray(512)
	private val i_slen2 = IntArray(256)
	private val tan1_1 = FloatArray(16)
	private val tan2_1 = FloatArray(16)
	private val tan1_2 = FloatArray(16)
	private val tan2_2 = FloatArray(16)
	private val pow1_1 = Array(2) { FloatArray(16) }
	private val pow2_1 = Array(2) { FloatArray(16) }
	private val pow1_2 = Array(2) { FloatArray(16) }
	private val pow2_2 = Array(2) { FloatArray(16) }

	private val sideinfo = MPG123.III_sideinfo()
	private val hybridIn = Array(2) { FloatArray(MPG123.SBLIMIT * MPG123.SSLIMIT) }
	private val hybridOut = Array(2) { FloatArray(MPG123.SSLIMIT * MPG123.SBLIMIT) }

	private fun get1bit(mp: MPGLib.mpstr_tag): Int {
		var rval = mp.wordpointer[mp.wordpointerPos].toInt() and 0xff shl mp.bitindex
		rval = rval and 0xff
		mp.bitindex++
		mp.wordpointerPos += mp.bitindex shr 3
		mp.bitindex = mp.bitindex and 7
		return rval shr 7
	}

	fun init_layer3(down_sample_sblimit: Int) {
		for (i in -256 until 118 + 4) gainpow2[i + 256] = 2.0.pow(-0.25 * (i + 210).toDouble()).toFloat()

		for (i in 0..8206) ispow[i] = i.toDouble().pow(4.0 / 3.0).toFloat()

		for (i in 0 until 8) {
			val sq = sqrt(1.0 + Ci[i] * Ci[i])
			aa_cs[i] = (1.0 / sq).toFloat()
			aa_ca[i] = (Ci[i] / sq).toFloat()
		}

		for (i in 0 until 18) {
			win[1][i] =
					(0.5 * sin(MPG123.M_PI / 72.0 * (2 * (i + 0) + 1).toDouble()) / cos(MPG123.M_PI * (2 * (i + 0) + 19).toDouble() / 72.0)).toFloat()
			win[0][i] = win[1][i]
			win[3][i + 18] =
					(0.5 * sin(MPG123.M_PI / 72.0 * (2 * (i + 18) + 1).toDouble()) / cos(MPG123.M_PI * (2 * (i + 18) + 19).toDouble() / 72.0)).toFloat()
			win[0][i + 18] = win[3][i + 18]
		}
		for (i in 0 until 6) {
			win[1][i + 18] = (0.5 / cos(MPG123.M_PI * (2 * (i + 18) + 19).toDouble() / 72.0)).toFloat()
			win[3][i + 12] = (0.5 / cos(MPG123.M_PI * (2 * (i + 12) + 19).toDouble() / 72.0)).toFloat()
			win[1][i + 24] =
					(0.5 * sin(MPG123.M_PI / 24.0 * (2 * i + 13).toDouble()) / cos(MPG123.M_PI * (2 * (i + 24) + 19).toDouble() / 72.0)).toFloat()
			win[1][i + 30] = 0.0f
			win[3][i] = 0.0f
			win[3][i + 6] =
					(0.5 * sin(MPG123.M_PI / 24.0 * (2 * i + 1).toDouble()) / cos(MPG123.M_PI * (2 * (i + 6) + 19).toDouble() / 72.0)).toFloat()
		}

		for (i in 0..8) COS9[i] = cos(MPG123.M_PI / 18.0 * i.toDouble()).toFloat()
		for (i in 0..8) tfcos36[i] = (0.5 / cos(MPG123.M_PI * (i * 2 + 1).toDouble() / 36.0)).toFloat()
		for (i in 0..2) tfcos12[i] = (0.5 / cos(MPG123.M_PI * (i * 2 + 1).toDouble() / 12.0)).toFloat()

		COS6_1 = cos(MPG123.M_PI / 6.0 * 1.toDouble()).toFloat()
		COS6_2 = cos(MPG123.M_PI / 6.0 * 2.toDouble()).toFloat()

		for (i in 0 until 12) {
			win[2][i] =
					(0.5 * sin(MPG123.M_PI / 24.0 * (2 * i + 1).toDouble()) / cos(MPG123.M_PI * (2 * i + 7).toDouble() / 24.0)).toFloat()
			for (j in 0..5) COS1[i][j] = cos(MPG123.M_PI / 24.0 * ((2 * i + 7) * (2 * j + 1)).toDouble()).toFloat()
		}

		for (j in 0 until 4) {
			for (i in 0 until len[j] step 2) win1[j][i] = +win[j][i]
			for (i in 1 until len[j] step 2) win1[j][i] = -win[j][i]
		}

		for (i in 0 until 16) {
			val t = tan(i.toDouble() * MPG123.M_PI / 12.0)
			tan1_1[i] = (t / (1.0 + t)).toFloat()
			tan2_1[i] = (1.0 / (1.0 + t)).toFloat()
			tan1_2[i] = (MPG123.M_SQRT2 * t / (1.0 + t)).toFloat()
			tan2_2[i] = (MPG123.M_SQRT2 / (1.0 + t)).toFloat()

			for (j in 0..1) {
				val base = 2.0.pow(-0.25 * (j + 1.0))
				var p1 = 1.0
				var p2 = 1.0
				if (i > 0) {
					if (i and 1 != 0) {
						p1 = base.pow((i + 1.0) * 0.5)
					} else {
						p2 = base.pow(i * 0.5)
					}
				}
				pow1_1[j][i] = p1.toFloat()
				pow2_1[j][i] = p2.toFloat()
				pow1_2[j][i] = (MPG123.M_SQRT2 * p1).toFloat()
				pow2_2[j][i] = (MPG123.M_SQRT2 * p2).toFloat()
			}
		}

		for (j in 0..8) {
			val bi = bandInfo[j]
			var lwin: Int

			map[j][0] = mapbuf0[j]
			var mp = 0
			var bdf = 0
			var i = 0
			var cb = 0
			while (cb < 8) {
				map[j][0][mp++] = bi.longDiff[bdf].toInt() shr 1
				map[j][0][mp++] = i
				map[j][0][mp++] = 3
				map[j][0][mp++] = cb
				cb++
				i += bi.longDiff[bdf++].toInt()
			}
			bdf = +3
			cb = 3
			while (cb < 13) {
				val l = bi.shortDiff[bdf++].toInt() shr 1
				lwin = 0
				while (lwin < 3) {
					map[j][0][mp++] = l
					map[j][0][mp++] = i + lwin
					map[j][0][mp++] = lwin
					map[j][0][mp++] = cb
					lwin++
				}
				i += 6 * l
				cb++
			}
			mapend[j][0] = mp

			map[j][1] = mapbuf1[j]
			mp = 0
			bdf = 0
			i = 0
			cb = 0
			while (cb < 13) {
				val l = bi.shortDiff[bdf++].toInt() shr 1
				lwin = 0
				while (lwin < 3) {
					map[j][1][mp++] = l
					map[j][1][mp++] = i + lwin
					map[j][1][mp++] = lwin
					map[j][1][mp++] = cb
					lwin++
				}
				i += 6 * l
				cb++
			}
			mapend[j][1] = mp

			map[j][2] = mapbuf2[j]
			mp = 0
			bdf = 0
			cb = 0
			while (cb < 22) {
				map[j][2][mp++] = bi.longDiff[bdf++].toInt() shr 1
				map[j][2][mp++] = cb
				cb++
			}
			mapend[j][2] = mp

		}

		for (j in 0..8) {
			for (i in 0..22) {
				longLimit[j][i] = (bandInfo[j].longIdx[i] - 1 + 8) / 18 + 1
				if (longLimit[j][i] > down_sample_sblimit) longLimit[j][i] = down_sample_sblimit
			}
			for (i in 0..13) {
				shortLimit[j][i] = (bandInfo[j].shortIdx[i] - 1) / 18 + 1
				if (shortLimit[j][i] > down_sample_sblimit) shortLimit[j][i] = down_sample_sblimit
			}
		}

		for (i in 0..4) for (j in 0..5) for (k in 0..5) {
			i_slen2[k + j * 6 + i * 36] = i or (j shl 3) or (k shl 6) or (3 shl 12)
		}
		for (i in 0..3) for (j in 0..3) for (k in 0..3) {
			i_slen2[(k + j * 4 + i * 16) + 180] = i or (j shl 3) or (k shl 6) or (4 shl 12)
		}
		for (i in 0..3) for (j in 0..2) {
			val n = j + i * 3
			i_slen2[n + 244] = i or (j shl 3) or (5 shl 12)
			n_slen2[n + 500] = i or (j shl 3) or (2 shl 12) or (1 shl 15)
		}

		for (i in 0..4) for (j in 0..4) for (k in 0..3) for (l in 0 until 4) {
			n_slen2[l + k * 4 + j * 16 + i * 80] = i or (j shl 3) or (k shl 6) or (l shl 9) or (0 shl 12)
		}
		for (i in 0..4) for (j in 0..4) for (k in 0..3) {
			n_slen2[(k + j * 4 + i * 20) + 400] = i or (j shl 3) or (k shl 6) or (1 shl 12)
		}
	}

	private fun III_get_side_info_1(
		mp: MPGLib.mpstr_tag,
		si: MPG123.III_sideinfo,
		stereo: Int,
		ms_stereo: Int,
		sfreq: Int,
		single: Int
	) {
		var ch: Int
		val powdiff = if (single == 3) 4 else 0

		si.main_data_begin = common.getbits(mp, 9)
		si.private_bits = if (stereo == 1) common.getbits_fast(mp, 5) else common.getbits_fast(mp, 3)

		ch = 0
		while (ch < stereo) {
			si.ch[ch].gr[0].scfsi = -1
			si.ch[ch].gr[1].scfsi = common.getbits_fast(mp, 4)
			ch++
		}

		var gr = 0
		while (gr < 2) {
			ch = 0
			while (ch < stereo) {
				val gr_infos = si.ch[ch].gr[gr]

				gr_infos.part2_3_length = common.getbits(mp, 12)
				gr_infos.big_values = common.getbits_fast(mp, 9)
				if (gr_infos.big_values > 288) {
					common.warningProcessor?.invoke("big_values too large! ${gr_infos.big_values}")
					gr_infos.big_values = 288
				}

				val qss = common.getbits_fast(mp, 8)
				gr_infos.pow2gain = gainpow2
				gr_infos.pow2gainPos = 256 - qss + powdiff
				mp.pinfo.qss[gr][ch] = qss

				if (ms_stereo != 0) gr_infos.pow2gainPos += 2
				gr_infos.scalefac_compress = common.getbits_fast(mp, 4)
				if (get1bit(mp) != 0) {
					gr_infos.block_type = common.getbits_fast(mp, 2)
					gr_infos.mixed_block_flag = get1bit(mp)
					gr_infos.table_select[0] = common.getbits_fast(mp, 5)
					gr_infos.table_select[1] = common.getbits_fast(mp, 5)

					gr_infos.table_select[2] = 0
					var i = 0
					while (i < 3) {
						val sbg = common.getbits_fast(mp, 3) shl 3
						gr_infos.full_gain[i] = gr_infos.pow2gain
						gr_infos.full_gainPos[i] = gr_infos.pow2gainPos + sbg
						mp.pinfo.sub_gain[gr][ch][i] = sbg / 8
						i++
					}

					if (gr_infos.block_type == 0) {
						common.warningProcessor?.invoke("Blocktype == 0 and window-switching == 1 not allowed.")
					}
					gr_infos.region1start = 36 shr 1
					gr_infos.region2start = 576 shr 1
				} else {
					var i = 0
					while (i < 3) {
						gr_infos.table_select[i] = common.getbits_fast(mp, 5)
						i++
					}
					val r0c = common.getbits_fast(mp, 4)
					val r1c = common.getbits_fast(mp, 3)
					gr_infos.region1start = bandInfo[sfreq].longIdx[r0c + 1].toInt() shr 1
					gr_infos.region2start = if (r0c + 1 + r1c + 1 < bandInfo[sfreq].longIdx.size) {
						bandInfo[sfreq].longIdx[r0c + 1 + r1c + 1].toInt() shr 1
					} else {
						bandInfo[sfreq].longDiff[r0c + 1 + r1c + 1 - bandInfo[sfreq].longIdx.size].toInt() shr 1
					}
					gr_infos.block_type = 0
					gr_infos.mixed_block_flag = 0
				}
				gr_infos.preflag = get1bit(mp)
				gr_infos.scalefac_scale = get1bit(mp)
				gr_infos.count1table_select = get1bit(mp)
				ch++
			}
			gr++
		}
	}

	/*
	 * Side Info for MPEG 2.0 / LSF
	 */
	private fun III_get_side_info_2(
		mp: MPGLib.mpstr_tag,
		si: MPG123.III_sideinfo,
		stereo: Int,
		ms_stereo: Int,
		sfreq: Int,
		single: Int
	) {
		val powdiff = if (single == 3) 4 else 0

		si.main_data_begin = common.getbits(mp, 8)

		si.private_bits = if (stereo == 1) get1bit(mp) else common.getbits_fast(mp, 2)

		var ch = 0
		while (ch < stereo) {
			val gr_infos = si.ch[ch].gr[0]
			gr_infos.part2_3_length = common.getbits(mp, 12)
			gr_infos.big_values = common.getbits_fast(mp, 9)
			if (gr_infos.big_values > 288) {
				common.warningProcessor?.invoke("big_values too large! ${gr_infos.big_values}")
				gr_infos.big_values = 288
			}
			val qss = common.getbits_fast(mp, 8)
			gr_infos.pow2gain = gainpow2
			gr_infos.pow2gainPos = 256 - qss + powdiff
			mp.pinfo.qss[0][ch] = qss

			if (ms_stereo != 0) gr_infos.pow2gainPos += 2
			gr_infos.scalefac_compress = common.getbits(mp, 9)
			if (get1bit(mp) != 0) {
				gr_infos.block_type = common.getbits_fast(mp, 2)
				gr_infos.mixed_block_flag = get1bit(mp)
				gr_infos.table_select[0] = common.getbits_fast(mp, 5)
				gr_infos.table_select[1] = common.getbits_fast(mp, 5)
				gr_infos.table_select[2] = 0
				for (i in 0 until 3) {
					val sbg = common.getbits_fast(mp, 3) shl 3
					gr_infos.full_gain[i] = gr_infos.pow2gain
					gr_infos.full_gainPos[i] = gr_infos.pow2gainPos + sbg
					mp.pinfo.sub_gain[0][ch][i] = sbg / 8
				}

				if (gr_infos.block_type == 0) common.warningProcessor?.invoke("Blocktype == 0 and window-switching == 1 not allowed.")

				gr_infos.region1start = if (gr_infos.block_type == 2) {
					if (sfreq == 8) 36 else 36 shr 1
				} else if (sfreq == 8) {
					108 shr 1
				} else {
					54 shr 1
				}
				gr_infos.region2start = 576 shr 1
			} else {
				for (i in 0 until 3) gr_infos.table_select[i] = common.getbits_fast(mp, 5)
				val r0c = common.getbits_fast(mp, 4)
				val r1c = common.getbits_fast(mp, 3)
				gr_infos.region1start = bandInfo[sfreq].longIdx[r0c + 1].toInt() shr 1
				gr_infos.region2start = bandInfo[sfreq].longIdx[r0c + 1 + r1c + 1].toInt() shr 1
				gr_infos.block_type = 0
				gr_infos.mixed_block_flag = 0
			}
			gr_infos.scalefac_scale = get1bit(mp)
			gr_infos.count1table_select = get1bit(mp)
			ch++
		}
	}

	private fun III_get_scale_factors_1(mp: MPGLib.mpstr_tag, scf: IntArray, gr_infos: MPG123.gr_info_s): Int {
		var scfPos = 0
		var numbits: Int
		val num0 = slen[0][gr_infos.scalefac_compress]
		val num1 = slen[1][gr_infos.scalefac_compress]

		if (gr_infos.block_type == 2) {
			var i = 18
			numbits = (num0 + num1) * 18

			if (gr_infos.mixed_block_flag != 0) {
				i = 8
				while (i != 0) {
					scf[scfPos++] = common.getbits_fast(mp, num0)
					i--
				}
				i = 9
				numbits -= num0 /* num0 * 17 + num1 * 18 */
			}

			while (i != 0) {
				scf[scfPos++] = common.getbits_fast(mp, num0)
				i--
			}
			i = 18
			while (i != 0) {
				scf[scfPos++] = common.getbits_fast(mp, num1)
				i--
			}
			scf[scfPos++] = 0
			scf[scfPos++] = 0
			scf[scfPos++] = 0 /* short[13][0..2] = 0 */
		} else {
			var i: Int
			val scfsi = gr_infos.scfsi

			if (scfsi < 0) { /* scfsi < 0 => granule == 0 */
				i = 11
				while (i != 0) {
					scf[scfPos++] = common.getbits_fast(mp, num0)
					i--
				}
				i = 10
				while (i != 0) {
					scf[scfPos++] = common.getbits_fast(mp, num1)
					i--
				}
				numbits = (num0 + num1) * 10 + num0
			} else {
				numbits = 0
				if (0 == scfsi and 0x8) {
					i = 6
					while (i != 0) {
						scf[scfPos++] = common.getbits_fast(mp, num0)
						i--
					}
					numbits += num0 * 6
				} else {
					scfPos += 6
				}

				if (0 == scfsi and 0x4) {
					i = 5
					while (i != 0) {
						scf[scfPos++] = common.getbits_fast(mp, num0)
						i--
					}
					numbits += num0 * 5
				} else {
					scfPos += 5
				}

				if (0 == scfsi and 0x2) {
					i = 5
					while (i != 0) {
						scf[scfPos++] = common.getbits_fast(mp, num1)
						i--
					}
					numbits += num1 * 5
				} else {
					scfPos += 5
				}

				if (0 == scfsi and 0x1) {
					i = 5
					while (i != 0) {
						scf[scfPos++] = common.getbits_fast(mp, num1)
						i--
					}
					numbits += num1 * 5
				} else {
					scfPos += 5
				}
			}

			scf[scfPos++] = 0 /* no l[21] in original sources */
		}
		return numbits
	}

	private fun III_get_scale_factors_2(mp: MPGLib.mpstr_tag, scf: IntArray, gr_infos: MPG123.gr_info_s, i_stereo: Int): Int {
		var scfPos = 0
		val pnt: IntArray
		var j: Int
		var slen: Int
		var numbits = 0

		if (i_stereo != 0) {
			slen = i_slen2[gr_infos.scalefac_compress shr 1]
		} else {
			slen = n_slen2[gr_infos.scalefac_compress]
		}

		gr_infos.preflag = slen shr 15 and 0x1

		var n = 0
		if (gr_infos.block_type == 2) {
			n++
			if (gr_infos.mixed_block_flag != 0) n++
		}

		pnt = stab[n][slen shr 12 and 0x7]

		var i = 0
		while (i < 4) {
			val num = slen and 0x7
			slen = slen shr 3
			if (num != 0) {
				j = 0
				while (j < pnt[i]) {
					scf[scfPos++] = common.getbits_fast(mp, num)
					j++
				}
				numbits += pnt[i] * num
			} else {
				j = 0
				while (j < pnt[i]) {
					scf[scfPos++] = 0
					j++
				}
			}
			i++
		}

		n = (n shl 1) + 1
		scf.fill(0, scfPos, scfPos + n)

		return numbits
	}

	private fun III_dequantize_sample(
		mp: MPGLib.mpstr_tag,
		xr: FloatArray,
		scf: IntArray,
		gr_infos: MPG123.gr_info_s,
		sfreq: Int,
		part2bits: Int
	): Int {
		var scfPos = 0
		val shift = 1 + gr_infos.scalefac_scale
		var xrpnt = xr
		var xrpntPos = 0
		val l = IntArray(3)
		var l3: Int = 0
		var part2remain = gr_infos.part2_3_length - part2bits
		val me: Int

		run {
			var i = MPG123.SBLIMIT * MPG123.SSLIMIT - xrpntPos shr 1
			while (i > 0) {
				xrpnt[xrpntPos++] = 0.0f
				xrpnt[xrpntPos++] = 0.0f
				i--
			}

			xrpnt = xr
			xrpntPos = 0
		}

		run {
			val bv = gr_infos.big_values
			val region1 = gr_infos.region1start
			val region2 = gr_infos.region2start

			l3 = (576 shr 1) - bv shr 1
			/*
			 * we may lose the 'odd' bit here !! check this later again
			 */
			if (bv <= region1) {
				l[0] = bv
				l[1] = 0
				l[2] = 0
			} else {
				l[0] = region1
				if (bv <= region2) {
					l[1] = bv - l[0]
					l[2] = 0
				} else {
					l[1] = region2 - l[0]
					l[2] = bv - region2
				}
			}
		}

		// MDH crash fix
		for (i in 0 until 3) {
			if (l[i] < 0) {
				common.warningProcessor?.invoke("hip: Bogus region length (${l[i]})")
				l[i] = 0
			}
		}

		if (gr_infos.block_type == 2) {
			var i: Int
			val max = IntArray(4)
			var step = 0
			var lwin = 0
			var cb = 0
			var v = 0.0f
			val m: IntArray
			var mc: Int
			var mPos = 0
			if (gr_infos.mixed_block_flag != 0) {
				max[0] = 2
				max[1] = 2
				max[2] = 2
				max[3] = -1
				m = map[sfreq][0]
				mPos = 0
				me = mapend[sfreq][0]
			} else {
				max[0] = -1
				max[1] = -1
				max[2] = -1
				max[3] = -1
				m = map[sfreq][1]
				mPos = 0
				me = mapend[sfreq][1]
			}

			mc = 0
			i = 0
			while (i < 2) {
				var lp = l[i]
				val h = Huffman.ht
				val hPos = gr_infos.table_select[i]

				while (lp != 0) {
					var x: Int = 0
					var y: Int = 0
					if (0 == mc) {
						mc = m[mPos++]
						xrpnt = xr
						xrpntPos = m[mPos++]
						lwin = m[mPos++]
						cb = m[mPos++]
						if (lwin == 3) {
							v = gr_infos.pow2gain[gr_infos.pow2gainPos + (scf[scfPos++] shl shift)]
							step = 1
						} else {
							v = gr_infos.full_gain[lwin][gr_infos.full_gainPos[lwin] + (scf[scfPos++] shl shift)]
							step = 3
						}
					}

					val val2 = h[hPos].table
					var valPos = 0
					while (true) {
						y = val2[valPos++].toInt()
						if (y >= 0) break
						if (get1bit(mp) != 0) valPos -= y
						part2remain--
					}
					x = y shr 4
					y = y and 0xf

					if (x == 15) {
						max[lwin] = cb
						part2remain -= h[hPos].linbits + 1
						x += common.getbits(mp, h[hPos].linbits)
						xrpnt[xrpntPos] = if (get1bit(mp) != 0) -ispow[x] * v else ispow[x] * v
					} else if (x != 0) {
						max[lwin] = cb
						xrpnt[xrpntPos] = if (get1bit(mp) != 0) -ispow[x] * v else ispow[x] * v
						part2remain--
					} else
						xrpnt[xrpntPos] = 0.0f
					xrpntPos += step
					if (y == 15) {
						max[lwin] = cb
						part2remain -= h[hPos].linbits + 1
						y += common.getbits(mp, h[hPos].linbits)
						xrpnt[xrpntPos] = if (get1bit(mp) != 0) -ispow[y] * v else ispow[y] * v
					} else if (y != 0) {
						max[lwin] = cb
						xrpnt[xrpntPos] = if (get1bit(mp) != 0) -ispow[y] * v else ispow[y] * v
						part2remain--
					} else
						xrpnt[xrpntPos] = 0.0f
					xrpntPos += step
					lp--
					mc--
				}
				i++
			}
			while (l3 != 0 && part2remain > 0) {
				val h = Huffman.htc
				val hPos = gr_infos.count1table_select
				val val2 = h[hPos].table
				var valPos = 0
				var a: Short

				while (true) {
					a = val2[valPos++]
					if (a >= 0) break
					part2remain--
					if (part2remain < 0) {
						part2remain++
						a = 0
						break
					}
					if (get1bit(mp) != 0) valPos -= a.toInt()
				}
				i = 0
				while (i < 4) {
					if (0 == i and 1) {
						if (0 == mc) {
							mc = m[mPos++]
							xrpnt = xr
							xrpntPos = m[mPos++]
							lwin = m[mPos++]
							cb = m[mPos++]
							if (lwin == 3) {
								v = gr_infos.pow2gain[gr_infos.pow2gainPos + (scf[scfPos++] shl shift)]
								step = 1
							} else {
								v = gr_infos.full_gain[lwin][gr_infos.full_gainPos[lwin] + (scf[scfPos++] shl shift)]
								step = 3
							}
						}
						mc--
					}
					if (a.toInt() and (0x8 shr i) != 0) {
						max[lwin] = cb
						part2remain--
						if (part2remain < 0) {
							part2remain++
							break
						}
						xrpnt[xrpntPos] = if (get1bit(mp) != 0) -v else v
					} else {
						xrpnt[xrpntPos] = 0.0f
					}
					xrpntPos += step
					i++
				}
				l3--
			}

			while (mPos < me) {
				if (0 == mc) {
					mc = m[mPos++]
					xrpnt = xr
					xrpntPos = m[mPos++]
					step = if (m[mPos++] == 3) 1 else 3
					mPos++ /* cb */
				}
				mc--
				xrpnt[xrpntPos] = 0.0f
				xrpntPos += step
				xrpnt[xrpntPos] = 0.0f
				xrpntPos += step
			}

			gr_infos.maxband[0] = max[0] + 1
			gr_infos.maxband[1] = max[1] + 1
			gr_infos.maxband[2] = max[2] + 1
			gr_infos.maxbandl = max[3] + 1


			var rmax = if (max[0] > max[1]) max[0] else max[1]
			rmax = (if (rmax > max[2]) rmax else max[2]) + 1
			gr_infos.maxb = if (rmax != 0) shortLimit[sfreq][rmax] else longLimit[sfreq][max[3] + 1]

		} else {
			val pretab = if (gr_infos.preflag != 0) pretab1 else pretab2
			var pretabPos = 0
			var i: Int
			var max = -1
			var cb = 0
			val m = map[sfreq][2]
			var mPos = 0
			var v = 0.0f
			var mc = 0

			i = 0
			while (i < 3) {
				var lp = l[i]
				val h = Huffman.ht
				val hPos = gr_infos.table_select[i]

				while (lp != 0) {
					var x: Int = 0
					var y: Int = 0

					if (0 == mc) {
						mc = m[mPos++]
						v = gr_infos.pow2gain[gr_infos.pow2gainPos + (scf[scfPos++] + pretab[pretabPos++] shl shift)]
						cb = m[mPos++]
					}

					val val2 = h[hPos].table
					var valPos = 0
					while (true) {
						y = val2[valPos++].toInt()
						if (y >= 0) break
						if (get1bit(mp) != 0) valPos -= y
						part2remain--
					}
					x = y shr 4
					y = y and 0xf

					if (x == 15) {
						max = cb
						part2remain -= h[hPos].linbits + 1
						x += common.getbits(mp, h[hPos].linbits)
						xrpnt[xrpntPos++] = if (get1bit(mp) != 0) -ispow[x] * v else ispow[x] * v
					} else if (x != 0) {
						max = cb
						xrpnt[xrpntPos++] = if (get1bit(mp) != 0) -ispow[x] * v else ispow[x] * v
						part2remain--
					} else
						xrpnt[xrpntPos++] = 0.0f

					if (y == 15) {
						max = cb
						part2remain -= h[hPos].linbits + 1
						y += common.getbits(mp, h[hPos].linbits)
						xrpnt[xrpntPos++] = if (get1bit(mp) != 0) -ispow[y] * v else ispow[y] * v
					} else if (y != 0) {
						max = cb
						xrpnt[xrpntPos++] = if (get1bit(mp) != 0) -ispow[y] * v else ispow[y] * v
						part2remain--
					} else
						xrpnt[xrpntPos++] = 0.0f
					lp--
					mc--
				}
				i++
			}

			/*
			 * short (count1table) values
			 */
			while (l3 != 0 && part2remain > 0) {
				val h = Huffman.htc
				val hPos = gr_infos.count1table_select
				val val2 = h[hPos].table
				var valPos = 0
				var a: Short

				while (true) {
					a = val2[valPos++]
					if (a >= 0) break
					part2remain--
					if (part2remain < 0) {
						part2remain++
						a = 0
						break
					}
					if (get1bit(mp) != 0) valPos -= a.toInt()
				}
				i = 0
				while (i < 4) {
					if (0 == i and 1) {
						if (0 == mc) {
							mc = m[mPos++]
							cb = m[mPos++]
							v =
									gr_infos.pow2gain[gr_infos.pow2gainPos + (scf[scfPos++] + pretab[pretabPos++] shl shift)]
						}
						mc--
					}
					if (a.toInt() and (0x8 shr i) != 0) {
						max = cb
						part2remain--
						if (part2remain < 0) {
							part2remain++
							break
						}
						if (get1bit(mp) != 0)
							xrpnt[xrpntPos++] = -v
						else
							xrpnt[xrpntPos++] = v
					} else
						xrpnt[xrpntPos++] = 0.0f
					i++
				}
				l3--
			}

			/*
			 * zero part
			 */
			i = MPG123.SBLIMIT * MPG123.SSLIMIT - xrpntPos shr 1
			while (i != 0) {
				xrpnt[xrpntPos++] = 0.0f
				xrpnt[xrpntPos++] = 0.0f
				i--
			}

			gr_infos.maxbandl = max + 1
			gr_infos.maxb = longLimit[sfreq][gr_infos.maxbandl]
		}

		while (part2remain > 16) {
			common.getbits(mp, 16) /* Dismiss stuffing Bits */
			part2remain -= 16
		}
		if (part2remain > 0) common.getbits(mp, part2remain)
		else if (part2remain < 0) {
			common.warningProcessor?.invoke("hip: Can't rewind stream by ${-part2remain} bits!")
			return 1
		}
		return 0
	}

	private fun III_i_stereo(
		xr_buf: Array<FloatArray>,
		scalefac: IntArray,
		gr_infos: MPG123.gr_info_s,
		sfreq: Int,
		ms_stereo: Int,
		lsf: Int
	) {
		val xr = xr_buf
		val bi = bandInfo[sfreq]
		val tabl1: FloatArray
		val tabl2: FloatArray

		if (lsf != 0) {
			val p = gr_infos.scalefac_compress and 0x1
			tabl1 = if (ms_stereo != 0) pow1_2[p] else pow1_1[p]
			tabl2 = if (ms_stereo != 0) pow2_2[p] else pow2_1[p]
		} else {
			tabl1 = if (ms_stereo != 0) tan1_2 else tan1_1
			tabl2 = if (ms_stereo != 0) tan2_2 else tan2_1
		}

		if (gr_infos.block_type == 2) {
			var do_l = if (gr_infos.mixed_block_flag != 0) 1 else 0
			var lwin = 0
			while (lwin < 3) { /* process each window */
				var is_p: Int
				var sb: Int
				var idx: Int
				var sfb = gr_infos.maxband[lwin]
				if (sfb > 3) do_l = 0

				while (sfb < 12) {
					is_p = scalefac[sfb * 3 + lwin - gr_infos.mixed_block_flag]
					if (is_p != 7) {
						sb = bi.shortDiff[sfb].toInt()
						idx = bi.shortIdx[sfb] + lwin
						val t1 = tabl1[is_p]
						val t2 = tabl2[is_p]
						while (sb > 0) {
							val v = xr[0][idx]
							xr[0][idx] = v * t1
							xr[1][idx] = v * t2
							sb--
							idx += 3
						}
					}
					sfb++
				}

				is_p = scalefac[11 * 3 + lwin - gr_infos.mixed_block_flag]
				sb = bi.shortDiff[12].toInt()
				idx = bi.shortIdx[12] + lwin
				if (is_p != 7) {
					val t1: Float
					val t2: Float
					t1 = tabl1[is_p]
					t2 = tabl2[is_p]
					while (sb > 0) {
						val v = xr[0][idx]
						xr[0][idx] = v * t1
						xr[1][idx] = v * t2
						sb--
						idx += 3
					}
				}
				lwin++
			} /* end for(lwin; .. ; . ) */

			if (do_l != 0) {
				var sfb = gr_infos.maxbandl
				var idx = bi.longIdx[sfb].toInt()

				while (sfb < 8) {
					var sb = bi.longDiff[sfb].toInt()
					val is_p = scalefac[sfb] /* scale: 0-15 */
					if (is_p != 7) {
						val t1: Float
						val t2: Float
						t1 = tabl1[is_p]
						t2 = tabl2[is_p]
						while (sb > 0) {
							val v = xr[0][idx]
							xr[0][idx] = v * t1
							xr[1][idx] = v * t2
							sb--
							idx++
						}
					} else
						idx += sb
					sfb++
				}
			}
		} else { /* ((gr_infos.block_type != 2)) */

			var sfb = gr_infos.maxbandl
			var is_p: Int
			var idx = bi.longIdx[sfb].toInt()
			while (sfb < 21) {
				var sb = bi.longDiff[sfb].toInt()
				is_p = scalefac[sfb] /* scale: 0-15 */
				if (is_p != 7) {
					val t1 = tabl1[is_p]
					val t2 = tabl2[is_p]
					while (sb > 0) {
						val v = xr[0][idx]
						xr[0][idx] = v * t1
						xr[1][idx] = v * t2
						sb--
						idx++
					}
				} else {
					idx += sb
				}
				sfb++
			}

			is_p = scalefac[20] /* copy l-band 20 to l-band 21 */
			if (is_p != 7) {
				val t1 = tabl1[is_p]
				val t2 = tabl2[is_p]

				var sb = bi.longDiff[21].toInt()
				while (sb > 0) {
					val v = xr[0][idx]
					xr[0][idx] = v * t1
					xr[1][idx] = v * t2
					sb--
					idx++
				}
			}
		} /* ... */
	}

	private fun III_antialias(xr: FloatArray, gr_infos: MPG123.gr_info_s) {
		val xr1 = xr
		var xr1Pos = MPG123.SSLIMIT

		var sb = if (gr_infos.block_type == 2) {
			if (0 == gr_infos.mixed_block_flag) return
			1
		} else {
			gr_infos.maxb - 1
		}
		while (sb != 0) {
			val cs = aa_cs
			val ca = aa_ca
			var caPos = 0
			var csPos = 0
			val xr2 = xr1
			var xr2Pos = xr1Pos

			var ss = 7
			while (ss >= 0) { /* upper and lower butterfly inputs */
				val bu = xr2[--xr2Pos]
				val bd = xr1[xr1Pos]
				xr2[xr2Pos] = bu * cs[csPos] - bd * ca[caPos]
				xr1[xr1Pos++] = bd * cs[csPos++] + bu * ca[caPos++]
				ss--
			}
			sb--
			xr1Pos += 10
		}
	}

	private fun dct36(
		inbuf: FloatArray, inbufPos: Int, o1: FloatArray, o1Pos: Int,
		o2: FloatArray, o2Pos: Int, wintab: FloatArray, tsbuf: FloatArray, tsPos: Int
	) {
		run {
			val inn = inbuf
			val inPos = inbufPos

			inn[inPos + 17] += inn[inPos + 16]
			inn[inPos + 16] += inn[inPos + 15]
			inn[inPos + 15] += inn[inPos + 14]
			inn[inPos + 14] += inn[inPos + 13]
			inn[inPos + 13] += inn[inPos + 12]
			inn[inPos + 12] += inn[inPos + 11]
			inn[inPos + 11] += inn[inPos + 10]
			inn[inPos + 10] += inn[inPos + 9]
			inn[inPos + 9] += inn[inPos + 8]
			inn[inPos + 8] += inn[inPos + 7]
			inn[inPos + 7] += inn[inPos + 6]
			inn[inPos + 6] += inn[inPos + 5]
			inn[inPos + 5] += inn[inPos + 4]
			inn[inPos + 4] += inn[inPos + 3]
			inn[inPos + 3] += inn[inPos + 2]
			inn[inPos + 2] += inn[inPos + 1]
			inn[inPos + 1] += inn[inPos + 0]

			inn[inPos + 17] += inn[inPos + 15]
			inn[inPos + 15] += inn[inPos + 13]
			inn[inPos + 13] += inn[inPos + 11]
			inn[inPos + 11] += inn[inPos + 9]
			inn[inPos + 9] += inn[inPos + 7]
			inn[inPos + 7] += inn[inPos + 5]
			inn[inPos + 5] += inn[inPos + 3]
			inn[inPos + 3] += inn[inPos + 1]

			run {
				val c = COS9
				val out2 = o2
				val out2Pos = o2Pos
				val w = wintab
				val out1 = o1
				val out1Pos = o1Pos
				val ts = tsbuf

				val ta33 = inn[inPos + 2 * 3 + 0] * c[3]
				val ta66 = inn[inPos + 2 * 6 + 0] * c[6]
				val tb33 = inn[inPos + 2 * 3 + 1] * c[3]
				val tb66 = inn[inPos + 2 * 6 + 1] * c[6]

				run {
					val tmp1a =
						inn[inPos + 2 * 1 + 0] * c[1] + ta33 + inn[inPos + 2 * 5 + 0] * c[5] + inn[inPos + 2 * 7 + 0] * c[7]
					val tmp1b =
						inn[inPos + 2 * 1 + 1] * c[1] + tb33 + inn[inPos + 2 * 5 + 1] * c[5] + inn[inPos + 2 * 7 + 1] * c[7]
					val tmp2a =
						inn[inPos + 2 * 0 + 0] + inn[inPos + 2 * 2 + 0] * c[2] + inn[inPos + 2 * 4 + 0] * c[4] + ta66 + inn[inPos + 2 * 8 + 0] * c[8]
					val tmp2b =
						inn[inPos + 2 * 0 + 1] + inn[inPos + 2 * 2 + 1] * c[2] + inn[inPos + 2 * 4 + 1] * c[4] + tb66 + inn[inPos + 2 * 8 + 1] * c[8]

					// MACRO1(0);
					run {
						var sum0 = tmp1a + tmp2a
						val sum1 = (tmp1b + tmp2b) * tfcos36[0]
						val tmp = sum0 + sum1
						out2[out2Pos + 9 + 0] = tmp * w[27 + 0]
						out2[out2Pos + 8 - 0] = tmp * w[26 - 0]
						sum0 -= sum1
						ts[tsPos + MPG123.SBLIMIT * (8 - 0)] = out1[out1Pos + 8 - 0] + sum0 * w[8 - 0]
						ts[tsPos + MPG123.SBLIMIT * (9 + 0)] = out1[out1Pos + 9 + 0] + sum0 * w[9 + 0]
					}
					// MACRO2(8);
					run {
						var sum0 = tmp2a - tmp1a
						val sum1 = (tmp2b - tmp1b) * tfcos36[8]
						val tmp = sum0 + sum1
						out2[out2Pos + 9 + 8] = tmp * w[27 + 8]
						out2[out2Pos + 8 - 8] = tmp * w[26 - 8]
						sum0 -= sum1
						ts[tsPos + MPG123.SBLIMIT * (8 - 8)] = out1[out1Pos + 8 - 8] + sum0 * w[8 - 8]
						ts[tsPos + MPG123.SBLIMIT * (9 + 8)] = out1[out1Pos + 9 + 8] + sum0 * w[9 + 8]
					}
				}

				run {
					val tmp1a = (inn[inPos + 2 * 1 + 0] - inn[inPos + 2 * 5 + 0] - inn[inPos + 2 * 7 + 0]) * c[3]
					val tmp1b = (inn[inPos + 2 * 1 + 1] - inn[inPos + 2 * 5 + 1] - inn[inPos + 2 * 7 + 1]) * c[3]
					val tmp2a =
						(inn[inPos + 2 * 2 + 0] - inn[inPos + 2 * 4 + 0] - inn[inPos + 2 * 8 + 0]) * c[6] - inn[inPos + 2 * 6 + 0] + inn[inPos + 2 * 0 + 0]
					val tmp2b =
						(inn[inPos + 2 * 2 + 1] - inn[inPos + 2 * 4 + 1] - inn[inPos + 2 * 8 + 1]) * c[6] - inn[inPos + 2 * 6 + 1] + inn[inPos + 2 * 0 + 1]

					// MACRO1(1);
					run {
						var sum0 = tmp1a + tmp2a
						val sum1 = (tmp1b + tmp2b) * tfcos36[1]
						val tmp = sum0 + sum1
						out2[out2Pos + 9 + 1] = tmp * w[27 + 1]
						out2[out2Pos + 8 - 1] = tmp * w[26 - 1]
						sum0 -= sum1
						ts[tsPos + MPG123.SBLIMIT * (8 - 1)] = out1[out1Pos + 8 - 1] + sum0 * w[8 - 1]
						ts[tsPos + MPG123.SBLIMIT * (9 + 1)] = out1[out1Pos + 9 + 1] + sum0 * w[9 + 1]
					}
					// MACRO2(7);
					run {
						var sum0: Float
						val sum1: Float
						sum0 = tmp2a - tmp1a
						sum1 = (tmp2b - tmp1b) * tfcos36[7]
						val tmp = sum0 + sum1
						out2[out2Pos + 9 + 7] = tmp * w[27 + 7]
						out2[out2Pos + 8 - 7] = tmp * w[26 - 7]
						sum0 -= sum1
						ts[tsPos + MPG123.SBLIMIT * (8 - 7)] = out1[out1Pos + 8 - 7] + sum0 * w[8 - 7]
						ts[tsPos + MPG123.SBLIMIT * (9 + 7)] = out1[out1Pos + 9 + 7] + sum0 * w[9 + 7]
					}
				}

				run {
					val tmp1a =
						inn[inPos + 2 * 1 + 0] * c[5] - ta33 - inn[inPos + 2 * 5 + 0] * c[7] + inn[inPos + 2 * 7 + 0] * c[1]
					val tmp1b =
						inn[inPos + 2 * 1 + 1] * c[5] - tb33 - inn[inPos + 2 * 5 + 1] * c[7] + inn[inPos + 2 * 7 + 1] * c[1]
					val tmp2a =
						inn[inPos + 2 * 0 + 0] - inn[inPos + 2 * 2 + 0] * c[8] - inn[inPos + 2 * 4 + 0] * c[2] + ta66 + inn[inPos + 2 * 8 + 0] * c[4]
					val tmp2b =
						inn[inPos + 2 * 0 + 1] - inn[inPos + 2 * 2 + 1] * c[8] - inn[inPos + 2 * 4 + 1] * c[2] + tb66 + inn[inPos + 2 * 8 + 1] * c[4]

					// MACRO1(2);
					run {
						var sum0 = tmp1a + tmp2a
						val sum1 = (tmp1b + tmp2b) * tfcos36[2]
						val tmp = sum0 + sum1
						out2[out2Pos + 9 + 2] = tmp * w[27 + 2]
						out2[out2Pos + 8 - 2] = tmp * w[26 - 2]
						sum0 -= sum1
						ts[tsPos + MPG123.SBLIMIT * (8 - 2)] = out1[out1Pos + 8 - 2] + sum0 * w[8 - 2]
						ts[tsPos + MPG123.SBLIMIT * (9 + 2)] = out1[out1Pos + 9 + 2] + sum0 * w[9 + 2]
					}
					// MACRO2(6);
					run {
						var sum0 = tmp2a - tmp1a
						val sum1 = (tmp2b - tmp1b) * tfcos36[6]
						val tmp = sum0 + sum1
						out2[out2Pos + 9 + 6] = tmp * w[27 + 6]
						out2[out2Pos + 8 - 6] = tmp * w[26 - 6]
						sum0 -= sum1
						ts[tsPos + MPG123.SBLIMIT * (8 - 6)] = out1[out1Pos + 8 - 6] + sum0 * w[8 - 6]
						ts[tsPos + MPG123.SBLIMIT * (9 + 6)] = out1[out1Pos + 9 + 6] + sum0 * w[9 + 6]
					}
				}

				run {
					val tmp1a =
						inn[inPos + 2 * 1 + 0] * c[7] - ta33 + inn[inPos + 2 * 5 + 0] * c[1] - inn[inPos + 2 * 7 + 0] * c[5]
					val tmp1b =
						inn[inPos + 2 * 1 + 1] * c[7] - tb33 + inn[inPos + 2 * 5 + 1] * c[1] - inn[inPos + 2 * 7 + 1] * c[5]
					val tmp2a =
						inn[inPos + 2 * 0 + 0] - inn[inPos + 2 * 2 + 0] * c[4] + inn[inPos + 2 * 4 + 0] * c[8] + ta66 - inn[inPos + 2 * 8 + 0] * c[2]
					val tmp2b =
						inn[inPos + 2 * 0 + 1] - inn[inPos + 2 * 2 + 1] * c[4] + inn[inPos + 2 * 4 + 1] * c[8] + tb66 - inn[inPos + 2 * 8 + 1] * c[2]

					// MACRO1(3);
					run {
						var sum0 = tmp1a + tmp2a
						val sum1 = (tmp1b + tmp2b) * tfcos36[3]
						val tmp = sum0 + sum1
						out2[out2Pos + 9 + 3] = tmp * w[27 + 3]
						out2[out2Pos + 8 - 3] = tmp * w[26 - 3]
						sum0 -= sum1
						ts[tsPos + MPG123.SBLIMIT * (8 - 3)] = out1[out1Pos + 8 - 3] + sum0 * w[8 - 3]
						ts[tsPos + MPG123.SBLIMIT * (9 + 3)] = out1[out1Pos + 9 + 3] + sum0 * w[9 + 3]
					}
					// MACRO2(5);
					run {
						var sum0 = tmp2a - tmp1a
						val sum1 = (tmp2b - tmp1b) * tfcos36[5]
						val tmp = sum0 + sum1
						out2[out2Pos + 9 + 5] = tmp * w[27 + 5]
						out2[out2Pos + 8 - 5] = tmp * w[26 - 5]
						sum0 -= sum1
						ts[tsPos + MPG123.SBLIMIT * (8 - 5)] = out1[out1Pos + 8 - 5] + sum0 * w[8 - 5]
						ts[tsPos + MPG123.SBLIMIT * (9 + 5)] = out1[out1Pos + 9 + 5] + sum0 * w[9 + 5]
					}
				}

				run {
					var sum0 =
						inn[inPos + 2 * 0 + 0] - inn[inPos + 2 * 2 + 0] + inn[inPos + 2 * 4 + 0] - inn[inPos + 2 * 6 + 0] + inn[inPos + 2 * 8 + 0]
					val sum1 =
						(inn[inPos + 2 * 0 + 1] - inn[inPos + 2 * 2 + 1] + inn[inPos + 2 * 4 + 1] - inn[inPos + 2 * 6 + 1] + inn[inPos + 2 * 8 + 1]) * tfcos36[4]
					// MACRO0(4)
					run {
						val tmp = sum0 + sum1
						out2[out2Pos + 9 + 4] = tmp * w[27 + 4]
						out2[out2Pos + 8 - 4] = tmp * w[26 - 4]
						sum0 -= sum1
						ts[tsPos + MPG123.SBLIMIT * (8 - 4)] = out1[out1Pos + 8 - 4] + sum0 * w[8 - 4]
						ts[tsPos + MPG123.SBLIMIT * (9 + 4)] = out1[out1Pos + 9 + 4] + sum0 * w[9 + 4]
					}
				}
			}

		}
	}

	/*
	 * new DCT12
	 */
	private fun dct12(
		inn: FloatArray, inbufPos: Int, rawout1: FloatArray,
		rawout1Pos: Int, rawout2: FloatArray, rawout2Pos: Int, wi: FloatArray,
		ts: FloatArray, tsPos: Int
	) {
		var inbufPos = inbufPos
		run {
			val out1 = rawout1
			val out1Pos = rawout1Pos
			ts[tsPos + MPG123.SBLIMIT * 0] = out1[out1Pos + 0]
			ts[tsPos + MPG123.SBLIMIT * 1] = out1[out1Pos + 1]
			ts[tsPos + MPG123.SBLIMIT * 2] = out1[out1Pos + 2]
			ts[tsPos + MPG123.SBLIMIT * 3] = out1[out1Pos + 3]
			ts[tsPos + MPG123.SBLIMIT * 4] = out1[out1Pos + 4]
			ts[tsPos + MPG123.SBLIMIT * 5] = out1[out1Pos + 5]

			// DCT12_PART1

			var in5 = inn[inbufPos + 5 * 3]
			var in4 = inn[inbufPos + 4 * 3]
			in5 += in4
			var in3 = inn[inbufPos + 3 * 3]
			in4 += in3
			var in2 = inn[inbufPos + 2 * 3]
			in3 += in2
			var in1 = inn[inbufPos + 1 * 3]
			in2 += in1
			var in0 = inn[inbufPos + 0 * 3]
			in1 += in0

			in5 += in3
			in3 += in1

			in2 *= COS6_1
			in3 *= COS6_1


			val tmp0: Float
			var tmp1 = in0 - in4

			val tmp2 = (in1 - in5) * tfcos12[1]
			tmp0 = tmp1 + tmp2
			tmp1 -= tmp2

			ts[tsPos + (17 - 1) * MPG123.SBLIMIT] = out1[out1Pos + 17 - 1] + tmp0 * wi[11 - 1]
			ts[tsPos + (12 + 1) * MPG123.SBLIMIT] = out1[out1Pos + 12 + 1] + tmp0 * wi[6 + 1]
			ts[tsPos + (6 + 1) * MPG123.SBLIMIT] = out1[out1Pos + 6 + 1] + tmp1 * wi[1]
			ts[tsPos + (11 - 1) * MPG123.SBLIMIT] = out1[out1Pos + 11 - 1] + tmp1 * wi[5 - 1]


			// DCT12_PART2

			in0 += in4 * COS6_2

			in4 = in0 + in2
			in0 -= in2

			in1 += in5 * COS6_2

			in5 = (in1 + in3) * tfcos12[0]
			in1 = (in1 - in3) * tfcos12[2]

			in3 = in4 + in5
			in4 -= in5

			in2 = in0 + in1
			in0 -= in1

			ts[tsPos + (17 - 0) * MPG123.SBLIMIT] = out1[out1Pos + 17 - 0] + in2 * wi[11 - 0]
			ts[tsPos + (12 + 0) * MPG123.SBLIMIT] = out1[out1Pos + 12 + 0] + in2 * wi[6 + 0]
			ts[tsPos + (12 + 2) * MPG123.SBLIMIT] = out1[out1Pos + 12 + 2] + in3 * wi[6 + 2]
			ts[tsPos + (17 - 2) * MPG123.SBLIMIT] = out1[out1Pos + 17 - 2] + in3 * wi[11 - 2]
			ts[tsPos + (6 + 0) * MPG123.SBLIMIT] = out1[out1Pos + 6 + 0] + in0 * wi[0]
			ts[tsPos + (11 - 0) * MPG123.SBLIMIT] = out1[out1Pos + 11 - 0] + in0 * wi[5 - 0]
			ts[tsPos + (6 + 2) * MPG123.SBLIMIT] = out1[out1Pos + 6 + 2] + in4 * wi[2]
			ts[tsPos + (11 - 2) * MPG123.SBLIMIT] = out1[out1Pos + 11 - 2] + in4 * wi[5 - 2]
		}

		inbufPos++

		run {
			val out2 = rawout2
			val out2Pos = rawout2Pos

			// DCT12_PART1
			var in5 = inn[inbufPos + 5 * 3]
			var in4 = inn[inbufPos + 4 * 3]
			in5 += in4
			var in3 = inn[inbufPos + 3 * 3]
			in4 += in3
			var in2 = inn[inbufPos + 2 * 3]
			in3 += in2
			var in1 = inn[inbufPos + 1 * 3]
			in2 += in1
			var in0 = inn[inbufPos + 0 * 3]
			in1 += in0

			in5 += in3
			in3 += in1

			in2 *= COS6_1
			in3 *= COS6_1

			val tmp0: Float
			var tmp1 = in0 - in4

			val tmp2 = (in1 - in5) * tfcos12[1]
			tmp0 = tmp1 + tmp2
			tmp1 -= tmp2

			out2[out2Pos + 5 - 1] = tmp0 * wi[11 - 1]
			out2[out2Pos + 0 + 1] = tmp0 * wi[6 + 1]
			ts[tsPos + (12 + 1) * MPG123.SBLIMIT] += tmp1 * wi[1]
			ts[tsPos + (17 - 1) * MPG123.SBLIMIT] += tmp1 * wi[5 - 1]


			// DCT12_PART2

			in0 += in4 * COS6_2

			in4 = in0 + in2
			in0 -= in2

			in1 += in5 * COS6_2

			in5 = (in1 + in3) * tfcos12[0]
			in1 = (in1 - in3) * tfcos12[2]

			in3 = in4 + in5
			in4 -= in5

			in2 = in0 + in1
			in0 -= in1

			out2[out2Pos + 5 - 0] = in2 * wi[11 - 0]
			out2[out2Pos + 0 + 0] = in2 * wi[6 + 0]
			out2[out2Pos + 0 + 2] = in3 * wi[6 + 2]
			out2[out2Pos + 5 - 2] = in3 * wi[11 - 2]

			ts[tsPos + (12 + 0) * MPG123.SBLIMIT] += in0 * wi[0]
			ts[tsPos + (17 - 0) * MPG123.SBLIMIT] += in0 * wi[5 - 0]
			ts[tsPos + (12 + 2) * MPG123.SBLIMIT] += in4 * wi[2]
			ts[tsPos + (17 - 2) * MPG123.SBLIMIT] += in4 * wi[5 - 2]
		}

		inbufPos++

		run {
			val out2 = rawout2
			val out2Pos = rawout2Pos
			out2[out2Pos + 12] = 0.0f
			out2[out2Pos + 13] = 0.0f
			out2[out2Pos + 14] = 0.0f
			out2[out2Pos + 15] = 0.0f
			out2[out2Pos + 16] = 0.0f
			out2[out2Pos + 17] = 0.0f

			// DCT12_PART1

			var in5 = inn[inbufPos + 5 * 3]
			var in4 = inn[inbufPos + 4 * 3]
			in5 += in4
			var in3 = inn[inbufPos + 3 * 3]
			in4 += in3
			var in2 = inn[inbufPos + 2 * 3]
			in3 += in2
			var in1 = inn[inbufPos + 1 * 3]
			in2 += in1
			var in0 = inn[inbufPos + 0 * 3]
			in1 += in0

			in5 += in3
			in3 += in1

			in2 *= COS6_1
			in3 *= COS6_1

			val tmp0: Float
			var tmp1 = in0 - in4
			val tmp2 = (in1 - in5) * tfcos12[1]
			tmp0 = tmp1 + tmp2
			tmp1 -= tmp2

			out2[out2Pos + 11 - 1] = tmp0 * wi[11 - 1]
			out2[out2Pos + 6 + 1] = tmp0 * wi[6 + 1]
			out2[out2Pos + 0 + 1] += tmp1 * wi[1]
			out2[out2Pos + 5 - 1] += tmp1 * wi[5 - 1]


			// DCT12_PART2

			in0 += in4 * COS6_2

			in4 = in0 + in2
			in0 -= in2

			in1 += in5 * COS6_2

			in5 = (in1 + in3) * tfcos12[0]
			in1 = (in1 - in3) * tfcos12[2]

			in3 = in4 + in5
			in4 -= in5

			in2 = in0 + in1
			in0 -= in1


			out2[out2Pos + 11 - 0] = in2 * wi[11 - 0]
			out2[out2Pos + 6 + 0] = in2 * wi[6 + 0]
			out2[out2Pos + 6 + 2] = in3 * wi[6 + 2]
			out2[out2Pos + 11 - 2] = in3 * wi[11 - 2]

			out2[out2Pos + 0 + 0] += in0 * wi[0]
			out2[out2Pos + 5 - 0] += in0 * wi[5 - 0]
			out2[out2Pos + 0 + 2] += in4 * wi[2]
			out2[out2Pos + 5 - 2] += in4 * wi[5 - 2]
		}
	}

	private fun III_hybrid(mp: MPGLib.mpstr_tag, fsIn: FloatArray, tsOut: FloatArray, ch: Int, gr_infos: MPG123.gr_info_s) {
		val tspnt = tsOut
		var tspntPos = 0
		val block = mp.hybrid_block
		val blc = mp.hybrid_blc
		var sb = 0

		var b = blc[ch]
		val rawout1 = block[b][ch]
		var rawout1Pos = 0
		b = -b + 1
		val rawout2 = block[b][ch]
		var rawout2Pos = 0
		blc[ch] = b

		if (gr_infos.mixed_block_flag != 0) {
			sb = 2
			dct36(fsIn, 0 * MPG123.SSLIMIT, rawout1, rawout1Pos, rawout2, rawout2Pos, win[0], tspnt, tspntPos + 0)
			dct36(
				fsIn,
				1 * MPG123.SSLIMIT,
				rawout1,
				rawout1Pos + 18,
				rawout2,
				rawout2Pos + 18,
				win1[0],
				tspnt,
				tspntPos + 1
			)
			rawout1Pos += 36
			rawout2Pos += 36
			tspntPos += 2
		}

		val bt = gr_infos.block_type
		if (bt == 2) {
			while (sb < gr_infos.maxb) {
				dct12(fsIn, sb * MPG123.SSLIMIT, rawout1, rawout1Pos, rawout2, rawout2Pos, win[2], tspnt, tspntPos + 0)
				dct12(
					fsIn,
					(sb + 1) * MPG123.SSLIMIT,
					rawout1,
					rawout1Pos + 18,
					rawout2,
					rawout2Pos + 18,
					win1[2],
					tspnt,
					tspntPos + 1
				)
				sb += 2
				tspntPos += 2
				rawout1Pos += 36
				rawout2Pos += 36
			}
		} else {
			while (sb < gr_infos.maxb) {
				dct36(fsIn, sb * MPG123.SSLIMIT, rawout1, rawout1Pos, rawout2, rawout2Pos, win[bt], tspnt, tspntPos + 0)
				dct36(
					fsIn,
					(sb + 1) * MPG123.SSLIMIT,
					rawout1,
					rawout1Pos + 18,
					rawout2,
					rawout2Pos + 18,
					win1[bt],
					tspnt,
					tspntPos + 1
				)
				sb += 2
				tspntPos += 2
				rawout1Pos += 36
				rawout2Pos += 36
			}
		}

		while (sb < MPG123.SBLIMIT) {
			for (i in 0..MPG123.SSLIMIT - 1) {
				tspnt[tspntPos + i * MPG123.SBLIMIT] = rawout1[rawout1Pos++]
				rawout2[rawout2Pos++] = 0.0f
			}
			sb++
			tspntPos++
		}
	}

	fun do_layer3_sideinfo(mp: MPGLib.mpstr_tag): Int {
		val fr = mp.fr
		val stereo = fr.stereo
		var single = fr.single
		val ms_stereo: Int
		val sfreq = fr.sampling_frequency
		val granules: Int
		var ch: Int
		var gr: Int
		var databits: Int

		if (stereo == 1) single = 0 /* stream is mono */

		ms_stereo = if (fr.mode == MPG123.MPG_MD_JOINT_STEREO) 0 else fr.mode_ext and 0x2

		if (fr.lsf != 0) {
			granules = 1
			III_get_side_info_2(mp, sideinfo, stereo, ms_stereo, sfreq, single)
		} else {
			granules = 2
			III_get_side_info_1(mp, sideinfo, stereo, ms_stereo, sfreq, single)
		}

		databits = 0
		gr = 0
		while (gr < granules) {
			ch = 0
			while (ch < stereo) {
				val gr_infos = sideinfo.ch[ch].gr[gr]
				databits += gr_infos.part2_3_length
				++ch
			}
			++gr
		}
		return databits - 8 * sideinfo.main_data_begin
	}

	fun do_layer3(mp: MPGLib.mpstr_tag, pcm_sample: FloatArray, pcm_point: MPGLib.ProcessedBytes, synth: Interface.ISynth): Int {
		var ss: Int
		var clip = 0
		val scalefacs = Array(2) { IntArray(39) }
		val fr = mp.fr
		val stereo = fr.stereo
		var single = fr.single
		val ms_stereo: Int
		val i_stereo: Int
		val sfreq = fr.sampling_frequency
		val granules: Int

		if (common.set_pointer(mp, sideinfo.main_data_begin) == MPGLib.MP3_ERR) return 0

		if (stereo == 1) single = 0
		val stereo1 = if (stereo == 1) 1 else if (single >= 0) 1 else 2

		if (fr.mode == MPG123.MPG_MD_JOINT_STEREO) {
			ms_stereo = fr.mode_ext and 0x2
			i_stereo = fr.mode_ext and 0x1
		} else {
			i_stereo = 0
			ms_stereo = i_stereo
		}

		granules = if (fr.lsf != 0) 1 else 2

		var gr = 0
		while (gr < granules) {

			val gr_infos2 = sideinfo.ch[0].gr[gr]
			val part2bits2: Int

			if (fr.lsf != 0)
				part2bits2 = III_get_scale_factors_2(mp, scalefacs[0], gr_infos2, 0)
			else {
				part2bits2 = III_get_scale_factors_1(mp, scalefacs[0], gr_infos2)
			}

			mp.pinfo.sfbits[gr][0] = part2bits2
			for (i in 0..38) {
				mp.pinfo.sfb_s[gr][0][i] = scalefacs[0][i].toDouble()
			}

			if (III_dequantize_sample(mp, hybridIn[0], scalefacs[0], gr_infos2, sfreq, part2bits2) != 0) return clip

			if (stereo == 2) {
				val gr_infos = sideinfo.ch[1].gr[gr]
				val part2bits: Int
				if (fr.lsf != 0)
					part2bits = III_get_scale_factors_2(mp, scalefacs[1], gr_infos, i_stereo)
				else {
					part2bits = III_get_scale_factors_1(mp, scalefacs[1], gr_infos)
				}
				mp.pinfo.sfbits[gr][1] = part2bits
				for (i in 0..38) mp.pinfo.sfb_s[gr][1][i] = scalefacs[1][i].toDouble()

				if (III_dequantize_sample(mp, hybridIn[1], scalefacs[1], gr_infos, sfreq, part2bits) != 0) return clip

				if (ms_stereo != 0) {
					for (i in 0..MPG123.SBLIMIT * MPG123.SSLIMIT - 1) {
						val tmp0: Float
						val tmp1: Float
						tmp0 = hybridIn[0][i]
						tmp1 = hybridIn[1][i]
						hybridIn[1][i] = tmp0 - tmp1
						hybridIn[0][i] = tmp0 + tmp1
					}
				}

				if (i_stereo != 0) III_i_stereo(hybridIn, scalefacs[1], gr_infos, sfreq, ms_stereo, fr.lsf)

				if (ms_stereo != 0 || i_stereo != 0 || single == 3) {
					if (gr_infos.maxb > sideinfo.ch[0].gr[gr].maxb) {
						sideinfo.ch[0].gr[gr].maxb = gr_infos.maxb
					} else {
						gr_infos.maxb = sideinfo.ch[0].gr[gr].maxb
					}
				}

				when (single) {
					3 -> {
						val in0 = hybridIn[0]
						val in1 = hybridIn[1]
						var in0Pos = 0
						var in1Pos = 0
						var i = 0
						while (i < MPG123.SSLIMIT * gr_infos.maxb) {
							in0[in0Pos] = in0[in0Pos] + in1[in1Pos++]
							i++
							in0Pos++
						}
					}
					1 -> {
						val in0 = hybridIn[0]
						val in1 = hybridIn[1]
						var in0Pos = 0
						var in1Pos = 0
						for (i in 0..MPG123.SSLIMIT * gr_infos.maxb - 1) in0[in0Pos++] = in1[in1Pos++]
					}
				}
			}


			var i: Int
			var ifqstep: Float

			mp.pinfo.bitrate = Common.tabsel_123[fr.lsf][fr.lay - 1][fr.bitrate_index]
			mp.pinfo.sampfreq = Common.freqs[sfreq]
			mp.pinfo.emph = fr.emphasis
			mp.pinfo.crc = if (fr.error_protection) 1 else 0
			mp.pinfo.padding = fr.padding
			mp.pinfo.stereo = fr.stereo
			mp.pinfo.js = if (fr.mode == MPG123.MPG_MD_JOINT_STEREO) 1 else 0
			mp.pinfo.ms_stereo = ms_stereo
			mp.pinfo.i_stereo = i_stereo
			mp.pinfo.maindata = sideinfo.main_data_begin

			for (ch in 0 until stereo1) {
				val gr_infos = sideinfo.ch[ch].gr[gr]
				mp.pinfo.big_values[gr][ch] = gr_infos.big_values
				mp.pinfo.scalefac_scale[gr][ch] = gr_infos.scalefac_scale
				mp.pinfo.mixed[gr][ch] = gr_infos.mixed_block_flag
				mp.pinfo.mpg123blocktype[gr][ch] = gr_infos.block_type
				mp.pinfo.mainbits[gr][ch] = gr_infos.part2_3_length
				mp.pinfo.preflag[gr][ch] = gr_infos.preflag
				if (gr == 1) mp.pinfo.scfsi[ch] = gr_infos.scfsi
			}

			for (ch in 0 until stereo1) {
				var sb: Int
				val gr_infos = sideinfo.ch[ch].gr[gr]
				ifqstep = if (mp.pinfo.scalefac_scale[gr][ch] == 0) .5f else 1.0f
				val doubles = mp.pinfo.sfb_s[gr][ch]
				if (2 == gr_infos.block_type) {
					i = 0
					while (i < 3) {
						val ints = mp.pinfo.sub_gain[gr][ch]
						sb = 0
						while (sb < 12) {
							val j = 3 * sb + i
							doubles[j] = -ifqstep * doubles[j - gr_infos.mixed_block_flag]
							doubles[j] -= (2 * ints[i]).toDouble()
							sb++
						}
						doubles[3 * sb + i] = (-2 * ints[i]).toDouble()
						i++
					}
				} else {
					val doubles1 = mp.pinfo.sfb[gr][ch]
					sb = 0
					while (sb < 21) {
						doubles1[sb] = doubles[sb]
						if (gr_infos.preflag != 0) doubles1[sb] += pretab1[sb].toDouble()
						doubles1[sb] *= (-ifqstep).toDouble()
						sb++
					}
					doubles1[21] = 0.0
				}
			}

			for (ch in 0 until stereo1) {
				var j = 0
				for (sb in 0 until MPG123.SBLIMIT) {
					ss = 0
					while (ss < MPG123.SSLIMIT) {
						mp.pinfo.mpg123xr[gr][ch][j] = hybridIn[ch][sb * MPG123.SSLIMIT + ss].toDouble()
						ss++
						j++
					}
				}
			}


			for (ch in 0 until stereo1) {
				val gr_infos = sideinfo.ch[ch].gr[gr]
				III_antialias(hybridIn[ch], gr_infos)
				III_hybrid(mp, hybridIn[ch], hybridOut[ch], ch, gr_infos)
			}

			ss = 0
			while (ss < MPG123.SSLIMIT) {
				if (single >= 0) {
					clip += synth.synth_1to1_mono_ptr(mp, hybridOut[0], ss * MPG123.SBLIMIT, pcm_sample, pcm_point)
				} else {
					val p1 = MPGLib.ProcessedBytes()
					p1.pb = pcm_point.pb
					clip += synth.synth_1to1_ptr(mp, hybridOut[0], ss * MPG123.SBLIMIT, 0, pcm_sample, p1)
					clip += synth.synth_1to1_ptr(mp, hybridOut[1], ss * MPG123.SBLIMIT, 1, pcm_sample, pcm_point)
				}
				ss++
			}
			gr++
		}

		return clip
	}

	private class bandInfoStruct(lIdx: ShortArray, lDiff: ShortArray, sIdx: ShortArray, sDiff: ShortArray) {
		internal var longIdx = ShortArray(23)
		internal var longDiff = ShortArray(22)
		internal var shortIdx = ShortArray(14)
		internal var shortDiff = ShortArray(13)

		init {
			longIdx = lIdx
			longDiff = lDiff
			shortIdx = sIdx
			shortDiff = sDiff
		}
	}

	companion object {
		private val slen = arrayOf(
			intArrayOf(0, 0, 0, 0, 3, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4),
			intArrayOf(0, 1, 2, 3, 0, 1, 2, 3, 1, 2, 3, 1, 2, 3, 2, 3)
		)
		private val stab = arrayOf(
			arrayOf(
				intArrayOf(6, 5, 5, 5),
				intArrayOf(6, 5, 7, 3),
				intArrayOf(11, 10, 0, 0),
				intArrayOf(7, 7, 7, 0),
				intArrayOf(6, 6, 6, 3),
				intArrayOf(8, 8, 5, 0)
			),
			arrayOf(
				intArrayOf(9, 9, 9, 9),
				intArrayOf(9, 9, 12, 6),
				intArrayOf(18, 18, 0, 0),
				intArrayOf(12, 12, 12, 0),
				intArrayOf(12, 9, 9, 6),
				intArrayOf(15, 12, 9, 0)
			),
			arrayOf(
				intArrayOf(6, 9, 9, 9),
				intArrayOf(6, 9, 12, 6),
				intArrayOf(15, 18, 0, 0),
				intArrayOf(6, 15, 12, 0),
				intArrayOf(6, 12, 9, 6),
				intArrayOf(6, 18, 9, 0)
			)
		)
		private val pretab1 =
			intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 3, 3, 3, 2, 0) /* char enough ? */
		private val pretab2 = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
		private val bandInfo = arrayOf(

			/* MPEG 1.0 */
			bandInfoStruct(
				shortArrayOf(
					0,
					4,
					8,
					12,
					16,
					20,
					24,
					30,
					36,
					44,
					52,
					62,
					74,
					90,
					110,
					134,
					162,
					196,
					238,
					288,
					342,
					418,
					576
				),
				shortArrayOf(4, 4, 4, 4, 4, 4, 6, 6, 8, 8, 10, 12, 16, 20, 24, 28, 34, 42, 50, 54, 76, 158),
				shortArrayOf(
					0,
					(4 * 3).toShort(),
					(8 * 3).toShort(),
					(12 * 3).toShort(),
					(16 * 3).toShort(),
					(22 * 3).toShort(),
					(30 * 3).toShort(),
					(40 * 3).toShort(),
					(52 * 3).toShort(),
					(66 * 3).toShort(),
					(84 * 3).toShort(),
					(106 * 3).toShort(),
					(136 * 3).toShort(),
					(192 * 3).toShort()
				),
				shortArrayOf(4, 4, 4, 4, 6, 8, 10, 12, 14, 18, 22, 30, 56)
			),

			bandInfoStruct(
				shortArrayOf(
					0,
					4,
					8,
					12,
					16,
					20,
					24,
					30,
					36,
					42,
					50,
					60,
					72,
					88,
					106,
					128,
					156,
					190,
					230,
					276,
					330,
					384,
					576
				),
				shortArrayOf(4, 4, 4, 4, 4, 4, 6, 6, 6, 8, 10, 12, 16, 18, 22, 28, 34, 40, 46, 54, 54, 192),
				shortArrayOf(
					0,
					(4 * 3).toShort(),
					(8 * 3).toShort(),
					(12 * 3).toShort(),
					(16 * 3).toShort(),
					(22 * 3).toShort(),
					(28 * 3).toShort(),
					(38 * 3).toShort(),
					(50 * 3).toShort(),
					(64 * 3).toShort(),
					(80 * 3).toShort(),
					(100 * 3).toShort(),
					(126 * 3).toShort(),
					(192 * 3).toShort()
				),
				shortArrayOf(4, 4, 4, 4, 6, 6, 10, 12, 14, 16, 20, 26, 66)
			),

			bandInfoStruct(
				shortArrayOf(
					0,
					4,
					8,
					12,
					16,
					20,
					24,
					30,
					36,
					44,
					54,
					66,
					82,
					102,
					126,
					156,
					194,
					240,
					296,
					364,
					448,
					550,
					576
				),
				shortArrayOf(4, 4, 4, 4, 4, 4, 6, 6, 8, 10, 12, 16, 20, 24, 30, 38, 46, 56, 68, 84, 102, 26),
				shortArrayOf(
					0,
					(4 * 3).toShort(),
					(8 * 3).toShort(),
					(12 * 3).toShort(),
					(16 * 3).toShort(),
					(22 * 3).toShort(),
					(30 * 3).toShort(),
					(42 * 3).toShort(),
					(58 * 3).toShort(),
					(78 * 3).toShort(),
					(104 * 3).toShort(),
					(138 * 3).toShort(),
					(180 * 3).toShort(),
					(192 * 3).toShort()
				),
				shortArrayOf(4, 4, 4, 4, 6, 8, 12, 16, 20, 26, 34, 42, 12)
			),

			/* MPEG 2.0 */
			bandInfoStruct(
				shortArrayOf(
					0,
					6,
					12,
					18,
					24,
					30,
					36,
					44,
					54,
					66,
					80,
					96,
					116,
					140,
					168,
					200,
					238,
					284,
					336,
					396,
					464,
					522,
					576
				),
				shortArrayOf(6, 6, 6, 6, 6, 6, 8, 10, 12, 14, 16, 20, 24, 28, 32, 38, 46, 52, 60, 68, 58, 54),
				shortArrayOf(
					0,
					(4 * 3).toShort(),
					(8 * 3).toShort(),
					(12 * 3).toShort(),
					(18 * 3).toShort(),
					(24 * 3).toShort(),
					(32 * 3).toShort(),
					(42 * 3).toShort(),
					(56 * 3).toShort(),
					(74 * 3).toShort(),
					(100 * 3).toShort(),
					(132 * 3).toShort(),
					(174 * 3).toShort(),
					(192 * 3).toShort()
				),
				shortArrayOf(4, 4, 4, 6, 6, 8, 10, 14, 18, 26, 32, 42, 18)
			),
			/* docs: 332. mpg123: 330 */
			bandInfoStruct(
				shortArrayOf(
					0,
					6,
					12,
					18,
					24,
					30,
					36,
					44,
					54,
					66,
					80,
					96,
					114,
					136,
					162,
					194,
					232,
					278,
					332,
					394,
					464,
					540,
					576
				),
				shortArrayOf(6, 6, 6, 6, 6, 6, 8, 10, 12, 14, 16, 18, 22, 26, 32, 38, 46, 54, 62, 70, 76, 36),
				shortArrayOf(
					0,
					(4 * 3).toShort(),
					(8 * 3).toShort(),
					(12 * 3).toShort(),
					(18 * 3).toShort(),
					(26 * 3).toShort(),
					(36 * 3).toShort(),
					(48 * 3).toShort(),
					(62 * 3).toShort(),
					(80 * 3).toShort(),
					(104 * 3).toShort(),
					(136 * 3).toShort(),
					(180 * 3).toShort(),
					(192 * 3).toShort()
				),
				shortArrayOf(4, 4, 4, 6, 8, 10, 12, 14, 18, 24, 32, 44, 12)
			),

			bandInfoStruct(
				shortArrayOf(
					0,
					6,
					12,
					18,
					24,
					30,
					36,
					44,
					54,
					66,
					80,
					96,
					116,
					140,
					168,
					200,
					238,
					284,
					336,
					396,
					464,
					522,
					576
				),
				shortArrayOf(6, 6, 6, 6, 6, 6, 8, 10, 12, 14, 16, 20, 24, 28, 32, 38, 46, 52, 60, 68, 58, 54),
				shortArrayOf(
					0,
					(4 * 3).toShort(),
					(8 * 3).toShort(),
					(12 * 3).toShort(),
					(18 * 3).toShort(),
					(26 * 3).toShort(),
					(36 * 3).toShort(),
					(48 * 3).toShort(),
					(62 * 3).toShort(),
					(80 * 3).toShort(),
					(104 * 3).toShort(),
					(134 * 3).toShort(),
					(174 * 3).toShort(),
					(192 * 3).toShort()
				),
				shortArrayOf(4, 4, 4, 6, 8, 10, 12, 14, 18, 24, 30, 40, 18)
			),
			/* MPEG 2.5 */
			bandInfoStruct(
				shortArrayOf(
					0,
					6,
					12,
					18,
					24,
					30,
					36,
					44,
					54,
					66,
					80,
					96,
					116,
					140,
					168,
					200,
					238,
					284,
					336,
					396,
					464,
					522,
					576
				),
				shortArrayOf(6, 6, 6, 6, 6, 6, 8, 10, 12, 14, 16, 20, 24, 28, 32, 38, 46, 52, 60, 68, 58, 54),
				shortArrayOf(0, 12, 24, 36, 54, 78, 108, 144, 186, 240, 312, 402, 522, 576),
				shortArrayOf(4, 4, 4, 6, 8, 10, 12, 14, 18, 24, 30, 40, 18)
			),
			bandInfoStruct(
				shortArrayOf(0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 116, 140, 168, 200, 238, 284, 336, 396, 464, 522, 576),
				shortArrayOf(6, 6, 6, 6, 6, 6, 8, 10, 12, 14, 16, 20, 24, 28, 32, 38, 46, 52, 60, 68, 58, 54),
				shortArrayOf(0, 12, 24, 36, 54, 78, 108, 144, 186, 240, 312, 402, 522, 576),
				shortArrayOf(4, 4, 4, 6, 8, 10, 12, 14, 18, 24, 30, 40, 18)
			),
			bandInfoStruct(
				shortArrayOf(0, 12, 24, 36, 48, 60, 72, 88, 108, 132, 160, 192, 232, 280, 336, 400, 476, 566, 568, 570, 572, 574, 576),
				shortArrayOf(12, 12, 12, 12, 12, 12, 16, 20, 24, 28, 32, 40, 48, 56, 64, 76, 90, 2, 2, 2, 2, 2),
				shortArrayOf(0, 24, 48, 72, 108, 156, 216, 288, 372, 480, 486, 492, 498, 576),
				shortArrayOf(8, 8, 8, 12, 16, 20, 24, 28, 36, 2, 2, 2, 26)
			)
		)
		private val Ci = doubleArrayOf(-0.6, -0.535, -0.33, -0.185, -0.095, -0.041, -0.0142, -0.0037)
		private val len = intArrayOf(36, 36, 12, 36)
	}

}

class MP3Data {
	var header_parsed: Boolean = false
	var stereo: Int = 0
	var samplerate: Int = 0
	var bitrate: Int = 0
	var mode: Int = 0
	var mode_ext: Int = 0
	var frameSize: Int = 0
	var numSamples: Int = 0
	var totalFrames: Int = 0
	var framesDecodedCounter: Int = 0
}

object MPG123 {
	const val M_SQRT2 = 1.41421356237309504880
	const val M_PI = 3.14159265358979323846

	const val SBLIMIT = 32
	const val SSLIMIT = 18

	//public static final int MPG_MD_STEREO = 0;
	const val MPG_MD_JOINT_STEREO = 1
	//public static final int MPG_MD_DUAL_CHANNEL = 2;
	const val MPG_MD_MONO = 3

	const val MAXFRAMESIZE = 2880

	/* AF: ADDED FOR LAYER1/LAYER2 */
	const val SCALE_BLOCK = 12

	internal class gr_info_s {
		var scfsi: Int = 0
		var part2_3_length: Int = 0
		var big_values: Int = 0
		var scalefac_compress: Int = 0
		var block_type: Int = 0
		var mixed_block_flag: Int = 0
		var table_select = IntArray(3)
		var maxband = IntArray(3)
		var maxbandl: Int = 0
		var maxb: Int = 0
		var region1start: Int = 0
		var region2start: Int = 0
		var preflag: Int = 0
		var scalefac_scale: Int = 0
		var count1table_select: Int = 0
		var full_gain = Array<FloatArray>(3) { floatArrayOf() }
		var full_gainPos = IntArray(3)
		var pow2gain: FloatArray = floatArrayOf()
		var pow2gainPos: Int = 0
	}

	internal class grT {

		var gr = Array<gr_info_s>(2) { gr_info_s() }

		init {
			gr[0] = gr_info_s()
			gr[1] = gr_info_s()
		}
	}

	internal class III_sideinfo {

		var main_data_begin: Int = 0

		var private_bits: Int = 0

		var ch = Array<grT>(2) { grT() }

		init {
			ch[0] = grT()
			ch[1] = grT()
		}
	}
}

class MPGLib(internal var interf: Interface) {
	companion object {
		const val MP3_ERR = -1
		const val MP3_OK = 0
		const val MP3_NEED_MORE = 1

		private val smpls = arrayOf(intArrayOf(0, 384, 1152, 1152), intArrayOf(0, 384, 1152, 576))
		private val OUTSIZE_CLIPPED = 4096
	}

	/* copy mono samples */
	protected fun COPY_MONO(
		pcm_l: FloatArray, pcm_lPos: Int,
		processed_samples: Int, p: FloatArray
	) {
		var lp = pcm_lPos
		var p_samples = 0
		for (i in 0 until processed_samples)
			pcm_l[lp++] = p[p_samples++]
	}

	/* copy stereo samples */
	protected fun COPY_STEREO(
		pcm_l: FloatArray, pcm_lPos: Int, pcm_r: FloatArray,
		pcm_rPos: Int, processed_samples: Int, p: FloatArray
	) {
		var lp = pcm_lPos
		var rp = pcm_rPos
		var p_samples = 0
		for (i in 0 until processed_samples) {
			pcm_l[lp++] = p[p_samples++]
			pcm_r[rp++] = p[p_samples++]
		}
	}

	private fun decode1_headersB_clipchoice(
		pmp: mpstr_tag, buffer: ByteArray,
		bufferPos: Int, len: Int, pcm_l: FloatArray, pcm_lPos: Int, pcm_r: FloatArray,
		pcm_rPos: Int, mp3data: MP3Data, enc: FrameSkip, p: FloatArray, psize: Int,
		decodeMP3_ptr: IDecoder
	): Int {

		mp3data.header_parsed = false

		val pb = ProcessedBytes()
		val ret = decodeMP3_ptr.decode(pmp, buffer, bufferPos, len, p, psize, pb)
		var processed_samples = pb.pb
		if (pmp.header_parsed || pmp.fsizeold > 0 || pmp.framesize > 0) {
			mp3data.header_parsed = true
			mp3data.stereo = pmp.fr.stereo
			mp3data.samplerate = Common.freqs[pmp.fr.sampling_frequency]
			mp3data.mode = pmp.fr.mode
			mp3data.mode_ext = pmp.fr.mode_ext
			mp3data.frameSize = smpls[pmp.fr.lsf][pmp.fr.lay]

			/* free format, we need the entire frame before we can determine
             * the bitrate.  If we haven't gotten the entire frame, bitrate=0 */
			if (pmp.fsizeold > 0)
			/* works for free format and fixed, no overrun, temporal results are < 400.e6 */ {
				mp3data.bitrate =
						(8 * (4 + pmp.fsizeold) * mp3data.samplerate / (1e3 * mp3data.frameSize) + 0.5).toInt()
			} else if (pmp.framesize > 0) {
				mp3data.bitrate =
						(8 * (4 + pmp.framesize) * mp3data.samplerate / (1e3 * mp3data.frameSize) + 0.5).toInt()
			} else {
				mp3data.bitrate = Common.tabsel_123[pmp.fr.lsf][pmp.fr.lay - 1][pmp.fr.bitrate_index]
			}


			if (pmp.num_frames > 0) {
				/* Xing VBR header found and num_frames was set */
				mp3data.totalFrames = pmp.num_frames
				mp3data.numSamples = mp3data.frameSize * pmp.num_frames
				enc.encoderDelay = pmp.enc_delay
				enc.encoderPadding = pmp.enc_padding
			}
		}

		when (ret) {
			MP3_OK -> when (pmp.fr.stereo) {
				1 -> COPY_MONO(pcm_l, pcm_lPos, processed_samples, p)
				2 -> {
					processed_samples = processed_samples shr 1
					COPY_STEREO(pcm_l, pcm_lPos, pcm_r, pcm_rPos, processed_samples, p)
				}
				else -> {
					processed_samples = -1
					assert(false)
				}
			}

			MP3_NEED_MORE -> processed_samples = 0

			MP3_ERR -> processed_samples = -1

			else -> {
				processed_samples = -1
				assert(false)
			}
		}

		return processed_samples
	}

	fun hip_decode_init(): mpstr_tag {
		return interf.InitMP3()
	}

	fun hip_decode_exit(hip: mpstr_tag?): Int {
		if (hip != null) interf.ExitMP3(hip)
		return 0
	}

	fun hip_decode1_headers(
		hip: mpstr_tag?, buffer: ByteArray,
		len: Int,
		pcm_l: FloatArray, pcm_r: FloatArray, mp3data: MP3Data,
		enc: FrameSkip
	): Int {
		if (hip != null) {
			val dec = object : IDecoder {
				override fun decode(
					mp: mpstr_tag,
					`in`: ByteArray,
					bufferPos: Int,
					isize: Int,
					out: FloatArray,
					osize: Int,
					done: ProcessedBytes
				): Int {
					return interf.decodeMP3(mp, `in`, bufferPos, isize, out, osize, done)
				}
			}
			val out = FloatArray(OUTSIZE_CLIPPED)
			return decode1_headersB_clipchoice(
				hip,
				buffer,
				0,
				len,
				pcm_l,
				0,
				pcm_r,
				0,
				mp3data,
				enc,
				out,
				OUTSIZE_CLIPPED,
				dec
			)
		}
		return -1
	}

	interface IDecoder {
		fun decode(
			mp: mpstr_tag,
			`in`: ByteArray,
			bufferPos: Int,
			isize: Int,
			out: FloatArray,
			osize: Int,
			done: ProcessedBytes
		): Int
	}

	class buf {
		internal var pnt: ByteArray = byteArrayOf()
		internal var size: Int = 0
		internal var pos: Int = 0
	}

	class mpstr_tag {
		var list: ArrayList<buf> = arrayListOf()
		var vbr_header: Boolean = false
		var num_frames: Int = 0
		var enc_delay: Int = 0
		var enc_padding: Int = 0
		var header_parsed: Boolean = false
		var side_parsed: Boolean = false
		var data_parsed: Boolean = false
		var free_format: Boolean = false
		var old_free_format: Boolean = false
		var bsize: Int = 0
		var framesize: Int = 0
		var ssize: Int = 0
		var dsize: Int = 0
		var fsizeold: Int = 0
		var fsizeold_nopadding: Int = 0
		var fr = Frame()
		var bsspace = Array(2) { ByteArray(MPG123.MAXFRAMESIZE + 1024) }
		var hybrid_block = Array(2) { Array(2) { FloatArray(MPG123.SBLIMIT * MPG123.SSLIMIT) } }
		var hybrid_blc = IntArray(2)
		var header: Long = 0
		var bsnum: Int = 0
		var synth_buffs = Array(2) { Array(2) { FloatArray(0x110) } }
		var synth_bo: Int = 0
		var sync_bitstream: Boolean = false
		var bitindex: Int = 0
		var wordpointer: ByteArray = byteArrayOf(0)
		var wordpointerPos: Int = 0
		var pinfo: PlottingData = PlottingData()
	}

	class ProcessedBytes {

		var pb: Int = 0
	}
}

class Parse {
	var layer: Int = -1;
	//public int silent;
	val mp3InputData = MP3Data()
}


/**
 * used by the frame analyzer
 */
class PlottingData {
	var mpg123xr = Array(2) { Array(2) { DoubleArray(576) } }
	var sfb = Array(2) { Array(2) { DoubleArray(SBMAX_l) } }
	var sfb_s = Array(2) { Array(2) { DoubleArray(3 * SBMAX_s) } }
	var qss = Array(2) { IntArray(2) }
	var big_values = Array(2) { IntArray(2) }
	var sub_gain = Array(2) { Array(2) { IntArray(3) } }
	var scalefac_scale = Array(2) { IntArray(2) }
	var preflag = Array(2) { IntArray(2) }
	var mpg123blocktype = Array(2) { IntArray(2) }
	var mixed = Array(2) { IntArray(2) }
	var mainbits = Array(2) { IntArray(2) }
	var sfbits = Array(2) { IntArray(2) }
	var stereo: Int = 0
	var js: Int = 0
	var ms_stereo: Int = 0
	var i_stereo: Int = 0
	var emph: Int = 0
	var bitrate: Int = 0
	var sampfreq: Int = 0
	var maindata: Int = 0
	var crc: Int = 0
	var padding: Int = 0
	var scfsi = IntArray(2)

	companion object {
		val SBMAX_l = 22
		val SBMAX_s = 13
	}
}

object TabInit {
	private val dewin = doubleArrayOf(0.000000000, -0.000015259, -0.000015259, -0.000015259, -0.000015259, -0.000015259, -0.000015259, -0.000030518, -0.000030518, -0.000030518, -0.000030518, -0.000045776, -0.000045776, -0.000061035, -0.000061035, -0.000076294, -0.000076294, -0.000091553, -0.000106812, -0.000106812, -0.000122070, -0.000137329, -0.000152588, -0.000167847, -0.000198364, -0.000213623, -0.000244141, -0.000259399, -0.000289917, -0.000320435, -0.000366211, -0.000396729, -0.000442505, -0.000473022, -0.000534058, -0.000579834, -0.000625610, -0.000686646, -0.000747681, -0.000808716, -0.000885010, -0.000961304, -0.001037598, -0.001113892, -0.001205444, -0.001296997, -0.001388550, -0.001480103, -0.001586914, -0.001693726, -0.001785278, -0.001907349, -0.002014160, -0.002120972, -0.002243042, -0.002349854, -0.002456665, -0.002578735, -0.002685547, -0.002792358, -0.002899170, -0.002990723, -0.003082275, -0.003173828, -0.003250122, -0.003326416, -0.003387451, -0.003433228, -0.003463745, -0.003479004, -0.003479004, -0.003463745, -0.003417969, -0.003372192, -0.003280640, -0.003173828, -0.003051758, -0.002883911, -0.002700806, -0.002487183, -0.002227783, -0.001937866, -0.001617432, -0.001266479, -0.000869751, -0.000442505, 0.000030518, 0.000549316, 0.001098633, 0.001693726, 0.002334595, 0.003005981, 0.003723145, 0.004486084, 0.005294800, 0.006118774, 0.007003784, 0.007919312, 0.008865356, 0.009841919, 0.010848999, 0.011886597, 0.012939453, 0.014022827, 0.015121460, 0.016235352, 0.017349243, 0.018463135, 0.019577026, 0.020690918, 0.021789551, 0.022857666, 0.023910522, 0.024932861, 0.025909424, 0.026840210, 0.027725220, 0.028533936, 0.029281616, 0.029937744, 0.030532837, 0.031005859, 0.031387329, 0.031661987, 0.031814575, 0.031845093, 0.031738281, 0.031478882, 0.031082153, 0.030517578, 0.029785156, 0.028884888, 0.027801514, 0.026535034, 0.025085449, 0.023422241, 0.021575928, 0.019531250, 0.017257690, 0.014801025, 0.012115479, 0.009231567, 0.006134033, 0.002822876, -0.000686646, -0.004394531, -0.008316040, -0.012420654, -0.016708374, -0.021179199, -0.025817871, -0.030609131, -0.035552979, -0.040634155, -0.045837402, -0.051132202, -0.056533813, -0.061996460, -0.067520142, -0.073059082, -0.078628540, -0.084182739, -0.089706421, -0.095169067, -0.100540161, -0.105819702, -0.110946655, -0.115921021, -0.120697021, -0.125259399, -0.129562378, -0.133590698, -0.137298584, -0.140670776, -0.143676758, -0.146255493, -0.148422241, -0.150115967, -0.151306152, -0.151962280, -0.152069092, -0.151596069, -0.150497437, -0.148773193, -0.146362305, -0.143264771, -0.139450073, -0.134887695, -0.129577637, -0.123474121, -0.116577148, -0.108856201, -0.100311279, -0.090927124, -0.080688477, -0.069595337, -0.057617187, -0.044784546, -0.031082153, -0.016510010, -0.001068115, 0.015228271, 0.032379150, 0.050354004, 0.069168091, 0.088775635, 0.109161377, 0.130310059, 0.152206421, 0.174789429, 0.198059082, 0.221984863, 0.246505737, 0.271591187, 0.297210693, 0.323318481, 0.349868774, 0.376800537, 0.404083252, 0.431655884, 0.459472656, 0.487472534, 0.515609741, 0.543823242, 0.572036743, 0.600219727, 0.628295898, 0.656219482, 0.683914185, 0.711318970, 0.738372803, 0.765029907, 0.791213989, 0.816864014, 0.841949463, 0.866363525, 0.890090942, 0.913055420, 0.935195923, 0.956481934, 0.976852417, 0.996246338, 1.014617920, 1.031936646, 1.048156738, 1.063217163, 1.077117920, 1.089782715, 1.101211548, 1.111373901, 1.120223999, 1.127746582, 1.133926392, 1.138763428, 1.142211914, 1.144287109, 1.144989014
	)
	val pnts = arrayOf(FloatArray(16), FloatArray(8), FloatArray(4), FloatArray(2), FloatArray(1))
	val decwin = FloatArray(512 + 32)

	init {
		make_decode_tables(32767)
	}

	private fun make_decode_tables(scaleval: Long) {
		make_decode_tables1()
		make_decode_tables2(scaleval)
		make_decode_tables3(scaleval)
	}

	private fun make_decode_tables1() {
		for (i in 0..4) {
			val kr = 0x10 shr i
			val divv = 0x40 shr i
			val costab = pnts[i]
			for (k in 0..kr - 1) {
				costab[k] = (1.0 / (2.0 * cos(MPG123.M_PI * (k.toDouble() * 2.0 + 1.0) / divv.toDouble()))).toFloat()
			}
		}
	}

	private fun make_decode_tables2(scaleval: Long) {
		var table = 0
		var sval = -scaleval
		var i = 0
		var j = 0
		while (i < 256) {
			if (table < 512 + 16) {
				decwin[table + 0] = (dewin[j] * sval).toFloat()
				decwin[table + 16] = decwin[table + 0]
			}
			if (i % 32 == 31) table -= 1023
			if (i % 64 == 63) sval = -sval
			i++
			j++
			table += 32
		}
	}

	private fun make_decode_tables3(scaleval: Long) {
		var table = 8
		var sval2 = -scaleval
		var i = 256
		var j = 256
		while (i < 512) {
			if (table < 512 + 16) {
				decwin[table + 0] = (dewin[j] * sval2).toFloat()
				decwin[table + 16] = decwin[table + 0]
			}
			if (i % 32 == 31) table -= 1023
			if (i % 64 == 63) sval2 = -sval2
			i++
			j--
			table += 32
		}
	}
}
object Tables {
	var bitrate_table = arrayOf(
		intArrayOf(0, 8, 16, 24, 32, 40, 48, 56, 64, 80, 96, 112, 128, 144, 160, -1), /* MPEG 2 */
		intArrayOf(0, 32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 256, 320, -1), /* MPEG 1 */
		intArrayOf(0, 8, 16, 24, 32, 40, 48, 56, 64, -1, -1, -1, -1, -1, -1, -1) /* MPEG 2.5 */
	)

	var samplerate_table = arrayOf(
		intArrayOf(22050, 24000, 16000, -1),
		intArrayOf(44100, 48000, 32000, -1),
		intArrayOf(11025, 12000, 8000, -1)
	)
}

class VBRTag {
	companion object {
		const val NUMTOCENTRIES = 100
		const private val FRAMES_FLAG = 0x0001
		const private val BYTES_FLAG = 0x0002
		const private val TOC_FLAG = 0x0004
		const private val VBR_SCALE_FLAG = 0x0008
		const private val VBRTag0 = "Xing"
		const private val VBRTag1 = "Info"
	}

	private fun extractInteger(buf: ByteArray, bufPos: Int): Int {
		var x = buf[bufPos + 0].unsigned
		x = x shl 8
		x = x or (buf[bufPos + 1].unsigned)
		x = x shl 8
		x = x or (buf[bufPos + 2].unsigned)
		x = x shl 8
		x = x or (buf[bufPos + 3].unsigned)
		return x
	}

	private fun isVbrTag(buf: ByteArray, bufPos: Int): Boolean {

		return buf.copyOfRange(bufPos, bufPos + VBRTag0.length).toString(ISO_8859_1) == VBRTag0 ||
				buf.copyOfRange(bufPos, bufPos + VBRTag1.length).toString(ISO_8859_1) == VBRTag1
	}

	fun getVbrTag(buf: ByteArray): VBRTagData? {
		val pTagData = VBRTagData()
		var bufPos = 0

		/* get Vbr header data */
		pTagData.flags = 0

		/* get selected MPEG header data */
		val hId = buf[bufPos + 1].unsigned shr 3 and 1
		val hSrIndex = buf[bufPos + 2].unsigned shr 2 and 3
		val hMode = buf[bufPos + 3].unsigned shr 6 and 3
		var hBitrate = buf[bufPos + 2].unsigned shr 4 and 0xf
		hBitrate = Tables.bitrate_table[hId][hBitrate]

		/* check for FFE syncword */
		pTagData.samprate =
				if (buf[bufPos + 1].unsigned shr 4 == 0xE) Tables.samplerate_table[2][hSrIndex] else Tables.samplerate_table[hId][hSrIndex]

		if (hId != 0) {
			bufPos += if (hMode != 3) 32 + 4 else 17 + 4 // mpeg1
		} else {
			bufPos += if (hMode != 3) 17 + 4 else 9 + 4 // mpeg2
		}

		if (!isVbrTag(buf, bufPos)) return null

		bufPos += 4

		pTagData.hId = hId

		/* get flags */
		pTagData.flags = extractInteger(buf, bufPos)
		val head_flags = pTagData.flags
		bufPos += 4

		if (head_flags and FRAMES_FLAG != 0) {
			pTagData.frames = extractInteger(buf, bufPos)
			bufPos += 4
		}

		if (head_flags and BYTES_FLAG != 0) {
			pTagData.bytes = extractInteger(buf, bufPos)
			bufPos += 4
		}

		if (head_flags and TOC_FLAG != 0) {
			arraycopy(buf, bufPos + 0, pTagData.toc, 0, NUMTOCENTRIES)
			bufPos += NUMTOCENTRIES
		}

		pTagData.vbrScale = -1

		if (head_flags and VBR_SCALE_FLAG != 0) {
			pTagData.vbrScale = extractInteger(buf, bufPos)
			bufPos += 4
		}

		pTagData.headersize = (hId + 1) * 72000 * hBitrate / pTagData.samprate

		bufPos += 21
		var encDelay = buf[bufPos + 0].unsigned shl 4
		encDelay += buf[bufPos + 1].unsigned shr 4
		var encPadding = buf[bufPos + 1].unsigned and 0x0F shl 8
		encPadding += buf[bufPos + 2].unsigned and 0xff
		if (encDelay < 0 || encDelay > 3000) encDelay = -1
		if (encPadding < 0 || encPadding > 3000) encPadding = -1

		pTagData.encDelay = encDelay
		pTagData.encPadding = encPadding

		return pTagData
	}
}

/**
 * Structure to receive extracted header (toc may be null).

 * @author Ken
 */
class VBRTagData {
	var frames: Int = 0
	var headersize: Int = 0
	var encDelay: Int = 0
	var encPadding: Int = 0
	var hId: Int = 0
	var samprate: Int = 0
	var flags: Int = 0
	var bytes: Int = 0
	var vbrScale: Int = 0
	var toc = ByteArray(VBRTag.NUMTOCENTRIES)
}
