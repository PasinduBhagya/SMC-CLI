package local.smc.scheduled.patching;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static local.smc.common.Database.connectToDatabase;

public class Tasks {
    public static void addTask(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter task name: ");
        String taskName = scanner.nextLine();

        Organization.getAllOrganizations();
        System.out.print("Enter the Organization ID: ");
        int organizationID = scanner.nextInt();
        scanner.nextLine();

        Users.getAllUsersByOrg(organizationID);
        System.out.print("Enter the primary contact user ID: ");
        int primaryContactID = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter the other contact user IDs [Add multiple users separated by commas]: ");
        String otherContactIDs = scanner.nextLine();

        Computers.getAllComputersByOrg(organizationID);
        System.out.print("Enter the Computer IDs [Add multiple computers separated by commas]: ");
        String computerIDs = scanner.nextLine();

        String query = "INSERT INTO patching_calender_tasks (taskName, organizationID, primaryContactID, otherContactIDs, computerIDs) VALUES (?, ?, ?, ?, ?)";
        System.out.println("INFO: Adding task - " + taskName);

        try (Connection connection = connectToDatabase()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, taskName);
                preparedStatement.setInt(2, organizationID);
                preparedStatement.setInt(3, primaryContactID);
                preparedStatement.setString(4, otherContactIDs.isEmpty() ? null : otherContactIDs);
                preparedStatement.setString(5, computerIDs.isEmpty() ? null : computerIDs);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("INFO: Task added successfully.");
                } else {
                    System.err.println("ERROR: Task not added.");
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }

    }

    public static void getAllTasks() {
        String query = "SELECT taskID, taskName FROM patching_calender_tasks";

        try (Connection connection = connectToDatabase();
             Statement statement = connection != null ? connection.createStatement() : null;
             ResultSet resultSet = statement != null ? statement.executeQuery(query) : null) {

            if (resultSet != null) {
                System.out.println("+------------+----------------------------------------------+");
                System.out.println("| Task ID    | Task Name                                    |");
                System.out.println("+------------+----------------------------------------------+");

                while (resultSet.next()) {
                    int taskID = resultSet.getInt("taskID");
                    String taskName = resultSet.getString("taskName");
                    System.out.printf("| %-10d | %-44s |\n", taskID, taskName);
                    System.out.println("+------------+----------------------------------------------+");
                }


            } else {
                System.out.println("No tasks found.");
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }
    }

    public static String[] getOneTask() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("----------------------------------------------------------------------------------");
        System.out.print("Enter the Task ID: ");
        int taskID = scanner.nextInt();
        scanner.nextLine();

        String query = """
        SELECT
            tasks.taskName,
            organization.orgName,
            tasks.organizationID,
            users.FirstName AS primaryContactFirstName,
            users.LastName AS primaryContactLastName,
            tasks.otherContactIDs,
            tasks.computerIDs,
            tasks.createdAt,
            tasks.updatedAt
        FROM
            patching_calender_tasks AS tasks
        JOIN
            patching_calender_organizations AS organization
        ON
            tasks.organizationID = organization.orgID
        LEFT JOIN
            patching_calender_users AS users
        ON
            tasks.primaryContactID = users.userID
        WHERE
            tasks.taskID =""" + taskID;

        String[] taskData = null;

        try (Connection connection = connectToDatabase();
             Statement statement = connection != null ? connection.createStatement() : null;
             ResultSet resultSet = statement != null ? statement.executeQuery(query) : null) {

            if (resultSet != null && resultSet.next()) {
                String taskName = resultSet.getString("tasks.taskName");
                String orgName = resultSet.getString("organization.orgName");
                String orgID = resultSet.getString("tasks.organizationID");
                String primaryContactFirstName = resultSet.getString("primaryContactFirstName");
                String primaryContactLastName = resultSet.getString("primaryContactLastName");
                String otherContactIDs = resultSet.getString("tasks.otherContactIDs");
                String computerIDs = resultSet.getString("tasks.computerIDs");
                java.sql.Timestamp createdAt = resultSet.getTimestamp("createdAt");
                java.sql.Timestamp updatedAt = resultSet.getTimestamp("updatedAt");

                List<String> userDataList = new ArrayList<>();

                if (otherContactIDs != null) {
                    String[] otherContactIDsArray = otherContactIDs.split(",");

                    for (String userID : otherContactIDsArray) {
                        String[] userData = Users.getOneUser(userID.trim());
                        if (userData != null && userData.length >= 3) {
                            String userFullName = userData[1] + " " + userData[2];
                            userDataList.add(userFullName);
                        }
                    }
                }

                List<String> computerDataList = new ArrayList<>();

                if (computerIDs != null) {
                    String[] computerIDsArray = computerIDs.split(",");
                    for (String compID : computerIDsArray){
                        String[] computerData = Computers.getOneComputer(Integer.parseInt(compID));
                        if (computerData != null && computerData.length > 2){
                            String computerName = computerData[1];
                            computerDataList.add(computerName);
                        }
                    }
                }

                String createdAtStr = createdAt != null ? createdAt.toString() : "N/A";
                String updatedAtStr = updatedAt != null ? updatedAt.toString() : "N/A";

                taskData = new String[]{
                        String.valueOf(taskID),
                        taskName,
                        orgName,
                        orgID,
                        primaryContactFirstName + " " + primaryContactLastName,
                        String.join(", ", userDataList),
                        String.join(", ", computerDataList),
                        createdAtStr,
                        updatedAtStr
                };
            } else {
                System.out.println("No Task found with ID: " + taskID);
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }

        return taskData;
    }

    public static void deleteTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("----------------------------------------------------------");
        System.out.print("Enter the Task ID: ");
        int taskID = Integer.parseInt(scanner.nextLine());

        String query = "DELETE FROM patching_calender_tasks where taskID=" + taskID;
        System.out.println("INFO: Deleting Computer - " + taskID);

        try (Connection connection = connectToDatabase()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("INFO: Task deleted successfully.");
                } else {
                    System.err.println("ERROR: Task not deleted.");
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }
    }

    public static void updateTask(){
        Scanner scanner = new Scanner(System.in);

        String[] currentTaskData = getOneTask();

        System.out.println("----------------------------------------------------------");
        System.out.println("Current Task name: " + currentTaskData[1]);
        System.out.print("Enter the new task name (leave blank to keep current): ");
        String newTaskName = scanner.nextLine().trim();

        System.out.println("Current Organization ID: " + currentTaskData[3]);
        Organization.getAllOrganizations();
        System.out.print("Enter the new Organization name (leave blank to keep current): ");
        String organizationID = scanner.nextLine().trim();

        if (organizationID.isEmpty()) {
            organizationID = currentTaskData[3];
        }

        System.out.println("Current Primary Contact: " + currentTaskData[3]);
        Users.getAllUsersByOrg(Integer.parseInt(organizationID));
        System.out.print("Enter the new Primary Contact ID name (leave blank to keep current): ");
        String primaryContactID = scanner.nextLine().trim();

        System.out.println("Current Other Contacts: " + currentTaskData[3]);
        System.out.print("Enter Other Contacts: (leave blank to keep current): ");
        String otherContactIDs = scanner.nextLine().trim();

        System.out.println("Current computer IDs: " + currentTaskData[3]);
        Computers.getAllComputersByOrg(Integer.parseInt(organizationID));
        System.out.print("Enter the new Computer IDs (leave blank to keep current): ");
        String computerIDs = scanner.nextLine().trim();


        if (newTaskName.isEmpty()) {
            newTaskName = currentTaskData[1];
        }

        String query = "UPDATE patching_calender_tasks SET taskName = ?, organizationID = ?, primaryContactID = ?, otherContactIDs = ?, computerIDs = ? WHERE taskID = ?";
        System.out.println("INFO: Updating Task - ID: " + currentTaskData[0]);

        try (Connection connection = connectToDatabase()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newTaskName);
                preparedStatement.setInt(2, Integer.parseInt(organizationID));
                preparedStatement.setInt(3, Integer.parseInt(primaryContactID));
                preparedStatement.setString(4, otherContactIDs);
                preparedStatement.setString(5, computerIDs);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("INFO: Task updated successfully.");
                } else {
                    System.err.println("ERROR: Task not found or not updated.");
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }
    }
}
