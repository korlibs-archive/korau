/*
 *      LAME MP3 encoding engine
 *
 *      Copyright (c) 1999 Mark Taylor
 *      Copyright (c) 2000-2002 Takehiro Tominaga
 *      Copyright (c) 2000-2005 Robert Hegemann
 *      Copyright (c) 2001 Gabriel Bouvigne
 *      Copyright (c) 2001 John Dahlstrom
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

/* $Id: Encoder.java,v 1.24 2012/03/12 18:38:59 kenchis Exp $ */
package net.sourceforge.lame.mp3;

public class Encoder {
    public static final int ENCDELAY = 576;
    public static final int POSTDELAY = 1152;
    public static final int MDCTDELAY = 48;
    public static final int SBMAX_l = 22;
    public static final int SBMAX_s = 13;
    public static final int PSFB21 = 6;
    public static final int PSFB12 = 6;
    public static final int MPG_MD_MS_LR = 2;
    BitStream bs;
    VBRTag vbr;

    public final void setModules(BitStream bs, VBRTag vbr) {
        this.bs = bs;
        this.vbr = vbr;
    }
}
