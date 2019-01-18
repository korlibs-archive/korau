/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/* JOrbis
 * Copyright (C) 2000 ymnk, JCraft,Inc.
 *
 * Written by: 2000 ymnk<ymnk@jcraft.com>
 *
 * Many thanks to
 *   Monty <monty@xiph.org> and
 *   The XIPHOPHORUS Company http://www.xiph.org/ .
 * JOrbis has been based on their awesome works, Vorbis codec.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public License
 * as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package com.soywiz.korau.format.ogg.jcraft

import com.soywiz.kmem.*
import com.soywiz.korio.lang.*
import com.soywiz.korio.stream.*
import kotlin.math.*

class Block(var vd: DspState) {
	var pcm = arrayOf<FloatArray>()
	var opb = Buffer()

	var lW: Int = 0
	var W: Int = 0
	var nW: Int = 0
	var pcmend: Int = 0
	var mode: Int = 0

	var eofflag: Int = 0
	var granulepos: Long = 0
	var sequence: Long = 0

	var glue_bits: Int = 0
	var time_bits: Int = 0
	var floor_bits: Int = 0
	var res_bits: Int = 0

	init {
		if (vd.analysisp != 0) opb.writeinit()
	}

	fun init(vd: DspState) {
		this.vd = vd
	}

	fun clear(): Int {
		if (vd.analysisp != 0) opb.writeclear()
		return 0
	}

	fun synthesis(op: Packet): Int {
		val vi = vd.vi
		opb.readinit(op.packet_base, op.packet, op.bytes)
		if (opb.read(1) != 0) return -1

		val _mode = opb.read(vd.modebits)
		if (_mode == -1) return -1

		mode = _mode
		W = vi.mode_param[mode].blockflag
		if (W != 0) {
			lW = opb.read(1)
			nW = opb.read(1)
			if (nW == -1) return -1
		} else {
			lW = 0
			nW = 0
		}

		granulepos = op.granulepos
		sequence = op.packetno - 3
		eofflag = op.e_o_s

		pcmend = vi.blocksizes[W]
		if (pcm.size < vi.channels) pcm = Array<FloatArray>(vi.channels) { floatArrayOf() }
		for (i in 0 until vi.channels) {
			if (pcm[i].size < pcmend) {
				pcm[i] = FloatArray(pcmend)
			} else {
				pcm[i].fill(0f)
			}
		}

		val type = vi.map_type[vi.mode_param[mode].mapping]
		return vd.vi.mapping_P[type].inverse(this, vd.mode[mode])
	}
}

class CodeBook {
	var dim: Int = 0
	var entries: Int = 0
	var c = StaticCodeBook()

	var valuelist: FloatArray? = null
	var decode_tree: DecodeAux? = null

	private var t = IntArray(15)

	@kotlin.jvm.Synchronized
	fun decodevs_add(a: FloatArray, offset: Int, b: Buffer, n: Int): Int {
		val step = n / dim
		var entry: Int
		var j: Int

		if (t.size < step) {
			t = IntArray(step)
		}

		var i = 0
		while (i < step) {
			entry = decode(b)
			if (entry == -1) {
				return -1
			}
			t[i] = entry * dim
			i++
		}
		i = 0
		var o = 0
		while (i < dim) {
			j = 0
			while (j < step) {
				a[offset + o + j] += valuelist!![t[j] + i]
				j++
			}
			i++
			o += step
		}

		return 0
	}

	fun decodev_add(a: FloatArray, offset: Int, b: Buffer, n: Int): Int {
		var i: Int
		var j: Int
		var entry: Int
		var t: Int

		if (dim > 8) {
			i = 0
			while (i < n) {
				entry = decode(b)
				if (entry == -1) {
					return -1
				}
				t = entry * dim
				j = 0
				while (j < dim) {
					a[offset + i++] += valuelist!![t + j++]
				}
			}
		} else {
			i = 0
			while (i < n) {
				entry = decode(b)
				if (entry == -1) {
					return -1
				}
				t = entry * dim
				j = 0
				for (m in 0 until dim) a[offset + i++] += valuelist!![t + j++]
			}
		}
		return 0
	}

	fun decodev_set(a: FloatArray, offset: Int, b: Buffer, n: Int): Int {
		var i: Int
		var j: Int
		var entry: Int
		var t: Int

		i = 0
		while (i < n) {
			entry = decode(b)
			if (entry == -1) {
				return -1
			}
			t = entry * dim
			j = 0
			while (j < dim) {
				a[offset + i++] = valuelist!![t + j++]
			}
		}
		return 0
	}

	fun decodevv_add(a: Array<FloatArray>, offset: Int, ch: Int, b: Buffer, n: Int): Int {
		var i: Int
		var j: Int
		var entry: Int
		var chptr = 0

		i = offset / ch
		while (i < (offset + n) / ch) {
			entry = decode(b)
			if (entry == -1)
				return -1

			val t = entry * dim
			j = 0
			while (j < dim) {
				a[chptr++][i] += valuelist!![t + j]
				if (chptr == ch) {
					chptr = 0
					i++
				}
				j++
			}
		}
		return 0
	}

	fun decode(b: Buffer): Int {
		var ptr = 0
		val t = decode_tree
		val lok = b.look(t!!.tabn)

		if (lok >= 0) {
			ptr = t.tab[lok]
			b.adv(t.tabl[lok])
			if (ptr <= 0) {
				return -ptr
			}
		}
		do {
			when (b.read1()) {
				0 -> ptr = t.ptr0[ptr]
				1 -> ptr = t.ptr1[ptr]
				else -> return -1
			}
		} while (ptr > 0)
		return -ptr
	}

	fun decodevs(a: FloatArray, index: Int, b: Buffer, step: Int, addmul: Int): Int {
		val entry = decode(b)
		if (entry == -1)
			return -1
		when (addmul) {
			-1 -> run {
				var i = 0
				var o = 0
				while (i < dim) {
					a[index + o] = valuelist!![entry * dim + i]
					i++
					o += step
				}
			}
			0 -> run {
				var i = 0
				var o = 0
				while (i < dim) {
					a[index + o] += valuelist!![entry * dim + i]
					i++
					o += step
				}
			}
			1 -> {
				var i = 0
				var o = 0
				while (i < dim) {
					a[index + o] *= valuelist!![entry * dim + i]
					i++
					o += step
				}
			}
		}//System.err.println("CodeBook.decodeves: addmul="+addmul);
		return entry
	}

	fun clear() {}

	fun init_decode(s: StaticCodeBook): Int {
		c = s
		entries = s.entries
		dim = s.dim
		valuelist = s.unquantize()

		decode_tree = make_decode_tree()
		if (decode_tree == null) {
			clear()
			return -1
		}
		return 0
	}

	// build the decode helper tree from the codewords
	fun make_decode_tree(): DecodeAux? {
		var top = 0
		val t = DecodeAux()
		t.ptr0 = IntArray(entries * 2)
		val ptr0 = t.ptr0
		t.ptr1 = IntArray(entries * 2)
		val ptr1 = t.ptr1
		val codelist = make_words(c.lengthlist, c.entries) ?: return null

		t.aux = entries * 2

		for (i in 0 until entries) {
			if (c.lengthlist[i] > 0) {
				var ptr = 0
				var j = 0
				while (j < c.lengthlist[i] - 1) {
					val bit = codelist[i].ushr(j) and 1
					if (bit == 0) {
						if (ptr0[ptr] == 0) {
							ptr0[ptr] = ++top
						}
						ptr = ptr0[ptr]
					} else {
						if (ptr1[ptr] == 0) {
							ptr1[ptr] = ++top
						}
						ptr = ptr1[ptr]
					}
					j++
				}

				if (codelist[i].ushr(j) and 1 == 0) {
					ptr0[ptr] = -i
				} else {
					ptr1[ptr] = -i
				}

			}
		}

		t.tabn = Util.ilog(entries) - 4

		if (t.tabn < 5)
			t.tabn = 5
		val n = 1 shl t.tabn
		t.tab = IntArray(n)
		t.tabl = IntArray(n)
		for (i in 0 until n) {
			var p = 0
			var j = 0
			while (j < t.tabn && (p > 0 || j == 0)) {
				p = (if (i and (1 shl j) != 0) ptr1[p] else ptr0[p])
				j++
			}
			t.tab[i] = p // -code
			t.tabl[i] = j // length
		}

		return t
	}

	inner class DecodeAux {
		var tab: IntArray = intArrayOf()
		var tabl: IntArray = intArrayOf()
		var tabn: Int = 0

		var ptr0: IntArray = intArrayOf()
		var ptr1: IntArray = intArrayOf()
		var aux: Int = 0 // number of tree entries
	}

	companion object {
		fun make_words(l: IntArray, n: Int): IntArray? {
			val marker = IntArray(33)
			val r = IntArray(n)

			for (i in 0 until n) {
				val length = l[i]
				if (length > 0) {
					var entry = marker[length]

					if (length < 32 && entry.ushr(length) != 0) return null
					r[i] = entry

					run {
						for (j in length downTo 1) {
							if (marker[j] and 1 != 0) {
								if (j == 1) {
									marker[1]++
								} else {
									marker[j] = marker[j - 1] shl 1
								}
								break
							}
							marker[j]++
						}
					}

					for (j in length + 1..32) {
						if (marker[j].ushr(1) == entry) {
							entry = marker[j]
							marker[j] = marker[j - 1] shl 1
						} else {
							break
						}
					}
				}
			}

			for (i in 0 until n) {
				var temp = 0
				for (j in 0 until l[i]) {
					temp = temp shl 1
					temp = temp or (r[i].ushr(j) and 1)
				}
				r[i] = temp
			}

			return r
		}
	}
}

class Comment {
	var user_comments: Array<ByteArray?>? = null
	var comment_lengths: IntArray? = null
	var comments: Int = 0
	var vendor: ByteArray? = null

	fun init() {
		user_comments = null
		comments = 0
		vendor = null
	}

	fun unpack(opb: Buffer): Int {
		val vendorlen = opb.read(32)
		if (vendorlen < 0) {
			clear()
			return -1
		}
		vendor = ByteArray(vendorlen + 1)
		opb.read(vendor!!, vendorlen)
		comments = opb.read(32)
		if (comments < 0) {
			clear()
			return -1
		}
		user_comments = arrayOfNulls<ByteArray>(comments + 1)
		comment_lengths = IntArray(comments + 1)

		for (i in 0 until comments) {
			val len = opb.read(32)
			if (len < 0) {
				clear()
				return -1
			}
			comment_lengths!![i] = len
			user_comments!![i] = ByteArray(len + 1)
			opb.read(user_comments!![i]!!, len)
		}
		if (opb.read(1) != 1) {
			clear()
			return -1

		}
		return 0
	}

	fun clear() {
		for (i in 0 until comments) user_comments!![i] = null
		user_comments = null
		vendor = null
	}

	private fun ByteArray.toZeroString() = this.toString(UTF8)

	fun getVendor(): String = vendor!!.toZeroString()

	override fun toString(): String {
		var foo = "Vendor: " + getVendor()
		for (i in 0 until comments) {
			foo = "$foo\nComment: ${user_comments!![i]!!.toZeroString()}"
		}
		foo += "\n"
		return foo
	}
}

class Drft {
	var n: Int = 0
	var trigcache: FloatArray = floatArrayOf()
	var splitcache: IntArray = intArrayOf()

	fun backward(data: FloatArray) {
		if (n == 1)
			return
		drftb1(n, data, trigcache, trigcache, n, splitcache)
	}

	fun init(n: Int) {
		this.n = n
		trigcache = FloatArray(3 * n)
		splitcache = IntArray(32)
		fdrffti(n, trigcache, splitcache)
	}

	fun clear() {
		trigcache = floatArrayOf()
		splitcache = intArrayOf()
	}

	companion object {

		val ntryh = intArrayOf(4, 2, 3, 5)
		val tpi = 6.28318530717958647692528676655900577f
		val hsqt2 = .70710678118654752440084436210485f
		val taui = .86602540378443864676372317075293618f
		val taur = -.5f
		val sqrt2 = 1.4142135623730950488016887242097f

		fun drfti1(n: Int, wa: FloatArray, index: Int, ifac: IntArray) {
			var arg: Float
			var argh: Float
			var argld: Float
			var fi: Float
			var ntry = 0
			var i: Int
			var j = -1
			var k1: Int
			var l1: Int
			var l2: Int
			var ib: Int
			var ld: Int
			var ii: Int
			var ip: Int
			var `is`: Int
			var nq: Int
			var nr: Int
			var ido: Int
			var ipm: Int
			var nfm1: Int
			var nl = n
			var nf = 0

			var state = 101

			loop@ while (true) {
				when (state) {
					101 -> {
						j++
						if (j < 4) {
							ntry = ntryh[j]
						} else {
							ntry += 2
						}
						nq = nl / ntry
						nr = nl - ntry * nq
						if (nr != 0) {
							state = 101
							continue@loop
						}
						nf++
						ifac[nf + 1] = ntry
						nl = nq
						if (ntry != 2) {
							state = 107
							continue@loop
						}
						if (nf == 1) {
							state = 107
							continue@loop
						}

						i = 1
						while (i < nf) {
							ib = nf - i + 1
							ifac[ib + 1] = ifac[ib]
							i++
						}
						ifac[2] = 2
						if (nl != 1) {
							state = 104
							continue@loop
						}
						ifac[0] = n
						ifac[1] = nf
						argh = tpi / n
						`is` = 0
						nfm1 = nf - 1
						l1 = 1

						if (nfm1 == 0)
							return

						k1 = 0
						while (k1 < nfm1) {
							ip = ifac[k1 + 2]
							ld = 0
							l2 = l1 * ip
							ido = n / l2
							ipm = ip - 1

							j = 0
							while (j < ipm) {
								ld += l1
								i = `is`
								argld = ld.toFloat() * argh
								fi = 0f
								ii = 2
								while (ii < ido) {
									fi += 1f
									arg = fi * argld
									wa[index + i++] = cos(arg.toDouble()).toFloat()
									wa[index + i++] = sin(arg.toDouble()).toFloat()
									ii += 2
								}
								`is` += ido
								j++
							}
							l1 = l2
							k1++
						}
						continue@loop
					}
					104 -> {
						nq = nl / ntry
						nr = nl - ntry * nq
						if (nr != 0) {
							state = 101
							continue@loop
						}
						nf++
						ifac[nf + 1] = ntry
						nl = nq
						if (ntry != 2) {
							state = 107
							continue@loop
						}
						if (nf == 1) {
							state = 107
							continue@loop
						}
						i = 1
						while (i < nf) {
							ib = nf - i + 1
							ifac[ib + 1] = ifac[ib]
							i++
						}
						ifac[2] = 2
						if (nl != 1) {
							state = 104
							continue@loop
						}
						ifac[0] = n
						ifac[1] = nf
						argh = tpi / n
						`is` = 0
						nfm1 = nf - 1
						l1 = 1
						if (nfm1 == 0)
							return
						k1 = 0
						while (k1 < nfm1) {
							ip = ifac[k1 + 2]
							ld = 0
							l2 = l1 * ip
							ido = n / l2
							ipm = ip - 1
							j = 0
							while (j < ipm) {
								ld += l1
								i = `is`
								argld = ld.toFloat() * argh
								fi = 0f
								ii = 2
								while (ii < ido) {
									fi += 1f
									arg = fi * argld
									wa[index + i++] = cos(arg.toDouble()).toFloat()
									wa[index + i++] = sin(arg.toDouble()).toFloat()
									ii += 2
								}
								`is` += ido
								j++
							}
							l1 = l2
							k1++
						}
						continue@loop
					}
					107 -> {
						if (nl != 1) {
							state = 104
							continue@loop
						}
						ifac[0] = n
						ifac[1] = nf
						argh = tpi / n
						`is` = 0
						nfm1 = nf - 1
						l1 = 1
						if (nfm1 == 0)
							return
						k1 = 0
						while (k1 < nfm1) {
							ip = ifac[k1 + 2]
							ld = 0
							l2 = l1 * ip
							ido = n / l2
							ipm = ip - 1
							j = 0
							while (j < ipm) {
								ld += l1
								i = `is`
								argld = ld.toFloat() * argh
								fi = 0f
								ii = 2
								while (ii < ido) {
									fi += 1f
									arg = fi * argld
									wa[index + i++] = cos(arg.toDouble()).toFloat()
									wa[index + i++] = sin(arg.toDouble()).toFloat()
									ii += 2
								}
								`is` += ido
								j++
							}
							l1 = l2
							k1++
						}
						continue@loop
					}
				}
			}
		}

		fun fdrffti(n: Int, wsave: FloatArray, ifac: IntArray) {
			if (n == 1)
				return
			drfti1(n, wsave, n, ifac)
		}

		fun dradf2(
			ido: Int, l1: Int, cc: FloatArray, ch: FloatArray, wa1: FloatArray,
			index: Int
		) {
			var i: Int
			var k: Int
			var ti2: Float
			var tr2: Float
			val t0: Int
			var t1: Int
			var t2: Int
			var t3: Int
			var t4: Int
			var t5: Int
			var t6: Int

			t1 = 0
			t2 = l1 * ido
			t0 = t2
			t3 = ido shl 1
			k = 0
			while (k < l1) {
				ch[t1 shl 1] = cc[t1] + cc[t2]
				ch[(t1 shl 1) + t3 - 1] = cc[t1] - cc[t2]
				t1 += ido
				t2 += ido
				k++
			}

			if (ido < 2)
				return

			if (ido != 2) {
				t1 = 0
				t2 = t0
				k = 0
				while (k < l1) {
					t3 = t2
					t4 = (t1 shl 1) + (ido shl 1)
					t5 = t1
					t6 = t1 + t1
					i = 2
					while (i < ido) {
						t3 += 2
						t4 -= 2
						t5 += 2
						t6 += 2
						tr2 = wa1[index + i - 2] * cc[t3 - 1] + wa1[index + i - 1] * cc[t3]
						ti2 = wa1[index + i - 2] * cc[t3] - wa1[index + i - 1] * cc[t3 - 1]
						ch[t6] = cc[t5] + ti2
						ch[t4] = ti2 - cc[t5]
						ch[t6 - 1] = cc[t5 - 1] + tr2
						ch[t4 - 1] = cc[t5 - 1] - tr2
						i += 2
					}
					t1 += ido
					t2 += ido
					k++
				}
				if (ido % 2 == 1)
					return
			}

			t1 = ido
			t2 = t1 - 1
			t3 = t2
			t2 += t0
			k = 0
			while (k < l1) {
				ch[t1] = -cc[t2]
				ch[t1 - 1] = cc[t3]
				t1 += ido shl 1
				t2 += ido
				t3 += ido
				k++
			}
		}

		fun dradf4(
			ido: Int, l1: Int, cc: FloatArray, ch: FloatArray, wa1: FloatArray,
			index1: Int, wa2: FloatArray, index2: Int, wa3: FloatArray, index3: Int
		) {
			var i: Int
			var k: Int
			val t0: Int
			var t1: Int
			var t2: Int
			var t3: Int
			var t4: Int
			var t5: Int
			var t6: Int
			var ci2: Float
			var ci3: Float
			var ci4: Float
			var cr2: Float
			var cr3: Float
			var cr4: Float
			var ti1: Float
			var ti2: Float
			var ti3: Float
			var ti4: Float
			var tr1: Float
			var tr2: Float
			var tr3: Float
			var tr4: Float
			t0 = l1 * ido

			t1 = t0
			t4 = t1 shl 1
			t2 = t1 + (t1 shl 1)
			t3 = 0

			k = 0
			while (k < l1) {
				tr1 = cc[t1] + cc[t2]
				tr2 = cc[t3] + cc[t4]

				t5 = t3 shl 2
				ch[t5] = tr1 + tr2
				ch[(ido shl 2) + t5 - 1] = tr2 - tr1
				t5 += ido shl 1
				ch[t5 - 1] = cc[t3] - cc[t4]
				ch[t5] = cc[t2] - cc[t1]

				t1 += ido
				t2 += ido
				t3 += ido
				t4 += ido
				k++
			}
			if (ido < 2)
				return

			if (ido != 2) {
				t1 = 0
				k = 0
				while (k < l1) {
					t2 = t1
					t4 = t1 shl 2
					t6 = ido shl 1
					t5 = t6 + t4
					i = 2
					while (i < ido) {
						t2 += 2
						t3 = 2
						t4 += 2
						t5 -= 2

						t3 += t0
						cr2 = wa1[index1 + i - 2] * cc[t3 - 1] + wa1[index1 + i - 1] * cc[t3]
						ci2 = wa1[index1 + i - 2] * cc[t3] - wa1[index1 + i - 1] * cc[t3 - 1]
						t3 += t0
						cr3 = wa2[index2 + i - 2] * cc[t3 - 1] + wa2[index2 + i - 1] * cc[t3]
						ci3 = wa2[index2 + i - 2] * cc[t3] - wa2[index2 + i - 1] * cc[t3 - 1]
						t3 += t0
						cr4 = wa3[index3 + i - 2] * cc[t3 - 1] + wa3[index3 + i - 1] * cc[t3]
						ci4 = wa3[index3 + i - 2] * cc[t3] - wa3[index3 + i - 1] * cc[t3 - 1]

						tr1 = cr2 + cr4
						tr4 = cr4 - cr2
						ti1 = ci2 + ci4
						ti4 = ci2 - ci4

						ti2 = cc[t2] + ci3
						ti3 = cc[t2] - ci3
						tr2 = cc[t2 - 1] + cr3
						tr3 = cc[t2 - 1] - cr3

						ch[t4 - 1] = tr1 + tr2
						ch[t4] = ti1 + ti2

						ch[t5 - 1] = tr3 - ti4
						ch[t5] = tr4 - ti3

						ch[t4 + t6 - 1] = ti4 + tr3
						ch[t4 + t6] = tr4 + ti3

						ch[t5 + t6 - 1] = tr2 - tr1
						ch[t5 + t6] = ti1 - ti2
						i += 2
					}
					t1 += ido
					k++
				}
				if (ido and 1 != 0)
					return
			}

			t1 = t0 + ido - 1
			t2 = t1 + (t0 shl 1)
			t3 = ido shl 2
			t4 = ido
			t5 = ido shl 1
			t6 = ido

			k = 0
			while (k < l1) {
				ti1 = -hsqt2 * (cc[t1] + cc[t2])
				tr1 = hsqt2 * (cc[t1] - cc[t2])

				ch[t4 - 1] = tr1 + cc[t6 - 1]
				ch[t4 + t5 - 1] = cc[t6 - 1] - tr1

				ch[t4] = ti1 - cc[t1 + t0]
				ch[t4 + t5] = ti1 + cc[t1 + t0]

				t1 += ido
				t2 += ido
				t4 += t3
				t6 += ido
				k++
			}
		}

		fun dradfg(
			ido: Int, ip: Int, l1: Int, idl1: Int, cc: FloatArray, c1: FloatArray,
			c2: FloatArray, ch: FloatArray, ch2: FloatArray, wa: FloatArray, index: Int
		) {
			var idij: Int
			val ipph: Int
			var i: Int
			var j: Int
			var k: Int
			var l: Int
			var ic: Int
			var ik: Int
			var `is`: Int
			val t0: Int
			var t1: Int
			var t2 = 0
			var t3: Int
			var t4: Int
			var t5: Int
			var t6: Int
			var t7: Int
			var t8: Int
			var t9: Int
			val t10: Int
			var dc2: Float
			var ai1: Float
			var ai2: Float
			var ar1: Float
			var ar2: Float
			var ds2: Float
			val nbd: Int
			var dcp = 0f
			val arg: Float
			var dsp = 0f
			var ar1h: Float
			var ar2h: Float
			val idp2: Int
			val ipp2: Int

			arg = tpi / ip.toFloat()
			dcp = cos(arg.toDouble()).toFloat()
			dsp = sin(arg.toDouble()).toFloat()
			ipph = ip + 1 shr 1
			ipp2 = ip
			idp2 = ido
			nbd = ido - 1 shr 1
			t0 = l1 * ido
			t10 = ip * ido

			var state = 100
			loop@ while (true) {
				when (state) {
					101 -> {
						if (ido == 1) {
							state = 119
							continue@loop
						}
						ik = 0
						while (ik < idl1) {
							ch2[ik] = c2[ik]
							ik++
						}

						t1 = 0
						j = 1
						while (j < ip) {
							t1 += t0
							t2 = t1
							k = 0
							while (k < l1) {
								ch[t2] = c1[t2]
								t2 += ido
								k++
							}
							j++
						}

						`is` = -ido
						t1 = 0
						if (nbd > l1) {
							j = 1
							while (j < ip) {
								t1 += t0
								`is` += ido
								t2 = -ido + t1
								k = 0
								while (k < l1) {
									idij = `is` - 1
									t2 += ido
									t3 = t2
									i = 2
									while (i < ido) {
										idij += 2
										t3 += 2
										ch[t3 - 1] = wa[index + idij - 1] * c1[t3 - 1] + wa[index + idij] * c1[t3]
										ch[t3] = wa[index + idij - 1] * c1[t3] - wa[index + idij] * c1[t3 - 1]
										i += 2
									}
									k++
								}
								j++
							}
						} else {

							j = 1
							while (j < ip) {
								`is` += ido
								idij = `is` - 1
								t1 += t0
								t2 = t1
								i = 2
								while (i < ido) {
									idij += 2
									t2 += 2
									t3 = t2
									k = 0
									while (k < l1) {
										ch[t3 - 1] = wa[index + idij - 1] * c1[t3 - 1] + wa[index + idij] * c1[t3]
										ch[t3] = wa[index + idij - 1] * c1[t3] - wa[index + idij] * c1[t3 - 1]
										t3 += ido
										k++
									}
									i += 2
								}
								j++
							}
						}

						t1 = 0
						t2 = ipp2 * t0
						if (nbd < l1) {
							j = 1
							while (j < ipph) {
								t1 += t0
								t2 -= t0
								t3 = t1
								t4 = t2
								i = 2
								while (i < ido) {
									t3 += 2
									t4 += 2
									t5 = t3 - ido
									t6 = t4 - ido
									k = 0
									while (k < l1) {
										t5 += ido
										t6 += ido
										c1[t5 - 1] = ch[t5 - 1] + ch[t6 - 1]
										c1[t6 - 1] = ch[t5] - ch[t6]
										c1[t5] = ch[t5] + ch[t6]
										c1[t6] = ch[t6 - 1] - ch[t5 - 1]
										k++
									}
									i += 2
								}
								j++
							}
						} else {
							j = 1
							while (j < ipph) {
								t1 += t0
								t2 -= t0
								t3 = t1
								t4 = t2
								k = 0
								while (k < l1) {
									t5 = t3
									t6 = t4
									i = 2
									while (i < ido) {
										t5 += 2
										t6 += 2
										c1[t5 - 1] = ch[t5 - 1] + ch[t6 - 1]
										c1[t6 - 1] = ch[t5] - ch[t6]
										c1[t5] = ch[t5] + ch[t6]
										c1[t6] = ch[t6 - 1] - ch[t5 - 1]
										i += 2
									}
									t3 += ido
									t4 += ido
									k++
								}
								j++
							}
						}
						ik = 0
						while (ik < idl1) {
							c2[ik] = ch2[ik]
							ik++
						}

						t1 = 0
						t2 = ipp2 * idl1
						j = 1
						while (j < ipph) {
							t1 += t0
							t2 -= t0
							t3 = t1 - ido
							t4 = t2 - ido
							k = 0
							while (k < l1) {
								t3 += ido
								t4 += ido
								c1[t3] = ch[t3] + ch[t4]
								c1[t4] = ch[t4] - ch[t3]
								k++
							}
							j++
						}

						ar1 = 1f
						ai1 = 0f
						t1 = 0
						t2 = ipp2 * idl1
						t3 = (ip - 1) * idl1
						l = 1
						while (l < ipph) {
							t1 += idl1
							t2 -= idl1
							ar1h = dcp * ar1 - dsp * ai1
							ai1 = dcp * ai1 + dsp * ar1
							ar1 = ar1h
							t4 = t1
							t5 = t2
							t6 = t3
							t7 = idl1

							ik = 0
							while (ik < idl1) {
								ch2[t4++] = c2[ik] + ar1 * c2[t7++]
								ch2[t5++] = ai1 * c2[t6++]
								ik++
							}

							dc2 = ar1
							ds2 = ai1
							ar2 = ar1
							ai2 = ai1

							t4 = idl1
							t5 = (ipp2 - 1) * idl1
							j = 2
							while (j < ipph) {
								t4 += idl1
								t5 -= idl1

								ar2h = dc2 * ar2 - ds2 * ai2
								ai2 = dc2 * ai2 + ds2 * ar2
								ar2 = ar2h

								t6 = t1
								t7 = t2
								t8 = t4
								t9 = t5
								ik = 0
								while (ik < idl1) {
									ch2[t6++] += ar2 * c2[t8++]
									ch2[t7++] += ai2 * c2[t9++]
									ik++
								}
								j++
							}
							l++
						}
						t1 = 0
						j = 1
						while (j < ipph) {
							t1 += idl1
							t2 = t1
							ik = 0
							while (ik < idl1) {
								ch2[ik] += c2[t2++]
								ik++
							}
							j++
						}

						if (ido < l1) {
							state = 132
							continue@loop
						}

						t1 = 0
						t2 = 0
						k = 0
						while (k < l1) {
							t3 = t1
							t4 = t2
							i = 0
							while (i < ido) {
								cc[t4++] = ch[t3++]
								i++
							}
							t1 += ido
							t2 += t10
							k++
						}
						state = 135
					}
					119 -> {
						ik = 0
						while (ik < idl1) {
							c2[ik] = ch2[ik]
							ik++
						}
						t1 = 0
						t2 = ipp2 * idl1
						j = 1
						while (j < ipph) {
							t1 += t0
							t2 -= t0
							t3 = t1 - ido
							t4 = t2 - ido
							k = 0
							while (k < l1) {
								t3 += ido
								t4 += ido
								c1[t3] = ch[t3] + ch[t4]
								c1[t4] = ch[t4] - ch[t3]
								k++
							}
							j++
						}
						ar1 = 1f
						ai1 = 0f
						t1 = 0
						t2 = ipp2 * idl1
						t3 = (ip - 1) * idl1
						l = 1
						while (l < ipph) {
							t1 += idl1
							t2 -= idl1
							ar1h = dcp * ar1 - dsp * ai1
							ai1 = dcp * ai1 + dsp * ar1
							ar1 = ar1h
							t4 = t1
							t5 = t2
							t6 = t3
							t7 = idl1
							ik = 0
							while (ik < idl1) {
								ch2[t4++] = c2[ik] + ar1 * c2[t7++]
								ch2[t5++] = ai1 * c2[t6++]
								ik++
							}
							dc2 = ar1
							ds2 = ai1
							ar2 = ar1
							ai2 = ai1
							t4 = idl1
							t5 = (ipp2 - 1) * idl1
							j = 2
							while (j < ipph) {
								t4 += idl1
								t5 -= idl1
								ar2h = dc2 * ar2 - ds2 * ai2
								ai2 = dc2 * ai2 + ds2 * ar2
								ar2 = ar2h
								t6 = t1
								t7 = t2
								t8 = t4
								t9 = t5
								ik = 0
								while (ik < idl1) {
									ch2[t6++] += ar2 * c2[t8++]
									ch2[t7++] += ai2 * c2[t9++]
									ik++
								}
								j++
							}
							l++
						}
						t1 = 0
						j = 1
						while (j < ipph) {
							t1 += idl1
							t2 = t1
							ik = 0
							while (ik < idl1) {
								ch2[ik] += c2[t2++]
								ik++
							}
							j++
						}
						if (ido < l1) {
							state = 132
							continue@loop
						}
						t1 = 0
						t2 = 0
						k = 0
						while (k < l1) {
							t3 = t1
							t4 = t2
							i = 0
							while (i < ido) {
								cc[t4++] = ch[t3++]
								i++
							}
							t1 += ido
							t2 += t10
							k++
						}
						state = 135
					}

					132 -> {
						i = 0
						while (i < ido) {
							t1 = i
							t2 = i
							k = 0
							while (k < l1) {
								cc[t2] = ch[t1]
								t1 += ido
								t2 += t10
								k++
							}
							i++
						}
						t1 = 0
						t2 = ido shl 1
						t3 = 0
						t4 = ipp2 * t0
						j = 1
						while (j < ipph) {
							t1 += t2
							t3 += t0
							t4 -= t0

							t5 = t1
							t6 = t3
							t7 = t4

							k = 0
							while (k < l1) {
								cc[t5 - 1] = ch[t6]
								cc[t5] = ch[t7]
								t5 += t10
								t6 += ido
								t7 += ido
								k++
							}
							j++
						}

						if (ido == 1)
							return
						if (nbd < l1) {
							state = 141
							continue@loop
						}

						t1 = -ido
						t3 = 0
						t4 = 0
						t5 = ipp2 * t0
						j = 1
						while (j < ipph) {
							t1 += t2
							t3 += t2
							t4 += t0
							t5 -= t0
							t6 = t1
							t7 = t3
							t8 = t4
							t9 = t5
							k = 0
							while (k < l1) {
								i = 2
								while (i < ido) {
									ic = idp2 - i
									cc[i + t7 - 1] = ch[i + t8 - 1] + ch[i + t9 - 1]
									cc[ic + t6 - 1] = ch[i + t8 - 1] - ch[i + t9 - 1]
									cc[i + t7] = ch[i + t8] + ch[i + t9]
									cc[ic + t6] = ch[i + t9] - ch[i + t8]
									i += 2
								}
								t6 += t10
								t7 += t10
								t8 += ido
								t9 += ido
								k++
							}
							j++
						}
						return
					}
					135 -> {
						t1 = 0
						t2 = ido shl 1
						t3 = 0
						t4 = ipp2 * t0
						j = 1
						while (j < ipph) {
							t1 += t2
							t3 += t0
							t4 -= t0
							t5 = t1
							t6 = t3
							t7 = t4
							k = 0
							while (k < l1) {
								cc[t5 - 1] = ch[t6]
								cc[t5] = ch[t7]
								t5 += t10
								t6 += ido
								t7 += ido
								k++
							}
							j++
						}
						if (ido == 1)
							return
						if (nbd < l1) {
							state = 141
							continue@loop
						}
						t1 = -ido
						t3 = 0
						t4 = 0
						t5 = ipp2 * t0
						j = 1
						while (j < ipph) {
							t1 += t2
							t3 += t2
							t4 += t0
							t5 -= t0
							t6 = t1
							t7 = t3
							t8 = t4
							t9 = t5
							k = 0
							while (k < l1) {
								i = 2
								while (i < ido) {
									ic = idp2 - i
									cc[i + t7 - 1] = ch[i + t8 - 1] + ch[i + t9 - 1]
									cc[ic + t6 - 1] = ch[i + t8 - 1] - ch[i + t9 - 1]
									cc[i + t7] = ch[i + t8] + ch[i + t9]
									cc[ic + t6] = ch[i + t9] - ch[i + t8]
									i += 2
								}
								t6 += t10
								t7 += t10
								t8 += ido
								t9 += ido
								k++
							}
							j++
						}
						return
					}
					141 -> {
						t1 = -ido
						t3 = 0
						t4 = 0
						t5 = ipp2 * t0
						j = 1
						while (j < ipph) {
							t1 += t2
							t3 += t2
							t4 += t0
							t5 -= t0
							i = 2
							while (i < ido) {
								t6 = idp2 + t1 - i
								t7 = i + t3
								t8 = i + t4
								t9 = i + t5
								k = 0
								while (k < l1) {
									cc[t7 - 1] = ch[t8 - 1] + ch[t9 - 1]
									cc[t6 - 1] = ch[t8 - 1] - ch[t9 - 1]
									cc[t7] = ch[t8] + ch[t9]
									cc[t6] = ch[t9] - ch[t8]
									t6 += t10
									t7 += t10
									t8 += ido
									t9 += ido
									k++
								}
								i += 2
							}
							j++
						}
						continue@loop
					}
				}
			}
		}

		fun drftf1(n: Int, c: FloatArray, ch: FloatArray, wa: FloatArray, ifac: IntArray) {
			var i: Int
			var k1: Int
			var l1: Int
			var l2: Int
			var na: Int
			var kh: Int
			val nf: Int
			var ip: Int
			var iw: Int
			var ido: Int
			var idl1: Int
			var ix2: Int
			var ix3: Int

			nf = ifac[1]
			na = 1
			l2 = n
			iw = n

			k1 = 0
			while (k1 < nf) {
				kh = nf - k1
				ip = ifac[kh + 1]
				l1 = l2 / ip
				ido = n / l2
				idl1 = ido * l1
				iw -= (ip - 1) * ido
				na = 1 - na

				var state = 100
				loop@ while (true) {
					when (state) {
						100 -> {
							if (ip != 4) {
								state = 102
								continue@loop
							}

							ix2 = iw + ido
							ix3 = ix2 + ido
							if (na != 0) {
								dradf4(
									ido,
									l1,
									ch,
									c,
									wa,
									iw - 1,
									wa,
									ix2 - 1,
									wa,
									ix3 - 1
								)
							} else {
								dradf4(
									ido,
									l1,
									c,
									ch,
									wa,
									iw - 1,
									wa,
									ix2 - 1,
									wa,
									ix3 - 1
								)
							}
							state = 110
						}
						102 -> {
							if (ip != 2) {
								state = 104
								continue@loop
							}
							if (na != 0) {
								state = 103
								continue@loop
							}
							dradf2(ido, l1, c, ch, wa, iw - 1)
							state = 110
						}
						103 -> {
							dradf2(ido, l1, ch, c, wa, iw - 1)
							if (ido == 1)
								na = 1 - na
							if (na != 0) {
								state = 109
								continue@loop
							}
							dradfg(
								ido,
								ip,
								l1,
								idl1,
								c,
								c,
								c,
								ch,
								ch,
								wa,
								iw - 1
							)
							na = 1
							state = 110
						}
						104 -> {
							if (ido == 1)
								na = 1 - na
							if (na != 0) {
								state = 109
								continue@loop
							}
							dradfg(
								ido,
								ip,
								l1,
								idl1,
								c,
								c,
								c,
								ch,
								ch,
								wa,
								iw - 1
							)
							na = 1
							state = 110
						}
						109 -> {
							dradfg(
								ido,
								ip,
								l1,
								idl1,
								ch,
								ch,
								ch,
								c,
								c,
								wa,
								iw - 1
							)
							na = 0
							l2 = l1
							continue@loop
						}
						110 -> {
							l2 = l1
							continue@loop
						}
					}
				}
				k1++
			}
			if (na == 1)
				return
			i = 0
			while (i < n) {
				c[i] = ch[i]
				i++
			}
		}

		fun dradb2(
			ido: Int, l1: Int, cc: FloatArray, ch: FloatArray, wa1: FloatArray,
			index: Int
		) {
			var i: Int
			var k: Int
			val t0: Int
			var t1: Int
			var t2: Int
			var t3: Int
			var t4: Int
			var t5: Int
			var t6: Int
			var ti2: Float
			var tr2: Float

			t0 = l1 * ido

			t1 = 0
			t2 = 0
			t3 = (ido shl 1) - 1
			k = 0
			while (k < l1) {
				ch[t1] = cc[t2] + cc[t3 + t2]
				ch[t1 + t0] = cc[t2] - cc[t3 + t2]
				t1 += ido
				t2 = t1 shl 1
				k++
			}

			if (ido < 2)
				return
			if (ido != 2) {
				t1 = 0
				t2 = 0
				k = 0
				while (k < l1) {
					t3 = t1
					t4 = t2
					t5 = t4 + (ido shl 1)
					t6 = t0 + t1
					i = 2
					while (i < ido) {
						t3 += 2
						t4 += 2
						t5 -= 2
						t6 += 2
						ch[t3 - 1] = cc[t4 - 1] + cc[t5 - 1]
						tr2 = cc[t4 - 1] - cc[t5 - 1]
						ch[t3] = cc[t4] - cc[t5]
						ti2 = cc[t4] + cc[t5]
						ch[t6 - 1] = wa1[index + i - 2] * tr2 - wa1[index + i - 1] * ti2
						ch[t6] = wa1[index + i - 2] * ti2 + wa1[index + i - 1] * tr2
						i += 2
					}
					t1 += ido
					t2 = t1 shl 1
					k++
				}
				if (ido % 2 == 1)
					return
			}

			t1 = ido - 1
			t2 = ido - 1
			k = 0
			while (k < l1) {
				ch[t1] = cc[t2] + cc[t2]
				ch[t1 + t0] = -(cc[t2 + 1] + cc[t2 + 1])
				t1 += ido
				t2 += ido shl 1
				k++
			}
		}

		fun dradb3(
			ido: Int, l1: Int, cc: FloatArray, ch: FloatArray, wa1: FloatArray,
			index1: Int, wa2: FloatArray, index2: Int
		) {
			var i: Int
			var k: Int
			val t0: Int
			var t1: Int
			val t2: Int
			var t3: Int
			val t4: Int
			var t5: Int
			var t6: Int
			var t7: Int
			var t8: Int
			var t9: Int
			var t10: Int
			var ci2: Float
			var ci3: Float
			var di2: Float
			var di3: Float
			var cr2: Float
			var cr3: Float
			var dr2: Float
			var dr3: Float
			var ti2: Float
			var tr2: Float
			t0 = l1 * ido

			t1 = 0
			t2 = t0 shl 1
			t3 = ido shl 1
			t4 = ido + (ido shl 1)
			t5 = 0
			k = 0
			while (k < l1) {
				tr2 = cc[t3 - 1] + cc[t3 - 1]
				cr2 = cc[t5] + taur * tr2
				ch[t1] = cc[t5] + tr2
				ci3 = taui * (cc[t3] + cc[t3])
				ch[t1 + t0] = cr2 - ci3
				ch[t1 + t2] = cr2 + ci3
				t1 += ido
				t3 += t4
				t5 += t4
				k++
			}

			if (ido == 1)
				return

			t1 = 0
			t3 = ido shl 1
			k = 0
			while (k < l1) {
				t7 = t1 + (t1 shl 1)
				t5 = t7 + t3
				t6 = t5
				t8 = t1
				t9 = t1 + t0
				t10 = t9 + t0

				i = 2
				while (i < ido) {
					t5 += 2
					t6 -= 2
					t7 += 2
					t8 += 2
					t9 += 2
					t10 += 2
					tr2 = cc[t5 - 1] + cc[t6 - 1]
					cr2 = cc[t7 - 1] + taur * tr2
					ch[t8 - 1] = cc[t7 - 1] + tr2
					ti2 = cc[t5] - cc[t6]
					ci2 = cc[t7] + taur * ti2
					ch[t8] = cc[t7] + ti2
					cr3 = taui * (cc[t5 - 1] - cc[t6 - 1])
					ci3 = taui * (cc[t5] + cc[t6])
					dr2 = cr2 - ci3
					dr3 = cr2 + ci3
					di2 = ci2 + cr3
					di3 = ci2 - cr3
					ch[t9 - 1] = wa1[index1 + i - 2] * dr2 - wa1[index1 + i - 1] * di2
					ch[t9] = wa1[index1 + i - 2] * di2 + wa1[index1 + i - 1] * dr2
					ch[t10 - 1] = wa2[index2 + i - 2] * dr3 - wa2[index2 + i - 1] * di3
					ch[t10] = wa2[index2 + i - 2] * di3 + wa2[index2 + i - 1] * dr3
					i += 2
				}
				t1 += ido
				k++
			}
		}

		fun dradb4(
			ido: Int, l1: Int, cc: FloatArray, ch: FloatArray, wa1: FloatArray,
			index1: Int, wa2: FloatArray, index2: Int, wa3: FloatArray, index3: Int
		) {
			var i: Int
			var k: Int
			val t0: Int
			var t1: Int
			var t2: Int
			var t3: Int
			var t4: Int
			var t5: Int
			val t6: Int
			var t7: Int
			var t8: Int
			var ci2: Float
			var ci3: Float
			var ci4: Float
			var cr2: Float
			var cr3: Float
			var cr4: Float
			var ti1: Float
			var ti2: Float
			var ti3: Float
			var ti4: Float
			var tr1: Float
			var tr2: Float
			var tr3: Float
			var tr4: Float
			t0 = l1 * ido

			t1 = 0
			t2 = ido shl 2
			t3 = 0
			t6 = ido shl 1
			k = 0
			while (k < l1) {
				t4 = t3 + t6
				t5 = t1
				tr3 = cc[t4 - 1] + cc[t4 - 1]
				tr4 = cc[t4] + cc[t4]
				t4 += t6
				tr1 = cc[t3] - cc[t4 - 1]
				tr2 = cc[t3] + cc[t4 - 1]
				ch[t5] = tr2 + tr3
				t5 += t0
				ch[t5] = tr1 - tr4
				t5 += t0
				ch[t5] = tr2 - tr3
				t5 += t0
				ch[t5] = tr1 + tr4
				t1 += ido
				t3 += t2
				k++
			}

			if (ido < 2)
				return
			if (ido != 2) {
				t1 = 0
				k = 0
				while (k < l1) {
					t2 = t1 shl 2
					t3 = t2 + t6
					t4 = t3
					t5 = t4 + t6
					t7 = t1
					i = 2
					while (i < ido) {
						t2 += 2
						t3 += 2
						t4 -= 2
						t5 -= 2
						t7 += 2
						ti1 = cc[t2] + cc[t5]
						ti2 = cc[t2] - cc[t5]
						ti3 = cc[t3] - cc[t4]
						tr4 = cc[t3] + cc[t4]
						tr1 = cc[t2 - 1] - cc[t5 - 1]
						tr2 = cc[t2 - 1] + cc[t5 - 1]
						ti4 = cc[t3 - 1] - cc[t4 - 1]
						tr3 = cc[t3 - 1] + cc[t4 - 1]
						ch[t7 - 1] = tr2 + tr3
						cr3 = tr2 - tr3
						ch[t7] = ti2 + ti3
						ci3 = ti2 - ti3
						cr2 = tr1 - tr4
						cr4 = tr1 + tr4
						ci2 = ti1 + ti4
						ci4 = ti1 - ti4

						t8 = t7 + t0
						ch[t8 - 1] = wa1[index1 + i - 2] * cr2 - wa1[index1 + i - 1] * ci2
						ch[t8] = wa1[index1 + i - 2] * ci2 + wa1[index1 + i - 1] * cr2
						t8 += t0
						ch[t8 - 1] = wa2[index2 + i - 2] * cr3 - wa2[index2 + i - 1] * ci3
						ch[t8] = wa2[index2 + i - 2] * ci3 + wa2[index2 + i - 1] * cr3
						t8 += t0
						ch[t8 - 1] = wa3[index3 + i - 2] * cr4 - wa3[index3 + i - 1] * ci4
						ch[t8] = wa3[index3 + i - 2] * ci4 + wa3[index3 + i - 1] * cr4
						i += 2
					}
					t1 += ido
					k++
				}
				if (ido % 2 == 1)
					return
			}

			t1 = ido
			t2 = ido shl 2
			t3 = ido - 1
			t4 = ido + (ido shl 1)
			k = 0
			while (k < l1) {
				t5 = t3
				ti1 = cc[t1] + cc[t4]
				ti2 = cc[t4] - cc[t1]
				tr1 = cc[t1 - 1] - cc[t4 - 1]
				tr2 = cc[t1 - 1] + cc[t4 - 1]
				ch[t5] = tr2 + tr2
				t5 += t0
				ch[t5] = sqrt2 * (tr1 - ti1)
				t5 += t0
				ch[t5] = ti2 + ti2
				t5 += t0
				ch[t5] = -sqrt2 * (tr1 + ti1)

				t3 += ido
				t1 += t2
				t4 += t2
				k++
			}
		}

		fun dradbg(
			ido: Int, ip: Int, l1: Int, idl1: Int, cc: FloatArray, c1: FloatArray,
			c2: FloatArray, ch: FloatArray, ch2: FloatArray, wa: FloatArray, index: Int
		) {

			var idij: Int
			var ipph = 0
			var i: Int
			var j: Int
			var k: Int
			var l: Int
			var ik: Int
			var `is`: Int
			var t0 = 0
			var t1: Int
			var t2: Int
			var t3: Int
			var t4: Int
			var t5: Int
			var t6: Int
			var t7: Int
			var t8: Int
			var t9: Int
			var t10 = 0
			var t11: Int
			var t12: Int
			var dc2: Float
			var ai1: Float
			var ai2: Float
			var ar1: Float
			var ar2: Float
			var ds2: Float
			var nbd = 0
			var dcp = 0f
			var arg: Float
			var dsp = 0f
			var ar1h: Float
			var ar2h: Float
			var ipp2 = 0

			var state = 100

			loop@ while (true) {
				when (state) {
					100 -> {
						t10 = ip * ido
						t0 = l1 * ido
						arg = tpi / ip.toFloat()
						dcp = cos(arg.toDouble()).toFloat()
						dsp = sin(arg.toDouble()).toFloat()
						nbd = (ido - 1).ushr(1)
						ipp2 = ip
						ipph = (ip + 1).ushr(1)
						if (ido < l1) {
							state = 103
							continue@loop
						}
						t1 = 0
						t2 = 0
						k = 0
						while (k < l1) {
							t3 = t1
							t4 = t2
							i = 0
							while (i < ido) {
								ch[t3] = cc[t4]
								t3++
								t4++
								i++
							}
							t1 += ido
							t2 += t10
							k++
						}
						state = 106
					}
					103 -> {
						t1 = 0
						i = 0
						while (i < ido) {
							t2 = t1
							t3 = t1
							k = 0
							while (k < l1) {
								ch[t2] = cc[t3]
								t2 += ido
								t3 += t10
								k++
							}
							t1++
							i++
						}
						t1 = 0
						t2 = ipp2 * t0
						t5 = ido shl 1
						t7 = t5
						j = 1
						while (j < ipph) {
							t1 += t0
							t2 -= t0
							t3 = t1
							t4 = t2
							t6 = t5
							k = 0
							while (k < l1) {
								ch[t3] = cc[t6 - 1] + cc[t6 - 1]
								ch[t4] = cc[t6] + cc[t6]
								t3 += ido
								t4 += ido
								t6 += t10
								k++
							}
							t5 += t7
							j++
						}
						if (ido == 1) {
							state = 116
							continue@loop
						}
						if (nbd < l1) {
							state = 112
							continue@loop
						}

						t1 = 0
						t2 = ipp2 * t0
						t7 = 0
						j = 1
						while (j < ipph) {
							t1 += t0
							t2 -= t0
							t3 = t1
							t4 = t2

							t7 += ido shl 1
							t8 = t7
							k = 0
							while (k < l1) {
								t5 = t3
								t6 = t4
								t9 = t8
								t11 = t8
								i = 2
								while (i < ido) {
									t5 += 2
									t6 += 2
									t9 += 2
									t11 -= 2
									ch[t5 - 1] = cc[t9 - 1] + cc[t11 - 1]
									ch[t6 - 1] = cc[t9 - 1] - cc[t11 - 1]
									ch[t5] = cc[t9] - cc[t11]
									ch[t6] = cc[t9] + cc[t11]
									i += 2
								}
								t3 += ido
								t4 += ido
								t8 += t10
								k++
							}
							j++
						}
						state = 116
					}
					106 -> {
						t1 = 0
						t2 = ipp2 * t0
						t5 = ido shl 1
						t7 = t5
						j = 1
						while (j < ipph) {
							t1 += t0
							t2 -= t0
							t3 = t1
							t4 = t2
							t6 = t5
							k = 0
							while (k < l1) {
								ch[t3] = cc[t6 - 1] + cc[t6 - 1]
								ch[t4] = cc[t6] + cc[t6]
								t3 += ido
								t4 += ido
								t6 += t10
								k++
							}
							t5 += t7
							j++
						}
						if (ido == 1) {
							state = 116
							continue@loop
						}
						if (nbd < l1) {
							state = 112
							continue@loop
						}
						t1 = 0
						t2 = ipp2 * t0
						t7 = 0
						j = 1
						while (j < ipph) {
							t1 += t0
							t2 -= t0
							t3 = t1
							t4 = t2
							t7 += ido shl 1
							t8 = t7
							k = 0
							while (k < l1) {
								t5 = t3
								t6 = t4
								t9 = t8
								t11 = t8
								i = 2
								while (i < ido) {
									t5 += 2
									t6 += 2
									t9 += 2
									t11 -= 2
									ch[t5 - 1] = cc[t9 - 1] + cc[t11 - 1]
									ch[t6 - 1] = cc[t9 - 1] - cc[t11 - 1]
									ch[t5] = cc[t9] - cc[t11]
									ch[t6] = cc[t9] + cc[t11]
									i += 2
								}
								t3 += ido
								t4 += ido
								t8 += t10
								k++
							}
							j++
						}
						state = 116
					}
					112 -> {
						t1 = 0
						t2 = ipp2 * t0
						t7 = 0
						j = 1
						while (j < ipph) {
							t1 += t0
							t2 -= t0
							t3 = t1
							t4 = t2
							t7 += ido shl 1
							t8 = t7
							t9 = t7
							i = 2
							while (i < ido) {
								t3 += 2
								t4 += 2
								t8 += 2
								t9 -= 2
								t5 = t3
								t6 = t4
								t11 = t8
								t12 = t9
								k = 0
								while (k < l1) {
									ch[t5 - 1] = cc[t11 - 1] + cc[t12 - 1]
									ch[t6 - 1] = cc[t11 - 1] - cc[t12 - 1]
									ch[t5] = cc[t11] - cc[t12]
									ch[t6] = cc[t11] + cc[t12]
									t5 += ido
									t6 += ido
									t11 += t10
									t12 += t10
									k++
								}
								i += 2
							}
							j++
						}
						ar1 = 1f
						ai1 = 0f
						t1 = 0
						t2 = ipp2 * idl1
						t9 = t2
						t3 = (ip - 1) * idl1
						l = 1
						while (l < ipph) {
							t1 += idl1
							t2 -= idl1

							ar1h = dcp * ar1 - dsp * ai1
							ai1 = dcp * ai1 + dsp * ar1
							ar1 = ar1h
							t4 = t1
							t5 = t2
							t6 = 0
							t7 = idl1
							t8 = t3
							ik = 0
							while (ik < idl1) {
								c2[t4++] = ch2[t6++] + ar1 * ch2[t7++]
								c2[t5++] = ai1 * ch2[t8++]
								ik++
							}
							dc2 = ar1
							ds2 = ai1
							ar2 = ar1
							ai2 = ai1

							t6 = idl1
							t7 = t9 - idl1
							j = 2
							while (j < ipph) {
								t6 += idl1
								t7 -= idl1
								ar2h = dc2 * ar2 - ds2 * ai2
								ai2 = dc2 * ai2 + ds2 * ar2
								ar2 = ar2h
								t4 = t1
								t5 = t2
								t11 = t6
								t12 = t7
								ik = 0
								while (ik < idl1) {
									c2[t4++] += ar2 * ch2[t11++]
									c2[t5++] += ai2 * ch2[t12++]
									ik++
								}
								j++
							}
							l++
						}

						t1 = 0
						j = 1
						while (j < ipph) {
							t1 += idl1
							t2 = t1
							ik = 0
							while (ik < idl1) {
								ch2[ik] += ch2[t2++]
								ik++
							}
							j++
						}

						t1 = 0
						t2 = ipp2 * t0
						j = 1
						while (j < ipph) {
							t1 += t0
							t2 -= t0
							t3 = t1
							t4 = t2
							k = 0
							while (k < l1) {
								ch[t3] = c1[t3] - c1[t4]
								ch[t4] = c1[t3] + c1[t4]
								t3 += ido
								t4 += ido
								k++
							}
							j++
						}

						if (ido == 1) {
							state = 132
							continue@loop
						}
						if (nbd < l1) {
							state = 128
							continue@loop
						}

						t1 = 0
						t2 = ipp2 * t0
						j = 1
						while (j < ipph) {
							t1 += t0
							t2 -= t0
							t3 = t1
							t4 = t2
							k = 0
							while (k < l1) {
								t5 = t3
								t6 = t4
								i = 2
								while (i < ido) {
									t5 += 2
									t6 += 2
									ch[t5 - 1] = c1[t5 - 1] - c1[t6]
									ch[t6 - 1] = c1[t5 - 1] + c1[t6]
									ch[t5] = c1[t5] + c1[t6 - 1]
									ch[t6] = c1[t5] - c1[t6 - 1]
									i += 2
								}
								t3 += ido
								t4 += ido
								k++
							}
							j++
						}
						state = 132
					}
					116 -> {
						ar1 = 1f
						ai1 = 0f
						t1 = 0
						t2 = ipp2 * idl1
						t9 = t2
						t3 = (ip - 1) * idl1
						l = 1
						while (l < ipph) {
							t1 += idl1
							t2 -= idl1
							ar1h = dcp * ar1 - dsp * ai1
							ai1 = dcp * ai1 + dsp * ar1
							ar1 = ar1h
							t4 = t1
							t5 = t2
							t6 = 0
							t7 = idl1
							t8 = t3
							ik = 0
							while (ik < idl1) {
								c2[t4++] = ch2[t6++] + ar1 * ch2[t7++]
								c2[t5++] = ai1 * ch2[t8++]
								ik++
							}
							dc2 = ar1
							ds2 = ai1
							ar2 = ar1
							ai2 = ai1
							t6 = idl1
							t7 = t9 - idl1
							j = 2
							while (j < ipph) {
								t6 += idl1
								t7 -= idl1
								ar2h = dc2 * ar2 - ds2 * ai2
								ai2 = dc2 * ai2 + ds2 * ar2
								ar2 = ar2h
								t4 = t1
								t5 = t2
								t11 = t6
								t12 = t7
								ik = 0
								while (ik < idl1) {
									c2[t4++] += ar2 * ch2[t11++]
									c2[t5++] += ai2 * ch2[t12++]
									ik++
								}
								j++
							}
							l++
						}
						t1 = 0
						j = 1
						while (j < ipph) {
							t1 += idl1
							t2 = t1
							ik = 0
							while (ik < idl1) {
								ch2[ik] += ch2[t2++]
								ik++
							}
							j++
						}
						t1 = 0
						t2 = ipp2 * t0
						j = 1
						while (j < ipph) {
							t1 += t0
							t2 -= t0
							t3 = t1
							t4 = t2
							k = 0
							while (k < l1) {
								ch[t3] = c1[t3] - c1[t4]
								ch[t4] = c1[t3] + c1[t4]
								t3 += ido
								t4 += ido
								k++
							}
							j++
						}
						if (ido == 1) {
							state = 132
							continue@loop
						}
						if (nbd < l1) {
							state = 128
							continue@loop
						}
						t1 = 0
						t2 = ipp2 * t0
						j = 1
						while (j < ipph) {
							t1 += t0
							t2 -= t0
							t3 = t1
							t4 = t2
							k = 0
							while (k < l1) {
								t5 = t3
								t6 = t4
								i = 2
								while (i < ido) {
									t5 += 2
									t6 += 2
									ch[t5 - 1] = c1[t5 - 1] - c1[t6]
									ch[t6 - 1] = c1[t5 - 1] + c1[t6]
									ch[t5] = c1[t5] + c1[t6 - 1]
									ch[t6] = c1[t5] - c1[t6 - 1]
									i += 2
								}
								t3 += ido
								t4 += ido
								k++
							}
							j++
						}
						state = 132
					}
					128 -> {
						t1 = 0
						t2 = ipp2 * t0
						j = 1
						while (j < ipph) {
							t1 += t0
							t2 -= t0
							t3 = t1
							t4 = t2
							i = 2
							while (i < ido) {
								t3 += 2
								t4 += 2
								t5 = t3
								t6 = t4
								k = 0
								while (k < l1) {
									ch[t5 - 1] = c1[t5 - 1] - c1[t6]
									ch[t6 - 1] = c1[t5 - 1] + c1[t6]
									ch[t5] = c1[t5] + c1[t6 - 1]
									ch[t6] = c1[t5] - c1[t6 - 1]
									t5 += ido
									t6 += ido
									k++
								}
								i += 2
							}
							j++
						}
						if (ido == 1)
							return

						ik = 0
						while (ik < idl1) {
							c2[ik] = ch2[ik]
							ik++
						}

						t1 = 0
						j = 1
						while (j < ip) {
							t1 += t0
							t2 = t1
							k = 0
							while (k < l1) {
								c1[t2] = ch[t2]
								t2 += ido
								k++
							}
							j++
						}

						if (nbd > l1) {
							state = 139
							continue@loop
						}

						`is` = -ido - 1
						t1 = 0
						j = 1
						while (j < ip) {
							`is` += ido
							t1 += t0
							idij = `is`
							t2 = t1
							i = 2
							while (i < ido) {
								t2 += 2
								idij += 2
								t3 = t2
								k = 0
								while (k < l1) {
									c1[t3 - 1] = wa[index + idij - 1] * ch[t3 - 1] - wa[index + idij] * ch[t3]
									c1[t3] = wa[index + idij - 1] * ch[t3] + wa[index + idij] * ch[t3 - 1]
									t3 += ido
									k++
								}
								i += 2
							}
							j++
						}
						return
					}
					132 -> {
						if (ido == 1)
							return
						ik = 0
						while (ik < idl1) {
							c2[ik] = ch2[ik]
							ik++
						}
						t1 = 0
						j = 1
						while (j < ip) {
							t1 += t0
							t2 = t1
							k = 0
							while (k < l1) {
								c1[t2] = ch[t2]
								t2 += ido
								k++
							}
							j++
						}
						if (nbd > l1) {
							state = 139
							continue@loop
						}
						`is` = -ido - 1
						t1 = 0
						j = 1
						while (j < ip) {
							`is` += ido
							t1 += t0
							idij = `is`
							t2 = t1
							i = 2
							while (i < ido) {
								t2 += 2
								idij += 2
								t3 = t2
								k = 0
								while (k < l1) {
									c1[t3 - 1] = wa[index + idij - 1] * ch[t3 - 1] - wa[index + idij] * ch[t3]
									c1[t3] = wa[index + idij - 1] * ch[t3] + wa[index + idij] * ch[t3 - 1]
									t3 += ido
									k++
								}
								i += 2
							}
							j++
						}
						return
					}

					139 -> {
						`is` = -ido - 1
						t1 = 0
						j = 1
						while (j < ip) {
							`is` += ido
							t1 += t0
							t2 = t1
							k = 0
							while (k < l1) {
								idij = `is`
								t3 = t2
								i = 2
								while (i < ido) {
									idij += 2
									t3 += 2
									c1[t3 - 1] = wa[index + idij - 1] * ch[t3 - 1] - wa[index + idij] * ch[t3]
									c1[t3] = wa[index + idij - 1] * ch[t3] + wa[index + idij] * ch[t3 - 1]
									i += 2
								}
								t2 += ido
								k++
							}
							j++
						}
						continue@loop
					}
				}
			}
		}

		fun drftb1(n: Int, c: FloatArray, ch: FloatArray, wa: FloatArray, index: Int, ifac: IntArray) {
			var i: Int
			var k1: Int
			var l1: Int
			var l2 = 0
			var na: Int
			val nf: Int
			var ip = 0
			var iw: Int
			var ix2: Int
			var ix3: Int
			var ido = 0
			var idl1 = 0

			nf = ifac[1]
			na = 0
			l1 = 1
			iw = 1

			k1 = 0
			while (k1 < nf) {
				var state = 100
				loop@ while (true) {
					when (state) {
						100 -> {
							ip = ifac[k1 + 2]
							l2 = ip * l1
							ido = n / l2
							idl1 = ido * l1
							if (ip != 4) {
								state = 103
								continue@loop
							}
							ix2 = iw + ido
							ix3 = ix2 + ido

							if (na != 0)
								dradb4(
									ido,
									l1,
									ch,
									c,
									wa,
									index + iw - 1,
									wa,
									index + ix2 - 1,
									wa,
									index + ix3 - 1
								)
							else
								dradb4(
									ido,
									l1,
									c,
									ch,
									wa,
									index + iw - 1,
									wa,
									index + ix2 - 1,
									wa,
									index + ix3 - 1
								)
							na = 1 - na
							state = 115
						}
						103 -> {
							if (ip != 2) {
								state = 106
								continue@loop
							}

							if (na != 0)
								dradb2(
									ido,
									l1,
									ch,
									c,
									wa,
									index + iw - 1
								)
							else
								dradb2(
									ido,
									l1,
									c,
									ch,
									wa,
									index + iw - 1
								)
							na = 1 - na
							state = 115
						}

						106 -> {
							if (ip != 3) {
								state = 109
								continue@loop
							}

							ix2 = iw + ido
							if (na != 0)
								dradb3(
									ido,
									l1,
									ch,
									c,
									wa,
									index + iw - 1,
									wa,
									index + ix2 - 1
								)
							else
								dradb3(
									ido,
									l1,
									c,
									ch,
									wa,
									index + iw - 1,
									wa,
									index + ix2 - 1
								)
							na = 1 - na
							state = 115
						}
						109 -> {
							if (na != 0)
								dradbg(
									ido,
									ip,
									l1,
									idl1,
									ch,
									ch,
									ch,
									c,
									c,
									wa,
									index + iw - 1
								)
							else
								dradbg(
									ido,
									ip,
									l1,
									idl1,
									c,
									c,
									c,
									ch,
									ch,
									wa,
									index + iw - 1
								)
							if (ido == 1)
								na = 1 - na
							l1 = l2
							iw += (ip - 1) * ido
							continue@loop
						}

						115 -> {
							l1 = l2
							iw += (ip - 1) * ido
							continue@loop
						}
					}
				}
				k1++
			}
			if (na == 0)
				return
			i = 0
			while (i < n) {
				c[i] = ch[i]
				i++
			}
		}
	}
}

class DspState() {

	var analysisp: Int = 0
	var vi: Info = Info()
	var modebits: Int = 0

	var pcm: Array<FloatArray> = arrayOf()
	var pcm_storage: Int = 0
	var pcm_current: Int = 0
	var pcm_returned: Int = 0

	var multipliers: FloatArray? = null
	var envelope_storage: Int = 0
	var envelope_current: Int = 0

	var eofflag: Int = 0

	var lW: Int = 0
	var W: Int = 0
	var nW: Int = 0
	var centerW: Int = 0

	var granulepos: Long = 0
	var sequence: Long = 0

	var glue_bits: Long = 0
	var time_bits: Long = 0
	var floor_bits: Long = 0
	var res_bits: Long = 0

	// local lookup storage
	var window: Array<Array<Array<Array<FloatArray>>>> // block, leadin, leadout, type
	var transform: Array<Array<Any>>
	var fullbooks: Array<CodeBook> = arrayOf()
	// backend lookups are tied to the mode, not the backend or naked mapping
	var mode: Array<Any> = arrayOf()

	// local storage, only used on the encoding side.  This way the
	// application does not need to worry about freeing some packets'
	// memory and not others'; packet storage is always tracked.
	// Cleared next call to a _dsp_ function
	var header: ByteArray? = null
	var header1: ByteArray? = null
	var header2: ByteArray? = null

	init {
		transform = Array<Array<Any>>(2) { arrayOf() }
		window = Array<Array<Array<Array<FloatArray>>>>(2) { arrayOf() }
		window[0] = Array<Array<Array<FloatArray>>>(2) { arrayOf() }
		window[0][0] = Array<Array<FloatArray>>(2) { arrayOf() }
		window[0][1] = Array<Array<FloatArray>>(2) { arrayOf() }
		window[0][0][0] = Array<FloatArray>(2) { floatArrayOf() }
		window[0][0][1] = Array<FloatArray>(2) { floatArrayOf() }
		window[0][1][0] = Array<FloatArray>(2) { floatArrayOf() }
		window[0][1][1] = Array<FloatArray>(2) { floatArrayOf() }
		window[1] = Array<Array<Array<FloatArray>>>(2) { arrayOf() }
		window[1][0] = Array<Array<FloatArray>>(2) { arrayOf() }
		window[1][1] = Array<Array<FloatArray>>(2) { arrayOf() }
		window[1][0][0] = Array<FloatArray>(2) { floatArrayOf() }
		window[1][0][1] = Array<FloatArray>(2) { floatArrayOf() }
		window[1][1][0] = Array<FloatArray>(2) { floatArrayOf() }
		window[1][1][1] = Array<FloatArray>(2) { floatArrayOf() }
	}

	// Analysis side code, but directly related to blocking.  Thus it's
	// here and not in analysis.c (which is for analysis transforms only).
	// The init is here because some of it is shared

	fun init(vi: Info, encp: Boolean): Int {
		this.vi = vi
		modebits = Util.ilog2(vi.modes)

		transform[0] = Array<Any>(VI_TRANSFORMB) { Unit }
		transform[1] = Array<Any>(VI_TRANSFORMB) { Unit }

		// MDCT is tranform 0

		transform[0][0] = Mdct()
		transform[1][0] = Mdct()
		(transform[0][0] as Mdct).init(vi.blocksizes[0])
		(transform[1][0] as Mdct).init(vi.blocksizes[1])

		window[0][0][0] = Array<FloatArray>(VI_WINDOWB) { floatArrayOf() }
		window[0][0][1] = window[0][0][0]
		window[0][1][0] = window[0][0][0]
		window[0][1][1] = window[0][0][0]
		window[1][0][0] = Array<FloatArray>(VI_WINDOWB) { floatArrayOf() }
		window[1][0][1] = Array<FloatArray>(VI_WINDOWB) { floatArrayOf() }
		window[1][1][0] = Array<FloatArray>(VI_WINDOWB) { floatArrayOf() }
		window[1][1][1] = Array<FloatArray>(VI_WINDOWB) { floatArrayOf() }

		for (i in 0 until VI_WINDOWB) {
			window[0][0][0][i] = window(
				i,
				vi.blocksizes[0],
				vi.blocksizes[0] / 2,
				vi.blocksizes[0] / 2
			)
			window[1][0][0][i] = window(
				i,
				vi.blocksizes[1],
				vi.blocksizes[0] / 2,
				vi.blocksizes[0] / 2
			)
			window[1][0][1][i] = window(
				i,
				vi.blocksizes[1],
				vi.blocksizes[0] / 2,
				vi.blocksizes[1] / 2
			)
			window[1][1][0][i] = window(
				i,
				vi.blocksizes[1],
				vi.blocksizes[1] / 2,
				vi.blocksizes[0] / 2
			)
			window[1][1][1][i] = window(
				i,
				vi.blocksizes[1],
				vi.blocksizes[1] / 2,
				vi.blocksizes[1] / 2
			)
		}

		fullbooks = Array<CodeBook>(vi.books) { CodeBook()
            .apply { init_decode(vi.book_param[it]) } }

		// initialize the storage vectors to a decent size greater than the
		// minimum

		pcm_storage = 8192 // we'll assume later that we have
		// a minimum of twice the blocksize of
		// accumulated samples in analysis
		pcm = Array<FloatArray>(vi.channels) { floatArrayOf() }
		run {
			for (i in 0 until vi.channels) {
				pcm[i] = FloatArray(pcm_storage)
			}
		}

		// all 1 (large block) or 0 (small block)
		// explicitly set for the sake of clarity
		lW = 0 // previous window size
		W = 0 // current window size

		// all vector indexes; multiples of samples_per_envelope_step
		centerW = vi.blocksizes[1] / 2

		pcm_current = centerW

		// initialize all the mapping/backend lookups
		mode = Array<Any>(vi.modes) { Unit }
		for (i in 0 until vi.modes) {
			val mapnum = vi.mode_param[i].mapping
			val maptype = vi.map_type[mapnum]
			mode[i] = vi.mapping_P[maptype].look(this, vi.mode_param[i], vi.map_param[mapnum]!!)
		}
		return 0
	}

	fun synthesis_init(vi: Info): Int {
		init(vi, false)
		// Adjust centerW to allow an easier mechanism for determining output
		pcm_returned = centerW
		centerW -= vi.blocksizes[W] / 4 + vi.blocksizes[lW] / 4
		granulepos = -1
		sequence = -1
		return 0
	}

	constructor(vi: Info) : this() {
		init(vi, false)
		// Adjust centerW to allow an easier mechanism for determining output
		pcm_returned = centerW
		centerW -= vi.blocksizes[W] / 4 + vi.blocksizes[lW] / 4
		granulepos = -1
		sequence = -1
	}

	// Unike in analysis, the window is only partially applied for each
	// block.  The time domain envelope is not yet handled at the point of
	// calling (as it relies on the previous block).

	fun synthesis_blockin(vb: Block): Int {
		// Shift out any PCM/multipliers that we returned previously
		// centerW is currently the center of the last block added
		if (centerW > vi.blocksizes[1] / 2 && pcm_returned > 8192) {
			// don't shift too much; we need to have a minimum PCM buffer of
			// 1/2 long block

			var shiftPCM = centerW - vi.blocksizes[1] / 2
			shiftPCM = if (pcm_returned < shiftPCM) pcm_returned else shiftPCM

			pcm_current -= shiftPCM
			centerW -= shiftPCM
			pcm_returned -= shiftPCM
			if (shiftPCM != 0) {
				for (i in 0 until vi.channels) {
                    arraycopy(pcm[i], shiftPCM, pcm[i], 0, pcm_current)
				}
			}
		}

		lW = W
		W = vb.W
		nW = -1

		glue_bits += vb.glue_bits.toLong()
		time_bits += vb.time_bits.toLong()
		floor_bits += vb.floor_bits.toLong()
		res_bits += vb.res_bits.toLong()

		if (sequence + 1 != vb.sequence)
			granulepos = -1 // out of sequence; lose count

		sequence = vb.sequence

		run {
			val sizeW = vi.blocksizes[W]
			var _centerW = centerW + vi.blocksizes[lW] / 4 + sizeW / 4
			val beginW = _centerW - sizeW / 2
			val endW = beginW + sizeW
			var beginSl = 0
			var endSl = 0

			// Do we have enough PCM/mult storage for the block?
			if (endW > pcm_storage) {
				// expand the storage
				pcm_storage = endW + vi.blocksizes[1]
				for (i in 0 until vi.channels) {
					val foo = FloatArray(pcm_storage)
                    arraycopy(pcm[i], 0, foo, 0, pcm[i].size)
					pcm[i] = foo
				}
			}

			// overlap/add PCM
			when (W) {
				0 -> {
					beginSl = 0
					endSl = vi.blocksizes[0] / 2
				}
				1 -> {
					beginSl = vi.blocksizes[1] / 4 - vi.blocksizes[lW] / 4
					endSl = beginSl + vi.blocksizes[lW] / 2
				}
			}

			for (j in 0 until vi.channels) {
				val _pcm = beginW
				// the overlap/add section
				var i = beginSl
				while (i < endSl) {
					pcm[j][_pcm + i] += vb.pcm[j][i]
					i++
				}
				// the remaining section
				while (i < sizeW) {
					pcm[j][_pcm + i] = vb.pcm[j][i]
					i++
				}
			}

			// track the frame number... This is for convenience, but also
			// making sure our last packet doesn't end with added padding.  If
			// the last packet is partial, the number of samples we'll have to
			// return will be past the vb->granulepos.
			//
			// This is not foolproof!  It will be confused if we begin
			// decoding at the last page after a seek or hole.  In that case,
			// we don't have a starting point to judge where the last frame
			// is.  For this reason, vorbisfile will always try to make sure
			// it reads the last two marked pages in proper sequence

			if (granulepos == -1L) {
				granulepos = vb.granulepos
			} else {
				granulepos += (_centerW - centerW).toLong()
				if (vb.granulepos != -1L && granulepos != vb.granulepos) {
					if (granulepos > vb.granulepos && vb.eofflag != 0) {
						// partial last frame.  Strip the padding off
						_centerW -= (granulepos - vb.granulepos).toInt()
					}// else{ Shouldn't happen *unless* the bitstream is out of
					// spec.  Either way, believe the bitstream }
					granulepos = vb.granulepos
				}
			}

			// Update, cleanup

			centerW = _centerW
			pcm_current = endW
			if (vb.eofflag != 0)
				eofflag = 1
		}
		return 0
	}

	// pcm==NULL indicates we just want the pending samples, no more
	fun synthesis_pcmout(_pcm: Array<Array<FloatArray>>?, index: IntArray): Int {
		if (pcm_returned < centerW) {
			if (_pcm != null) {
				for (i in 0 until vi.channels) {
					index[i] = pcm_returned
				}
				_pcm[0] = pcm
			}
			return centerW - pcm_returned
		}
		return 0
	}

	fun synthesis_read(bytes: Int): Int {
		if (bytes != 0 && pcm_returned + bytes > centerW)
			return -1
		pcm_returned += bytes
		return 0
	}

	fun clear() {}

	companion object {
		val M_PI = 3.1415926539f
		val VI_TRANSFORMB = 1
		val VI_WINDOWB = 1

		fun window(type: Int, window: Int, left: Int, right: Int): FloatArray {
			val ret = FloatArray(window)
			when (type) {
				0 ->
					// The 'vorbis window' (window 0) is sin(sin(x)*sin(x)*2pi)
				{
					val leftbegin = window / 4 - left / 2
					val rightbegin = window - window / 4 - right / 2

					for (i in 0 until left) {
						var x = ((i + .5) / left * M_PI / 2.0).toFloat()
						x = sin(x.toDouble()).toFloat()
						x *= x
						x *= (M_PI / 2.0).toFloat()
						x = sin(x.toDouble()).toFloat()
						ret[i + leftbegin] = x
					}

					for (i in leftbegin + left until rightbegin) {
						ret[i] = 1f
					}

					for (i in 0 until right) {
						var x = ((right.toDouble() - i.toDouble() - .5) / right * M_PI / 2.0).toFloat()
						x = sin(x.toDouble()).toFloat()
						x *= x
						x *= (M_PI / 2.0).toFloat()
						x = sin(x.toDouble()).toFloat()
						ret[i + rightbegin] = x
					}
				}
				else ->
					//free(ret);
					//return null
					invalidOp("type != 0")
			}
			return ret
		}
	}
}

class Floor0 : FuncFloor() {
	override fun pack(i: Any, opb: Buffer) {
		val info = i as InfoFloor0
		opb.write(info.order, 8)
		opb.write(info.rate, 16)
		opb.write(info.barkmap, 16)
		opb.write(info.ampbits, 6)
		opb.write(info.ampdB, 8)
		opb.write(info.numbooks - 1, 4)
		for (j in 0 until info.numbooks) {
			opb.write(info.books[j], 8)
		}
	}

	override fun unpack(vi: Info, opb: Buffer): Any? {
		val info = InfoFloor0()
		info.order = opb.read(8)
		info.rate = opb.read(16)
		info.barkmap = opb.read(16)
		info.ampbits = opb.read(6)
		info.ampdB = opb.read(8)
		info.numbooks = opb.read(4) + 1

		if (info.order < 1 || info.rate < 1 || info.barkmap < 1 || info.numbooks < 1) {
			return null
		}

		for (j in 0 until info.numbooks) {
			info.books[j] = opb.read(8)
			if (info.books[j] < 0 || info.books[j] >= vi.books) {
				return null
			}
		}
		return info
	}

	override fun look(vd: DspState, mi: InfoMode, i: Any): Any {
		val scale: Float
		val vi = vd.vi
		val info = i as InfoFloor0
		val look = LookFloor0()
		look.m = info.order
		look.n = vi.blocksizes[mi.blockflag] / 2
		look.ln = info.barkmap
		look.vi = info
		look.lpclook.init(look.ln, look.m)

		// we choose a scaling constant so that:
		scale = look.ln / toBARK((info.rate / 2.0).toFloat())

		// the mapping from a linear scale to a smaller bark scale is
		// straightforward.  We do *not* make sure that the linear mapping
		// does not skip bark-scale bins; the decoder simply skips them and
		// the encoder may do what it wishes in filling them.  They're
		// necessary in some mapping combinations to keep the scale spacing
		// accurate
		look.linearmap = IntArray(look.n)
		for (j in 0 until look.n) {
			var `val` =
				floor((toBARK((info.rate / 2.0 / look.n * j).toFloat()) * scale).toDouble()).toInt() // bark numbers represent band edges
			if (`val` >= look.ln) {
				`val` = look.ln // guard against the approximation
			}
			look.linearmap[j] = `val`
		}
		return look
	}

	fun state(i: Any): Any {
		val state = EchstateFloor0()
		val info = i as InfoFloor0

		// a safe size if usually too big (dim==1)
		state.codewords = IntArray(info.order)
		state.curve = FloatArray(info.barkmap)
		state.frameno = -1
		return state
	}

	override fun free_info(i: Any) {}

	override fun free_look(i: Any) {}

	override fun free_state(vs: Any) {}

	override fun forward(vb: Block, i: Any, `in`: FloatArray, out: FloatArray, vs: Any): Int {
		return 0
	}

	var lsp: FloatArray = floatArrayOf()
	private val lock = Lock()

	fun inverse(vb: Block, i: Any, out: FloatArray): Int {
		//System.err.println("Floor0.inverse "+i.getClass()+"]");
		val look = i as LookFloor0
		val info = look.vi
		val ampraw = vb.opb.read(info!!.ampbits)
		if (ampraw > 0) { // also handles the -1 out of data case
			val maxval = (1 shl info.ampbits) - 1
			val amp = ampraw.toFloat() / maxval * info.ampdB
			val booknum = vb.opb.read(Util.ilog(info.numbooks))

			if (booknum != -1 && booknum < info.numbooks) {

				return lock {
					if (lsp.size < look.m) {
						lsp = FloatArray(look.m)
					} else {
						lsp.fill(0f, 0, look.m)
					}

					val b = vb.vd.fullbooks[info.books[booknum]]
					var last = 0f

					for (j in 0 until look.m) {
						out[j] = 0.0f
					}

					run {
						var j = 0
						while (j < look.m) {
							if (b.decodevs(lsp, j, vb.opb, 1, -1) == -1) {
								for (k in 0 until look.n) {
									out[k] = 0.0f
								}
								return@lock 0
							}
							j += b.dim
						}
					}
					var j = 0
					while (j < look.m) {
						var k = 0
						while (k < b.dim) {
							lsp[j] += last
							k++
							j++
						}
						last = lsp[j - 1]
					}
					// take the coefficients back to a spectral envelope curve
					Lsp.lsp_to_curve(
						out,
						look.linearmap,
						look.n,
						look.ln,
						lsp,
						look.m,
						amp,
						info.ampdB.toFloat()
					)
					return@lock 1
				}
			}
		}
		return 0
	}

	override fun inverse1(vb: Block, i: Any, memo: Any?): Any? {
		val look = i as LookFloor0
		val info = look.vi
		var lsp: FloatArray? = null
		if (memo is FloatArray) {
			lsp = memo
		}

		val ampraw = vb.opb.read(info!!.ampbits)
		if (ampraw > 0) { // also handles the -1 out of data case
			val maxval = (1 shl info.ampbits) - 1
			val amp = ampraw.toFloat() / maxval * info.ampdB
			val booknum = vb.opb.read(Util.ilog(info.numbooks))

			if (booknum != -1 && booknum < info.numbooks) {
				val b = vb.vd.fullbooks[info.books[booknum]]
				var last = 0f

				if (lsp == null || lsp.size < look.m + 1) {
					lsp = FloatArray(look.m + 1)
				} else {
					for (j in lsp.indices) {
						lsp[j] = 0f
					}
				}

				run {
					var j = 0
					while (j < look.m) {
						if (b.decodev_set(lsp, j, vb.opb, b.dim) == -1) {
							return null
						}
						j += b.dim
					}
				}

				var j = 0
				while (j < look.m) {
					var k = 0
					while (k < b.dim) {
						lsp[j] += last
						k++
						j++
					}
					last = lsp[j - 1]
				}
				lsp[look.m] = amp
				return lsp
			}
		}
		return null
	}

	override fun inverse2(vb: Block, i: Any, memo: Any?, out: FloatArray): Int {
		val look = i as LookFloor0
		val info = look.vi

		if (memo != null) {
			val lsp = memo as FloatArray?
			val amp = lsp!![look.m]

			Lsp.lsp_to_curve(
				out,
				look.linearmap,
				look.n,
				look.ln,
				lsp,
				look.m,
				amp,
				info!!.ampdB.toFloat()
			)
			return 1
		}
		for (j in 0 until look.n) {
			out[j] = 0f
		}
		return 0
	}

	inner class InfoFloor0 {
		var order: Int = 0
		var rate: Int = 0
		var barkmap: Int = 0

		var ampbits: Int = 0
		var ampdB: Int = 0

		var numbooks: Int = 0 // <= 16
		var books = IntArray(16)
	}

	inner class LookFloor0 {
		var n: Int = 0
		var ln: Int = 0
		var m: Int = 0
		var linearmap: IntArray = intArrayOf()

		var vi: InfoFloor0? = null
		var lpclook = Lpc()
	}

	inner class EchstateFloor0 {
		var codewords: IntArray? = null
		var curve: FloatArray? = null
		var frameno: Long = 0
		var codes: Long = 0
	}

	companion object {
		fun toBARK(f: Float): Float =
			(13.1 * atan(.00074 * f) + 2.24 * atan(f.toDouble() * f.toDouble() * 1.85e-8) + 1e-4 * f).toFloat()
	}
}

class Floor1 : FuncFloor() {

	override fun pack(i: Any, opb: Buffer) {
		val info = i as InfoFloor1

		var count = 0
		val rangebits: Int
		val maxposit = info.postlist[1]
		var maxclass = -1

		/* save out partitions */
		opb.write(info.partitions, 5) /* only 0 to 31 legal */
		for (j in 0 until info.partitions) {
			opb.write(info.partitionclass[j], 4) /* only 0 to 15 legal */
			if (maxclass < info.partitionclass[j])
				maxclass = info.partitionclass[j]
		}

		/* save out partition classes */
		for (j in 0 until maxclass + 1) {
			opb.write(info.class_dim[j] - 1, 3) /* 1 to 8 */
			opb.write(info.class_subs[j], 2) /* 0 to 3 */
			if (info.class_subs[j] != 0) {
				opb.write(info.class_book[j], 8)
			}
			for (k in 0 until (1 shl info.class_subs[j])) {
				opb.write(info.class_subbook[j][k] + 1, 8)
			}
		}

		/* save out the post list */
		opb.write(info.mult - 1, 2) /* only 1,2,3,4 legal now */
		opb.write(Util.ilog2(maxposit), 4)
		rangebits = Util.ilog2(maxposit)

		var j = 0
		var k = 0
		while (j < info.partitions) {
			count += info.class_dim[info.partitionclass[j]]
			while (k < count) {
				opb.write(info.postlist[k + 2], rangebits)
				k++
			}
			j++
		}
	}

	override fun unpack(vi: Info, opb: Buffer): Any? {
		var count = 0
		var maxclass = -1
		val rangebits: Int
		val info = InfoFloor1()

		/* read partitions */
		info.partitions = opb.read(5) /* only 0 to 31 legal */
		for (j in 0 until info.partitions) {
			info.partitionclass[j] = opb.read(4) /* only 0 to 15 legal */
			if (maxclass < info.partitionclass[j])
				maxclass = info.partitionclass[j]
		}

		/* read partition classes */
		for (j in 0 until maxclass + 1) {
			info.class_dim[j] = opb.read(3) + 1 /* 1 to 8 */
			info.class_subs[j] = opb.read(2) /* 0,1,2,3 bits */
			if (info.class_subs[j] < 0) {
				info.free()
				return null
			}
			if (info.class_subs[j] != 0) {
				info.class_book[j] = opb.read(8)
			}
			if (info.class_book[j] < 0 || info.class_book[j] >= vi.books) {
				info.free()
				return null
			}
			for (k in 0 until (1 shl info.class_subs[j])) {
				info.class_subbook[j][k] = opb.read(8) - 1
				if (info.class_subbook[j][k] < -1 || info.class_subbook[j][k] >= vi.books) {
					info.free()
					return null
				}
			}
		}

		/* read the post list */
		info.mult = opb.read(2) + 1 /* only 1,2,3,4 legal now */
		rangebits = opb.read(4)

		var j = 0
		var k = 0
		while (j < info.partitions) {
			count += info.class_dim[info.partitionclass[j]]
			while (k < count) {
				val t = opb.read(rangebits)
				info.postlist[k + 2] = t
				if (t < 0 || t >= 1 shl rangebits) {
					info.free()
					return null
				}
				k++
			}
			j++
		}
		info.postlist[0] = 0
		info.postlist[1] = 1 shl rangebits

		return info
	}

	override fun look(vd: DspState, mi: InfoMode, i: Any): Any {
		var _n = 0

		val sortpointer = IntArray(VIF_POSIT + 2)

		//    Info vi=vd.vi;

		val info = i as InfoFloor1
		val look = LookFloor1()
		look.vi = info
		look.n = info.postlist!![1]

		/* we drop each position value in-between already decoded values,
	 and use linear interpolation to predict each new value past the
	 edges.  The positions are read in the order of the position
	 list... we precompute the bounding positions in the lookup.  Of
	 course, the neighbors can change (if a position is declined), but
	 this is an initial mapping */

		for (j in 0 until info.partitions) {
			_n += info.class_dim[info.partitionclass[j]]
		}
		_n += 2
		look.posts = _n

		/* also store a sorted position index */
		for (j in 0 until _n) {
			sortpointer[j] = j
		}
		//    qsort(sortpointer,n,sizeof(int),icomp); // !!

		var foo: Int
		for (j in 0 until _n - 1) {
			for (k in j until _n) {
				if (info.postlist[sortpointer[j]] > info.postlist[sortpointer[k]]) {
					foo = sortpointer[k]
					sortpointer[k] = sortpointer[j]
					sortpointer[j] = foo
				}
			}
		}

		/* points from sort order back to range number */
		for (j in 0 until _n) {
			look.forward_index[j] = sortpointer[j]
		}
		/* points from range order to sorted position */
		for (j in 0 until _n) {
			look.reverse_index[look.forward_index[j]] = j
		}
		/* we actually need the post values too */
		for (j in 0 until _n) {
			look.sorted_index[j] = info.postlist[look.forward_index[j]]
		}

		/* quantize values to multiplier spec */
		when (info.mult) {
			1 /* 1024 -> 256 */ -> look.quant_q = 256
			2 /* 1024 -> 128 */ -> look.quant_q = 128
			3 /* 1024 -> 86 */ -> look.quant_q = 86
			4 /* 1024 -> 64 */ -> look.quant_q = 64
			else -> look.quant_q = -1
		}

		/* discover our neighbors for decode where we don't use fit flags
	   (that would push the neighbors outward) */
		for (j in 0 until _n - 2) {
			var lo = 0
			var hi = 1
			var lx = 0
			var hx = look.n
			val currentx = info.postlist[j + 2]
			for (k in 0 until j + 2) {
				val x = info.postlist[k]
				if (x in (lx + 1) until currentx) {
					lo = k
					lx = x
				}
				if (x in (currentx + 1) until hx) {
					hi = k
					hx = x
				}
			}
			look.loneighbor[j] = lo
			look.hineighbor[j] = hi
		}

		return look
	}

	override fun free_info(i: Any) {}

	override fun free_look(i: Any) {}

	override fun free_state(vs: Any) {}

	override fun forward(vb: Block, i: Any, `in`: FloatArray, out: FloatArray, vs: Any): Int {
		return 0
	}

	override fun inverse1(vb: Block, ii: Any, memo: Any?): Any? {
		val look = ii as LookFloor1
		val info = look.vi
		val books = vb.vd.fullbooks

		/* unpack wrapped/predicted values from stream */
		if (vb.opb.read(1) == 1) {
			var fit_value: IntArray = intArrayOf()
			if (memo is IntArray) {
				fit_value = memo
			}
			if (fit_value.size < look.posts) {
				fit_value = IntArray(look.posts)
			} else {
				fit_value.fill(0)
			}

			fit_value[0] = vb.opb.read(Util.ilog(look.quant_q - 1))
			fit_value[1] = vb.opb.read(Util.ilog(look.quant_q - 1))

			/* partition by partition */
			run {
				var i = 0
				var j = 2
				while (i < info!!.partitions) {
					val clss = info.partitionclass[i]
					val cdim = info.class_dim[clss]
					val csubbits = info.class_subs[clss]
					val csub = 1 shl csubbits
					var cval = 0

					/* decode the partition's first stage cascade value */
					if (csubbits != 0) {
						cval = books[info.class_book[clss]].decode(vb.opb)

						if (cval == -1) {
							return null
						}
					}

					for (k in 0 until cdim) {
						val book = info.class_subbook[clss][cval and csub - 1]
						cval = cval ushr csubbits
						if (book >= 0) {
							fit_value[j + k] = books[book].decode(vb.opb)
							if (fit_value[j + k] == -1) {
								return null
							}
						} else {
							fit_value[j + k] = 0
						}
					}
					j += cdim
					i++
				}
			}

			/* unwrap positive values and reconsitute via linear interpolation */
			for (i in 2 until look.posts) {
				val predicted = render_point(
					info!!.postlist[look.loneighbor[i - 2]],
					info.postlist[look.hineighbor[i - 2]],
					fit_value[look.loneighbor[i - 2]], fit_value[look.hineighbor[i - 2]],
					info.postlist[i]
				)
				val hiroom = look.quant_q - predicted
				val loroom = predicted
				val room = (if (hiroom < loroom) hiroom else loroom) shl 1
				var `val` = fit_value[i]

				if (`val` != 0) {
					if (`val` >= room) {
						if (hiroom > loroom) {
							`val` -= loroom
						} else {
							`val` = -1 - (`val` - hiroom)
						}
					} else {
						if (`val` and 1 != 0) {
							`val` = -(`val` + 1).ushr(1)
						} else {
							`val` = `val` shr 1
						}
					}

					fit_value[i] = `val` + predicted
					fit_value[look.loneighbor[i - 2]] = fit_value[look.loneighbor[i - 2]] and 0x7fff
					fit_value[look.hineighbor[i - 2]] = fit_value[look.hineighbor[i - 2]] and 0x7fff
				} else {
					fit_value[i] = predicted or 0x8000
				}
			}
			return fit_value
		}

		return null
	}

	override fun inverse2(vb: Block, i: Any, memo: Any?, out: FloatArray): Int {
		val look = i as LookFloor1
		val info = look.vi
		val n = vb.vd.vi.blocksizes[vb.mode] / 2

		if (memo != null) {
			/* render the lines */
			val fit_value = memo as IntArray?
			var hx = 0
			var lx = 0
			var ly = fit_value!![0] * info!!.mult
			for (j in 1 until look.posts) {
				val current = look.forward_index!![j]
				var hy = fit_value[current] and 0x7fff
				if (hy == fit_value[current]) {
					hy *= info.mult
					hx = info.postlist!![current]

					render_line(lx, hx, ly, hy, out)

					lx = hx
					ly = hy
				}
			}
			for (j in hx until n) {
				out[j] *= out[j - 1] /* be certain */
			}
			return 1
		}
		for (j in 0 until n) {
			out[j] = 0f
		}
		return 0
	}

	class InfoFloor1 {

		var partitions: Int = 0 /* 0 to 31 */
		var partitionclass: IntArray = IntArray(VIF_PARTS) /* 0 to 15 */
		var class_dim: IntArray = IntArray(VIF_CLASS) /* 1 to 8 */
		var class_subs: IntArray = IntArray(VIF_CLASS) /* 0,1,2,3 (bits: 1<<n poss) */
		var class_book: IntArray = IntArray(VIF_CLASS) /* subs ^ dim entries */
		var class_subbook: Array<IntArray> = Array(VIF_CLASS) { intArrayOf() } /* [VIF_CLASS][subs] */

		var mult: Int = 0 /* 1 2 3 or 4 */
		var postlist: IntArray = IntArray(VIF_POSIT + 2) /* first two implicit */

		/* encode side analysis parameters */
		var maxover: Float = 0.toFloat()
		var maxunder: Float = 0.toFloat()
		var maxerr: Float = 0.toFloat()

		var twofitminsize: Int = 0
		var twofitminused: Int = 0
		var twofitweight: Int = 0
		var twofitatten: Float = 0.toFloat()
		var unusedminsize: Int = 0
		var unusedmin_n: Int = 0

		var n: Int = 0

		init {
			for (i in class_subbook.indices) {
				class_subbook[i] = IntArray(8)
			}
		}

		fun free() {
			partitionclass = intArrayOf()
			class_dim = intArrayOf()
			class_subs = intArrayOf()
			class_book = intArrayOf()
			class_subbook = arrayOf()
			postlist = intArrayOf()
		}

		fun copy_info(): Any {
			val info = this
			val ret = InfoFloor1()

			ret.partitions = info.partitions
            arraycopy(info.partitionclass, 0, ret.partitionclass, 0,
				VIF_PARTS
			)
            arraycopy(info.class_dim, 0, ret.class_dim, 0,
				VIF_CLASS
			)
            arraycopy(info.class_subs, 0, ret.class_subs, 0,
				VIF_CLASS
			)
            arraycopy(info.class_book, 0, ret.class_book, 0,
				VIF_CLASS
			)

			for (j in 0 until VIF_CLASS) {
                arraycopy(info.class_subbook[j], 0, ret.class_subbook[j], 0, 8)
			}

			ret.mult = info.mult
            arraycopy(info.postlist, 0, ret.postlist, 0, VIF_POSIT + 2)

			ret.maxover = info.maxover
			ret.maxunder = info.maxunder
			ret.maxerr = info.maxerr

			ret.twofitminsize = info.twofitminsize
			ret.twofitminused = info.twofitminused
			ret.twofitweight = info.twofitweight
			ret.twofitatten = info.twofitatten
			ret.unusedminsize = info.unusedminsize
			ret.unusedmin_n = info.unusedmin_n

			ret.n = info.n

			return ret
		}

		companion object {
			val VIF_POSIT = 63
			val VIF_CLASS = 16
			val VIF_PARTS = 31
		}

	}

	class LookFloor1 {

		var sorted_index: IntArray = IntArray(VIF_POSIT + 2)
		var forward_index: IntArray = IntArray(VIF_POSIT + 2)
		var reverse_index: IntArray = IntArray(VIF_POSIT + 2)
		var hineighbor: IntArray = IntArray(VIF_POSIT)
		var loneighbor: IntArray = IntArray(VIF_POSIT)
		var posts: Int = 0

		var n: Int = 0
		var quant_q: Int = 0
		var vi: InfoFloor1? = null

		var phrasebits: Int = 0
		var postbits: Int = 0
		var frames: Int = 0

		fun free() {
			sorted_index = intArrayOf()
			forward_index = intArrayOf()
			reverse_index = intArrayOf()
			hineighbor = intArrayOf()
			loneighbor = intArrayOf()
		}

		companion object {
			val VIF_POSIT = 63
		}
	}

	class Lsfit_acc {
		var x0: Long = 0
		var x1: Long = 0

		var xa: Long = 0
		var ya: Long = 0
		var x2a: Long = 0
		var y2a: Long = 0
		var xya: Long = 0
		var n: Long = 0
		var an: Long = 0
		var un: Long = 0
		var edgey0: Long = 0
		var edgey1: Long = 0
	}

	class EchstateFloor1 {
		var codewords: IntArray? = null
		var curve: FloatArray? = null
		var frameno: Long = 0
		var codes: Long = 0
	}

	companion object {
		val floor1_rangedb = 140
		val VIF_POSIT = 63

		private fun render_point(x0: Int, x1: Int, y0: Int, y1: Int, x: Int): Int {
			var y0 = y0
			var y1 = y1
			y0 = y0 and 0x7fff /* mask off flag */
			y1 = y1 and 0x7fff

			run {
				val dy = y1 - y0
				val adx = x1 - x0
				val ady = abs(dy)
				val err = ady * (x - x0)

				val off = (err / adx).toInt()
				if (dy < 0)
					return y0 - off
				return y0 + off
			}
		}

		private val FLOOR_fromdB_LOOKUP = floatArrayOf(
			1.0649863e-07f,
			1.1341951e-07f,
			1.2079015e-07f,
			1.2863978e-07f,
			1.3699951e-07f,
			1.4590251e-07f,
			1.5538408e-07f,
			1.6548181e-07f,
			1.7623575e-07f,
			1.8768855e-07f,
			1.9988561e-07f,
			2.128753e-07f,
			2.2670913e-07f,
			2.4144197e-07f,
			2.5713223e-07f,
			2.7384213e-07f,
			2.9163793e-07f,
			3.1059021e-07f,
			3.3077411e-07f,
			3.5226968e-07f,
			3.7516214e-07f,
			3.9954229e-07f,
			4.2550680e-07f,
			4.5315863e-07f,
			4.8260743e-07f,
			5.1396998e-07f,
			5.4737065e-07f,
			5.8294187e-07f,
			6.2082472e-07f,
			6.6116941e-07f,
			7.0413592e-07f,
			7.4989464e-07f,
			7.9862701e-07f,
			8.5052630e-07f,
			9.0579828e-07f,
			9.6466216e-07f,
			1.0273513e-06f,
			1.0941144e-06f,
			1.1652161e-06f,
			1.2409384e-06f,
			1.3215816e-06f,
			1.4074654e-06f,
			1.4989305e-06f,
			1.5963394e-06f,
			1.7000785e-06f,
			1.8105592e-06f,
			1.9282195e-06f,
			2.0535261e-06f,
			2.1869758e-06f,
			2.3290978e-06f,
			2.4804557e-06f,
			2.6416497e-06f,
			2.8133190e-06f,
			2.9961443e-06f,
			3.1908506e-06f,
			3.3982101e-06f,
			3.6190449e-06f,
			3.8542308e-06f,
			4.1047004e-06f,
			4.3714470e-06f,
			4.6555282e-06f,
			4.9580707e-06f,
			5.2802740e-06f,
			5.6234160e-06f,
			5.9888572e-06f,
			6.3780469e-06f,
			6.7925283e-06f,
			7.2339451e-06f,
			7.7040476e-06f,
			8.2047000e-06f,
			8.7378876e-06f,
			9.3057248e-06f,
			9.9104632e-06f,
			1.0554501e-05f,
			1.1240392e-05f,
			1.1970856e-05f,
			1.2748789e-05f,
			1.3577278e-05f,
			1.4459606e-05f,
			1.5399272e-05f,
			1.6400004e-05f,
			1.7465768e-05f,
			1.8600792e-05f,
			1.9809576e-05f,
			2.1096914e-05f,
			2.2467911e-05f,
			2.3928002e-05f,
			2.5482978e-05f,
			2.7139006e-05f,
			2.8902651e-05f,
			3.0780908e-05f,
			3.2781225e-05f,
			3.4911534e-05f,
			3.7180282e-05f,
			3.9596466e-05f,
			4.2169667e-05f,
			4.4910090e-05f,
			4.7828601e-05f,
			5.0936773e-05f,
			5.4246931e-05f,
			5.7772202e-05f,
			6.1526565e-05f,
			6.5524908e-05f,
			6.9783085e-05f,
			7.4317983e-05f,
			7.9147585e-05f,
			8.4291040e-05f,
			8.9768747e-05f,
			9.5602426e-05f,
			0.00010181521f,
			0.00010843174f,
			0.00011547824f,
			0.00012298267f,
			0.00013097477f,
			0.00013948625f,
			0.00014855085f,
			0.00015820453f,
			0.00016848555f,
			0.00017943469f,
			0.00019109536f,
			0.00020351382f,
			0.00021673929f,
			0.00023082423f,
			0.00024582449f,
			0.00026179955f,
			0.00027881276f,
			0.00029693158f,
			0.00031622787f,
			0.00033677814f,
			0.00035866388f,
			0.00038197188f,
			0.00040679456f,
			0.00043323036f,
			0.00046138411f,
			0.00049136745f,
			0.00052329927f,
			0.00055730621f,
			0.00059352311f,
			0.00063209358f,
			0.00067317058f,
			0.00071691700f,
			0.00076350630f,
			0.00081312324f,
			0.00086596457f,
			0.00092223983f,
			0.00098217216f,
			0.0010459992f,
			0.0011139742f,
			0.0011863665f,
			0.0012634633f,
			0.0013455702f,
			0.0014330129f,
			0.0015261382f,
			0.0016253153f,
			0.0017309374f,
			0.0018434235f,
			0.0019632195f,
			0.0020908006f,
			0.0022266726f,
			0.0023713743f,
			0.0025254795f,
			0.0026895994f,
			0.0028643847f,
			0.0030505286f,
			0.0032487691f,
			0.0034598925f,
			0.0036847358f,
			0.0039241906f,
			0.0041792066f,
			0.0044507950f,
			0.0047400328f,
			0.0050480668f,
			0.0053761186f,
			0.0057254891f,
			0.0060975636f,
			0.0064938176f,
			0.0069158225f,
			0.0073652516f,
			0.0078438871f,
			0.0083536271f,
			0.0088964928f,
			0.009474637f,
			0.010090352f,
			0.010746080f,
			0.011444421f,
			0.012188144f,
			0.012980198f,
			0.013823725f,
			0.014722068f,
			0.015678791f,
			0.016697687f,
			0.017782797f,
			0.018938423f,
			0.020169149f,
			0.021479854f,
			0.022875735f,
			0.024362330f,
			0.025945531f,
			0.027631618f,
			0.029427276f,
			0.031339626f,
			0.033376252f,
			0.035545228f,
			0.037855157f,
			0.040315199f,
			0.042935108f,
			0.045725273f,
			0.048696758f,
			0.051861348f,
			0.055231591f,
			0.058820850f,
			0.062643361f,
			0.066714279f,
			0.071049749f,
			0.075666962f,
			0.080584227f,
			0.085821044f,
			0.091398179f,
			0.097337747f,
			0.10366330f,
			0.11039993f,
			0.11757434f,
			0.12521498f,
			0.13335215f,
			0.14201813f,
			0.15124727f,
			0.16107617f,
			0.17154380f,
			0.18269168f,
			0.19456402f,
			0.20720788f,
			0.22067342f,
			0.23501402f,
			0.25028656f,
			0.26655159f,
			0.28387361f,
			0.30232132f,
			0.32196786f,
			0.34289114f,
			0.36517414f,
			0.38890521f,
			0.41417847f,
			0.44109412f,
			0.46975890f,
			0.50028648f,
			0.53279791f,
			0.56742212f,
			0.60429640f,
			0.64356699f,
			0.68538959f,
			0.72993007f,
			0.77736504f,
			0.82788260f,
			0.88168307f,
			0.9389798f,
			1f
		)

		private fun render_line(x0: Int, x1: Int, y0: Int, y1: Int, d: FloatArray) {
			val dy = y1 - y0
			val adx = x1 - x0
			var ady = abs(dy)
			val base = dy / adx
			val sy = if (dy < 0) base - 1 else base + 1
			var x = x0
			var y = y0
			var err = 0

			ady -= abs(base * adx)

			d[x] *= FLOOR_fromdB_LOOKUP[y]
			while (++x < x1) {
				err = err + ady
				if (err >= adx) {
					err -= adx
					y += sy
				} else {
					y += base
				}
				d[x] *= FLOOR_fromdB_LOOKUP[y]
			}
		}
	}
}

