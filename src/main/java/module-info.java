module com.example.foldercrackergui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.foldercrackergui to javafx.fxml;
    exports com.example.foldercrackergui;
}