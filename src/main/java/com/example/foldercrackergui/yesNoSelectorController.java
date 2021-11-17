package com.example.foldercrackergui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class yesNoSelectorController {

    public static Stage yesNoSelectorStage;

    public Button yesButton;
    public Button noButton;
    public Label messageLabel;

    public boolean result;
    public boolean receivedResult = false;

    public yesNoSelectorController()
    {
        yesNoSelectorStage = new Stage();
        yesNoSelectorStage.setTitle("Conflict");
        yesNoSelectorStage.setScene(Main.yesNoSelectorScene);
    }

    //!! Current Problem: We cannot return the y/n value back to folderCracker.
    public boolean buildWindow(String message)
    {
        yesNoSelectorStage.showAndWait();
        if(messageLabel!=null) messageLabel.setText(message);
        System.out.println("message is " + message);

        while(!receivedResult)
        {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("waiting");
        }

        return result;
    }

    @FXML
    protected void yesButtonOnAction(ActionEvent actionEvent)
    {
        result = true;
        receivedResult = true;
        System.out.println("YESS");
    }

    @FXML
    protected void noButtonOnAction(ActionEvent actionEvent)
    {
        result = false;
        receivedResult = true;
        System.out.println("NOO!!");
    }

}
