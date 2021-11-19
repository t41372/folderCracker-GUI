/** Main function of the application. Invoke the GUI.
 *
 * Yi-Ting Chiu, 2021. 11. 19.
 */
package com.example.foldercrackergui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {
    //store an instance after the creation of the main window for convenience.
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