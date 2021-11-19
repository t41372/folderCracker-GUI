/** Controller class of the mainMenu.
 * See src/main/resources/com/example/foldercrackergui/mainMenuLayout.fxml for layout of this window.
 *
 * Yi-Ting Chiu, 2021. 11. 19.
 */
package com.example.foldercrackergui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class mainMenuController {

    @FXML
    protected Label showSelectedPathLabel;
    protected Button selectFolderButton;
    protected Button executeButton;

    private File selectedDirectory;

    /** Handler function of the select folder button.
     * Will invoke a DirectoryChooser, get a folder from the user, and store it into File selectedDirectory
     *
     * @param actionEvent
     */
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

    /** Handler function of executeButton "Break All The Sub folders!".
     * Invoke the destroyAllFoldersMain and put selectedDirectory as parameter.
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    protected void executeButtonOnAction(ActionEvent actionEvent) throws IOException {
        if(selectedDirectory == null)
        {
            invokeInfoWindow("Wait", "Please select a folder first");
            return;
        }
        folderCracker.destroyAllFoldersMain(selectedDirectory, this);
        invokeInfoWindow("Action Complete", "Operation Complete!");
    }


    /** Invoke a JavaFX Confirmation Window with Yes No Buttons and texts.
     *
     * @param windowTitle title of the window
     * @param headerText there are two parts of text inside the window. headerText is what shows above the message.
     * @param yesButtonText the text of the Yes Button. Remember, there are two options right?
     * @param noButtonTxt the text of the No Button. Remember, there are two options right?
     * @param message the message you want to inform. This text appears below the headerText and above the buttons.
     * @return The user input. Return true if the user pushed yes button, false if user pushed no button.
     */
    public boolean invokeYesNoSelectorWindow(String windowTitle, String headerText, String yesButtonText,
                                             String noButtonTxt, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(windowTitle);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        ButtonType yesButton = new ButtonType(yesButtonText);
        ButtonType noButton = new ButtonType(noButtonTxt);

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yesButton) return true;
        else if (result.get() == noButton) return false;
        else {
            System.out.println("WFT??? Which button did the user pressed ?!!");
            invokeInfoWindow("What??", "I'm not receiving your response, so I pushed "
                    + yesButtonText + " for you! Thanks me!");
            return true;
        }

    }


    /** Invoke an information Window (JavaFX Alert) with given window title and message
     *
     * @param windowTitle The title of the information window
     * @param message The text inside the information window
     */
    public void invokeInfoWindow(String windowTitle, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(windowTitle);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}