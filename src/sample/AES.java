package sample;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.*;

public class AES {
    private static String algorithm = "AES";

    // Performs Encryption
    public static String encrypt(String plainText, Key key) throws Exception {
        Cipher chiper = Cipher.getInstance(algorithm);
        chiper.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = chiper.doFinal(plainText.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encVal);
        return encryptedValue;
    }

    // Performs decryption
    public static String decrypt(String encryptedText, Key key) throws Exception {
        Cipher chiper = Cipher.getInstance(algorithm);
        chiper.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedText);
        byte[] decValue = chiper.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    //generateKey() is used to generate a secret key for AES algorithm
    protected static Key generateKey(String keyWord) throws Exception {
        Key key = new SecretKeySpec(chengeKeyLength(keyWord.getBytes()), algorithm);
        return key;
    }

    public static byte[] chengeKeyLength(byte[] key) {
        byte[] result = new byte[16];
        int i = 0;
        while (i < 15) {
            for (int j = 0; j < key.length; j++) {
                result[i] = key[j];
                i++;
                if (i == 16) {
                    break;
                }
            }
        }
        return result;
    }

    // performs encryption & decryption
    public static void main(String[] args) throws Exception {

        String plainText = "P";
        Key key = generateKey("фыв");
        String encryptedText = AES.encrypt(plainText, key);
        String decryptedText = AES.decrypt(encryptedText, key);

        System.out.println("Plain Text : " + plainText);
        System.out.println("Encrypted Text : " + encryptedText);
        System.out.println("Decrypted Text : " + decryptedText);
    }
}