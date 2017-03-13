/*
 *      Xing VBR tagging for LAME.
 *
 *      Copyright (c) 1999 A.L. Faber
 *      Copyright (c) 2001 Jonathan Dee
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
package net.sourceforge.lame.mp3;

import java.nio.charset.Charset;

/**
 * A Vbr header may be present in the ancillary data field of the first frame of
 * an mp3 bitstream<BR>
 * The Vbr header (optionally) contains
 * <UL>
 * <LI>frames total number of audio frames in the bitstream
 * <LI>bytes total number of bytes in the bitstream
 * <LI>toc table of contents
 * </UL>
 * <p/>
 * toc (table of contents) gives seek points for random access.<BR>
 * The ith entry determines the seek point for i-percent duration.<BR>
 * seek point in bytes = (toc[i]/256.0) * total_bitstream_bytes<BR>
 * e.g. half duration seek point = (toc[50]/256.0) * total_bitstream_bytes
 */
public class VBRTag {
    public static final int NUMTOCENTRIES = 100;
    private static final int VBRHEADERSIZE = (NUMTOCENTRIES + 4 + 4 + 4 + 4 + 4);
    private static final int LAMEHEADERSIZE = (VBRHEADERSIZE + 9 + 1 + 1 + 8 + 1 + 1 + 3 + 1 + 1 + 2 + 4 + 2 + 2);
    public static final int MAXFRAMESIZE = 2880;
    private static final int FRAMES_FLAG = 0x0001;
    private static final int BYTES_FLAG = 0x0002;
    private static final int TOC_FLAG = 0x0004;
    private static final int VBR_SCALE_FLAG = 0x0008;
    private static final int XING_BITRATE1 = 128;
    private static final int XING_BITRATE2 = 64;
    private static final int XING_BITRATE25 = 32;
    private static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    private static final String VBRTag0 = "Xing";
    private static final String VBRTag1 = "Info";

    Lame lame;
    BitStream bs;

    public final void setModules(Lame lame, BitStream bs) {
        this.lame = lame;
        this.bs = bs;
    }

    /**
     * Read big endian integer (4-bytes) from header.
     *
     * @param buf    header containing the integer
     * @param bufPos offset into the header
     * @return extracted integer
     */
    private int extractInteger(final byte[] buf, final int bufPos) {
        int x = buf[bufPos + 0] & 0xff;
        x <<= 8;
        x |= buf[bufPos + 1] & 0xff;
        x <<= 8;
        x |= buf[bufPos + 2] & 0xff;
        x <<= 8;
        x |= buf[bufPos + 3] & 0xff;
        return x;
    }

    /**
     * Check for magic strings (Xing/Info).
     *
     * @param buf    header to check
     * @param bufPos header offset to check
     * @return magic string found
     */
    private boolean isVbrTag(final byte[] buf, final int bufPos) {
        return new String(buf, bufPos, VBRTag0.length(), ISO_8859_1)
                .equals(VBRTag0)
                || new String(buf, bufPos, VBRTag1.length(), ISO_8859_1)
                .equals(VBRTag1);
    }

    private byte shiftInBitsValue(final byte x, final int n, final int v) {
        return (byte) ((x << n) | (v & ~(-1 << n)));
    }

