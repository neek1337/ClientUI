package sample;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static sample.AES.decrypt;
import static sample.Controller1.*;
import static sample.Session.*;

public class Listener implements Runnable {
    Controller controller;
    private static String encryptedValue = "";


    public void run() {
        String responseLine;
        try {
            while ((responseLine = in.readLine()) != null) {
                if (responseLine.startsWith("START")) {
                    Session.startIndex = Integer.valueOf(responseLine.substring(5));
                }
                if (responseLine.startsWith("+")) {
                    users.add(responseLine.substring(1, responseLine.length()));
                    controller.updateUserListField();
                }
                if (responseLine.startsWith("-")) {
                    users.remove(responseLine.substring(1, responseLine.length()));
                    controller.updateUserListField();
                }
                if (responseLine.startsWith("|")) {
                    if (responseLine.length() == 1) {
                        responseLine = in.readLine();
                    }
                    if (cryptEnabledBool) {
                        if (responseLine.charAt(1) != '|') {
                            encryptedValue += responseLine.charAt(1);
                        } else {
                            controller.updateInputField(decrypt(encryptedValue, cryptKeySession));
                            encryptedValue = "";
                        }
                    } else {
                        controller.updateInputField(responseLine.charAt(1));
                    }
                }
                if (responseLine.startsWith("*")) {
                    int result = 0;
                    String indexStr = responseLine.substring(1);
                    int index = Integer.valueOf(indexStr);
                    for (String user : users) {
                        if (keys.containsKey(user)) {
                            int next = keys.get(user).next();
                            result += next;
                        }
                    }
                    if (outputByte != null && outputByte.size() > 0 && startIndex != -1) {
                        result += outputByte.get(index - startIndex);
                    }
                    if (outputByte != null && outputByte.size() + startIndex == index + 1) {
                        startIndex = -1;
                        outputStr = null;
                    }

                    out.println("*" + index + "*" + result % 2);
                    System.out.println(result % 2);
                }
                System.out.println("Получено от сервера: " + responseLine);
                Controller1.stoped = false;
            }
        } catch (IOException e) {
            System.out.println("Ошибка соединения с сервером. Для выхода нажмите Enter" +
                    "");
            Controller1.stoped = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
