package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import stribog.Stribog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

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

    public void registration(ActionEvent event) throws IOException {
        Parent window;
        window = FXMLLoader.load(getClass().getResource("3.fxml"));

        Scene newScene;
        newScene = new Scene(window);

        Stage mainWindow;
        mainWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();

        mainWindow.setScene(newScene);

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

        while ((responseLine = in.readLine()) != null) {

            if (responseLine.equals("!")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setResizable(true);
                alert.getDialogPane().setPrefSize(300, 100);
                alert.setContentText("Соединение с сервером установлено.");
                alert.showAndWait();
                break;
            }

            if (responseLine.startsWith("Ошибка")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setResizable(true);
                alert.getDialogPane().setPrefSize(300, 100);
                alert.setContentText("Пользователь с таким именем уже в сети.");
                alert.showAndWait();
                return;
            }
            if (responseLine.startsWith("В доступе отказано")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setResizable(true);
                alert.getDialogPane().setPrefSize(300, 100);
                alert.setContentText("В доступе отказано.");
                alert.showAndWait();
                return;
            }
            users.add(responseLine);
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

}

