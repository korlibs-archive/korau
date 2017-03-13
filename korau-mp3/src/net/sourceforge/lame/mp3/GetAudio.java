/*
 *	Get Audio routines source file
 *
 *	Copyright (c) 1999 Albert L Faber
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	 See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

/* $Id: GetAudio.java,v 1.30 2012/03/12 18:38:59 kenchis Exp $ */

package net.sourceforge.lame.mp3;

import net.sourceforge.lame.mpg.MPGLib;
import net.sourceforge.lame.util.RandomReader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

@SuppressWarnings("PointlessArithmeticExpression")
public class GetAudio {
    private static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    private static final char abl2[] = {0, 7, 7, 7, 0, 7, 0, 0, 0, 0, 0, 8, 8, 8, 8, 8};
    Parse parse;
    MPGLib mpg;

    /* AIFF Definitions */
    private RandomReader musicin;
    private MPGLib.mpstr_tag hip;

    public GetAudio(Parse parse, MPGLib mpg) {
        this.parse = parse;
        this.mpg = mpg;
    }

    public final void initInFile(final LameGlobalFlags gfp, final RandomReader inPath, final FrameSkip enc) {
        try {
            musicin = OpenSndFile(gfp, inPath, enc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final int get_audio16(final LameGlobalFlags gfp, final float buffer[][]) {
        return (get_audio_common(gfp, null, buffer));
    }

    private int get_audio_common(final LameGlobalFlags gfp, final float buffer[][], final float buffer16[][]) {
        int num_channels = gfp.getInNumChannels();
        float buf_tmp16[][] = new float[2][1152];
        int samples_read;

        samples_read = read_samples_mp3(gfp, musicin, (buffer != null) ? buf_tmp16 : buffer16);
        if (samples_read < 0) return samples_read;

        if (buffer != null) {
            for (int i = samples_read; --i >= 0; ) {
                int value = (int) (buf_tmp16[0][i]);
                buffer[0][i] = value << 16;
            }
            if (num_channels == 2) {
                for (int i = samples_read; --i >= 0; ) {
                    int value = (int) (buf_tmp16[1][i]);
                    buffer[1][i] = value << 16;
                }
            } else if (num_channels == 1) {
                Arrays.fill(buffer[1], 0, samples_read, 0);
            } else {
                throw new RuntimeException("Channels must be 1 or 2");
            }
        }

        return samples_read;
    }

    int read_samples_mp3(final LameGlobalFlags gfp, RandomReader musicin, float mpg123pcm[][]) {
        int out;

        out = lame_decode_fromfile(musicin, mpg123pcm[0], mpg123pcm[1], parse.getMp3InputData());

        if (out < 0) {
            Arrays.fill(mpg123pcm[0], (short) 0);
            Arrays.fill(mpg123pcm[1], (short) 0);
            return 0;
        }

        if (gfp.getInNumChannels() != parse.getMp3InputData().getStereo()) {
            throw new RuntimeException("number of channels has changed");
        }
        if (gfp.getInSampleRate() != parse.getMp3InputData().getSamplerate()) {
            throw new RuntimeException("sample frequency has changed");
        }
        return out;
    }

    private RandomReader OpenSndFile(
            final LameGlobalFlags gfp,
            final RandomReader musicin2, final FrameSkip enc
    ) throws IOException {

		/* set the defaults from info in case we cannot determine them from file */
        gfp.setNum_samples(-1);

        musicin = musicin2;

        if (-1 == lame_decode_initfile(musicin, parse.getMp3InputData(), enc)) {
            throw new RuntimeException(String.format("Error reading headers in mp3 input file %s.", musicin2));
        }
        gfp.setInNumChannels(parse.getMp3InputData().getStereo());
        gfp.setInSampleRate(parse.getMp3InputData().getSamplerate());
        gfp.setNum_samples(parse.getMp3InputData().getNumSamples());

        if (gfp.getNum_samples() == -1) {

            double flen = musicin2.length();
            if (flen >= 0) {
                if (parse.getMp3InputData().getBitrate() > 0) {
                    double totalseconds = (flen * 8.0 / (1000.0 * parse.getMp3InputData().getBitrate()));
                    int tmp_num_samples = (int) (totalseconds * gfp.getInSampleRate());

                    gfp.setNum_samples(tmp_num_samples);
                    parse.getMp3InputData().setNumSamples(tmp_num_samples);
                }
            }
        }
        return musicin;
    }

    private boolean check_aid(final byte[] header) {
        return new String(header, ISO_8859_1).startsWith("AiD\1");
    }

    private boolean is_syncword_mp123(final byte[] headerptr) {
        int p = 0;

        if ((headerptr[p + 0] & 0xFF) != 0xFF) return false; /* first 8 bits must be '1' */
        if ((headerptr[p + 1] & 0xE0) != 0xE0) return false; /* next 3 bits are also */
        if ((headerptr[p + 1] & 0x18) == 0x08) return false; /* no MPEG-1, -2 or -2.5 */

        switch (headerptr[p + 1] & 0x06) {
            default:
            case 0x00:
            /* illegal Layer */
                return false;

            case 0x02:
            /* Layer3 */
                if (parse.getInputFormat() != SoundFileFormat.sf_mp3 && parse.getInputFormat() != SoundFileFormat.sf_mp123) {
                    return false;
                }
                parse.setInputFormat(SoundFileFormat.sf_mp3);
                break;

            case 0x04:
            /* Layer2 */
                if (parse.getInputFormat() != SoundFileFormat.sf_mp2 && parse.getInputFormat() != SoundFileFormat.sf_mp123) {
                    return false;
                }
                parse.setInputFormat(SoundFileFormat.sf_mp2);
                break;

            case 0x06:
            /* Layer1 */
                if (parse.getInputFormat() != SoundFileFormat.sf_mp1 && parse.getInputFormat() != SoundFileFormat.sf_mp123) {
                    return false;
                }
                parse.setInputFormat(SoundFileFormat.sf_mp1);
                break;
        }
        if ((headerptr[p + 1] & 0x06) == 0x00) return false; /* no Layer I, II and III */
        if ((headerptr[p + 2] & 0xF0) == 0xF0) return false; /* bad bitrate */
        if ((headerptr[p + 2] & 0x0C) == 0x0C) return false; /* no sample frequency with (32,44.1,48)/(1,2,4) */
        if ((headerptr[p + 1] & 0x18) == 0x18 && (headerptr[p + 1] & 0x06) == 0x04 && (abl2[(headerptr[p + 2] & 0xff) >> 4] & (1 << ((headerptr[p + 3] & 0xff) >> 6))) != 0)
            return false;
        if ((headerptr[p + 3] & 3) == 2) return false; /* reserved enphasis mode */
        return true;
    }

    private int lame_decode_initfile(final RandomReader fd, final MP3Data mp3data, final FrameSkip enc) {
        byte buf[] = new byte[100];
        float[] pcm_l = new float[1152], pcm_r = new float[1152];
        boolean freeformat = false;

        if (hip != null) mpg.hip_decode_exit(hip);
        hip = mpg.hip_decode_init();

        int len = 4;
        try {
            fd.readFully(buf, 0, len);
        } catch (IOException e) {
            e.printStackTrace();
            return -1; /* failed */
        }
        if (buf[0] == 'I' && buf[1] == 'D' && buf[2] == '3') {
            //System.out.println("ID3v2 found. Be aware that the ID3 tag is currently lost when transcoding.");
            len = 6;
            try {
                fd.readFully(buf, 0, len);
            } catch (IOException e) {
                e.printStackTrace();
                return -1; /* failed */
            }
            buf[2] &= 127;
            buf[3] &= 127;
            buf[4] &= 127;
            buf[5] &= 127;
            len = (((((buf[2] << 7) + buf[3]) << 7) + buf[4]) << 7) + buf[5];
            try {
                fd.skipBytes(len);
            } catch (IOException e) {
                e.printStackTrace();
                return -1; /* failed */
            }
            len = 4;
            try {
                fd.readFully(buf, 0, len);
            } catch (IOException e) {
                e.printStackTrace();
                return -1; /* failed */
            }
        }
        if (check_aid(buf)) {
            try {
                fd.readFully(buf, 0, 2);
            } catch (IOException e) {
                e.printStackTrace();
                return -1; /* failed */
            }
            int aid_header = (buf[0] & 0xff) + 256 * (buf[1] & 0xff);
            //System.out.printf("Album ID found.  length=%d \n", aid_header);
            /* skip rest of AID, except for 6 bytes we have already read */
            try {
                fd.skipBytes(aid_header - 6);
            } catch (IOException e) {
                e.printStackTrace();
                return -1; /* failed */
            }

			/* read 4 more bytes to set up buffer for MP3 header check */
            try {
                fd.readFully(buf, 0, len);
            } catch (IOException e) {
                e.printStackTrace();
                return -1; /* failed */
            }
        }
        len = 4;
        while (!is_syncword_mp123(buf)) {
            int i;
            for (i = 0; i < len - 1; i++)
                buf[i] = buf[i + 1];
            try {
                fd.readFully(buf, len - 1, 1);
            } catch (IOException e) {
                e.printStackTrace();
                return -1; /* failed */
            }
        }

        if ((buf[2] & 0xf0) == 0) {
            //System.out.println("Input file is freeformat.");
            freeformat = true;
        }

        int ret = mpg.hip_decode1_headers(hip, buf, len, pcm_l, pcm_r, mp3data, enc);
        if (ret == -1) return -1;

        while (!mp3data.getHeader_parsed()) {
            try {
                fd.readFully(buf);
            } catch (IOException e) {
                e.printStackTrace();
                return -1; /* failed */
            }
            ret = mpg.hip_decode1_headers(hip, buf, buf.length, pcm_l, pcm_r, mp3data, enc);
            if (ret == -1) return -1;
        }

        if (mp3data.getBitrate() == 0 && !freeformat) return lame_decode_initfile(fd, mp3data, enc);
        if (mp3data.getTotalFrames() <= 0) mp3data.setNumSamples(-1);

        return 0;
    }

    private int lame_decode_fromfile(final RandomReader fd, final float[] pcm_l, final float[] pcm_r, final MP3Data mp3data) {
        int len = 0;
        byte buf[] = new byte[1024];

		/* first see if we still have data buffered in the decoder: */
        int ret = mpg.hip_decode1_headers(hip, buf, len, pcm_l, pcm_r, mp3data, new FrameSkip());
        if (ret != 0) return ret;

		/* read until we get a valid output frame */
        while (true) {
            try {
                len = fd.read(buf, 0, 1024);
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
            if (len <= 0) {
                /* we are done reading the file, but check for buffered data */
                ret = mpg.hip_decode1_headers(hip, buf, 0, pcm_l, pcm_r,
                        mp3data, new FrameSkip());
                if (ret <= 0) {
                    mpg.hip_decode_exit(hip);
					/* release mp3decoder memory */
                    hip = null;
                    return -1; /* done with file */
                }
                break;
            }

            ret = mpg.hip_decode1_headers(hip, buf, len, pcm_l, pcm_r, mp3data, new FrameSkip());
            if (ret == -1) {
                mpg.hip_decode_exit(hip);
				/* release mp3decoder memory */
                hip = null;
                return -1;
            }
            if (ret > 0)
                break;
        }
        return ret;
    }

    // Rest of portableio.c:

    public enum SoundFileFormat {
        sf_mp1, sf_mp2, sf_mp3, sf_mp123
    }
}