    /**
     * Construct the MP3 header using the settings of the global flags.
     * <p/>
     * <img src="1000px-Mp3filestructure.svg.png">
     *
     * @param gfp    global flags
     * @param buffer header
     */
    private void setLameTagFrameHeader(final LameGlobalFlags gfp,
                                       final byte[] buffer) {
        final LameInternalFlags gfc = gfp.internal_flags;

        // MP3 Sync Word
        buffer[0] = shiftInBitsValue(buffer[0], 8, 0xff);

        buffer[1] = shiftInBitsValue(buffer[1], 3, 7);
        buffer[1] = shiftInBitsValue(buffer[1], 1,
                (gfp.getOutSampleRate() < 16000) ? 0 : 1);
        // Version
        buffer[1] = shiftInBitsValue(buffer[1], 1, gfp.getMpegVersion());
        // 01 == Layer 3
        buffer[1] = shiftInBitsValue(buffer[1], 2, 4 - 3);
        // Error protection
        buffer[1] = shiftInBitsValue(buffer[1], 1, (!gfp.error_protection) ? 1
                : 0);

        // Bit rate
        buffer[2] = shiftInBitsValue(buffer[2], 4, gfc.bitrate_index);
        // Frequency
        buffer[2] = shiftInBitsValue(buffer[2], 2, gfc.samplerate_index);
        // Pad. Bit
        buffer[2] = shiftInBitsValue(buffer[2], 1, 0);
        // Priv. Bit
        buffer[2] = shiftInBitsValue(buffer[2], 1, gfp.extension);

        // Mode
        buffer[3] = shiftInBitsValue(buffer[3], 2, gfp.getMode().getNumMode());
        // Mode extension (Used with Joint Stereo)
        buffer[3] = shiftInBitsValue(buffer[3], 2, gfc.mode_ext);
        // Copy
        buffer[3] = shiftInBitsValue(buffer[3], 1, gfp.copyright);
        // Original
        buffer[3] = shiftInBitsValue(buffer[3], 1, gfp.original);
        // Emphasis
        buffer[3] = shiftInBitsValue(buffer[3], 2, gfp.emphasis);

		/* the default VBR header. 48 kbps layer III, no padding, no crc */
    /* but sampling freq, mode and copyright/copy protection taken */
        /* from first valid frame */
        buffer[0] = (byte) 0xff;
        byte abyte = (byte) (buffer[1] & 0xf1);

        int bitrate;
        if (1 == gfp.getMpegVersion()) {
            bitrate = XING_BITRATE1;
        } else {
            if (gfp.getOutSampleRate() < 16000)
                bitrate = XING_BITRATE25;
            else
                bitrate = XING_BITRATE2;
        }

        if (gfp.getVBR() == VbrMode.vbr_off)
            bitrate = gfp.getBitRate();

        byte bbyte;
        if (gfp.free_format)
            bbyte = 0x00;
        else
            bbyte = (byte) (16 * lame.BitrateIndex(bitrate, gfp.getMpegVersion(),
                    gfp.getOutSampleRate()));

		/*
         * Use as much of the info from the real frames in the Xing header:
		 * samplerate, channels, crc, etc...
		 */
        if (gfp.getMpegVersion() == 1) {
            /* MPEG1 */
            buffer[1] = (byte) (abyte | 0x0a); /* was 0x0b; */
            abyte = (byte) (buffer[2] & 0x0d); /* AF keep also private bit */
            buffer[2] = (byte) (bbyte | abyte); /* 64kbs MPEG1 frame */
        } else {
			/* MPEG2 */
            buffer[1] = (byte) (abyte | 0x02); /* was 0x03; */
            abyte = (byte) (buffer[2] & 0x0d); /* AF keep also private bit */
            buffer[2] = (byte) (bbyte | abyte); /* 64kbs MPEG2 frame */
        }
    }

    /**
     * Get VBR tag information
     *
     * @param buf header to analyze
     * @return VBR tag data
     */
    public final VBRTagData getVbrTag(final byte[] buf) {
        final VBRTagData pTagData = new VBRTagData();
        int bufPos = 0;
		
		/* get Vbr header data */
        pTagData.flags = 0;

		/* get selected MPEG header data */
        int hId = (buf[bufPos + 1] >> 3) & 1;
        int hSrIndex = (buf[bufPos + 2] >> 2) & 3;
        int hMode = (buf[bufPos + 3] >> 6) & 3;
        int hBitrate = ((buf[bufPos + 2] >> 4) & 0xf);
        hBitrate = Tables.bitrate_table[hId][hBitrate];

		/* check for FFE syncword */
        if ((buf[bufPos + 1] >> 4) == 0xE)
            pTagData.samprate = Tables.samplerate_table[2][hSrIndex];
        else
            pTagData.samprate = Tables.samplerate_table[hId][hSrIndex];

		/* determine offset of header */
        if (hId != 0) {
			/* mpeg1 */
            if (hMode != 3)
                bufPos += (32 + 4);
            else
                bufPos += (17 + 4);
        } else {
			/* mpeg2 */
            if (hMode != 3)
                bufPos += (17 + 4);
            else
                bufPos += (9 + 4);
        }

        if (!isVbrTag(buf, bufPos))
            return null;

        bufPos += 4;

        pTagData.hId = hId;

		/* get flags */
        int head_flags = pTagData.flags = extractInteger(buf, bufPos);
        bufPos += 4;

        if ((head_flags & FRAMES_FLAG) != 0) {
            pTagData.frames = extractInteger(buf, bufPos);
            bufPos += 4;
        }

        if ((head_flags & BYTES_FLAG) != 0) {
            pTagData.bytes = extractInteger(buf, bufPos);
            bufPos += 4;
        }

        if ((head_flags & TOC_FLAG) != 0) {
            if (pTagData.toc != null) {
                for (int i = 0; i < NUMTOCENTRIES; i++)
                    pTagData.toc[i] = buf[bufPos + i];
            }
            bufPos += NUMTOCENTRIES;
        }

        pTagData.vbrScale = -1;

        if ((head_flags & VBR_SCALE_FLAG) != 0) {
            pTagData.vbrScale = extractInteger(buf, bufPos);
            bufPos += 4;
        }

        pTagData.headersize = ((hId + 1) * 72000 * hBitrate)
                / pTagData.samprate;

        bufPos += 21;
        int encDelay = buf[bufPos + 0] << 4;
        encDelay += buf[bufPos + 1] >> 4;
        int encPadding = (buf[bufPos + 1] & 0x0F) << 8;
        encPadding += buf[bufPos + 2] & 0xff;
		/* check for reasonable values (this may be an old Xing header, */
		/* not a INFO tag) */
        if (encDelay < 0 || encDelay > 3000)
            encDelay = -1;
        if (encPadding < 0 || encPadding > 3000)
            encPadding = -1;

        pTagData.encDelay = encDelay;
        pTagData.encPadding = encPadding;

		/* success */
        return pTagData;
    }

