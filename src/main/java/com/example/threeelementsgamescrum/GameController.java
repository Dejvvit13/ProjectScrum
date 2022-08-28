package com.example.threeelementsgamescrum;


import javafx.animation.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GameController implements Initializable {

    @FXML
    private Label computerWonRoundsLabel;
    @FXML
    private Label playerWonRoundsLabel;
    //1- left image 2 - middle image 3 -  right image
    // Result image for PC
    @FXML
    private ImageView computerResultImage3;
    @FXML
    private ImageView computerResultImage2;
    @FXML
    private ImageView computerResultImage1;

    // Result image for player
    @FXML
    private ImageView playerResultImage3;
    @FXML
    private ImageView playerResultImage2;
    @FXML
    private ImageView playerResultImage1;

    //versus buttons
    @FXML
    private Button vsButton1;
    @FXML
    private Button vsButton2;
    @FXML
    private Button vsButton3;
    @FXML
    private GridPane gridPaneComputer; // pc cards container
    @FXML
    private GridPane gridPanePlayer; // player cards container

    // player imageViews
    @FXML
    private ImageView playerImageView1;
    @FXML
    private ImageView playerImageView2;
    @FXML
    private ImageView playerImageView3;

    // pc imageViews
    @FXML
    private ImageView computerImageView1;
    @FXML
    private ImageView computerImageView2;
    @FXML
    private ImageView computerImageView3;

    @FXML
    private Label scoreLabel; // score text
    @FXML
    private Label roundLabel; // round text

    private final List<String> pathsToGameImages = new ArrayList<>(List.of(
            String.valueOf(this.getClass().getResource("Images/FireCard.png")),
            String.valueOf(this.getClass().getResource("Images/WaterCard.png")),
            String.valueOf(this.getClass().getResource("Images/WindCard.png"))
    ));
    private ImageView currentImageView; //currently picked card slot
    private List<ImageView> currentPickedImageViews = new ArrayList<>();
    private List<Image> computerGeneratedCards = new ArrayList<>();
    private final Random random = new Random();
    private int playerScore;
    private int computerScore;
    private final IntegerProperty countFights = new SimpleIntegerProperty(0);
    private final IntegerProperty countRounds = new SimpleIntegerProperty(1);
    private int playerWonRounds;
    private int domputerWonRounds;
    protected static final Image backOfCard = new Image(String.valueOf(GameController.class.getResource("Images/BackOfCard.png")));
    private final Image windCard = new Image(String.valueOf(this.getClass().getResource("Images/WindCard.png")));
    private final Image fireCard = new Image(String.valueOf(this.getClass().getResource("Images/FireCard.png")));
    private final Image waterCard = new Image(String.valueOf(this.getClass().getResource("Images/WaterCard.png")));
    private final Image draw = new Image(String.valueOf(this.getClass().getResource("Images/draw.png")));
    private final Image win = new Image(String.valueOf(this.getClass().getResource("Images/win.png")));
    private final Image lose = new Image(String.valueOf(this.getClass().getResource("Images/lose.png")));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generatePcCards();
        setCurrentImageViewOnClick();
        setPlayerBackCard();
        setRoundsSystem();
        setScoreSystem();
    }

    private void setRoundsSystem() {
        this.countRounds.addListener(event -> {
            this.roundLabel.setText("Round " + this.countRounds.getValue());

            if (this.computerScore > this.playerScore) {
                this.domputerWonRounds++;
                this.computerWonRoundsLabel.setText("Computer won rounds: " + this.domputerWonRounds);
            }
            if (this.computerScore < this.playerScore) {
                this.playerWonRounds++;
                this.playerWonRoundsLabel.setText("Player won rounds: " + this.playerWonRounds);
            }

            if (this.playerWonRounds == 2) {
                openAlertStage("Player Won");
                playAgain();

            } else if (this.domputerWonRounds == 2) {
                openAlertStage("Computer Won");
                playAgain();
            }

            if (this.countRounds.getValue() > 3) {
                if (this.playerWonRounds == this.domputerWonRounds && this.countRounds.getValue() > 3) {
                    openAlertStage("It's DRAW");
                    playAgain();
                } else if (this.playerWonRounds > this.domputerWonRounds) {
                    openAlertStage("Player Won");
                    playAgain();
                } else {
                    openAlertStage("Computer Won");
                    playAgain();
                }
            }
            resetGameSettings();
        });

    }

    private void setScoreSystem() {
        this.countFights.addListener(event -> {
            if (this.countFights.getValue() == 3) {
                Timeline roundChange = new Timeline(
                        new KeyFrame(
                                Duration.millis(2000),
                                e -> {
                                    this.countRounds.setValue(this.countRounds.getValue() + 1);
                                    this.countFights.set(0);
                                    this.playerScore = 0;
                                    this.computerScore = 0;
                                    this.scoreLabel.setText("Player - 0 : 0 - Computer");
                                }
                        ));
                roundChange.setCycleCount(1);
                roundChange.play();
            }
        });
    }

    private void setPlayerBackCard() {
        this.playerImageView1.setImage(backOfCard);
        this.playerImageView2.setImage(backOfCard);
        this.playerImageView3.setImage(backOfCard);
        this.playerImageView1.setRotate(0);
        this.playerImageView2.setRotate(0);
        this.playerImageView3.setRotate(0);
    }

    private void generatePcCards() {
        for (Node node : this.gridPaneComputer.getChildren()) {
            if (node instanceof ImageView imageView) {
                Image image = new Image(this.pathsToGameImages.get(random.nextInt(0, 3)));
                imageView.setRotate(0);
                imageView.setImage(backOfCard);
                this.computerGeneratedCards.add(image);
            }
        }
    }

    @FXML
    private synchronized void onElementButtonClick(ActionEvent actionEvent) {
        if (currentPickedImageViews != null) {
            AnimationsUtility.stopAllPulseAnimation();
            switch (((Button) actionEvent.getSource()).getText()) {
                case "FIRE" -> playRotationAnimation(this.fireCard);
                case "WATER" -> playRotationAnimation(this.waterCard);
                case "WIND" -> playRotationAnimation(this.windCard);
                default ->
                        throw new IllegalStateException("Unexpected value: " + ((Button) actionEvent.getSource()).getText());
            }
            currentPickedImageViews = new ArrayList<>();
        }
    }

    private void playRotationAnimation(Image imageToSet) {
        for (ImageView currentPickedImageView : currentPickedImageViews) {
            if (currentPickedImageView.getImage().getUrl().equals(imageToSet.getUrl())) {
                continue;
            }
            if (currentPickedImageView.getImage() == backOfCard) {
                Animation rotator = AnimationsUtility.createRotator(currentPickedImageView, imageToSet);
                rotator.setCycleCount(1);
                rotator.play();
            } else {
                Animation rotator = AnimationsUtility.createRotator360(currentPickedImageView, imageToSet, currentPickedImageView.getImage());
                rotator.setCycleCount(1);
                rotator.play();
            }
        }
    }


    private void setIcons(ImageView playerImage, ImageView computerImage, Image computerIcon, Image playerIcon) {
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(300),
                        e -> {
                            playerImage.setImage(playerIcon);
                            computerImage.setImage(computerIcon);
                        }
                )
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    @FXML
    private void onFightButtonClick(ActionEvent actionEvent) {
        switch (((Button) actionEvent.getSource()).getId()) {
            case "vsButton1" ->
                    fight(computerImageView1, playerImageView1, this.computerGeneratedCards.get(0), computerResultImage1, playerResultImage1, vsButton1);
            case "vsButton2" ->
                    fight(computerImageView2, playerImageView2, this.computerGeneratedCards.get(1), computerResultImage2, playerResultImage2, vsButton2);
            case "vsButton3" ->
                    fight(computerImageView3, playerImageView3, this.computerGeneratedCards.get(2), computerResultImage3, playerResultImage3, vsButton3);
            default ->
                    throw new IllegalStateException("Unexpected value: " + ((Button) actionEvent.getSource()).getText());
        }
    }

    private void fight(ImageView computerImageView, ImageView playerImageView, Image computerImageToSet, ImageView computerResultImageView, ImageView playerResultImageView, Button vsButton) {
        if (!playerImageView.getImage().getUrl().equals(backOfCard.getUrl())) {
            AnimationsUtility.stopPulseAnimation(playerImageView);
            currentPickedImageViews.remove(playerImageView);
            vsButton.setDisable(true);
            playerImageView.setOnMouseClicked(null);
            this.scoreLabel.setText("Player - %d : %d - Computer".formatted(this.playerScore, this.computerScore));
            this.countFights.setValue(this.countFights.getValue() + 1);

            Animation rotator = AnimationsUtility.createRotator(computerImageView, computerImageToSet);
            rotator.setCycleCount(1);
            rotator.play();

            checkScore(computerImageToSet, playerImageView, computerResultImageView, playerResultImageView);
        }
    }

    private void checkScore(Image computerImageToSet, ImageView playerImageView, ImageView computerResultImageView, ImageView playerResultImageView) {
        String computerImageUrl = computerImageToSet.getUrl().substring(computerImageToSet.getUrl().lastIndexOf('/') + 1);
        String playerImageUrl = playerImageView.getImage().getUrl().substring(playerImageView.getImage().getUrl().lastIndexOf('/') + 1);
        String waterElement = "WaterCard.png";
        String fireElement = "FireCard.png";
        String windElement = "WindCard.png";

        checkWhoWon(playerImageUrl, computerImageUrl, playerResultImageView, computerResultImageView, waterElement, fireElement);
        checkWhoWon(playerImageUrl, computerImageUrl, playerResultImageView, computerResultImageView, windElement, waterElement);
        checkWhoWon(playerImageUrl, computerImageUrl, playerResultImageView, computerResultImageView, fireElement, windElement);
    }

    private void checkWhoWon(String playerImageUrl, String computerImageUrl, ImageView playerImage, ImageView computerImage, String firstElement, String secondElement) {
        if (playerImageUrl.equals(computerImageUrl)) {
            setIcons(playerImage, computerImage, this.draw, this.draw);

        } else if (playerImageUrl.equals(firstElement) && computerImageUrl.equals(secondElement)) {
            setIcons(playerImage, computerImage, this.lose, this.win);
            this.playerScore++;

        } else if (playerImageUrl.equals(secondElement) && computerImageUrl.equals(firstElement)) {
            setIcons(playerImage, computerImage, this.win, this.lose);
            this.computerScore++;
        }
    }


    private void setCurrentImageViewOnClick() {
        for (Node node : this.gridPanePlayer.getChildren()) {
            if (node instanceof ImageView imageView) {
                imageView.setOnMouseClicked(e -> {
                    currentImageView = imageView;

                    if (!currentPickedImageViews.contains(currentImageView)) {
                        currentPickedImageViews.add(currentImageView);
                    }

                    if (AnimationsUtility.isPulsing(currentImageView).get()) {
                        AnimationsUtility.stopPulseAnimation(currentImageView);
                        currentPickedImageViews.remove(currentImageView);
                        currentImageView = null;

                    } else {
                        AnimationsUtility.playPulseAnimation(currentImageView);
                    }

                });
            }
        }
    }

    private void openAlertStage(String text) {
        FXMLLoader fxmlLoader = new FXMLLoader(GameOverAlert.class.getResource("gameOverAlert.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            EndGameController endGameController = fxmlLoader.getController();
            endGameController.displayWinner(text);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Error while opening new Window");
        }
    }

    @FXML
    private void playAgain() {
        resetGameSettings();
        domputerWonRounds = 0;
        playerWonRounds = 0;
        computerScore = 0;
        playerScore = 0;
        countFights.set(0);
        countRounds.set(1);
        computerWonRoundsLabel.setText("Computer won rounds: " + 0);
        playerWonRoundsLabel.setText("Player won rounds: " + 0);
    }

    private void resetGameSettings() {
        playerImageView1.setImage(null);
        playerImageView2.setImage(null);
        playerImageView3.setImage(null);
        computerImageView1.setImage(null);
        computerImageView2.setImage(null);
        computerImageView3.setImage(null);
        computerResultImage1.setImage(null);
        computerResultImage2.setImage(null);
        computerResultImage3.setImage(null);
        playerResultImage1.setImage(null);
        playerResultImage2.setImage(null);
        playerResultImage3.setImage(null);
        vsButton1.setDisable(false);
        vsButton2.setDisable(false);
        vsButton3.setDisable(false);
        AnimationsUtility.stopAllPulseAnimation();
        computerGeneratedCards = new ArrayList<>();
        currentPickedImageViews = new ArrayList<>();
        generatePcCards();
        setPlayerBackCard();
        setCurrentImageViewOnClick();
        scoreLabel.setText("Player - 0 : 0 - Computer");
    }
}

