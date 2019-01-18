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

class Buffer {
	var ptr = 0
	var buffer: ByteArray = byteArrayOf()
	var endbit = 0
	var endbyte = 0
	var storage = 0

	fun writeinit() {
		buffer = ByteArray(BUFFER_INCREMENT)
		ptr = 0
		buffer[0] = '\u0000'.toByte()
		storage = BUFFER_INCREMENT
	}

	fun write(s: ByteArray) {
		for (i in s.indices) {
			if (s[i].toInt() == 0)
				break
			write(s[i].toInt(), 8)
		}
	}

	fun read(s: ByteArray, bytes: Int) {
		var bytes = bytes
		var i = 0
		while (bytes-- != 0) {
			s[i++] = read(8).toByte()
		}
	}

	fun writeclear() {
		buffer = byteArrayOf()
	}

	fun readinit(buf: ByteArray, start: Int, bytes: Int) {
		ptr = start
		buffer = buf
		endbit = 0
		endbyte = 0
		storage = bytes
	}

	fun write(value: Int, bits: Int) {
		var value = value
		var bits = bits
		if (endbyte + 4 >= storage) {
			val foo = ByteArray(storage + BUFFER_INCREMENT)
            arraycopy(buffer, 0, foo, 0, storage)
			buffer = foo
			storage += BUFFER_INCREMENT
		}

		value = value and mask[bits]
		bits += endbit
		buffer[ptr] = (buffer[ptr].unsigned or (value shl endbit)).toByte()

		if (bits >= 8) {
			buffer[ptr + 1] = value.ushr(8 - endbit).toByte()
			if (bits >= 16) {
				buffer[ptr + 2] = value.ushr(16 - endbit).toByte()
				if (bits >= 24) {
					buffer[ptr + 3] = value.ushr(24 - endbit).toByte()
					if (bits >= 32) {
						if (endbit > 0) {
							buffer[ptr + 4] = value.ushr(32 - endbit).toByte()
						} else {
							buffer[ptr + 4] = 0
						}
					}
				}
			}
		}

		endbyte += bits / 8
		ptr += bits / 8
		endbit = bits and 7
	}

	fun look(bits: Int): Int {
		var bits = bits
		var ret: Int
		val m = mask[bits]

		bits += endbit

		if (endbyte + 4 >= storage) {
			if (endbyte + (bits - 1) / 8 >= storage)
				return -1
		}

		ret = (buffer[ptr].unsigned).ushr(endbit)
		if (bits > 8) {
			ret = ret or (buffer[ptr + 1].unsigned shl 8 - endbit)
			if (bits > 16) {
				ret = ret or (buffer[ptr + 2].unsigned shl 16 - endbit)
				if (bits > 24) {
					ret = ret or (buffer[ptr + 3].unsigned shl 24 - endbit)
					if (bits > 32 && endbit != 0) {
						ret = ret or (buffer[ptr + 4].unsigned shl 32 - endbit)
					}
				}
			}
		}
		return m and ret
	}

	fun adv(bits: Int) {
		var bits = bits
		bits += endbit
		ptr += bits / 8
		endbyte += bits / 8
		endbit = bits and 7
	}

	fun read(bits: Int): Int {
		var bits = bits
		var ret: Int
		val m = mask[bits]

		bits += endbit

		if (endbyte + 4 >= storage) {
			ret = -1
			if (endbyte + (bits - 1) / 8 >= storage) {
				ptr += bits / 8
				endbyte += bits / 8
				endbit = bits and 7
				return ret
			}
		}

		ret = (buffer[ptr].unsigned).ushr(endbit)
		if (bits > 8) {
			ret = ret or (buffer[ptr + 1].unsigned shl 8 - endbit)
			if (bits > 16) {
				ret = ret or (buffer[ptr + 2].unsigned shl 16 - endbit)
				if (bits > 24) {
					ret = ret or (buffer[ptr + 3].unsigned shl 24 - endbit)
					if (bits > 32 && endbit != 0) {
						ret = ret or (buffer[ptr + 4].unsigned shl 32 - endbit)
					}
				}
			}
		}

		ret = ret and m

		ptr += bits / 8
		endbyte += bits / 8
		endbit = bits and 7
		return ret
	}

