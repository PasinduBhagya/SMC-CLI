package local.smc.common.Settings;

import local.smc.common.Ticket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class SettingsMainMenu {
    public static void loadSettings(){
        System.out.println("INFO: Printing Settings");
        Scanner scanner = new Scanner(System.in);
        String settingsFilePath = "./config/help-content/settings-help.txt";
        String settingLine;
        try (BufferedReader helpFileContent = new BufferedReader(new FileReader(settingsFilePath))){
            int i=0;
            System.out.println("+-----+-------------------------------------+");
            System.out.printf("| %-3s | %-35s |\n", "No", "Setting");
            System.out.println("+-----+-------------------------------------+");

            while ((settingLine = helpFileContent.readLine()) != null) {
                System.out.printf("| %-3d | %-35s |\n", i++, settingLine.trim());
                System.out.println("+-----+-------------------------------------+");
            }


        } catch (IOException e){
            System.out.println("Error: Unable to open the settings_help.txt file." + e);
        }
        while (true){
            System.out.print("Enter the setting number [Q or q for quit] > ");
            String userInput = scanner.nextLine();
            if (Objects.equals(userInput, "Q") || Objects.equals(userInput, "q")){
                System.out.println("INFO: Exiting settings");
                break;
            }
            try {
                Integer.parseInt(userInput);
                switch (Integer.parseInt(userInput)){
                    case 0:
                        Jira.getJIRASettings();
                        Ticket.getJIRASettings("Sample Summary 2", "Sample Description 2");
                        break;
                    case 1:
                        System.out.println("Loading Slack Settings.");
                        break;
                    default:
                        System.out.println("ERROR: Invalid input. Please try again.");
                }
            }catch (Exception e){
                System.out.println("ERROR: Invalid input. Please enter a value from the above list.");
            }

        }
    }
}
