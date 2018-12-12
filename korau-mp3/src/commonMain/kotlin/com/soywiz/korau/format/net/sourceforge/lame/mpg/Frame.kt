package com.soywiz.korau.format.net.sourceforge.lame.mpg

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