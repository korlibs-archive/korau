package net.sourceforge.lame.mp3;

/**
 * Control Parameters set by User. These parameters are here for backwards
 * compatibility with the old, non-shared lib API. Please use the
 * lame_set_variablename() functions below
 *
 * @author Ken
 */
public class LameGlobalFlags {
    public int num_samples;
    private int inNumChannels;
    private int inSampleRate;

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
}
