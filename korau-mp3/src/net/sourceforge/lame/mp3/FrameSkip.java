package net.sourceforge.lame.mp3;

public class FrameSkip {
    private int encoderDelay = -1;
    private int encoderPadding = -1;

    public final int getEncoderDelay() {
        return encoderDelay;
    }

    public final void setEncoderDelay(final int encoderDelay) {
        this.encoderDelay = encoderDelay;
    }

    public final int getEncoderPadding() {
        return encoderPadding;
    }

    public final void setEncoderPadding(int enccoderPadding) {
        this.encoderPadding = enccoderPadding;
    }
}