abstract class FuncFloor {

	abstract fun pack(i: Any, opb: Buffer)

	abstract fun unpack(vi: Info, opb: Buffer): Any?

	abstract fun look(vd: DspState, mi: InfoMode, i: Any): Any

	abstract fun free_info(i: Any)

	abstract fun free_look(i: Any)

	abstract fun free_state(vs: Any)

	abstract fun forward(vb: Block, i: Any, `in`: FloatArray, out: FloatArray, vs: Any): Int

	abstract fun inverse1(vb: Block, i: Any, memo: Any?): Any?

	abstract fun inverse2(vb: Block, i: Any, memo: Any?, out: FloatArray): Int

	companion object {

		var floor_P = arrayOf(
			Floor0(),
			Floor1()
        )
	}
}

abstract class FuncMapping {

	abstract fun pack(info: Info, imap: Any, buffer: Buffer)

	abstract fun unpack(info: Info, buffer: Buffer): Any?

	abstract fun look(vd: DspState, vm: InfoMode, m: Any): Any

	abstract fun free_info(imap: Any)

	abstract fun free_look(imap: Any)

	abstract fun inverse(vd: Block, lm: Any): Int
}

abstract class FuncResidue {

	abstract fun pack(vr: Any, opb: Buffer)

	abstract fun unpack(vi: Info, opb: Buffer): Any?

