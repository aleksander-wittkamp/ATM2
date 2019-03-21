package ATM;

import java.util.Scanner;

public class InputUtility {

    // THIS CLASS IS BEING USED. DO NOT DELETE.

    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static int getPositiveInteger() {
        Scanner userInput = new Scanner(System.in);
        int amount;
        while (true) {
            System.out.println("How much would you like to withdraw?");
            System.out.println("Amount must be lower than 1000000000.");
            System.out.print("Enter here: $");
            String option = userInput.nextLine();
            if (AllNumeric(option)) {
                if (option.length() > 9) {
                    System.out.println("Amount is not lower than 1000000000");
                } else {
                    amount = Integer.valueOf(option);
                    if (amount > 0) {
                        return amount;
                    } else {
                        System.out.println("Amount must be greater than 0. Please try again.");
                    }
                }
            } else {
                System.out.println("Invalid amount. Please try again.");
            }
        }
    }

    private static boolean AllNumeric(String option) {
        boolean result = true;
        for (int i = 0; i < option.length(); i++) {
            if (!Character.isDigit(option.charAt(i))) {
                result = false;
            }
        }
        return result;
    }

    static double getPositiveDollarInput(String question) {
        Scanner userInput = new Scanner(System.in);
        double amount = 0;
        while (amount <= 0) {
            System.out.println(question);
            System.out.print("Enter here: $");
            if (userInput.hasNextDouble()) {
                amount = userInput.nextDouble();
                // The below is needed to consume the \n character created by hitting enter following the
                // vale inputted by the user.
                userInput.nextLine();
            } else {
                System.out.println("Invalid amount. Please try again.");
                userInput.nextLine();
                //Skip the below if
                continue;
            }
            //TODO test that this logic is correct.
            if (amount <= 0) {
                System.out.println("Amount must be greater than 0. Please try again.");
//                userInput.nextLine();
            }
        }
        return amount;
    }

//    public static String takeStringInput(String textToDisplay){
//        Scanner in = new Scanner(System.in);
//        String input = "";
//        while (input.equals("") ) {
//            System.out.print(textToDisplay);
//            try {
//                input = in.nextLine();
//            } catch (Exception e) {
//                System.out.println("Please input a valid String.");
//            }
//        }
////        System.out.println(input);
//        return input;
//    }
//    public static int takeNaturalNumberInput(String textToDisplay){
//        Scanner in = new Scanner(System.in);
//        int number = -1;
//        while (number < 0) {
//            System.out.print(textToDisplay);
//            try {
//                number = Integer.parseInt(in.nextLine());
//            } catch (NumberFormatException e) {
//                System.out.println("Please input a Number.");
//            }
//        }
//        return number;
//    }
}
