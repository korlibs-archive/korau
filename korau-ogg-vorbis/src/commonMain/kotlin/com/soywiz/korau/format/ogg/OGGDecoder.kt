package com.soywiz.korau.format.ogg

import com.soywiz.korau.format.*
import com.soywiz.korau.format.ogg.jcraft.*
import com.soywiz.korau.sound.*
import com.soywiz.korio.annotations.*
import com.soywiz.korio.async.*
import com.soywiz.korio.lang.*
import com.soywiz.korio.stream.*
import kotlin.math.*

@Keep
open class OGGDecoder : OggBase() {
    companion object : OGGDecoder()

    val BUFSIZE = 4096 * 2
    val MAX_CHANNELS = 6

    data class OutInfo(
        var rate: Int = 0,
        var channels: Int = 0
    )

    override suspend fun decodeStream(data: AsyncStream): AudioStream? {
        val info = OutInfo()
        val samplesStream = samplesStream(info, data.buffered()).iterator()
        val buffer = AudioSamplesDeque(MAX_CHANNELS)
        var reachedEnd = false

        suspend fun readChunk() {
            while (!reachedEnd && buffer.availableRead <= 0) {
                if (samplesStream.hasNext()) {
                    buffer.write(samplesStream.next())
                } else {
                    reachedEnd = true
                    break
                }
            }
        }

        // Needed to read header and determine rate and channels
        readChunk()

        return object : AudioStream(info.rate, info.channels) {
            override suspend fun read(out: AudioSamples, offset: Int, length: Int): Int {
                readChunk()
                return buffer.read(out, offset, length)
            }
        }
    }

    private suspend fun samplesStream(outInfo: OutInfo, data: AsyncStream) = produce<AudioSamples> {
        val oy = SyncState()
        val os = StreamState()
        val og = Page()
        val op = Packet()
        val vi = com.soywiz.korau.format.ogg.jcraft.Info()
        val vc = Comment()
        val vd = DspState()
        val vb = Block(vd)
        var eos = false
        try {

            var buffer: ByteArray? = null
            var bytes = 0

            oy.init()

            var index = oy.buffer(BUFSIZE)
            val rbytes = data.read(oy.data, index, BUFSIZE)
            oy.wrote(rbytes)

            if (oy.pageout(og) != 1) {
                invalidOp("Input does not appear to be an Ogg bitstream.")
            }

            os.init(og.serialno())
            os.reset()

            vi.init()
            vc.init()

            if (os.pagein(og) < 0) invalidOp("Error reading first page of Ogg bitstream data.")
            if (os.packetout(op) != 1) invalidOp("Error reading initial header packet.")
            if (vi.synthesis_headerin(vc, op) < 0) invalidOp("This Ogg bitstream does not contain Vorbis audio data.")

            var i = 0
            while (i < 2) {
                while (i < 2) {
                    var result = oy.pageout(og)
                    if (result == 0) break // Need more data
                    if (result == 1) {
                        os.pagein(og)
                        while (i < 2) {
                            result = os.packetout(op)
                            if (result == 0) break
                            if (result == -1) invalidOp("Corrupt secondary header.  Exiting.")
                            vi.synthesis_headerin(vc, op)
                            i++
                        }
                    }
                }

                if (i == 2) break

                index = oy.buffer(BUFSIZE)
                buffer = oy.data
                bytes = data.read(buffer, index, BUFSIZE)

                if (bytes == 0 && i < 2) invalidOp("End of file before finding all Vorbis headers!")
                oy.wrote(bytes)
            }

            val ptr = vc.user_comments
            val sb = StringBuilder()

            for (j in ptr!!.indices) {
                if (ptr[j] == null) break
                //System.err.println("Comment: " + String(ptr[j], 0, ptr[j].size - 1))
                //sb.append(" " + String(ptr[j]!!, 0, ptr[j]!!.size - 1))
                sb.append(" " + ptr[j]!!.copyOf(ptr[j]!!.size - 1).toString(UTF8))
            }
            //System.err.println("Bitstream is ${vi.channels} channel, ${vi.rate}Hz")
            //System.err.println("Encoded by: ${String(vc.vendor, 0, vc.vendor.size - 1)}\n")

            //acontext.showStatus(sb.toString())

            outInfo.rate = vi.rate
            outInfo.channels = vi.channels

            val convsize = BUFSIZE / vi.channels

            vd.synthesis_init(vi)
            vb.init(vd)

            val _pcmf = Array<Array<FloatArray>>(1) { arrayOf() }
            val _index = IntArray(vi.channels)

            var chained = false
            val BUFSIZE = 4096 * 2
            val convbuffer = ShortArray(convsize / 2)

            val deque = AudioSamplesDeque(MAX_CHANNELS)

            while (!eos) {
                while (!eos) {
                    var result = oy.pageout(og)
                    if (result == 0) break
                    if (result != -1) {
                        os.pagein(og)

                        if (og.granulepos() == 0L) {
                            chained = true
                            eos = true
                            break
                        }

                        while (true) {
                            result = os.packetout(op)
                            if (result == 0) break
                            if (result != -1) {
                                var samples: Int
                                // test for success!
                                if (vb.synthesis(op) == 0) vd.synthesis_blockin(vb)

                                while (true) {
                                    samples = vd.synthesis_pcmout(_pcmf, _index)
                                    if (samples <= 0) break
                                    val pcmf = _pcmf[0]
                                    val bout = min(samples, convsize)

                                    for (i in 0 until vi.channels) {
                                        deque.write(i, pcmf[i], _index[i], bout)
                                    }
                                    if (vi.channels * bout > 0) {
                                        //println(vi.channels * bout)
                                        val samples = AudioSamples(vi.channels, bout).also { deque.read(it) }
                                        channel.send(samples)
                                    }
                                    vd.synthesis_read(bout)
                                }
                            }
                        }
                        if (og.eos() != 0) eos = true
                    }
                }

                if (!eos) {
                    index = oy.buffer(BUFSIZE)
                    buffer = oy.data
                    try {
                        bytes = data.read(buffer, index, BUFSIZE)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        throw e
                    }

                    if (bytes == -1) break
                    oy.wrote(bytes)
                    if (bytes == 0) eos = true
                }
            }

        } catch (e: Throwable) {
            e.printStackTrace()
            throw e
        } finally {
            os.clear()
            vb.clear()
            vd.clear()
            vi.clear()
        }
    }
}

fun AudioFormats.registerOggVorbisDecoder(): AudioFormats = this.apply { register(OGGDecoder) }
