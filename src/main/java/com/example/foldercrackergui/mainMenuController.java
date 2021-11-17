package com.example.foldercrackergui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class mainMenuController {

    @FXML
    protected Label showSelectedPathLabel;
    protected Button selectFolderButton;
    protected Button executeButton;

    private File selectedDirectory;

    //constructor
    public mainMenuController()
    {
        //executeButton.setDisable(true);
        //selectFolderButton.setDisable(true);
    }

    @FXML
    protected void selectFolderButtonOnAction(ActionEvent actionEvent)
    {
        //Open the File chooser

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a Folder");
        File selectedFolder = directoryChooser.showDialog(Main.openStage);
        System.out.println("the folder is " + selectedFolder);

        if(selectedFolder == null) return; //if the user didn't select a folder, then do nothing and exit.

        if(selectedFolder.isDirectory())
        {
            selectedDirectory = selectedFolder;
            showSelectedPathLabel.setText(selectedDirectory.toString());

        }
        else
        {
            System.out.println("Invalid Path or Selected Nothing");
            showSelectedPathLabel.setText("Selected Nothing.");
        }


    }
    @FXML
    protected void executeButtonOnAction(ActionEvent actionEvent) throws IOException {
        folderCracker.destroyAllFoldersMain(selectedDirectory, this);
    }


    public boolean invokeYesNoSelectorWindow(String fileName, String possibleName, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conflict");
        alert.setHeaderText("Opes, the file named \"" + fileName + "\" already exist.");
        alert.setContentText(message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);


        ButtonType yesButton = new ButtonType("Rename to \"" + possibleName + "\"");
        ButtonType noButton = new ButtonType("Replace with this file");

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yesButton) return true;
        else if (result.get() == noButton) return false;
        else {
            System.out.println("WFT??? Which button did the user pressed ?!!");
            return false;
        }

    }
}