	abstract fun look(vd: DspState, vm: InfoMode, vr: Any): Any

	abstract fun free_info(i: Any)

	abstract fun free_look(i: Any)

	abstract fun inverse(vb: Block, vl: Any, `in`: Array<FloatArray>, nonzero: IntArray, ch: Int): Int
}

abstract class FuncTime {

	abstract fun pack(i: Any, opb: Buffer)

	abstract fun unpack(vi: Info, opb: Buffer): Any

	abstract fun look(vd: DspState, vm: InfoMode, i: Any): Any

	abstract fun free_info(i: Any)

	abstract fun free_look(i: Any)

	abstract fun inverse(vb: Block, i: Any, `in`: FloatArray, out: FloatArray): Int
}

class Info {
	var time_P = arrayOf<FuncTime>(Time0())
	val mapping_P = arrayOf<FuncMapping>(Mapping0())
	var residue_P = arrayOf<FuncResidue>(
		Residue0(),
		Residue1(),
		Residue2()
    )

	var version: Int = 0
	var channels: Int = 0
	var rate: Int = 0

	// The below bitrate declarations are *hints*.
	// Combinations of the three values carry the following implications:
	//
	// all three set to the same value:
	// implies a fixed rate bitstream
	// only nominal set:
	// implies a VBR stream that averages the nominal bitrate.  No hard
	// upper/lower limit
	// upper and or lower set:
	// implies a VBR bitstream that obeys the bitrate limits. nominal
	// may also be set to give a nominal rate.
	// none set:
	//  the coder does not care to speculate.

