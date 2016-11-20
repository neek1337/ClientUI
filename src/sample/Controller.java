package sample;

import javafx.application.Platform;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;

import static sample.Session.users;

/**
 * Created by nvdoshkin on 11/19/16.
 */
public class Controller {
    public TextField usersListField;
    public TextArea inputField;
    static Listener listener;
    public static StringBuilder inputString = new StringBuilder();
    public static String outputStr;
    public static ArrayList<Byte> outputByte;

    public void updateUserListField() {
        StringBuffer stringBuffer = new StringBuffer();
        for (String user : users) {
            stringBuffer.append(user + ",");
        }
        if (stringBuffer.length() > 0) {
            usersListField.setText(stringBuffer.substring(0, stringBuffer.length() - 1));
        } else {
            usersListField.setText("");
        }
    }


    public void updateInputField(char c) {
        if (c == '|') {
            inputString.append("\n");
        } else {
            inputString.append(c);
        }
        Platform.runLater(() -> {
            inputField.setText(inputString.toString());
        });
    }
}
