package local.smc.scheduled.patching;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static local.smc.common.Database.connectToDatabase;
import static local.smc.common.verifications.*;

public class Tasks {
    public static void addTask(){

        String inputMessage1 = "Enter the Task name: ";
        String taskName = checkForEmptyString(inputMessage1);
        List<Integer> compOrganizationIDsArray = Organization.getAllOrganizations();

        String inputMessage2 = "Enter the Organization ID: ";
        int organizationID = checkForAllowedValues(inputMessage2, compOrganizationIDsArray);

        List<Integer> orgUsersIDs = Users.getAllUsersByOrg(organizationID);
        String inputMessage3 = "Enter the primary contact user ID: ";
        int primaryContactID = checkForAllowedValues(inputMessage3, orgUsersIDs);

        String inputMessage4 = "Enter the other contact user IDs [Add multiple users separated by commas]: ";
        String otherContactIDs = checkForFormating(inputMessage4, orgUsersIDs);

        List<Integer> orgComputerIDs = Computers.getAllComputersByOrg(organizationID);
        String inputMessage5 = "Enter the Computer IDs [Add multiple computers separated by commas]: ";
        String computerIDs = checkForFormating(inputMessage5, orgComputerIDs);


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
        String query = """
                SELECT
                    taskID,
                    taskName
                FROM
                    patching_calender_tasks
                """;

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

        System.out.println("----------------------------------------------------------------------------------");
        String inputMessage = "Enter the Task ID: ";
        int taskID = checkForIntegers(inputMessage);

        String query = """
        SELECT
            tasks.taskName,
            organization.orgName,
            tasks.organizationID,
            tasks.primaryContactID,
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
            tasks.taskID = ?""";

        String[] taskData = null;

        try (Connection connection = connectToDatabase()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, taskID);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String taskName = resultSet.getString("taskName");
                        String orgName = resultSet.getString("orgName");
                        String orgID = resultSet.getString("organizationID");
                        String primaryContactID = resultSet.getString("primaryContactID");
                        String primaryContactFirstName = resultSet.getString("primaryContactFirstName");
                        String primaryContactLastName = resultSet.getString("primaryContactLastName");
                        String otherContactIDs = resultSet.getString("otherContactIDs");
                        String computerIDs = resultSet.getString("computerIDs");
                        Timestamp createdAt = resultSet.getTimestamp("createdAt");
                        Timestamp updatedAt = resultSet.getTimestamp("updatedAt");

                        List<String> userDataList = new ArrayList<>();
                        if (otherContactIDs != null) {
                            for (String userID : otherContactIDs.split(",")) {
                                String[] userData = Users.getOneUser(userID.trim());
                                if (userData != null && userData.length >= 3) {
                                    userDataList.add(userData[1] + " " + userData[2]);
                                }
                            }
                        }

                        List<String> computerDataList = new ArrayList<>();
                        if (computerIDs != null) {
                            for (String compID : computerIDs.split(",")) {
                                String[] computerData = Computers.getOneComputer(Integer.parseInt(compID.trim()));
                                if (computerData != null && computerData.length > 2) {
                                    computerDataList.add(computerData[1]);
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
                                primaryContactID,
                                primaryContactFirstName + " " + primaryContactLastName,
                                String.join(", ", userDataList),
                                otherContactIDs,
                                computerIDs,
                                String.join(", ", computerDataList),
                                createdAtStr,
                                updatedAtStr
                        };
                    } else {
                        System.out.println("No Task found with ID: " + taskID);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }
        if (taskData != null){
            System.out.println("+----------------------+---------------------------------------------------------+");
            System.out.printf("| %-20s | %-55s |%n", "Field", "Value");
            System.out.println("+----------------------+---------------------------------------------------------+");
            System.out.printf("| %-20s | %-55s |%n", "Task ID", taskData[0]);
            System.out.println("+----------------------+---------------------------------------------------------+");
            System.out.printf("| %-20s | %-55s |%n", "Task Name", taskData[1]);
            System.out.println("+----------------------+---------------------------------------------------------+");
            System.out.printf("| %-20s | %-55s |%n", "Organization Name", taskData[2]);
            System.out.println("+----------------------+---------------------------------------------------------+");
            System.out.printf("| %-20s | %-55s |%n", "Primary Contact", taskData[5]);
            System.out.println("+----------------------+---------------------------------------------------------+");
            System.out.printf("| %-20s | %-55s |%n", "Secondary Contacts", taskData[6]);
            System.out.println("+----------------------+---------------------------------------------------------+");
            System.out.printf("| %-20s | %-55s |%n", "Computer Names", taskData[9]);
            System.out.println("+----------------------+---------------------------------------------------------+");
            System.out.printf("| %-20s | %-55s |%n", "Created Date", taskData[10]);
            System.out.println("+----------------------+---------------------------------------------------------+");
            System.out.printf("| %-20s | %-55s |%n", "Updated Date", taskData[11]);
            System.out.println("+----------------------+---------------------------------------------------------+");
        }else {
            System.out.println("-----------------------------------------------------------------------------------");
            System.out.println("WARNING: No Task is found.");
            System.out.println("-----------------------------------------------------------------------------------");
        }
        return taskData;
    }

    public static void deleteTask() {
        getAllTasks();
        String inputMessage = "Enter the Task ID: ";
        int taskID = checkForIntegers(inputMessage);

        String query = "DELETE FROM patching_calender_tasks where taskID=?";
        System.out.println("INFO: Deleting Computer - " + taskID);

        try (Connection connection = connectToDatabase()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, taskID);
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


        getAllTasks();
        String[] currentTaskData = getOneTask();

        int primaryContactID = Integer.parseInt(currentTaskData[4]);
        String otherContactIDs = currentTaskData[7];
        String computerIDs = currentTaskData[8];

        System.out.println("----------------------------------------------------------");
        System.out.println("Current Task name: " + currentTaskData[1]);
        System.out.print("Enter the new task name (leave blank to keep current): ");
        String newTaskName = scanner.nextLine().trim();

        if (newTaskName.isEmpty()) {
            newTaskName = currentTaskData[1];
        }

        System.out.println("Current Organization Name: " + currentTaskData[2]);

        List<Integer> orgIDs = Organization.getAllOrganizations();

        String inputMessage = "Enter the new Organization name (leave blank to keep current): ";
        int organizationID = (checkForUpdatedAllowedValues(inputMessage, orgIDs));

        if (organizationID == -1){
            List<Integer> orgUsersIDs = Users.getAllUsersByOrg(Integer.parseInt(currentTaskData[3]));
            organizationID = Integer.parseInt(currentTaskData[3]);
            System.out.println("Current Primary Contact: " + currentTaskData[5]);
            String inputMessage3 = "Enter the new Primary Contact ID name (leave blank to keep current): ";
            primaryContactID = checkForUpdatedAllowedValues(inputMessage3, orgUsersIDs);
            if (primaryContactID == -1){
                primaryContactID = Integer.parseInt(currentTaskData[4]);
            }

            System.out.println("Current Other Contacts: " + currentTaskData[6]);
            String inputMessage1 = "Enter Other Contacts: (leave blank to keep current): ";
            otherContactIDs = checkForFormating(inputMessage1, orgUsersIDs, currentTaskData[7]);
            if (otherContactIDs == null || otherContactIDs.isEmpty()) {
                otherContactIDs = currentTaskData[7];
            }

            System.out.println("Current computer IDs: " + currentTaskData[7]);
            List<Integer> orgComputerIDs = Computers.getAllComputersByOrg(organizationID);
            String inputMessage2 = "Enter the Computer IDs [Add multiple computers separated by commas]: ";
            computerIDs = checkForFormating(inputMessage2, orgComputerIDs, currentTaskData[8]);

        }else{
            if (organizationID != Integer.parseInt(currentTaskData[3])){
                System.out.println("INFO: Since the organization has been changed, please update the Below Information\n");
                List<Integer> orgUsersIDs = Users.getAllUsersByOrg(organizationID);
                String inputMessage3 = "Enter the primary contact user ID: ";
                primaryContactID = checkForAllowedValues(inputMessage3, orgUsersIDs);

                String inputMessage4 = "Enter the other contact user IDs [Add multiple users separated by commas]: ";
                otherContactIDs = checkForFormating(inputMessage4, orgUsersIDs);

                List<Integer> orgComputerIDs = Computers.getAllComputersByOrg(organizationID);
                String inputMessage5 = "Enter the Computer IDs [Add multiple computers separated by commas]: ";
                computerIDs = checkForFormating(inputMessage5, orgComputerIDs);
            }
        }

        String query = "UPDATE patching_calender_tasks SET taskName = ?, organizationID = ?, primaryContactID = ?, otherContactIDs = ?, computerIDs = ? WHERE taskID = ?";
        try (Connection connection = connectToDatabase()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newTaskName);
                preparedStatement.setInt(2, Integer.parseInt(String.valueOf(organizationID)));
                preparedStatement.setInt(3, primaryContactID);
                preparedStatement.setString(4, otherContactIDs);
                preparedStatement.setString(5, computerIDs);
                preparedStatement.setString(6, currentTaskData[0]);

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
