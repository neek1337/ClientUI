package sample;

import java.math.BigInteger;

public class BBs {
    BigInteger p, q, n, e, x;
    String xstr;

    BBs(String xStr) {
        this.xstr = xStr;
        p = new BigInteger("128937129837129873");
        q = new BigInteger("2348972389472387482937");
        e = new BigInteger("2");
        x = new BigInteger(String.valueOf(xStr.hashCode()));
        n = p.multiply(q);
        x = x.modPow(e, n);
    }

    public byte lastBite() {
        x = x.modPow(e, n);
        if (x.testBit(0)) {
            return 1;
        } else {
            return 0;
        }
    }


    int next() {
        int result = 0;
        for (int i = 0; i < 16; i++) {
            result = (result << 1) | lastBite();
        }
        return result % 256;
    }

    public String getXstr() {
        return xstr;
    }
}
