package stribog;

import java.nio.ByteBuffer;
import java.util.BitSet;

public class ArrayUtils {
    private static final int BYTES_IN_LONG = 8;
    private static final int BYTES_IN_INT  = 4;

    public static void copyBytesToBytes(byte[] source, int sourceStart, byte[] destination, int destinStart, int size) {
        for (int i = 0; i < size; i++) {
            destination[destinStart + i] = source[sourceStart + i];
        }
    }

    public static boolean[] getBooleanFromByte(final byte b) {
        return new boolean[] {
                (b &    1) != 0,
                (b &    2) != 0,
                (b &    4) != 0,
                (b &    8) != 0,
                (b & 0x10) != 0,
                (b & 0x20) != 0,
                (b & 0x40) != 0,
                (b & 0x80) != 0
        };
    }

    public static BitSet getBitSetFromBytes(byte[] bytes) {
        BitSet bits = new BitSet();
        for (int i = 0; i < bytes.length * 8; i++) {
            if ((bytes[bytes.length - i / 8 - 1] & (1 << (i % 8))) > 0) {
                bits.set(i);
            }
        }
        return bits;
    }

    public static void reverseBitsInBitSet(BitSet bits) {
        for (int i = 0; i < bits.size() / 2; i++) {
            boolean temp1 = bits.get(i);
            boolean temp2 = bits.get(bits.size()-i-1);
            bits.set(i, temp2);
            bits.set(bits.size()-i-1, temp1);
        }
    }

    public static byte[] getBytesFromLong(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(BYTES_IN_LONG);
        buffer.putLong(x);
        return buffer.array();
    }

    public static byte[] getBytesFromInt(int x) {
        ByteBuffer buffer = ByteBuffer.allocate(BYTES_IN_INT);
        buffer.putInt(x);
        return buffer.array();
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
}
