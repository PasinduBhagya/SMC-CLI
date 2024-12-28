package local.smc.scheduled.patching;

import java.util.Arrays;
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
                    System.out.println();
                    System.out.println("INFO: Printing Help Menu");
                    System.out.println("=============================================================================");
                    System.out.printf(" %-25s %-50s%n", "COMMAND", "DESCRIPTION");
                    System.out.println("=============================================================================");
                    System.out.println("=============================================================================");
                    System.out.printf(" %-25s %-50s%n", "help", "Displays the help menu.");
                    System.out.println("=============================================================================\n\n");
                    System.out.println("=============================================================================");
                    System.out.printf(" %-25s %-50s%n", "show-organizations", "Lists all available organizations.");
                    System.out.println("-----------------------------------------------------------------------------");
                    System.out.printf(" %-25s %-50s%n", "show-organization", "Displays details of a specific organization.");
                    System.out.println("-----------------------------------------------------------------------------");
                    System.out.printf(" %-25s %-50s%n", "add-organization", "Adds a new organization.");
                    System.out.println("-----------------------------------------------------------------------------");
                    System.out.printf(" %-25s %-50s%n", "delete-organization", "Deletes an organization by ID.");
                    System.out.println("-----------------------------------------------------------------------------");
                    System.out.printf(" %-25s %-50s%n", "update-organization", "Updates the name of an organization by ID.");
                    System.out.println("=============================================================================\n\n");
                    System.out.println("=============================================================================");
                    System.out.printf(" %-25s %-50s%n", "show-users", "Lists all users.");
                    System.out.println("-----------------------------------------------------------------------------");
                    System.out.printf(" %-25s %-50s%n", "show-user", "Displays details of a specific user.");
                    System.out.println("-----------------------------------------------------------------------------");
                    System.out.printf(" %-25s %-50s%n", "add-user", "Adds a new user.");
                    System.out.println("-----------------------------------------------------------------------------");
                    System.out.printf(" %-25s %-50s%n", "delete-user", "Deletes a user by ID.");
                    System.out.println("-----------------------------------------------------------------------------");
                    System.out.printf(" %-25s %-50s%n", "update-user", "Updates the details of a user by ID.");
                    System.out.println("=============================================================================\n\n");
                    System.out.println("=============================================================================");
                    System.out.printf(" %-25s %-50s%n", "show-computers", "Lists all computers.");
                    System.out.println("-----------------------------------------------------------------------------");
                    System.out.printf(" %-25s %-50s%n", "show-computer", "Displays details of a specific computer.");
                    System.out.println("-----------------------------------------------------------------------------");
                    System.out.printf(" %-25s %-50s%n", "add-computer", "Adds a new computer.");
                    System.out.println("-----------------------------------------------------------------------------");
                    System.out.printf(" %-25s %-50s%n", "delete-computer", "Deletes a computer by ID.");
                    System.out.println("-----------------------------------------------------------------------------");
                    System.out.printf(" %-25s %-50s%n", "update-computer", "Updates the details of a computer by ID.");
                    System.out.println("=============================================================================\n\n");
                    System.out.println("=============================================================================");
                    System.out.printf(" %-25s %-50s%n", "create-patching-ticket", "To create patching ticket for an organization");
                    System.out.println("=============================================================================\n\n");
                    System.out.println("=============================================================================================================================");
                    System.out.printf(" %-25s %-50s%n", "create-task", "Create a patching task by adding a computers of an organization with their contact points");
                    System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
                    System.out.printf(" %-25s %-50s%n", "get-tasks", "To get all tasks");
                    System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
                    System.out.printf(" %-25s %-50s%n", "get-task", "To get information about one task");
                    System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
                    System.out.printf(" %-25s %-50s%n", "delete-task", "To delete task");
                    System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
                    System.out.printf(" %-25s %-50s%n", "update-task", "To update task");
                    System.out.println("=============================================================================================================================\n\n");
                    System.out.println("=============================================================================");
                    System.out.printf(" %-25s %-50s%n", "quit", "Exits the program.");
                    System.out.println("=============================================================================");

                    System.out.println();
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
                    String[] computerData = Computers.getOneComputer();;
                    System.out.println("+-----------------+--------------------------------+---------------------+");
                    System.out.println("| Computer ID     | Computer Name                  | Organization ID     |");
                    System.out.println("+-----------------+--------------------------------+---------------------+");
                    System.out.printf("| %-15d | %-30s | %-19d |\n", Integer.parseInt(computerData[0]), computerData[1], Integer.parseInt(computerData[2]));
                    System.out.println("+-----------------+--------------------------------+---------------------+");
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
























































