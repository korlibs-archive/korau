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

internal class CodeBook {
    var dim: Int = 0 // codebook dimensions (elements per vector)
    var entries: Int = 0 // codebook entries
    var c = StaticCodeBook()

    var valuelist: FloatArray? = null // list of dim*entries actual entry values
    var codelist: IntArray? = null // list of bitstream codewords for each entry
    var decode_tree: DecodeAux? = null

    // returns the number of bits
    fun encode(a: Int, b: Buffer): Int {
        b.write(codelist!![a], c.lengthlist[a])
        return c.lengthlist[a]
    }

    // One the encode side, our vector writers are each designed for a
    // specific purpose, and the encoder is not flexible without modification:
    //
    // The LSP vector coder uses a single stage nearest-match with no
    // interleave, so no step and no error return.  This is specced by floor0
    // and doesn't change.
    //
    // Residue0 encoding interleaves, uses multiple stages, and each stage
    // peels of a specific amount of resolution from a lattice (thus we want
    // to match by threshhold, not nearest match).  Residue doesn't *have* to
    // be encoded that way, but to change it, one will need to add more
    // infrastructure on the encode side (decode side is specced and simpler)

    // floor0 LSP (single stage, non interleaved, nearest match)
    // returns entry number and *modifies a* to the quantization value
    fun errorv(a: FloatArray): Int {
        val best = best(a, 1)
        for (k in 0 until dim) {
            a[k] = valuelist!![best * dim + k]
        }
        return best
    }

    // returns the number of bits and *modifies a* to the quantization value
    fun encodev(best: Int, a: FloatArray, b: Buffer): Int {
        for (k in 0 until dim) {
            a[k] = valuelist!![best * dim + k]
        }
        return encode(best, b)
    }

    // res0 (multistage, interleave, lattice)
    // returns the number of bits and *modifies a* to the remainder value
    fun encodevs(a: FloatArray, b: Buffer, step: Int, addmul: Int): Int {
        val best = besterror(a, step, addmul)
        return encode(best, b)
    }

    private var t = IntArray(15) // decodevs_add is synchronized for re-using t.

    @Synchronized fun decodevs_add(a: FloatArray, offset: Int, b: Buffer, n: Int): Int {
        val step = n / dim
        var entry: Int
        var i: Int
        var j: Int
        var o: Int

        if (t.size < step) {
            t = IntArray(step)
        }

        i = 0
        while (i < step) {
            entry = decode(b)
            if (entry == -1) {
                return -1
            }
            t[i] = entry * dim
            i++
        }
        i = 0
        o = 0
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

    // Decode side is specced and easier, because we don't need to find
    // matches using different criteria; we simply read and map.  There are
    // two things we need to do 'depending':
    //
    // We may need to support interleave.  We don't really, but it's
    // convenient to do it here rather than rebuild the vector later.
    //
    // Cascades may be additive or multiplicitive; this is not inherent in
    // the codebook, but set in the code using the codebook.  Like
    // interleaving, it's easiest to do it here.
    // stage==0 -> declarative (set the value)
    // stage==1 -> additive
    // stage==2 -> multiplicitive

    // returns the entry number or -1 on eof
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

    // returns the entry number or -1 on eof
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

    fun best(a: FloatArray, step: Int): Int {
        // brute force it!
        run {
            var besti = -1
            var best = 0f
            var e = 0
            for (i in 0..entries - 1) {
                if (c.lengthlist[i] > 0) {
                    val _this = dist(dim, valuelist!!, e, a, step)
                    if (besti == -1 || _this < best) {
                        best = _this
                        besti = i
                    }
                }
                e += dim
            }
            return besti
        }
    }

    // returns the entry number and *modifies a* to the remainder value
    fun besterror(a: FloatArray, step: Int, addmul: Int): Int {
        val best = best(a, step)
        when (addmul) {
            0 -> run {
                var i = 0
                var o = 0
                while (i < dim) {
                    a[o] -= valuelist!![best * dim + i]
                    i++
                    o += step
                }
            }
            1 -> {
                var i = 0
                var o = 0
                while (i < dim) {
                    val `val` = valuelist!![best * dim + i]
                    if (`val` == 0f) {
                        a[o] = 0f
                    } else {
                        a[o] /= `val`
                    }
                    i++
                    o += step
                }
            }
        }
        return best
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

        for (i in 0..entries - 1) {
            if (c.lengthlist[i] > 0) {
                var ptr = 0
                var j: Int
                j = 0
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
        for (i in 0..n - 1) {
            var p = 0
            var j = 0
            j = 0
            while (j < t.tabn && (p > 0 || j == 0)) {
                if (i and (1 shl j) != 0) {
                    p = ptr1[p]
                } else {
                    p = ptr0[p]
                }
                j++
            }
            t.tab[i] = p // -code
            t.tabl[i] = j // length
        }

        return t
    }

    internal inner class DecodeAux {
        var tab: IntArray = intArrayOf()
        var tabl: IntArray = intArrayOf()
        var tabn: Int = 0

        var ptr0: IntArray = intArrayOf()
        var ptr1: IntArray = intArrayOf()
        var aux: Int = 0 // number of tree entries
    }

    companion object {

        private fun dist(el: Int, ref: FloatArray, index: Int, b: FloatArray, step: Int): Float {
            var acc = 0.0.toFloat()
            for (i in 0..el - 1) {
                val `val` = ref[index + i] - b[i * step]
                acc += `val` * `val`
            }
            return acc
        }

        // given a list of word lengths, generate a list of codewords.  Works
        // for length ordered or unordered, always assigns the lowest valued
        // codewords first.  Extended to handle unused entries (length 0)
        fun make_words(l: IntArray, n: Int): IntArray? {
            val marker = IntArray(33)
            val r = IntArray(n)

            for (i in 0..n - 1) {
                val length = l[i]
                if (length > 0) {
                    var entry = marker[length]

                    // when we claim a node for an entry, we also claim the nodes
                    // below it (pruning off the imagined tree that may have dangled
                    // from it) as well as blocking the use of any nodes directly
                    // above for leaves

                    // update ourself
                    if (length < 32 && entry.ushr(length) != 0) {
                        // error condition; the lengths must specify an overpopulated tree
                        //free(r);
                        return null
                    }
                    r[i] = entry

                    // Look to see if the next shorter marker points to the node
                    // above. if so, update it and repeat.
                    run {
                        for (j in length downTo 1) {
                            if (marker[j] and 1 != 0) {
                                // have to jump branches
                                if (j == 1)
                                    marker[1]++
                                else
                                    marker[j] = marker[j - 1] shl 1
                                break // invariant says next upper marker would already
                                // have been moved if it was on the same path
                            }
                            marker[j]++
                        }
                    }

                    // prune the tree; the implicit invariant says all the longer
                    // markers were dangling from our just-taken node.  Dangle them
                    // from our *new* node.
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

            // bitreverse the words because our bitwise packer/unpacker is LSb
            // endian
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
