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
import java.util.Locale;
import java.util.Scanner;
import java.util.Stack;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class folderCracker
{
    /**
     * This main function drive the CLI version of the app for debug purposes.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        File targetLocation;

        if(args != null && args.length > 0)
        {
            targetLocation = new File(args[0]);
        }
        else{
            System.out.println("Folder Cracker will help you DESTROY your folder, " +
                    "and get everything inside the folder out");
            System.out.println("Please give me a location, so I can break *ALL of the folder in that location! ");
            Scanner sc = new Scanner(System.in);
            targetLocation = new File(sc.nextLine());
        }

        if(!targetLocation.isDirectory())
        {
            System.out.println("Opes, This is not a valid folder, isn't it?\nReenter a valid folder please?");
            main(new String[0]);
        }



        System.out.println("Thanks for your location! Now run the program.");

        destroyAllFoldersMain(targetLocation);

    }

    /**
     * Driver function.
     * Delete all the folder under the given directory and move all the files out into a "result" folder.
     * The directory has to be a folder.
     * @param destroyTargetsLocation
     * @throws IOException
     */
    public static void destroyAllFoldersMain(File destroyTargetsLocation) throws IOException {
        Stack<File> allFiles = getEveryFilesInFolder(destroyTargetsLocation);

        //dev print the stack
        //while(!allFiles.isEmpty())
        //    System.out.println(allFiles.pop());

        File targetLocation = new File(destroyTargetsLocation + "//result//");
        if(!targetLocation.isDirectory())
            targetLocation.mkdir();

        moveFilesTo(allFiles, targetLocation);

        System.out.println("Those files have been moved!");

        deleteEmptyFolder(destroyTargetsLocation);

        System.out.println("Those folders have been deleted!");


    }

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
                String possibleNewName = "(" + appearTimes + ") " + fileName;
                nameList.put(fileName, appearTimes);

                System.out.println("\n-----------------------");

                System.out.println("Opes, the file named \"" + fileName + "\" already exist.");

                while(true) { //flow control for user input error handling

                    yesNoSelectorController controller = new yesNoSelectorController();
                    boolean yes = controller.buildWindow("\nDo you want to rename the current file: \n("
                            + currentFile.getAbsolutePath() + ") \nto \"" + possibleNewName + "\" ? (enter y)\n\n" +
                            "or if you want to replace another file (that file is in result folder now) " +
                            "(" + targetLocation.getAbsolutePath() + "\\" + fileName + ")\n" +
                            " with this one? (enter n)" +
                            "\n(Y/N)");

                    //!! Current Problem: We cannot get the y/n data back from the window

                    if(yes)
                    {
                        //make possible name the real name
                        fileName = possibleNewName;
                    }
                    else
                    {
                        System.out.println("OK, I'll replace the existing one with the current file.");
                        //default option of move is replace, so no need to do anything here
                    }

                    System.out.println("\n-----------------------");
                    break;
                }//true loop ends



            } else {
                nameList.put(fileName, 1);

                // A never appeared name situation
                Path targetPath = //get the path after moving the file
                        new File(targetLocation.getAbsolutePath() + "\\" + fileName).toPath();

                //really move it
                Files.move(currentFile.toPath(), targetPath, REPLACE_EXISTING);
            }
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




    /**Get the list of all the files/folders under the targeted directory. <br/>
     * Return Null if folderPath is not valid or Empty.
     *
     * @param folderPath the target directory which I will find things in.
     * @return Everything under the target directory. In the form of File[]. Return Null if its Empty or Invalid
     */
    public static File[] getDirectoriesInAFolder(File folderPath)
    {
        if(!folderPath.isDirectory()) {
            System.out.println("Shit, this is not even a folder");
            return null;
        }

        //get all the names of folders/files under the directory
        String[] fileNames = folderPath.list();

        if(fileNames == null) //if the folder is empty
        {
            return null;
        }


        //convert those String file names into a File[]
        File[] result = new File[fileNames.length];

        for(int index = 0; index < fileNames.length; index ++)
        {
            result[index] = new File(folderPath.getAbsolutePath() + "\\" + fileNames[index]);
        }

        return result;
    }


}
