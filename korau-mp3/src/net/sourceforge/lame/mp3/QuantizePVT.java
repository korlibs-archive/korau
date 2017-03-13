/*
 *      quantize_pvt source file
 *
 *      Copyright (c) 1999-2002 Takehiro Tominaga
 *      Copyright (c) 2000-2002 Robert Hegemann
 *      Copyright (c) 2001 Naoki Shibata
 *      Copyright (c) 2002-2005 Gabriel Bouvigne
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

/* $Id: QuantizePVT.java,v 1.25 2012/03/12 15:58:57 kenchis Exp $ */
package net.sourceforge.lame.mp3;


public class QuantizePVT {

    public final ScaleFac sfBandIndex[] = {
            // Table B.2.b: 22.05 kHz
            new ScaleFac(new int[]{0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 116, 140, 168, 200, 238, 284, 336, 396, 464,
                    522, 576},
                    new int[]{0, 4, 8, 12, 18, 24, 32, 42, 56, 74, 100, 132, 174, 192}
                    , new int[]{0, 0, 0, 0, 0, 0, 0} //  sfb21 pseudo sub bands
                    , new int[]{0, 0, 0, 0, 0, 0, 0} //  sfb12 pseudo sub bands
            ),
                         /* Table B.2.c: 24 kHz */ /* docs: 332. mpg123(broken): 330 */
            new ScaleFac(new int[]{0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 114, 136, 162, 194, 232, 278, 332, 394, 464,
                    540, 576},
                    new int[]{0, 4, 8, 12, 18, 26, 36, 48, 62, 80, 104, 136, 180, 192}
                    , new int[]{0, 0, 0, 0, 0, 0, 0} /*  sfb21 pseudo sub bands */
                    , new int[]{0, 0, 0, 0, 0, 0, 0} /*  sfb12 pseudo sub bands */
            ),
                         /* Table B.2.a: 16 kHz */
            new ScaleFac(new int[]{0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 116, 140, 168, 200, 238, 284, 336, 396, 464,
                    522, 576},
                    new int[]{0, 4, 8, 12, 18, 26, 36, 48, 62, 80, 104, 134, 174, 192}
                    , new int[]{0, 0, 0, 0, 0, 0, 0} /*  sfb21 pseudo sub bands */
                    , new int[]{0, 0, 0, 0, 0, 0, 0} /*  sfb12 pseudo sub bands */
            ),
                         /* Table B.8.b: 44.1 kHz */
            new ScaleFac(new int[]{0, 4, 8, 12, 16, 20, 24, 30, 36, 44, 52, 62, 74, 90, 110, 134, 162, 196, 238, 288, 342, 418,
                    576},
                    new int[]{0, 4, 8, 12, 16, 22, 30, 40, 52, 66, 84, 106, 136, 192}
                    , new int[]{0, 0, 0, 0, 0, 0, 0} /*  sfb21 pseudo sub bands */
                    , new int[]{0, 0, 0, 0, 0, 0, 0} /*  sfb12 pseudo sub bands */
            ),
                         /* Table B.8.c: 48 kHz */
            new ScaleFac(new int[]{0, 4, 8, 12, 16, 20, 24, 30, 36, 42, 50, 60, 72, 88, 106, 128, 156, 190, 230, 276, 330, 384,
                    576},
                    new int[]{0, 4, 8, 12, 16, 22, 28, 38, 50, 64, 80, 100, 126, 192}
                    , new int[]{0, 0, 0, 0, 0, 0, 0} /*  sfb21 pseudo sub bands */
                    , new int[]{0, 0, 0, 0, 0, 0, 0} /*  sfb12 pseudo sub bands */
            ),
                         /* Table B.8.a: 32 kHz */
            new ScaleFac(new int[]{0, 4, 8, 12, 16, 20, 24, 30, 36, 44, 54, 66, 82, 102, 126, 156, 194, 240, 296, 364, 448, 550,
                    576},
                    new int[]{0, 4, 8, 12, 16, 22, 30, 42, 58, 78, 104, 138, 180, 192}
                    , new int[]{0, 0, 0, 0, 0, 0, 0} /*  sfb21 pseudo sub bands */
                    , new int[]{0, 0, 0, 0, 0, 0, 0} /*  sfb12 pseudo sub bands */
            ),
                         /* MPEG-2.5 11.025 kHz */
            new ScaleFac(new int[]{0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 116, 140, 168, 200, 238, 284, 336, 396, 464,
                    522, 576},
                    new int[]{0 / 3, 12 / 3, 24 / 3, 36 / 3, 54 / 3, 78 / 3, 108 / 3, 144 / 3, 186 / 3, 240 / 3, 312 / 3,
                            402 / 3, 522 / 3, 576 / 3}
                    , new int[]{0, 0, 0, 0, 0, 0, 0} /*  sfb21 pseudo sub bands */
                    , new int[]{0, 0, 0, 0, 0, 0, 0} /*  sfb12 pseudo sub bands */
            ),
                         /* MPEG-2.5 12 kHz */
            new ScaleFac(new int[]{0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 116, 140, 168, 200, 238, 284, 336, 396, 464,
                    522, 576},
                    new int[]{0 / 3, 12 / 3, 24 / 3, 36 / 3, 54 / 3, 78 / 3, 108 / 3, 144 / 3, 186 / 3, 240 / 3, 312 / 3,
                            402 / 3, 522 / 3, 576 / 3}
                    , new int[]{0, 0, 0, 0, 0, 0, 0} /*  sfb21 pseudo sub bands */
                    , new int[]{0, 0, 0, 0, 0, 0, 0} /*  sfb12 pseudo sub bands */
            ),
                         /* MPEG-2.5 8 kHz */
            new ScaleFac(new int[]{0, 12, 24, 36, 48, 60, 72, 88, 108, 132, 160, 192, 232, 280, 336, 400, 476, 566, 568, 570,
                    572, 574, 576},
                    new int[]{0 / 3, 24 / 3, 48 / 3, 72 / 3, 108 / 3, 156 / 3, 216 / 3, 288 / 3, 372 / 3, 480 / 3, 486 / 3,
                            492 / 3, 498 / 3, 576 / 3}
                    , new int[]{0, 0, 0, 0, 0, 0, 0} /*  sfb21 pseudo sub bands */
                    , new int[]{0, 0, 0, 0, 0, 0, 0} /*  sfb12 pseudo sub bands */
            )
    };

}
