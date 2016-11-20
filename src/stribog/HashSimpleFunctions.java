package stribog;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;

public class HashSimpleFunctions {
    public static byte[] xFunction(byte[] a, byte[] b) {
        byte[] result = new byte[64];

        for (int i = 0; i < 64; i++) {
            result[i] = (byte)(a[i] ^ b[i]);
        }

        return result;
    }

    public static byte[] sFunction(byte[] state) {
        byte[] result = new byte[64];

        for (int i = 0; i < 64; i++) {
            result[i] = HashData.S_VALUES[((int)state[i] & 0xff)];
        }
        return result;
    }

    public static byte[] pFunction(byte[] state) {
        byte[] result = new byte[64];

        for (int i = 0; i < 64; i++) {
            result[i] = state[HashData.P_VALUES[i]];
        }

        return result;
    }

    public static byte[] lFunction(byte[] state) {
        byte[] result = new byte[64];

        for (int i = 0; i < 8; i++) {
            long t = 0L;
            byte[] tempArray = new byte[8];
            ArrayUtils.copyBytesToBytes(state, i * 8, tempArray, 0, 8);
            Collections.reverse(Arrays.asList(tempArray));
            BitSet bitSet = ArrayUtils.getBitSetFromBytes(tempArray);
            ArrayUtils.reverseBitsInBitSet(bitSet);

            for (int j = 0; j < 64; j++) {
                if (bitSet.get(j) != false)
                    t = t ^ HashData.L_VALUES[j];
            }
            byte[] partResult = ArrayUtils.getBytesFromLong(t);
            Collections.reverse(Arrays.asList(partResult));
            ArrayUtils.copyBytesToBytes(partResult, 0, result, i * 8, 8);
        }

        return result;
    }

    public static byte[] eFunction(byte[] key, byte[] m) {
        byte[] state = xFunction(key, m);
        for (int i = 0; i < 12; i++) {
            state = sFunction(state);
            state = pFunction(state);
            state = lFunction(state);
            key = keySchedule(key, i);
            state = xFunction(state, key);
        }
        return state;
    }

    public static byte[] keySchedule(byte[] key, int i) {
        key = xFunction(key, HashData.K_VALUES[i]);
        key = sFunction(key);
        key = pFunction(key);
        key = lFunction(key);
        return key;
    }

    public static byte[] addMod512(byte[] a, byte[] b) {
        byte[] result = new byte[64];

        int i = 0, t = 0;
        byte[] tempA = new byte[64];
        byte[] tempB = new byte[64];
        ArrayUtils.copyBytesToBytes(a, 0, tempA, 64 - a.length, a.length);
        ArrayUtils.copyBytesToBytes(b, 0, tempB, 64 - b.length, b.length);
        for (i = 63; i >= 0; i--) {
            t = ((int)tempA[i] & 0xff) + ((int)tempB[i] & 0xff) + (t >> 8);
            result[i] = (byte)(t & 0xFF);
        }

        return result;
    }

    public static byte[] compression(byte[] N, byte[] h, byte[] m) {
        byte[] key = xFunction(h, N);
        key = sFunction(key);
        key = pFunction(key);
        key = lFunction(key);
        byte[] t = eFunction(key, m);
        t = xFunction(t, h);
        return xFunction(t, m);
    }

}
