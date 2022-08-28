package com.example.threeelementsgamescrum;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameOverAlert extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(GameOverAlert.class.getResource("gameOverAlert.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setTitle("Game Over");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
