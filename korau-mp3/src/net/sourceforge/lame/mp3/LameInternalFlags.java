package net.sourceforge.lame.mp3;

import net.sourceforge.lame.mpg.MPGLib;

public class LameInternalFlags {
    public static final int MAX_HEADER_BUF = 256;
    public Header[] header = new Header[MAX_HEADER_BUF];
    public long Class_ID;
    public int mode_gr;
    public int channels_in;
    public int channels_out;
    public double resample_ratio;
    public int mf_samples_to_encode;
    public int mf_size;
    public int VBR_min_bitrate;
    public int VBR_max_bitrate;
    public int bitrate_index;
    public int samplerate_index;
    public int mode_ext;
    public float lowpass1, lowpass2;
    public float highpass1, highpass2;
    public int noise_shaping;
    public int noise_shaping_amp;
    public int substep_shaping;
    public int psymodel;
    public int noise_shaping_stop;
    public int subblock_gain;
    public int use_best_huffman;
    public int full_outer_loop;
    public int padding;
    public int frac_SpF;
    public int slot_lag;
    public ID3TagSpec tag_spec;
    public int nMusicCRC;
    public int OldValue[] = new int[2];
    public int CurrentStep[] = new int[2];
    public float masking_lower;
    public boolean sfb21_extra;
    public int sideinfo_len;
    public float amp_filter[] = new float[32];
    public int h_ptr;
    public int w_ptr;
    public ScaleFac scalefac_band = new ScaleFac();
    public NsPsy nsPsy = new NsPsy();
    public VBRSeekInfo VBR_seek_table = new VBRSeekInfo();
    public ATH ATH;
    public boolean decode_on_the_fly = true;
    public boolean findReplayGain = true;
    public boolean findPeakSample = true;
    public float PeakSample;
    public int RadioGain;
    public int AudiophileGain;
    public int noclipGainChange;
    public float noclipScale;
    public int bitrate_stereoMode_Hist[][] = new int[16][4 + 1];
    public int bitrate_blockType_Hist[][] = new int[16][4 + 1 + 1];
    public PlottingData pinfo;
    public MPGLib.mpstr_tag hip;

    public LameInternalFlags() {
        for (int i = 0; i < header.length; i++) header[i] = new Header();
    }

    public static class Header {
        public int write_timing;
    }

}
