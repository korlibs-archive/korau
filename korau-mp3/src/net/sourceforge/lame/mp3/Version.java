package net.sourceforge.lame.mp3;

public class Version {
    private static final String LAME_URL = "http://www.mp3dev.org/";
    private static final int LAME_MAJOR_VERSION = 3;
    private static final int LAME_MINOR_VERSION = 98;
    private static final int LAME_PATCH_VERSION = 4;
    private static final int PSY_MAJOR_VERSION = 0;
    private static final int PSY_MINOR_VERSION = 93;

    public final String getLameVersion() {
        return (LAME_MAJOR_VERSION + "." + LAME_MINOR_VERSION + "." + LAME_PATCH_VERSION);
    }

    public final String getLameShortVersion() {
        return (LAME_MAJOR_VERSION + "." + LAME_MINOR_VERSION + "." + LAME_PATCH_VERSION);
    }

    public final String getLameVeryShortVersion() {
        return ("LAME" + LAME_MAJOR_VERSION + "." + LAME_MINOR_VERSION + "r");
    }

    public final String getPsyVersion() {
        return (PSY_MAJOR_VERSION + "." + PSY_MINOR_VERSION);
    }

    public final String getVersion() {
        return "LAME Java version " + getLameVersion() + " (" + LAME_URL + ")";
    }
}