	fun read1(): Int {
		val ret: Int
		if (endbyte >= storage) {
			ret = -1
			endbit++
			if (endbit > 7) {
				endbit = 0
				ptr++
				endbyte++
			}
			return ret
		}

		ret = buffer[ptr].toInt() shr endbit and 1

		endbit++
		if (endbit > 7) {
			endbit = 0
			ptr++
			endbyte++
		}
		return ret
	}

	fun bytes(): Int {
		return endbyte + (endbit + 7) / 8
	}

	fun gbuffer(): ByteArray = buffer

	companion object {
		private val BUFFER_INCREMENT = 256
		private val mask = intArrayOf(
			0x00000000,
			0x00000001,
			0x00000003,
			0x00000007,
			0x0000000f,
			0x0000001f,
			0x0000003f,
			0x0000007f,
			0x000000ff,
			0x000001ff,
			0x000003ff,
			0x000007ff,
			0x00000fff,
			0x00001fff,
			0x00003fff,
			0x00007fff,
			0x0000ffff,
			0x0001ffff,
			0x0003ffff,
			0x0007ffff,
			0x000fffff,
			0x001fffff,
			0x003fffff,
			0x007fffff,
			0x00ffffff,
			0x01ffffff,
			0x03ffffff,
			0x07ffffff,
			0x0fffffff,
			0x1fffffff,
			0x3fffffff,
			0x7fffffff,
			0xffffffff.toInt()
		)
	}
}

class Packet {
	var packet_base: ByteArray = byteArrayOf()
	var packet: Int = 0
	var bytes: Int = 0
	var b_o_s: Int = 0
	var e_o_s: Int = 0
	var granulepos: Long = 0
	var packetno: Long = 0
}

class Page {
	var header_base: ByteArray = byteArrayOf()
	var header: Int = 0
	var header_len: Int = 0
	var body_base: ByteArray = byteArrayOf()
	var body: Int = 0
	var body_len: Int = 0

	fun version(): Int = header_base[header + 4].toInt() and 0xff
	fun continued(): Int = header_base[header + 5].toInt() and 0x01
	fun bos(): Int = header_base[header + 5].toInt() and 0x02
	fun eos(): Int = header_base[header + 5].toInt() and 0x04

	fun granulepos(): Long {
		var foo = (header_base[header + 13].unsigned).toLong()
		foo = foo shl 8 or (header_base[header + 12].unsigned.toLong())
		foo = foo shl 8 or (header_base[header + 11].unsigned.toLong())
		foo = foo shl 8 or (header_base[header + 10].unsigned.toLong())
		foo = foo shl 8 or (header_base[header + 9].unsigned.toLong())
		foo = foo shl 8 or (header_base[header + 8].unsigned.toLong())
		foo = foo shl 8 or (header_base[header + 7].unsigned.toLong())
		foo = foo shl 8 or (header_base[header + 6].unsigned.toLong())
		return foo
	}

	fun serialno(): Int {
		return header_base[header + 14].unsigned or (header_base[header + 15].unsigned shl 8) or (header_base[header + 16].unsigned shl 16) or (header_base[header + 17].unsigned shl 24)
	}

	fun pageno(): Int {
		return header_base[header + 18].unsigned or (header_base[header + 19].unsigned shl 8) or (header_base[header + 20].unsigned shl 16) or (header_base[header + 21].unsigned shl 24)
	}

	fun checksum() {
		var crc_reg = 0

		for (i in 0 until header_len) {
			crc_reg = crc_reg shl 8 xor crc_lookup[crc_reg.ushr(24) and 0xff xor (header_base[header + i].unsigned)]
		}
		for (i in 0 until body_len) {
			crc_reg = crc_reg shl 8 xor crc_lookup[crc_reg.ushr(24) and 0xff xor (body_base[body + i].unsigned)]
		}
		header_base[header + 22] = crc_reg.toByte()
		header_base[header + 23] = crc_reg.ushr(8).toByte()
		header_base[header + 24] = crc_reg.ushr(16).toByte()
		header_base[header + 25] = crc_reg.ushr(24).toByte()
	}