	var bitrate_upper: Int = 0
	var bitrate_nominal: Int = 0
	var bitrate_lower: Int = 0

	// Vorbis supports only short and long blocks, but allows the
	// encoder to choose the sizes

	var blocksizes = IntArray(2)

	// modes are the primary means of supporting on-the-fly different
	// blocksizes, different channel mappings (LR or mid-side),
	// different residue backends, etc.  Each mode consists of a
	// blocksize flag and a mapping (along with the mapping setup

	var modes: Int = 0
	var maps: Int = 0
	var times: Int = 0
	var floors: Int = 0
	var residues: Int = 0
	var books: Int = 0
	var psys: Int = 0 // encode only

	var mode_param: Array<InfoMode> = arrayOf()

	var map_type: IntArray = intArrayOf()
	var map_param: Array<Any?> = arrayOf()

	var time_type: IntArray = intArrayOf()
	var time_param: Array<Any> = arrayOf()

	var floor_type: IntArray = intArrayOf()
	var floor_param: Array<Any?> = arrayOf()

	var residue_type: IntArray = intArrayOf()
	var residue_param: Array<Any?> = arrayOf()

	var book_param: Array<StaticCodeBook> = arrayOf()

	var psy_param = Array<PsyInfo>(64) { PsyInfo() } // encode only

