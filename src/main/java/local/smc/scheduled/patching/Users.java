package local.smc.scheduled.patching;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static local.smc.common.Database.connectToDatabase;
import static local.smc.common.verifications.*;

public class Users {
    public static void getAllUsers() {
        String query = "SELECT userID, FirstName, LastName, email, OrganizationID FROM patching_calender_users";

        try (Connection connection = connectToDatabase();
             Statement statement = connection != null ? connection.createStatement() : null;
             ResultSet resultSet = statement != null ? statement.executeQuery(query) : null) {

            if (resultSet != null) {
                System.out.println("+------------+---------------------+---------------------+----------------------------------+------------+");
                System.out.println("| User ID    | First Name          | Last Name           | Email                            | OrgID      |");
                System.out.println("+------------+---------------------+---------------------+----------------------------------+------------+");

                while (resultSet.next()) {
                    int userID = resultSet.getInt("userID");
                    String FirstName = resultSet.getString("FirstName");
                    String LastName = resultSet.getString("LastName");
                    String email = resultSet.getString("email");
                    int OrganizationID = resultSet.getInt("OrganizationID");

                    System.out.printf("| %-10d | %-19s | %-19s | %-32s | %-10d |\n",
                            userID, FirstName, LastName, email, OrganizationID);
                System.out.println("+------------+---------------------+---------------------+----------------------------------+------------+");
                }

            } else {
                System.out.println("No users found.");
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }
    }

    public static String[] getOneUser() {
        System.out.println("----------------------------------------------------------");
        String inputMessage = "Enter the User ID: ";
        int inputUserID1 = checkForIntegers(inputMessage);

        String query = "SELECT * FROM patching_calender_users WHERE userID = ?";
        String[] userData = null;

        try(Connection connection = connectToDatabase()){
            assert connection != null;
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
                preparedStatement.setInt(1, inputUserID1);
                    try(ResultSet resultSet = preparedStatement.executeQuery()){
                        if (resultSet != null && resultSet.next()) {
                            int userID = resultSet.getInt("userID");
                            String firstName = resultSet.getString("FirstName");
                            String lastName = resultSet.getString("LastName");
                            String email = resultSet.getString("email");
                            int organizationID = resultSet.getInt("OrganizationID");

                            userData = new String[]{
                                    String.valueOf(userID),
                                    firstName,
                                    lastName,
                                    email,
                                    String.valueOf(organizationID)
                            };
                        } else {
                            System.out.println("No user found with ID: " + inputUserID1);
                        }
                    }
            }
        }catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }
        if (userData != null){
            System.out.println("+------------+---------------------+---------------------+----------------------------------+------------+");
            System.out.println("| User ID    | First Name          | Last Name           | Email                            | OrgID      |");
            System.out.println("+------------+---------------------+---------------------+----------------------------------+------------+");

            System.out.printf("| %-10d | %-19s | %-19s | %-32s | %-10d |\n",
                Integer.parseInt(userData[0]), userData[1], userData[2], userData[3], Integer.parseInt(userData[4]));
            System.out.println("+------------+---------------------+---------------------+----------------------------------+------------+");
        }else{
           System.out.println("WARNING: No user found for the ID.");
        }
        return userData;
    }

    public static String[] getOneUser(String userID) {

        String query = "SELECT userID, FirstName, LastName, email, OrganizationID FROM patching_calender_users WHERE userID = " + Integer.parseInt(userID);
        String[] userData = null;

        try (Connection connection = connectToDatabase();
             Statement statement = connection != null ? connection.createStatement() : null;
             ResultSet resultSet = statement != null ? statement.executeQuery(query) : null) {

            if (resultSet != null && resultSet.next()) {
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                String email = resultSet.getString("email");
                int organizationID = resultSet.getInt("OrganizationID");

                userData = new String[]{
                        userID,
                        firstName,
                        lastName,
                        email,
                        String.valueOf(organizationID)
                };
            } else {
                System.out.println("No user found with ID: " + Integer.parseInt(userID));
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }

        return userData;
    }

    public static void addUser() {

        System.out.println("----------------------------------------------------------");
        String inputMessage1 = "Enter the First Name: ";
        String FirstName =  checkForEmptyString(inputMessage1);
        String inputMessage2 = "Enter the Last Name: ";
        String LastName =  checkForEmptyString(inputMessage2);
        String inputMessage3 = "Enter the Email: ";
        String email =  checkForEmptyString(inputMessage3);
        String inputMessage4 = "Enter the Organization ID: ";
        List<Integer> compOrganizationIDsArray = Organization.getAllOrganizations();
        int OrganizationID = checkForAllowedValues(inputMessage4, compOrganizationIDsArray);

        String query = "INSERT INTO patching_calender_users (FirstName, LastName, email, OrganizationID) VALUES (?, ?, ?, ?)";
        System.out.println("INFO: Adding user - " + FirstName + " " + LastName);

        try (Connection connection = connectToDatabase()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, FirstName);
                preparedStatement.setString(2, LastName);
                preparedStatement.setString(3, email);
                preparedStatement.setInt(4, OrganizationID);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("INFO: User added successfully.");
                } else {
                    System.err.println("ERROR: User not added.");
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }
    }

    public static void deleteUser() {
        System.out.println("----------------------------------------------------------");
        String inputMessage = "Enter the User ID: ";

        int userID = checkForIntegers(inputMessage);

        String query = "DELETE FROM patching_calender_users where userID=?";
        System.out.println("INFO: Deleting user - " + userID);

        try (Connection connection = connectToDatabase()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userID);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("INFO: User deleted successfully.");
                } else {
                    System.err.println("ERROR: User cannot be deleted.");
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }
    }

    public static void updateUser() {
        String query = "UPDATE patching_calender_users " +
                "SET FirstName = ?, LastName = ?, email = ?, OrganizationID = ? " +
                "WHERE userID = ?";
        Scanner scanner = new Scanner(System.in);
        getAllUsers();
        String[] currentUserData = getOneUser();

        System.out.println("INFO: Leave fields blank to keep current data.");
        System.out.println("----------------------------------------------------------");

        System.out.print("Enter the First Name: ");
        String FirstName = scanner.nextLine().trim();
        if (FirstName.isEmpty()){
            FirstName = currentUserData[1];
        }

        System.out.print("Enter the Last Name: ");
        String LastName = scanner.nextLine().trim();

        if (LastName.isEmpty()){
            LastName = currentUserData[2];
        }

        System.out.print("Enter the Email: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()){
            email = currentUserData[3];
        }

        String inputMessage = "Enter the Organization ID: ";
        List<Integer> orgIDs = Organization.getAllOrganizations();
        int OrganizationID = checkForUpdatedAllowedValues(inputMessage, orgIDs);
        if (OrganizationID == -1){
            OrganizationID = Integer.parseInt(currentUserData[4]);
        }


        try (Connection connection = connectToDatabase()) {
            assert connection != null;

            String newFirstName = FirstName.isEmpty() ? currentUserData[1] : FirstName;
            String newLastName = LastName.isEmpty() ? currentUserData[2] : LastName;
            String newEmail = email.isEmpty() ? currentUserData[3] : email;

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newFirstName);
                preparedStatement.setString(2, newLastName);
                preparedStatement.setString(3, newEmail);
                preparedStatement.setInt(4, OrganizationID);
                preparedStatement.setInt(5, Integer.parseInt(currentUserData[0]));

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("INFO: User updated successfully.");
                } else {
                    System.err.println("ERROR: User not found or not updated.");
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }
    }

    public static List<Integer> getAllUsersByOrg(int organizationID) {
        String query = "SELECT userID, FirstName, LastName, email FROM patching_calender_users WHERE OrganizationID = ?";

        List<Integer> orgUserIDs = new ArrayList<>();

        try (Connection connection = connectToDatabase()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, organizationID);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {

                    System.out.println("+------------+---------------------+---------------------+----------------------------------+");
                    System.out.println("| User ID    | First Name          | Last Name           | Email                            |");
                    System.out.println("+------------+---------------------+---------------------+----------------------------------+");

                    while (resultSet.next()) {
                        int userID = resultSet.getInt("userID");
                        orgUserIDs.add(userID);

                        String firstName = resultSet.getString("FirstName");
                        String lastName = resultSet.getString("LastName");
                        String email = resultSet.getString("email");

                        System.out.printf("| %-10d | %-19s | %-19s | %-32s |\n", userID, firstName, lastName, email);
                    }

                    System.out.println("+------------+---------------------+---------------------+----------------------------------+");

                    if (orgUserIDs.isEmpty()) {
                        System.out.println("No users found.");
                    }
                } catch (SQLException e) {
                    System.err.println("ERROR: Failed to execute query: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to connect to the database: " + e.getMessage());
        }
        return orgUserIDs;
    }

}

