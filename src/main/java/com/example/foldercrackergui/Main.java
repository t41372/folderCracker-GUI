package com.example.foldercrackergui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    public static Stage openStage;

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainMenuLayout.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        openStage = stage;


        stage.setTitle("Folder Cracker");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}