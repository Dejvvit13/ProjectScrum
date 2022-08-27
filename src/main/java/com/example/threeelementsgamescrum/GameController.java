package com.example.threeelementsgamescrum;


import animatefx.animation.Pulse;
import javafx.animation.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GameController implements Initializable {

    //1- left image 2 - middle image 3 -  right image
    // Result image for PC
    @FXML
    public ImageView computerResultImage3;
    @FXML
    public ImageView computerResultImage2;
    @FXML
    private ImageView computerResultImage1;

    // Result image for User
    @FXML
    public ImageView playerResultImage3;
    @FXML
    public ImageView playerResultImage2;
    @FXML
    private ImageView playerResultImage1;

    //versus buttons
    @FXML
    public Button vsButton1;
    @FXML
    public Button vsButton2;
    @FXML
    public Button vsButton3;
    @FXML
    private GridPane gridPanePC; // pc cards container
    @FXML
    private GridPane gridPaneUser; // user cards container

    private ImageView currentImageView; //currently picked card slot
    private List<ImageView> currentPickedImageViews = new ArrayList<>();
    private List<Image> pcGeneratedCards = new ArrayList<>();

    // user images
    @FXML
    private ImageView playerImage1;
    @FXML
    private ImageView playerImage2;
    @FXML
    private ImageView playerImage3;
    @FXML
    private ImageView computerImage1;

    // pc images
    @FXML
    private ImageView computerImage2;
    @FXML
    private ImageView computerImage3;

    @FXML
    private Label scoreLabel; // score text
    @FXML
    private Label roundDisplayLabel; // round text

    private final List<String> pathsToGameImages = new ArrayList<>(List.of(
            String.valueOf(this.getClass().getResource("Images/FireCard.png")),
            String.valueOf(this.getClass().getResource("Images/WaterCard.png")),
            String.valueOf(this.getClass().getResource("Images/WindCard.png"))
    ));
    private int playerScore;
    private int computerScore;
    private final Random random = new Random();
    private final IntegerProperty countFights = new SimpleIntegerProperty(0);
    private final IntegerProperty countRounds = new SimpleIntegerProperty(1);
    private final Image backOfCard = new Image(String.valueOf(this.getClass().getResource("Images/BackOfCard.png")));
    private final Image windCard = new Image(String.valueOf(this.getClass().getResource("Images/WindCard.png")));
    private final Image fireCard = new Image(String.valueOf(this.getClass().getResource("Images/FireCard.png")));
    private final Image waterCard = new Image(String.valueOf(this.getClass().getResource("Images/WaterCard.png")));
    private final Image draw = new Image(String.valueOf(this.getClass().getResource("Images/draw.png")));
    private final Image win = new Image(String.valueOf(this.getClass().getResource("Images/win.png")));
    private final Image lose = new Image(String.valueOf(this.getClass().getResource("Images/lose.png")));

    private Set<Pulse> currentPulsingAnimations = new HashSet<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generatePcCards();
        setCurrentImageViewOnClick();
        setPlayerBackCard();

        countRounds.addListener(event -> {
            roundDisplayLabel.setText("Round " + countRounds.getValue());
            if (countRounds.getValue() > 3) {
                if (playerScore == computerScore) {
                    openAlertStage("It's DRAW");
                } else if (playerScore > computerScore) {
                    openAlertStage("Player Won");

                } else {
                    openAlertStage("Computer Won");
                }
                roundDisplayLabel.setText("Round 1");
                computerScore = 0;
                playerScore = 0;
                countRounds.setValue(1);
            }
            resetGameSettings();
        });
        countFights.addListener(event -> {
            if (countFights.getValue() == 3) {
                Timeline roundChange = new Timeline(
                        new KeyFrame(
                                Duration.millis(2000),
                                e -> {
                                    countRounds.setValue(countRounds.getValue() + 1);
                                    countFights.set(0);
                                }
                        ));
                roundChange.setCycleCount(1);
                roundChange.play();
            }
        });
    }

    public void setPlayerBackCard() {
        this.playerImage1.setImage(this.backOfCard);
        this.playerImage2.setImage(this.backOfCard);
        this.playerImage3.setImage(this.backOfCard);
        playerImage1.setRotate(0);
        playerImage2.setRotate(0);
        playerImage3.setRotate(0);

    }

    public void generatePcCards() {
        for (Node node : this.gridPanePC.getChildren()) {
            if (node instanceof ImageView imageView) {
                Image image = new Image(this.pathsToGameImages.get(random.nextInt(0, 3)));
                imageView.setRotate(0);
                this.pcGeneratedCards.add(image);
                imageView.setImage(this.backOfCard);
            }
        }
    }

    @FXML
    public void onFireButtonClick() {
        if (this.currentPickedImageViews != null) {
            this.currentPulsingAnimations.forEach(e -> {
                e.stop();
                e.setCycleCount(1);
                e.play();
            });
            this.currentPulsingAnimations = new HashSet<>();
            for (ImageView currentPickedImageView : this.currentPickedImageViews) {
                if (currentPickedImageView.getImage() == this.backOfCard) {
                    Animation rotator = createRotator(currentPickedImageView, this.fireCard);
                    rotator.setCycleCount(1);
                    rotator.play();
                } else {
                    Animation rotator = createRotator360(currentPickedImageView, this.fireCard, currentPickedImageView.getImage());
                    rotator.setCycleCount(1);
                    rotator.play();
                }
            }
            setUserImage(fireCard);

        }
    }

    @FXML
    public void onWaterButtonClick() {
        if (this.currentPickedImageViews != null) {
            this.currentPulsingAnimations.forEach(e -> {
                e.stop();
                e.setCycleCount(1);
                e.play();
            });
            this.currentPulsingAnimations = new HashSet<>();

            for (ImageView currentPickedImageView : this.currentPickedImageViews) {
                if (currentPickedImageView.getImage() == this.backOfCard) {
                    Animation rotator = createRotator(currentPickedImageView, this.waterCard);
                    rotator.setCycleCount(1);
                    rotator.play();
                } else {
                    Animation rotator = createRotator360(currentPickedImageView, this.waterCard, currentPickedImageView.getImage());
                    rotator.setCycleCount(1);
                    rotator.play();
                }
            }
            setUserImage(this.waterCard);
        }
    }

    @FXML
    public void onWindButtonClick() {
        if (this.currentPickedImageViews != null) {
            this.currentPulsingAnimations.forEach(e -> {
                e.stop();
                e.setCycleCount(1);
                e.play();
            });
            this.currentPulsingAnimations = new HashSet<>();
            for (ImageView currentPickedImageView : this.currentPickedImageViews) {

                if (currentPickedImageView.getImage() == this.backOfCard) {
                    Animation rotator = createRotator(currentPickedImageView, this.windCard);
                    rotator.setCycleCount(1);
                    rotator.play();
                } else {
                    Animation rotator = createRotator360(currentPickedImageView, this.windCard, currentPickedImageView.getImage());
                    rotator.setCycleCount(1);
                    rotator.play();
                }
            }
            setUserImage(this.windCard);
        }
    }

    public void setUserImage(Image image) {
        this.currentPickedImageViews.forEach(e -> e.setImage(image));
        this.currentPickedImageViews = new ArrayList<>();
    }

    public void checkScore(String playerImageUrl, String computerImageUrl, ImageView computerImage, ImageView playerImage) {
        String waterElement = "WaterCard.png";
        String fireElement = "FireCard.png";
        String windElement = "WindCard.png";

        checkWhoWon(playerImage, computerImage, playerImageUrl, computerImageUrl, waterElement, fireElement);
        checkWhoWon(playerImage, computerImage, playerImageUrl, computerImageUrl, windElement, waterElement);
        checkWhoWon(playerImage, computerImage, playerImageUrl, computerImageUrl, fireElement, windElement);
    }

    public void checkWhoWon(ImageView playerImage, ImageView computerImage, String playerImageUrl, String computerImageUrl, String firstElement, String secondElement) {


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

    public Animation createRotator(ImageView card, Image imageToSet) {

        card.setImage(imageToSet);
        RotateTransition rotator = new RotateTransition(Duration.millis(600), card);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setFromAngle(360);
        rotator.setToAngle(180);
        rotator.setInterpolator(Interpolator.LINEAR);
        rotator.setCycleCount(1);
        Image front = new Image(
                card.getImage().getUrl(),
                false);
        Timeline imageSwitcher = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(card.imageProperty(), this.backOfCard, Interpolator.DISCRETE)),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(card.imageProperty(), front, Interpolator.DISCRETE))
        );
        imageSwitcher.setCycleCount(1);
        return new ParallelTransition(card, rotator, imageSwitcher);
    }

    public Animation createRotator360(ImageView card, Image imageToSet, Image currentImage) {

        card.setImage(imageToSet);
        RotateTransition rotator = new RotateTransition(Duration.millis(900), card);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setFromAngle(180);
        rotator.setToAngle(540);
        rotator.setInterpolator(Interpolator.LINEAR);
        rotator.setCycleCount(1);
        Image front = new Image(
                card.getImage().getUrl(),
                false);
        Timeline imageSwitcher = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(card.imageProperty(), currentImage, Interpolator.DISCRETE)),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(card.imageProperty(), this.backOfCard, Interpolator.DISCRETE)),
                new KeyFrame(Duration.millis(600),
                        new KeyValue(card.imageProperty(), front, Interpolator.DISCRETE))
        );
        imageSwitcher.setCycleCount(1);
        return new ParallelTransition(card, rotator, imageSwitcher);
    }


    @FXML
    public void onFirstFightButtonClick() {
        if (!this.playerImage1.getImage().getUrl().equals(this.backOfCard.getUrl())) {
            Animation flip = createRotator(this.computerImage1, this.pcGeneratedCards.get(0));
            flip.setAutoReverse(true);
            flip.setCycleCount(1);
            flip.play();

            fightAction(this.playerImage1, this.pcGeneratedCards.get(0), this.computerResultImage1, this.playerResultImage1, this.vsButton1);
        }
    }

    @FXML
    public void onSecondFightButtonClick() {
        if (!this.playerImage2.getImage().getUrl().equals(this.backOfCard.getUrl())) {
            Animation rotator = createRotator(this.computerImage2, this.pcGeneratedCards.get(1));
            rotator.setCycleCount(1);
            rotator.play();
            fightAction(this.playerImage2, this.pcGeneratedCards.get(1), this.computerResultImage2, this.playerResultImage2, this.vsButton2);
        }
    }

    @FXML
    public void onThirdFightButtonClick() {

        if (!this.playerImage3.getImage().getUrl().equals(this.backOfCard.getUrl())) {
            Animation rotator = createRotator(this.computerImage3, this.pcGeneratedCards.get(2));
            rotator.setCycleCount(1);
            rotator.play();

            fightAction(this.playerImage3, this.pcGeneratedCards.get(2), this.computerResultImage3, this.playerResultImage3, this.vsButton3);
        }
    }

    private void fightAction(ImageView imageUser, Image imagePC, ImageView fightResultPC, ImageView fightResultUser, Button vsButton) {
        String userImageUrl = imageUser.getImage().getUrl().substring(imageUser.getImage().getUrl().lastIndexOf('/') + 1);
        String pcImageUrl = imagePC.getUrl().substring(imageUser.getImage().getUrl().lastIndexOf('/') + 1);
        checkScore(userImageUrl, pcImageUrl, fightResultPC, fightResultUser);
        imageUser.getParent().setOnMouseClicked(null);
        vsButton.setDisable(true);
        scoreLabel.setText("Player - %d : %d - Computer".formatted(playerScore, computerScore));
        countFights.setValue(countFights.getValue() + 1);
    }

    public void playAgain() {
        resetGameSettings();
        computerScore = 0;
        playerScore = 0;
        countFights.set(0);
        countRounds.set(1);
    }

    public void resetGameSettings() {
        playerImage1.setImage(null);
        playerImage2.setImage(null);
        playerImage3.setImage(null);
        computerImage1.setImage(null);
        computerImage2.setImage(null);
        computerImage3.setImage(null);
        computerResultImage1.setImage(null);
        computerResultImage2.setImage(null);
        computerResultImage3.setImage(null);
        playerResultImage1.setImage(null);
        playerResultImage2.setImage(null);
        playerResultImage3.setImage(null);
        vsButton1.setDisable(false);
        vsButton2.setDisable(false);
        vsButton3.setDisable(false);
        pcGeneratedCards = new ArrayList<>();
        currentPickedImageViews = new ArrayList<>();
        generatePcCards();
        setPlayerBackCard();
        setCurrentImageViewOnClick();
        scoreLabel.setText("Player - %d : %d - Computer".formatted(playerScore, computerScore));
    }

    public void setCurrentImageViewOnClick() {
        for (Node node : this.gridPaneUser.getChildren()) {
            if (node instanceof ImageView imageView) {
                imageView.setOnMouseClicked(e -> {
                    this.currentImageView = imageView;
                    if (!this.currentPickedImageViews.contains(this.currentImageView)) {
                        this.currentPickedImageViews.add(this.currentImageView);
                    }
                    setPuleAnimation();

                });
            }
        }
    }


    private void pulseAnimation(ImageView img) {
        Pulse pulse = new Pulse(img);
        this.currentPulsingAnimations.add(pulse);
        pulse.setCycleCount(-1);
        pulse.play();
    }
    private void setPuleAnimation(){
        this.currentPulsingAnimations.forEach(i -> {
            if (i.getNode().equals(this.currentImageView)) {
                this.currentPickedImageViews.remove(this.currentImageView);
                i.stop();
                i.setCycleCount(1);
                i.play();
                this.currentPulsingAnimations.remove(i);
                this.currentImageView = null;
            }
        });

        if (this.currentImageView != null) {
            pulseAnimation(this.currentImageView);
        }

    }

    public void openAlertStage(String text) {
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

}

