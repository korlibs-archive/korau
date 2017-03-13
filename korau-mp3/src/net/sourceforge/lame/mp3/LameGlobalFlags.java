package net.sourceforge.lame.mp3;

/**
 * Control Parameters set by User. These parameters are here for backwards
 * compatibility with the old, non-shared lib API. Please use the
 * lame_set_variablename() functions below
 *
 * @author Ken
 */
public class LameGlobalFlags {
    public long class_id;
    public int num_samples;
    public float scale;
    public boolean analysis;
    public boolean bWriteVbrTag;
    public boolean decode_only;
    public boolean force_ms;
    public boolean free_format;
    public boolean decode_on_the_fly;
    public float compression_ratio;
    public int copyright;
    public int original;
    public int extension;
    public int emphasis;
    public boolean error_protection;
    public boolean disable_reservoir;
    public int quant_comp;
    public int quant_comp_short;
    public boolean experimentalY;
    public int exp_nspsytune;
    public int preset;
    public float VBR_q_frac;
    public int VBR_mean_bitrate_kbps;
    public int VBR_min_bitrate_kbps;
    public int VBR_max_bitrate_kbps;
    public int VBR_hard_min;
    public int lowpassfreq;
    public int highpassfreq;
    public int lowpasswidth;
    public int highpasswidth;
    public float maskingadjust;
    public float maskingadjust_short;
    public int ATHtype;
    public float ATHcurve;
    public float ATHlower;
    public int athaa_type;
    public int athaa_loudapprox;
    public float athaa_sensitivity;
    public ShortBlock short_blocks;
    public Boolean useTemporal;
    public float interChRatio;
    public float msfix;
    public boolean tune;
    public int encoder_padding;
    public int frameNum;
    public int lame_allocated_gfp;
    public LameInternalFlags internal_flags;
    private int inNumChannels;
    private int inSampleRate;
    private int outSampleRate;
    private int quality;
    private MPEGMode mode = MPEGMode.STEREO;
    private boolean findReplayGain;
    private boolean writeId3tagAutomatic;
    private int bitRate;
    private VbrMode VBR;
    private int VBRQuality;
    private int mpegVersion;
    private int frameSize;

    public final int getInNumChannels() {
        return inNumChannels;
    }

    public final void setInNumChannels(final int inNumChannels) {
        this.inNumChannels = inNumChannels;
    }

    public final int getInSampleRate() {
        return inSampleRate;
    }

    public final void setInSampleRate(final int inSampleRate) {
        this.inSampleRate = inSampleRate;
    }

    public int getOutSampleRate() {
        return outSampleRate;
    }

    public void setOutSampleRate(int outSampleRate) {
        this.outSampleRate = outSampleRate;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public final MPEGMode getMode() {
        return mode;
    }

    public final void setMode(final MPEGMode mode) {
        this.mode = mode;
    }

    public boolean isFindReplayGain() {
        return findReplayGain;
    }

    public void setFindReplayGain(boolean findReplayGain) {
        this.findReplayGain = findReplayGain;
    }

    public final boolean isWriteId3tagAutomatic() {
        return writeId3tagAutomatic;
    }

    public final void setWriteId3tagAutomatic(final boolean writeId3tagAutomatic) {
        this.writeId3tagAutomatic = writeId3tagAutomatic;
    }

    public final int getBitRate() {
        return bitRate;
    }

    public final void setBitRate(final int bitRate) {
        this.bitRate = bitRate;
    }

    public final VbrMode getVBR() {
        return VBR;
    }

    public final void setVBR(final VbrMode vBR) {
        VBR = vBR;
    }

    public final int getVBRQuality() {
        return VBRQuality;
    }

    public final void setVBRQuality(final int vBRQuality) {
        VBRQuality = vBRQuality;
    }

    public int getMpegVersion() {
        return mpegVersion;
    }

    public void setMpegVersion(int mpegVersion) {
        this.mpegVersion = mpegVersion;
    }

    public int getFrameSize() {
        return frameSize;
    }

    public void setFrameSize(int frameSize) {
        this.frameSize = frameSize;
    }

}
