package local.smc.scheduled.patching;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static local.smc.common.Database.connectToDatabase;
import static local.smc.common.verifications.checkForEmptyString;
import static local.smc.common.verifications.checkForIntegers;

public class Organization {

    public static List<Integer> getAllOrganizations() {
        String query = """
                SELECT
                    orgID,
                    orgName
                FROM
                    patching_calender_organizations
                """;

        List<Integer> organizationIDs = new ArrayList<>();

        try (Connection connection = connectToDatabase();
             Statement statement = connection != null ? connection.createStatement() : null;
             ResultSet resultSet = statement != null ? statement.executeQuery(query) : null) {

            if (resultSet != null) {
                System.out.println("+-----------------+--------------------------------------+");
                System.out.println("| Organization ID | Organization Name                    |");
                System.out.println("+-----------------+--------------------------------------+");

                while (resultSet.next()) {
                    int orgID = resultSet.getInt("orgID");
                    organizationIDs.add(orgID);
                    String orgName = resultSet.getString("orgName");

                    System.out.printf("| %-15d | %-36s |\n", orgID, orgName);
                    System.out.println("+-----------------+--------------------------------------+");
                }

            } else {
                System.out.println("No organizations found.");
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }
        return organizationIDs;
    }

    public static String[] getOneOrganization() {

        System.out.println("----------------------------------------------------------");
        int orgID;

        String inputMessage = "Enter the Computer ID: ";
        orgID = checkForIntegers(inputMessage);

        String query = "SELECT orgID, orgName FROM patching_calender_organizations WHERE orgID = ?";
        String[] organizationData = null;

        try (Connection connection = connectToDatabase()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, orgID);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String orgName = resultSet.getString("orgName");
                        organizationData = new String[] {String.valueOf(orgID), orgName};
                    } else {
                        System.out.println("No organization found with ID: " + orgID);
                    }
                }

            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }

        if (organizationData != null) {
            System.out.println("+-----------------+--------------------------------------+");
            System.out.println("| Organization ID | Organization Name                    |");
            System.out.println("+-----------------+--------------------------------------+");
            System.out.printf("| %-15d | %-36s |\n", Integer.parseInt(organizationData[0]), organizationData[1]);
            System.out.println("+-----------------+--------------------------------------+");
        } else {
            System.out.println("-----------------------------------------------------------------------------------");
            System.out.println("WARNING: No Organization is found.");
            System.out.println("-----------------------------------------------------------------------------------");
        }

        return organizationData;
    }


    public static void addOrganization() {

        System.out.println("----------------------------------------------------------");
        String inputMessage = "Enter the Organization name: ";
        String orgName = checkForEmptyString(inputMessage);

        String query = "INSERT INTO patching_calender_organizations (orgName) VALUES (?)";
        System.out.println("INFO: Adding organization - " + orgName);

        try (Connection connection = connectToDatabase()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, orgName);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("INFO: Organization added successfully.");
                } else {
                    System.err.println("ERROR: Organization not added.");
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }
    }

    public static void deleteOrganization() {
        getAllOrganizations();
        String inputMessage = "Enter the Organization ID: ";
        int orgID = checkForIntegers(inputMessage);

        String query = "DELETE FROM patching_calender_organizations WHERE orgID = ?";

        System.out.println("INFO: Deleting organization with ID - " + orgID);

        try (Connection connection = connectToDatabase()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, orgID);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("INFO: Organization deleted successfully.");
                } else {
                    System.err.println("ERROR: No organization found with the specified ID.");
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }
    }

    public static void updateOrganization() {
        getAllOrganizations();
        Scanner scanner = new Scanner(System.in);

        String[] currentOrgData = getOneOrganization();

        if (currentOrgData==null){
            System.out.println("WARNING: No Organization was found.");
            return;
        }

        System.out.println("----------------------------------------------------------");
        System.out.println("Current organization name: " + currentOrgData[1]);
        System.out.print("Enter the new organization name (leave blank to keep current): ");
        String newOrgName = scanner.nextLine().trim();

        if (newOrgName.isEmpty()) {
            newOrgName = currentOrgData[1];
        }

        String query = "UPDATE patching_calender_organizations SET orgName = ? WHERE orgID = ?";
        System.out.println("INFO: Updating organization - ID: " + currentOrgData[0]);

        try (Connection connection = connectToDatabase()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newOrgName);
                preparedStatement.setInt(2, Integer.parseInt(currentOrgData[0]));

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("INFO: Organization updated successfully.");
                } else {
                    System.err.println("ERROR: Organization not found or not updated.");
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to execute query: " + e.getMessage());
        }
    }
}
