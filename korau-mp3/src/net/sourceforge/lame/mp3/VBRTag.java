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

@SuppressWarnings("PointlessArithmeticExpression")
public class VBRTag {
    public static final int NUMTOCENTRIES = 100;
    private static final int FRAMES_FLAG = 0x0001;
    private static final int BYTES_FLAG = 0x0002;
    private static final int TOC_FLAG = 0x0004;
    private static final int VBR_SCALE_FLAG = 0x0008;
    private static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    private static final String VBRTag0 = "Xing";
    private static final String VBRTag1 = "Info";

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

    private boolean isVbrTag(final byte[] buf, final int bufPos) {
        return new String(buf, bufPos, VBRTag0.length(), ISO_8859_1).equals(VBRTag0) || new String(buf, bufPos, VBRTag1.length(), ISO_8859_1).equals(VBRTag1);
    }

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
        pTagData.samprate = ((buf[bufPos + 1] >> 4) == 0xE) ? Tables.samplerate_table[2][hSrIndex] : Tables.samplerate_table[hId][hSrIndex];

        if (hId != 0) {
            bufPos += (hMode != 3) ? (32 + 4) : (17 + 4); // mpeg1
        } else {
            bufPos += (hMode != 3) ? (17 + 4) : (9 + 4); // mpeg2
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
                System.arraycopy(buf, bufPos + 0, pTagData.toc, 0, NUMTOCENTRIES);
            }
            bufPos += NUMTOCENTRIES;
        }

        pTagData.vbrScale = -1;

        if ((head_flags & VBR_SCALE_FLAG) != 0) {
            pTagData.vbrScale = extractInteger(buf, bufPos);
            bufPos += 4;
        }

        pTagData.headersize = ((hId + 1) * 72000 * hBitrate) / pTagData.samprate;

        bufPos += 21;
        int encDelay = buf[bufPos + 0] << 4;
        encDelay += buf[bufPos + 1] >> 4;
        int encPadding = (buf[bufPos + 1] & 0x0F) << 8;
        encPadding += buf[bufPos + 2] & 0xff;
        if (encDelay < 0 || encDelay > 3000) encDelay = -1;
        if (encPadding < 0 || encPadding > 3000) encPadding = -1;

        pTagData.encDelay = encDelay;
        pTagData.encPadding = encPadding;

        return pTagData;
    }
}
