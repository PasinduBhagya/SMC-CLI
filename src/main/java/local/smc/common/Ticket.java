package local.smc.common;

import org.json.JSONObject;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.net.HttpURLConnection;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.net.URL;

public class Ticket {
        private static String encodeCredentials(String username, String apiToken) {
            String auth = username + ":" + apiToken;
            return java.util.Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        }
        public static void getJIRASettings(String summary, String description){
            Properties properties = new Properties();

            try (FileInputStream settings = new FileInputStream("./config/settings.txt")) {
                properties.load(settings);

                String jiraUrl = properties.getProperty("JIRA_URL");
                String jiraApiEndpoint = properties.getProperty("JIRA_API_ENDPOINT");
                String jiraUsername = properties.getProperty("JIRA_USERNAME");
                String jiraApiToken = properties.getProperty("JIRA_API_TOKEN");


                JSONObject issueJson = new JSONObject();
                JSONObject fields = new JSONObject();

                fields.put("project", new JSONObject().put("key", "SCRUM"));
                fields.put("summary", summary);
                fields.put("description", description);
                fields.put("issuetype", new JSONObject().put("name", "Task"));

                issueJson.put("fields", fields);

                URL url = new URL(jiraUrl + jiraApiEndpoint);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Basic " + encodeCredentials(jiraUsername, jiraApiToken));
                connection.setDoOutput(true);

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = issueJson.toString().getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_CREATED) {
                    System.out.println("Issue created successfully.");
                } else {
                    System.out.println("Failed to create issue. Response Code: " + responseCode);
                }

            } catch (IOException e) {
                System.out.println("ERROR: Failed to create the JIRA\n" + e);
            }

        }
}
