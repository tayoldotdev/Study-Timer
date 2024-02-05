package sample.stimer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class HelloController {
    private SaveData db = new SaveData();
    private boolean addNewPredmetClicked = false;
    private String currentPredmet;
    private String[] listPredmeti;
    private boolean start = true;
    private long timestamp;

    @FXML
    private TextField addNewPredmetField;

    @FXML
    private Button addNewPredmetButton;

    @FXML
    private Button deleteButton;


    @FXML
    private VBox vbox;

    @FXML
    private Text imePredmeta;

    @FXML
    private Text casUcenja;

    @FXML
    private Button startStop;

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
    private void startStop() throws InterruptedException {
        if (start) {
            listView.setDisable(true);
           timestamp = System.currentTimeMillis();
           startStop.setText("Stop");
            System.out.println("zacel");
            System.out.println(imePredmeta.getText());
           start = false;
        }else{
            listView.setDisable(false);
            int time = (int) ((System.currentTimeMillis() - timestamp)/100);
            db.updateTime(imePredmeta.getText(),time);
            startStop.setText("Start");
            start = true;
            updateTime();
        }

    }

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

}
