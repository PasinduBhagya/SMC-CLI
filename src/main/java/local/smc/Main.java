package local.smc;

import local.smc.scheduled.patching.MainMenu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        String[] validArguments = {"--help", "--show-options", "--use-option"};

        System.out.println("5GN SMC CLI Tool\n");
        // Checking if Arguments are given
        if (args.length == 0){
            System.out.println("Error: Required arguments are missing. Please use --help option to check arguments");
            return;
        }

        for (String passedArgument: args) {
            // Checking for invalid arguments and printing help menu
            if (!Arrays.asList(validArguments).contains(passedArgument)){
                System.out.println("Error: Invalid argument provided. " + passedArgument);
                break;
            }
            // TO print --help option even other arguments are given
            if (passedArgument.contains("--help")) {
                System.out.println("INFO: Printing the helper Menu.\n");
                String helpFilePath = "config/help.txt";
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
                return;
            }
            // To show options
            if (passedArgument.equals("--show-options")) {
                System.out.println("INFO: Showing options.\n");
                String optionsFilePath = "config/options.txt";
                try (BufferedReader optionsFileContent = new BufferedReader(new FileReader(optionsFilePath))) {
                    String optionLine;
                    int i = 0;
                    System.out.println("+-----+-------------------------------------+");
                    System.out.println("| No. | Option Name                         |");
                    System.out.println("+-----+-------------------------------------+");
                    while ((optionLine = optionsFileContent.readLine()) != null) {
                        System.out.printf("| %-3d | %-35s |\n", i++, optionLine);
                        System.out.println("+-----+-------------------------------------+");
                    }
                } catch (IOException e){
                    System.out.println("Error: Unable to open the options.txt file.");
                }
            }
            // To use options
            if (passedArgument.equals("--use-option")){
                if (args.length > 1){
                    String optionValue = args[1];
                    switch (optionValue){
                        case "0":
                            MainMenu.Menu();
                            break;
                        case "1":
                            System.out.println("INFO: Bandwidth report Generator");
                            break;
                        default:
                            System.out.println("ERROR: Invalid option is provided.");
                    }
                }
                else {
                    System.out.println("ERROR: Option value is missing after --use-option");
                }
                return;
            }
        }
    }
}