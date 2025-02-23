package local.smc.backup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class NGBBackupMainMenu {
    public static void Menu(){
        System.out.println("INFO: NGB Backup Verification");

        Scanner commandInput = new Scanner(System.in);

        while(true){
            System.out.print("NGB-Backup-Verification> ");
            String command = commandInput.nextLine().trim();
            switch (command){
                case "help":
                    System.out.println("INFO: Printing the helper Menu.\n");
                    String helpFilePath = "config/help-content/ngb-backup-verification";
                    String helpLine;
                    try (BufferedReader helpFileContent = new BufferedReader(new FileReader(helpFilePath))){
                        while ((helpLine = helpFileContent.readLine()) != null){
                            String optionValue = helpLine.split(",")[0];
                            String optionDescription = helpLine.split(",")[1];
                            System.out.printf(" %-30s %-50s\n", optionValue, optionDescription);
                        }
                    } catch (IOException e){
                        System.out.println("Error: Unable to open the help.txt file.");
                    }
                    break;
                case "show-all":
                case "sa":
                      NGBBackupVerification.showJobsByStatus("");
                    break;

                case "show-failed":
                case "sf":
                    NGBBackupVerification.showJobsByStatus("failed");
                    break;
                case "show-success":
                case "ss":
                    NGBBackupVerification.showJobsByStatus("success");
                    break;

                case "quit":
                    System.out.println("Bye");
                    commandInput.close();
                    return;
            }
        }
    }
}
