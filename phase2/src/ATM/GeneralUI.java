package ATM;

import java.util.Scanner;

abstract class GeneralUI {

    Bank bank;
    Scanner userInput;

    GeneralUI(Bank b) {
        bank = b;
        userInput = new Scanner(System.in);
    }

    abstract void displayGeneralOptions();
}
