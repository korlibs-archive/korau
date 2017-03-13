package net.sourceforge.lame.mp3;


/* MPEG modes */
public enum MPEGMode {
    STEREO(0), JOINT_STEREO(1),
    DUAL_CHANNEL(2), MONO(3), NOT_SET(-1);
    private int mode;

    private MPEGMode(final int md) {
        mode = md;
    }

    public final int getNumMode() {
        return mode;
    }
}
