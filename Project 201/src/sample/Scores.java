package sample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Scores implements Comparable<Scores>, Cloneable {

    private int previousTime = 0, matchedTime = 0, roundMoves = 0, previousMoves = 0, roundsCounter = 0, totalMoves = 0,
            totalScore = 0, matchedCounter = 0;
    private Timeline timeline;
    private Text totalScoreText = new Text("" + totalScore), movesText = new Text("" + roundMoves),
            roundsText = new Text("" + roundsCounter), scoreMessage = new Text("");
    private ArrayList<Integer> scoreList = new ArrayList<Integer>();
    private AnimationTimer timerAnimation;
    private static int maxMatched;
    private Date dateCreated;
    File score = new File("Scores.dat");

    Scores() {
        dateCreated = new Date(); // For future implementation (Online).
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
        timerAnimation = new AnimationTimer() {
            @Override
            public void handle(long l) {
            }
        };

        // To update the scores texts every half a second.
        Duration duration = Duration.millis(500);
        EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                totalScoreText.setText("" + totalScore);
                roundsText.setText("" + roundsCounter);
                movesText.setText("" + roundMoves);
            }
        };
        KeyFrame keyFrame = new KeyFrame(duration, onFinished);
        timeline.getKeyFrames().add(keyFrame);

    }

    // to calculate the score for every matching.
    public void calculateScore() {
        matchedTime = calcMatchedTime();
        int matchPeriod = matchedTime - previousTime;
        int matchMoves = roundMoves - previousMoves;
        System.out.println("match period: " + matchPeriod + " seconds");
        if (matchMoves <= 1) {
            totalScore += 10;
            System.out.println("Bonus: " + 10 + " points");
        } else if (matchMoves > 1 && matchMoves <= 2) {
            totalScore += 7;
            System.out.println("Bonus: " + 7 + " points");
        } else if (matchMoves > 2 && matchMoves <= 3) {
            totalScore += 5;
            System.out.println("Bonus: " + 5 + " points");
        } else if (matchMoves > 3 && matchMoves <= 5) {
            totalScore += 3;
            System.out.println("Bonus: " + 3 + " points");
        } else {
            totalScore++;
            System.out.println("Bonus: " + 1 + " point");
        }
        previousMoves = roundMoves;
        if (matchPeriod < 3 && matchPeriod >= 0) {
            totalScore += 10;
            System.out.println("Bonus: " + 10 + " points");
        } else if (matchPeriod >= 3 && matchPeriod < 5) {
            totalScore += 7;
            System.out.println("Bonus: " + 7 + " points");
        } else if (matchPeriod >= 5 && matchPeriod < 7) {
            totalScore += 5;
            System.out.println("Bonus: " + 5 + " points");
        } else if (matchPeriod >= 7 && matchPeriod < 10) {
            totalScore += 3;
            System.out.println("Bonus: " + 3 + " points");
        } else if (matchPeriod >= 10) {
            totalScore++;
            System.out.println("Bonus: " + 1 + " point");
        }
        previousTime = matchedTime;
    }

    // to calculate the time when two cards are matched.
    private int calcMatchedTime() {
        return Timer.getMinutes() * 60 + Timer.getSeconds();
    }

    // To compare between different players' scores (Future implementation)
    @Override
    public int compareTo(Scores pastScore) {
        if (totalScore / totalMoves < pastScore.getTotalScore() / pastScore.getTotalMoves()) {
            return -1;
        } else if (totalScore / totalMoves > pastScore.getTotalScore() / pastScore.getTotalMoves()) {
            return 1;
        } else {
            return 0;
        }
    }

    // to start the score counters.
    public void StartingButton() {
        timerAnimation.start();
        System.out.println("Started...");
        timeline.play();
    }

    // to reset the score counters for the next rounds.
    // - it will reset the round moves and will add it to the total moves.
    // - it will reset the match counter.
    // -it will increment the round counter by one.
    public void nextButton() {
        System.out.println("Started a next round...");
        matchedCounter = 0;
        roundsCounter++;
        totalMoves += roundMoves;
        roundMoves = 0;

    }

    public void giveUpButton() throws IOException {
        timerAnimation.stop();
        scoreMessage.setFont(Font.font("Arial", FontWeight.NORMAL, 30));
        System.out.println("Ended!");
        timeline.pause();
        // For further development (Online).
        if (matchedCounter == maxMatched) {
            // Save the scores in a binary file.
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(score));
            if (scoreList.size() > 0) {

                // If the player got a score less than or equal the highest score, it will
                // display a message that compare between his current score and the highest one.
                if (totalScore <= scoreList.get(scoreList.size() - 1)) {
                    scoreMessage.setText("High score:   " + scoreList.get(scoreList.size() - 1) + "\nYour score:   "
                            + getTotalScore());

                    // if he got a score more than the highest one, it will display massage that
                    // will congratulate him.
                } else {
                    scoreMessage.setText("Congratulation!!!\n You got the highest score: " + totalScore);
                }
                // To display the first score he got.
            } else {
                scoreMessage.setText("Your score:   " + getTotalScore());
            }

            // to add the score to the score list.
            scoreList.add(totalScore);

            //to sort the scores.
            scoreList.sort(null);
            outputStream.writeObject(scoreList);
            outputStream.close();
            totalScore = 0;
            roundMoves = 0;
            roundsCounter = 0;
            matchedCounter = 0;
            System.out.println("All counters have been reseted, game points have been added.");
        } else {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(score));
            if (scoreList.size() > 0) {
                if (totalScore <= scoreList.get(scoreList.size() - 1)) {
                    scoreMessage.setText("High score:   " + scoreList.get(scoreList.size() - 1) + "\nYour score:   "
                            + getTotalScore());
                } else {
                    scoreMessage.setText("Congratulation!!!\n You got the highest score: " + totalScore);
                }
            } else {
                scoreMessage.setText("Your score:   " + getTotalScore());
            }
            scoreList.add(totalScore);
            scoreList.sort(null);
            outputStream.writeObject(scoreList);
            outputStream.close();
            totalScore = 0;
            roundMoves = 0;
            roundsCounter = 0;
            matchedCounter = 0;
            System.out.println("All counters have been reseted, game points have been added.");
        }

        // A window to display the scores message.
        Stage stage = new Stage();
        stage.setHeight(200);
        stage.setWidth(500);
        Button btOk = new Button("OK");
        btOk.setPrefWidth(100);
        btOk.setOnAction(e -> {
            stage.close();
        });
        scoreMessage.setTextAlignment(TextAlignment.CENTER);
        HBox btBox = new HBox(btOk), textBox = new HBox(scoreMessage);
        textBox.setAlignment(Pos.CENTER);
        textBox.setPadding(new Insets(15));
        btBox.setPadding(new Insets(5));
        btBox.setAlignment(Pos.CENTER);
        VBox vbox = new VBox(textBox, btBox);
        vbox.setAlignment(Pos.CENTER);
        stage.setScene(new Scene(vbox));
        stage.setTitle("Notification");
        stage.show();

    }

    // Setters, getters, clone and toString methods.
    public ArrayList<Integer> getScoreList() {
        return scoreList;
    }

    public void setScoreList(ArrayList<Integer> scoreList) {
        this.scoreList = scoreList;
    }

    public int getRoundMoves() {
        return roundMoves;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getPreviousTime() {
        return previousTime;
    }

    public void setPreviousTime(int previousTime) {
        this.previousTime = previousTime;
    }

    public int getMatchedTime() {
        return matchedTime;
    }

    public void setMatchedTime(int matchedTime) {
        this.matchedTime = matchedTime;
    }

    public int getMatchedCounter() {
        return matchedCounter;
    }

    public void setMatchedCounter(int matchedCounter) {
        this.matchedCounter = matchedCounter;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getRoundsCounter() {
        return roundsCounter;
    }

    public void setRoundsCounter(int roundsCounter) {
        this.roundsCounter = roundsCounter;
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public Text getTotalScoreText() {
        return totalScoreText;
    }

    public void setTotalScoreText(Text totalScoreText) {
        this.totalScoreText = totalScoreText;
    }

    public Text getMovesText() {
        return movesText;
    }

    public void setMovesText(Text movesText) {
        this.movesText = movesText;
    }

    public Text getRoundsText() {
        return roundsText;
    }

    public void setRoundsText(Text roundsText) {
        this.roundsText = roundsText;
    }

    public AnimationTimer getTimerAnimation() {
        return timerAnimation;
    }

    public void setTimerAnimation(AnimationTimer timerAnimation) {
        this.timerAnimation = timerAnimation;
    }

    public int getMaxMatched() {
        return maxMatched;
    }

    public void setMaxMatched(int maxMatched) {
        Scores.maxMatched = maxMatched;
    }

    public void setRoundMoves(int moves) {
        this.roundMoves = moves;
    }

    public int getTotalMoves() {
        return totalMoves;
    }

    public void setTotalMoves(int totalMoves) {
        this.totalMoves = totalMoves;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public String toString() {
        return String.format("Score: %d, moves: ", totalScore, totalMoves);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Scores copy = (Scores) super.clone();
        copy.dateCreated = (Date) dateCreated.clone();
        return copy;
    }
}
