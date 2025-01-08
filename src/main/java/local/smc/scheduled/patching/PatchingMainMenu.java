package local.smc.scheduled.patching;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class PatchingMainMenu {

    public static void Menu(){

        System.out.println("INFO: Welcome To Patching Menu\n");

        Scanner commandInput = new Scanner(System.in);

        while (true){
            System.out.print("patching-calender> ");
            String command = commandInput.nextLine().trim();
            switch (command){
                case "":
                    continue;

                case "help":
                    System.out.println("INFO: Printing the helper Menu.\n");
                    String helpFilePath = "config/help-content/patching-calender-help.txt";
                    String helpLine;
                    try (BufferedReader helpFileContent = new BufferedReader(new FileReader(helpFilePath))){
                        while ((helpLine = helpFileContent.readLine()) != null){
                            String optionValue = helpLine.split(",")[0];
                            String optionDescription = helpLine.split(",")[1];
                            System.out.printf(" %-30s %-50s\n", optionValue, optionDescription);
                        }
                    } catch (IOException e){
                        System.out.println("Error: Unable to open the help.txt file.");
                    }
                    break;

                case "show-organizations":
                    Organization.getAllOrganizations();
                    break;

                case "add-organization":
                    Organization.addOrganization();
                    break;

                case "show-organization":
                    Organization.getOneOrganization();
                    break;

                case "delete-organization":
                    Organization.deleteOrganization();
                    break;

                case "update-organization":
                    Organization.updateOrganization();
                    break;

                case "show-users":
                    Users.getAllUsers();
                    break;

                case "show-user":
                    Users.getOneUser();
                    break;

                case "add-user":
                    Users.addUser();
                    break;

                case "delete-user":
                    Users.deleteUser();
                    break;

                case "update-user":
                    Users.updateUser();
                    break;

                case "show-computers":
                    Computers.getAllComputers();
                    break;

                case "show-computer":
                    Computers.getOneComputer();
                    break;

                case "add-computer":
                    Computers.addComputer();
                    break;

                case "delete-computer":
                    Computers.deleteComputer();
                    break;

                case "update-computer":
                    Computers.updateComputer();
                    break;

                case "create-task":
                    Tasks.addTask();
                    break;

                case "get-tasks":
                    Tasks.getAllTasks();
                    break;

                case "get-task":
                    Tasks.getOneTask();
                    break;

                case "start-task":
                    Tasks.startTask();
                    break;

                case "delete-task":
                    Tasks.deleteTask();
                    break;

                case "update-task":
                    Tasks.updateTask();
                    break;

                case "quit":
                    System.out.println("Bye");
                    commandInput.close();
                    return;

                default:
                    System.out.println("ERROR: Invalid command. Use 'help' command to see available commands.");
            }
        }
    }
}
























































