package net.sourceforge.lame.mp3;

/**
 * Structure to receive extracted header (toc may be null).
 *
 * @author Ken
 */
public class VBRTagData {
    public int frames;
    public int headersize;
    public int encDelay;
    public int encPadding;
    protected int hId;
    protected int samprate;
    protected int flags;
    protected int bytes;
    protected int vbrScale;
    protected byte[] toc = new byte[VBRTag.NUMTOCENTRIES];
}