	// for block long/sort tuning; encode only
	var envelopesa: Int = 0
	var preecho_thresh: Float = 0.toFloat()
	var preecho_clamp: Float = 0.toFloat()

	// used by synthesis, which has a full, alloced vi
	fun init() {
		rate = 0
	}

	fun clear() {
		for (i in 0 until modes) {
			mode_param[i] = InfoMode()
		}
		mode_param = arrayOf()

		for (i in 0 until maps) { // unpack does the range checking
			mapping_P[map_type[i]].free_info(map_param[i]!!)
		}
		map_param = arrayOf()

		for (i in 0 until times) { // unpack does the range checking
			time_P[time_type[i]].free_info(time_param[i])
		}
		time_param = arrayOf()

		for (i in 0 until floors) { // unpack does the range checking
			FuncFloor.floor_P[floor_type[i]].free_info(floor_param[i]!!)
		}
		floor_param = arrayOf()

		for (i in 0 until residues) { // unpack does the range checking
			residue_P[residue_type[i]].free_info(residue_param[i]!!)
		}
		residue_param = arrayOf()

		// the static codebooks *are* freed if you call info_clear, because
		// decode side does alloc a 'static' codebook. Calling clear on the
		// full codebook does not clear the static codebook (that's our
		// responsibility)
		for (i in 0 until books) {
			// just in case the decoder pre-cleared to save space
			book_param[i].clear()
			book_param[i] = StaticCodeBook()
		}
		//if(vi->book_param)free(vi->book_param);
		book_param = arrayOf()

		for (i in 0 until psys) {
			psy_param[i].free()
		}

	}

	// Header packing/unpacking
	fun unpack_info(opb: Buffer): Int {
		version = opb.read(32)
		if (version != 0)
			return -1

		channels = opb.read(8)
		rate = opb.read(32)

		bitrate_upper = opb.read(32)
		bitrate_nominal = opb.read(32)
		bitrate_lower = opb.read(32)

		blocksizes[0] = 1 shl opb.read(4)
		blocksizes[1] = 1 shl opb.read(4)

		if (rate < 1 || channels < 1 || blocksizes[0] < 8 || blocksizes[1] < blocksizes[0]
			|| opb.read(1) != 1
		) {
			clear()
			return -1
		}
		return 0
	}

	// all of the real encoding details are here.  The modes, books,
	// everything
	fun unpack_books(opb: Buffer): Int {

		books = opb.read(8) + 1

		if (book_param.size != books)
			book_param = Array(books) { StaticCodeBook() }
		for (i in 0 until books) {
			book_param[i] = StaticCodeBook()
			if (book_param[i].unpack(opb) != 0) {
				clear()
				return -1
			}
		}

		// time backend settings
		times = opb.read(6) + 1
		if (time_type.size != times)
			time_type = IntArray(times)
		if (time_param.size != times)
			time_param = Array<Any>(times) { Unit }
		for (i in 0 until times) {
			time_type[i] = opb.read(16)
			if (time_type[i] < 0 || time_type[i] >= VI_TIMEB) {
				clear()
				return -1
			}
			time_param[i] = time_P[time_type[i]].unpack(this, opb)
		}

		// floor backend settings
		floors = opb.read(6) + 1
		if (floor_type.size != floors)
			floor_type = IntArray(floors)
		if (floor_param.size != floors)
			floor_param = Array<Any?>(floors) { Unit }

		for (i in 0 until floors) {
			floor_type[i] = opb.read(16)
			if (floor_type[i] < 0 || floor_type[i] >= VI_FLOORB) {
				clear()
				return -1
			}

			floor_param[i] = FuncFloor.floor_P[floor_type[i]].unpack(this, opb)
			if (floor_param[i] == null) {
				clear()
				return -1
			}
		}

		// residue backend settings
		residues = opb.read(6) + 1

		if (residue_type.size != residues)
			residue_type = IntArray(residues)

		if (residue_param.size != residues)
			residue_param = Array<Any?>(residues) { Unit }

		for (i in 0 until residues) {
			residue_type[i] = opb.read(16)
			if (residue_type[i] < 0 || residue_type[i] >= VI_RESB) {
				clear()
				return -1
			}
			residue_param[i] = residue_P[residue_type[i]].unpack(this, opb)
			if (residue_param[i] == null) {
				clear()
				return -1
			}
		}

		// map backend settings
		maps = opb.read(6) + 1
		if (map_type.size != maps)
			map_type = IntArray(maps)
		if (map_param.size != maps)
			map_param = Array<Any?>(maps) { Unit }
		for (i in 0 until maps) {
			map_type[i] = opb.read(16)
			if (map_type[i] < 0 || map_type[i] >= VI_MAPB) {
				clear()
				return -1
			}
			map_param[i] = mapping_P[map_type[i]].unpack(this, opb)
			if (map_param[i] == null) {
				clear()
				return -1
			}
		}

		// mode settings
		modes = opb.read(6) + 1
		if (mode_param.size != modes)
			mode_param = Array<InfoMode>(modes) { InfoMode() }
		for (i in 0 until modes) {
			mode_param[i] = InfoMode()
			mode_param[i].blockflag = opb.read(1)
			mode_param[i].windowtype = opb.read(16)
			mode_param[i].transformtype = opb.read(16)
			mode_param[i].mapping = opb.read(8)

			if (mode_param[i].windowtype >= VI_WINDOWB
				|| mode_param[i].transformtype >= VI_WINDOWB
				|| mode_param[i].mapping >= maps
			) {
				clear()
				return -1
			}
		}

		if (opb.read(1) != 1) {
			clear()
			return -1
		}

		return 0
	}

	// The Vorbis header is in three packets; the initial small packet in
	// the first page that identifies basic parameters, a second packet
	// with bitstream comments and a third packet that holds the
	// codebook.

	fun synthesis_headerin(vc: Comment, op: Packet?): Int {
		val opb = Buffer()

		if (op != null) {
			opb.readinit(op.packet_base, op.packet, op.bytes)

			// Which of the three types of header is this?
			// Also verify header-ness, vorbis
			run {
				val buffer = ByteArray(6)
				val packtype = opb.read(8)
				opb.read(buffer, 6)
				if (buffer[0] != 'v'.toByte() || buffer[1] != 'o'.toByte() || buffer[2] != 'r'.toByte() || buffer[3] != 'b'.toByte() || buffer[4] != 'i'.toByte() || buffer[5] != 's'.toByte()) {
					// not a vorbis header
					return -1
				}
				when (packtype) {
					0x01 // least significant *bit* is read first
					-> {
						if (op.b_o_s == 0) {
							// Not the initial packet
							return -1
						}
						if (rate != 0) {
							// previously initialized info header
							return -1
						}
						return unpack_info(opb)
					}
					0x03 // least significant *bit* is read first
					-> {
						if (rate == 0) {
							// um... we didn't get the initial header
							return -1
						}
						return vc.unpack(opb)
					}
					0x05 // least significant *bit* is read first
					-> {
						if (rate == 0) {
							// um... we didn;t get the initial header or comments yet
							return -1
						}
						return unpack_books(opb)
					}
					else -> {
					}
				}// Not a valid vorbis header type
				//return(-1);
			}
		}
		return -1
	}

	// pack side
	fun pack_info(opb: Buffer): Int {
		// preamble
		opb.write(0x01, 8)
		opb.write(_vorbis)

		// basic information about the stream
		opb.write(0x00, 32)
		opb.write(channels, 8)
		opb.write(rate, 32)

		opb.write(bitrate_upper, 32)
		opb.write(bitrate_nominal, 32)
		opb.write(bitrate_lower, 32)

		opb.write(Util.ilog2(blocksizes[0]), 4)
		opb.write(Util.ilog2(blocksizes[1]), 4)
		opb.write(1, 1)
		return 0
	}

	fun pack_books(opb: Buffer): Int {
		opb.write(0x05, 8)
		opb.write(_vorbis)

		// books
		opb.write(books - 1, 8)
		for (i in 0 until books) {
			if (book_param[i].pack(opb) != 0) {
				//goto err_out;
				return -1
			}
		}

		// times
		opb.write(times - 1, 6)
		for (i in 0 until times) {
			opb.write(time_type[i], 16)
			time_P[time_type[i]].pack(this.time_param[i], opb)
		}

		// floors
		opb.write(floors - 1, 6)
		for (i in 0 until floors) {
			opb.write(floor_type[i], 16)
			FuncFloor.floor_P[floor_type[i]].pack(floor_param[i]!!, opb)
		}

		// residues
		opb.write(residues - 1, 6)
		for (i in 0 until residues) {
			opb.write(residue_type[i], 16)
			residue_P[residue_type[i]].pack(residue_param[i]!!, opb)
		}

		// maps
		opb.write(maps - 1, 6)
		for (i in 0 until maps) {
			opb.write(map_type[i], 16)
			mapping_P[map_type[i]].pack(this, map_param[i]!!, opb)
		}

		// modes
		opb.write(modes - 1, 6)
		for (i in 0 until modes) {
			opb.write(mode_param[i].blockflag, 1)
			opb.write(mode_param[i].windowtype, 16)
			opb.write(mode_param[i].transformtype, 16)
			opb.write(mode_param[i].mapping, 8)
		}
		opb.write(1, 1)
		return 0
	}

	fun blocksize(op: Packet): Int {
		//codec_setup_info
		val opb = Buffer()

		var mode: Int = 0

		opb.readinit(op.packet_base, op.packet, op.bytes)

		/* Check the packet type */
		if (opb.read(1) != 0) {
			/* Oops.  This is not an audio data packet */
			return OV_ENOTAUDIO
		}
		run {
			var modebits = 0
			var v = modes
			while (v > 1) {
				modebits++
				v = v ushr 1
			}

			/* read our mode and pre/post windowsize */
			mode = opb.read(modebits)
		}
		if (mode == -1)
			return OV_EBADPACKET
		return blocksizes[mode_param[mode].blockflag]
	}

	override fun toString(): String {
		return "version:$version, channels:$channels, rate:$rate, bitrate:$bitrate_upper,$bitrate_nominal,$bitrate_lower"
	}

	companion object {
		private val OV_EBADPACKET = -136
		private val OV_ENOTAUDIO = -135

		private val _vorbis = "vorbis".toByteArray(UTF8)
		private val VI_TIMEB = 1
		//  private static final int VI_FLOORB=1;
		private val VI_FLOORB = 2
		//  private static final int VI_RESB=1;
		private val VI_RESB = 3
		private val VI_MAPB = 1
		private val VI_WINDOWB = 1
	}
}

class InfoMode {
	var blockflag: Int = 0
	var windowtype: Int = 0
	var transformtype: Int = 0
	var mapping: Int = 0
}

class JOrbisException(s: String = "") : Exception("JOrbis: $s")
internal object Lookup {
	val COS_LOOKUP_SZ = 128
	val COS_LOOKUP = floatArrayOf(
		+1.0000000000000f,
		+0.9996988186962f,
		+0.9987954562052f,
		+0.9972904566787f,
		+0.9951847266722f,
		+0.9924795345987f,
		+0.9891765099648f,
		+0.9852776423889f,
		+0.9807852804032f,
		+0.9757021300385f,
		+0.9700312531945f,
		+0.9637760657954f,
		+0.9569403357322f,
		+0.9495281805930f,
		+0.9415440651830f,
		+0.9329927988347f,
		+0.9238795325113f,
		+0.9142097557035f,
		+0.9039892931234f,
		+0.8932243011955f,
		+0.8819212643484f,
		+0.8700869911087f,
		+0.8577286100003f,
		+0.8448535652497f,
		+0.8314696123025f,
		+0.8175848131516f,
		+0.8032075314806f,
		+0.7883464276266f,
		+0.7730104533627f,
		+0.7572088465065f,
		+0.7409511253550f,
		+0.7242470829515f,
		+0.7071067811865f,
		+0.6895405447371f,
		+0.6715589548470f,
		+0.6531728429538f,
		+0.6343932841636f,
		+0.6152315905806f,
		+0.5956993044924f,
		+0.5758081914178f,
		+0.5555702330196f,
		+0.5349976198871f,
		+0.5141027441932f,
		+0.4928981922298f,
		+0.4713967368260f,
		+0.4496113296546f,
		+0.4275550934303f,
		+0.4052413140050f,
		+0.3826834323651f,
		+0.3598950365350f,
		+0.3368898533922f,
		+0.3136817403989f,
		+0.2902846772545f,
		+0.2667127574749f,
		+0.2429801799033f,
		+0.2191012401569f,
		+0.1950903220161f,
		+0.1709618887603f,
		+0.1467304744554f,
		+0.1224106751992f,
		+0.0980171403296f,
		+0.0735645635997f,
		+0.0490676743274f,
		+0.0245412285229f,
		+0.0000000000000f,
		-0.0245412285229f,
		-0.0490676743274f,
		-0.0735645635997f,
		-0.0980171403296f,
		-0.1224106751992f,
		-0.1467304744554f,
		-0.1709618887603f,
		-0.1950903220161f,
		-0.2191012401569f,
		-0.2429801799033f,
		-0.2667127574749f,
		-0.2902846772545f,
		-0.3136817403989f,
		-0.3368898533922f,
		-0.3598950365350f,
		-0.3826834323651f,
		-0.4052413140050f,
		-0.4275550934303f,
		-0.4496113296546f,
		-0.4713967368260f,
		-0.4928981922298f,
		-0.5141027441932f,
		-0.5349976198871f,
		-0.5555702330196f,
		-0.5758081914178f,
		-0.5956993044924f,
		-0.6152315905806f,
		-0.6343932841636f,
		-0.6531728429538f,
		-0.6715589548470f,
		-0.6895405447371f,
		-0.7071067811865f,
		-0.7242470829515f,
		-0.7409511253550f,
		-0.7572088465065f,
		-0.7730104533627f,
		-0.7883464276266f,
		-0.8032075314806f,
		-0.8175848131516f,
		-0.8314696123025f,
		-0.8448535652497f,
		-0.8577286100003f,
		-0.8700869911087f,
		-0.8819212643484f,
		-0.8932243011955f,
		-0.9039892931234f,
		-0.9142097557035f,
		-0.9238795325113f,
		-0.9329927988347f,
		-0.9415440651830f,
		-0.9495281805930f,
		-0.9569403357322f,
		-0.9637760657954f,
		-0.9700312531945f,
		-0.9757021300385f,
		-0.9807852804032f,
		-0.9852776423889f,
		-0.9891765099648f,
		-0.9924795345987f,
		-0.9951847266722f,
		-0.9972904566787f,
		-0.9987954562052f,
		-0.9996988186962f,
		-1.0000000000000f
	)

	/* interpolated lookup based cos function, domain 0 to PI only */
	fun coslook(a: Float): Float {
		val d = a * (.31830989 * COS_LOOKUP_SZ.toFloat())
		val i = d.toInt()
		return COS_LOOKUP[i] + (d - i).toFloat() * (COS_LOOKUP[i + 1] - COS_LOOKUP[i])
	}

	val INVSQ_LOOKUP_SZ = 32
	val INVSQ_LOOKUP = floatArrayOf(
		1.414213562373f,
		1.392621247646f,
		1.371988681140f,
		1.352246807566f,
		1.333333333333f,
		1.315191898443f,
		1.297771369046f,
		1.281025230441f,
		1.264911064067f,
		1.249390095109f,
		1.234426799697f,
		1.219988562661f,
		1.206045378311f,
		1.192569588000f,
		1.179535649239f,
		1.166919931983f,
		1.154700538379f,
		1.142857142857f,
		1.131370849898f,
		1.120224067222f,
		1.109400392450f,
		1.098884511590f,
		1.088662107904f,
		1.078719779941f,
		1.069044967650f,
		1.059625885652f,
		1.050451462878f,
		1.041511287847f,
		1.032795558989f,
		1.024295039463f,
		1.016001016002f,
		1.007905261358f,
		1.000000000000f
	)

	/* interpolated 1./sqrt(p) where .5 <= p < 1. */
	fun invsqlook(a: Float): Float {
		val d = (a * (2f * INVSQ_LOOKUP_SZ.toFloat()) - INVSQ_LOOKUP_SZ.toFloat()).toDouble()
		val i = d.toInt()
		return INVSQ_LOOKUP[i] + (d - i).toFloat() * (INVSQ_LOOKUP[i + 1] - INVSQ_LOOKUP[i])
	}

	val INVSQ2EXP_LOOKUP_MIN = -32
	val INVSQ2EXP_LOOKUP_MAX = 32
	val INVSQ2EXP_LOOKUP = floatArrayOf(
		65536f,
		46340.95001f,
		32768f,
		23170.47501f,
		16384f,
		11585.2375f,
		8192f,
		5792.618751f,
		4096f,
		2896.309376f,
		2048f,
		1448.154688f,
		1024f,
		724.0773439f,
		512f,
		362.038672f,
		256f,
		181.019336f,
		128f,
		90.50966799f,
		64f,
		45.254834f,
		32f,
		22.627417f,
		16f,
		11.3137085f,
		8f,
		5.656854249f,
		4f,
		2.828427125f,
		2f,
		1.414213562f,
		1f,
		0.7071067812f,
		0.5f,
		0.3535533906f,
		0.25f,
		0.1767766953f,
		0.125f,
		0.08838834765f,
		0.0625f,
		0.04419417382f,
		0.03125f,
		0.02209708691f,
		0.015625f,
		0.01104854346f,
		0.0078125f,
		0.005524271728f,
		0.00390625f,
		0.002762135864f,
		0.001953125f,
		0.001381067932f,
		0.0009765625f,
		0.000690533966f,
		0.00048828125f,
		0.000345266983f,
		0.000244140625f,
		0.0001726334915f,
		0.0001220703125f,
		8.631674575e-05f,
		6.103515625e-05f,
		4.315837288e-05f,
		3.051757812e-05f,
		2.157918644e-05f,
		1.525878906e-05f
	)

	/* interpolated 1./sqrt(p) where .5 <= p < 1. */
	fun invsq2explook(a: Int): Float {
		return INVSQ2EXP_LOOKUP[a - INVSQ2EXP_LOOKUP_MIN]
	}

	val FROMdB_LOOKUP_SZ = 35
	val FROMdB2_LOOKUP_SZ = 32
	val FROMdB_SHIFT = 5
	val FROMdB2_SHIFT = 3
	val FROMdB2_MASK = 31
	val FROMdB_LOOKUP = floatArrayOf(
		1f,
		0.6309573445f,
		0.3981071706f,
		0.2511886432f,
		0.1584893192f,
		0.1f,
		0.06309573445f,
		0.03981071706f,
		0.02511886432f,
		0.01584893192f,
		0.01f,
		0.006309573445f,
		0.003981071706f,
		0.002511886432f,
		0.001584893192f,
		0.001f,
		0.0006309573445f,
		0.0003981071706f,
		0.0002511886432f,
		0.0001584893192f,
		0.0001f,
		6.309573445e-05f,
		3.981071706e-05f,
		2.511886432e-05f,
		1.584893192e-05f,
		1e-05f,
		6.309573445e-06f,
		3.981071706e-06f,
		2.511886432e-06f,
		1.584893192e-06f,
		1e-06f,
		6.309573445e-07f,
		3.981071706e-07f,
		2.511886432e-07f,
		1.584893192e-07f
	)
	val FROMdB2_LOOKUP = floatArrayOf(
		0.9928302478f,
		0.9786445908f,
		0.9646616199f,
		0.9508784391f,
		0.9372921937f,
		0.92390007f,
		0.9106992942f,
		0.8976871324f,
		0.8848608897f,
		0.8722179097f,
		0.8597555737f,
		0.8474713009f,
		0.835362547f,
		0.8234268041f,
		0.8116616003f,
		0.8000644989f,
		0.7886330981f,
		0.7773650302f,
		0.7662579617f,
		0.755309592f,
		0.7445176537f,
		0.7338799116f,
		0.7233941627f,
		0.7130582353f,
		0.7028699885f,
		0.6928273125f,
		0.6829281272f,
		0.6731703824f,
		0.6635520573f,
		0.6540711597f,
		0.6447257262f,
		0.6355138211f
	)

	/* interpolated lookup based fromdB function, domain -140dB to 0dB only */
	fun fromdBlook(a: Float): Float {
		val i = (a * (-(1 shl FROMdB2_SHIFT)).toFloat()).toInt()
		return if (i < 0)
			1f
		else
			if (i >= FROMdB_LOOKUP_SZ shl FROMdB_SHIFT)
				0f
			else
				FROMdB_LOOKUP[i.ushr(FROMdB_SHIFT)] * FROMdB2_LOOKUP[i and FROMdB2_MASK]
	}

}

class Lpc {
	// en/decode lookups
	var fft = Drft()

	var ln: Int = 0
	var m: Int = 0

	// Input : n element envelope spectral curve
	// Output: m lpc coefficients, excitation energy

	fun lpc_from_curve(curve: FloatArray, lpc: FloatArray): Float {
		var n = ln
		val work = FloatArray(n + n)
		val fscale = (.5 / n).toFloat()
		var i: Int
		var j: Int

		// input is a real curve. make it complex-real
		// This mixes phase, but the LPC generation doesn't care.
		i = 0
		while (i < n) {
			work[i * 2] = curve[i] * fscale
			work[i * 2 + 1] = 0f
			i++
		}
		work[n * 2 - 1] = curve[n - 1] * fscale

		n *= 2
		fft.backward(work)

		// The autocorrelation will not be circular.  Shift, else we lose
		// most of the power in the edges.

		i = 0
		j = n / 2
		while (i < n / 2) {
			val temp = work[i]
			work[i++] = work[j]
			work[j++] = temp
		}

		return lpc_from_data(work, lpc, n, m)
	}

	fun init(mapped: Int, m: Int) {
		ln = mapped
		this.m = m

		// we cheat decoding the LPC spectrum via FFTs
		fft.init(mapped * 2)
	}

	fun clear() {
		fft.clear()
	}

	// One can do this the long way by generating the transfer function in
	// the time domain and taking the forward FFT of the result.  The
	// results from direct calculation are cleaner and faster.
	//
	// This version does a linear curve generation and then later
	// interpolates the log curve from the linear curve.

	fun lpc_to_curve(curve: FloatArray, lpc: FloatArray, amp: Float) {

		for (i in 0 until ln * 2) {
			curve[i] = 0.0f
		}

		if (amp == 0f) {
			return
		}

		for (i in 0 until m) {
			curve[i * 2 + 1] = lpc[i] / (4 * amp)
			curve[i * 2 + 2] = -lpc[i] / (4 * amp)
		}

		fft.backward(curve)

		run {
			val l2 = ln * 2
			val unit = (1.0 / amp).toFloat()
			curve[0] = (1.0 / (curve[0] * 2 + unit)).toFloat()
			for (i in 1 until ln) {
				val real = curve[i] + curve[l2 - i]
				val imag = curve[i] - curve[l2 - i]

				val a = real + unit
				curve[i] = (1.0 / FAST_HYPOT(a, imag)).toFloat()
			}
		}
	}

