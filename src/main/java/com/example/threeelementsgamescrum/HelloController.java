package com.example.threeelementsgamescrum;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class HelloController implements Initializable {

    @FXML
    public ImageView fightResultPC3; //fight result images
    @FXML
    public ImageView fightResultPC2;
    @FXML
    private ImageView fightResultPC1;
    @FXML
    public ImageView fightResultUser3;
    @FXML
    public ImageView fightResultUser2;
    @FXML
    private ImageView fightResultUser1;
    @FXML
    public Button vsButton1; //versus buttons
    @FXML
    public Button vsButton2;
    @FXML
    public Button vsButton3;
    @FXML
    private GridPane gridPanePC; // pc cards container
    @FXML
    private GridPane gridPaneUser; // user cards container

    @FXML
    private ImageView currentImageView; //currently picked card slot
    @FXML
    private ImageView imageUser1;// user images
    @FXML
    private ImageView imageUser2;
    @FXML
    private ImageView imageUser3;
    @FXML
    private ImageView imagePC1;// pc images
    @FXML
    private ImageView imagePC2;
    @FXML
    private ImageView imagePC3;

    @FXML
    private Label scoreLabel; // score text
    @FXML
    private Label roundDisplay; // round text
    private final List<String> paths = new ArrayList<>(List.of(
            String.valueOf(this.getClass().getResource("Images/FireElement.png")),
            String.valueOf(this.getClass().getResource("Images/WaterElement.png")),
            String.valueOf(this.getClass().getResource("Images/WindElement.png"))
    ));
    private int userScore;
    private int pcScore;
    private final Random random = new Random();
    private final IntegerProperty countFights = new SimpleIntegerProperty(0);
    private final IntegerProperty countRounds = new SimpleIntegerProperty(1);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generatePcCards();
        generateOnMouseClick();
        countRounds.addListener(event -> {
            roundDisplay.setText("Round " + countRounds.getValue());
            if (countRounds.getValue() > 3) {
                if (userScore == pcScore) {
                    GameOverAlert.display("Game ends with draw");
                } else if (userScore > pcScore) {
                    GameOverAlert.display("Player won");
                } else {
                    GameOverAlert.display("Computer won");
                }
                roundDisplay.setText("Round 1");
                pcScore = 0;
                userScore = 0;
                countRounds.setValue(0);
            }
            resetGame();
        });
            countFights.addListener(event -> {
            if (countFights.getValue() == 3) {
                Timeline pause = new Timeline(
               new KeyFrame(
                       Duration.millis(2000),
                       e -> {
                    countRounds.setValue(countRounds.getValue() + 1);
                    countFights.set(0);}
                ));
                pause.setCycleCount(1);
                pause.play();
            }
        });
    }

    public void generatePcCards() {
        for (Node node : gridPanePC.getChildren()) {
            if (node instanceof ImageView imageView) {
                Image image = new Image(paths.get(random.nextInt(0, 3)));
                imageView.setOpacity(0);
                imageView.setImage(image);
            }
        }
    }

    @FXML
    public void onFireButtonClick() {
        if (currentImageView != null && currentImageView.getOnMouseClicked() != null) {
            Image image = new Image(String.valueOf(this.getClass().getResource("Images/FireElement.png")));
            currentImageView.setImage(image);
        }
    }

    @FXML
    public void onWaterButtonClick() {
        if (currentImageView != null && currentImageView.getOnMouseClicked() != null) {
            Image image = new Image(String.valueOf(this.getClass().getResource("Images/WaterElement.png")));
            currentImageView.setImage(image);
        }
    }

    @FXML
    public void onWindButtonClick() {
        if (currentImageView != null && currentImageView.getOnMouseClicked() != null) {
            Image image = new Image(String.valueOf(this.getClass().getResource("Images/WindElement.png")));
            currentImageView.setImage(image);
        }
    }

    public void checkScore(String userImageUrl, String pcImageUrl, ImageView pcImage, ImageView usrImage) {
        Image imageDraw = new Image(String.valueOf(this.getClass().getResource("Images/draw.png")));
        Image imageWin = new Image(String.valueOf(this.getClass().getResource("Images/win.png")));
        Image imageLose = new Image(String.valueOf(this.getClass().getResource("Images/lose.png")));

        // draw
        if (userImageUrl.equals(pcImageUrl)) {
            pcImage.setImage(imageDraw);
            usrImage.setImage(imageDraw);
        }

        //Check water and fire
        if (userImageUrl.equals("WaterElement.png") && pcImageUrl.equals("FireElement.png")) {
            pcImage.setImage(imageLose);
            usrImage.setImage(imageWin);
            userScore++;
        } else if (userImageUrl.equals("FireElement.png") && pcImageUrl.equals("WaterElement.png")) {
            pcImage.setImage(imageWin);
            usrImage.setImage(imageLose);
            pcScore++;
        }

        //check water and wind
        if (userImageUrl.equals("WaterElement.png") && pcImageUrl.equals("WindElement.png")) {
            pcImage.setImage(imageWin);
            usrImage.setImage(imageLose);
            pcScore++;
        } else if (userImageUrl.equals("WindElement.png") && pcImageUrl.equals("WaterElement.png")) {
            pcImage.setImage(imageLose);
            usrImage.setImage(imageWin);
            userScore++;
        }
        // Check fire and wind
        if (userImageUrl.equals("FireElement.png") && pcImageUrl.equals("WindElement.png")) {
            pcImage.setImage(imageLose);
            usrImage.setImage(imageWin);
            userScore++;
        } else if (userImageUrl.equals("WindElement.png") && pcImageUrl.equals("FireElement.png")) {
            pcImage.setImage(imageWin);
            usrImage.setImage(imageLose);
            pcScore++;
        }

    }

    @FXML
    public void onFirstFightButtonClick() {
        String userImageUrl = imageUser1.getImage().getUrl().substring(imageUser1.getImage().getUrl().lastIndexOf('/') + 1);
        String pcImageUrl = imagePC1.getImage().getUrl().substring(imageUser1.getImage().getUrl().lastIndexOf('/') + 1);
        checkScore(userImageUrl, pcImageUrl, fightResultPC1, fightResultUser1);
        imagePC1.setOpacity(1);
        imageUser1.setOnMouseClicked(null);
        vsButton1.setDisable(true);
        scoreLabel.setText("Player - %d : %d - Computer".formatted(userScore, pcScore));
        countFights.setValue(countFights.getValue() + 1);
    }

    @FXML
    public void onSecondFightButtonClick() {
        String userImageUrl = imageUser2.getImage().getUrl().substring(imageUser2.getImage().getUrl().lastIndexOf('/') + 1);
        String pcImageUrl = imagePC2.getImage().getUrl().substring(imageUser2.getImage().getUrl().lastIndexOf('/') + 1);
        checkScore(userImageUrl, pcImageUrl, fightResultPC2, fightResultUser2);
        imagePC2.setOpacity(1);
        imageUser2.setOnMouseClicked(null);
        vsButton2.setDisable(true);
        scoreLabel.setText("Player - %d : %d - Computer".formatted(userScore, pcScore));
        countFights.setValue(countFights.getValue() + 1);

    }

    @FXML
    public void onThirdFightButtonClick() {
        String userImageUrl = imageUser3.getImage().getUrl().substring(imageUser3.getImage().getUrl().lastIndexOf('/') + 1);
        String pcImageUrl = imagePC3.getImage().getUrl().substring(imageUser3.getImage().getUrl().lastIndexOf('/') + 1);
        checkScore(userImageUrl, pcImageUrl, fightResultPC3, fightResultUser3);
        imagePC3.setOpacity(1);
        imageUser3.setOnMouseClicked(null);
        vsButton3.setDisable(true);
        scoreLabel.setText("Player - %d : %d - Computer".formatted(userScore, pcScore));
        countFights.setValue(countFights.getValue() + 1);

    }



    public void onResetGameButtonClick(){
        resetGame();
        pcScore =0;
        userScore =0;
        countFights.set(0);
        countRounds.set(0);

    }

    public void resetGame() {
        imageUser1.setImage(null);
        imageUser2.setImage(null);
        imageUser3.setImage(null);
        imagePC1.setImage(null);
        imagePC2.setImage(null);
        imagePC3.setImage(null);
        fightResultPC1.setImage(null);
        fightResultPC2.setImage(null);
        fightResultPC3.setImage(null);
        fightResultUser1.setImage(null);
        fightResultUser2.setImage(null);
        fightResultUser3.setImage(null);
        vsButton1.setDisable(false);
        vsButton2.setDisable(false);
        vsButton3.setDisable(false);
        generatePcCards();
        generateOnMouseClick();
        scoreLabel.setText("Player - %d : %d - Computer".formatted(userScore, pcScore));

    }

    public void generateOnMouseClick() {
        for (Node node : gridPaneUser.getChildren()) {
            if (node instanceof ImageView imageView) {
                imageView.setOnMouseClicked(e -> currentImageView = imageView);
            }
        }
    }
}

