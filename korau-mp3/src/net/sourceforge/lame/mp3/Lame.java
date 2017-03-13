/*
 *      LAME MP3 encoding engine
 *
 *      Copyright (c) 1999-2000 Mark Taylor
 *      Copyright (c) 2000-2005 Takehiro Tominaga
 *      Copyright (c) 2000-2005 Robert Hegemann
 *      Copyright (c) 2000-2005 Gabriel Bouvigne
 *      Copyright (c) 2000-2004 Alexander Leidinger
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

/* $Id: Lame.java,v 1.44 2012/03/23 10:02:29 kenchis Exp $ */

package net.sourceforge.lame.mp3;

import net.sourceforge.lame.mpg.Interface;
import net.sourceforge.lame.mpg.MPGLib;

public class Lame {
    private LameGlobalFlags gfp;
    private VBRTag vbr;
    private GetAudio gaud;
    private Parse parse;
    private MPGLib mpg;
    private Interface intf;

    public Lame() {
        gfp = new LameGlobalFlags();
        gaud = new GetAudio();
        vbr = new VBRTag();
        parse = new Parse();

        mpg = new MPGLib();
        intf = new Interface();

        gaud.setModules(parse, mpg);
        mpg.setModules(intf);
        intf.setModules(vbr);

        lame_init();
    }

    public LameGlobalFlags getFlags() {
        return gfp;
    }

    public Parse getParser() {
        return parse;
    }

    public GetAudio getAudio() {
        return gaud;
    }

    public VBRTag getVbr() {
        return vbr;
    }

    private final void lame_init() {
        gfp.setInSampleRate(44100);
        gfp.setInNumChannels(2);
        gfp.num_samples = -1;
    }
}
