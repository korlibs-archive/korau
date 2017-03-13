/*
 *      GTK plotting routines source file
 *
 *      Copyright (c) 1999 Mark Taylor
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */
package net.sourceforge.lame.mp3;

/**
 * used by the frame analyzer
 */
public class PlottingData {
    public static final int SBMAX_l = 22;
    public static final int SBMAX_s = 13;
    public double mpg123xr[][][] = new double[2][2][576];
    public double sfb[][][] = new double[2][2][SBMAX_l];
    public double sfb_s[][][] = new double[2][2][3 * SBMAX_s];
    public int qss[][] = new int[2][2];
    public int big_values[][] = new int[2][2];
    public int sub_gain[][][] = new int[2][2][3];
    public int scalefac_scale[][] = new int[2][2];
    public int preflag[][] = new int[2][2];
    public int mpg123blocktype[][] = new int[2][2];
    public int mixed[][] = new int[2][2];
    public int mainbits[][] = new int[2][2];
    public int sfbits[][] = new int[2][2];
    public int stereo, js, ms_stereo, i_stereo, emph, bitrate, sampfreq, maindata;
    public int crc, padding;
    public int scfsi[] = new int[2];
}
