package com.example.foldercrackergui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;

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
        folderCracker.destroyAllFoldersMain(selectedDirectory);
    }
}