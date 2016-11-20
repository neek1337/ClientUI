package stribog;

import java.util.Arrays;
import java.util.Collections;

public class Stribog {

    //This might be either 256 bits or 512 bits
    private Integer hashLength;

    public Integer getHashLength() {
        return hashLength;
    }

    public void setHashLength(Integer hashLength) {
        this.hashLength = hashLength;
    }

    private byte[] initial = new byte[64];

    private byte[] N = new byte[64];

    private byte[] Sigma = new byte[64];

    public Stribog(int outputLenght) throws Exception {
        if (outputLenght == 512) {
            for (int i = 0; i < 64; i++) {
                N[i] = 0x00;
                Sigma[i] = 0x00;
                initial[i] = 0x00;
            }
            hashLength = new Integer(512);
        }
        else if (outputLenght == 256) {
            for (int i = 0; i < 64; i++) {
                N[i] = 0x00;
                Sigma[i] = 0x00;
                initial[i] = 0x01;
            }
            hashLength = new Integer(256);
        }
        else {
            throw new Exception("Wrong lenght!");
        }
    }

    public String  getHash(String messageStr) throws Exception {
        byte[] message = messageStr.getBytes();
        byte[] paddedMes = new byte[64];
        int lengthOfMessageInBits = message.length * 8;
        byte[] h = Arrays.copyOf(initial, 64);
        byte[] N_0 ={
                0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
                0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
                0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
                0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00
        };

        if (hashLength == 512) {
            for (int i = 0; i < 64; i++) {
                N[i] = 0x00;
                Sigma[i] = 0x00;
                initial[i] = 0x00;
            }
        }
        else if (hashLength == 256) {
            for (int i = 0; i < 64; i++) {
                N[i] = 0x00;
                Sigma[i] = 0x00;
                initial[i] = 0x01;
            }
        }
        else {
            throw new Exception("Wrong lenght!");
        }

        byte[] N_512 = ArrayUtils.getBytesFromInt(512);
        Collections.reverse(Arrays.asList(N_512));
        int i = 0;
        while (lengthOfMessageInBits >= 512) {
            i++;
            byte[] tempMessage = new byte[64];
            ArrayUtils.copyBytesToBytes(message, message.length - i*64, tempMessage, 0, 64);
            h = HashSimpleFunctions.compression(N, h, tempMessage);
            N = HashSimpleFunctions.addMod512(N, N_512);
            Sigma = HashSimpleFunctions.addMod512(Sigma, tempMessage);
            lengthOfMessageInBits -= 512;
        }

        byte[] message1 = new byte[message.length - i*64];
        ArrayUtils.copyBytesToBytes(message, 0, message1, 0, message.length - i*64);
        if (message1.length < 64) {
            for (int j = 0; j < (64 - message1.length - 1); j++) {
                paddedMes[j] = 0;
            }
            paddedMes[64 - message1.length - 1] = 0x01;
            ArrayUtils.copyBytesToBytes(message1, 0, paddedMes, 64 - message1.length, message1.length);
        }

        h= HashSimpleFunctions.compression(N, h, paddedMes);
        byte[] messageLength = ArrayUtils.getBytesFromInt(message1.length * 8);
        Collections.reverse(Arrays.asList(messageLength));

        N = HashSimpleFunctions.addMod512(N, messageLength);
        Sigma = HashSimpleFunctions.addMod512(Sigma, paddedMes);
        h = HashSimpleFunctions.compression(N_0, h, N);
        h = HashSimpleFunctions.compression(N_0, h, Sigma);

        if (hashLength == 512) {
            return byteArrayToHex(h);
        } else {
            byte[] h256 = new byte[32];
            ArrayUtils.copyBytesToBytes(h, 0, h256, 0, 32);
            return byteArrayToHex(h256);
        }
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }

}