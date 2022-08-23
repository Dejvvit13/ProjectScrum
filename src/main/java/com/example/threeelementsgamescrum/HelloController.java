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

    public ImageView fightResultPC3;
    public ImageView fightResultUser3;
    public ImageView fightResultUser2;
    public ImageView fightResultPC2;
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
    @FXML
    private ImageView fightResultUser1;
    @FXML
    private ImageView fightResultPC1;


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

    public void checkScore(String userImageUrl, String pcImageUrl, ImageView pcImage, ImageView usrImage){
        // draw
        if (userImageUrl.equals(pcImageUrl)) {
            Image imageDraw = new Image(String.valueOf(this.getClass().getResource("Images/draw.png")));
            pcImage.setImage(imageDraw);
            usrImage.setImage(imageDraw);
        }
//Check water and fire
        if (userImageUrl.equals("WaterElement.png") && pcImageUrl.equals("FireElement.png")) {
            Image imageWin = new Image(String.valueOf(this.getClass().getResource("Images/win.png")));
            Image imageLose = new Image(String.valueOf(this.getClass().getResource("Images/lose.png")));
            pcImage.setImage(imageLose);
            usrImage.setImage(imageWin);
        } else if (userImageUrl.equals("FireElement.png") && pcImageUrl.equals("WaterElement.png")){
            Image imageWin = new Image(String.valueOf(this.getClass().getResource("Images/win.png")));
            Image imageLose = new Image(String.valueOf(this.getClass().getResource("Images/lose.png")));
            pcImage.setImage(imageWin);
            usrImage.setImage(imageLose);
        }

        // Check fire and wind
        if (userImageUrl.equals("FireElement.png") && pcImageUrl.equals("WindElement.jpg")) {
            Image imageWin = new Image(String.valueOf(this.getClass().getResource("Images/win.png")));
            Image imageLose = new Image(String.valueOf(this.getClass().getResource("Images/lose.png")));
            pcImage.setImage(imageLose);
            usrImage.setImage(imageWin);
        } else if (userImageUrl.equals("WindElement.jpg") && pcImageUrl.equals("FireElement.png")){
            Image imageWin = new Image(String.valueOf(this.getClass().getResource("Images/win.png")));
            Image imageLose = new Image(String.valueOf(this.getClass().getResource("Images/lose.png")));
            pcImage.setImage(imageWin);
            usrImage.setImage(imageLose);
        }
    }
    @FXML
    public void onFirstFightButtonClick() {

        String userImageUrl = String.valueOf(imageUser1.getImage().getUrl().substring(imageUser1.getImage().getUrl().lastIndexOf('/')+1));
        String pcImageUrl = String.valueOf(imagePC1.getImage().getUrl().substring(imageUser1.getImage().getUrl().lastIndexOf('/')+1));
        checkScore(userImageUrl, pcImageUrl, fightResultPC1, fightResultUser1);
    }
    @FXML
    public void onSecondFightButtonClick() {

        String userImageUrl = String.valueOf(imageUser2.getImage().getUrl().substring(imageUser2.getImage().getUrl().lastIndexOf('/')+1));
        String pcImageUrl = String.valueOf(imagePC2.getImage().getUrl().substring(imageUser2.getImage().getUrl().lastIndexOf('/')+1));
        checkScore(userImageUrl, pcImageUrl, fightResultPC2, fightResultUser2);
    }
    @FXML
    public void onThirdFightButtonClick() {

        String userImageUrl = String.valueOf(imageUser3.getImage().getUrl().substring(imageUser3.getImage().getUrl().lastIndexOf('/')+1));
        String pcImageUrl = String.valueOf(imagePC3.getImage().getUrl().substring(imageUser3.getImage().getUrl().lastIndexOf('/')+1));
        checkScore(userImageUrl, pcImageUrl, fightResultPC3, fightResultUser3);
    }

}

