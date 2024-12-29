package local.smc.scheduled.patching;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

import static local.smc.common.Database.connectToDatabase;

public class Computers {
    public static void getAllComputers() {

        String query = """
                SELECT
                    pcc.compID,
                    pcc.compName,
                    pcc.compOrganizationID,
                    org.orgName
                FROM
                    patching_calender_computers pcc
                JOIN
                    patching_calender_organizations org
                ON
                    pcc.compOrganizationID = org.orgID;
                """;

        try (Connection connection = connectToDatabase();
             Statement statement = connection != null ? connection.createStatement() : null;
             ResultSet resultSet = statement != null ? statement.executeQuery(query) : null) {

            if (resultSet != null) {

                System.out.println("+-----------------+--------------------------------+-----------------------------+");
                System.out.printf("| %-15s | %-30s | %-27s |\n", "Computer ID", "Computer Name", "Organization Name");
                System.out.println("+-----------------+--------------------------------+-----------------------------+");

                while (resultSet.next()) {
                    int compID = resultSet.getInt("pcc.compID");
                    String compName = resultSet.getString("pcc.compName");
                    String orgName = resultSet.getString("org.orgName");

                    System.out.printf("| %-15d | %-30s | %-27s |\n", compID, compName, orgName);
                    System.out.println("+-----------------+--------------------------------+-----------------------------+");
                }
            } else {
                System.out.println("No computers found.");
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }

    }

    public static String[] getOneComputer() {

        String query = """
                SELECT
                    pcc.compID,
                    pcc.compName,
                    pcc.compOrganizationID,
                    org.orgName
                FROM
                    patching_calender_computers pcc
                JOIN
                    patching_calender_organizations org
                ON
                    pcc.compOrganizationID = org.orgID
                WHERE
                    pcc.compID = ?""";

        Scanner scanner = new Scanner(System.in);
        int compID;
        String[] computerData = null;

        System.out.println("--------------------------------------------------------------------------");

        while (true){
            try {
                System.out.print("Enter the Computer ID: ");
                compID = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e){
                System.out.println("ERROR: Computer ID must be an Integer.");
            } catch (java.util.NoSuchElementException e) {
                System.out.println("INFO: Exiting program.");
            }
        }

        try (Connection connection = connectToDatabase()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, compID);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String compName = resultSet.getString("pcc.compName");
                        String compOrganizationID = resultSet.getString("pcc.compOrganizationID");
                        String orgName = resultSet.getString("org.orgName");

                        computerData = new String[]{String.valueOf(compID), compName, compOrganizationID, orgName};
                    } else {
                        System.out.println("No Computer found with ID: " + compID);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }

        if (computerData != null) {
            System.out.println("+-----------------+--------------------------------+------------------------------+");
            System.out.printf("| %-15s | %-30s | %-28s |\n", "Computer ID", "Computer Name", "Organization Name");
            System.out.println("+-----------------+--------------------------------+------------------------------+");
            System.out.printf("| %-15d | %-30s | %-28s |\n",
                    Integer.parseInt(computerData[0]), computerData[1], computerData[3]);
            System.out.println("+-----------------+--------------------------------+------------------------------+");
        } else {
            System.out.println("-----------------------------------------------------------------------------------");
            System.out.println("WARNING: No Computer is found.");
            System.out.println("-----------------------------------------------------------------------------------");
        }

        return computerData;
    }

