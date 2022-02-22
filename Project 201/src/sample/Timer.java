package sample;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Timer {
    private Timeline timeline;
    private AnimationTimer timer;
    private Text text;
    private String StopWatch = null;
    private static int minutes = 0;
    private static int seconds = 0;

    public static int getMinutes() {
        return minutes;
    }

    public static int getSeconds() {
        return seconds;
    }

    public Timer(Scores score) {
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
            }
        };
        text = new Text("0" + minutes + ":0" + seconds); // the initial state of the timer text.

        Duration duration = Duration.seconds(1); // to update the time every one second
        EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                // if the cards are matched then it will stop the timer.
                if (score.getMatchedCounter() < score.getMaxMatched()) {
                    seconds++;
                    // it will update the text every one second.
                    text.setText(getTimerString());
                    System.out.println("time update: " + getTimerString());
                } else if (score.getMatchedCounter() == score.getMaxMatched()) {
                    timeline.pause();
                }
            }
        };
        KeyFrame keyFrame = new KeyFrame(duration, onFinished);
        timeline.getKeyFrames().add(keyFrame);

    }

    public Timer() {
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
            }
        };
        text = new Text("0" + minutes + ":0" + seconds);

        Duration duration = Duration.millis(1000);
        EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                seconds++;
                text.setText(getTimerString());
                System.out.println(getTimerString());
            }
        };
        KeyFrame keyFrame = new KeyFrame(duration, onFinished);
        timeline.getKeyFrames().add(keyFrame);

    }

    // to start the timer.
    public void StartingButton() {
        timer.start();
        System.out.println("Timer has started...");
        timeline.play();
    }

    // to stop the timer.
    public void giveUpButton() {
        timer.stop();
        System.out.println("Timer Ended!");
        timeline.pause();
        seconds = 0;
        minutes = 0;
    }

    // to reset the timer for the next round.
    public void nextButton() {
        timer.start();
        System.out.println("Timer has been reseted!");
        timeline.play();
        seconds = 0;
        minutes = 0;
    }

    // to get the text object of the timer.
    public Text getTimerText() {
        return text;
    }

    // to get the timer of four digits format.
    public String getTimerString() {
        if (seconds < 60) {
            if (seconds < 10 && minutes < 10) { // 0X:0X format.
                StopWatch = "0" + minutes + ":0" + seconds;
            } else if (seconds >= 10 && minutes < 10) { // 0X:XX format
                StopWatch = "0" + minutes + ":" + seconds;
            } else if (seconds < 10 && minutes >= 10) { // XX:0X format
                StopWatch = minutes + ":0" + seconds;
            } else if (minutes >= 10 && seconds >= 10) { // XX:XX format
                StopWatch = minutes + ":" + seconds;

            } else {
                StopWatch = "ERROR"; // if the timer has a glitch
            }
        } else if (seconds == 60) {
            minutes++;
            seconds = 0;
            if (minutes < 10) { // 0X:00 format
                StopWatch = "0" + minutes + ":0" + seconds;
            } else if (minutes >= 10) { // XX:00 format
                StopWatch = minutes + ":0" + seconds;

            } else {
                StopWatch = "ERROR"; // if the timer has a glitch
            }
        }
        return StopWatch;
    }
}
