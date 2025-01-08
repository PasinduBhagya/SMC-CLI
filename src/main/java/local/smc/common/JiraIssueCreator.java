package local.smc.common;

import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JiraIssueCreator {

    private static final String JIRA_URL = "https://pasindub5gncomau.atlassian.net/";
    private static final String API_ENDPOINT = "/rest/api/2/issue";
    private static final String USERNAME = "pasindub5gncomau@gmail.com";
    private static final String API_TOKEN = "ATATT3xFfGF0j3OPaCzdn2QsLRYvPJ9WJM18mGGZY4hMwfsNbN9Tw5ijojNEefEWT7QyxkFb1it_ez5k0uppq3cunaykUAceZcGMl10OevfU4dqVxK-bEBWkSqA5gQuLzTvNjM8xSSLm4NKBJIDkwN9FHeD7IXVQfHjy5gSymK5tgGqhuKibbLY=DBEE0C43";

    public static void main(String[] args) {
        try {
            createJiraIssue("Sample Summary", "Sample Description");
        } catch (Exception e) {
            System.err.println("Error");
        }
    }

    public static void createJiraIssue(String summary, String description) throws IOException {
        // Prepare the JSON payload for creating the issue
        JSONObject issueJson = new JSONObject();
        JSONObject fields = new JSONObject();

        fields.put("project", new JSONObject().put("key", "SCRUM")); // Project key
        fields.put("summary", summary);
        fields.put("description", description);
        fields.put("issuetype", new JSONObject().put("name", "Task")); // Issue type (e.g., Bug, Task)

        issueJson.put("fields", fields);

        // Create URL and connection
        URL url = new URL(JIRA_URL + API_ENDPOINT);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Basic " + encodeCredentials(USERNAME, API_TOKEN));
        connection.setDoOutput(true);

        // Write JSON data to the output stream
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = issueJson.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Get the response code
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            System.out.println("Issue created successfully.");
        } else {
            System.out.println("Failed to create issue. Response Code: " + responseCode);
        }
    }

    private static String encodeCredentials(String username, String apiToken) {
        String auth = username + ":" + apiToken;
        return java.util.Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    }
}
