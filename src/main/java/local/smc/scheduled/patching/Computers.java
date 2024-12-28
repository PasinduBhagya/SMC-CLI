package local.smc.scheduled.patching;

import java.sql.*;
import java.util.Scanner;

import static local.smc.common.Database.connectToDatabase;

public class Computers {
    public static void getAllComputers() {
        String query = "SELECT compID, compName, compOrganizationID FROM patching_calender_computers";

        try (Connection connection = connectToDatabase();
             Statement statement = connection != null ? connection.createStatement() : null;
             ResultSet resultSet = statement != null ? statement.executeQuery(query) : null) {

            if (resultSet != null) {

                System.out.println("+-----------------+--------------------------------+---------------------+");
                System.out.println("| Computer ID     | Computer Name                  | Organization ID     |");
                System.out.println("+-----------------+--------------------------------+---------------------+");

                while (resultSet.next()) {
                    int compID = resultSet.getInt("compID");
                    String compName = resultSet.getString("compName");
                    int compOrganizationID = resultSet.getInt("compOrganizationID");

                    System.out.printf("| %-15d | %-30s | %-19d |\n", compID, compName, compOrganizationID);
                    System.out.println("+-----------------+--------------------------------+---------------------+");
                }



            } else {
                System.out.println("No computers found.");
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }
    }

    public static String[] getOneComputer() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("----------------------------------------------------------");
        System.out.print("Enter the Computer ID: ");

        int compID = Integer.parseInt(scanner.nextLine());
        String query = "SELECT compID, compName, compOrganizationID FROM patching_calender_computers WHERE compID = " + compID;
        String[] computerData = null;

        try (Connection connection = connectToDatabase();
             Statement statement = connection != null ? connection.createStatement() : null;
             ResultSet resultSet = statement != null ? statement.executeQuery(query) : null) {

            if (resultSet != null && resultSet.next()) {
                String compName = resultSet.getString("compName");
                int compOrganizationID = Integer.parseInt(resultSet.getString("compOrganizationID"));

                computerData = new String[] {String.valueOf(compID), compName, String.valueOf(compOrganizationID)};

            } else {
                System.out.println("No Computer found with ID: " + compID);
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }

        return computerData;
    }

    public static String[] getOneComputer(int compID) {
        Scanner scanner = new Scanner(System.in);
        String query = "SELECT compID, compName, compOrganizationID FROM patching_calender_computers WHERE compID = " + compID;
        String[] computerData = null;

        try (Connection connection = connectToDatabase();
             Statement statement = connection != null ? connection.createStatement() : null;
             ResultSet resultSet = statement != null ? statement.executeQuery(query) : null) {

            if (resultSet != null && resultSet.next()) {
                String compName = resultSet.getString("compName");
                int compOrganizationID = Integer.parseInt(resultSet.getString("compOrganizationID"));

                computerData = new String[] {String.valueOf(compID), compName, String.valueOf(compOrganizationID)};

            } else {
                System.out.println("No Computer found with ID: " + compID);
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }

        return computerData;
    }

    public static void addComputer() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("----------------------------------------------------------");
        System.out.print("Enter the Computer name: ");
        String compName = scanner.nextLine();
        System.out.print("Enter the Organization ID: ");
        int compOrganizationID = Integer.parseInt(scanner.nextLine());

        String query = "INSERT INTO patching_calender_computers (compName, compOrganizationID) VALUES (?, ?)";
        System.out.println("INFO: Adding organization - " + compName);

        try (Connection connection = connectToDatabase()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, compName);
                preparedStatement.setString(2, String.valueOf(compOrganizationID));

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("INFO: Computer added successfully.");
                } else {
                    System.err.println("ERROR: Computer not added.");
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }
    }

    public static void deleteComputer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("----------------------------------------------------------");
        System.out.print("Enter the Computer ID: ");
        int compID = Integer.parseInt(scanner.nextLine());

        String query = "DELETE FROM patching_calender_computers where compID=" + compID;
        System.out.println("INFO: Deleting Computer - " + compID);

        try (Connection connection = connectToDatabase()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("INFO: Organization deleted successfully.");
                } else {
                    System.err.println("ERROR: Organization not deleted.");
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }
    }

    public static void updateComputer() {
        Scanner scanner = new Scanner(System.in);

        String[] currentComputerData = getOneComputer();

        System.out.println("----------------------------------------------------------");
        System.out.println("Current Computer name: " + currentComputerData[1]);
        System.out.print("Enter the new Computer name (leave blank to keep current): ");
        String newCompName = scanner.nextLine().trim();

        System.out.println("Current Organization ID: " + currentComputerData[2]);
        System.out.print("Enter the new Organization ID (leave blank to keep current): ");
        String compOrganizationID = scanner.nextLine().trim();

        if (newCompName.isEmpty()) {
            newCompName = currentComputerData[1];
        }
        if (compOrganizationID.isEmpty()) {
            compOrganizationID = currentComputerData[2];
        }

        String query = "UPDATE patching_calender_computers SET compName = ?, compOrganizationID = ?  WHERE compID = ?";
        System.out.println("INFO: Updating Computer - ID: " + currentComputerData[0]);

        try (Connection connection = connectToDatabase()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newCompName);
                preparedStatement.setInt(2, Integer.parseInt(compOrganizationID));
                preparedStatement.setInt(3, Integer.parseInt(currentComputerData[0]));

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("INFO: Computer updated successfully.");
                } else {
                    System.err.println("ERROR: Computer not found or not updated.");
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }
    }

    public static void getAllComputersByOrg(int compOrganizationID) {
        String query = "SELECT compID, compName FROM patching_calender_computers where compOrganizationID=" + compOrganizationID;

        try (Connection connection = connectToDatabase();
             Statement statement = connection != null ? connection.createStatement() : null;
             ResultSet resultSet = statement != null ? statement.executeQuery(query) : null) {

            if (resultSet != null) {

                System.out.println("+-----------------+--------------------------------+");
                System.out.println("| Computer ID     | Computer Name                  |");
                System.out.println("+-----------------+--------------------------------+");

                while (resultSet.next()) {
                    int compID = resultSet.getInt("compID");
                    String compName = resultSet.getString("compName");

                    System.out.printf("| %-15d | %-30s |\n", compID, compName);
                    System.out.println("+-----------------+--------------------------------+");
                }

            } else {
                System.out.println("No computers found.");
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }
    }
}
