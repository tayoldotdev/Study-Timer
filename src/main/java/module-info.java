module sample.stimer {
    requires javafx.controls;
    requires javafx.fxml;


    opens sample.stimer to javafx.fxml;
    exports sample.stimer;
}