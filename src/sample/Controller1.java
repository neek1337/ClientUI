package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import static sample.Session.*;

public class Controller1 extends Controller {
    public Button connectButton;
    public Button sendButton;
    public TextField ipField;
    public TextField portField;
    public TextField nameField;

    public static boolean stoped;
    public TableView tableview;
    public TableColumn nameColumn;
    public TableColumn pathColumn;
    private final ObservableList<Row> data =
            FXCollections.observableArrayList();
    public TextField addingNameField;
    public TextField addingPathField;
    public Button addButton;
    public TextField usersListField;


    public static HashMap<String, BBs> keys = new HashMap<>();


    public Button hideButton;

    @FXML
    public void initialize() {
        tableview.setPlaceholder(new Label(""));
        for (String s : keys.keySet()) {
            addRow(s, keys.get(s).getXstr());
        }
        updateUserListField();
        listener = new Listener();
        listener.controller = this;
        Thread thread = new Thread(listener);
        thread.start();
    }


    public void addRow(ActionEvent actionEvent) {
        BBs generator = new BBs(addingPathField.getText());
        keys.put(addingNameField.getText(), generator);
        tableview.setEditable(true);
        nameColumn.setCellValueFactory(new PropertyValueFactory<Row, String>("name"));
        pathColumn.setCellValueFactory(new PropertyValueFactory<Row, String>("path"));
        data.add(new Row(addingNameField.getText(), addingPathField.getText()));
        tableview.setItems(data);

    }

    public void addRow(String key, String value) {
        tableview.setEditable(true);
        nameColumn.setCellValueFactory(new PropertyValueFactory<Row, String>("name"));
        pathColumn.setCellValueFactory(new PropertyValueFactory<Row, String>("path"));
        data.add(new Row(key, value));
        tableview.setItems(data);

    }

    public void deleteRow(ActionEvent actionEvent) {
        Row selectedRow = (Row) tableview.getSelectionModel().getSelectedItem();
        keys.remove(addingNameField.getText());
        data.remove(selectedRow);

    }


    public void hide(ActionEvent event) throws IOException {
        Parent window;
        window = FXMLLoader.load(getClass().getResource("hide.fxml"));

        Scene newScene;
        newScene = new Scene(window);

        Stage mainWindow;
        mainWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        mainWindow.setTitle(currentName);
        mainWindow.setScene(newScene);
    }
}
