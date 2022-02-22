package sample;

import java.io.File;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class PlayCard extends ImageView {
    private Image shownImage;

    private final Image flipped = new Image("flipped.png"),
            unknown = new Image("http://clipart-library.com/images/8iAEy8jbT.png");
    private boolean isShown, isMatched = false, allowFlipping = true;
    static PlayCard selectedCard;
    static int imagesShown = 0;
    File matchFile = new File("Matching.mp3");
    File nonMatchFile = new File("NonMatching.mp3");
    Media matchSound = new Media(matchFile.toURI().toString()),
            noMatchSound = new Media(nonMatchFile.toURI().toString());
    MediaPlayer matchPlayer = new MediaPlayer(matchSound);
    MediaPlayer nonMatchPlayer = new MediaPlayer(noMatchSound);
    Timeline showAnimation, flipAnimation, delay, AllowFlippingDelay;
    int flipHandlerVar = 99, showHandlerVar = 1;
    static int animationNumber = 0;

    // Default constructor
    public PlayCard() {
        setFitWidth(100);
        setFitHeight(100);
        setImage(unknown);
    }

    // the constructor that will generate the cards.
    public PlayCard(Image image, Scores score) {
        System.out.println("New card object has been generated.");
        setFitWidth(100);
        setFitHeight(100);
        // Adding shadows beneath the cards.
        setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0)");

        // to prevent the user from flipping the cards until the two cards are flipped
        // again.
        EventHandler<ActionEvent> allowFlipHandler = e -> {
            setImagesShown(0);
            AllowFlippingDelay.stop();
            selectedCard.AllowFlippingDelay.stop();
        };

        // the animation for flipping the card.
        // - the idea is to decrease the width of the card until it reaches 1, then it
        // will change the card then it will call another animation to increase the
        // width
        // of the card.

        EventHandler<ActionEvent> flipHandler = e -> {
            if (getFitWidth() <= 100 && getFitWidth() > 1 && animationNumber == 0) {
                setFitWidth(flipHandlerVar--);
                if (getFitWidth() == 1) {
                    flipHandlerVar = 99;
                    setImage(getShownImage());
                    showAnimation.play();
                }
            } else if (getFitWidth() <= 100 && getFitWidth() > 1 && animationNumber == 1) {
                setFitWidth(flipHandlerVar--);
                if (getFitWidth() == 1) {
                    flipHandlerVar = 99;
                    showAnimation.play();
                }

            } else if (getFitWidth() <= 100 && getFitWidth() > 1 && animationNumber == 2) {
                setFitWidth(flipHandlerVar--);
                if (getFitWidth() == 1) {
                    flipHandlerVar = 99;
                    showAnimation.play();
                }
            }

        };

        // to show the content of the card.
        EventHandler<ActionEvent> showHandler = e -> {
            if (getFitWidth() < 100 && getFitWidth() >= 0 && animationNumber == 0) {
                setFitWidth(showHandlerVar++);
                if (getFitWidth() == 99 || getFitWidth() == 100) {
                    showHandlerVar = 1;
                }
            } else if (getFitWidth() < 100 && getFitWidth() >= 0 && animationNumber == 1) {

                setFitWidth(showHandlerVar++);
                setImage(getShownImage());
                if (getFitWidth() == 99 || getFitWidth() == 100) {
                    showHandlerVar = 1;
                }

            } else if (getFitWidth() < 100 && getFitWidth() >= 0 && animationNumber == 2) {
                setFitWidth(showHandlerVar++);
                setFlipped();
                if (getFitWidth() == 100) {
                    showHandlerVar = 1;
                }
            }

        };

        // Delay the flipping to show the second card
        EventHandler<ActionEvent> delayHandler = e -> {

            animationNumber = 2;
            flipAnimation.play();
        };

        delay = new Timeline(new KeyFrame(Duration.millis(500), delayHandler));
        delay.setCycleCount(1);
        showAnimation = new Timeline(new KeyFrame(Duration.millis(2), showHandler));
        showAnimation.setCycleCount(99);
        flipAnimation = new Timeline(new KeyFrame(Duration.millis(2), flipHandler));
        flipAnimation.setCycleCount(99);
        AllowFlippingDelay = new Timeline(new KeyFrame(Duration.millis(1000), allowFlipHandler));
        AllowFlippingDelay.setCycleCount(2);
        setShownImage(image);

        // Playing an action when the player clicks on the card
        setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {

                // To show the first non-matched card
                if (!isShown() && imagesShown == 0) {
                    animationNumber = 0;
                    flipAnimation.play();
                    matchPlayer.stop();
                    nonMatchPlayer.stop();
                    setShown(true);
                    setSelectedCard((PlayCard) event.getSource());
                    selectedCard.setFlipAnimation(new Timeline(new KeyFrame(Duration.millis(2), flipHandler)));
                    selectedCard.getFlipAnimation().setCycleCount(99);
                    selectedCard.setShowAnimation(new Timeline(new KeyFrame(Duration.millis(2), showHandler)));
                    selectedCard.getShowAnimation().setCycleCount(99);
                    selectedCard.setDelay(new Timeline(new KeyFrame(Duration.millis(500), delayHandler)));
                    selectedCard
                            .setAllowFlippingDelay(new Timeline(new KeyFrame(Duration.millis(1000), allowFlipHandler)));
                    selectedCard.getDelay().setCycleCount(1);
                    selectedCard.getAllowFlippingDelay().setCycleCount(2);
                    selectedCard.setFlipHandlerVar(flipHandlerVar);
                    selectedCard.setShowHandlerVar(showHandlerVar);
                    imagesShown++;

                    // To show and match the second card
                } else if (!isShown() && isMatchedByPixels() && !isMatched() && imagesShown == 1) {
                    AllowFlippingDelay.play();
                    selectedCard.AllowFlippingDelay.play();
                    imagesShown++;
                    animationNumber = 1;
                    matchPlayer.play();
                    flipAnimation.play();
                    System.out.println("Matched!");
                    setShown(true);
                    setMatched(true);
                    score.calculateScore();

                    // To increment the move by one when two cards are shown
                    score.setRoundMoves(score.getRoundMoves() + 1);
                    score.setMatchedCounter(score.getMatchedCounter() + 1);
                    selectedCard.setMatched(true);

                    // To flip the cards again if they are not matched
                } else if (!isMatchedByPixels() && !isMatched() && !isShown() && imagesShown == 1) {
                    AllowFlippingDelay.play();
                    selectedCard.AllowFlippingDelay.play();
                    System.out.println("Unmatched!");
                    imagesShown++;
                    animationNumber = 1;
                    nonMatchPlayer.play();
                    flipAnimation.play();
                    delay.play();
                    selectedCard.delay.play();

                    // To increment the move by one when two cards are shown.
                    score.setRoundMoves(score.getRoundMoves() + 1);
                    setShown(false);
                    selectedCard.setShown(false);
                }
            }
        });
    }

    // to find if the cards are matched.
    public boolean isMatchedByPixels() {
        if (getShownImage().getPixelReader().equals(selectedCard.getShownImage().getPixelReader())) {
            return true;
        }

        return false;
    }

    // Getters and setters.
    public File getMatchFile() {
        return matchFile;
    }

    public void setMatchFile(File matchFile) {
        this.matchFile = matchFile;
    }

    public Media getMatchSound() {
        return matchSound;
    }

    public void setMatchSound(Media matchSound) {
        this.matchSound = matchSound;
    }

    public File getNonMatchFile() {
        return nonMatchFile;
    }

    public void setNonMatchFile(File nonMatchFile) {
        this.nonMatchFile = nonMatchFile;
    }

    public Media getNoMatchSound() {
        return noMatchSound;
    }

    public void setNoMatchSound(Media noMatchSound) {
        this.noMatchSound = noMatchSound;
    }

    public MediaPlayer getMatchPlayer() {
        return matchPlayer;
    }

    public void setMatchPlayer(MediaPlayer matchPlayer) {
        this.matchPlayer = matchPlayer;
    }

    public MediaPlayer getNonMatchPlayer() {
        return nonMatchPlayer;
    }

    public void setNonMatchPlayer(MediaPlayer nonMatchPlayer) {
        this.nonMatchPlayer = nonMatchPlayer;
    }

    public Timeline getAllowFlippingDelay() {
        return AllowFlippingDelay;
    }

    public void setAllowFlippingDelay(Timeline allowFlippingDelay) {
        AllowFlippingDelay = allowFlippingDelay;
    }

    public int getFlipHandlerVar() {
        return flipHandlerVar;
    }

    public void setFlipHandlerVar(int flipHandlerVar) {
        this.flipHandlerVar = flipHandlerVar;
    }

    public static int getAnimationNumber() {
        return animationNumber;
    }

    public static void setAnimationNumber(int animationNumber) {
        PlayCard.animationNumber = animationNumber;
    }

    public int getShowHandlerVar() {
        return showHandlerVar;
    }

    public void setShowHandlerVar(int showHandlerVar) {
        this.showHandlerVar = showHandlerVar;
    }

    public Timeline getDelay() {
        return delay;
    }

    public void setDelay(Timeline delay) {
        this.delay = delay;
    }

    public Timeline getShowAnimation() {
        return showAnimation;
    }

    public void setShowAnimation(Timeline showAnimation) {
        this.showAnimation = showAnimation;
    }

    public Timeline getFlipAnimation() {
        return flipAnimation;
    }

    public void setFlipAnimation(Timeline flipAnimation) {
        this.flipAnimation = flipAnimation;
    }

    public Image getShownImage() {
        return shownImage;
    }

    public void setShownImage(Image shownImage) {
        this.shownImage = shownImage;
    }

    public void setFlipped() {
        setImage(flipped);
    }

    public boolean isShown() {
        return isShown;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        this.isMatched = matched;
    }

    public Image getFlipped() {
        return flipped;
    }

    public Image getUnknown() {
        return unknown;
    }

    public void setShown(boolean isShown) {
        this.isShown = isShown;
    }

    public static void setSelectedCard(PlayCard selectedCard) {
        PlayCard.selectedCard = selectedCard;
    }

    public static PlayCard getSelectedCard() {
        return selectedCard;
    }

    public static int getImagesShown() {
        return imagesShown;
    }

    public static void setImagesShown(int imagesShown) {
        PlayCard.imagesShown = imagesShown;
    }

    public boolean isAllowFlipping() {
        return allowFlipping;
    }

    public void setAllowFlipping(boolean allowFlipping) {
        this.allowFlipping = allowFlipping;
    }

}
