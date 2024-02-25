package sample.stimer;

import javafx.application.Platform;
import javafx.scene.text.Text;

import java.util.Timer;
import java.util.TimerTask;


public class MyTimer {
    private Timer timer = new Timer();
    private TimerTask timerTask;
    private int timeStoparica;

    private int countDownTimerMin;
    private int countDownTimerMinReset;


    public void startStoparica(Text minuteStoparica) {

        timeStoparica = 0;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                timeStoparica++;
                Platform.runLater(() -> minuteStoparica.setText(Integer.toString(timeStoparica)));
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    public void endStoparica(Text minuteStoparica) {
        timeStoparica = 0;
        timerTask.cancel();
        timer.purge();
        Platform.runLater(() -> minuteStoparica.setText("00"));
    }



}