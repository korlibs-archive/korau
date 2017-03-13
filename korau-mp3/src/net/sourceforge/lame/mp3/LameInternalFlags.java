package net.sourceforge.lame.mp3;

import net.sourceforge.lame.mpg.MPGLib;

public class LameInternalFlags {
    public static final int MFSIZE = (3 * 1152 + Encoder.ENCDELAY - Encoder.MDCTDELAY);

    /**
     * also, max_header_buf has to be a power of two
     */
    public static final int MAX_HEADER_BUF = 256;
    public Header[] header = new Header[MAX_HEADER_BUF];
    /**
     * max size of header is 38
     */
    private static final int MAX_HEADER_LEN = 40;

    public long Class_ID;
    public int iteration_init_init;
    /**
     * granules per frame
     */
    public int mode_gr;
    /**
     * number of channels in the input data stream (PCM or decoded PCM)
     */
    public int channels_in;
    /**
     * number of channels in the output data stream (not used for decoding)
     */
    public int channels_out;
    /**
     * input_samp_rate/output_samp_rate
     */
    public double resample_ratio;
    public int mf_samples_to_encode;
    public int mf_size;
    /**
     * min bitrate index
     */
    public int VBR_min_bitrate;

	/* lowpass and highpass filter control */
    /**
     * max bitrate index
     */
    public int VBR_max_bitrate;
    public int bitrate_index;
    public int samplerate_index;
    public int mode_ext;
    /**
     * normalized frequency bounds of passband
     */
    public float lowpass1, lowpass2;
    /**
     * normalized frequency bounds of passband
     */
    public float highpass1, highpass2;
    /**
     * 0 = none 1 = ISO AAC model 2 = allow scalefac_select=1
     */
    public int noise_shaping;
    /**
     * 0 = ISO model: amplify all distorted bands<BR>
     * 1 = amplify within 50% of max (on db scale)<BR>
     * 2 = amplify only most distorted band<BR>
     * 3 = method 1 and refine with method 2<BR>
     */
    public int noise_shaping_amp;
    /**
     * 0 = no substep<BR>
     * 1 = use substep shaping at last step(VBR only)<BR>
     * (not implemented yet)<BR>
     * 2 = use substep inside loop<BR>
     * 3 = use substep inside loop and last step<BR>
     */
    public int substep_shaping;
    /**
     * 1 = gpsycho. 0 = none
     */
    public int psymodel;
    /**
     * 0 = stop at over=0, all scalefacs amplified or<BR>
     * a scalefac has reached max value<BR>
     * 1 = stop when all scalefacs amplified or a scalefac has reached max value<BR>
     * 2 = stop when all scalefacs amplified
     */
    public int noise_shaping_stop;
    /**
     * 0 = no, 1 = yes
     */
    public int subblock_gain;

	/* used for padding */
    /**
     * 0 = no. 1=outside loop 2=inside loop(slow)
     */
    public int use_best_huffman;
    /**
     * 0 = stop early after 0 distortion found. 1 = full search
     */
    public int full_outer_loop;
    public IIISideInfo l3_side = new IIISideInfo();
    public float ms_ratio[] = new float[2];
    /**
     * padding for the current frame?
     */
    public int padding;
    public int frac_SpF;
    public int slot_lag;
    /**
     * optional ID3 tags
     */
    public ID3TagSpec tag_spec;
    public int nMusicCRC;
    /* variables used by Quantize */
    public int OldValue[] = new int[2];
    public int CurrentStep[] = new int[2];
    public float masking_lower;
    /**
     * will be set in Lame.initParams
     */
    public boolean sfb21_extra;
    public int sideinfo_len;

    public float amp_filter[] = new float[32];
    public int h_ptr;
    public int w_ptr;
    public ScaleFac scalefac_band = new ScaleFac();
    public III_psy_xmin[] thm = new III_psy_xmin[4];
    public III_psy_xmin[] en = new III_psy_xmin[4];
    public NsPsy nsPsy = new NsPsy();
    /**
     * used for Xing VBR header
     */
    public VBRSeekInfo VBR_seek_table = new VBRSeekInfo();
    /**
     * all ATH related stuff
     */
    public ATH ATH;
    public PSY PSY;
    public int nogap_total;
    public int nogap_current;
    /* ReplayGain */
    public boolean decode_on_the_fly = true;
    public boolean findReplayGain = true;
    public boolean findPeakSample = true;
    public float PeakSample;
    public int RadioGain;
    public int AudiophileGain;
    public ReplayGain rgdata;
    /**
     * gain change required for preventing clipping
     */
    public int noclipGainChange;
    /**
     * user-specified scale factor required for preventing clipping
     */
    public float noclipScale;
    /* simple statistics */
    public int bitrate_stereoMode_Hist[][] = new int[16][4 + 1];
    /**
     * norm/start/short/stop/mixed(short)/sum
     */
    public int bitrate_blockType_Hist[][] = new int[16][4 + 1 + 1];
    public PlottingData pinfo;
    public MPGLib.mpstr_tag hip;

    public LameInternalFlags() {
        for (int i = 0; i < en.length; i++) {
            en[i] = new III_psy_xmin();
        }
        for (int i = 0; i < thm.length; i++) {
            thm[i] = new III_psy_xmin();
        }
        for (int i = 0; i < header.length; i++) {
            header[i] = new Header();
        }
    }

    public static class Header {
        public int write_timing;
        public int ptr;
        public byte buf[] = new byte[MAX_HEADER_LEN];
    }

}
