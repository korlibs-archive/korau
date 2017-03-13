package net.sourceforge.lame.mpg;

import net.sourceforge.lame.mpg.L2Tables.al_table2;

public class Frame {
    int stereo;
    int jsbound;
    int single;
    int lsf;
    boolean mpeg25;
    int lay;
    boolean error_protection;
    int bitrate_index;
    int sampling_frequency;
    int padding;
    int extension;
    int mode;
    int mode_ext;
    int copyright;
    int original;
    int emphasis;
    int framesize;
    int II_sblimit;
    al_table2[] alloc;
    int down_sample_sblimit;
    int down_sample;
}