	fun copy(p: Page = Page()): Page {
		var tmp = ByteArray(header_len)
        arraycopy(header_base, header, tmp, 0, header_len)
		p.header_len = header_len
		p.header_base = tmp
		p.header = 0
		tmp = ByteArray(body_len)
        arraycopy(body_base, body, tmp, 0, body_len)
		p.body_len = body_len
		p.body_base = tmp
		p.body = 0
		return p
	}

	companion object {
		private val crc_lookup = IntArray(256) {
			var r = it shl 24
			for (i in 0..7) {
				r = if (r and 0x80000000.toInt() != 0) {
					r shl 1 xor 0x04c11db7
				} else {
					r shl 1
				}
			}
			r and 0xffffffff.toInt()
		}
	}
}

class StreamState {
	var body_storage: Int = 16 * 1024
	var body_data: ByteArray = ByteArray(body_storage)
	var body_fill: Int = 0
	private var body_returned: Int = 0

	var lacing_storage: Int = 1024
	var lacing_vals: IntArray = IntArray(lacing_storage)
	var granule_vals: LongArray = LongArray(lacing_storage)
	var lacing_fill: Int = 0
	var lacing_packet: Int = 0
	var lacing_returned: Int = 0

	var header = ByteArray(282)
	var header_fill: Int = 0

	var e_o_s: Int = 0
	var b_o_s: Int = 0
	var serialno: Int = 0
	var pageno: Int = 0
	var packetno: Long = 0
	var granulepos: Long = 0

	fun init(serialno: Int) {
		for (i in body_data.indices) body_data[i] = 0
		for (i in lacing_vals.indices) lacing_vals[i] = 0
		for (i in granule_vals.indices) granule_vals[i] = 0
		this.serialno = serialno
	}

	fun clear() {
	}

	fun destroy() {
		clear()
	}

	fun body_expand(needed: Int) {
		if (body_storage <= body_fill + needed) {
			body_storage += needed + 1024
			val foo = ByteArray(body_storage)
            arraycopy(body_data, 0, foo, 0, body_data.size)
			body_data = foo
		}
	}

	fun lacing_expand(needed: Int) {
		if (lacing_storage <= lacing_fill + needed) {
			lacing_storage += needed + 32
			val foo = IntArray(lacing_storage)
            arraycopy(lacing_vals, 0, foo, 0, lacing_vals.size)
			lacing_vals = foo

			val bar = LongArray(lacing_storage)
            arraycopy(granule_vals, 0, bar, 0, granule_vals.size)
			granule_vals = bar
		}
	}

	fun packetout(op: Packet): Int {
		var ptr = lacing_returned

		if (lacing_packet <= ptr) return 0

		if (lacing_vals[ptr] and 0x400 != 0) {
			lacing_returned++
			packetno++
			return -1
		}

		run {
			var size = lacing_vals[ptr] and 0xff
			var bytes = 0

			op.packet_base = body_data
			op.packet = body_returned
			op.e_o_s = lacing_vals[ptr] and 0x200
			op.b_o_s = lacing_vals[ptr] and 0x100
			bytes += size

			while (size == 255) {
				val vall = lacing_vals[++ptr]
				size = vall and 0xff
				if (vall and 0x200 != 0) op.e_o_s = 0x200
				bytes += size
			}

			op.packetno = packetno
			op.granulepos = granule_vals[ptr]
			op.bytes = bytes

			body_returned += bytes

			lacing_returned = ptr + 1
		}
		packetno++
		return 1
	}