    /**
     * Initializes the header
     *
     * @param gfp global flags
     */
    public final void InitVbrTag(final LameGlobalFlags gfp) {
        final LameInternalFlags gfc = gfp.internal_flags;

        /**
         * <PRE>
         * Xing VBR pretends to be a 48kbs layer III frame.  (at 44.1kHz).
         * (at 48kHz they use 56kbs since 48kbs frame not big enough for
         * table of contents)
         * let's always embed Xing header inside a 64kbs layer III frame.
         * this gives us enough room for a LAME version string too.
         * size determined by sampling frequency (MPEG1)
         * 32kHz:    216 bytes@48kbs    288bytes@ 64kbs
         * 44.1kHz:  156 bytes          208bytes@64kbs     (+1 if padding = 1)
         * 48kHz:    144 bytes          192
         *
         * MPEG 2 values are the same since the framesize and samplerate
         * are each reduced by a factor of 2.
         * </PRE>
         */
        int kbps_header;
        if (1 == gfp.getMpegVersion()) {
            kbps_header = XING_BITRATE1;
        } else {
            if (gfp.getOutSampleRate() < 16000)
                kbps_header = XING_BITRATE25;
            else
                kbps_header = XING_BITRATE2;
        }

        if (gfp.getVBR() == VbrMode.vbr_off)
            kbps_header = gfp.getBitRate();

        // make sure LAME Header fits into Frame
        int totalFrameSize = ((gfp.getMpegVersion() + 1) * 72000 * kbps_header)
                / gfp.getOutSampleRate();
        int headerSize = (gfc.sideinfo_len + LAMEHEADERSIZE);
        gfc.VBR_seek_table.TotalFrameSize = totalFrameSize;
        if (totalFrameSize < headerSize || totalFrameSize > MAXFRAMESIZE) {
			/* disable tag, it wont fit */
            gfp.bWriteVbrTag = false;
            return;
        }

        gfc.VBR_seek_table.nVbrNumFrames = 0;
        gfc.VBR_seek_table.nBytesWritten = 0;
        gfc.VBR_seek_table.sum = 0;

        gfc.VBR_seek_table.seen = 0;
        gfc.VBR_seek_table.want = 1;
        gfc.VBR_seek_table.pos = 0;

        if (gfc.VBR_seek_table.bag == null) {
            gfc.VBR_seek_table.bag = new int[400];
            gfc.VBR_seek_table.size = 400;
        }

        // write dummy VBR tag of all 0's into bitstream
        byte buffer[] = new byte[MAXFRAMESIZE];

        setLameTagFrameHeader(gfp, buffer);
        int n = gfc.VBR_seek_table.TotalFrameSize;
        for (int i = 0; i < n; ++i) {
            bs.add_dummy_byte(gfp, buffer[i] & 0xff, 1);
        }
    }

}
