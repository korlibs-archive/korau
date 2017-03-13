package net.sourceforge.lame.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileRandomReader extends RandomReader {
    RandomAccessFile raf;

    public FileRandomReader(RandomAccessFile raf) {
        this.raf = raf;
    }

    public FileRandomReader(String path, String mode) throws FileNotFoundException {
        this.raf = new RandomAccessFile(path, mode);
    }

    @Override
    public long length() throws IOException {
        return raf.length();
    }

    @Override
    public long getFilePointer() throws IOException {
        return raf.getFilePointer();
    }

    @Override
    public void seek(long position) throws IOException {
        raf.seek(position);
    }

    //@Override
    //public void write(byte[] buffer, int pos, int len) throws IOException {
    //    raf.write(buffer, pos, len);
    //}

    @Override
    public int read(byte[] buffer, int pos, int len) throws IOException {
        return raf.read(buffer, pos, len);
    }

    @Override
    public void close() throws IOException {
        raf.close();
    }
}