	fun pagein(og: Page): Int {
		val header_base = og.header_base
		val header = og.header
		val body_base = og.body_base
		var body = og.body
		var bodysize = og.body_len
		var segptr = 0

		val version = og.version()
		val continued = og.continued()
		var bos = og.bos()
		val eos = og.eos()
		val granulepos = og.granulepos()
		val _serialno = og.serialno()
		val _pageno = og.pageno()
		val segments = header_base[header + 26].unsigned

		// clean up 'returned data'
		run {
			val lr = lacing_returned
			val br = body_returned

			// body data
			if (br != 0) {
				body_fill -= br
				if (body_fill != 0) {
                    arraycopy(body_data, br, body_data, 0, body_fill)
				}
				body_returned = 0
			}

			if (lr != 0) {
				// segment table
				if (lacing_fill - lr != 0) {
                    arraycopy(lacing_vals, lr, lacing_vals, 0, lacing_fill - lr)
                    arraycopy(granule_vals, lr, granule_vals, 0, lacing_fill - lr)
				}
				lacing_fill -= lr
				lacing_packet -= lr
				lacing_returned = 0
			}
		}

		// check the serial number
		if (_serialno != serialno)
			return -1
		if (version > 0)
			return -1

		lacing_expand(segments + 1)

		// are we in sequence?
		if (_pageno != pageno) {
			var i: Int

			// unroll previous partial packet (if any)
			i = lacing_packet
			while (i < lacing_fill) {
				body_fill -= lacing_vals[i] and 0xff
				i++
				//System.out.println("??");
			}
			lacing_fill = lacing_packet

			// make a note of dropped data in segment table
			if (pageno != -1) {
				lacing_vals[lacing_fill++] = 0x400
				lacing_packet++
			}

			// are we a 'continued packet' page?  If so, we'll need to skip
			// some segments
			if (continued != 0) {
				bos = 0
				while (segptr < segments) {
					val vall = header_base[header + 27 + segptr].unsigned
					body += vall
					bodysize -= vall
					if (vall < 255) {
						segptr++
						break
					}
					segptr++
				}
			}
		}

		if (bodysize != 0) {
			body_expand(bodysize)
            arraycopy(body_base, body, body_data, body_fill, bodysize)
			body_fill += bodysize
		}

		run {
			var saved = -1
			while (segptr < segments) {
				val vall = header_base[header + 27 + segptr].unsigned
				lacing_vals[lacing_fill] = vall
				granule_vals[lacing_fill] = -1

				if (bos != 0) {
					lacing_vals[lacing_fill] = lacing_vals[lacing_fill] or 0x100
					bos = 0
				}

				if (vall < 255) {
					saved = lacing_fill
				}

				lacing_fill++
				segptr++

				if (vall < 255) {
					lacing_packet = lacing_fill
				}
			}

			/* set the granulepos on the last pcmval of the last full packet */
			if (saved != -1) {
				granule_vals[saved] = granulepos
			}
		}

		if (eos != 0) {
			e_o_s = 1
			if (lacing_fill > 0)
				lacing_vals[lacing_fill - 1] = lacing_vals[lacing_fill - 1] or 0x200
		}

		pageno = _pageno + 1
		return 0
	}

