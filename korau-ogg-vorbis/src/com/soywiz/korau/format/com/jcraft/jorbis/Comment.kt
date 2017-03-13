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

package com.soywiz.korau.format.com.jcraft.jorbis

import com.soywiz.korau.format.com.jcraft.jogg.Buffer
import com.soywiz.korau.format.com.jcraft.jogg.Packet
import com.soywiz.korio.util.toUnsigned

// the comments are not part of vorbis_info so that vorbis_info can be
// static storage
class Comment {

    // unlimited user comment fields.
    var user_comments: Array<ByteArray?>? = null
    var comment_lengths: IntArray? = null
    var comments: Int = 0
    var vendor: ByteArray? = null

    fun init() {
        user_comments = null
        comments = 0
        vendor = null
    }

    fun add(comment: String) {
        add(comment.toByteArray())
    }

    private fun add(comment: ByteArray) {
        val foo = arrayOfNulls<ByteArray>(comments + 2)
        if (user_comments != null) {
            System.arraycopy(user_comments!!, 0, foo, 0, comments)
        }
        user_comments = foo

        val goo = IntArray(comments + 2)
        if (comment_lengths != null) {
            System.arraycopy(comment_lengths!!, 0, goo, 0, comments)
        }
        comment_lengths = goo

        val bar = ByteArray(comment.size + 1)
        System.arraycopy(comment, 0, bar, 0, comment.size)
        user_comments!![comments] = bar
        comment_lengths!![comments] = comment.size
        comments++
        user_comments!![comments] = null
    }

    fun add_tag(tag: String, contents: String?) {
        var contents = contents
        if (contents == null)
            contents = ""
        add(tag + "=" + contents)
    }

    @JvmOverloads fun query(tag: String, count: Int = 0): String? {
        val foo = query(tag.toByteArray(), count)
        if (foo == -1)
            return null
        val comment = user_comments!![foo]
        for (i in 0 until comment_lengths!![foo]) {
            if (comment!![i] == '='.toByte()) {
                return String(comment, i + 1, comment_lengths!![foo] - (i + 1))
            }
        }
        return null
    }

    private fun query(tag: ByteArray, count: Int): Int {
        var i = 0
        var found = 0
        val fulltaglen = tag.size + 1
        val fulltag = ByteArray(fulltaglen)
        System.arraycopy(tag, 0, fulltag, 0, tag.size)
        fulltag[tag.size] = '='.toByte()

        i = 0
        while (i < comments) {
            if (tagcompare(user_comments!![i]!!, fulltag, fulltaglen)) {
                if (count == found) {
                    // We return a pointer to the data, not a copy
                    //return user_comments[i] + taglen + 1;
                    return i
                } else {
                    found++
                }
            }
            i++
        }
        return -1
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

        for (i in 0..comments - 1) {
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

    fun pack(opb: Buffer): Int {
        // preamble
        opb.write(0x03, 8)
        opb.write(_vorbis)

        // vendor
        opb.write(_vendor.size, 32)
        opb.write(_vendor)

        // comments
        opb.write(comments, 32)
        if (comments != 0) {
            for (i in 0..comments - 1) {
                if (user_comments!![i] != null) {
                    opb.write(comment_lengths!![i], 32)
                    opb.write(user_comments!![i]!!)
                } else {
                    opb.write(0, 32)
                }
            }
        }
        opb.write(1, 1)
        return 0
    }

    fun header_out(op: Packet): Int {
        val opb = Buffer()
        opb.writeinit()

        if (pack(opb) != 0)
            return OV_EIMPL

        op.packet_base = ByteArray(opb.bytes())
        op.packet = 0
        op.bytes = opb.bytes()
        System.arraycopy(opb.buffer(), 0, op.packet_base, 0, op.bytes)
        op.b_o_s = 0
        op.e_o_s = 0
        op.granulepos = 0
        return 0
    }

    fun clear() {
        for (i in 0 until comments)
            user_comments!![i] = null
        user_comments = null
        vendor = null
    }

    fun getVendor(): String {
        return String(vendor!!, 0, vendor!!.size - 1)
    }

    fun getComment(i: Int): String? {
        if (comments <= i)
            return null
        return String(user_comments!![i]!!, 0, user_comments!![i]!!.size - 1)
    }

    override fun toString(): String {
        var foo = "Vendor: " + String(vendor!!, 0, vendor!!.size - 1)
        for (i in 0..comments - 1) {
            foo = foo + "\nComment: "+String(user_comments!![i]!!, 0, user_comments!![i]!!.size - 1)
        }
        foo = foo + "\n"
        return foo
    }

    companion object {
        private val _vorbis = "vorbis".toByteArray()
        private val _vendor = "Xiphophorus libVorbis I 20000508".toByteArray()

        private val OV_EIMPL = -130

        internal fun tagcompare(s1: ByteArray, s2: ByteArray, n: Int): Boolean {
            var c = 0
            var u1: Char
            var u2: Char
            while (c < n) {
                u1 = s1[c].toUnsigned().toChar()
                u2 = s2[c].toUnsigned().toChar()
                if (u1 in 'A'..'Z')
                    u1 = (u1 - 'A'.toInt() + 'a'.toInt()).toChar()
                if (u2 in 'A'..'Z')
                    u2 = (u2 - 'A'.toInt() + 'a'.toInt()).toChar()
                if (u1 != u2) {
                    return false
                }
                c++
            }
            return true
        }
    }
}
