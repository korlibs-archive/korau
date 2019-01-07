package com.soywiz.korau.internal

import com.soywiz.kmem.*

internal fun List<ShortArray>.combine(): ShortArray {
    val combined = ShortArray(this.sumBy { it.size })
    var pos = 0
    for (buffer in this) {
        arraycopy(buffer, 0, combined, pos, buffer.size)
        pos += size
    }
    return combined
}