	fun flush(og: Page): Int {

		var i: Int
		var vals = 0
		val maxvals = if (lacing_fill > 255) 255 else lacing_fill
		var bytes = 0
		var acc = 0
		var granule_pos = granule_vals[0]

		if (maxvals == 0) return 0

		if (b_o_s == 0) {
			granule_pos = 0
			vals = 0
			while (vals < maxvals) {
				if (lacing_vals[vals] and 0x0ff < 255) {
					vals++
					break
				}
				vals++
			}
		} else {
			vals = 0
			while (vals < maxvals) {
				if (acc > 4096) {
					break
				}
				acc += lacing_vals[vals] and 0x0ff
				granule_pos = granule_vals[vals]
				vals++
			}
		}

        arraycopy("OggS".toByteArray(), 0, header, 0, 4)
		header[4] = 0x00

		header[5] = 0x00
		if (lacing_vals[0] and 0x100 == 0) header[5] = (header[5].toInt() or 0x01).toByte()
		if (b_o_s == 0) header[5] = (header[5].toInt() or 0x02).toByte()
		if (e_o_s != 0 && lacing_fill == vals) header[5] = (header[5].toInt() or 0x04).toByte()
		b_o_s = 1

		i = 6
		while (i < 14) {
			header[i] = granule_pos.toByte()
			granule_pos = granule_pos ushr 8
			i++
		}

		run {
			var _serialno = serialno
			i = 14
			while (i < 18) {
				header[i] = _serialno.toByte()
				_serialno = _serialno ushr 8
				i++
			}
		}

		if (pageno == -1) pageno = 0

		run {
			var _pageno = pageno++
			i = 18
			while (i < 22) {
				header[i] = _pageno.toByte()
				_pageno = _pageno ushr 8
				i++
			}
		}

		header[22] = 0
		header[23] = 0
		header[24] = 0
		header[25] = 0

		header[26] = vals.toByte()
		i = 0
		while (i < vals) {
			header[i + 27] = lacing_vals[i].toByte()
			bytes += header[i + 27].unsigned
			i++
		}

		og.header_base = header
		og.header = 0
		header_fill = vals + 27
		og.header_len = header_fill
		og.body_base = body_data
		og.body = body_returned
		og.body_len = bytes

		lacing_fill -= vals
        arraycopy(lacing_vals, vals, lacing_vals, 0, lacing_fill * 4)
        arraycopy(granule_vals, vals, granule_vals, 0, lacing_fill * 8)
		body_returned += bytes

		og.checksum()

		return 1
	}

	fun pageout(og: Page): Int {
		if (e_o_s != 0 && lacing_fill != 0 || /* 'were done, now flush' case */
			body_fill - body_returned > 4096 || /* 'page nominal size' case */
			lacing_fill >= 255 || /* 'segment table full' case */
			lacing_fill != 0 && b_o_s == 0
		) { /* 'initial header page' case */
			return flush(og)
		}
		return 0
	}

	fun eof(): Int {
		return e_o_s
	}

	fun reset(): Int {
		body_fill = 0
		body_returned = 0

		lacing_fill = 0
		lacing_packet = 0
		lacing_returned = 0

		header_fill = 0

		e_o_s = 0
		b_o_s = 0
		pageno = -1
		packetno = 0
		granulepos = 0
		return 0
	}
}

class SyncState {
	var data: ByteArray = byteArrayOf()
	internal var storage: Int = 0
	var bufferOffset: Int = 0
		internal set
	var dataOffset: Int = 0
		internal set

	internal var unsynced: Int = 0
	internal var headerbytes: Int = 0
	internal var bodybytes: Int = 0

	fun clear(): Int {
		data = byteArrayOf()
		return 0
	}

	fun buffer(size: Int): Int {
		// first, clear out any space that has been previously returned
		if (dataOffset != 0) {
			bufferOffset -= dataOffset
			if (bufferOffset > 0) {
                arraycopy(data, dataOffset, data, 0, bufferOffset)
			}
			dataOffset = 0
		}

		if (size > storage - bufferOffset) {
			// We need to extend the internal buffer
			val newsize = size + bufferOffset + 4096 // an extra page to be nice
			if (data != null) {
				val foo = ByteArray(newsize)
                arraycopy(data, 0, foo, 0, data.size)
				data = foo
			} else {
				data = ByteArray(newsize)
			}
			storage = newsize
		}

		return bufferOffset
	}

	fun wrote(bytes: Int): Int {
		if (bufferOffset + bytes > storage) {
			return -1
		}
		bufferOffset += bytes
		return 0
	}

	// sync the stream.  This is meant to be useful for finding page
	// boundaries.
	//
	// return values for this:
	// -n) skipped n bytes
	//  0) page not ready; more data (no bytes skipped)
	//  n) page synced at current location; page length n bytes
	private val pageseek = Page()
	private val chksum = ByteArray(4)
	private val chksumLock = Lock()

