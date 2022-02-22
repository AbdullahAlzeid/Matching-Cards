package sample;
import javafx.scene.layout.GridPane;
import java.util.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.*;

public class CardPane extends GridPane {
    ArrayList<Image> images = new ArrayList<>(); // List of images
    ArrayList<PlayCard> imageViews = new ArrayList<>(); // List of cards
    private final int IMAGES_NUMBER = 8;

    public CardPane(Scores score) {
        score.setMaxMatched(IMAGES_NUMBER);
        String imageName;
        for (int index = 0; index < IMAGES_NUMBER; index++) { // adding images
            imageName = (index + 1) + ".jpg";
            images.add(new Image(imageName));
        }
        for (int index = 0; index < images.size(); index++) { // adding images to playCards
            imageViews.add(new PlayCard(images.get(index), score));
            imageViews.add(new PlayCard(images.get(index), score));

        }
        Collections.shuffle(imageViews); // shuffle the cards
        setPadding(new Insets(15));
        setVgap(15);
        setHgap(15);
        setAlignment(Pos.CENTER);
        int index = 0;
        for (int row = 0; row < (int) Math.sqrt(imageViews.size()); row++) { // Adding the cards to the pane.
            for (int column = 0; column < (int) Math.sqrt(imageViews.size()); column++) {
                imageViews.get(index).setFlipped();
                add(imageViews.get(index++), column, row);
            }
        }
    }

    // to reset the game.
    public void resetAll() {
        for (int i = 0; i < imageViews.size(); i++) {
            imageViews.get(i).setFlipped(); // to flip the card.
            imageViews.get(i).setShown(false);
            imageViews.get(i).setMatched(false); // to unmatch the cards.
            if (PlayCard.getSelectedCard() != null) {
                PlayCard.getSelectedCard().setMatched(false);
                PlayCard.selectedCard.setShown(false);
            }
        }
        getChildren().clear(); // to remove the cards from the pane.
        Collections.shuffle(imageViews); // to shuffle the cards.
        // to add the cards after shuffling.
        int index = 0;
        for (int row = 0; row < (int) Math.sqrt(imageViews.size()); row++) { // to add the cards to the pane.
            for (int column = 0; column < (int) Math.sqrt(imageViews.size()); column++) {
                add(imageViews.get(index++), column, row);
            }
        }
    }
}
