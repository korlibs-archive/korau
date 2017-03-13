package net.sourceforge.lame.mp3;

public class MP3Data {
    public boolean header_parsed;
    public int stereo;
    public int samplerate;
    public int bitrate;
    public int mode;
    public int mode_ext;
    private int frameSize;
    private int numSamples;
    private int totalFrames;
    private int framesDecodedCounter;

    public int getFrameSize() {
        return frameSize;
    }

    public void setFrameSize(int frameSize) {
        this.frameSize = frameSize;
    }

    public int getNumSamples() {
        return numSamples;
    }

    public void setNumSamples(int numSamples) {
        this.numSamples = numSamples;
    }

    public int getTotalFrames() {
        return totalFrames;
    }

    public void setTotalFrames(int totalFrames) {
        this.totalFrames = totalFrames;
    }

    public int getFramesDecodedCounter() {
        return framesDecodedCounter;
    }

    public void setFramesDecodedCounter(int framesDecodedCounter) {
        this.framesDecodedCounter = framesDecodedCounter;
    }
}
