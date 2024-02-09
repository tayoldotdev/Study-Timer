package sample.stimer;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.util.Timer;
import java.util.TimerTask;

public class HelloController {
    private SaveData db = new SaveData();
    private boolean addNewPredmetClicked = false;
    private String currentPredmet;
    private String[] listPredmeti;
    private long timestamp;

    @FXML
    private BorderPane borderPaneTimer;
    private  Timer timer;
    private boolean isPaused = false;
    private boolean startStopTimerClicked = false;
    private int timerPrev;
    private int timerPrevReset;
    @FXML
    private Button startEndTimerButton;
    @FXML
    private Button pauseResumeTimerButton;
    @FXML
    private Button resetTimer;
    @FXML
    private Button timerButton;
    @FXML
    private Button upButton;
    @FXML
    private Button downButton;
    @FXML
    private Text minuteTimer;


    private boolean isStartStoparica = true;
    @FXML
    private Button stoparicaButton;
    @FXML
    private Button startStopStoparica;
    @FXML
    private Text minuteStoparica;
    @FXML
    private BorderPane borderPaneStoparica;
    int timeStoparica=0;



    @FXML
    private TextField addNewPredmetField;
    @FXML
    private Button addNewPredmetButton;
    @FXML

    private Button deleteButton;





    @FXML
    private Text imePredmeta;

    @FXML
    private Text casUcenja;





    @FXML
    private ListView<String> listView; // Specify the type for ListView

    @FXML
    private Label Predmeti;

    @FXML
    private void initialize() {
        // Initialize the list of predmeti and populate the ListView
        listPredmeti = db.getArrayListPredmetov().toArray(new String[0]);
        listView.getItems().addAll(listPredmeti);

        // Add a listener to handle selection changes in the ListView
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                currentPredmet = listView.getSelectionModel().getSelectedItem();
                imePredmeta.setText(currentPredmet);
                casUcenja.setVisible(true);
                updateTime();
            }
        });
    }

    @FXML
    private void addNewPredmet() {
        if (!addNewPredmetClicked) {
            // Enable the text field for adding a new predmet
            addNewPredmetClicked = true;
            addNewPredmetField.setVisible(true);
            addNewPredmetField.setDisable(false);
        } else {
            // Add the new predmet to the database and update UI
            String newPredmet = addNewPredmetField.getText();
           if (!newPredmet.equals("")){
               db.newPredmet(newPredmet);
               listView.getItems().add(newPredmet);
               System.out.println(newPredmet);
               addNewPredmetField.clear();
               addNewPredmetField.setVisible(false);
               addNewPredmetField.setDisable(true);
               addNewPredmetClicked = false;
           }

        }
    }

    @FXML
    private void dellPredmet() {
        String delPredmet = imePredmeta.getText();
        db.delete(delPredmet);
        listView.getItems().remove(delPredmet);

    }

    @FXML
    private void stoparicaVisible() {
        borderPaneTimer.setVisible(false);
        borderPaneStoparica.setVisible(true);
    }

    @FXML
    private void timerVisible() {
        borderPaneStoparica.setVisible(false);
        borderPaneTimer.setVisible(true);
    }



    @FXML
    private void startStopStoparica() throws InterruptedException {
        if (isStartStoparica) {
            listView.setDisable(true);
            timerButton.setDisable(true);
            timestamp = System.currentTimeMillis();
            startStopStoparica.setText("Stop");
            startStoparica();
           isStartStoparica = false;
        }else{

            int time = (int) ((System.currentTimeMillis() - timestamp)/1000);
            System.out.println("stop "+time);
            db.updateTime(imePredmeta.getText(),time);
            startStopStoparica.setText("Start");
            minuteStoparica.setText("00");

            updateTime();
            timeStoparica = 0;
            listView.setDisable(false);
            timerButton.setDisable(false);
            timer.cancel();
            timer.purge();
            isStartStoparica = true;
        }

    }
    private void startStoparica() {

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                    timeStoparica ++;
                    Platform.runLater(() -> minuteStoparica.setText(Integer.toString(timeStoparica)));

            }
        }, 0, 1000);
    }


    @FXML
    private void updateTime(){
        int input = db.timeUcenja(currentPredmet);
        int numberOfDays = input / 86400;
        int numberOfHours = (input % 86400) / 3600 ;
        int numberOfMinutes = ((input % 86400) % 3600) / 60;
        int numberOfSeconds = ((input % 86400) % 3600) % 60;
        String formattedTime = String.format("Cas ucenja: %d days, %d hours, %d minutes, %d seconds",
                numberOfDays, numberOfHours, numberOfMinutes, numberOfSeconds);
        casUcenja.setText(formattedTime);
    }

    @FXML
    private void upMinute (){
        minuteTimer.setText(Integer.toString(Integer.parseInt(minuteTimer.getText()) + 1));
    }
    @FXML
    private void downMinute (){
        minuteTimer.setText(Integer.toString(Integer.parseInt(minuteTimer.getText()) - 1));
    }

    @FXML
    private void startTimer() {
        timerPrev = Integer.parseInt(minuteTimer.getText());
        timerPrevReset = timerPrev;
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (timerPrev > 0) {
                    timerPrev--;
                    Platform.runLater(() -> minuteTimer.setText(Integer.toString(timerPrev)));
                } else {
                    Platform.runLater(() -> {
                        int time = (int) ((System.currentTimeMillis() - timestamp)/1000);
                        System.out.println("stop "+time);
                        db.updateTime(imePredmeta.getText(),time);
                        updateTime();
                        minuteTimer.setText("00");
                        startEndTimerButton.setText("start");
                        listView.setDisable(false);
                        startStopTimerClicked = false;
                        pauseResumeTimerButton.setDisable(false);
                        timer.cancel();
                        timer.purge();
                    });
                }
            }
        }, 0, 1000);
    }


    @FXML
    private void resetTimer (){
        minuteTimer.setText(Integer.toString(timerPrevReset));
    }


    @FXML
    private void startEndTimer() {
        if (!startStopTimerClicked && !isPaused) {
            timestamp = System.currentTimeMillis();
            startTimer();
            startEndTimerButton.setText("End");
            startStopTimerClicked = true;
            listView.setDisable(true);
            pauseResumeTimerButton.setDisable(true);
        } else if (startStopTimerClicked){

            int time = (int) ((System.currentTimeMillis() - timestamp)/1000);
            System.out.println("stop "+time);
            db.updateTime(imePredmeta.getText(),time);
            updateTime();
            startEndTimerButton.setText("Start");
            minuteTimer.setText("00");
            listView.setDisable(false);
            startStopTimerClicked = false;
            pauseResumeTimerButton.setDisable(false);
            timer.cancel();
            timer.purge();
        }
    }





}
