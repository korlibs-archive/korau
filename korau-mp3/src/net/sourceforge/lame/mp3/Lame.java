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
    // low mean bitrate in VBR mode
    public static final int QUALITY_LOWEST = 9;
    public static final int QUALITY_LOW = 7;
    public static final int QUALITY_MIDDLE_LOW = 6;
    public static final int QUALITY_MIDDLE = 5;
    public static final int QUALITY_HIGH = 2;
    // quality==0 not yet coded in LAME (3.83alpha)
    // high mean bitrate in VBR // mode
    public static final int QUALITY_HIGHEST = 1;
    public static final long LAME_ID = 0xFFF88E3B;
    public static final int V9 = 410;
    public static final int V8 = 420;
    public static final int V7 = 430;
    public static final int V6 = 440;
    public static final int V5 = 450;
    public static final int V4 = 460;
    public static final int V3 = 470;
    public static final int V2 = 480;
    public static final int V1 = 490;
    public static final int V0 = 500;
    public static final int R3MIX = 1000;
    public static final int STANDARD = 1001;
    public static final int EXTREME = 1002;
    public static final int INSANE = 1003;
    public static final int STANDARD_FAST = 1004;
    public static final int EXTREME_FAST = 1005;
    public static final int MEDIUM = 1006;
    public static final int MEDIUM_FAST = 1007;
    /**
     * maximum size of albumart image (128KB), which affects LAME_MAXMP3BUFFER
     * as well since lame_encode_buffer() also returns ID3v2 tag data
     */
    static final int LAME_MAXALBUMART = (128 * 1024);
    /**
     * maximum size of mp3buffer needed if you encode at most 1152 samples for
     * each call to lame_encode_buffer. see lame_encode_buffer() below
     * (LAME_MAXMP3BUFFER is now obsolete)
     */
    public static final int LAME_MAXMP3BUFFER = (16384 + LAME_MAXALBUMART);
    private static final int QUALITY_DEFAULT = 3;
    private LameGlobalFlags gfp;
    private GainAnalysis ga;
    private BitStream bs;
    private Presets p;
    private QuantizePVT qupvt;

    /* presets */
  /* values from 8 to 320 should be reserved for abr bitrates */
    /* for abr I'd suggest to directly use the targeted bitrate as a value */
    private VBRTag vbr;
    private ID3Tag id3;
    private MPGLib mpglib;
    private Encoder enc;
    private GetAudio gaud;
    private Parse parse;

    /* still there for compatibility */
    private MPGLib mpg;
    private Interface intf;

    public Lame() {
        gfp = new LameGlobalFlags();
        gaud = new GetAudio();
        ga = new GainAnalysis();
        bs = new BitStream();
        p = new Presets();
        qupvt = new QuantizePVT();
        vbr = new VBRTag();
        id3 = new ID3Tag();
        parse = new Parse();
        enc = new Encoder();

        mpg = new MPGLib();
        intf = new Interface();

        enc.setModules(bs, qupvt, vbr);
        bs.setModules(ga, mpg, vbr);
        id3.setModules(bs);
        p.setModules(this);
        vbr.setModules(this, bs);
        gaud.setModules(parse, mpg);
        parse.setModules(id3, p);

        // decoder modules
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

    public ID3Tag getId3() {
        return id3;
    }

    private float filter_coef(final float x) {
        if (x > 1.0)
            return 0.0f;
        if (x <= 0.0)
            return 1.0f;

        return (float) Math.cos(Math.PI / 2 * x);
    }

    private void lame_init_params_ppflt() {
        final LameInternalFlags gfc = gfp.internal_flags;
        /***************************************************************/
        /* compute info needed for polyphase filter (filter type==0, default) */
        /***************************************************************/

        int lowpass_band = 32;
        int highpass_band = -1;

        if (gfc.lowpass1 > 0) {
            int minband = 999;
            for (int band = 0; band <= 31; band++) {
                float freq = (float) (band / 31.0);
                /* this band and above will be zeroed: */
                if (freq >= gfc.lowpass2) {
                    lowpass_band = Math.min(lowpass_band, band);
                }
                if (gfc.lowpass1 < freq && freq < gfc.lowpass2) {
                    minband = Math.min(minband, band);
                }
            }

			/*
             * compute the *actual* transition band implemented by the polyphase
			 * filter
			 */
            if (minband == 999) {
                gfc.lowpass1 = (lowpass_band - .75f) / 31.0f;
            } else {
                gfc.lowpass1 = (minband - .75f) / 31.0f;
            }
            gfc.lowpass2 = lowpass_band / 31.0f;
        }

		/*
         * make sure highpass filter is within 90% of what the effective
		 * highpass frequency will be
		 */
        if (gfc.highpass2 > 0) {
            if (gfc.highpass2 < .9 * (.75 / 31.0)) {
                gfc.highpass1 = 0;
                gfc.highpass2 = 0;
                System.err.println("Warning: highpass filter disabled.  "
                        + "highpass frequency too small\n");
            }
        }

        if (gfc.highpass2 > 0) {
            int maxband = -1;
            for (int band = 0; band <= 31; band++) {
                float freq = band / 31.0f;
                /* this band and below will be zereod */
                if (freq <= gfc.highpass1) {
                    highpass_band = Math.max(highpass_band, band);
                }
                if (gfc.highpass1 < freq && freq < gfc.highpass2) {
                    maxband = Math.max(maxband, band);
                }
            }
			/*
			 * compute the *actual* transition band implemented by the polyphase
			 * filter
			 */
            gfc.highpass1 = highpass_band / 31.0f;
            if (maxband == -1) {
                gfc.highpass2 = (highpass_band + .75f) / 31.0f;
            } else {
                gfc.highpass2 = (maxband + .75f) / 31.0f;
            }
        }

        for (int band = 0; band < 32; band++) {
            double fc1, fc2;
            float freq = band / 31.0f;
            if (gfc.highpass2 > gfc.highpass1) {
                fc1 = filter_coef((gfc.highpass2 - freq)
                        / (gfc.highpass2 - gfc.highpass1 + 1e-20f));
            } else {
                fc1 = 1.0;
            }
            if (gfc.lowpass2 > gfc.lowpass1) {
                fc2 = filter_coef((freq - gfc.lowpass1)
                        / (gfc.lowpass2 - gfc.lowpass1 + 1e-20f));
            } else {
                fc2 = 1.0;
            }
            gfc.amp_filter[band] = (float) (fc1 * fc2);
        }
    }

    private void optimum_bandwidth(final LowPassHighPass lh, final int bitrate) {
        /**
         * <PRE>
         *  Input:
         *      bitrate     total bitrate in kbps
         *
         *   Output:
         *      lowerlimit: best lowpass frequency limit for input filter in Hz
         *      upperlimit: best highpass frequency limit for input filter in Hz
         * </PRE>
         */
        final BandPass freq_map[] = new BandPass[]{new BandPass(8, 2000),
                new BandPass(16, 3700), new BandPass(24, 3900),
                new BandPass(32, 5500), new BandPass(40, 7000),
                new BandPass(48, 7500), new BandPass(56, 10000),
                new BandPass(64, 11000), new BandPass(80, 13500),
                new BandPass(96, 15100), new BandPass(112, 15600),
                new BandPass(128, 17000), new BandPass(160, 17500),
                new BandPass(192, 18600), new BandPass(224, 19400),
                new BandPass(256, 19700), new BandPass(320, 20500)};

        int table_index = nearestBitrateFullIndex(bitrate);
        lh.lowerlimit = freq_map[table_index].lowpass;
    }

    private int optimum_samplefreq(final int lowpassfreq,
                                   final int input_samplefreq) {
		/*
		 * Rules:
		 *
		 * - if possible, sfb21 should NOT be used
		 */
        int suggested_samplefreq = 44100;

        if (input_samplefreq >= 48000)
            suggested_samplefreq = 48000;
        else if (input_samplefreq >= 44100)
            suggested_samplefreq = 44100;
        else if (input_samplefreq >= 32000)
            suggested_samplefreq = 32000;
        else if (input_samplefreq >= 24000)
            suggested_samplefreq = 24000;
        else if (input_samplefreq >= 22050)
            suggested_samplefreq = 22050;
        else if (input_samplefreq >= 16000)
            suggested_samplefreq = 16000;
        else if (input_samplefreq >= 12000)
            suggested_samplefreq = 12000;
        else if (input_samplefreq >= 11025)
            suggested_samplefreq = 11025;
        else if (input_samplefreq >= 8000)
            suggested_samplefreq = 8000;

        if (lowpassfreq == -1)
            return suggested_samplefreq;

        if (lowpassfreq <= 15960)
            suggested_samplefreq = 44100;
        if (lowpassfreq <= 15250)
            suggested_samplefreq = 32000;
        if (lowpassfreq <= 11220)
            suggested_samplefreq = 24000;
        if (lowpassfreq <= 9970)
            suggested_samplefreq = 22050;
        if (lowpassfreq <= 7230)
            suggested_samplefreq = 16000;
        if (lowpassfreq <= 5420)
            suggested_samplefreq = 12000;
        if (lowpassfreq <= 4510)
            suggested_samplefreq = 11025;
        if (lowpassfreq <= 3970)
            suggested_samplefreq = 8000;

        if (input_samplefreq < suggested_samplefreq) {
			/*
			 * choose a valid MPEG sample frequency above the input sample
			 * frequency to avoid SFB21/12 bitrate bloat rh 061115
			 */
            if (input_samplefreq > 44100) {
                return 48000;
            }
            if (input_samplefreq > 32000) {
                return 44100;
            }
            if (input_samplefreq > 24000) {
                return 32000;
            }
            if (input_samplefreq > 22050) {
                return 24000;
            }
            if (input_samplefreq > 16000) {
                return 22050;
            }
            if (input_samplefreq > 12000) {
                return 16000;
            }
            if (input_samplefreq > 11025) {
                return 12000;
            }
            if (input_samplefreq > 8000) {
                return 11025;
            }
            return 8000;
        }
        return suggested_samplefreq;
    }

    /**
     * set internal feature flags. USER should not access these since some
     * combinations will produce strange results
     */
    private void lame_init_qval() {
        final LameInternalFlags gfc = gfp.internal_flags;

        switch (gfp.getQuality()) {
            default:
            case 9: /* no psymodel, no noise shaping */
                gfc.psymodel = 0;
                gfc.noise_shaping = 0;
                gfc.noise_shaping_amp = 0;
                gfc.noise_shaping_stop = 0;
                gfc.use_best_huffman = 0;
                gfc.full_outer_loop = 0;
                break;

            case 8:
                gfp.setQuality(7);
                //$FALL-THROUGH$
            case 7:
			/*
			 * use psymodel (for short block and m/s switching), but no noise
			 * shapping
			 */
                gfc.psymodel = 1;
                gfc.noise_shaping = 0;
                gfc.noise_shaping_amp = 0;
                gfc.noise_shaping_stop = 0;
                gfc.use_best_huffman = 0;
                gfc.full_outer_loop = 0;
                break;

            case 6:
                gfc.psymodel = 1;
                if (gfc.noise_shaping == 0)
                    gfc.noise_shaping = 1;
                gfc.noise_shaping_amp = 0;
                gfc.noise_shaping_stop = 0;
                if (gfc.subblock_gain == -1)
                    gfc.subblock_gain = 1;
                gfc.use_best_huffman = 0;
                gfc.full_outer_loop = 0;
                break;

            case 5:
                gfc.psymodel = 1;
                if (gfc.noise_shaping == 0)
                    gfc.noise_shaping = 1;
                gfc.noise_shaping_amp = 0;
                gfc.noise_shaping_stop = 0;
                if (gfc.subblock_gain == -1)
                    gfc.subblock_gain = 1;
                gfc.use_best_huffman = 0;
                gfc.full_outer_loop = 0;
                break;

            case 4:
                gfc.psymodel = 1;
                if (gfc.noise_shaping == 0)
                    gfc.noise_shaping = 1;
                gfc.noise_shaping_amp = 0;
                gfc.noise_shaping_stop = 0;
                if (gfc.subblock_gain == -1)
                    gfc.subblock_gain = 1;
                gfc.use_best_huffman = 1;
                gfc.full_outer_loop = 0;
                break;

            case 3:
                gfc.psymodel = 1;
                if (gfc.noise_shaping == 0)
                    gfc.noise_shaping = 1;
                gfc.noise_shaping_amp = 1;
                gfc.noise_shaping_stop = 1;
                if (gfc.subblock_gain == -1)
                    gfc.subblock_gain = 1;
                gfc.use_best_huffman = 1;
                gfc.full_outer_loop = 0;
                break;

            case 2:
                gfc.psymodel = 1;
                if (gfc.noise_shaping == 0)
                    gfc.noise_shaping = 1;
                if (gfc.substep_shaping == 0)
                    gfc.substep_shaping = 2;
                gfc.noise_shaping_amp = 1;
                gfc.noise_shaping_stop = 1;
                if (gfc.subblock_gain == -1)
                    gfc.subblock_gain = 1;
                gfc.use_best_huffman = 1; /* inner loop */
                gfc.full_outer_loop = 0;
                break;

            case 1:
                gfc.psymodel = 1;
                if (gfc.noise_shaping == 0)
                    gfc.noise_shaping = 1;
                if (gfc.substep_shaping == 0)
                    gfc.substep_shaping = 2;
                gfc.noise_shaping_amp = 2;
                gfc.noise_shaping_stop = 1;
                if (gfc.subblock_gain == -1)
                    gfc.subblock_gain = 1;
                gfc.use_best_huffman = 1;
                gfc.full_outer_loop = 0;
                break;

            case 0:
                gfc.psymodel = 1;
                if (gfc.noise_shaping == 0)
                    gfc.noise_shaping = 1;
                if (gfc.substep_shaping == 0)
                    gfc.substep_shaping = 2;
                gfc.noise_shaping_amp = 2;
                gfc.noise_shaping_stop = 1;
                if (gfc.subblock_gain == -1)
                    gfc.subblock_gain = 1;
                gfc.use_best_huffman = 1;
			/*
			 * type 2 disabled because of it slowness, in favor of full outer
			 * loop search
			 */
                gfc.full_outer_loop = 0;
			/*
			 * full outer loop search disabled because of audible distortions it
			 * may generate rh 060629
			 */
                break;
        }

    }

    private double linear_int(final double a, final double b, final double m) {
        return a + m * (b - a);
    }

    /**
     * @param bRate legal rates from 8 to 320
     */
    private int FindNearestBitrate(final int bRate, int version,
                                   final int samplerate) {
		/* MPEG-1 or MPEG-2 LSF */
        if (samplerate < 16000)
            version = 2;

        int bitrate = Tables.bitrate_table[version][1];

        for (int i = 2; i <= 14; i++) {
            if (Tables.bitrate_table[version][i] > 0) {
                if (Math.abs(Tables.bitrate_table[version][i] - bRate) < Math
                        .abs(bitrate - bRate))
                    bitrate = Tables.bitrate_table[version][i];
            }
        }
        return bitrate;
    }

    /**
     * Used to find table index when we need bitrate-based values determined
     * using tables
     * <p/>
     * bitrate in kbps
     * <p/>
     * Gabriel Bouvigne 2002-11-03
     */
    public final int nearestBitrateFullIndex(final int bitrate) {
		/* borrowed from DM abr presets */

        final int full_bitrate_table[] = {8, 16, 24, 32, 40, 48, 56, 64, 80,
                96, 112, 128, 160, 192, 224, 256, 320};

        int lower_range = 0, lower_range_kbps = 0, upper_range = 0, upper_range_kbps = 0;

		/* We assume specified bitrate will be 320kbps */
        upper_range_kbps = full_bitrate_table[16];
        upper_range = 16;
        lower_range_kbps = full_bitrate_table[16];
        lower_range = 16;

		/*
		 * Determine which significant bitrates the value specified falls
		 * between, if loop ends without breaking then we were correct above
		 * that the value was 320
		 */
        for (int b = 0; b < 16; b++) {
            if ((Math.max(bitrate, full_bitrate_table[b + 1])) != bitrate) {
                upper_range_kbps = full_bitrate_table[b + 1];
                upper_range = b + 1;
                lower_range_kbps = full_bitrate_table[b];
                lower_range = (b);
                break; /* We found upper range */
            }
        }

		/* Determine which range the value specified is closer to */
        if ((upper_range_kbps - bitrate) > (bitrate - lower_range_kbps)) {
            return lower_range;
        }
        return upper_range;
    }

    /**
     * map frequency to a valid MP3 sample frequency
     * <p/>
     * Robert Hegemann 2000-07-01
     */
    private int map2MP3Frequency(final int freq) {
        if (freq <= 8000)
            return 8000;
        if (freq <= 11025)
            return 11025;
        if (freq <= 12000)
            return 12000;
        if (freq <= 16000)
            return 16000;
        if (freq <= 22050)
            return 22050;
        if (freq <= 24000)
            return 24000;
        if (freq <= 32000)
            return 32000;
        if (freq <= 44100)
            return 44100;

        return 48000;
    }

    /**
     * convert samp freq in Hz to index
     */
    private int SmpFrqIndex(final int sample_freq) {
        switch (sample_freq) {
            case 44100:
                gfp.setMpegVersion(1);
                return 0;
            case 48000:
                gfp.setMpegVersion(1);
                return 1;
            case 32000:
                gfp.setMpegVersion(1);
                return 2;
            case 22050:
                gfp.setMpegVersion(0);
                return 0;
            case 24000:
                gfp.setMpegVersion(0);
                return 1;
            case 16000:
                gfp.setMpegVersion(0);
                return 2;
            case 11025:
                gfp.setMpegVersion(0);
                return 0;
            case 12000:
                gfp.setMpegVersion(0);
                return 1;
            case 8000:
                gfp.setMpegVersion(0);
                return 2;
            default:
                gfp.setMpegVersion(0);
                return -1;
        }
    }

    /**
     * @param bRate   legal rates from 32 to 448 kbps
     * @param version MPEG-1 or MPEG-2/2.5 LSF
     */
    public final int BitrateIndex(final int bRate, int version,
                                  final int samplerate) {
		/* convert bitrate in kbps to index */
        if (samplerate < 16000)
            version = 2;
        for (int i = 0; i <= 14; i++) {
            if (Tables.bitrate_table[version][i] > 0) {
                if (Tables.bitrate_table[version][i] == bRate) {
                    return i;
                }
            }
        }
        return -1;
    }


    /**
     * *****************************************************************
     * initialize internal params based on data in gf (globalflags struct filled
     * in by calling program)
     * <p/>
     * OUTLINE:
     * <p/>
     * We first have some complex code to determine bitrate, output samplerate
     * and mode. It is complicated by the fact that we allow the user to set
     * some or all of these parameters, and need to determine best possible
     * values for the rest of them:
     * <p/>
     * 1. set some CPU related flags 2. check if we are mono.mono, stereo.mono
     * or stereo.stereo 3. compute bitrate and output samplerate: user may have
     * set compression ratio user may have set a bitrate user may have set a
     * output samplerate 4. set some options which depend on output samplerate
     * 5. compute the actual compression ratio 6. set mode based on compression
     * ratio
     * <p/>
     * The remaining code is much simpler - it just sets options based on the
     * mode & compression ratio:
     * <p/>
     * set allow_diff_short based on mode select lowpass filter based on
     * compression ratio & mode set the bitrate index, and min/max bitrates for
     * VBR modes disable VBR tag if it is not appropriate initialize the
     * bitstream initialize scalefac_band data set sideinfo_len (based on
     * channels, CRC, out_samplerate) write an id3v2 tag into the bitstream
     * write VBR tag into the bitstream set mpeg1/2 flag estimate the number of
     * frames (based on a lot of data)
     * <p/>
     * now we set more flags: nspsytune: see code VBR modes see code CBR/ABR see
     * code
     * <p/>
     * Finally, we set the algorithm flags based on the gfp.quality value
     * lame_init_qval(gfp);
     * <p/>
     * ******************************************************************
     */
    public final int initParams() {
        LameInternalFlags gfc = gfp.internal_flags;

        gfc.Class_ID = 0;
        if (gfc.ATH == null)
            gfc.ATH = new ATH();
        if (gfc.PSY == null)
            gfc.PSY = new PSY();
        if (gfc.rgdata == null)
            gfc.rgdata = new ReplayGain();

        gfc.channels_in = gfp.getInNumChannels();
        if (gfc.channels_in == 1)
            gfp.setMode(MPEGMode.MONO);
        gfc.channels_out = (gfp.getMode() == MPEGMode.MONO) ? 1 : 2;
        gfc.mode_ext = Encoder.MPG_MD_MS_LR;
        if (gfp.getMode() == MPEGMode.MONO)
            gfp.force_ms = false;
		/*
		 * don't allow forced mid/side stereo for mono output
		 */

        if (gfp.getVBR() == VbrMode.vbr_off && gfp.VBR_mean_bitrate_kbps != 128
                && gfp.getBitRate() == 0)
            gfp.setBitRate(gfp.VBR_mean_bitrate_kbps);

        if (gfp.getVBR() == VbrMode.vbr_off || gfp.getVBR() == VbrMode.vbr_mtrh
                || gfp.getVBR() == VbrMode.vbr_mt) {
			/* these modes can handle free format condition */
        } else {
            gfp.free_format = false; /* mode can't be mixed with free format */
        }

        if (gfp.getVBR() == VbrMode.vbr_off && gfp.getBitRate() == 0) {
			/* no bitrate or compression ratio specified, use 11.025 */
            if (BitStream.EQ(gfp.compression_ratio, 0))
                gfp.compression_ratio = 11.025f;
			/*
			 * rate to compress a CD down to exactly 128000 bps
			 */
        }

		/* find bitrate if user specify a compression ratio */
        if (gfp.getVBR() == VbrMode.vbr_off && gfp.compression_ratio > 0) {

            if (gfp.getOutSampleRate() == 0)
                gfp.setOutSampleRate(map2MP3Frequency((int) (0.97 * gfp.getInSampleRate())));
			/*
			 * round up with a margin of 3 %
			 */

			/*
			 * choose a bitrate for the output samplerate which achieves
			 * specified compression ratio
			 */
            gfp.setBitRate((int) (gfp.getOutSampleRate() * 16 * gfc.channels_out / (1.e3f * gfp.compression_ratio)));

			/* we need the version for the bitrate table look up */
            gfc.samplerate_index = SmpFrqIndex(gfp.getOutSampleRate());

            if (!gfp.free_format) /*
								 * for non Free Format find the nearest allowed
								 * bitrate
								 */
                gfp.setBitRate(FindNearestBitrate(gfp.getBitRate(), gfp.getMpegVersion(),
                        gfp.getOutSampleRate()));
        }

        if (gfp.getOutSampleRate() != 0) {
            if (gfp.getOutSampleRate() < 16000) {
                gfp.VBR_mean_bitrate_kbps = Math.max(gfp.VBR_mean_bitrate_kbps,
                        8);
                gfp.VBR_mean_bitrate_kbps = Math.min(gfp.VBR_mean_bitrate_kbps,
                        64);
            } else if (gfp.getOutSampleRate() < 32000) {
                gfp.VBR_mean_bitrate_kbps = Math.max(gfp.VBR_mean_bitrate_kbps,
                        8);
                gfp.VBR_mean_bitrate_kbps = Math.min(gfp.VBR_mean_bitrate_kbps,
                        160);
            } else {
                gfp.VBR_mean_bitrate_kbps = Math.max(gfp.VBR_mean_bitrate_kbps,
                        32);
                gfp.VBR_mean_bitrate_kbps = Math.min(gfp.VBR_mean_bitrate_kbps,
                        320);
            }
        }

        /****************************************************************/
		/* if a filter has not been enabled, see if we should add one: */
        /****************************************************************/
        if (gfp.lowpassfreq == 0) {
            double lowpass = 16000;

            switch (gfp.getVBR()) {
                case vbr_off: {
                    LowPassHighPass lh = new LowPassHighPass();
                    optimum_bandwidth(lh, gfp.getBitRate());
                    lowpass = lh.lowerlimit;
                    break;
                }
                case vbr_abr: {
                    LowPassHighPass lh = new LowPassHighPass();
                    optimum_bandwidth(lh, gfp.VBR_mean_bitrate_kbps);
                    lowpass = lh.lowerlimit;
                    break;
                }
                case vbr_rh: {
                    final int x[] = {19500, 19000, 18600, 18000, 17500, 16000,
                            15600, 14900, 12500, 10000, 3950};
                    if (0 <= gfp.getVBRQuality() && gfp.getVBRQuality() <= 9) {
                        double a = x[gfp.getVBRQuality()], b = x[gfp.getVBRQuality() + 1], m = gfp.VBR_q_frac;
                        lowpass = linear_int(a, b, m);
                    } else {
                        lowpass = 19500;
                    }
                    break;
                }
                default: {
                    final int x[] = {19500, 19000, 18500, 18000, 17500, 16500,
                            15500, 14500, 12500, 9500, 3950};
                    if (0 <= gfp.getVBRQuality() && gfp.getVBRQuality() <= 9) {
                        double a = x[gfp.getVBRQuality()], b = x[gfp.getVBRQuality() + 1], m = gfp.VBR_q_frac;
                        lowpass = linear_int(a, b, m);
                    } else {
                        lowpass = 19500;
                    }
                }
            }
            if (gfp.getMode() == MPEGMode.MONO
                    && (gfp.getVBR() == VbrMode.vbr_off || gfp.getVBR() == VbrMode.vbr_abr))
                lowpass *= 1.5;

            gfp.lowpassfreq = (int) lowpass;
        }

        if (gfp.getOutSampleRate() == 0) {
            if (2 * gfp.lowpassfreq > gfp.getInSampleRate()) {
                gfp.lowpassfreq = gfp.getInSampleRate() / 2;
            }
            gfp.setOutSampleRate(optimum_samplefreq((int) gfp.lowpassfreq,
                    gfp.getInSampleRate()));
        }

        gfp.lowpassfreq = Math.min(20500, gfp.lowpassfreq);
        gfp.lowpassfreq = Math.min(gfp.getOutSampleRate() / 2, gfp.lowpassfreq);

        if (gfp.getVBR() == VbrMode.vbr_off) {
            gfp.compression_ratio = gfp.getOutSampleRate() * 16 * gfc.channels_out
                    / (1.e3f * gfp.getBitRate());
        }
        if (gfp.getVBR() == VbrMode.vbr_abr) {
            gfp.compression_ratio = gfp.getOutSampleRate() * 16 * gfc.channels_out
                    / (1.e3f * gfp.VBR_mean_bitrate_kbps);
        }

		/*
		 * do not compute ReplayGain values and do not find the peak sample if
		 * we can't store them
		 */
        if (!gfp.bWriteVbrTag) {
            gfp.setFindReplayGain(false);
            gfp.decode_on_the_fly = false;
            gfc.findPeakSample = false;
        }
        gfc.findReplayGain = gfp.isFindReplayGain();
        gfc.decode_on_the_fly = gfp.decode_on_the_fly;

        if (gfc.decode_on_the_fly)
            gfc.findPeakSample = true;

        if (gfc.findReplayGain) {
            if (ga.InitGainAnalysis(gfc.rgdata, gfp.getOutSampleRate()) == GainAnalysis.INIT_GAIN_ANALYSIS_ERROR) {
                gfp.internal_flags = null;
                return -6;
            }
        }

        if (gfc.decode_on_the_fly && !gfp.decode_only) {
            if (gfc.hip != null) {
                mpglib.hip_decode_exit(gfc.hip);
            }
            gfc.hip = mpglib.hip_decode_init();
        }

        gfc.mode_gr = gfp.getOutSampleRate() <= 24000 ? 1 : 2;
		/*
		 * Number of granules per frame
		 */
        gfp.setFrameSize(576 * gfc.mode_gr);
        gfp.setEncoderDelay(Encoder.ENCDELAY);

        gfc.resample_ratio = (double) gfp.getInSampleRate() / gfp.getOutSampleRate();

        /**
         * <PRE>
         *  sample freq       bitrate     compression ratio
         *     [kHz]      [kbps/channel]   for 16 bit input
         *     44.1            56               12.6
         *     44.1            64               11.025
         *     44.1            80                8.82
         *     22.05           24               14.7
         *     22.05           32               11.025
         *     22.05           40                8.82
         *     16              16               16.0
         *     16              24               10.667
         * </PRE>
         */
        /**
         * <PRE>
         *  For VBR, take a guess at the compression_ratio.
         *  For example:
         *
         *    VBR_q    compression     like
         *     -        4.4         320 kbps/44 kHz
         *   0...1      5.5         256 kbps/44 kHz
         *     2        7.3         192 kbps/44 kHz
         *     4        8.8         160 kbps/44 kHz
         *     6       11           128 kbps/44 kHz
         *     9       14.7          96 kbps
         *
         *  for lower bitrates, downsample with --resample
         * </PRE>
         */
        switch (gfp.getVBR()) {
            case vbr_mt:
            case vbr_rh:
            case vbr_mtrh: {
			/* numbers are a bit strange, but they determine the lowpass value */
                final float cmp[] = {5.7f, 6.5f, 7.3f, 8.2f, 10f, 11.9f, 13f, 14f,
                        15f, 16.5f};
                gfp.compression_ratio = cmp[gfp.getVBRQuality()];
            }
            break;
            case vbr_abr:
                gfp.compression_ratio = gfp.getOutSampleRate() * 16 * gfc.channels_out
                        / (1.e3f * gfp.VBR_mean_bitrate_kbps);
                break;
            default:
                gfp.compression_ratio = gfp.getOutSampleRate() * 16 * gfc.channels_out
                        / (1.e3f * gfp.getBitRate());
                break;
        }

		/*
		 * mode = -1 (not set by user) or mode = MONO (because of only 1 input
		 * channel). If mode has not been set, then select J-STEREO
		 */
        if (gfp.getMode() == MPEGMode.NOT_SET) {
            gfp.setMode(MPEGMode.JOINT_STEREO);
        }

		/* apply user driven high pass filter */
        if (gfp.highpassfreq > 0) {
            gfc.highpass1 = 2.f * gfp.highpassfreq;

            if (gfp.highpasswidth >= 0)
                gfc.highpass2 = 2.f * (gfp.highpassfreq + gfp.highpasswidth);
            else
				/* 0% above on default */
                gfc.highpass2 = (1 + 0.00f) * 2.f * gfp.highpassfreq;

            gfc.highpass1 /= gfp.getOutSampleRate();
            gfc.highpass2 /= gfp.getOutSampleRate();
        } else {
            gfc.highpass1 = 0;
            gfc.highpass2 = 0;
        }
		/* apply user driven low pass filter */
        if (gfp.lowpassfreq > 0) {
            gfc.lowpass2 = 2.f * gfp.lowpassfreq;
            if (gfp.lowpasswidth >= 0) {
                gfc.lowpass1 = 2.f * (gfp.lowpassfreq - gfp.lowpasswidth);
                if (gfc.lowpass1 < 0) /* has to be >= 0 */
                    gfc.lowpass1 = 0;
            } else { /* 0% below on default */
                gfc.lowpass1 = (1 - 0.00f) * 2.f * gfp.lowpassfreq;
            }
            gfc.lowpass1 /= gfp.getOutSampleRate();
            gfc.lowpass2 /= gfp.getOutSampleRate();
        } else {
            gfc.lowpass1 = 0;
            gfc.lowpass2 = 0;
        }

        /**********************************************************************/
		/* compute info needed for polyphase filter (filter type==0, default) */
        /**********************************************************************/
        lame_init_params_ppflt();

        /*******************************************************
         * samplerate and bitrate index
         *******************************************************/
        gfc.samplerate_index = SmpFrqIndex(gfp.getOutSampleRate());
        if (gfc.samplerate_index < 0) {
            gfp.internal_flags = null;
            return -1;
        }

        if (gfp.getVBR() == VbrMode.vbr_off) {
            if (gfp.free_format) {
                gfc.bitrate_index = 0;
            } else {
                gfp.setBitRate(FindNearestBitrate(gfp.getBitRate(), gfp.getMpegVersion(),
                        gfp.getOutSampleRate()));
                gfc.bitrate_index = BitrateIndex(gfp.getBitRate(), gfp.getMpegVersion(),
                        gfp.getOutSampleRate());
                if (gfc.bitrate_index <= 0) {
                    gfp.internal_flags = null;
                    return -1;
                }
            }
        } else {
            gfc.bitrate_index = 1;
        }

		/* for CBR, we will write an "info" tag. */

        if (gfp.analysis)
            gfp.bWriteVbrTag = false;

		/* some file options not allowed if output is: not specified or stdout */
        if (gfc.pinfo != null)
            gfp.bWriteVbrTag = false; /* disable Xing VBR tag */

        bs.init_bit_stream_w(gfc);

        int j = gfc.samplerate_index + (3 * gfp.getMpegVersion()) + 6
                * (gfp.getOutSampleRate() < 16000 ? 1 : 0);
        for (int i = 0; i < Encoder.SBMAX_l + 1; i++)
            gfc.scalefac_band.l[i] = qupvt.sfBandIndex[j].l[i];

        for (int i = 0; i < Encoder.PSFB21 + 1; i++) {
            final int size = (gfc.scalefac_band.l[22] - gfc.scalefac_band.l[21])
                    / Encoder.PSFB21;
            final int start = gfc.scalefac_band.l[21] + i * size;
            gfc.scalefac_band.psfb21[i] = start;
        }
        gfc.scalefac_band.psfb21[Encoder.PSFB21] = 576;

        for (int i = 0; i < Encoder.SBMAX_s + 1; i++)
            gfc.scalefac_band.s[i] = qupvt.sfBandIndex[j].s[i];

        for (int i = 0; i < Encoder.PSFB12 + 1; i++) {
            final int size = (gfc.scalefac_band.s[13] - gfc.scalefac_band.s[12])
                    / Encoder.PSFB12;
            final int start = gfc.scalefac_band.s[12] + i * size;
            gfc.scalefac_band.psfb12[i] = start;
        }
        gfc.scalefac_band.psfb12[Encoder.PSFB12] = 192;

		/* determine the mean bitrate for main data */
        if (gfp.getMpegVersion() == 1) /* MPEG 1 */
            gfc.sideinfo_len = (gfc.channels_out == 1) ? 4 + 17 : 4 + 32;
        else
			/* MPEG 2 */
            gfc.sideinfo_len = (gfc.channels_out == 1) ? 4 + 9 : 4 + 17;

        if (gfp.error_protection)
            gfc.sideinfo_len += 2;

        lame_init_bitstream();

        gfc.Class_ID = LAME_ID;

        {
            int k;

            for (k = 0; k < 19; k++)
                gfc.nsPsy.pefirbuf[k] = 700 * gfc.mode_gr * gfc.channels_out;

            if (gfp.ATHtype == -1)
                gfp.ATHtype = 4;
        }

        assert (gfp.getVBRQuality() <= 9);
        assert (gfp.getVBRQuality() >= 0);

        switch (gfp.getVBR()) {

            case vbr_mt:
                gfp.setVBR(VbrMode.vbr_mtrh);
                //$FALL-THROUGH$
            case vbr_mtrh: {
                if (gfp.useTemporal == null) {
                    gfp.useTemporal = false; /* off by default for this VBR mode */
                }

                p.apply_preset(gfp, 500 - (gfp.getVBRQuality() * 10), 0);
                /**
                 * <PRE>
                 *   The newer VBR code supports only a limited
                 * 	 subset of quality levels:
                 * 	 9-5=5 are the same, uses x^3/4 quantization
                 *   4-0=0 are the same  5 plus best huffman divide code
                 * </PRE>
                 */
                if (gfp.getQuality() < 0)
                    gfp.setQuality(QUALITY_DEFAULT);
                if (gfp.getQuality() < QUALITY_MIDDLE)
                    gfp.setQuality(0);
                if (gfp.getQuality() > QUALITY_MIDDLE)
                    gfp.setQuality(QUALITY_MIDDLE);

                gfc.PSY.mask_adjust = gfp.maskingadjust;
                gfc.PSY.mask_adjust_short = gfp.maskingadjust_short;

			/*
			 * sfb21 extra only with MPEG-1 at higher sampling rates
			 */
                if (gfp.experimentalY)
                    gfc.sfb21_extra = false;
                else
                    gfc.sfb21_extra = (gfp.getOutSampleRate() > 44000);

                break;

            }
            case vbr_rh: {

                p.apply_preset(gfp, 500 - (gfp.getVBRQuality() * 10), 0);

                gfc.PSY.mask_adjust = gfp.maskingadjust;
                gfc.PSY.mask_adjust_short = gfp.maskingadjust_short;

			/*
			 * sfb21 extra only with MPEG-1 at higher sampling rates
			 */
                if (gfp.experimentalY)
                    gfc.sfb21_extra = false;
                else
                    gfc.sfb21_extra = (gfp.getOutSampleRate() > 44000);

			/*
			 * VBR needs at least the output of GPSYCHO, so we have to garantee
			 * that by setting a minimum quality level, actually level 6 does
			 * it. down to level 6
			 */
                if (gfp.getQuality() > QUALITY_MIDDLE_LOW)
                    gfp.setQuality(QUALITY_MIDDLE_LOW);

                if (gfp.getQuality() < 0)
                    gfp.setQuality(QUALITY_DEFAULT);

                break;
            }

            default: /* cbr/abr */ {
                VbrMode vbrmode;

			/*
			 * no sfb21 extra with CBR code
			 */
                gfc.sfb21_extra = false;

                if (gfp.getQuality() < 0)
                    gfp.setQuality(QUALITY_DEFAULT);

                vbrmode = gfp.getVBR();
                if (vbrmode == VbrMode.vbr_off)
                    gfp.VBR_mean_bitrate_kbps = gfp.getBitRate();
			/* second, set parameters depending on bitrate */
                p.apply_preset(gfp, gfp.VBR_mean_bitrate_kbps, 0);
                gfp.setVBR(vbrmode);

                gfc.PSY.mask_adjust = gfp.maskingadjust;
                gfc.PSY.mask_adjust_short = gfp.maskingadjust_short;

                if (vbrmode == VbrMode.vbr_off) {
                } else {
                }
                break;
            }
        }

		/* initialize default values common for all modes */

        if (gfp.getVBR() != VbrMode.vbr_off) { /* choose a min/max bitrate for VBR */
			/* if the user didn't specify VBR_max_bitrate: */
            gfc.VBR_min_bitrate = 1;
			/*
			 * default: allow 8 kbps (MPEG-2) or 32 kbps (MPEG-1)
			 */
            gfc.VBR_max_bitrate = 14;
			/*
			 * default: allow 160 kbps (MPEG-2) or 320 kbps (MPEG-1)
			 */
            if (gfp.getOutSampleRate() < 16000)
                gfc.VBR_max_bitrate = 8; /* default: allow 64 kbps (MPEG-2.5) */
            if (gfp.VBR_min_bitrate_kbps != 0) {
                gfp.VBR_min_bitrate_kbps = FindNearestBitrate(
                        gfp.VBR_min_bitrate_kbps, gfp.getMpegVersion(),
                        gfp.getOutSampleRate());
                gfc.VBR_min_bitrate = BitrateIndex(gfp.VBR_min_bitrate_kbps,
                        gfp.getMpegVersion(), gfp.getOutSampleRate());
                if (gfc.VBR_min_bitrate < 0)
                    return -1;
            }
            if (gfp.VBR_max_bitrate_kbps != 0) {
                gfp.VBR_max_bitrate_kbps = FindNearestBitrate(
                        gfp.VBR_max_bitrate_kbps, gfp.getMpegVersion(),
                        gfp.getOutSampleRate());
                gfc.VBR_max_bitrate = BitrateIndex(gfp.VBR_max_bitrate_kbps,
                        gfp.getMpegVersion(), gfp.getOutSampleRate());
                if (gfc.VBR_max_bitrate < 0)
                    return -1;
            }
            gfp.VBR_min_bitrate_kbps = Tables.bitrate_table[gfp.getMpegVersion()][gfc.VBR_min_bitrate];
            gfp.VBR_max_bitrate_kbps = Tables.bitrate_table[gfp.getMpegVersion()][gfc.VBR_max_bitrate];
            gfp.VBR_mean_bitrate_kbps = Math.min(
                    Tables.bitrate_table[gfp.getMpegVersion()][gfc.VBR_max_bitrate],
                    gfp.VBR_mean_bitrate_kbps);
            gfp.VBR_mean_bitrate_kbps = Math.max(
                    Tables.bitrate_table[gfp.getMpegVersion()][gfc.VBR_min_bitrate],
                    gfp.VBR_mean_bitrate_kbps);
        }

		/* just another daily changing developer switch */
        if (gfp.tune) {
            gfc.PSY.mask_adjust += gfp.tune_value_a;
            gfc.PSY.mask_adjust_short += gfp.tune_value_a;
        }

		/* initialize internal qval settings */
        lame_init_qval();

        gfc.ATH.useAdjust = (gfp.athaa_type < 0) ? 3 : gfp.athaa_type;

		/* initialize internal adaptive ATH settings -jd */
        gfc.ATH.aaSensitivityP = (float) Math.pow(10.0, gfp.athaa_sensitivity / -10.0);

        if (gfp.short_blocks == null) {
            gfp.short_blocks = ShortBlock.short_block_allowed;
        }

        if (gfp.short_blocks == ShortBlock.short_block_allowed
                && (gfp.getMode() == MPEGMode.JOINT_STEREO || gfp.getMode() == MPEGMode.STEREO)) {
            gfp.short_blocks = ShortBlock.short_block_coupled;
        }

        if (gfp.quant_comp < 0)
            gfp.quant_comp = 1;
        if (gfp.quant_comp_short < 0)
            gfp.quant_comp_short = 0;

        if (gfp.msfix < 0)
            gfp.msfix = 0;

		/* select psychoacoustic model */
        gfp.exp_nspsytune = gfp.exp_nspsytune | 1;

        if (gfp.internal_flags.nsPsy.attackthre < 0)
            gfp.internal_flags.nsPsy.attackthre = PsyModel.NSATTACKTHRE;
        if (gfp.internal_flags.nsPsy.attackthre_s < 0)
            gfp.internal_flags.nsPsy.attackthre_s = PsyModel.NSATTACKTHRE_S;

        if (gfp.scale < 0)
            gfp.scale = 1;

        if (gfp.ATHtype < 0)
            gfp.ATHtype = 4;

        if (gfp.ATHcurve < 0)
            gfp.ATHcurve = 4;

        if (gfp.athaa_loudapprox < 0)
            gfp.athaa_loudapprox = 2;

        if (gfp.interChRatio < 0)
            gfp.interChRatio = 0;

        if (gfp.useTemporal == null)
            gfp.useTemporal = true; /* on by default */

		/*
		 * padding method as described in
		 * "MPEG-Layer3 / Bitstream Syntax and Decoding" by Martin Sieler, Ralph
		 * Sperschneider
		 *
		 * note: there is no padding for the very first frame
		 *
		 * Robert Hegemann 2000-06-22
		 */
        gfc.slot_lag = gfc.frac_SpF = 0;
        if (gfp.getVBR() == VbrMode.vbr_off)
            gfc.slot_lag = gfc.frac_SpF = (int) (((gfp.getMpegVersion() + 1) * 72000L * gfp.getBitRate()) % gfp.getOutSampleRate());

        //qupvt.iteration_init(gfp);
        //psy.psymodel_init(gfp);

        return 0;
    }

    /*
     * called by Lame.initParams. You can also call this after flush_nogap if
     * you want to write new id3v2 and Xing VBR tags into the bitstream
     */
    public final void lame_init_bitstream() {
        final LameInternalFlags gfc = gfp.internal_flags;
        gfp.frameNum = 0;

        if (gfp.isWriteId3tagAutomatic()) {
            id3.id3tag_write_v2(gfp);
        }
		/* initialize histogram data optionally used by frontend */

        gfc.bitrate_stereoMode_Hist = new int[16][4 + 1];
        gfc.bitrate_blockType_Hist = new int[16][4 + 1 + 1];

        gfc.PeakSample = 0.0f;

		/* Write initial VBR Header to bitstream and init VBR data */
        if (gfp.bWriteVbrTag)
            vbr.InitVbrTag(gfp);
    }

    /**
     * frees internal buffers
     */
    public final int close() {
        int ret = 0;
        if (gfp != null && gfp.class_id == LAME_ID) {
            final LameInternalFlags gfc = gfp.internal_flags;
            gfp.class_id = 0;
            if (null == gfc || gfc.Class_ID != LAME_ID) {
                ret = -3;
            }
            gfc.Class_ID = 0;
            gfp.internal_flags = null;
            gfp.lame_allocated_gfp = 0;
        }
        return ret;
    }

    private final void lame_init() {
        LameInternalFlags gfc;

        gfp.class_id = LAME_ID;

        gfc = gfp.internal_flags = new LameInternalFlags();

		/* Global flags. set defaults here for non-zero values */
		/* see lame.h for description */
		/*
		 * set integer values to -1 to mean that LAME will compute the best
		 * value, UNLESS the calling program as set it (and the value is no
		 * longer -1)
		 */

        gfp.setMode(MPEGMode.NOT_SET);
        gfp.original = 1;
        gfp.setInSampleRate(44100);
        gfp.setInNumChannels(2);
        gfp.num_samples = -1;

        gfp.bWriteVbrTag = true;
        gfp.setQuality(-1);
        gfp.short_blocks = null;
        gfc.subblock_gain = -1;

        gfp.lowpassfreq = 0;
        gfp.highpassfreq = 0;
        gfp.lowpasswidth = -1;
        gfp.highpasswidth = -1;

        gfp.setVBR(VbrMode.vbr_off);
        gfp.setVBRQuality(4);
        gfp.ATHcurve = -1;
        gfp.VBR_mean_bitrate_kbps = 128;
        gfp.VBR_min_bitrate_kbps = 0;
        gfp.VBR_max_bitrate_kbps = 0;
        gfp.VBR_hard_min = 0;
        gfc.VBR_min_bitrate = 1; /* not 0 ????? */
        gfc.VBR_max_bitrate = 13; /* not 14 ????? */

        gfp.quant_comp = -1;
        gfp.quant_comp_short = -1;

        gfp.msfix = -1;

        gfc.resample_ratio = 1;

        gfc.OldValue[0] = 180;
        gfc.OldValue[1] = 180;
        gfc.CurrentStep[0] = 4;
        gfc.CurrentStep[1] = 4;
        gfc.masking_lower = 1;
        gfc.nsPsy.attackthre = -1;
        gfc.nsPsy.attackthre_s = -1;

        gfp.scale = -1;

        gfp.athaa_type = -1;
        gfp.ATHtype = -1; /* default = -1 = set in Lame.initParams */
        gfp.athaa_loudapprox = -1; /* 1 = flat loudness approx. (total energy) */
		/* 2 = equal loudness curve */
        gfp.athaa_sensitivity = 0.0f; /* no offset */
        gfp.useTemporal = null;
        gfp.interChRatio = -1;

		/*
		 * The reason for int mf_samples_to_encode = ENCDELAY + POSTDELAY;
		 * ENCDELAY = internal encoder delay. And then we have to add
		 * POSTDELAY=288 because of the 50% MDCT overlap. A 576 MDCT granule
		 * decodes to 1152 samples. To synthesize the 576 samples centered under
		 * this granule we need the previous granule for the first 288 samples
		 * (no problem), and the next granule for the next 288 samples (not
		 * possible if this is last granule). So we need to pad with 288 samples
		 * to make sure we can encode the 576 samples we are interested in.
		 */
        gfc.mf_samples_to_encode = Encoder.ENCDELAY + Encoder.POSTDELAY;
        gfp.encoder_padding = 0;
        gfc.mf_size = Encoder.ENCDELAY - Encoder.MDCTDELAY;
		/*
		 * we pad input with this many 0's
		 */

        gfp.setFindReplayGain(false);
        gfp.decode_on_the_fly = false;

        gfc.decode_on_the_fly = false;
        gfc.findReplayGain = false;
        gfc.findPeakSample = false;

        gfc.RadioGain = 0;
        gfc.AudiophileGain = 0;
        gfc.noclipGainChange = 0;
        gfc.noclipScale = -1.0f;

        gfp.preset = 0;

        gfp.setWriteId3tagAutomatic(true);

        gfp.lame_allocated_gfp = 1;
    }

    protected static class LowPassHighPass {
        double lowerlimit;
    }

    private static class BandPass {
        public int lowpass;

        public BandPass(int bitrate, int lPass) {
            lowpass = lPass;
        }
    }

}
