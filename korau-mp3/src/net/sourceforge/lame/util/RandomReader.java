package net.sourceforge.lame.util;

import org.jetbrains.annotations.NotNull;

import java.io.*;

abstract public class RandomReader implements DataInput, Closeable {
    abstract public long length() throws IOException;

    abstract public long getFilePointer() throws IOException;

    abstract public void seek(long position) throws IOException;

    /*
    @Override
    public void write(int b) throws IOException {
        byte[] bytes = {(byte) b};
        write(bytes, 0, 1);
    }

    @Override
    public void write(@NotNull byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    abstract public void write(byte[] buffer, int pos, int len) throws IOException;

    @Override
    public void writeBoolean(boolean v) throws IOException {
        write(v ? 1 : 0);
    }

    @Override
    public void writeByte(int v) throws IOException {
        write(v);
    }

    @Override
    public void writeShort(int v) throws IOException {
        write((v >>> 8) & 0xFF);
        write((v >>> 0) & 0xFF);
    }

    @Override
    public void writeChar(int v) throws IOException {
        writeShort(v);
    }

    @Override
    public void writeInt(int v) throws IOException {
        write((v >>> 24) & 0xFF);
        write((v >>> 16) & 0xFF);
        write((v >>> 8) & 0xFF);
        write((v >>> 0) & 0xFF);
    }

    @Override
    public void writeLong(long v) throws IOException {
        write((int) (v >>> 56) & 0xFF);
        write((int) (v >>> 48) & 0xFF);
        write((int) (v >>> 40) & 0xFF);
        write((int) (v >>> 32) & 0xFF);
        write((int) (v >>> 24) & 0xFF);
        write((int) (v >>> 16) & 0xFF);
        write((int) (v >>> 8) & 0xFF);
        write((int) (v >>> 0) & 0xFF);
    }

    @Override
    public void writeFloat(float v) throws IOException {
        writeInt(Float.floatToIntBits(v));
    }

    @Override
    public void writeDouble(double v) throws IOException {
        writeLong(Double.doubleToLongBits(v));
    }

    private void writeBytes(byte b[], int off, int len) throws IOException {
        write(b, off, len);
    }

    @Override
    public void writeBytes(@NotNull String s) throws IOException {
        int len = s.length();
        byte[] b = new byte[len];
        s.getBytes(0, len, b, 0);
        writeBytes(b, 0, len);
    }

    @Override
    public void writeChars(@NotNull String s) throws IOException {
        int clen = s.length();
        int blen = 2 * clen;
        byte[] b = new byte[blen];
        char[] c = new char[clen];
        s.getChars(0, clen, c, 0);
        for (int i = 0, j = 0; i < clen; i++) {
            b[j++] = (byte) (c[i] >>> 8);
            b[j++] = (byte) (c[i] >>> 0);
        }
        writeBytes(b, 0, blen);
    }

    @Override
    public void writeUTF(@NotNull String s) throws IOException {
        //DataOutputStream.writeUTF(s, this);
        write(s.getBytes("UTF-8"));
    }
    */

    abstract public int read(byte[] buffer, int pos, int len) throws IOException;

    public int read() throws IOException {
        byte[] temp = new byte[1];
        if (read(temp, 0, 1) > 0) {
            return temp[0];
        } else {
            return -1;
        }
    }

    final public void readFully(byte[] out) throws IOException {
        readFully(out, 0, out.length);
    }

    final public void readFully(byte[] out, int pos, int len) throws IOException {
        final int end = pos + len;
        while (true) {
            int remaining = end - pos;
            if (remaining <= 0) break;
            pos += read(out, pos, remaining);
        }
    }

    abstract public void close() throws IOException;

    final public int skipBytes(int count) throws IOException {
        long start = this.getFilePointer();
        long end = this.length();
        long target = start + count;
        long actual = Math.min(target, end);
        this.seek(actual);
        return (int) (actual - target);
    }

    @Override
    public boolean readBoolean() throws IOException {
        return readUnsignedByte() != 0;
    }

    @Override
    public byte readByte() throws IOException {
        return (byte) readUnsignedByte();
    }

    public int readUnsignedByte() throws IOException {
        int res = read();
        if (res < 0) throw new EOFException();
        return res & 0xFF;
    }

    @Override
    public short readShort() throws IOException {
        return (short) readUnsignedShort();
    }

    @Override
    public int readUnsignedShort() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (ch1 << 8) + (ch2 << 0);
    }

    @Override
    public char readChar() throws IOException {
        return (char) readUnsignedShort();
    }

    @Override
    public final int readInt() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        int ch3 = this.read();
        int ch4 = this.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

    @Override
    public final long readLong() throws IOException {
        return ((long) (readInt()) << 32) + (readInt() & 0xFFFFFFFFL);
    }

    @Override
    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    @Override
    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    @Override
    public final String readLine() throws IOException {
        StringBuffer input = new StringBuffer();
        int c = -1;
        boolean eol = false;

        while (!eol) {
            switch (c = read()) {
                case -1:
                case '\n':
                    eol = true;
                    break;
                case '\r':
                    eol = true;
                    long cur = getFilePointer();
                    if ((read()) != '\n') {
                        seek(cur);
                    }
                    break;
                default:
                    input.append((char) c);
                    break;
            }
        }

        if ((c == -1) && (input.length() == 0)) {
            return null;
        }
        return input.toString();
    }

    @NotNull
    @Override
    public final String readUTF() throws IOException {
        return DataInputStream.readUTF(this);
    }
}