    public static String[] getOneComputer(int compID) {

        String query = """
                SELECT
                    compID,
                    compName,
                    compOrganizationID
                FROM
                    patching_calender_computers
                WHERE
                    compID = ?
                """;

        String[] computerData = null;

        try (Connection connection = connectToDatabase()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, compID);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String compName = resultSet.getString("compName");
                        int compOrganizationID = resultSet.getInt("compOrganizationID");

                        computerData = new String[]{String.valueOf(compID), compName, String.valueOf(compOrganizationID)};
                    } else {
                        System.out.println("No Computer found with ID: " + compID);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }
        return computerData;
    }

    public static void addComputer() {

        String query = """
                INSERT
                INTO
                    patching_calender_computers
                    (compName, compOrganizationID)
                VALUES (?, ?)
                """;

        int compOrganizationID;

        Scanner scanner = new Scanner(System.in);
        System.out.println("--------------------------------------------------------------------------");
        String compName;
        while (true){
            System.out.print("Enter the Computer name: ");
            compName = scanner.nextLine();
            if (compName.isEmpty()){
                System.out.println("ERROR: Computer name cannot be empty");
            }else {
                break;
            }
        }

        List<Integer> compOrganizationIDsArray = Organization.getAllOrganizations();

        while (true) {
            System.out.print("Enter the Organization ID: ");
            try {
                compOrganizationID = Integer.parseInt(scanner.nextLine());
                int finalCompOrganizationID = compOrganizationID;
                if (compOrganizationIDsArray.stream().noneMatch(id -> id == finalCompOrganizationID)) {
                    System.out.println("ERROR: Organization ID not found. Please enter an ID from the above table.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Organization ID should be an Integer.");
            } catch (java.util.NoSuchElementException e) {
                System.out.println("INFO: Exiting program.");
            }
        }

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

        String query = """
                DELETE
                FROM
                    patching_calender_computers
                WHERE
                    compID = ?
                """;

        Scanner scanner = new Scanner(System.in);
        getAllComputers();

        int compID;

        while (true){
            System.out.print("Enter the Computer ID: ");
            try {
                compID = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Computer ID should be an Integer");
            } catch (java.util.NoSuchElementException e) {
                System.out.println("INFO: Exiting program.");
            }
        }

        System.out.println("INFO: Deleting Computer - " + compID);

        try (Connection connection = connectToDatabase()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, compID);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("INFO: Computer deleted successfully.");
                } else {
                    System.err.println("ERROR: No computer found with ID: " + compID);
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }
    }

    public static void updateComputer() {
        String query = """
                UPDATE
                    patching_calender_computers
                SET
                    compName = ?,
                    compOrganizationID = ?
                WHERE
                    compID = ?
                """;

        getAllComputers();
        Scanner scanner = new Scanner(System.in);

        String[] currentComputerData = getOneComputer();
        if (currentComputerData==null){
            System.out.println("WARNING: No Computer was found.");
            return;
        }

        System.out.println("Current Computer name: " + currentComputerData[1]);
        System.out.print("Enter the new Computer name (leave blank to keep current): ");
        String newCompName = scanner.nextLine().trim();

        System.out.println("Current Organization Name: " + currentComputerData[3]);
        List<Integer> compOrganizationIDsArray =  Organization.getAllOrganizations();
        String compOrganizationID;
        while (true){
            System.out.print("Enter the new Organization ID (leave blank to keep current): ");
            compOrganizationID = scanner.nextLine().trim();
            try {
                Integer.parseInt(compOrganizationID);
                int finalCompOrganizationID = Integer.parseInt(compOrganizationID);
                if (compOrganizationIDsArray.stream().noneMatch(id -> id == finalCompOrganizationID)) {
                    System.out.println("ERROR: Organization ID not found. Please enter an ID from the above table.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Organization ID should be an Integer.");
            } catch (java.util.NoSuchElementException e) {
                System.out.println("INFO: Exiting program.");
            }
        }

        if (newCompName.isEmpty()) {
            newCompName = currentComputerData[1];
        }
        if (compOrganizationID.isEmpty()) {
            compOrganizationID = currentComputerData[2];
        }


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
        String query = """
                SELECT
                    compID,
                    compName
                FROM
                    patching_calender_computers
                WHERE
                    compOrganizationID = ?
                """;

        try (Connection connection = connectToDatabase()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, compOrganizationID);  // Set the parameter for the query

                try (ResultSet resultSet = preparedStatement.executeQuery()) {

                    if (resultSet.next()) {
                        System.out.println("+-----------------+--------------------------------+");
                        System.out.println("| Computer ID     | Computer Name                  |");
                        System.out.println("+-----------------+--------------------------------+");

                        do {
                            int compID = resultSet.getInt("compID");
                            String compName = resultSet.getString("compName");

                            System.out.printf("| %-15d | %-30s |\n", compID, compName);
                            System.out.println("+-----------------+--------------------------------+");
                        } while (resultSet.next());  // Continue to the next row

                    } else {
                        System.out.println("No computers found for Organization ID: " + compOrganizationID);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }
    }
}
