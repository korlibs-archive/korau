/*
 *  ReplayGainAnalysis - analyzes input samples and give the recommended dB change
 *  Copyright (C) 2001 David Robinson and Glen Sawyer
 *  Improvements and optimizations added by Frank Klemm, and by Marcel Muller 
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  concept and filter values by David Robinson (David@Robinson.org)
 *    -- blame him if you think the idea is flawed
 *  original coding by Glen Sawyer (mp3gain@hotmail.com)
 *    -- blame him if you think this runs too slowly, or the coding is otherwise flawed
 *
 *  lots of code improvements by Frank Klemm ( http://www.uni-jena.de/~pfk/mpp/ )
 *    -- credit him for all the _good_ programming ;)
 *
 *
 *  For an explanation of the concepts and the basic algorithms involved, go to:
 *    http://www.replaygain.org/
 */

/*
 *  Here's the deal. Call
 *
 *    InitGainAnalysis ( long samplefreq );
 *
 *  to initialize everything. Call
 *
 *    AnalyzeSamples ( const Float_t*  left_samples,
 *                     const Float_t*  right_samples,
 *                     size_t          num_samples,
 *                     int             num_channels );
 *
 *  as many times as you want, with as many or as few samples as you want.
 *  If mono, pass the sample buffer in through left_samples, leave
 *  right_samples NULL, and make sure num_channels = 1.
 *
 *    GetTitleGain()
 *
 *  will return the recommended dB level change for all samples analyzed
 *  SINCE THE LAST TIME you called GetTitleGain() OR InitGainAnalysis().
 *
 *    GetAlbumGain()
 *
 *  will return the recommended dB level change for all samples analyzed
 *  since InitGainAnalysis() was called and finalized with GetTitleGain().
 *
 *  Pseudo-code to process an album:
 *
 *    Float_t       l_samples [4096];
 *    Float_t       r_samples [4096];
 *    size_t        num_samples;
 *    unsigned int  num_songs;
 *    unsigned int  i;
 *
 *    InitGainAnalysis ( 44100 );
 *    for ( i = 1; i <= num_songs; i++ ) {
 *        while ( ( num_samples = getSongSamples ( song[i], left_samples, right_samples ) ) > 0 )
 *            AnalyzeSamples ( left_samples, right_samples, num_samples, 2 );
 *        fprintf ("Recommended dB change for song %2d: %+6.2f dB\n", i, GetTitleGain() );
 *    }
 *    fprintf ("Recommended dB change for whole album: %+6.2f dB\n", GetAlbumGain() );
 */

/*
 *  So here's the main source of potential code confusion:
 *
 *  The filters applied to the incoming samples are IIR filters,
 *  meaning they rely on up to <filter order> number of previous samples
 *  AND up to <filter order> number of previous filtered samples.
 *
 *  I set up the AnalyzeSamples routine to minimize memory usage and interface
 *  complexity. The speed isn't compromised too much (I don't think), but the
 *  internal complexity is higher than it should be for such a relatively
 *  simple routine.
 *
 *  Optimization/clarity suggestions are welcome.
 */
package net.sourceforge.lame.mp3;

import java.util.Arrays;

public class GainAnalysis {
    public static final int INIT_GAIN_ANALYSIS_ERROR = 0;
    public static final int INIT_GAIN_ANALYSIS_OK = 1;
    static final float STEPS_per_dB = 100.f;
    static final float MAX_dB = 120.f;
    private static final int YULE_ORDER = 10;
    static final int MAX_ORDER = YULE_ORDER;
    private static final int MAX_SAMP_FREQ = 48000;
    private static final int RMS_WINDOW_TIME_NUMERATOR = 1;
    private static final int RMS_WINDOW_TIME_DENOMINATOR = 20;
    static final int MAX_SAMPLES_PER_WINDOW = ((MAX_SAMP_FREQ * RMS_WINDOW_TIME_NUMERATOR) / RMS_WINDOW_TIME_DENOMINATOR + 1);

    private int ResetSampleFrequency(final ReplayGain rgData, final long samplefreq) {
        /* zero out initial values */
        for (int i = 0; i < MAX_ORDER; i++)
            rgData.linprebuf[i] = rgData.lstepbuf[i] = rgData.loutbuf[i] = rgData.rinprebuf[i] = rgData.rstepbuf[i] = rgData.routbuf[i] = 0.f;

        switch ((int) (samplefreq)) {
            case 48000:
                rgData.freqindex = 0;
                break;
            case 44100:
                rgData.freqindex = 1;
                break;
            case 32000:
                rgData.freqindex = 2;
                break;
            case 24000:
                rgData.freqindex = 3;
                break;
            case 22050:
                rgData.freqindex = 4;
                break;
            case 16000:
                rgData.freqindex = 5;
                break;
            case 12000:
                rgData.freqindex = 6;
                break;
            case 11025:
                rgData.freqindex = 7;
                break;
            case 8000:
                rgData.freqindex = 8;
                break;
            default:
                return INIT_GAIN_ANALYSIS_ERROR;
        }

        rgData.sampleWindow = (int) ((samplefreq * RMS_WINDOW_TIME_NUMERATOR
                + RMS_WINDOW_TIME_DENOMINATOR - 1) / RMS_WINDOW_TIME_DENOMINATOR);

        rgData.lsum = 0.;
        rgData.rsum = 0.;
        rgData.totsamp = 0;

        Arrays.fill(rgData.A, 0);

        return INIT_GAIN_ANALYSIS_OK;
    }

    public final int InitGainAnalysis(final ReplayGain rgData, final long samplefreq) {
        if (ResetSampleFrequency(rgData, samplefreq) != INIT_GAIN_ANALYSIS_OK) {
            return INIT_GAIN_ANALYSIS_ERROR;
        }

        rgData.linpre = MAX_ORDER;
        rgData.rinpre = MAX_ORDER;
        rgData.lstep = MAX_ORDER;
        rgData.rstep = MAX_ORDER;
        rgData.lout = MAX_ORDER;
        rgData.rout = MAX_ORDER;

        Arrays.fill(rgData.B, 0);

        return INIT_GAIN_ANALYSIS_OK;
    }

}
