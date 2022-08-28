package com.example.threeelementsgamescrum;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class EndGameController {

    @FXML
    private Button playAgainButton;
    @FXML
    private Label winnerLabel;


    public void displayWinner(String text) {
        winnerLabel.setText(text);
    }

    public void playAgain() {
        Stage stage = (Stage) playAgainButton.getScene().getWindow();
        stage.close();
    }

    public void exitGame() {
        Platform.exit();
        System.exit(0);
    }


}