	companion object {

		// Autocorrelation LPC coeff generation algorithm invented by
		// N. Levinson in 1947, modified by J. Durbin in 1959.

		// Input : n elements of time doamin data
		// Output: m lpc coefficients, excitation energy

		fun lpc_from_data(data: FloatArray, lpc: FloatArray, n: Int, m: Int): Float {
			val aut = FloatArray(m + 1)
			var error: Float
			var i: Int
			var j: Int

			// autocorrelation, p+1 lag coefficients

			j = m + 1
			while (j-- != 0) {
				var d = 0f
				i = j
				while (i < n) {
					d += data[i] * data[i - j]
					i++
				}
				aut[j] = d
			}

			// Generate lpc coefficients from autocorr values

			error = aut[0]
			/*
    if(error==0){
      for(int k=0; k<m; k++) lpc[k]=0.0f;
      return 0;
    }
    */

			i = 0
			while (i < m) {
				var r = -aut[i + 1]

				if (error == 0f) {
					for (k in 0 until m) {
						lpc[k] = 0.0f
					}
					return 0f
				}

				// Sum up this iteration's reflection coefficient; note that in
				// Vorbis we don't save it.  If anyone wants to recycle this code
				// and needs reflection coefficients, save the results of 'r' from
				// each iteration.

				j = 0
				while (j < i) {
					r -= lpc[j] * aut[i - j]
					j++
				}
				r /= error

				// Update LPC coefficients and total error

				lpc[i] = r
				j = 0
				while (j < i / 2) {
					val tmp = lpc[j]
					lpc[j] += r * lpc[i - 1 - j]
					lpc[i - 1 - j] += r * tmp
					j++
				}
				if (i % 2 != 0) {
					lpc[j] += lpc[j] * r
				}

				error *= (1.0 - r * r).toFloat()
				i++
			}

			// we need the error value to know how big an impulse to hit the
			// filter with later

			return error
		}

		fun FAST_HYPOT(a: Float, b: Float): Float {
			return sqrt((a * a + b * b).toDouble()).toFloat()
		}
	}
}

internal object Lsp {

	val M_PI = 3.1415926539.toFloat()

	fun lsp_to_curve(
		curve: FloatArray,
		map: IntArray,
		n: Int,
		ln: Int,
		lsp: FloatArray,
		m: Int,
		amp: Float,
		ampoffset: Float
	) {
		var i: Int
		val wdel = M_PI / ln
		i = 0
		while (i < m) {
			lsp[i] = Lookup.coslook(lsp[i])
			i++
		}
		val m2 = m / 2 * 2

		i = 0
		while (i < n) {
			val k = map[i]
			var p = .7071067812f
			var q = .7071067812f
			val w = Lookup.coslook(wdel * k)

			var j = 0
			while (j < m2) {
				q *= lsp[j] - w
				p *= lsp[j + 1] - w
				j += 2
			}

			if (m and 1 != 0) {
				/* odd order filter; slightly assymetric */
				/* the last coefficient */
				q *= lsp[m - 1] - w
				q *= q
				p *= p * (1f - w * w)
			} else {
				/* even order filter; still symmetric */
				q *= q * (1f + w)
				p *= p * (1f - w)
			}

			//  q=frexp(p+q,&qexp);
			q = p + q
			var hx = q.toBits()
			var ix = 0x7fffffff and hx
			var qexp = 0

			if (ix >= 0x7f800000 || ix == 0) {
				// 0,inf,nan
			} else {
				if (ix < 0x00800000) { // subnormal
					q *= 3.3554432000e+07f // 0x4c000000
					hx = q.toBits()
					ix = 0x7fffffff and hx
					qexp = -25
				}
				qexp += ix.ushr(23) - 126
				hx = hx and 0x807fffff.toInt() or 0x3f000000
				q = Float.fromBits(hx)
			}

			q = Lookup.fromdBlook(
				amp * Lookup.invsqlook(q) * Lookup.invsq2explook(
					qexp + m
				) - ampoffset
			)

			do {
				curve[i++] *= q
			} while (i < n && map[i] == k)

		}
	}
}

internal class Mapping0 : FuncMapping() {

	override fun free_info(imap: Any) {}

	override fun free_look(imap: Any) {}

	override fun look(vd: DspState, vm: InfoMode, m: Any): Any {
		//System.err.println("Mapping0.look");
		val vi = vd.vi
		val look = LookMapping0()
		val info = m as InfoMapping0
		look.map = info
		look.mode = vm

		look.time_look = Array<Any>(info.submaps) { Unit }
		look.floor_look = Array<Any>(info.submaps) { Unit }
		look.residue_look = Array<Any>(info.submaps) { Unit }

		look.time_func = Array<FuncTime>(info.submaps) { Time0() }
		look.floor_func = Array<FuncFloor>(info.submaps) { Floor0() }
		look.residue_func = Array<FuncResidue>(info.submaps) { Residue0() }

		for (i in 0 until info.submaps) {
			val timenum = info.timesubmap[i]
			val floornum = info.floorsubmap[i]
			val resnum = info.residuesubmap[i]

			look.time_func[i] = vi.time_P[vi.time_type[timenum]]
			look.time_look[i] = look.time_func[i].look(vd, vm, vi.time_param[timenum])
			look.floor_func[i] = FuncFloor.floor_P[vi.floor_type[floornum]]
			look.floor_look[i] = look.floor_func[i].look(vd, vm, vi.floor_param[floornum]!!)
			look.residue_func[i] = vi.residue_P[vi.residue_type[resnum]]
			look.residue_look[i] = look.residue_func[i].look(vd, vm, vi.residue_param[resnum]!!)

		}

		if (vi.psys != 0 && vd.analysisp != 0) {
			// ??
		}

		look.ch = vi.channels

		return look
	}

	override fun pack(vi: Info, imap: Any, opb: Buffer) {
		val info = imap as InfoMapping0

		if (info.submaps > 1) {
			opb.write(1, 1)
			opb.write(info.submaps - 1, 4)
		} else {
			opb.write(0, 1)
		}

		if (info.coupling_steps > 0) {
			opb.write(1, 1)
			opb.write(info.coupling_steps - 1, 8)
			for (i in 0 until info.coupling_steps) {
				opb.write(info.coupling_mag[i], Util.ilog2(vi.channels))
				opb.write(info.coupling_ang[i], Util.ilog2(vi.channels))
			}
		} else {
			opb.write(0, 1)
		}

		opb.write(0, 2) /* 2,3:reserved */

		/* we don't write the channel submappings if we only have one... */
		if (info.submaps > 1) {
			for (i in 0 until vi.channels)
				opb.write(info.chmuxlist[i], 4)
		}
		for (i in 0 until info.submaps) {
			opb.write(info.timesubmap[i], 8)
			opb.write(info.floorsubmap[i], 8)
			opb.write(info.residuesubmap[i], 8)
		}
	}

	// also responsible for range checking
	override fun unpack(vi: Info, opb: Buffer): Any? {
		val info = InfoMapping0()

		if (opb.read(1) != 0) {
			info.submaps = opb.read(4) + 1
		} else {
			info.submaps = 1
		}

		if (opb.read(1) != 0) {
			info.coupling_steps = opb.read(8) + 1

			for (i in 0 until info.coupling_steps) {
				val testM = opb.read(Util.ilog2(vi.channels))
				info.coupling_mag[i] = testM
				val testA = opb.read(Util.ilog2(vi.channels))
				info.coupling_ang[i] = testA

				if (testM < 0 || testA < 0 || testM == testA || testM >= vi.channels || testA >= vi.channels) {
					//goto err_out;
					info.free()
					return null
				}
			}
		}

		if (opb.read(2) > 0) { /* 2,3:reserved */
			info.free()
			return null
		}

		if (info.submaps > 1) {
			for (i in 0 until vi.channels) {
				info.chmuxlist[i] = opb.read(4)
				if (info.chmuxlist[i] >= info.submaps) {
					info.free()
					return null
				}
			}
		}

		for (i in 0 until info.submaps) {
			info.timesubmap[i] = opb.read(8)
			if (info.timesubmap[i] >= vi.times) {
				info.free()
				return null
			}
			info.floorsubmap[i] = opb.read(8)
			if (info.floorsubmap[i] >= vi.floors) {
				info.free()
				return null
			}
			info.residuesubmap[i] = opb.read(8)
			if (info.residuesubmap[i] >= vi.residues) {
				info.free()
				return null
			}
		}
		return info
	}

	var pcmbundle: Array<FloatArray> = arrayOf()
	var zerobundle: IntArray = intArrayOf()
	var nonzero: IntArray = intArrayOf()
	var floormemo: Array<Any?> = arrayOf()

	@kotlin.jvm.Synchronized
	override fun inverse(vb: Block, l: Any): Int {
		val vd = vb.vd
		val vi = vd.vi
		val look = l as LookMapping0
		val info = look.map
		val mode = look.mode
		val n = vi.blocksizes[vb.W]
		vb.pcmend = n

		val window = vd.window[vb.W][vb.lW][vb.nW][mode.windowtype]
		if (pcmbundle == null || pcmbundle.size < vi.channels) {
			pcmbundle = Array<FloatArray>(vi.channels) { floatArrayOf() }
			nonzero = IntArray(vi.channels)
			zerobundle = IntArray(vi.channels)
			floormemo = Array<Any?>(vi.channels) { Unit }
		}

		// time domain information decode (note that applying the
		// information would have to happen later; we'll probably add a
		// function entry to the harness for that later
		// NOT IMPLEMENTED

		// recover the spectral envelope; store it in the PCM vector for now
		for (i in 0 until vi.channels) {
			val pcm = vb.pcm[i]
			val submap = info.chmuxlist[i]

			floormemo[i] = look.floor_func[submap].inverse1(vb, look.floor_look[submap], floormemo[i])
			if (floormemo[i] != null) {
				nonzero[i] = 1
			} else {
				nonzero[i] = 0
			}
			for (j in 0 until n / 2) {
				pcm[j] = 0f
			}

		}

		for (i in 0 until info.coupling_steps) {
			if (nonzero[info.coupling_mag[i]] != 0 || nonzero[info.coupling_ang[i]] != 0) {
				nonzero[info.coupling_mag[i]] = 1
				nonzero[info.coupling_ang[i]] = 1
			}
		}

		// recover the residue, apply directly to the spectral envelope

		for (i in 0 until info.submaps) {
			var ch_in_bundle = 0
			for (j in 0 until vi.channels) {
				if (info.chmuxlist[j] == i) {
					if (nonzero[j] != 0) {
						zerobundle[ch_in_bundle] = 1
					} else {
						zerobundle[ch_in_bundle] = 0
					}
					pcmbundle[ch_in_bundle++] = vb.pcm[j]
				}
			}

			look.residue_func[i].inverse(vb, look.residue_look[i], pcmbundle, zerobundle, ch_in_bundle)
		}

		for (i in info.coupling_steps - 1 downTo 0) {
			val pcmM = vb.pcm[info.coupling_mag[i]]
			val pcmA = vb.pcm[info.coupling_ang[i]]

			for (j in 0 until n / 2) {
				val mag = pcmM[j]
				val ang = pcmA[j]

				if (mag > 0) {
					if (ang > 0) {
						pcmM[j] = mag
						pcmA[j] = mag - ang
					} else {
						pcmA[j] = mag
						pcmM[j] = mag + ang
					}
				} else {
					if (ang > 0) {
						pcmM[j] = mag
						pcmA[j] = mag + ang
					} else {
						pcmA[j] = mag
						pcmM[j] = mag - ang
					}
				}
			}
		}

		//    /* compute and apply spectral envelope */

		for (i in 0 until vi.channels) {
			val pcm = vb.pcm[i]
			val submap = info.chmuxlist[i]
			look.floor_func[submap].inverse2(
				vb, look.floor_look[submap],
				floormemo[i], pcm
			)
		}

		// transform the PCM data; takes PCM vector, vb; modifies PCM vector
		// only MDCT right now....

		for (i in 0 until vi.channels) {
			val pcm = vb.pcm[i]
			//_analysis_output("out",seq+i,pcm,n/2,0,0);
			(vd.transform[vb.W][0] as Mdct).backward(pcm, pcm)
		}

		// now apply the decoded pre-window time information
		// NOT IMPLEMENTED

		// window the data
		for (i in 0 until vi.channels) {
			val pcm = vb.pcm[i]
			if (nonzero[i] != 0) {
				for (j in 0 until n) {
					pcm[j] *= window[j]
				}
			} else {
				for (j in 0 until n) {
					pcm[j] = 0f
				}
			}
		}

		// now apply the decoded post-window time information
		// NOT IMPLEMENTED
		// all done!
		return 0
	}

	internal inner class InfoMapping0 {
		var submaps: Int = 0 // <= 16
		var chmuxlist: IntArray = IntArray(256) // up to 256 channels in a Vorbis stream

		var timesubmap: IntArray = IntArray(16) // [mux]
		var floorsubmap: IntArray = IntArray(16) // [mux] submap to floors
		var residuesubmap: IntArray = IntArray(16)// [mux] submap to residue
		var psysubmap: IntArray = IntArray(16) // [mux]; encode only

		var coupling_steps: Int = 0
		var coupling_mag: IntArray = IntArray(256)
		var coupling_ang: IntArray = IntArray(256)

		fun free() {
			chmuxlist = intArrayOf()
			timesubmap = intArrayOf()
			floorsubmap = intArrayOf()
			residuesubmap = intArrayOf()
			psysubmap = intArrayOf()

			coupling_mag = intArrayOf()
			coupling_ang = intArrayOf()
		}
	}

	internal inner class LookMapping0 {
		var mode: InfoMode =
			InfoMode()
		var map: InfoMapping0 = InfoMapping0()
		var time_look: Array<Any> = arrayOf()
		var floor_look: Array<Any> = arrayOf()
		var floor_state: Array<Any> = arrayOf()
		var residue_look: Array<Any> = arrayOf()
		var psy_look: Array<PsyLook> = arrayOf()

		var time_func: Array<FuncTime> = arrayOf()
		var floor_func: Array<FuncFloor> = arrayOf()
		var residue_func: Array<FuncResidue> = arrayOf()

		var ch: Int = 0
		var decay: Array<FloatArray>? = null
		var lastframe: Int = 0 // if a different mode is called, we need to
		// invalidate decay and floor state
	}
}

internal class Mdct {

	var n: Int = 0
	var log2n: Int = 0

	var trig: FloatArray = floatArrayOf()
	var bitrev: IntArray = intArrayOf()

	var scale: Float = 0.toFloat()

	fun init(n: Int) {
		bitrev = IntArray(n / 4)
		trig = FloatArray(n + n / 4)

		log2n = rint(log(n.toDouble(), 2.0)).toInt()
		this.n = n

		val AE = 0
		val AO = 1
		val BE = AE + n / 2
		val BO = BE + 1
		val CE = BE + n / 2
		val CO = CE + 1
		// trig lookups...
		for (i in 0 until n / 4) {
			trig[AE + i * 2] = cos(PI / n * (4 * i)).toFloat()
			trig[AO + i * 2] = (-sin(PI / n * (4 * i))).toFloat()
			trig[BE + i * 2] = cos(PI / (2 * n) * (2 * i + 1)).toFloat()
			trig[BO + i * 2] = sin(PI / (2 * n) * (2 * i + 1)).toFloat()
		}
		for (i in 0 until n / 8) {
			trig[CE + i * 2] = cos(PI / n * (4 * i + 2)).toFloat()
			trig[CO + i * 2] = (-sin(PI / n * (4 * i + 2))).toFloat()
		}

		run {
			val mask = (1 shl log2n - 1) - 1
			val msb = 1 shl log2n - 2
			for (i in 0 until n / 8) {
				var acc = 0
				var j = 0
				while (msb.ushr(j) != 0) {
					if (msb.ushr(j) and i != 0) {
						acc = acc or (1 shl j)
					}
					j++
				}
				bitrev[i * 2] = acc.inv() and mask
				//	bitrev[i*2]=((~acc)&mask)-1;
				bitrev[i * 2 + 1] = acc
			}
		}
		scale = 4f / n
	}

	fun clear() {}

	fun forward(`in`: FloatArray, out: FloatArray) {}

	var _x = FloatArray(1024)
	var _w = FloatArray(1024)

	@kotlin.jvm.Synchronized
	fun backward(`in`: FloatArray, out: FloatArray) {
		if (_x.size < n / 2) {
			_x = FloatArray(n / 2)
		}
		if (_w.size < n / 2) {
			_w = FloatArray(n / 2)
		}
		val x = _x
		val w = _w
		val n2 = n.ushr(1)
		val n4 = n.ushr(2)
		val n8 = n.ushr(3)

		// rotate + step 1
		run {
			var inO = 1
			var xO = 0
			var A = n2

			var i: Int
			i = 0
			while (i < n8) {
				A -= 2
				x[xO++] = -`in`[inO + 2] * trig[A + 1] - `in`[inO] * trig[A]
				x[xO++] = `in`[inO] * trig[A + 1] - `in`[inO + 2] * trig[A]
				inO += 4
				i++
			}

			inO = n2 - 4

			i = 0
			while (i < n8) {
				A -= 2
				x[xO++] = `in`[inO] * trig[A + 1] + `in`[inO + 2] * trig[A]
				x[xO++] = `in`[inO] * trig[A] - `in`[inO + 2] * trig[A + 1]
				inO -= 4
				i++
			}
		}

		val xxx = mdct_kernel(x, w, n, n2, n4, n8)
		var xx = 0

		// step 8

		run {
			var B = n2
			var o1 = n4
			var o2 = o1 - 1
			var o3 = n4 + n2
			var o4 = o3 - 1

			for (i in 0 until n4) {
				val temp1 = xxx[xx] * trig[B + 1] - xxx[xx + 1] * trig[B]
				val temp2 = -(xxx[xx] * trig[B] + xxx[xx + 1] * trig[B + 1])

				out[o1] = -temp1
				out[o2] = temp1
				out[o3] = temp2
				out[o4] = temp2

				o1++
				o2--
				o3++
				o4--
				xx += 2
				B += 2
			}
		}
	}

	private fun mdct_kernel(x: FloatArray, w: FloatArray, n: Int, n2: Int, n4: Int, n8: Int): FloatArray {
		var x = x
		var w = w
		// step 2

		var xA = n4
		var xB = 0
		var w2 = n4
		var A = n2

		run {
			var i = 0
			while (i < n4) {
				val x0 = x[xA] - x[xB]
				val x1: Float
				w[w2 + i] = x[xA++] + x[xB++]

				x1 = x[xA] - x[xB]
				A -= 4

				w[i++] = x0 * trig[A] + x1 * trig[A + 1]
				w[i] = x1 * trig[A] - x0 * trig[A + 1]

				w[w2 + i] = x[xA++] + x[xB++]
				i++
			}
		}

		// step 3

		run {
			for (i in 0 until log2n - 3) {
				var k0 = n.ushr(i + 2)
				val k1 = 1 shl i + 3
				var wbase = n2 - 2

				A = 0


				for (r in 0 until k0.ushr(2)) {
					var w1 = wbase
					w2 = w1 - (k0 shr 1)
					val AEv = trig[A]
					var wA: Float
					val AOv = trig[A + 1]
					var wB: Float
					wbase -= 2

					k0++
					for (s in 0 until (2 shl i)) {
						wB = w[w1] - w[w2]
						x[w1] = w[w1] + w[w2]

						wA = w[++w1] - w[++w2]
						x[w1] = w[w1] + w[w2]

						x[w2] = wA * AEv - wB * AOv
						x[w2 - 1] = wB * AEv + wA * AOv

						w1 -= k0
						w2 -= k0
					}
					k0--
					A += k1
				}

				val temp = w
				w = x
				x = temp
			}
		}

		// step 4, 5, 6, 7
		run {
			var C = n
			var bit = 0
			var x1 = 0
			var x2 = n2 - 1

			for (i in 0 until n8) {
				val t1 = bitrev[bit++]
				val t2 = bitrev[bit++]

				val wA = w[t1] - w[t2 + 1]
				val wB = w[t1 - 1] + w[t2]
				val wC = w[t1] + w[t2 + 1]
				val wD = w[t1 - 1] - w[t2]

				val wACE = wA * trig[C]
				val wBCE = wB * trig[C++]
				val wACO = wA * trig[C]
				val wBCO = wB * trig[C++]

				x[x1++] = (wC + wACO + wBCE) * .5f
				x[x2--] = (-wD + wBCO - wACE) * .5f
				x[x1++] = (wD + wBCO - wACE) * .5f
				x[x2--] = (wC - wACO - wBCE) * .5f
			}
		}
		return x
	}
}

// psychoacoustic setup
class PsyInfo {
	var athp: Int = 0
	var decayp: Int = 0
	var smoothp: Int = 0
	var noisefitp: Int = 0
	var noisefit_subblock: Int = 0
	var noisefit_threshdB: Float = 0.toFloat()

	var ath_att: Float = 0.toFloat()

	var tonemaskp: Int = 0
	var toneatt_125Hz = FloatArray(5)
	var toneatt_250Hz = FloatArray(5)
	var toneatt_500Hz = FloatArray(5)
	var toneatt_1000Hz = FloatArray(5)
	var toneatt_2000Hz = FloatArray(5)
	var toneatt_4000Hz = FloatArray(5)
	var toneatt_8000Hz = FloatArray(5)

	var peakattp: Int = 0
	var peakatt_125Hz = FloatArray(5)
	var peakatt_250Hz = FloatArray(5)
	var peakatt_500Hz = FloatArray(5)
	var peakatt_1000Hz = FloatArray(5)
	var peakatt_2000Hz = FloatArray(5)
	var peakatt_4000Hz = FloatArray(5)
	var peakatt_8000Hz = FloatArray(5)

	var noisemaskp: Int = 0
	var noiseatt_125Hz = FloatArray(5)
	var noiseatt_250Hz = FloatArray(5)
	var noiseatt_500Hz = FloatArray(5)
	var noiseatt_1000Hz = FloatArray(5)
	var noiseatt_2000Hz = FloatArray(5)
	var noiseatt_4000Hz = FloatArray(5)
	var noiseatt_8000Hz = FloatArray(5)

	var max_curve_dB: Float = 0.toFloat()

	var attack_coeff: Float = 0.toFloat()
	var decay_coeff: Float = 0.toFloat()

	fun free() {}
}

class PsyLook {
	var n: Int = 0
	var vi: PsyInfo? = null

	var tonecurves: Array<Array<FloatArray>> = arrayOf()
	var peakatt: Array<FloatArray> = arrayOf()
	var noisecurves: Array<Array<FloatArray>> = arrayOf()

	var ath: FloatArray? = null
	var octave: IntArray? = null

	fun init(vi: PsyInfo, n: Int, rate: Int) {}
}

internal open class Residue0 : FuncResidue() {
	override fun pack(vr: Any, opb: Buffer) {
		val info = vr as InfoResidue0
		var acc = 0
		opb.write(info.begin, 24)
		opb.write(info.end, 24)

		opb.write(info.grouping - 1, 24) /* residue vectors to group and
                           code with a partitioned book */
		opb.write(info.partitions - 1, 6) /* possible partition choices */
		opb.write(info.groupbook, 8) /* group huffman book */

		/* secondstages is a bitmask; as encoding progresses pass by pass, a
	   bitmask of one indicates this partition class has bits to write
	   this pass */
		for (j in 0 until info.partitions) {
			val i = info.secondstages[j]
			if (Util.ilog(i) > 3) {
				/* yes, this is a minor hack due to not thinking ahead */
				opb.write(i, 3)
				opb.write(1, 1)
				opb.write(i.ushr(3), 5)
			} else {
				opb.write(i, 4) /* trailing zero */
			}
			acc += Util.icount(i)
		}
		for (j in 0 until acc) {
			opb.write(info.booklist[j], 8)
		}
	}

	override fun unpack(vi: Info, opb: Buffer): Any? {
		var acc = 0
		val info = InfoResidue0()
		info.begin = opb.read(24)
		info.end = opb.read(24)
		info.grouping = opb.read(24) + 1
		info.partitions = opb.read(6) + 1
		info.groupbook = opb.read(8)

		for (j in 0 until info.partitions) {
			var cascade = opb.read(3)
			if (opb.read(1) != 0) {
				cascade = cascade or (opb.read(5) shl 3)
			}
			info.secondstages[j] = cascade
			acc += Util.icount(cascade)
		}

		for (j in 0 until acc) {
			info.booklist[j] = opb.read(8)
		}

		if (info.groupbook >= vi.books) {
			free_info(info)
			return null
		}

		for (j in 0 until acc) {
			if (info.booklist[j] >= vi.books) {
				free_info(info)
				return null
			}
		}
		return info
	}

	override fun look(vd: DspState, vm: InfoMode, vr: Any): Any {
		val info = vr as InfoResidue0
		val look = LookResidue0()
		var acc = 0
		val dim: Int
		var maxstage = 0
		look.info = info
		look.map = vm.mapping

		look.parts = info.partitions
		look.fullbooks = vd.fullbooks
		look.phrasebook = vd.fullbooks[info.groupbook]

		dim = look.phrasebook!!.dim

		look.partbooks = Array<IntArray>(look.parts) { intArrayOf() }

		for (j in 0 until look.parts) {
			val i = info.secondstages[j]
			val stages = Util.ilog(i)
			if (stages != 0) {
				if (stages > maxstage) maxstage = stages
				look.partbooks[j] = IntArray(stages)
				for (k in 0 until stages) {
					if (i and (1 shl k) != 0) {
						look.partbooks!![j][k] = info.booklist[acc++]
					}
				}
			}
		}

		look.partvals = rint(look.parts.toDouble().pow(dim.toDouble()))
            .toInt()
		look.stages = maxstage
		look.decodemap = Array<IntArray>(look.partvals) { intArrayOf() }
		for (j in 0 until look.partvals) {
			var `val` = j
			var mult = look.partvals / look.parts
			look.decodemap[j] = IntArray(dim)

			for (k in 0 until dim) {
				val deco = `val` / mult
				`val` -= deco * mult
				mult /= look.parts
				look.decodemap[j][k] = deco
			}
		}
		return look
	}

	override fun free_info(i: Any) {}

	override fun free_look(i: Any) {}

	override fun inverse(vb: Block, vl: Any, `in`: Array<FloatArray>, nonzero: IntArray, ch: Int): Int {
		var used = 0
		for (i in 0 until ch) {
			if (nonzero[i] != 0) {
				`in`[used++] = `in`[i]
			}
		}
		if (used != 0)
			return _01inverse(vb, vl, `in`, used, 0)
		else
			return 0
	}

