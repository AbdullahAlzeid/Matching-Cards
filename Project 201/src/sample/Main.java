package sample;

import java.io.*;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Main extends Application {
    // BorderPane for each screen.
    BorderPane[] pane = { new BorderPane(), new BorderPane(), new BorderPane(), new BorderPane() };
    // StackPane to center the counters on the rounded rectangles.
    StackPane[] fields = { new StackPane(), new StackPane(), new StackPane(), new StackPane() };
    HBox menuBar = new HBox(); // HBox to add the give up and next round buttons.
    VBox scoreBar = new VBox(); // VBox to arrange the counters.
    // FlowPanes to arrange the main menu buttons and the score board.
    FlowPane mainMenu = new FlowPane(), scoreBoardFlowPane = new FlowPane();

    Button btNext = new Button("Next round"), btStart = new Button("Start"), btExit = new Button("Give up"),
            btQuit = new Button("Quit Game"), btCredit = new Button("Credits"), btScores = new Button("Score board"),
            btCreditBack = new Button("Back"), btScoreBack = new Button("Back"); // Action buttons
    Scene scene = new Scene(pane[0]);
    Scores score = new Scores(); // Generate the score object.
    Timer timer = new Timer(score); // Generate a timer object.
    HBox CrScBox = new HBox(btCredit, btScores);
    CardPane cardPane = new CardPane(score); // Where the cards will be displayed
    // To get the counters.
    Text[] texts = { timer.getTimerText(), score.getTotalScoreText(), score.getMovesText(), score.getRoundsText() };

    // Generate counters' headers.
    Label[] labels = { new Label("Time:"), new Label("Score:"), new Label("Moves:"), new Label("Rounds") }; // Generate
    // Generate score board and credits headers.
    Text scoreBoardBanner = new Text("Score Board"), creditsBanner = new Text("Credits");
    // Arranging the headers.
    HBox scoreBannerBox = new HBox(scoreBoardBanner), creditsBannerBox = new HBox(creditsBanner);

    Text[] credits = { new Text("Omar Algamdi\t\t\t201855000"), new Text("Abdulaziz Allohayb\t\t201861340"),
            new Text("Abdullah Zeid\t\t\t201834480") };
    VBox creditsBox = new VBox();

    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage primaryStage) throws IOException {
        // To arrange the names.
        for (int i = 0; i < credits.length; i++) {
            credits[i].setFont(Font.font("Arial", FontWeight.NORMAL, 20));
            creditsBox.getChildren().add(credits[i]);
        }

        // Formating the layout.
        creditsBox.setPadding(new Insets(15));
        creditsBox.setSpacing(15);
        creditsBannerBox.setPadding(new Insets(15));
        creditsBannerBox.setAlignment(Pos.CENTER);
        scoreBannerBox.setPadding(new Insets(15));
        scoreBannerBox.setAlignment(Pos.CENTER);
        scoreBoardBanner.setFont(Font.font("Arial", FontWeight.NORMAL, 30));
        creditsBanner.setFont(Font.font("Arial", FontWeight.NORMAL, 30));
        scoreBoardFlowPane.setOrientation(Orientation.VERTICAL);
        scoreBoardFlowPane.setPadding(new Insets(20));
        scoreBoardFlowPane.setVgap(20);
        btStart.setPrefWidth(300);
        btQuit.setPrefWidth(300);
        btCredit.setPrefWidth(140);
        btScores.setPrefWidth(140);
        btCreditBack.setPrefWidth(140);
        CrScBox.setSpacing(20);
        btNext.setPrefWidth(150);
        btExit.setPrefWidth(150);
        mainMenu.setAlignment(Pos.CENTER);
        mainMenu.setOrientation(Orientation.VERTICAL);
        mainMenu.setVgap(15);
        menuBar.setAlignment(Pos.CENTER);
        menuBar.setSpacing(50);
        menuBar.setPadding(new Insets(15));
        scoreBar.setAlignment(Pos.BASELINE_LEFT);
        scoreBar.setSpacing(15);
        scoreBar.setPadding(new Insets(10));

        // to add the timer, total scores, round moves, total rounds to the game screen.
        for (int i = 0; i < fields.length; i++) {
            Rectangle rectangle = new Rectangle(150, 30, Color.WHITE);
            fields[i].getChildren().addAll(rectangle, texts[i]);
            rectangle.setArcHeight(20);
            rectangle.setArcWidth(20);
            scoreBar.getChildren().addAll(labels[i], fields[i]);

        }

        mainMenu.getChildren().addAll(btStart, CrScBox, btQuit); // To add the main menu buttons.
        menuBar.getChildren().addAll(btExit, btNext); // to add the menu bar buttons.
        pane[0].setCenter(mainMenu); // To put the main menu buttons to the center of the main menu.
        pane[0].setPrefSize(500, 500);

        // Create actions for the buttons.
        // to start the game.
        btStart.setOnAction(e -> {
            scene.setRoot(pane[1]);
            primaryStage.setWidth(650);
            primaryStage.setHeight(560);
            timer.StartingButton();
            score.StartingButton();
        });

        // to start the next round.
        btNext.setOnAction(e -> {
            // to flip all the cards.
            // It will check if all the cards matched with each other, the player cannot
            // start the next round unless he match all the cards.
            if (score.getMatchedCounter() == score.getMaxMatched()) {
                cardPane.resetAll(); // it will reset all the cards to the initial state.
                score.nextButton();
                timer.nextButton();
            }
        });

        // display the score board.
        btScores.setOnAction(e -> {
            try {
                File file = new File("Scores.dat");
                if (file.exists()) {
                    // create object input stream to read the array list from the binary file.
                    ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
                    int arrangeNumber = 1;
                    scoreBoardFlowPane.getChildren().clear(); // to clear the score board.
                    scoreBoardFlowPane.setAlignment(Pos.TOP_CENTER);
                    score.setScoreList((ArrayList<Integer>) inputStream.readObject()); // to read the list from the
                    // file.
                    inputStream.close(); // closing the stream.
                    score.getScoreList().sort(null); // to sort the scores.
                    // to display the scores from highest to lowest (Top 3).
                    for (int index = score.getScoreList().size() - 1; index >= score.getScoreList().size()
                            - 4; index--) {
                        // if the index is greater than or equal to zero it will display the scores.
                        if (index >= 0) {
                            Text text = new Text(
                                    "Top #" + (arrangeNumber++) + ": \t\t\t\t" + score.getScoreList().get(index));
                            text.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
                            scoreBoardFlowPane.getChildren().add(text); // arranging the top 3 scores.
                            // if the index is less than the zero it will exit from the loop.
                        } else {
                            break;
                        }
                    }
                    // if the file is not found it will display a message to tell the player that
                    // there is no previous scores.
                } else {
                    scoreBoardFlowPane.getChildren().clear();
                    Text text = new Text("No Score Available.");
                    scoreBoardFlowPane.setAlignment(Pos.CENTER);
                    text.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
                    scoreBoardFlowPane.getChildren().add(text);
                }

                btScoreBack.setPrefWidth(150);
                HBox btBackPane = new HBox(btScoreBack);
                btBackPane.setPadding(new Insets(15));
                btBackPane.setAlignment(Pos.CENTER);
                pane[3].setCenter(scoreBoardFlowPane);
                pane[3].setBottom(btBackPane);
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            // switch the root to the score board screen.
            scene.setRoot(pane[3]);
        });

        // switch the root to the main menu screen.
        btScoreBack.setOnAction(e -> {
            scene.setRoot(pane[0]);
        });

        // To exit the cards screen (return to main menu screen).
        // It will reset the counters, save and display the scores.
        btExit.setOnAction(e -> {
            scene.setRoot(pane[0]);
            try {
                score.giveUpButton();
            } catch (IOException e1) {
                System.err.println("File does not exist.");
            }
            cardPane.resetAll(); // it will reset the cards to the initial state.
            primaryStage.setWidth(500);
            primaryStage.setHeight(500);
            timer.giveUpButton();
        });

        // it will open the credits screen.
        btCredit.setOnAction(e -> {
            scene.setRoot(pane[2]);
            primaryStage.setWidth(500);
            primaryStage.setHeight(300);
            HBox btBackPane = new HBox(btCreditBack);
            btBackPane.setPadding(new Insets(15));
            btBackPane.setAlignment(Pos.CENTER);
            pane[2].setBottom(btBackPane);
        });

        // it will return to the main menu screen from the credits screen
        btCreditBack.setOnAction(e -> {
            scene.setRoot(pane[0]);
            primaryStage.setWidth(500);
            primaryStage.setHeight(500);
        });

        // it will close the game.
        btQuit.setOnAction(e -> {
            primaryStage.close();
        });

        pane[1].setTop(menuBar);
        pane[1].setLeft(cardPane);
        pane[1].setCenter(scoreBar);
        pane[2].setTop(creditsBannerBox);
        pane[2].setCenter(creditsBox);
        pane[3].setTop(scoreBannerBox);
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false); // to prevent the user from resizing the window.
        primaryStage.setTitle("Team#9 Project");
        primaryStage.setScene(scene);
        primaryStage.show(); // to display the window.
    }

    public static void main(String[] args) {
        launch(args);
    }
}
