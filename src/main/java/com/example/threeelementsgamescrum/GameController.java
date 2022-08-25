package com.example.threeelementsgamescrum;

import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    //1- left image 2 - middle image 3 -  right image
    // Result image for PC
    @FXML
    public ImageView computerResultImage3;
    @FXML
    public ImageView computerResultImage2;
    public ColumnConstraints column1;
    @FXML
    private ImageView computerResultImage1;

    // Result image for User
    @FXML
    public ImageView playerResultImage3;
    @FXML
    public ImageView fightResultUser2;
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
            String.valueOf(this.getClass().getResource("Images/FireElement.png")),
            String.valueOf(this.getClass().getResource("Images/WaterElement.png")),
            String.valueOf(this.getClass().getResource("Images/WindElement.png"))
    ));
    private int playerScore;
    private int computerScore;
    private final Random random = new Random();
    private final IntegerProperty countFights = new SimpleIntegerProperty(0);
    private final IntegerProperty countRounds = new SimpleIntegerProperty(1);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generatePcCards();
        setCurrentImageViewOnClick();

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
                countRounds.setValue(0);
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

    public void generatePcCards() {
        for (Node node : gridPanePC.getChildren()) {
            if (node instanceof ImageView imageView) {
                Image image = new Image(pathsToGameImages.get(random.nextInt(0, 3)));
                imageView.setOpacity(0);
                imageView.setImage(image);
            }
        }
    }

    @FXML
    public void onFireButtonClick() {
        if (currentPickedImageViews != null) {
            Image image = new Image(String.valueOf(this.getClass().getResource("Images/FireElement.png")));
            currentPickedImageViews.forEach(e-> {
                e.setImage(image);
                e.getParent().setStyle("-fx-background-color: white");
            });
            currentPickedImageViews = new ArrayList<>();
        }
    }

    @FXML
    public void onWaterButtonClick() {
        if (currentImageView != null) {
            Image image = new Image(String.valueOf(this.getClass().getResource("Images/WaterElement.png")));
            currentPickedImageViews.forEach(e-> {
                e.setImage(image);
                e.getParent().setStyle("-fx-background-color: white");
            });
            currentPickedImageViews = new ArrayList<>();

        }
    }

    @FXML
    public void onWindButtonClick() {
        if (currentImageView != null) {
            Image image = new Image(String.valueOf(this.getClass().getResource("Images/WindElement.png")));
            currentPickedImageViews.forEach(e-> {
                e.setImage(image);
                e.getParent().setStyle("-fx-background-color: white");
            });
            currentPickedImageViews = new ArrayList<>();


        }
    }

    public void checkScore(String playerImageUrl, String computerImageUrl, ImageView computerImage, ImageView playerImage) {

        String waterElement = "WaterElement.png";
        String fireElement = "FireElement.png";
        String windElement = "WindElement.png";

        checkWhoWon(playerImage, computerImage, playerImageUrl, computerImageUrl, waterElement, fireElement);
        checkWhoWon(playerImage, computerImage, playerImageUrl, computerImageUrl, windElement, waterElement);
        checkWhoWon(playerImage, computerImage, playerImageUrl, computerImageUrl, fireElement, windElement);
    }

    public void checkWhoWon(ImageView playerImage, ImageView computerImage, String playerImageUrl, String computerImageUrl, String firstElement, String secondElement) {
        Image draw = new Image(String.valueOf(this.getClass().getResource("Images/draw.png")));
        Image win = new Image(String.valueOf(this.getClass().getResource("Images/win.png")));
        Image lose = new Image(String.valueOf(this.getClass().getResource("Images/lose.png")));

        if (playerImageUrl.equals(computerImageUrl)) {
            playerImage.setImage(draw);
            computerImage.setImage(draw);
        } else if (playerImageUrl.equals(firstElement) && computerImageUrl.equals(secondElement)) {
            playerImage.setImage(win);
            computerImage.setImage(lose);
            this.playerScore++;
        } else if (playerImageUrl.equals(secondElement) && computerImageUrl.equals(firstElement)) {
            playerImage.setImage(lose);
            computerImage.setImage(win);
            this.computerScore++;
        }

    }

    @FXML
    public void onFirstFightButtonClick() {

        if (fightValidate(this.playerImage1)) {
            fightAciton(this.playerImage1, this.computerImage1, this.computerResultImage1, this.playerResultImage1, this.vsButton1);
        }

    }

    @FXML
    public void onSecondFightButtonClick() {

        if (fightValidate(this.playerImage2)) {
            fightAciton(this.playerImage2, this.computerImage2, this.computerResultImage2, this.fightResultUser2, this.vsButton2);
        }
    }

    @FXML
    public void onThirdFightButtonClick() {

        if (fightValidate(this.playerImage3)) {
            fightAciton(this.playerImage3, this.computerImage3, this.computerResultImage3, this.playerResultImage3, this.vsButton3);
        }
    }

    public boolean fightValidate(ImageView imageView) {

        return imageView != null;
    }

    private void fightAciton(ImageView imageUser, ImageView imagePC, ImageView fightResultPC, ImageView fightResultUser, Button vsButton) {
        String userImageUrl = imageUser.getImage().getUrl().substring(imageUser.getImage().getUrl().lastIndexOf('/') + 1);
        String pcImageUrl = imagePC.getImage().getUrl().substring(imageUser.getImage().getUrl().lastIndexOf('/') + 1);
        checkScore(userImageUrl, pcImageUrl, fightResultPC, fightResultUser);
        imagePC.setOpacity(1);
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
        fightResultUser2.setImage(null);
        playerResultImage3.setImage(null);
        vsButton1.setDisable(false);
        vsButton2.setDisable(false);
        vsButton3.setDisable(false);
        generatePcCards();
        setCurrentImageViewOnClick();
        scoreLabel.setText("Player - %d : %d - Computer".formatted(playerScore, computerScore));


    }


    public void setCurrentImageViewOnClick() {
        for (Node node : gridPaneUser.getChildren()) {
            if (node instanceof Pane pane) {
                pane.setOnMouseClicked(e -> {
                    currentImageView = (ImageView) pane.getChildren().get(0);
                    currentPickedImageViews.add(currentImageView);
                    System.out.println(currentPickedImageViews);
                    pane.setStyle("-fx-background-color: lightblue");
                    if (currentImageView.getImage() == null) {
                    }
                    if (currentImageView.getImage() != null) {
                        makeScaleTransition();
                    }
                });
            }
        }
    }

    private void makeScaleTransition() {
        ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setNode(currentImageView);
        scaleTransition.durationProperty().setValue(Duration.millis(1000));
        scaleTransition.setFromX(1);
        scaleTransition.setToX(1.5);
        scaleTransition.setCycleCount(2);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
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

