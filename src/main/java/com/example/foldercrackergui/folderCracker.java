/** Utility Class. This class contains all the functionality used by the application, including file moving, deleting,
 * and other stuff. This class also has a main function, which allows this class to be executed without GUI for debugging.
 *
 * Yi-Ting Chiu, 2021. 11. 16
 */


package com.example.foldercrackergui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Stack;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class folderCracker
{
    //Store the instance of the main window, because we need to invoke alert window from its controller class.
    public static mainMenuController motherWindow;

    /**
     * Driver Function.
     * Delete all the folder under the given directory and move all the files out into a "result" folder.
     * The directory has to be a folder.
     * @param selectedDirectory directory that all the sub folders will be deleted
     * @throws IOException
     */
    public static void destroyAllFoldersMain(File selectedDirectory, mainMenuController motherWindowParam)
            throws IOException {
        motherWindow = motherWindowParam;

        Stack<File> allFiles = getEveryFilesInFolder(selectedDirectory);

        File resultFolder = new File(selectedDirectory + "//result//");
        if(!resultFolder.isDirectory())
            resultFolder.mkdir();

        moveFilesTo(allFiles, resultFolder);

        System.out.println("Those files have been moved!");

        deleteEmptyFolder(selectedDirectory);

        System.out.println("Those folders have been deleted!");

    }

    /** Get every file in a folder. Files in the sub folders are included.
     *
     * @param folderPath
     * @return a stack of the File instance of all the files in the folder.
     */
    private static Stack<File> getEveryFilesInFolder(File folderPath)
    {

        Stack<File> allFiles = new Stack<>();

        if(folderPath.isFile())//if the target folder is actually a file
        {
            allFiles.push(folderPath);
            return allFiles; //just return that file
        }

        //get everything under target directory
        File[] dirList = folderPath.listFiles();
        if(dirList == null) //if no file under the folder
            return allFiles; //return an empty stack

        for(File currentFile : dirList)
        {
            if(currentFile.isFile())
                allFiles.push(currentFile);
            else if(currentFile.isDirectory())
            {
                Stack<File> filesInSubFolder = getEveryFilesInFolder(currentFile);

                //push everything in "files in Sub Folder" into allFiles
                while(!filesInSubFolder.isEmpty())
                    allFiles.push(filesInSubFolder.pop());
                //now the all file stack should contain all the files in the sub folder
            }
        }
        return allFiles;
    }

    /** Move a stack of files to a target location. Will invoke a YesNoSelector window (GUI) if there
     * are two identical files to get user's opinion on dealing with it. To invoke the GUI, there must be an instance of
     * mainMenuController in this file, which is motherWindow (static).
     *
     * @param fileStack a stack of files that are going to be moved to target location
     * @param targetLocation the target location that will have all the files in fileStack after
     *                       the use of this function
     * @throws IOException
     */
    public static void moveFilesTo(Stack<File> fileStack, File targetLocation) throws IOException {
        File currentFile;

        //Name List of Files in target location
        HashMap<String, Integer> nameList = new HashMap<String, Integer>();

        //go through everyone in the stack
        //purpose is to move everyone to a new place in this loop
        while(!fileStack.isEmpty())
        {
            currentFile = fileStack.pop();
            String fileName = currentFile.getName();
            if(nameList.containsKey(fileName))
            {
                int appearTimes = nameList.get(fileName) + 1;
                String possibleNewName = putTextBetweenNameAndExtension(fileName, " (" + appearTimes + ")");
                nameList.put(fileName, appearTimes);

                System.out.println("\n-----------------------");

                System.out.println("Opes, the file named \"" + fileName + "\" already exist.");

                String message = "Do you want to rename the current file: \n\""
                        + currentFile.getName() + "\" in (" + currentFile.getAbsolutePath() + ")" +
                        "\nto\n\"" + possibleNewName + "\" ?\n\n" +
                        "Or replace another file (the file currently in result folder now, location: "
                        + targetLocation.getAbsolutePath() + "\\" + fileName + ")\n" +
                        " with this one?\n(" + currentFile.getAbsolutePath() + ")\n\n\n";


                //
                boolean yes = motherWindow.invokeYesNoSelectorWindow("Conflict",
                        "Opes, the file named \"" + fileName + "\" already exist.",
                        "Rename to \"" + possibleNewName + "\"",
                        "Replace with this file", message);
                if(yes)
                {
                    System.out.println("OK, I'll name this file to " + possibleNewName);
                    fileName = possibleNewName;
                }
                else
                    System.out.println("OK, I'll replace the existing one with the current file.");
                    //default option of move is replace, so no need to do anything here

                System.out.println("\n-----------------------");
            }
            //if the current fileName does not have conflict with existing files
            //or the user have selected a way to resolve the conflict
            nameList.put(fileName, 1);

            // A never appeared name situation
            Path targetPath = //get the path after moving the file
                    new File(targetLocation.getAbsolutePath() + "\\" + fileName).toPath();

            //really move it
            Files.move(currentFile.toPath(), targetPath, REPLACE_EXISTING);

        }


    }



    /** Delete all the empty folders under a directory. <br/>
     * <p>All the folders contain no file, even for those contained empty folders will be deleted.</p>
     *
     * @param targetLocation target location
     * @return true if everything under this folder has been deleted. False if there is something cannot be deleted.
     */
    public static boolean deleteEmptyFolder(File targetLocation)
    {
        // this means current directory and its sub folder (if exists) do not contain ANY file.
        boolean currentFolderCanBeDeleted = true;

        //things in stack are folders that contained something
        Stack<File> containedFolderStack = new Stack<>();

        for(File target : targetLocation.listFiles())
        {
            if(target.isDirectory())
            {
                if(target.listFiles().length == 0)
                {   //delete the folder that contains nothing
                    target.delete();
                    continue;
                }
                else containedFolderStack.push(target);
            }
            else
                currentFolderCanBeDeleted = false;

        }

        // some folder may only contain empty folder

        while(!containedFolderStack.isEmpty())
        {
            File currentFolder = containedFolderStack.pop();
            boolean isSubFolderDeletable = deleteEmptyFolder(currentFolder);
            if(isSubFolderDeletable)
                currentFolder.delete();
            else
                currentFolderCanBeDeleted = false;
        }
        return currentFolderCanBeDeleted;
    }

    /**
     * Insert a text after the file name, before the file extension. For example, insert " (1)" into "index.java" would
     * return "index (1).java". If fileName does not have an extension, the insertText will be appended after fileName
     * @param fileName The name of the file
     * @param insertText The text you want to insert between the file name and its extension
     * @return the result
     */
    private static String putTextBetweenNameAndExtension(String fileName, String insertText)
    {
        int indexOfExtension = fileName.lastIndexOf(".");
        return (indexOfExtension != -1) ?
                fileName.substring(0, indexOfExtension) + insertText + fileName.substring(indexOfExtension) :
                fileName + insertText;
    }


}
