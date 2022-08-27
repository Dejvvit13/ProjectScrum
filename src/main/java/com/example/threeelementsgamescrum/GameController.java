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
import javafx.scene.layout.GridPane;
import javafx.scene.transform.Rotate;
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
        this.playerImageView1.setImage(this.backOfCard);
        this.playerImageView2.setImage(this.backOfCard);
        this.playerImageView3.setImage(this.backOfCard);
        this.playerImageView1.setRotate(0);
        this.playerImageView2.setRotate(0);
        this.playerImageView3.setRotate(0);

    }

    private void generatePcCards() {
        for (Node node : this.gridPaneComputer.getChildren()) {
            if (node instanceof ImageView imageView) {
                Image image = new Image(this.pathsToGameImages.get(random.nextInt(0, 3)));
                imageView.setRotate(0);
                imageView.setImage(this.backOfCard);
                this.computerGeneratedCards.add(image);
            }
        }
    }

    @FXML
    private void onFireButtonClick() {
        if (this.currentPickedImageViews != null) {
            this.currentPulsingAnimations.forEach(e -> {
                e.stop();
                e.setCycleCount(1);
                e.play();
            });
            this.currentPulsingAnimations = new HashSet<>();
            for (ImageView currentPickedImageView : this.currentPickedImageViews) {
                if(currentPickedImageView.getImage().getUrl().equals(this.fireCard.getUrl())){
                    continue;
                }
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
            setPlayerImage(this.fireCard);

        }
    }

    @FXML
    private void onWaterButtonClick() {
        if (this.currentPickedImageViews != null) {
            this.currentPulsingAnimations.forEach(e -> {
                e.stop();
                e.setCycleCount(1);
                e.play();
            });
            this.currentPulsingAnimations = new HashSet<>();

            for (ImageView currentPickedImageView : this.currentPickedImageViews) {
                if(currentPickedImageView.getImage().getUrl().equals(this.waterCard.getUrl())){
                    continue;
                }
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
            setPlayerImage(this.waterCard);
        }
    }

    @FXML
    private void onWindButtonClick() {
        if (this.currentPickedImageViews != null) {
            this.currentPulsingAnimations.forEach(e -> {
                e.stop();
                e.setCycleCount(1);
                e.play();
            });
            this.currentPulsingAnimations = new HashSet<>();
            for (ImageView currentPickedImageView : this.currentPickedImageViews) {
                if(currentPickedImageView.getImage().getUrl().equals(this.windCard.getUrl())){
                    continue;
                }
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
            setPlayerImage(this.windCard);
        }
    }

    private void setPlayerImage(Image image) {
        this.currentPickedImageViews.forEach(e -> e.setImage(image));
        this.currentPickedImageViews = new ArrayList<>();
    }

    private void checkScore(String playerImageUrl, String computerImageUrl, ImageView computerImage, ImageView playerImage) {
        String waterElement = "WaterCard.png";
        String fireElement = "FireCard.png";
        String windElement = "WindCard.png";

        checkWhoWon(playerImage, computerImage, playerImageUrl, computerImageUrl, waterElement, fireElement);
        checkWhoWon(playerImage, computerImage, playerImageUrl, computerImageUrl, windElement, waterElement);
        checkWhoWon(playerImage, computerImage, playerImageUrl, computerImageUrl, fireElement, windElement);
    }

    private void checkWhoWon(ImageView playerImage, ImageView computerImage, String playerImageUrl, String computerImageUrl, String firstElement, String secondElement) {

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

    private Animation createRotator(ImageView card, Image imageToSet) {

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

    private Animation createRotator360(ImageView card, Image imageToSet, Image currentImage) {

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
    private void onFirstFightButtonClick() {
        if (!this.playerImageView1.getImage().getUrl().equals(this.backOfCard.getUrl())) {
            Animation flip = createRotator(this.computerImageView1, this.computerGeneratedCards.get(0));
            flip.setCycleCount(1);
            flip.play();
            fightAction(this.playerImageView1, this.computerGeneratedCards.get(0), this.computerResultImage1, this.playerResultImage1, this.vsButton1);
            this.playerImageView1.setOnMouseClicked(null);
            removePulseAnimation(this.playerImageView1);
        }
    }

    @FXML
    private void onSecondFightButtonClick() {
        if (!this.playerImageView2.getImage().getUrl().equals(this.backOfCard.getUrl())) {
            Animation rotator = createRotator(this.computerImageView2, this.computerGeneratedCards.get(1));
            rotator.setCycleCount(1);
            rotator.play();
            fightAction(this.playerImageView2, this.computerGeneratedCards.get(1), this.computerResultImage2, this.playerResultImage2, this.vsButton2);
            this.playerImageView2.setOnMouseClicked(null);
            removePulseAnimation(this.playerImageView2);
        }

    }

    @FXML
    private void onThirdFightButtonClick() {
        if (!this.playerImageView3.getImage().getUrl().equals(this.backOfCard.getUrl())) {
            Animation rotator = createRotator(this.computerImageView3, this.computerGeneratedCards.get(2));
            rotator.setCycleCount(1);
            rotator.play();
            fightAction(this.playerImageView3, this.computerGeneratedCards.get(2), this.computerResultImage3, this.playerResultImage3, this.vsButton3);
            this.playerImageView3.setOnMouseClicked(null);
            removePulseAnimation(this.playerImageView3);
        }
    }

    private void fightAction(ImageView playerImageView, Image computerImage, ImageView fightResultPC, ImageView fightResultUser, Button vsButton) {
        String userImageUrl = playerImageView.getImage().getUrl().substring(playerImageView.getImage().getUrl().lastIndexOf('/') + 1);
        String pcImageUrl = computerImage.getUrl().substring(computerImage.getUrl().lastIndexOf('/') + 1);
        checkScore(userImageUrl, pcImageUrl, fightResultPC, fightResultUser);
        playerImageView.getParent().setOnMouseClicked(null);
        vsButton.setDisable(true);
        this.scoreLabel.setText("Player - %d : %d - Computer".formatted(this.playerScore, this.computerScore));
        this.countFights.setValue(this.countFights.getValue() + 1);
    }

    @FXML
    public void playAgain() {
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
        removeAllPulseAnimation();
        computerGeneratedCards = new ArrayList<>();
        currentPickedImageViews = new ArrayList<>();
        currentPulsingAnimations = new HashSet<>();
        generatePcCards();
        setPlayerBackCard();
        setCurrentImageViewOnClick();
        scoreLabel.setText("Player - 0 : 0 - Computer");
    }

    private void setCurrentImageViewOnClick() {
        for (Node node : this.gridPanePlayer.getChildren()) {
            if (node instanceof ImageView imageView) {
                imageView.setOnMouseClicked(e -> {
                    this.currentImageView = imageView;
                    if (!this.currentPickedImageViews.contains(this.currentImageView)) {
                        this.currentPickedImageViews.add(this.currentImageView);
                    }
                    setPulseAnimation();

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

    public void removeAllPulseAnimation() {
        this.currentPulsingAnimations.forEach(e -> {
            e.stop();
            e.setCycleCount(1);
            e.play();
        });
    }

    public void removePulseAnimation(ImageView playerImageView) {
        this.currentPulsingAnimations.forEach(i -> {
            if (i.getNode() == (playerImageView)) {
                i.stop();
                i.setCycleCount(1);
                i.play();
                this.currentPickedImageViews.remove(playerImageView);
                this.currentImageView = null;
            }
        });
        this.currentPulsingAnimations.removeIf(e -> e.getNode() == playerImageView);
    }


    private void setPulseAnimation() {
        removePulseAnimation(this.currentImageView);
        if (this.currentImageView != null) {
            pulseAnimation(this.currentImageView);
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

}

