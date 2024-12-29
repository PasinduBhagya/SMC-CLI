package local.smc.scheduled.patching;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class MainMenu {

    public static void Menu(){

        System.out.println("INFO: Welcome To Patching Menu\n");

        Scanner commandInput = new Scanner(System.in);

        while (true){
            System.out.print("patching-calender> ");
            String command = commandInput.nextLine();
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
                    String[] organizationData = Organization.getOneOrganization();

                    System.out.println("+-----------------+--------------------------------------+");
                    System.out.println("| Organization ID | Organization Name                    |");
                    System.out.println("+-----------------+--------------------------------------+");
                    System.out.printf("| %-15d | %-36s |\n", Integer.parseInt(organizationData[0]), organizationData[1]);
                    System.out.println("+-----------------+--------------------------------------+");

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
                    String[] userData = Users.getOneUser();

                    System.out.println("+------------+---------------------+---------------------+----------------------------------+------------+");
                    System.out.println("| User ID    | First Name          | Last Name           | Email                            | OrgID      |");
                    System.out.println("+------------+---------------------+---------------------+----------------------------------+------------+");

                    System.out.printf("| %-10d | %-19s | %-19s | %-32s | %-10d |\n",
                            Integer.parseInt(userData[0]), userData[1], userData[2], userData[3], Integer.parseInt(userData[4]));
                    System.out.println("+------------+---------------------+---------------------+----------------------------------+------------+");
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
                    String[] taskData = Tasks.getOneTask();

                    System.out.println("+----------------------+---------------------------------------------------------+");
                    System.out.printf("| %-20s | %-55s |%n", "Field", "Value");
                    System.out.println("+----------------------+---------------------------------------------------------+");
                    System.out.printf("| %-20s | %-55s |%n", "Task ID", taskData[0]);
                    System.out.println("+----------------------+---------------------------------------------------------+");
                    System.out.printf("| %-20s | %-55s |%n", "Task Name", taskData[1]);
                    System.out.println("+----------------------+---------------------------------------------------------+");
                    System.out.printf("| %-20s | %-55s |%n", "Organization Name", taskData[2]);
                    System.out.println("+----------------------+---------------------------------------------------------+");
                    System.out.printf("| %-20s | %-55s |%n", "Primary Contact", taskData[4]);
                    System.out.println("+----------------------+---------------------------------------------------------+");
                    System.out.printf("| %-20s | %-55s |%n", "Secondary Contacts", taskData[5]);
                    System.out.println("+----------------------+---------------------------------------------------------+");
                    System.out.printf("| %-20s | %-55s |%n", "Computer Names", taskData[6]);
                    System.out.println("+----------------------+---------------------------------------------------------+");
                    System.out.printf("| %-20s | %-55s |%n", "Created Date", taskData[7]);
                    System.out.println("+----------------------+---------------------------------------------------------+");
                    System.out.printf("| %-20s | %-55s |%n", "Updated Date", taskData[8]);
                    System.out.println("+----------------------+---------------------------------------------------------+");
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
























































