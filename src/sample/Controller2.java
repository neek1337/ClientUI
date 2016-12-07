package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import stribog.Stribog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static sample.AES.generateKey;
import static sample.Session.*;

/**
 * Created by neek on 16.11.2016.
 */
public class Controller2 {
    public Button regButton;
    public Button connectButton;
    public PasswordField passwordField;
    public TextField portField;
    public TextField nameField;
    public TextField ipField;
    public CheckBox cryptEnabled;
    public TextField cryptKey;
    public Label cryptKeyLabel;

    public void registration(ActionEvent event) throws IOException {
        Parent window;
        window = FXMLLoader.load(getClass().getResource("3.fxml"));

        Scene newScene;
        newScene = new Scene(window);

        Stage mainWindow;
        mainWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();

        mainWindow.setScene(newScene);

    }

    @FXML
    public void initialize() {
        cryptEnabled.setSelected(cryptEnabledBool);
        if (cryptEnabled.isSelected()) {
            cryptKey.setVisible(true);
            cryptKeyLabel.setVisible(true);
        } else {
            cryptKey.setVisible(false);
            cryptKeyLabel.setVisible(false);
        }

    }

    public void connect(ActionEvent actionEvent) throws Exception {


        System.out.println("connect");
        String server = ipField.getText();
        String port = portField.getText();
        String name = nameField.getText();
        String password = passwordField.getText();
        Socket fromserver;
        try {
            fromserver = new Socket(server, Integer.parseInt(port));
        } catch (Exception e) {
            System.out.println("Неверно введен ip или порт");
            return;
        }


        in = new
                BufferedReader(new
                InputStreamReader(fromserver.getInputStream()));
        out = new
                PrintWriter(fromserver.getOutputStream(), true);
        out.println("connect");
        System.out.println(name);
        System.out.println(password);
        out.println(name);
        Stribog stribog = new Stribog(256);
        out.println(stribog.getHash(password));
        String responseLine;

        boolean flag = false;
        while ((responseLine = in.readLine()) != null) {

            if (responseLine.equals("!")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setResizable(true);
                alert.getDialogPane().setPrefSize(300, 100);
                alert.setContentText("Соединение с сервером установлено.");
                alert.showAndWait();
                flag = true;
                break;
            }

            users.add(responseLine);
        }
        if (!flag) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText(null);
            alert.setResizable(true);
            alert.getDialogPane().setPrefSize(300, 100);
            alert.setContentText("В доступе отказано.");
            alert.showAndWait();
            return;
        }
        if (cryptEnabledBool) {
            cryptKeySession = generateKey(cryptKey.getText());
        }

        Parent window;
        window = FXMLLoader.load(getClass().getResource("1.fxml"));

        Scene newScene;
        newScene = new Scene(window, 500, 300);
        currentName = name;
        Stage mainWindow;
        mainWindow = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        mainWindow.setTitle(currentName);
        mainWindow.setScene(newScene);
    }

    public void changeCryptEnabled(ActionEvent actionEvent) {
        if (cryptEnabled.isSelected()) {
            cryptKey.setVisible(true);
            cryptKeyLabel.setVisible(true);
            cryptEnabledBool = true;
        } else {
            cryptKey.setVisible(false);
            cryptKeyLabel.setVisible(false);
            cryptEnabledBool = false;
        }
    }
}

