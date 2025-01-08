package local.smc.common.Settings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Jira {
    public static void getJIRASettings(){
        System.out.println("Loading JIRA Settings.");
        String helpFilePath = "./config/settings.txt";
        String helpLine;
        try (BufferedReader helpFileContent = new BufferedReader(new FileReader(helpFilePath))){
            int i = 1;
            System.out.println("+----+------------------------------+----------------------------------------------------+");
            System.out.printf("| %-2s | %-28s | %-50s |\n", "ID", "Option", "Value");
            System.out.println("+----+------------------------------+----------------------------------------------------+");

            while ((helpLine = helpFileContent.readLine()) != null) {
                String[] parts = helpLine.split("=", 2); // Limit split to 2 parts to avoid issues with '=' in values
                String optionValue = parts[0].trim();
                String optionDescription = parts.length > 1 ? parts[1].trim() : "";
                if (optionValue.equals("JIRA_API_TOKEN")){
                    optionDescription = "##################################################";
                }

                System.out.printf("| %-2d | %-28s | %-50s |\n", i++, optionValue, optionDescription);
                System.out.println("+----+------------------------------+----------------------------------------------------+");
            }



        } catch (IOException e){
            System.out.println("Error: Unable to open the help.txt file.");
        }
    }
}
