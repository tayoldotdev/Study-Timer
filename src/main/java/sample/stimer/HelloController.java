package sample.stimer;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class HelloController {
    private SaveData db = new SaveData(new File("src/main/resources/data.txt"));
    private MyTimer myTimer = new MyTimer();
    private boolean addNewPredmetClicked = false;
    private boolean addNewToDoClicked = false;
    private String currentPredmet;
    private String[] listPredmeti;
    private String[] listToDo;
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
    @FXML
    private TextField addNewPredmetField;
    @FXML
    private Button addNewPredmetButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TextField addNewToDoField;
    @FXML
    private Button addNewToDoButton;
    @FXML
    private Button deleteToDoButton;
    @FXML
    private Button checkUncheckButton;
    @FXML
    private Text imePredmeta;
    @FXML
    private Text casUcenja;
    @FXML
    private ListView<String> listView;
    @FXML
    private ListView<String> toDdListView;
    @FXML
    private Label Predmeti;

    public HelloController() throws IOException {
    }

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
                toDdListView.getItems().clear();
                toDdListView.getItems().addAll(SaveDataToDo.arrayOpravil(currentPredmet));
                checkUncheckButton.setDisable(false);
                checkUncheckButton.setVisible(true);
                checkUncheckButton.setText("☑");
                addNewToDoButton.setDisable(false);
                addNewToDoButton.setVisible(true);
                deleteToDoButton.setDisable(false);
                deleteToDoButton.setVisible(true);
                deleteButton.setVisible(true);
                casUcenja.setVisible(true);
                stoparicaButton.setVisible(true);
                timerButton.setVisible(true);
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
    private void addNewToDo() {
        if (!addNewToDoClicked) {
            addNewToDoClicked = true;
            addNewToDoField.setVisible(true);
            addNewToDoField.setDisable(false);
        } else {
            String newToDo = addNewToDoField.getText();
            if (!newToDo.equals("")){
                SaveDataToDo.newOpravilo(currentPredmet, newToDo, toDdListView);
                addNewToDoField.clear();
                toDdListView.getItems().clear();
                toDdListView.getItems().addAll(SaveDataToDo.arrayOpravil(currentPredmet));
                addNewToDoField.setVisible(false);
                addNewToDoField.setDisable(true);
                addNewToDoClicked = false;
            }

        }
    }

    @FXML
    private void dellToDo() {
        SaveDataToDo.deleteOpravilo(currentPredmet, toDdListView.getSelectionModel().getSelectedItem(), toDdListView);
    }

    @FXML
    private void checkUncheckToDo() {
        if (toDdListView.getSelectionModel().getSelectedItem() != null) {
            SaveDataToDo.checkUncheck(currentPredmet, toDdListView.getSelectionModel().getSelectedItem(), toDdListView);
        }
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
            myTimer.startStoparica(minuteStoparica);
            isStartStoparica = false;
        }else{

            int time = (int) ((System.currentTimeMillis() - timestamp)/1000);
            myTimer.endStoparica(minuteStoparica);
            System.out.println("stop "+time);
            db.updateTime(imePredmeta.getText(),time);
            startStopStoparica.setText("Start");
            updateTime();

            listView.setDisable(false);
            timerButton.setDisable(false);
            isStartStoparica = true;
        }

    }



    @FXML
    private void updateTime(){
        int input = db.timeUcenja(currentPredmet);
        int numberOfDays = input / 86400;
        int numberOfHours = (input % 86400) / 3600 ;
        int numberOfMinutes = ((input % 86400) % 3600) / 60;
        int numberOfSeconds = ((input % 86400) % 3600) % 60;
        String formattedTime = String.format("Cas ucenja: \n%d dni, %d ur, %d min, %d sec",
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
        timerPrev = Integer.parseInt(minuteTimer.getText())+1;
        if (! isPaused){timerPrevReset = timerPrev;}
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
        }, 0, 60000);
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
            pauseResumeTimerButton.setDisable(false);
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

    @FXML
    private void pauseResumeTimer() {
        if (startStopTimerClicked && !isPaused) {
            isPaused=true;
            int time = (int) ((System.currentTimeMillis() - timestamp)/1000);
            db.updateTime(imePredmeta.getText(),time);
            updateTime();
            pauseResumeTimerButton.setText("Resume");
        } else if (isPaused){
            timestamp = System.currentTimeMillis();
            startTimer();
            startStopTimerClicked = true;
            listView.setDisable(true);
            pauseResumeTimerButton.setDisable(false);
            pauseResumeTimerButton.setText("Stop");
            isPaused=false;
        }
    }





}
