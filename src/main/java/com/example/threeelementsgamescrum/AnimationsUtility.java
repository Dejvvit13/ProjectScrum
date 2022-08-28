package com.example.threeelementsgamescrum;

import animatefx.animation.Pulse;
import javafx.animation.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class AnimationsUtility {
    protected static Set<Pulse> currentPulsingAnimations = new HashSet<>();

    private AnimationsUtility() {
    }

    public static void playPulseAnimation(ImageView img) {
        Pulse pulse = new Pulse(img);
        currentPulsingAnimations.add(pulse);
        pulse.setCycleCount(-1);
        pulse.play();
    }

    public static void stopAllPulseAnimation() {
        currentPulsingAnimations.forEach(e -> {
            e.stop();
            e.setCycleCount(1);
            e.play();
        });
        currentPulsingAnimations = new HashSet<>();
    }

    public static void stopPulseAnimation(ImageView playerImageView) {
        currentPulsingAnimations.forEach(i -> {
            if (i.getNode() == (playerImageView)) {
                i.stop();
                i.setCycleCount(1);
                i.play();
            }
        });
        currentPulsingAnimations.removeIf(e -> e.getNode() == playerImageView);
    }


    public static AtomicBoolean isCardPulsing(ImageView currentImageView) {
        AtomicBoolean result = new AtomicBoolean(false);
        currentPulsingAnimations.forEach(e -> {
            if (e.getNode() == currentImageView) {
                result.set(true);
            }
        });
        return result;
    }

    public static Animation createRotator(ImageView card, Image imageToSet) {
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
                        new KeyValue(card.imageProperty(), GameController.backOfCard, Interpolator.DISCRETE)),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(card.imageProperty(), front, Interpolator.DISCRETE))
        );
        imageSwitcher.setCycleCount(1);
        return new ParallelTransition(card, rotator, imageSwitcher);
    }

    public static Animation createRotator360(ImageView card, Image imageToSet, Image currentImage) {
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
                        new KeyValue(card.imageProperty(), GameController.backOfCard, Interpolator.DISCRETE)),
                new KeyFrame(Duration.millis(600),
                        new KeyValue(card.imageProperty(), front, Interpolator.DISCRETE))
        );
        imageSwitcher.setCycleCount(1);
        return new ParallelTransition(card, rotator, imageSwitcher);
    }
}
