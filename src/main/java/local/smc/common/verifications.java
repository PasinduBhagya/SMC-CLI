package local.smc.common;

import java.util.List;
import java.util.Scanner;

public class verifications {

    public static int checkForIntegers(String value){
        Scanner scanner = new Scanner(System.in);
        int compID;
        while (true){
            try {
                System.out.print(value);
                compID = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e){
                System.out.println("ERROR: ID must be an Integer.");
            } catch (java.util.NoSuchElementException e) {
                System.out.println("INFO: Exiting program.");
            }
        }
        return compID;
    }

    public static String checkForEmptyString(String inputMessage){
        Scanner scanner = new Scanner(System.in);
        String inputValue;
        while (true){
            System.out.print(inputMessage);
            inputValue = scanner.nextLine();
            if (inputValue.isEmpty()){
                System.out.println("ERROR: This field cannot be empty");
            }else {
                break;
            }
        }
        return inputValue;
    }

    public static int checkForAllowedValues(String inputMessage, List<Integer> IDsArray){
        Scanner scanner = new Scanner(System.in);
        int ID;
        while (true) {
            System.out.print(inputMessage);
            try {
                String input = scanner.nextLine();
                if (input.isBlank()) {
                    System.out.println("ERROR: ID cannot be empty.");
                    continue;
                }
                ID = Integer.parseInt(input);
                if (!IDsArray.contains(ID)) {
                    System.out.println("ERROR: ID not found. Please enter an ID from the above table.");
                    continue;
                }
                return ID;
            } catch (NumberFormatException e) {
                System.out.println("ERROR: ID should be an Integer.");
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }

    public static int checkForUpdatedAllowedValues(String inputMessage, List<Integer> IDsArray){
        Scanner scanner = new Scanner(System.in);
        int ID;
        while (true) {
            System.out.print(inputMessage);
            try {
                String input = scanner.nextLine();
                if (input.isEmpty()){
                    return -1;
                }
                ID = Integer.parseInt(input);
                if (!IDsArray.contains(ID)) {
                    System.out.println("ERROR: ID not found. Please enter an ID from the above table.");
                    continue;
                }
                return ID;
            } catch (NumberFormatException e) {
                System.out.println("ERROR: ID should be an Integer.");
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }

    public static String checkForFormating(String inputMessage, List<Integer> IDsArray){
        System.out.print(inputMessage);
        Scanner scanner = new Scanner(System.in);

        String enteredOtherIDs;

        while (true) {
            enteredOtherIDs = scanner.nextLine();
            String[] splitStrings = enteredOtherIDs.split(",");

            boolean isValid = true;
            for (String splitString : splitStrings) {
                try {
                    int otherUserID = Integer.parseInt(splitString.trim());
                    if (!IDsArray.contains(otherUserID)) {
                        System.out.println("ERROR: ID not found. Please enter an ID from the above table.");
                        isValid = false;
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("ERROR: Each ID must be an integer. Please try again.");
                    isValid = false;
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("ERROR: " + e.getMessage());
                    isValid = false;
                    break;
                }
            }
            if (isValid) {
                return enteredOtherIDs;
            }
            System.out.print(inputMessage);
        }
    }

    public static String checkForFormating(String inputMessage, List<Integer> IDsArray, String currentValue){
        System.out.print(inputMessage);
        Scanner scanner = new Scanner(System.in);

        String enteredOtherIDs;

        while (true) {
            enteredOtherIDs = scanner.nextLine();

            if (enteredOtherIDs.isEmpty()){
                return currentValue;
            }

            String[] splitStrings = enteredOtherIDs.split(",");

            boolean isValid = true;
            for (String splitString : splitStrings) {
                try {
                    int otherUserID = Integer.parseInt(splitString.trim());
                    if (!IDsArray.contains(otherUserID)) {
                        System.out.println("ERROR: ID not found. Please enter an ID from the above table.");
                        isValid = false;
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("ERROR: Each ID must be an integer. Please try again.");
                    isValid = false;
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("ERROR: " + e.getMessage());
                    isValid = false;
                    break;
                }
            }
            if (isValid) {
                return enteredOtherIDs;
            }
            System.out.print(inputMessage);
        }
    }
}
