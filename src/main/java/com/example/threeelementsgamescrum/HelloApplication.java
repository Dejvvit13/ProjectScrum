package com.example.threeelementsgamescrum;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("game-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("css.css")).toExternalForm());
        stage.setTitle("The Elements Game");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(this.getClass().getResource("Images/vs.png"))));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}