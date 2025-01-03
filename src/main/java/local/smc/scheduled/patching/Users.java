package local.smc.scheduled.patching;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static local.smc.common.Database.connectToDatabase;

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
        Scanner scanner = new Scanner(System.in);
        System.out.println("----------------------------------------------------------");
        System.out.print("Enter the User ID: ");
        String inputUserID1 = scanner.nextLine().trim();

        String query = "SELECT userID, FirstName, LastName, email, OrganizationID FROM patching_calender_users WHERE userID = " + Integer.parseInt(inputUserID1);
        String[] userData = null;

        try (Connection connection = connectToDatabase();
             Statement statement = connection != null ? connection.createStatement() : null;
             ResultSet resultSet = statement != null ? statement.executeQuery(query) : null) {

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
                System.out.println("No user found with ID: " + Integer.parseInt(inputUserID1));
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
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

        Scanner scanner = new Scanner(System.in);
        System.out.println("----------------------------------------------------------");
        System.out.print("Enter the First Name: ");
        String FirstName =  scanner.nextLine();
        System.out.print("Enter the Last Name: ");
        String LastName =  scanner.nextLine();
        System.out.print("Enter the Email: ");
        String email =  scanner.nextLine();
        System.out.print("Enter the Organization ID: ");
        int OrganizationID = Integer.parseInt(scanner.nextLine());

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
        Scanner scanner = new Scanner(System.in);
        System.out.println("----------------------------------------------------------");
        System.out.print("Enter the User ID: ");
        int userID = Integer.parseInt(scanner.nextLine());

        String query = "DELETE FROM patching_calender_users where userID=" + userID;
        System.out.println("INFO: Deleting user - " + userID);

        try (Connection connection = connectToDatabase()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("INFO: User deleted successfully.");
                } else {
                    System.err.println("ERROR: User not deleted.");
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

        String[] currentUserData = getOneUser();

        System.out.println("+------------+---------------------+---------------------+----------------------------------+------------+");
        System.out.println("| User ID    | First Name          | Last Name           | Email                            | OrgID      |");
        System.out.println("+------------+---------------------+---------------------+----------------------------------+------------+");

        System.out.printf("| %-10d | %-19s | %-19s | %-32s | %-10d |\n",
                Integer.parseInt(currentUserData[0]), currentUserData[1], currentUserData[2], currentUserData[3], Integer.parseInt(currentUserData[4]));
        System.out.println("+------------+---------------------+---------------------+----------------------------------+------------+");

        System.out.println("INFO: Leave fields blank to keep current data.");
        System.out.println("----------------------------------------------------------");

        System.out.print("Enter the First Name: ");
        String FirstName = scanner.nextLine().trim();

        System.out.print("Enter the Last Name: ");
        String LastName = scanner.nextLine().trim();

        System.out.print("Enter the Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter the Organization ID: ");
        Integer OrganizationID = null;
        String orgInput = scanner.nextLine().trim();
        if (!orgInput.isEmpty()) {
            try {
                OrganizationID = Integer.parseInt(orgInput);
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Invalid Organization ID format. Update aborted.");
                return;
            }
        }

        try (Connection connection = connectToDatabase()) {
            assert connection != null;

            String newFirstName = FirstName.isEmpty() ? currentUserData[1] : FirstName;
            String newLastName = LastName.isEmpty() ? currentUserData[2] : LastName;
            String newEmail = email.isEmpty() ? currentUserData[3] : email;
            int newOrganizationID = OrganizationID == null ? Integer.parseInt(currentUserData[4]) : OrganizationID;

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newFirstName);
                preparedStatement.setString(2, newLastName);
                preparedStatement.setString(3, newEmail);
                preparedStatement.setInt(4, newOrganizationID);
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

    public static List<Integer> getAllUsersByOrg(int OrganizationID) {
        String query = "SELECT userID, FirstName, LastName, email FROM patching_calender_users where OrganizationID=" + OrganizationID;

        List<Integer> orgUsersIDs = new ArrayList<>();

        try (Connection connection = connectToDatabase();
             Statement statement = connection != null ? connection.createStatement() : null;
             ResultSet resultSet = statement != null ? statement.executeQuery(query) : null) {

            if (resultSet != null) {
                System.out.println("+------------+---------------------+---------------------+----------------------------------+");
                System.out.println("| User ID    | First Name          | Last Name           | Email                            |");
                System.out.println("+------------+---------------------+---------------------+----------------------------------+");

                while (resultSet.next()) {
                    int userID = resultSet.getInt("userID");
                    orgUsersIDs.add(userID);
                    String FirstName = resultSet.getString("FirstName");
                    String LastName = resultSet.getString("LastName");
                    String email = resultSet.getString("email");

                    System.out.printf("| %-10d | %-19s | %-19s | %-32s |\n",
                            userID, FirstName, LastName, email);
                    System.out.println("+------------+---------------------+---------------------+----------------------------------+");
                }

            } else {
                System.out.println("No users found.");
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }
        return orgUsersIDs;
    }
}
