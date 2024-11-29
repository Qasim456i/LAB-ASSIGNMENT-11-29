module tit3 {
    requires javafx.controls;
    requires javafx.fxml;

    opens tit3 to javafx.fxml;
    exports tit3;
}
