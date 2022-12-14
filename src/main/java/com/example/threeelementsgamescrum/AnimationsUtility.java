package com.example.threeelementsgamescrum;

import animatefx.animation.Pulse;
import javafx.animation.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Set;

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


    public static boolean isCardPulsing(ImageView currentImageView) {
        return currentPulsingAnimations.stream()
                .anyMatch(e-> e.getNode().equals(currentImageView));
    }

    public static Animation createRotator(ImageView card, Image imageToSet) {
        RotateTransition rotator = new RotateTransition(Duration.millis(500), card);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setFromAngle(360);
        rotator.setToAngle(180);
        rotator.setInterpolator(Interpolator.LINEAR);
        rotator.setCycleCount(1);

        Timeline imageSwitcher = new Timeline(
                new KeyFrame(Duration.millis(250),
                        new KeyValue(card.imageProperty(), imageToSet, Interpolator.DISCRETE))
        );
        imageSwitcher.setCycleCount(1);
        return new ParallelTransition(card, rotator, imageSwitcher);
    }

    public static Animation createRotator360(ImageView card, Image imageToSet) {
        RotateTransition rotator = new RotateTransition(Duration.millis(800), card);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setFromAngle(180);
        rotator.setToAngle(540);
        rotator.setInterpolator(Interpolator.LINEAR);
        rotator.setCycleCount(1);

        Timeline imageSwitcher = new Timeline(
                new KeyFrame(Duration.millis(200),
                        new KeyValue(card.imageProperty(), GameController.backOfCard, Interpolator.DISCRETE)),
                new KeyFrame(Duration.millis(600),
                        new KeyValue(card.imageProperty(), imageToSet, Interpolator.DISCRETE))
        );
        imageSwitcher.setCycleCount(1);
        return new ParallelTransition(card, rotator, imageSwitcher);
    }
}
