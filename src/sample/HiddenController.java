package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static sample.Session.*;

/**
 * Created by nvdoshkin on 11/19/16.
 */
public class HiddenController extends Controller {

    public Button sendButton;
    public TextArea text;


    public static HashMap<String, BBs> keys = new HashMap<>();
    public static String alphabet = " абвгдежзийклмнопрстуфхцчшщьыэюя";

    public Button hideButton;

    @FXML
    public void initialize() {
        updateUserListField();
        inputField.setText(inputString.toString());
        listener.controller = this;
    }


    public void send(ActionEvent actionEvent) throws InterruptedException {
        outputStr = text.getText().toLowerCase() + "|";
        outputStr = outputStr.replaceAll("ё", "е");
        outputStr = outputStr.replaceAll("ъ", "ь");

        if (outputStr.contains("\n")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText(null);
            alert.setContentText("Сообщение не должно содержать символ переноса коретки.");
            alert.showAndWait();
            return;
        }
        outputByte = strToByteArr(outputStr);
        while (outputStr != null && outputByte != null && startIndex == -1) {
            out.println("?" + outputStr.length() * 16);
            Thread.sleep(1000);
        }
        text.setText("");
    }

/*    public ArrayList<Byte> strToByteArr(String s) {
        ArrayList<Byte> result = new ArrayList<>();
        for (Character c : s.toCharArray()) {
            int index;
            if (alphabet.indexOf(c) >= 0) {
                index = alphabet.indexOf(c);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setContentText("Допустимыми символами являются буквы русского алфавита и пробел.");
                alert.showAndWait();
                return null;
            }
            for (int i = 0; i < 5; i++) {
                result.add((byte) (index % 2));
                index = index / 2;
            }
        }

        return result;
    }*/

    public ArrayList<Byte> strToByteArr(String s) {
        ArrayList<Byte> result = new ArrayList<>();
        for (char c : s.toCharArray()) {
            System.out.println((int) c);
            String local = Integer.toBinaryString(c);
            for (int i = 0; i < 16 - local.length(); i++) {
                result.add((byte) 0);
            }
            for (char c1 : local.toCharArray()) {
                if (c1 == '0') {
                    result.add((byte) 0);
                } else if (c1 == '1') {
                    result.add((byte) 1);
                } else {
                    System.out.println("Ошибка!");
                }
            }
        }


        return result;
    }


    public void hide(ActionEvent event) throws IOException {
        Parent window;
        window = FXMLLoader.load(getClass().getResource("1.fxml"));

        Scene newScene;
        newScene = new Scene(window);

        Stage mainWindow;
        mainWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        mainWindow.setTitle(currentName);
        mainWindow.setScene(newScene);
    }
}
