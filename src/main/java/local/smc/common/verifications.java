package local.smc.common;

public class verifications {
    // Integer
    public static boolean checkForIntegers(String value){
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e){
            System.out.println("ERROR: The input must be an Integer(Ex: 4, 9)");
            return false;
        }
    }
    // Allowed values
    // valid formats
}
