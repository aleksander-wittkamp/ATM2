package ATM;

import java.util.List;
import java.util.Scanner;

/**
 * This UI allows a Client to do the following:
 * - see their account info, including dates of creation, account balances, and transactions
 * - change their password
 * -
 */
class ClientUI extends IsUserUI {
    private Client currClient;

    ClientUI(Bank b, User c){
        super(b, c);
        currClient = (Client) currUser;
    }

    void displayGeneralOptions() {
        String option;
        while (running) {
            System.out.print("\n-----------------------------------------------\n");
            System.out.println("\nHello " + currClient.getUsername() + "!");
            System.out.println("\nPlease select one of the following options below: ");
            System.out.println("\nEnter 'A' to view accounts");
            System.out.println("Enter 'B' to change password");
            System.out.println("Enter 'Z' to log out");
            System.out.print("Enter here: ");
            option = userInput.nextLine();
            while (!option.equals("A") && !option.equals("B") && !option.equals("Z")) {
                System.out.println("Invalid option. Please try again.");
                System.out.print("Enter here: ");
                option = userInput.nextLine();
            }

            if (option.equals("A")) {
                displayAccounts();
            } else if (option.equals("B")){
                changePassword();
            } else {
                logout();
            }
        }
    }

    private void changePassword() {
        System.out.print("\n-----------------------------------------------\n");
        System.out.println("Current password: " + currClient.getPassword());
        System.out.print("Please enter a new password: ");
        String newPassword = userInput.nextLine();
        while (newPassword.equals(currClient.getPassword())) {
            System.out.println("Entered password is same as old password. Try again.");
            System.out.print("Please enter a new password: ");
            newPassword = userInput.nextLine();
        }
        currClient.setPassword(newPassword);
    }

