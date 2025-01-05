package local.smc.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class Database {

    public static Connection connectToDatabase() {
        Properties properties = new Properties();
        try (FileInputStream databaseConfigs = new FileInputStream("config/database.txt")) {
            properties.load(databaseConfigs);

            String url = properties.getProperty("db.url");
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");

            return DriverManager.getConnection(url, username, password);

        } catch (IOException e) {
            System.err.println("ERROR: Failed to read the configuration file: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("ERROR: Database connection failed: " + e.getMessage());
        }
        return null;
    }

}
