package com.soywiz.korau.format.com.jcraft.jorbis.internal

import kotlin.math.*

internal fun rint(v: Double): Double = if (v >= floor(v) + 0.5) ceil(v) else round(v)  // @TODO: Is this right?