    private void displayAccounts() {
        System.out.print("\n-----------------------------------------------\n");
        System.out.println("\nClient: " + currClient.username);
        System.out.println("\nNet total: " + currClient.calculateNetTotal()); // unsure whether we should have a netTotal variable
        System.out.println("Enter 'A' to view transactions");
        System.out.println("Enter 'B' to view debt accounts");
        System.out.println("Enter 'C' to view asset accounts");
        System.out.println("Enter 'X' to go back");
        boolean run = true;
        do {
            System.out.print("Enter here: ");
            String option = userInput.nextLine();
            if (option.equals("A")) {
                run = false;
                displayTransactions();
            } else if (option.equals("B")) {
                run = false;
                viewDebtAccounts();
            } else if (option.equals("C")) {
                run = false;
                displayAssetAccounts();
            } else if (option.equals("X")) {
                run = false;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        } while (run);
    }


    private void displayTransactions() {
        System.out.print("\n-----------------------------------------------");
        System.out.println("\nClient: " + currClient.getUsername());
        System.out.println("\nTransactions: ");

        StringBuilder transactionsString = new StringBuilder();
        List<Transaction> transactions = currClient.getTransactions();
        for (Transaction t : transactions) {
            if (t.getType() == 1) {
                String toAppend = "Between Account Transfer: $" + t.getAmount() + " transferred from "
                        + t.getFromAccountId() + " to " + t.getToAccountId() + "\n";
                transactionsString.append(toAppend);
            } else if (t.getType() == 2) {
                String toAppend = "Between Client Transfer: $" + t.getAmount() + " transferred from "
                        + t.getFromAccountId() + " to " + t.getReceiverUsername() + "\n";
                transactionsString.append(toAppend);
            } else {
                String toAppend = "ToNonUserTransfer$" + t.getAmount() + " transferred from " + t.getFromAccountId()
                        + " to  " + t.getNonUserReceiver() + "\n";
                transactionsString.append(toAppend);
            }
        }
        System.out.println(transactionsString);

        System.out.println("\nEnter 'X' to go back");
        System.out.print("Enter here: ");
        String option = userInput.nextLine();
        while (!option.equals("X")) {
            System.out.println("Invalid input. Try again.");
            System.out.print("Enter here: ");
            option = userInput.nextLine();
        }
        displayAccounts();
    }


    private void displayAssetAccounts() {
        System.out.print("\n-----------------------------------------------");
        System.out.println("\n\nClient: " + currClient.getUsername());
        System.out.println("\nAsset accounts:");
        displayAssetAccountOptions();
        // TODO print out the total asset?
        // TODO show which chequing account is primary?
        boolean invalidOption = true;

        while (invalidOption) {
            System.out.print("\nEnter here: ");
            String option = userInput.nextLine();
            //TODO if time allows fix error checking. Inputting account A- crashes the system.
            while (option.length() < 1){
                System.out.print("\nPlease enter a valid account option. ");
                System.out.print("\nEnter here: ");
                option = userInput.nextLine();
            }

            String letterOption = option.substring(0, 1);
            int accountNum = Character.getNumericValue(option.charAt(option.length() - 1));

            if (letterOption.equals("A")) {
                if (accountNum < currClient.getSavingsList().size()) {
                    Savings thisSavings = currClient.getSavingsList().get(accountNum);
                    invalidOption = false;
                    displaySavings(thisSavings);
                } else {
                    System.out.println("Savings account entered does not exist.");
                    System.out.println("Please enter a valid account option.");
                }
            } else if (letterOption.equals("B")) {
                if (accountNum < currClient.getChequingList().size()) {
                    Chequing thisChequing = currClient.getChequingList().get(accountNum);
                    invalidOption = false;
                    displayChequing(thisChequing);
                } else {
                    System.out.println("Chequing account entered does not exist.");
                    System.out.println("Please enter a valid account option.");
                }
            }
            else if (letterOption.equals("C")){
                invalidOption = false;
                createAssetAccount();
            }
            else if (letterOption.equals("X")) {
                invalidOption = false;
                displayAccounts();
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void createAssetAccount(){
        System.out.println("\n-----------------------------------------------\n");
        System.out.println("What kind of asset account would you like to create?");
        System.out.println("Enter 'A' for savings account");
        System.out.println("Enter 'B' for chequing account");
        System.out.println("Enter 'X' to go back");
        System.out.print("Enter here: ");
        String option = userInput.nextLine();
        while (!option.equals("A") && !option.equals("B") && !option.equals("X")){
            System.out.println("Invalid option. Try again.");
            System.out.print("Enter here: ");
            option = userInput.nextLine();
        }

        if(option.equals("A")){
            //System.out.println("createAssetAccount (Client class)" + userManager.getMap());
            currClient.requestSavingsAccountCreation(currClient.getSavingsList().size() + 1);
            System.out.println("New savings account has been requested!");
            displayAssetAccounts();
        }
        else if (option.equals("B")){
            currClient.requestChequingAccountCreation(currClient.getChequingList().size() + 1);
            System.out.println("New chequing account has been requested!");
            displayAssetAccounts();
        }
        else{
            displayAssetAccounts();
        }
    }


    private void displayAssetAccountOptions() {
        System.out.println("\nSaving accounts:\n");
        String savingsAccountString = "";
        for (int i = 0; i < currClient.getSavingsList().size(); i++) {
            savingsAccountString =  savingsAccountString + "    Enter " + "'A" + i + "' " + "to view " + currClient.getSavingsList().get(i) + "\n";
        }
        System.out.println(savingsAccountString);

        String chequingAccountString = "";
        System.out.println("\nChequing accounts:\n");
        for (int i = 0; i < currClient.getChequingList().size(); i++) {
            if (currClient.getChequingList().get(i).isPrimary()) {
                chequingAccountString = chequingAccountString + "    Enter " + "'B" + i + "'" + "to view "
                        + currClient.getChequingList().get(i) + " (Primary)" + "\n";
            } else {
                chequingAccountString = chequingAccountString + "    Enter " + "'B" + i + "'" + "to view "
                        + currClient.getChequingList().get(i) + "\n";
            }
        }
        System.out.print(chequingAccountString);

        System.out.println("\nEnter 'C' to create an asset account");

        System.out.println("\nEnter 'X' to go back");
    }


    /*
    A savings account stores a $0 or positive balance at all times.
    Before closing, on the 1st of every month, all savings account balances
    increase by a factor of 0.1%. In other words, a savings account with $100
    in it on January 31, will have a balance of $100.10 on February 1st.

    TODO - need to implement interest rate
     */
    private void displaySavings(Savings thisSavings) {
        System.out.println("\n-----------------------------------------------\n");
        System.out.println("Client: " + currClient.getUsername());
        System.out.println("\nSavings id: " + thisSavings.id);
        System.out.println("\n Date of creation: " + thisSavings.getCreationDate());
        System.out.println("Balance: " + thisSavings.balance);
        System.out.println("Enter 'A' to transfer in money from another account");
        System.out.println("Enter 'B' to transfer money out");
        System.out.println("Enter 'C' to make a withdrawal");
        System.out.println("Enter 'X' to go back");
        System.out.println("Enter here: ");
        String option = userInput.nextLine();
        while (!option.equals("A") && !option.equals("B") && !option.equals("C") && !option.equals("X")) {
            System.out.println("Invalid option. Please try again.");
            System.out.println("Enter here: ");
            option = userInput.nextLine();
        }

        if (option.equals("A")) {
            displayTransferIn(thisSavings);
            displaySavings(thisSavings);

        }
        else if (option.equals("B")){
            //TODO fix this part
            displayTransferOut(thisSavings);
            displaySavings(thisSavings);
        }
        else if (option.equals("C")) {
            System.out.println("Valid withdraw amounts are any denomination of 5.");
            boolean invalidamount = true;
            while (invalidamount) {
                int amount = InputUtility.getPositiveInteger();
                if (amount % 5 == 0) {
                    tryWithdrawal(amount, thisSavings);
                    invalidamount = false;
                } else {
                    System.out.println("Invalid amount. Amount must be denomination of 5. Please try again.");
                }
            } displaySavings(thisSavings);
        }
        else{
            displayAssetAccounts();
        }
    }

    public boolean tryWithdrawal(int amount, Account account){
        boolean successfulWithdraw = currClient.withdraw(amount, account);
        if (successfulWithdraw){
            System.out.println("Successfully withdrew $" + amount);
        }
        else {
            System.out.println("Insufficient funds.");
        }
        return successfulWithdraw;
    }

    private void displayChequing(Chequing thisChequing) {
        System.out.println("\n-----------------------------------------------\n");
        System.out.println("Client: " + currClient.getUsername());
        System.out.println("\nSavings id: " + thisChequing.id);
        System.out.println("\n Date of creation: " + thisChequing.getCreationDate());
        System.out.println("Balance: " + thisChequing.balance);

        System.out.println("Enter 'A' to make a withdrawal");
        if (thisChequing.isPrimary()) {
            System.out.println("Enter 'B' to make a deposit");
        }
        System.out.println("Enter 'C' to transfer in money from another account");
        System.out.println("Enter 'D' to transfer money out");
        System.out.println("Enter 'X' to go back");

        System.out.println("Enter here: ");
        String option = userInput.nextLine();

        boolean isPrimaryOptions = thisChequing.isPrimary() && !option.equals("A") && !option.equals("B") && !option.equals("C") && !option.equals("D") && !option.equals("X");
        boolean isNotPrimaryOptions = !thisChequing.isPrimary() && !option.equals("A") && !option.equals("C") && !option.equals("D") && !option.equals("X");

        while (isPrimaryOptions || isNotPrimaryOptions) {
            System.out.println("Invalid option. Please try again.");
            System.out.println("Enter here: ");
            option = userInput.nextLine();
        }

        if (option.equals("A")) {
            System.out.println("Valid withdraw amounts are any denomination of 5.");
            boolean invalidamount = true;
            while (invalidamount) {
                int amount = InputUtility.getPositiveInteger();
                if (amount % 5 == 0) {
                    tryWithdrawal(amount, thisChequing);
                    invalidamount = false;
                } else {
                    System.out.println("Invalid amount. Amount must be denomination of 5. Please try again.");
                }
            }
            displayChequing(thisChequing);
        } else if (option.equals("B")) {
            currClient.deposit(thisChequing);
            System.out.println("Successfully deposited to "+ thisChequing.toString());
            displayChequing(thisChequing);
        } else if(option.equals("C")){
            displayTransferIn(thisChequing);
            displayChequing(thisChequing);

        } else if (option.equals("D")){
            displayTransferOut(thisChequing);
            displayChequing(thisChequing);
        } else {
            displayAssetAccounts();
        }
    }


    private void viewDebtAccounts() {
        System.out.println("\n-----------------------------------------------\n");
        System.out.println("Client: " + currClient.getUsername());
        System.out.println("Debt accounts:");
        // TODO print out the total debt?

        displayDebtAccountOptions();
        boolean invalidOption = true;

        while (invalidOption) {
            System.out.print("Enter here: ");
            String option = userInput.nextLine();
            while (option.length() < 1){
                System.out.print("\nPlease enter a valid account option. ");
                System.out.print("\nEnter here: ");
                option = userInput.nextLine();
            }
            String letterOption = option.substring(0, 1);
            int accountNum = Character.getNumericValue(option.charAt(option.length() - 1));
            if (letterOption.equals("A")) {
                if (accountNum < currClient.getCreditCardList().size()) {
                    CreditCard thisCreditCard = currClient.getCreditCardList().get(accountNum);
                    invalidOption = false;
                    displayCreditCard(thisCreditCard);
                } else {
                    System.out.println("Credit card account entered does not exist.");
                    System.out.println("Please enter a valid account option.");
                }
            } else if (letterOption.equals("B")) {
                if (accountNum < currClient.getLineOfCreditList().size()) {
                    LineOfCredit thisLineOfCredit = currClient.getLineOfCreditList().get(accountNum);
                    invalidOption = false;
                    displayLineOfCredit(thisLineOfCredit);
                } else {
                    System.out.println("Line of credit account entered does not exist.");
                    System.out.println("Please enter a valid account option.");
                }
            } else if (letterOption.equals("C")) {
                invalidOption = false;
                createDebtAccount();
            } else if (letterOption.equals("X")) {
                invalidOption = false;
                displayAccounts();
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void displayDebtAccountOptions(){
        System.out.println("\nCredit Card accounts:\n");

        if (currClient.getCreditCardList().size() != 0){
            for (int i = 0; i < currClient.getCreditCardList().size(); i++) {
                System.out.println("Enter " + "'A" + i + "'" + " to view " + currClient.getCreditCardList().get(i));
            }
        }
        else{
            System.out.println("No Credit Card accounts exists.");
        }

        System.out.println("\nLine of Credit accounts:\n");

        if (currClient.getLineOfCreditList().size() != 0){
            for (int i = 0; i < currClient.getLineOfCreditList().size(); i++) {
                System.out.println("Enter " + "'B" + i + "'" + " to view " + currClient.getLineOfCreditList().get(i));
            }
        }
        else{
            System.out.println("No Line Of Credit Card accounts exists.");
        }

        System.out.println("\nEnter 'C' to create debt account");
        System.out.println("Enter 'X' to go back");
    }

    // TODO create debt or line of credit account
    private void createDebtAccount(){
        System.out.println("\n-----------------------------------------------\n");
        System.out.println("What kind of debt account would you like to create?");
        System.out.println("Enter 'A' for credit card account");
        System.out.println("Enter 'B' for line of credit account");
        System.out.println("Enter 'X' if you would like to go back");
        System.out.print("Enter here: ");
        String option = userInput.nextLine();
        while (!option.equals("A") && !option.equals("B") && !option.equals("X")){
            System.out.println("Invalid option. Try again.");
            System.out.print("Enter here: ");
            option = userInput.nextLine();
        }

        if(option.equals("A")){
            currClient.requestCreditCardAccountCreation(currClient.getCreditCardList().size() + 1);
            System.out.println("Requested credit card account creation! ");
            //Sleep so that user can see output messageA
            InputUtility.sleep(1000);
//            viewDebtAccounts();
        }
        else if (option.equals("B")){
            System.out.println("Requested line of credit account creation! ");
            currClient.requestLineOfCreditAccountCreation(currClient.getLineOfCreditList().size() + 1);
//            viewDebtAccounts();
            InputUtility.sleep(1000);
        }
        else{
            viewDebtAccounts();
        }
        //Return to the previous screen after this is done.
        viewDebtAccounts();
    }



    private void displayCreditCard(CreditCard thisCreditCard) {
        System.out.println("\n-----------------------------------------------\n");
        System.out.println("Client: " + currClient.getUsername());
        System.out.println("\nCredit card id: " + thisCreditCard.id);
        System.out.println("\n Date of creation: " + thisCreditCard.getCreationDate());
        System.out.println("Balance: " + thisCreditCard.balance);
        System.out.println("Enter 'A' to transfer in money from another account");
        System.out.println("Enter 'X' to go back");
        System.out.println("Enter here: ");
        String option = userInput.nextLine();
        while (!option.equals("A") && !option.equals("X")) {
            System.out.println("Invalid option. Please try again.");
            System.out.println("Enter here: ");
            option = userInput.nextLine();
        }

        if (option.equals("A")) {
            displayTransferIn(thisCreditCard);
            displayCreditCard(thisCreditCard);
        } else {
            displayDebtAccountOptions();
        }
    }

    private void displayLineOfCredit(LineOfCredit thisLineOfCredit) {
        System.out.println("\n-----------------------------------------------\n");
        System.out.println("Client: " + currClient.getUsername());
        System.out.println("\nLine of cred id: " + thisLineOfCredit.id);
        System.out.println("\n Date of creation: " + thisLineOfCredit.getCreationDate());
        System.out.println("Balance: " + thisLineOfCredit.balance);
        System.out.println("Enter 'A' to transfer in money from another account");
        System.out.println("Enter 'B' to transfer out money");
        System.out.println("Enter 'X' to go back");
        System.out.println("Enter here: ");
        String option = userInput.nextLine();
        while (!option.equals("A") && !option.equals("B") && !option.equals("X")) {
            System.out.println("Invalid option. Please try again.");
            System.out.println("Enter here: ");
            option = userInput.nextLine();
        }

        if (option.equals("A")) {
            displayTransferIn(thisLineOfCredit);
            displayLineOfCredit(thisLineOfCredit);

        } else if (option.equals("B")) {
            // Removed some code here
            displayTransferOut(thisLineOfCredit);
            displayLineOfCredit(thisLineOfCredit);
        } else {
            displayDebtAccountOptions();
        }
    }

    /**
     * HELPERS FOR DISPLAYING OPTIONS.
     */

    private void displayTransferIn(Account thisAccount) {
        System.out.println("Enter account id to transfer in from.");

        System.out.println("Enter here: ");

        String fromAccount = userInput.nextLine();

        while (!currClient.hasAccount(fromAccount)) {
            System.out.println("Account id entered does not exist.");
            System.out.println("Please enter a valid account id.");
            System.out.println("Enter account id to transfer in from.");
            System.out.println("Enter here: ");
            fromAccount = userInput.nextLine();
        }

        //Now we have a valid account

        boolean validTransferAmount = false;

        double amount = InputUtility.getPositiveDollarInput("How much would you like to transfer in?");
//        double amount = 0;
//        while (amount <= 0) {
//            System.out.println("How much would you like to transfer in?");
//            System.out.print("Enter here: $");
//            if (userInput.hasNextDouble()) {
//                amount = userInput.nextDouble();
//                // The below is needed to consume the \n character created by hitting enter following the
//                // vale inputted by the user.
//                userInput.nextLine();
//            } else {
//                System.out.println("Invalid amount. Please try again.");
//                userInput.nextLine();
//            }
//            //TODO test that this logic is correct.
//            if (amount <= 0) {
//                System.out.println("Amount must be greater than 0. Please try again.");
//                userInput.nextLine();
//            }
//        }

        // Now we have an account and an amount

        boolean successfulTransfer =
                currClient.withinUserTransfer(currClient, fromAccount, thisAccount, amount);
        if (successfulTransfer) {
            System.out.println("Successfully transferred $" + amount);

        } else {
            System.out.println("Insufficient funds.");
        }
    }

    private void displayTransferOut(Account thisAccount){
        boolean invalidTransferAmount = true;
        Scanner userInput = new Scanner(System.in);        double amount = InputUtility.getPositiveDollarInput("How much would you like to transfer out?");



        while (invalidTransferAmount) {

            System.out.println("Enter 'A' to transfer money to another user");
            System.out.println("Enter 'B' to transfer money to a non user");
            System.out.println("Enter 'X' to go back");
            String transferOutOption = userInput.nextLine();
            invalidTransferAmount = false;

            while (!transferOutOption.equals("A") && !transferOutOption.equals("B") && !transferOutOption.equals("X")) {
                System.out.println("Invalid option. Please try again.");
                System.out.println("Enter here: ");
                transferOutOption = userInput.nextLine();
            }

            if (transferOutOption.equals("A")) {
//                    boolean invalidUser = true;
//                    while (invalidUser) {
                System.out.println("Enter username of other user to transfer to.");
                System.out.println("Enter here:");
                String username = userInput.nextLine();
                boolean successfulTransfer =
                        currClient.betweenUsersTransfer(currClient, thisAccount, username, amount);
                if (successfulTransfer){
                    System.out.println("Successfully transferred $" + amount);
                }
                else {
                    System.out.println("Transfer failed. Either insufficient funds or invalid user.");
                }
//                    }
            }
            else if (transferOutOption.equals("B")) {
                System.out.print("Enter name of receiver.");
                System.out.print("Enter here:");
                String receiver = userInput.nextLine();
                if(currClient.toNonUserTransfer(currClient, thisAccount, receiver, amount)) {
                    System.out.println("Transfer successful.");
                } else{
                    System.out.println("Insufficient funds.");
                }
            }
            else {
                //user has chosen to go back
                goBackToAccount(thisAccount);
            }
        }
    }

    private void goBackToAccount(Account thisAccount){
        if (thisAccount instanceof Chequing){
            displayChequing((Chequing) thisAccount);
        }
        else if(thisAccount instanceof Savings){
            displaySavings((Savings) thisAccount);
        }
        else if(thisAccount instanceof LineOfCredit){
            displayLineOfCredit((LineOfCredit) thisAccount);
        }
        else{
            displayCreditCard((CreditCard)thisAccount);
        }
    }

//    public boolean displayTransferOut(Account thisAccount){
//        boolean invalidTransferAmount = true;
//        Scanner userInput = new Scanner(System.in);
//        System.out.println("How much would you like to transfer out?");
//
//        while (invalidTransferAmount) {
//            System.out.print("Enter here: $");
//            if (userInput.hasNextDouble()) {
//                double amount = userInput.nextDouble();
//                // The below is needed to consume the \n character created by hitting enter following the
//                // vale inputted by the user.
//                userInput.nextLine();
//
//                System.out.println("Enter 'A' to transfer money to another user");
//                System.out.println("Enter 'B' to transfer money to a non user");
//                System.out.println("Enter 'x' to go back");
//                String transferOutOption = userInput.nextLine();
//                invalidTransferAmount = false;
//
//                while (!transferOutOption.equals("A") && !transferOutOption.equals("B") && !transferOutOption.equals("x")) {
//                    System.out.println("Invalid option. Please try again.");
//                    System.out.println("Enter here: ");
//                    transferOutOption = userInput.nextLine();
//                }
//
//                if (transferOutOption.equals("A")){
//                    boolean invalidUser = true;
//                    while (invalidUser) {
//                        System.out.println("Enter username of other user to transfer to.");
//                        System.out.println("Enter here:");
//                        String username = userInput.nextLine();
//                        boolean successfulTransfer =
//                                currClient.betweenUsersTransfer(currClient, thisAccount, username, amount);
//                        if(successfulTransfer){
////                            currClient.betweenUsersTransfer(currClient, thisAccount, username, amount);
//                            System.out.println("Successfully transferred $" + amount);
//                            invalidUser = false;
//                        } else{
//                            System.out.println("Invalid user. Please enter a valid user to transfer to.");
//                        }
//                    }
//                } else if (transferOutOption.equals("B")){
//                    System.out.print("Enter name of receiver.");
//                    System.out.print("Enter here:");
//                    String receiver = userInput.nextLine();
//                    currClient.toNonUserTransfer(currClient, thisAccount, receiver, amount);
//                } else {
//                    //user has chosen to go back
//                    return true;
//                }
//            } else {
//                System.out.println("Invalid amount. Please try again.");
//                userInput.nextLine();
//            }
//        }
//        //user has not chosen to go back
//        return false;
//    }

}
