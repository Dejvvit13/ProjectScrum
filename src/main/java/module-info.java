module com.example.threeelementsgamescrum {
    requires javafx.controls;
    requires javafx.fxml;
    requires AnimateFX;


    opens com.example.threeelementsgamescrum to javafx.fxml;
    exports com.example.threeelementsgamescrum;
}