	internal inner class LookResidue0 {
		var info: InfoResidue0? = null
		var map: Int = 0

		var parts: Int = 0
		var stages: Int = 0
		var fullbooks: Array<CodeBook> = arrayOf()
		var phrasebook: CodeBook? = null
		var partbooks: Array<IntArray> = arrayOf()

		var partvals: Int = 0
		var decodemap: Array<IntArray> = arrayOf()

		var postbits: Int = 0
		var phrasebits: Int = 0
		var frames: Int = 0
	}

	internal inner class InfoResidue0 {
		// block-partitioned VQ coded straight residue
		var begin: Int = 0
		var end: Int = 0

		// first stage (lossless partitioning)
		var grouping: Int = 0 // group n vectors per partition
		var partitions: Int = 0 // possible codebooks for a partition
		var groupbook: Int = 0 // huffbook for partitioning
		var secondstages = IntArray(64) // expanded out to pointers in lookup
		var booklist = IntArray(256) // list of second stage books

		// encode-only heuristic settings
		var entmax = FloatArray(64) // book entropy threshholds
		var ampmax = FloatArray(64) // book amp threshholds
		var subgrp = IntArray(64) // book heuristic subgroup size
		var blimit = IntArray(64) // subgroup position limits
	}

	private var _01inverse_partword = arrayOfNulls<Array<IntArray>>(2) // _01inverse is synchronized for

	// re-using partword
	@kotlin.jvm.Synchronized
	fun _01inverse(
		vb: Block, vl: Any, `in`: Array<FloatArray>, ch: Int,
		decodepart: Int
	): Int {
		var i: Int
		var j: Int
		var k: Int
		var l: Int
		var s: Int
		val look = vl as LookResidue0
		val info = look.info

		// move all this setup out later
		val samples_per_partition = info!!.grouping
		val partitions_per_word = look.phrasebook!!.dim
		val n = info.end - info.begin

		val partvals = n / samples_per_partition
		val partwords = (partvals + partitions_per_word - 1) / partitions_per_word

		if (_01inverse_partword.size < ch) {
			_01inverse_partword = arrayOfNulls<Array<IntArray>>(ch)
		}

		j = 0
		while (j < ch) {
			if (_01inverse_partword[j] == null || _01inverse_partword[j]!!.size < partwords) {
				_01inverse_partword[j] = Array<IntArray>(partwords) { intArrayOf() }
			}
			j++
		}

		s = 0
		while (s < look.stages) {
			// each loop decodes on partition codeword containing
			// partitions_pre_word partitions
			i = 0
			l = 0
			while (i < partvals) {
				if (s == 0) {
					// fetch the partition word for each channel
					j = 0
					while (j < ch) {
						val temp = look.phrasebook!!.decode(vb.opb)
						if (temp == -1) {
							return 0
						}
						_01inverse_partword!![j]!![l] = look.decodemap!![temp]
						if (_01inverse_partword!![j]!![l] == null) {
							return 0
						}
						j++
					}
				}

				// now we decode residual values for the partitions
				k = 0
				while (k < partitions_per_word && i < partvals) {

					j = 0
					while (j < ch) {
						val offset = info.begin + i * samples_per_partition
						val index = _01inverse_partword[j]!![l][k]
						if (info.secondstages[index] and (1 shl s) != 0) {
							val stagebook = look.fullbooks!![look.partbooks!![index][s]]
							if (stagebook != null) {
								if (decodepart == 0) {
									if (stagebook.decodevs_add(
											`in`[j], offset, vb.opb,
											samples_per_partition
										) == -1
									) {
										return 0
									}
								} else if (decodepart == 1) {
									if (stagebook.decodev_add(
											`in`[j], offset, vb.opb,
											samples_per_partition
										) == -1
									) {
										return 0
									}
								}
							}
						}
						j++
					}

					k++
					i++
				}
				l++
			}
			s++
		}
		return 0
	}

	var _2inverse_partword: Array<IntArray>? = null

	@kotlin.jvm.Synchronized
	fun _2inverse(vb: Block, vl: Any, `in`: Array<FloatArray>, ch: Int): Int {
		var i: Int
		var k: Int
		var l: Int
		var s: Int
		val look = vl as LookResidue0
		val info = look.info

		// move all this setup out later
		val samples_per_partition = info!!.grouping
		val partitions_per_word = look.phrasebook!!.dim
		val n = info.end - info.begin

		val partvals = n / samples_per_partition
		val partwords = (partvals + partitions_per_word - 1) / partitions_per_word

		if (_2inverse_partword == null || _2inverse_partword!!.size < partwords) {
			_2inverse_partword = Array<IntArray>(partwords) { intArrayOf() }
		}
		s = 0
		while (s < look.stages) {
			i = 0
			l = 0
			while (i < partvals) {
				if (s == 0) {
					// fetch the partition word for each channel
					val temp = look.phrasebook!!.decode(vb.opb)
					if (temp == -1) {
						return 0
					}
					_2inverse_partword!![l] = look.decodemap!![temp]
					if (_2inverse_partword!![l] == null) {
						return 0
					}
				}

				// now we decode residual values for the partitions
				k = 0
				while (k < partitions_per_word && i < partvals) {
					val offset = info.begin + i * samples_per_partition
					val index = _2inverse_partword!![l][k]
					if (info.secondstages[index] and (1 shl s) != 0) {
						val stagebook = look.fullbooks!![look.partbooks!![index][s]]
						if (stagebook != null) {
							if (stagebook.decodevv_add(
									`in`, offset, ch, vb.opb,
									samples_per_partition
								) == -1
							) {
								return 0
							}
						}
					}
					k++
					i++
				}
				l++
			}
			s++
		}
		return 0
	}
}

internal class Residue1 : Residue0() {

	override fun inverse(vb: Block, vl: Any, `in`: Array<FloatArray>, nonzero: IntArray, ch: Int): Int {
		var used = 0
		for (i in 0 until ch) {
			if (nonzero[i] != 0) {
				`in`[used++] = `in`[i]
			}
		}
		if (used != 0) {
			return _01inverse(vb, vl, `in`, used, 1)
		} else {
			return 0
		}
	}
}

internal class Residue2 : Residue0() {
	override fun inverse(vb: Block, vl: Any, `in`: Array<FloatArray>, nonzero: IntArray, ch: Int): Int {
		var i = 0
		while (i < ch) {
			if (nonzero[i] != 0)
				break
			i++
		}
		if (i == ch)
			return 0 /* no nonzero vectors */

		return _2inverse(vb, vl, `in`, ch)
	}
}

class StaticCodeBook
// map == 2: list of dim*entries quantized entry vals

constructor() {
	var dim: Int = 0 // codebook dimensions (elements per vector)
	var entries: Int = 0 // codebook entries
	var lengthlist: IntArray = intArrayOf() // codeword lengths in bits

	// mapping
	var maptype: Int = 0 // 0=none
	// 1=implicitly populated values from map column
	// 2=listed arbitrary values

	// The below does a linear, single monotonic sequence mapping.
	var q_min: Int = 0 // packed 32 bit float; quant value 0 maps to minval
	var q_delta: Int = 0 // packed 32 bit float; val 1 - val 0 == delta
	var q_quant: Int = 0 // bits: 0 < quant <= 16
	var q_sequencep: Int = 0 // bitflag

	// additional information for log (dB) mapping; the linear mapping
	// is assumed to actually be values in dB.  encodebias is used to
	// assign an error weight to 0 dB. We have two additional flags:
	// zeroflag indicates if entry zero is to represent -Inf dB; negflag
	// indicates if we're to represent negative linear values in a
	// mirror of the positive mapping.

	var quantlist: IntArray? = null // map == 1: (int)(entries/dim) element column map

	fun pack(opb: Buffer): Int {
		var i: Int
		var ordered = false

		opb.write(0x564342, 24)
		opb.write(dim, 16)
		opb.write(entries, 24)

		// pack the codewords.  There are two packings; length ordered and
		// length random.  Decide between the two now.

		i = 1
		while (i < entries) {
			if (lengthlist[i] < lengthlist[i - 1])
				break
			i++
		}
		if (i == entries)
			ordered = true

		if (ordered) {
			// length ordered.  We only need to say how many codewords of
			// each length.  The actual codewords are generated
			// deterministically

			var count = 0
			opb.write(1, 1) // ordered
			opb.write(lengthlist[0] - 1, 5) // 1 to 32

			i = 1
			while (i < entries) {
				val _this = lengthlist[i]
				val _last = lengthlist[i - 1]
				if (_this > _last) {
					for (j in _last until _this) {
						opb.write(i - count, Util.ilog(entries - count))
						count = i
					}
				}
				i++
			}
			opb.write(i - count, Util.ilog(entries - count))
		} else {
			// length random.  Again, we don't code the codeword itself, just
			// the length.  This time, though, we have to encode each length
			opb.write(0, 1) // unordered

			// algortihmic mapping has use for 'unused entries', which we tag
			// here.  The algorithmic mapping happens as usual, but the unused
			// entry has no codeword.
			i = 0
			while (i < entries) {
				if (lengthlist[i] == 0)
					break
				i++
			}

			if (i == entries) {
				opb.write(0, 1) // no unused entries
				i = 0
				while (i < entries) {
					opb.write(lengthlist[i] - 1, 5)
					i++
				}
			} else {
				opb.write(1, 1) // we have unused entries; thus we tag
				i = 0
				while (i < entries) {
					if (lengthlist[i] == 0) {
						opb.write(0, 1)
					} else {
						opb.write(1, 1)
						opb.write(lengthlist[i] - 1, 5)
					}
					i++
				}
			}
		}

		// is the entry number the desired return value, or do we have a
		// mapping? If we have a mapping, what type?
		opb.write(maptype, 4)
		when (maptype) {
			0 -> {
			}
			1, 2 -> {
				// implicitly populated value mapping
				// explicitly populated value mapping
				if (quantlist == null) {
					// no quantlist?  error
					return -1
				}

				// values that define the dequantization
				opb.write(q_min, 32)
				opb.write(q_delta, 32)
				opb.write(q_quant - 1, 4)
				opb.write(q_sequencep, 1)

				run {
					var quantvals = 0
					when (maptype) {
						1 ->
							// a single column of (c->entries/c->dim) quantized values for
							// building a full value list algorithmically (square lattice)
							quantvals = maptype1_quantvals()
						2 ->
							// every value (c->entries*c->dim total) specified explicitly
							quantvals = entries * dim
					}

					// quantized values
					i = 0
					while (i < quantvals) {
						opb.write(abs(quantlist!![i]), q_quant)
						i++
					}
				}
			}
			else ->
				// error case; we don't have any other map types now
				return -1
		}// no mapping
		return 0
	}

	// unpacks a codebook from the packet buffer into the codebook struct,
	// readies the codebook auxiliary structures for decode
	fun unpack(opb: Buffer): Int {
		var i: Int
		//memset(s,0,sizeof(static_codebook));

		// make sure alignment is correct
		if (opb.read(24) != 0x564342) {
			//    goto _eofout;
			clear()
			return -1
		}

		// first the basic parameters
		dim = opb.read(16)
		entries = opb.read(24)
		if (entries == -1) {
			//    goto _eofout;
			clear()
			return -1
		}

		// codeword ordering.... length ordered or unordered?
		when (opb.read(1)) {
			0 -> {
				// unordered
				lengthlist = IntArray(entries)

				// allocated but unused entries?
				if (opb.read(1) != 0) {
					// yes, unused entries

					i = 0
					while (i < entries) {
						if (opb.read(1) != 0) {
							val num = opb.read(5)
							if (num == -1) {
								//            goto _eofout;
								clear()
								return -1
							}
							lengthlist[i] = num + 1
						} else {
							lengthlist[i] = 0
						}
						i++
					}
				} else {
					// all entries used; no tagging
					i = 0
					while (i < entries) {
						val num = opb.read(5)
						if (num == -1) {
							//          goto _eofout;
							clear()
							return -1
						}
						lengthlist[i] = num + 1
						i++
					}
				}
			}
			1 ->
				// ordered
			{
				var length = opb.read(5) + 1
				lengthlist = IntArray(entries)

				i = 0
				while (i < entries) {
					val num = opb.read(Util.ilog(entries - i))
					if (num == -1) {
						//          goto _eofout;
						clear()
						return -1
					}
					var j = 0
					while (j < num) {
						lengthlist[i] = length
						j++
						i++
					}
					length++
				}
			}
			else ->
				// EOF
				return -1
		}

		// Do we have a mapping to unpack?
		maptype = opb.read(4)
		when (maptype) {
			0 -> {
			}
			1, 2 -> {
				// implicitly populated value mapping
				// explicitly populated value mapping
				q_min = opb.read(32)
				q_delta = opb.read(32)
				q_quant = opb.read(4) + 1
				q_sequencep = opb.read(1)

				run {
					var quantvals = 0
					when (maptype) {
						1 -> quantvals = maptype1_quantvals()
						2 -> quantvals = entries * dim
					}

					// quantized values
					quantlist = IntArray(quantvals)
					i = 0
					while (i < quantvals) {
						quantlist!![i] = opb.read(q_quant)
						i++
					}
					if (quantlist!![quantvals - 1] == -1) {
						//        goto _eofout;
						clear()
						return -1
					}
				}
			}
			else -> {
				//    goto _eofout;
				clear()
				return -1
			}
		}// no mapping
		// all set
		return 0
		//    _errout:
		//    _eofout:
		//    vorbis_staticbook_clear(s);
		//    return(-1);
	}

	// there might be a straightforward one-line way to do the below
	// that's portable and totally safe against roundoff, but I haven't
	// thought of it.  Therefore, we opt on the side of caution
	private fun maptype1_quantvals(): Int {
		var vals = floor(entries.toDouble().pow(1.0 / dim)).toInt()

		// the above *should* be reliable, but we'll not assume that FP is
		// ever reliable when bitstream sync is at stake; verify via integer
		// means that vals really is the greatest value of dim for which
		// vals^b->bim <= b->entries
		// treat the above as an initial guess
		while (true) {
			var acc = 1
			var acc1 = 1
			for (i in 0 until dim) {
				acc *= vals
				acc1 *= vals + 1
			}
			if (entries in acc until acc1) {
				return vals
			} else {
				if (acc > entries) {
					vals--
				} else {
					vals++
				}
			}
		}
	}

	fun clear() {}

	// unpack the quantized list of values for encode/decode
	// we need to deal with two map types: in map type 1, the values are
	// generated algorithmically (each column of the vector counts through
	// the values in the quant vector). in map type 2, all the values came
	// in in an explicit list.  Both value lists must be unpacked
	fun unquantize(): FloatArray? {

		if (maptype == 1 || maptype == 2) {
			val quantvals: Int
			val mindel = float32_unpack(q_min)
			val delta = float32_unpack(q_delta)
			val r = FloatArray(entries * dim)

			// maptype 1 and 2 both use a quantized value vector, but
			// different sizes
			when (maptype) {
				1 -> {
					// most of the time, entries%dimensions == 0, but we need to be
					// well defined.  We define that the possible vales at each
					// scalar is values == entries/dim.  If entries%dim != 0, we'll
					// have 'too few' values (values*dim<entries), which means that
					// we'll have 'left over' entries; left over entries use zeroed
					// values (and are wasted).  So don't generate codebooks like that
					quantvals = maptype1_quantvals()
					for (j in 0 until entries) {
						var last = 0f
						var indexdiv = 1
						for (k in 0 until dim) {
							val index = j / indexdiv % quantvals
							var `val` = quantlist!![index].toFloat()
							`val` = abs(`val`) * delta + mindel + last
							if (q_sequencep != 0)
								last = `val`
							r[j * dim + k] = `val`
							indexdiv *= quantvals
						}
					}
				}
				2 -> for (j in 0 until entries) {
					var last = 0f
					for (k in 0 until dim) {
						var `val` = quantlist!![j * dim + k].toFloat()
						//if((j*dim+k)==0){System.err.println(" | 0 -> "+val+" | ");}
						`val` = abs(`val`) * delta + mindel + last
						if (q_sequencep != 0)
							last = `val`
						r[j * dim + k] = `val`
						//if((j*dim+k)==0){System.err.println(" $ r[0] -> "+r[0]+" | ");}
					}
				}
			}//System.err.println("\nr[0]="+r[0]);
			return r
		}
		return null
	}

	companion object {

		// 32 bit float (not IEEE; nonnormalized mantissa +
		// biased exponent) : neeeeeee eeemmmmm mmmmmmmm mmmmmmmm
		// Why not IEEE?  It's just not that important here.

		val VQ_FEXP = 10
		val VQ_FMAN = 21
		val VQ_FEXP_BIAS = 768 // bias toward values smaller than 1.

		// doesn't currently guard under/overflow
		fun float32_pack(`val`: Float): Long {
			var `val` = `val`
			var sign = 0
			var exp: Int
			val mant: Int
			if (`val` < 0) {
				sign = 0x80000000.toInt()
				`val` = -`val`
			}
			exp = floor(log(`val`.toDouble(), 2.0)).toInt()
			mant = rint(`val`.toDouble().pow((VQ_FMAN - 1 - exp).toDouble()))
                .toInt()
			exp = exp + VQ_FEXP_BIAS shl
					VQ_FMAN
			return (sign or exp or mant).toLong()
		}

		fun float32_unpack(`val`: Int): Float {
			var mant = (`val` and 0x1fffff).toFloat()
			val exp = (`val` and 0x7fe00000).ushr(VQ_FMAN).toFloat()
			if (`val` and 0x80000000.toInt() != 0)
				mant = -mant
			return ldexp(
				mant,
				exp.toInt() - (VQ_FMAN - 1) - VQ_FEXP_BIAS
			)
		}

		fun ldexp(foo: Float, e: Int): Float {
			return (foo * 2.0.pow(e.toDouble())).toFloat()
		}
	}
}

class Time0 : FuncTime() {
	override fun pack(i: Any, opb: Buffer) {}
	override fun unpack(vi: Info, opb: Buffer): Any = ""
	override fun look(vd: DspState, mi: InfoMode, i: Any): Any = ""
	override fun free_info(i: Any) = Unit
	override fun free_look(i: Any) = Unit
	override fun inverse(vb: Block, i: Any, `in`: FloatArray, out: FloatArray): Int = 0
}

internal object Util {
	fun ilog(v: Int): Int {
		var v = v
		var ret = 0
		while (v != 0) {
			ret++
			v = v ushr 1
		}
		return ret
	}

	fun ilog2(v: Int): Int {
		var v = v
		var ret = 0
		while (v > 1) {
			ret++
			v = v ushr 1
		}
		return ret
	}

	fun icount(v: Int): Int {
		var v = v
		var ret = 0
		while (v != 0) {
			ret += v and 1
			v = v ushr 1
		}
		return ret
	}
}

class VorbisFile {
	var datasource: SyncStream? = null
	var seekable = false
	var offset: Long = 0
	var end: Long = 0

	var oy = SyncState()

	var links: Int = 0
	var offsets: LongArray = longArrayOf()
	var dataoffsets: LongArray = longArrayOf()
	var serialnos: IntArray = intArrayOf()
	var pcmlengths: LongArray = longArrayOf()
	var info: Array<Info> = arrayOf()
	var comment: Array<Comment> = arrayOf()

	// Decoding working state local storage
	var pcm_offset: Long = 0
	var decode_ready = false

	var current_serialno: Int = 0
	var current_link: Int = 0

	var bittrack: Float = 0.toFloat()
	var samptrack: Float = 0.toFloat()

	var os = StreamState() // take physical pages, weld into a logical
	// stream of packets
	var vd = DspState() // central working state for
	// the packet->PCM decoder
	var vb = Block(vd) // local working space for packet->PCM decode

	//ov_callbacks callbacks;

	//@Throws(JOrbisException::class)
	//constructor(file: String) : super() {
	//	var `is`: SyncStream? = null
	//	try {
	//		`is` = SeekableInputStream(file)
	//		val ret = open(`is`, null, 0)
	//		if (ret == -1) {
	//			throw JOrbisException("VorbisFile: open return -1")
	//		}
	//	} catch (e: Exception) {
	//		throw JOrbisException("VorbisFile: " + e.toString())
	//	} finally {
	//		if (`is` != null) {
	//			try {
	//				`is`.close()
	//			} catch (e: IOException) {
	//				e.printStackTrace()
	//			}
	//
	//		}
	//	}
	//}

	//@Throws(JOrbisException::class)
	constructor(`is`: SyncStream, initial: ByteArray, ibytes: Int) : super() {
		val ret = open(`is`, initial, ibytes)
		if (ret == -1) {
		}
	}

	private val _data: Int
		get() {
			val index = oy.buffer(CHUNKSIZE)
			val buffer = oy.data
			var bytes = 0
			try {
				bytes = datasource!!.read(buffer, index,
					CHUNKSIZE
				)
			} catch (e: Exception) {
				return OV_EREAD
			}

			oy.wrote(bytes)
			if (bytes == -1) {
				bytes = 0
			}
			return bytes
		}

	private fun seek_helper(offst: Long) {
		fseek(
			datasource!!,
			offst,
			SEEK_SET
		)
		this.offset = offst
		oy.reset()
	}

	private fun get_next_page(page: Page, boundary: Long): Int {
		var boundary = boundary
		if (boundary > 0)
			boundary += offset
		while (true) {
			val more: Int
			if (boundary > 0 && offset >= boundary)
				return OV_FALSE
			more = oy.pageseek(page)
			if (more < 0) {
				offset -= more.toLong()
			} else {
				if (more == 0) {
					if (boundary == 0L)
						return OV_FALSE
					val ret = _data
					if (ret == 0)
						return OV_EOF
					if (ret < 0)
						return OV_EREAD
				} else {
					val ret = offset.toInt() //!!!
					offset += more.toLong()
					return ret
				}
			}
		}
	}

	//@Throws(JOrbisException::class)
	private fun get_prev_page(page: Page): Int {
		var begin = offset //!!!
		var ret: Int
		var offst = -1
		while (offst == -1) {
			begin -= CHUNKSIZE.toLong()
			if (begin < 0) {
				begin = 0
			}
			seek_helper(begin)
			while (offset < begin + CHUNKSIZE) {
				ret = get_next_page(page, begin + CHUNKSIZE - offset)
				if (ret == OV_EREAD) {
					return OV_EREAD
				}
				if (ret < 0) {
					if (offst == -1) {
						throw JOrbisException()
					}
					break
				} else {
					offst = ret
				}
			}
		}
		seek_helper(offst.toLong()) //!!!
		ret = get_next_page(page, CHUNKSIZE.toLong())
		if (ret < 0) {
			return OV_EFAULT
		}
		return offst
	}

	fun bisect_forward_serialno(begin: Long, searched: Long, end: Long, currentno: Int, m: Int): Int {
		var searched = searched
		var endsearched = end
		var next = end
		val page = Page()
		var ret: Int

		while (searched < endsearched) {
			val bisect: Long
			if (endsearched - searched < CHUNKSIZE) {
				bisect = searched
			} else {
				bisect = (searched + endsearched) / 2
			}

			seek_helper(bisect)
			ret = get_next_page(page, -1)
			if (ret == OV_EREAD)
				return OV_EREAD
			if (ret < 0 || page.serialno() != currentno) {
				endsearched = bisect
				if (ret >= 0)
					next = ret.toLong()
			} else {
				searched = (ret + page.header_len + page.body_len).toLong()
			}
		}
		seek_helper(next)
		ret = get_next_page(page, -1)
		if (ret == OV_EREAD)
			return OV_EREAD

		if (searched >= end || ret == -1) {
			links = m + 1
			offsets = LongArray(m + 2)
			offsets[m + 1] = searched
		} else {
			ret = bisect_forward_serialno(next, offset, end, page.serialno(), m + 1)
			if (ret == OV_EREAD)
				return OV_EREAD
		}
		offsets[m] = begin
		return 0
	}

	// uses the local ogg_stream storage in vf; this is important for
	// non-streaming input sources
	fun fetch_headers(vi: Info, vc: Comment, serialno: IntArray?, og_ptr: Page?): Int {
		var og_ptr = og_ptr
		val og = Page()
		val op = Packet()
		val ret: Int

		if (og_ptr == null) {
			ret = get_next_page(og, CHUNKSIZE.toLong())
			if (ret == OV_EREAD)
				return OV_EREAD
			if (ret < 0)
				return OV_ENOTVORBIS
			og_ptr = og
		}

		if (serialno != null)
			serialno[0] = og_ptr.serialno()

		os.init(og_ptr.serialno())

		// extract the initial header from the first page and verify that the
		// Ogg bitstream is in fact Vorbis data

		vi.init()
		vc.init()

		var i = 0
		while (i < 3) {
			os.pagein(og_ptr)
			while (i < 3) {
				val result = os.packetout(op)
				if (result == 0)
					break
				if (result == -1) {
					vi.clear()
					vc.clear()
					os.clear()
					return -1
				}
				if (vi.synthesis_headerin(vc, op) != 0) {
					vi.clear()
					vc.clear()
					os.clear()
					return -1
				}
				i++
			}
			if (i < 3)
				if (get_next_page(og_ptr, 1) < 0) {
					vi.clear()
					vc.clear()
					os.clear()
					return -1
				}
		}
		return 0
	}

	// last step of the OggVorbis_File initialization; get all the
	// vorbis_info structs and PCM positions.  Only called by the seekable
	// initialization (local stream storage is hacked slightly; pay
	// attention to how that's done)
	//@Throws(JOrbisException::class)
	fun prefetch_all_headers(first_i: Info?, first_c: Comment?, dataoffset: Int) {
		val og = Page()
		var ret: Int

		info = Array<Info>(links) { Info() }
		comment = Array<Comment>(links) { Comment() }
		dataoffsets = LongArray(links)
		pcmlengths = LongArray(links)
		serialnos = IntArray(links)

		for (i in 0 until links) {
			if (first_i != null && first_c != null && i == 0) {
				// we already grabbed the initial header earlier.  This just
				// saves the waste of grabbing it again
				info[i] = first_i
				comment[i] = first_c
				dataoffsets[i] = dataoffset.toLong()
			} else {
				// seek to the location of the initial header
				seek_helper(offsets[i]) //!!!
				info[i] = Info()
				comment[i] = Comment()
				if (fetch_headers(info[i], comment[i], null, null) == -1) {
					dataoffsets[i] = -1
				} else {
					dataoffsets[i] = offset
					os.clear()
				}
			}

			// get the serial number and PCM length of this link. To do this,
			// get the last page of the stream
			run {
				val end = offsets[i + 1] //!!!
				seek_helper(end)

				while (true) {
					ret = get_prev_page(og)
					if (ret == -1) {
						// this should not be possible
						info[i].clear()
						comment[i].clear()
						break
					}
					if (og.granulepos() != -1L) {
						serialnos[i] = og.serialno()
						pcmlengths[i] = og.granulepos()
						break
					}
				}
			}
		}
	}

