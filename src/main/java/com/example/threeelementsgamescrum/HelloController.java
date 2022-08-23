package com.example.threeelementsgamescrum;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    private List<String> paths = new ArrayList<>(List.of(
            String.valueOf(this.getClass().getResource("Images/FireElement.png")),
            String.valueOf(this.getClass().getResource("Images/WaterElement.png")),
            String.valueOf(this.getClass().getResource("Images/WindElement.jpg"))
    ));
    private int roundCounter;
    @FXML
    private GridPane gridPanePC;
    @FXML
    private GridPane gridPaneUser;
    @FXML
    private TextField roundDisplay;
    private String currentPlayerCard;
    private ImageView currentImageView;
    int index = 1;
    @FXML
    private ImageView imageUser1;
    @FXML
    private ImageView imageUser2;  // user images
    @FXML
    private ImageView imageUser3;
    @FXML
    private ImageView imagePC1;
    @FXML
    private ImageView imagePC2;  // pc images
    @FXML
    private ImageView imagePC3;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generatePcCards();

        for (Node node : gridPaneUser.getChildren()) {
            if (index == 4) {
                index = 1;
            }
            if (node instanceof ImageView imageView) {
                imageView.setOnMouseClicked(e -> {
                    this.currentPlayerCard = imageView.getId();
                    this.currentImageView = imageView;
                    System.out.println(currentPlayerCard);

                });
            }
        }
    }

    public void generatePcCards() {

        Collections.shuffle(this.paths);
        int index = 0;
        for (Node node : gridPanePC.getChildren()) {
            if (node instanceof ImageView imageView) {
                Image image = new Image(paths.get(index++));
                imageView.setOpacity(0.2);
                imageView.setImage(image);
            }
        }
    }

    @FXML
    public void onFireButtonClick() {
        if (currentImageView != null) {
            Image image = new Image(String.valueOf(this.getClass().getResource("Images/FireElement.png")));
            currentImageView.setImage(image);
        }
    }

    @FXML
    public void onWaterButtonClick() {
        if (currentImageView != null) {
            Image image = new Image(String.valueOf(this.getClass().getResource("Images/WaterElement.png")));
            currentImageView.setImage(image);
        }
    }

    @FXML
    public void onWindButtonClick() {
        if (currentImageView != null) {
            Image image = new Image(String.valueOf(this.getClass().getResource("Images/WindElement.jpg")));
            currentImageView.setImage(image);
        }
    }

    @FXML
    public void onFirstFightButtonClick() {

        if (imageUser1.getId().equals(imagePC1)){



        }


    }

}