	fun pageseek(og: Page?): Int {
		var page = dataOffset
		var next: Int
		var bytes = bufferOffset - dataOffset

		if (headerbytes == 0) {
			val _headerbytes: Int
			var i: Int
			if (bytes < 27) {
				return 0 // not enough for a header
			}

			/* verify capture pattern */
			if (data[page] != 'O'.toByte() || data[page + 1] != 'g'.toByte() || data[page + 2] != 'g'.toByte() || data[page + 3] != 'S'.toByte()) {
				headerbytes = 0
				bodybytes = 0

				// search for possible capture
				next = 0
				for (ii in 0..bytes - 1 - 1) {
					if (data[page + 1 + ii] == 'O'.toByte()) {
						next = page + 1 + ii
						break
					}
				}
				//next=memchr(page+1,'O',bytes-1);
				if (next == 0) {
					next = bufferOffset
				}

				dataOffset = next
				return -(next - page)
			}
			_headerbytes = (data[page + 26].unsigned) + 27
			if (bytes < _headerbytes) {
				return 0 // not enough for header + seg table
			}

			// count up body length in the segment table

			i = 0
			while (i < data[page + 26].unsigned) {
				bodybytes += data[page + 27 + i].unsigned
				i++
			}
			headerbytes = _headerbytes
		}

		if (bodybytes + headerbytes > bytes)
			return 0

		// The whole test page is buffered.  Verify the checksum
		val result = chksumLock {
			// Grab the checksum bytes, set the header field to zero

            arraycopy(data, page + 22, chksum, 0, 4)
			data[page + 22] = 0
			data[page + 23] = 0
			data[page + 24] = 0
			data[page + 25] = 0

			// set up a temp page struct and recompute the checksum
			val log = pageseek
			log.header_base = data
			log.header = page
			log.header_len = headerbytes

			log.body_base = data
			log.body = page + headerbytes
			log.body_len = bodybytes
			log.checksum()

			// Compare
			if (chksum[0] != data[page + 22] || chksum[1] != data[page + 23] || chksum[2] != data[page + 24] || chksum[3] != data[page + 25]) {
				// D'oh.  Mismatch! Corrupt page (or miscapture and not a page at all)
				// replace the computed checksum with the one actually read in
                arraycopy(chksum, 0, data, page + 22, 4)
				// Bad checksum. Lose sync */

				headerbytes = 0
				bodybytes = 0
				// search for possible capture
				next = 0
				for (ii in 0..bytes - 1 - 1) {
					if (data[page + 1 + ii] == 'O'.toByte()) {
						next = page + 1 + ii
						break
					}
				}
				//next=memchr(page+1,'O',bytes-1);
				if (next == 0)
					next = bufferOffset
				dataOffset = next
				-(next - page)
			} else {
				null
			}
		}

		if (result != null) {
			return result
		}

		// yes, have a whole page all ready to go
		run {
			page = dataOffset

			if (og != null) {
				og.header_base = data
				og.header = page
				og.header_len = headerbytes
				og.body_base = data
				og.body = page + headerbytes
				og.body_len = bodybytes
			}

			unsynced = 0
			bytes = headerbytes + bodybytes
			dataOffset += bytes
			headerbytes = 0
			bodybytes = 0
			return bytes
		}
	}

	// sync the stream and get a page.  Keep trying until we find a page.
	// Supress 'sync errors' after reporting the first.
	//
	// return values:
	//  -1) recapture (hole in data)
	//   0) need more data
	//   1) page returned
	//
	// Returns pointers into buffered data; invalidated by next call to
	// _stream, _clear, _init, or _buffer

	fun pageout(og: Page): Int {
		// all we need to do is verify a page at the head of the stream
		// buffer.  If it doesn't verify, we look for the next potential
		// frame

		while (true) {
			val ret = pageseek(og)
			if (ret > 0) {
				// have a page
				return 1
			}
			if (ret == 0) {
				// need more data
				return 0
			}

			// head did not start a synced page... skipped some bytes
			if (unsynced == 0) {
				unsynced = 1
				return -1
			}
			// loop. keep looking
		}
	}

	// clear things to an initial state.  Good to call, eg, before seeking
	fun reset(): Int {
		bufferOffset = 0
		dataOffset = 0
		unsynced = 0
		headerbytes = 0
		bodybytes = 0
		return 0
	}

	fun init() {}
}