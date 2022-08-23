package com.example.threeelementsgamescrum;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    private int roundCounter;
    @FXML
    private GridPane gridPanePC;
    @FXML
    private GridPane gridPaneUser;
    @FXML
    private TextField roundDisplay;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        roundDisplay.setText("ROUND  " + roundCounter);
    }
}

