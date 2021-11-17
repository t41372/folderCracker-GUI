package com.example.foldercrackergui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class mainMenuController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}