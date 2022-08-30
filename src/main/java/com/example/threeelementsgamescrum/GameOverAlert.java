package com.example.threeelementsgamescrum;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameOverAlert extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(GameOverAlert.class.getResource("gameOverAlert.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.getIcons().add(new Image(String.valueOf(this.getClass().getResource("Images/vs.png"))));
        primaryStage.setTitle("Game Over");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
