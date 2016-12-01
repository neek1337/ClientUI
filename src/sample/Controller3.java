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

import static sample.Session.*;

/**
 * Created by neek on 16.11.2016.
 */
public class Controller3 {
    public Button regButton;
    public TextField portField;
    public TextField nameField;
    public TextField ipField;
    public Button backButton;
    public PasswordField pswdField1;
    public PasswordField pswdField2;

    public void registration(ActionEvent event) throws Exception {
        System.out.println("registration");
        String server = ipField.getText();
        String port = portField.getText();
        String name = nameField.getText();
        String password1 = pswdField1.getText();
        String password2 = pswdField2.getText();
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
        out.println("registration");
        if (!password1.equals(password2)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText(null);
            alert.setResizable(true);
            alert.getDialogPane().setPrefSize(300, 100);
            alert.setContentText("Введенные пароли не совпадают.");
            alert.showAndWait();
            return;
        }
        Stribog stribog = new Stribog(256);
        out.println(name);
        out.println(stribog.getHash(password1));

        String responseLine;

        while ((responseLine = in.readLine()) != null) {
            if (responseLine.equals("!")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setResizable(true);
                alert.getDialogPane().setPrefSize(300, 100);
                alert.setContentText("Регистрация прошла успешно.");
                alert.showAndWait();
                break;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setResizable(true);
                alert.getDialogPane().setPrefSize(300, 100);
                alert.setContentText("При регистрации произошла ошибка.");
                alert.showAndWait();
                return;
            }

        }
        Parent window;
        window = FXMLLoader.load(getClass().getResource("2.fxml"));

        Scene newScene;
        newScene = new Scene(window);

        Stage mainWindow;
        mainWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();

        mainWindow.setScene(newScene);
    }


    public void back(ActionEvent event) throws IOException {
        Parent window;
        window = FXMLLoader.load(getClass().getResource("2.fxml"));

        Scene newScene;
        newScene = new Scene(window);

        Stage mainWindow;
        mainWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();

        mainWindow.setScene(newScene);
    }
}

