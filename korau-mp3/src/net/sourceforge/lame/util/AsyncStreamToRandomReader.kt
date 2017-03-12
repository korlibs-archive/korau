package net.sourceforge.lame.util

import com.soywiz.korio.async.syncTest
import com.soywiz.korio.stream.AsyncStream

fun AsyncStreamToRandomReader(s: AsyncStream): RandomReader {
    return object : RandomReader() {
        override fun close() = syncTest {
            s.close()
        }

        override fun length(): Long {
            var out = 0L
            syncTest {
                out = s.getLength()
            }
            return out
        }

        override fun getFilePointer(): Long {
            var out = 0L
            syncTest {
                out = s.getPosition()
            }
            return out
        }

        override fun seek(position: Long) = syncTest {
            s.setPosition(position)
        }

        override fun write(buffer: ByteArray, pos: Int, len: Int) = syncTest {
            s.write(buffer, pos, len)
        }

        override fun read(buffer: ByteArray, pos: Int, len: Int): Int {
            var out = 0
            syncTest {
                out = s.read(buffer, pos, len)
            }
            return out
        }
    }
}