	private fun make_decode_ready(): Int {
		if (decode_ready) {
			throw RuntimeException("exit(1)")
		}
		vd.synthesis_init(info[0])
		vb.init(vd)
		decode_ready = true
		return 0
	}

	//@Throws(JOrbisException::class)
	fun open_seekable(): Int {
		val initial_i = Info()
		val initial_c = Comment()
		val serialno: Int
		var end: Long
		val ret: Int
		val dataoffset: Int
		val og = Page()
		// is this even vorbis...?
		val foo = IntArray(1)
		ret = fetch_headers(initial_i, initial_c, foo, null)
		serialno = foo[0]
		dataoffset = offset.toInt() //!!
		os.clear()
		if (ret == -1)
			return -1
		if (ret < 0)
			return ret
		// we can seek, so set out learning all about this file
		seekable = true
		fseek(
			datasource!!,
			0,
			SEEK_END
		)
		offset = ftell(datasource!!)
		end = offset
		// We get the offset for the last page of the physical bitstream.
		// Most OggVorbis files will contain a single logical bitstream
		end = get_prev_page(og).toLong()
		// moer than one logical bitstream?
		if (og.serialno() != serialno) {
			// Chained bitstream. Bisect-search each logical bitstream
			// section.  Do so based on serial number only
			if (bisect_forward_serialno(0, 0, end + 1, serialno, 0) < 0) {
				clear()
				return OV_EREAD
			}
		} else {
			// Only one logical bitstream
			if (bisect_forward_serialno(0, end, end + 1, serialno, 0) < 0) {
				clear()
				return OV_EREAD
			}
		}
		prefetch_all_headers(initial_i, initial_c, dataoffset)
		return 0
	}

	fun open_nonseekable(): Int {
		// we cannot seek. Set up a 'single' (current) logical bitstream entry
		links = 1
		info = Array<Info>(links) { Info() }
		info[0] = Info() // ??
		comment = Array<Comment>(links) { Comment() }
		comment[0] = Comment() // ?? bug?

		// Try to fetch the headers, maintaining all the storage
		val foo = IntArray(1)
		if (fetch_headers(info[0], comment[0], foo, null) == -1)
			return -1
		current_serialno = foo[0]
		make_decode_ready()
		return 0
	}

	// clear out the current logical bitstream decoder
	fun decode_clear() {
		os.clear()
		vd.clear()
		vb.clear()
		decode_ready = false
		bittrack = 0f
		samptrack = 0f
	}

	// fetch and process a packet.  Handles the case where we're at a
	// bitstream boundary and dumps the decoding machine.  If the decoding
	// machine is unloaded, it loads it.  It also keeps pcm_offset up to
	// date (seek and read both use this.  seek uses a special hack with
	// readp).
	//
	// return: -1) hole in the data (lost packet)
	//          0) need more date (only if readp==0)/eof
	//          1) got a packet

	fun process_packet(readp: Int): Int {
		val og = Page()

		// handle one packet.  Try to fetch it from current stream state
		// extract packets from page
		while (true) {
			// process a packet if we can.  If the machine isn't loaded,
			// neither is a page
			if (decode_ready) {
				val op = Packet()
				val result = os.packetout(op)
				var granulepos: Long
				// if(result==-1)return(-1); // hole in the data. For now, swallow
				// and go. We'll need to add a real
				// error code in a bit.
				if (result > 0) {
					// got a packet.  process it
					granulepos = op.granulepos
					if (vb.synthesis(op) == 0) { // lazy check for lazy
						// header handling.  The
						// header packets aren't
						// audio, so if/when we
						// submit them,
						// vorbis_synthesis will
						// reject them
						// suck in the synthesis data and track bitrate
						run {
							val oldsamples = vd.synthesis_pcmout(null, intArrayOf())
							vd.synthesis_blockin(vb)
							samptrack += (vd.synthesis_pcmout(null, intArrayOf()) - oldsamples).toFloat()
							bittrack += (op.bytes * 8).toFloat()
						}

						// update the pcm offset.
						if (granulepos != -1L && op.e_o_s == 0) {
							val link = if (seekable) current_link else 0

							val samples = vd.synthesis_pcmout(null, intArrayOf())
							granulepos -= samples.toLong()
							for (i in 0 until link) {
								granulepos += pcmlengths[i]
							}
							pcm_offset = granulepos
						}
						return 1
					}
				}
			}

			if (readp == 0)
				return 0
			if (get_next_page(og, -1) < 0)
				return 0 // eof. leave unitialized

			// bitrate tracking; add the header's bytes here, the body bytes
			// are done by packet above
			bittrack += (og.header_len * 8).toFloat()

			// has our decoding just traversed a bitstream boundary?
			if (decode_ready) {
				if (current_serialno != og.serialno()) {
					decode_clear()
				}
			}

			// Do we need to load a new machine before submitting the page?
			// This is different in the seekable and non-seekable cases.
			//
			// In the seekable case, we already have all the header
			// information loaded and cached; we just initialize the machine
			// with it and continue on our merry way.
			//
			// In the non-seekable (streaming) case, we'll only be at a
			// boundary if we just left the previous logical bitstream and
			// we're now nominally at the header of the next bitstream

			if (!decode_ready) {
				var i: Int
				if (seekable) {
					current_serialno = og.serialno()

					// match the serialno to bitstream section.  We use this rather than
					// offset positions to avoid problems near logical bitstream
					// boundaries
					i = 0
					while (i < links) {
						if (serialnos[i] == current_serialno)
							break
						i++
					}
					if (i == links)
						return -1 // sign of a bogus stream.  error out,
					// leave machine uninitialized
					current_link = i

					os.init(current_serialno)
					os.reset()

				} else {
					// we're streaming
					// fetch the three header packets, build the info struct
					val foo = IntArray(1)
					val ret = fetch_headers(info[0], comment[0], foo, og)
					current_serialno = foo[0]
					if (ret != 0)
						return ret
					current_link++
					i = 0
				}
				make_decode_ready()
			}
			os.pagein(og)
		}
	}

	// The helpers are over; it's all toplevel interface from here on out
	// clear out the OggVorbis_File struct
	fun clear(): Int {
		vb.clear()
		vd.clear()
		os.clear()

		if (links != 0) {
			for (i in 0 until links) {
				info[i].clear()
				comment[i].clear()
			}
			info = arrayOf()
			comment = arrayOf()
		}
		dataoffsets = longArrayOf()
		pcmlengths = longArrayOf()
		serialnos = intArrayOf()
		offsets = longArrayOf()
		oy.clear()

		return 0
	}

	// inspects the OggVorbis file and finds/documents all the logical
	// bitstreams contained in it.  Tries to be tolerant of logical
	// bitstream sections that are truncated/woogie.
	//
	// return: -1) error
	//          0) OK

	//@Throws(JOrbisException::class)
	fun open(`is`: SyncStream, initial: ByteArray?, ibytes: Int): Int {
		return open_callbacks(`is`, initial, ibytes)
	}

	//@Throws(JOrbisException::class)
	fun open_callbacks(
        `is`: SyncStream, initial: ByteArray?, ibytes: Int//, callbacks callbacks
	): Int {
		val ret: Int
		datasource = `is`

		oy.init()

		// perhaps some data was previously read into a buffer for testing
		// against other stream types.  Allow initialization from this
		// previously read data (as we may be reading from a non-seekable
		// stream)
		if (initial != null) {
			val index = oy.buffer(ibytes)
            arraycopy(initial, 0, oy.data, index, ibytes)
			oy.wrote(ibytes)
		}
		// can we seek? Stevens suggests the seek test was portable
		if (`is`.isSeekable) {
			ret = open_seekable()
		} else {
			ret = open_nonseekable()
		}
		if (ret != 0) {
			datasource = null
			clear()
		}
		return ret
	}

	// How many logical bitstreams in this physical bitstream?
	fun streams(): Int {
		return links
	}

	// Is the FILE * associated with vf seekable?
	fun isSeekable(): Boolean {
		return seekable
	}

	// returns the bitrate for a given logical bitstream or the entire
	// physical bitstream.  If the file is open for random access, it will
	// find the *actual* average bitrate.  If the file is streaming, it
	// returns the nominal bitrate (if set) else the average of the
	// upper/lower bounds (if set) else -1 (unset).
	//
	// If you want the actual bitrate field settings, get them from the
	// vorbis_info structs

	fun bitrate(i: Int): Int {
		if (i >= links)
			return -1
		if (!seekable && i != 0)
			return bitrate(0)
		if (i < 0) {
			var bits: Long = 0
			for (j in 0 until links) {
				bits += (offsets[j + 1] - dataoffsets[j]) * 8
			}
			return rint((bits / time_total(-1)).toDouble()).toInt()
		} else {
			if (seekable) {
				// return the actual bitrate
				return rint(
					((offsets[i + 1] - dataoffsets[i]) * 8 / time_total(
						i
					)).toDouble()
				).toInt()
			} else {
				// return nominal if set
				if (info[i].bitrate_nominal > 0) {
					return info[i].bitrate_nominal
				} else {
					if (info[i].bitrate_upper > 0) {
						if (info[i].bitrate_lower > 0) {
							return (info[i].bitrate_upper + info[i].bitrate_lower) / 2
						} else {
							return info[i].bitrate_upper
						}
					}
					return -1
				}
			}
		}
	}

	// returns the actual bitrate since last call.  returns -1 if no
	// additional data to offer since last call (or at beginning of stream)
	fun bitrate_instant(): Int {
		val _link = if (seekable) current_link else 0
		if (samptrack == 0f) return -1
		val ret = (bittrack / samptrack * info[_link].rate + .5).toInt()
		bittrack = 0f
		samptrack = 0f
		return ret
	}

	fun serialnumber(i: Int): Int {
		if (i >= links) return -1
		if (!seekable && i >= 0) return serialnumber(-1)
		return if (i < 0) current_serialno else serialnos[i]
	}

	// returns: total raw (compressed) length of content if i==-1
	//          raw (compressed) length of that logical bitstream for i==0 to n
	//          -1 if the stream is not seekable (we can't know the length)

	fun raw_total(i: Int): Long {
		if (!seekable || i >= links) return (-1).toLong()
		if (i < 0) {
			var acc: Long = 0 // bug?
			for (j in 0 until links) {
				acc += raw_total(j)
			}
			return acc
		} else {
			return offsets[i + 1] - offsets[i]
		}
	}

	// returns: total PCM length (samples) of content if i==-1
	//          PCM length (samples) of that logical bitstream for i==0 to n
	//          -1 if the stream is not seekable (we can't know the length)
	fun pcm_total(i: Int): Long {
		if (!seekable || i >= links)
			return (-1).toLong()
		if (i < 0) {
			var acc: Long = 0
			for (j in 0 until links) {
				acc += pcm_total(j)
			}
			return acc
		} else {
			return pcmlengths[i]
		}
	}

	// returns: total seconds of content if i==-1
	//          seconds in that logical bitstream for i==0 to n
	//          -1 if the stream is not seekable (we can't know the length)
	fun time_total(i: Int): Float {
		if (!seekable || i >= links)
			return (-1).toFloat()
		if (i < 0) {
			var acc = 0f
			for (j in 0 until links) {
				acc += time_total(j)
			}
			return acc
		} else {
			return pcmlengths[i].toFloat() / info[i].rate
		}
	}

	// seek to an offset relative to the *compressed* data. This also
	// immediately sucks in and decodes pages to update the PCM cursor. It
	// will cross a logical bitstream boundary, but only if it can't get
	// any packets out of the tail of the bitstream we seek to (so no
	// surprises).
	//
	// returns zero on success, nonzero on failure

	fun raw_seek(pos: Int): Int {
		if (!seekable)
			return -1 // don't dump machine if we can't seek
		if (pos < 0 || pos > offsets[links]) {
			//goto seek_error;
			pcm_offset = -1
			decode_clear()
			return -1
		}

		// clear out decoding machine state
		pcm_offset = -1
		decode_clear()

		// seek
		seek_helper(pos.toLong())

		// we need to make sure the pcm_offset is set.  We use the
		// _fetch_packet helper to process one packet with readp set, then
		// call it until it returns '0' with readp not set (the last packet
		// from a page has the 'granulepos' field set, and that's how the
		// helper updates the offset

		when (process_packet(1)) {
			0 -> {
				// oh, eof. There are no packets remaining.  Set the pcm offset to
				// the end of file
				pcm_offset = pcm_total(-1)
				return 0
			}
			-1 -> {
				// error! missing data or invalid bitstream structure
				//goto seek_error;
				pcm_offset = -1
				decode_clear()
				return -1
			}
			else -> {
			}
		}// all OK
		while (true) {
			when (process_packet(0)) {
				0 ->
					// the offset is set.  If it's a bogus bitstream with no offset
					// information, it's not but that's not our fault.  We still run
					// gracefully, we're just missing the offset
					return 0
				-1 -> {
					// error! missing data or invalid bitstream structure
					//goto seek_error;
					pcm_offset = -1
					decode_clear()
					return -1
				}
				else -> {
				}
			}// continue processing packets
		}

		// seek_error:
		// dump the machine so we're in a known state
		//pcm_offset=-1;
		//decode_clear();
		//return -1;
	}

	// seek to a sample offset relative to the decompressed pcm stream
	// returns zero on success, nonzero on failure

	fun pcm_seek(pos: Long): Int {
		var link = -1
		var total = pcm_total(-1)

		if (!seekable)
			return -1 // don't dump machine if we can't seek
		if (pos < 0 || pos > total) {
			//goto seek_error;
			pcm_offset = -1
			decode_clear()
			return -1
		}

		// which bitstream section does this pcm offset occur in?
		link = links - 1
		while (link >= 0) {
			total -= pcmlengths[link]
			if (pos >= total)
				break
			link--
		}

		// search within the logical bitstream for the page with the highest
		// pcm_pos preceeding (or equal to) pos.  There is a danger here;
		// missing pages or incorrect frame number information in the
		// bitstream could make our task impossible.  Account for that (it
		// would be an error condition)
		run {
			val target = pos - total
			var end = offsets[link + 1]
			var begin = offsets[link]
			var best = begin.toInt()

			val og = Page()
			while (begin < end) {
				val bisect: Long
				val ret: Int

				if (end - begin < CHUNKSIZE) {
					bisect = begin
				} else {
					bisect = (end + begin) / 2
				}

				seek_helper(bisect)
				ret = get_next_page(og, end - bisect)

				if (ret == -1) {
					end = bisect
				} else {
					val granulepos = og.granulepos()
					if (granulepos < target) {
						best = ret // raw offset of packet with granulepos
						begin = offset // raw offset of next packet
					} else {
						end = bisect
					}
				}
			}
			// found our page. seek to it (call raw_seek).
			if (raw_seek(best) != 0) {
				//goto seek_error;
				pcm_offset = -1
				decode_clear()
				return -1
			}
		}

		// verify result
		if (pcm_offset >= pos) {
			//goto seek_error;
			pcm_offset = -1
			decode_clear()
			return -1
		}
		if (pos > pcm_total(-1)) {
			//goto seek_error;
			pcm_offset = -1
			decode_clear()
			return -1
		}

		// discard samples until we reach the desired position. Crossing a
		// logical bitstream boundary with abandon is OK.
		while (pcm_offset < pos) {
			val target = (pos - pcm_offset).toInt()
			val _pcm = Array<Array<FloatArray>>(1) { arrayOf() }
			val _index = IntArray(getInfo(-1)!!.channels)
			var samples = vd.synthesis_pcmout(_pcm, _index)

			if (samples > target)
				samples = target
			vd.synthesis_read(samples)
			pcm_offset += samples.toLong()

			if (samples < target)
				if (process_packet(1) == 0) {
					pcm_offset = pcm_total(-1) // eof
				}
		}
		return 0

		// seek_error:
		// dump machine so we're in a known state
		//pcm_offset=-1;
		//decode_clear();
		//return -1;
	}

	// seek to a playback time relative to the decompressed pcm stream
	// returns zero on success, nonzero on failure
	fun time_seek(seconds: Float): Int {
		// translate time to PCM position and call pcm_seek

		var link = -1
		var pcm_total = pcm_total(-1)
		var time_total = time_total(-1)

		if (!seekable)
			return -1 // don't dump machine if we can't seek
		if (seconds < 0 || seconds > time_total) {
			//goto seek_error;
			pcm_offset = -1
			decode_clear()
			return -1
		}

		// which bitstream section does this time offset occur in?
		link = links - 1
		while (link >= 0) {
			pcm_total -= pcmlengths[link]
			time_total -= time_total(link)
			if (seconds >= time_total)
				break
			link--
		}

		// enough information to convert time offset to pcm offset
		run {
			val target = (pcm_total + (seconds - time_total) * info[link].rate).toLong()
			return pcm_seek(target)
		}

		//seek_error:
		// dump machine so we're in a known state
		//pcm_offset=-1;
		//decode_clear();
		//return -1;
	}

	// tell the current stream offset cursor.  Note that seek followed by
	// tell will likely not give the set offset due to caching
	fun raw_tell(): Long = offset

	// return PCM offset (sample) of next PCM sample to be read
	fun pcm_tell(): Long = pcm_offset

	// return time offset (seconds) of next PCM sample to be read
	fun time_tell(): Float {
		// translate time to PCM position and call pcm_seek

		var link = -1
		var pcm_total: Long = 0
		var time_total = 0f

		if (seekable) {
			pcm_total = pcm_total(-1)
			time_total = time_total(-1)

			// which bitstream section does this time offset occur in?
			link = links - 1
			while (link >= 0) {
				pcm_total -= pcmlengths[link]
				time_total -= time_total(link)
				if (pcm_offset >= pcm_total)
					break
				link--
			}
		}

		return time_total.toFloat() + (pcm_offset - pcm_total).toFloat() / info[link].rate
	}

	//  link:   -1) return the vorbis_info struct for the bitstream section
	//              currently being decoded
	//         0-n) to request information for a specific bitstream section
	//
	// In the case of a non-seekable bitstream, any call returns the
	// current bitstream.  NULL in the case that the machine is not
	// initialized

	fun getInfo(link: Int): Info? {
		if (seekable) {
			if (link < 0) {
				if (decode_ready) {
					return info[current_link]
				} else {
					return null
				}
			} else {
				if (link >= links) {
					return null
				} else {
					return info[link]
				}
			}
		} else {
			if (decode_ready) {
				return info[0]
			} else {
				return null
			}
		}
	}

	fun getComment(link: Int): Comment? {
		if (seekable) {
			if (link < 0) {
				if (decode_ready) {
					return comment[current_link]
				} else {
					return null
				}
			} else {
				if (link >= links) {
					return null
				} else {
					return comment[link]
				}
			}
		} else {
			if (decode_ready) {
				return comment[0]
			} else {
				return null
			}
		}
	}

	fun host_is_big_endian(): Int {
		return 1
		//    short pattern = 0xbabe;
		//    unsigned char *bytewise = (unsigned char *)&pattern;
		//    if (bytewise[0] == 0xba) return 1;
		//    assert(bytewise[0] == 0xbe);
		//    return 0;
	}

	// up to this point, everything could more or less hide the multiple
	// logical bitstream nature of chaining from the toplevel application
	// if the toplevel application didn't particularly care.  However, at
	// the point that we actually read audio back, the multiple-section
	// nature must surface: Multiple bitstream sections do not necessarily
	// have to have the same number of channels or sampling rate.
	//
	// read returns the sequential logical bitstream number currently
	// being decoded along with the PCM data in order that the toplevel
	// application can take action on channel/sample rate changes.  This
	// number will be incremented even for streamed (non-seekable) streams
	// (for seekable streams, it represents the actual logical bitstream
	// index within the physical bitstream.  Note that the accessor
	// functions above are aware of this dichotomy).
	//
	// input values: buffer) a buffer to hold packed PCM data for return
	//               length) the byte length requested to be placed into buffer
	//               bigendianp) should the data be packed LSB first (0) or
	//                           MSB first (1)
	//               word) word size for output.  currently 1 (byte) or
	//                     2 (16 bit short)
	//
	// return values: -1) error/hole in data
	//                 0) EOF
	//                 n) number of bytes of PCM actually returned.  The
	//                    below works on a packet-by-packet basis, so the
	//                    return length is not related to the 'length' passed
	//                    in, just guaranteed to fit.
	//
	// *section) set to the logical bitstream number

	fun read(
		buffer: ByteArray, length: Int, bigendianp: Int, word: Int, sgned: Int,
		bitstream: IntArray?
	): Int {
		val host_endian = host_is_big_endian()
		var index = 0

		while (true) {
			if (decode_ready) {
				val pcm: Array<FloatArray>
				val _pcm = Array<Array<FloatArray>>(1) { arrayOf() }
				val _index = IntArray(getInfo(-1)!!.channels)
				var samples = vd.synthesis_pcmout(_pcm, _index)
				pcm = _pcm[0]
				if (samples != 0) {
					// yay! proceed to pack data into the byte buffer
					val channels = getInfo(-1)!!.channels
					val bytespersample = word * channels
					if (samples > length / bytespersample)
						samples = length / bytespersample

					// a tight loop to pack each size
					run {
						var `val`: Int
						if (word == 1) {
							val off = if (sgned != 0) 0 else 128
							for (j in 0 until samples) {
								for (i in 0 until channels) {
									`val` = (pcm[i][_index[i] + j] * 128.0 + 0.5).toInt()
									if (`val` > 127)
										`val` = 127
									else if (`val` < -128)
										`val` = -128
									buffer[index++] = (`val` + off).toByte()
								}
							}
						} else {
							val off = if (sgned != 0) 0 else 32768

							if (host_endian == bigendianp) {
								if (sgned != 0) {
									for (i in 0 until channels) { // It's faster in this order
										val src = _index[i]
										var dest = i
										for (j in 0 until samples) {
											`val` = (pcm[i][src + j] * 32768.0 + 0.5).toInt()
											if (`val` > 32767)
												`val` = 32767
											else if (`val` < -32768)
												`val` = -32768
											buffer[dest] = `val`.ushr(8).toByte()
											buffer[dest + 1] = `val`.toByte()
											dest += channels * 2
										}
									}
								} else {
									for (i in 0 until channels) {
										val src = pcm[i]
										var dest = i
										for (j in 0 until samples) {
											`val` = (src[j] * 32768.0 + 0.5).toInt()
											if (`val` > 32767)
												`val` = 32767
											else if (`val` < -32768)
												`val` = -32768
											buffer[dest] = (`val` + off).ushr(8).toByte()
											buffer[dest + 1] = (`val` + off).toByte()
											dest += channels * 2
										}
									}
								}
							} else if (bigendianp != 0) {
								for (j in 0 until samples) {
									for (i in 0 until channels) {
										`val` = (pcm[i][j] * 32768.0 + 0.5).toInt()
										if (`val` > 32767)
											`val` = 32767
										else if (`val` < -32768)
											`val` = -32768
										`val` += off
										buffer[index++] = `val`.ushr(8).toByte()
										buffer[index++] = `val`.toByte()
									}
								}
							} else {
								//int val;
								for (j in 0 until samples) {
									for (i in 0 until channels) {
										`val` = (pcm[i][j] * 32768.0 + 0.5).toInt()
										if (`val` > 32767)
											`val` = 32767
										else if (`val` < -32768)
											`val` = -32768
										`val` += off
										buffer[index++] = `val`.toByte()
										buffer[index++] = `val`.ushr(8).toByte()
									}
								}
							}
						}
					}

					vd.synthesis_read(samples)
					pcm_offset += samples.toLong()
					if (bitstream != null)
						bitstream[0] = current_link
					return samples * bytespersample
				}
			}

			// suck in another packet
			when (process_packet(1)) {
				0 -> return 0
				-1 -> return -1
				else -> {
				}
			}
		}
	}

	//@Throws(IOException::class)
	fun close() {
		datasource!!.close()
	}

//	inner class SeekableInputStream @Throws(IOException::class)
//	constructor(file: String) : InputStream() {
//		var raf: java.io.RandomAccessFile? = null
//		val mode = "r"
//
//		init {
//			raf = java.io.RandomAccessFile(file, mode)
//		}
//
//		//@Throws(IOException::class)
//		override fun read(): Int {
//			return raf!!.read()
//		}
//
//		//@Throws(IOException::class)
//		override fun read(buf: ByteArray): Int {
//			return raf!!.read(buf)
//		}
//
//		//@Throws(IOException::class)
//		override fun read(buf: ByteArray, s: Int, len: Int): Int {
//			return raf!!.read(buf, s, len)
//		}
//
//		//@Throws(IOException::class)
//		override fun skip(n: Long): Long {
//			return raf!!.skipBytes(n.toInt()).toLong()
//		}
//
//		val length: Long
//			//@Throws(IOException::class)
//			get() = raf!!.length()
//
//		//@Throws(IOException::class)
//		fun tell(): Long {
//			return raf!!.filePointer
//		}
//
//		//@Throws(IOException::class)
//		override fun available(): Int {
//			return if (raf!!.length() == raf!!.filePointer) 0 else 1
//		}
//
//		//@Throws(IOException::class)
//		override fun close() {
//			raf!!.close()
//		}
//
//		@kotlin.jvm.Synchronized
//		override fun mark(m: Int) {
//		}
//
//		@kotlin.jvm.Synchronized
//		//@Throws(IOException::class)
//		override fun reset() {
//		}
//
//		override fun markSupported(): Boolean {
//			return false
//		}
//
//		//@Throws(IOException::class)
//		fun seek(pos: Long) {
//			raf!!.seek(pos)
//		}
//	}

	companion object {
		val CHUNKSIZE = 8500
		val SEEK_SET = 0
		val SEEK_CUR = 1
		val SEEK_END = 2

		val OV_FALSE = -1
		val OV_EOF = -2
		val OV_HOLE = -3

		val OV_EREAD = -128
		val OV_EFAULT = -129
		val OV_EIMPL = -130
		val OV_EINVAL = -131
		val OV_ENOTVORBIS = -132
		val OV_EBADHEADER = -133
		val OV_EVERSION = -134
		val OV_ENOTAUDIO = -135
		val OV_EBADPACKET = -136
		val OV_EBADLINK = -137
		val OV_ENOSEEK = -138

		fun fseek(fis: SyncStream, off: Long, whence: Int): Int {
			val sis = fis
			try {
				if (whence == SEEK_SET) {
					sis.position = off
				} else if (whence == SEEK_END) {
					sis.position = sis.length - off
				} else {
				}
			} catch (e: Exception) {
			}

			return 0
		}

		fun ftell(fis: SyncStream): Long {
			try {
				return fis.position
			} catch (e: Exception) {
			}

			return 0
		}
	}

	// @TODO:
	val SyncStream.isSeekable: Boolean get() = true
}

internal fun rint(v: Double): Double = if (v >= floor(v) + 0.5) ceil(v) else round(v)  // @TODO: Is this right?
