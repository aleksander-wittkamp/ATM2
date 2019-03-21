package ATM;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class BankManagerUI extends IsUserUI {
    private BankManager bankManager;

    BankManagerUI(Bank b, User u) {
        super(b, u);
        bankManager = (BankManager) currUser;
    }

    /**
     * 1) Create a login and set the initial password for a user .
     * 2) Increase the number of $5, $10, $20, and/or $50 bills in the machine to simulate restocking the machine
     * 3) Lastly, the manager has the ability to undo the most recent transaction on any asset or debt account,
     * except for paying bills.
     */

    @Override
    void displayGeneralOptions() {
        while (running) {
            System.out.println("\nWelcome, " + bankManager.getUsername() + "!\n");

            printNotifications();

            System.out.println("Please choose what you'd like to do.");
            System.out.println("Enter 'A' to restock the ATM.");
            System.out.println("Enter 'B' to create a new user.");
            System.out.println("Enter 'C' to view new account requests or to create a new account.");
            System.out.println("Enter 'D' to undo client transactions.");
            System.out.println("Enter 'E' to change your password.");
            System.out.println("Enter 'Z' to logout.");
            System.out.print("Enter here: ");

            String option = userInput.nextLine();

            while (!option.equals("A") && !option.equals("B") && !option.equals("C") && !option.equals("D")
                    && !option.equals("E") && !option.equals("Z")) {
                option = rePrompt();
            }

            switch (option) {
                case "A":
                    restockATM();
                    break;
                case "B":
                    createNewUser();
                    break;
                case "C":
                    createNewAccount();
                    break;
                case "D":
                    undoTransaction();
                    break;
                case "E":
                    displayChangePassword();
                    break;
                default:
                    logout();
                    break;
            }
        }
    }

    private void printNotifications() {
        System.out.println("----------------NOTIFICATIONS--------------\n");
        restockNotification();
        System.out.println("-------------------------------------------\n");
        shortNewAccountRequestNotification();
        System.out.println("-------------------------------------------\n");
    }

    private void longNewAccountRequestNotification() {
        List<String[]> accountRequests = bankManager.getNewAccountRequests();

        if (!(accountRequests.size() == 0)) {
            System.out.println("We have " + accountRequests.size() + " new account requests: \n");
            for (int i = 0; i < accountRequests.size(); i++) {
                String[] request = accountRequests.get(i);
                System.out.println(i + ") User " + request[0] + " wants a new " + request[1] +
                        " account with the id: " + request[2]);
            }
        } else {
            System.out.println("There are no new account requests.");
        }
    }

    private void shortNewAccountRequestNotification() {
        if (!(bankManager.getNewAccountRequests().size() == 0)) {
            System.out.println("We have " + bankManager.getNewAccountRequests().size() + " new account request(s).");
        } else {
            System.out.println("There are no new account requests.");
        }
    }

    private void restockNotification() {
        String directory = (System.getProperty("user.dir") + "/phase2/src/IO_Files/");
        File tmpDir = new File(directory + "alerts.txt");
        boolean exists = tmpDir.exists();

        if (exists) {
            System.out.println("The ATM is running low.");
        } else {
            System.out.println("The ATM is well-stocked.");
        }
    }

    /**
     * RESTOCK
     */

    private void restockATM() {
        MoneyManager moneyManager = bank.getMoneyManager();
        boolean[] isLow = IOManager.getIsLow();
        int[] numBills = moneyManager.getMoney();

        int[] billsToIncrease = getBillsToIncrease(isLow, numBills);

        moneyManager.increase(billsToIncrease);
        System.out.println("Operation completed.");
        System.out.println("New amount of bills; ");
        System.out.println(moneyManager.representBills());
        moneyManager.deleteAlerts();
    }

    private int[] getBillsToIncrease(boolean[] isLow, int[] numBills) {
        int[] billsToIncrease = new int[4];
        for (int i = 0; i < isLow.length; i++) {
            if (isLow[i]) {
                String billName = indexToDollars(i);
                System.out.println("Please enter the amount by which you want to increase " + billName + ".");
                System.out.println("Current amount: " + numBills[i]);
                System.out.println("This increase should make the total number of bills above 20.");
                System.out.println("Enter here: ");
                String option = userInput.nextLine();

                // If manager does not enter a number.
                while (notAllNumeric(option) || Integer.valueOf(option) + numBills[i] < 20) {
                    option = rePrompt();
                }

                billsToIncrease[i] = Integer.valueOf(option);
            }
        }
        return billsToIncrease;
    }

    /**
     * CREATE NEW USER
     */

    private void createNewUser() {
        System.out.println("Please enter the username for our new client.");
        System.out.println("Enter 'E' to exit to main menu.");
        System.out.println("Enter here: ");
        String userName = userInput.nextLine();

        if (!userName.equals("E")) {
            System.out.println("Please enter the password for our new client.");
            System.out.println("Enter 'E' to exit to main menu.");
            System.out.println("Enter here: ");
            String password = userInput.nextLine();

            if (!password.equals("E")) {
                bankManager.createClient(userName, password, bankManager);

                System.out.println(userName + " now has a user account.");

                String option = exitQuestionForUser();
                if (option.equals("A")) {
                    createNewUser();
                }
            }
        }
    }

    private String exitQuestionForUser() {
        System.out.println("Enter 'A' to create another user.");
        System.out.println("Enter anything else to go back to main menu.");
        System.out.println("Enter here: ");
        return userInput.nextLine();
    }


    /**
     * NEW ACCOUNT
     */

    private void createNewAccount() {
        longNewAccountRequestNotification();
        System.out.println("\nEnter 'A' for automatically approving all of these requests.");
        System.out.println("Enter 'B' for automatically approving requests one by one.");
        System.out.println("Enter 'C' for manually creating an account for a user.");
        System.out.println("Enter 'E' to exit to main menu.");
        System.out.println("Enter here: ");

        String option = userInput.nextLine();

        while (!option.equals("A") && !option.equals("B") && !option.equals("C") && !option.equals("E")) {
            option = rePrompt();
        }

        switch (option) {
            case "A":
                if(bankManager.getNewAccountRequests().size() > 0){
                approveAllNewAccountRequests();
                } else{ System.out.println("There is no request to approve.");
                }
                break;
            case "B":
                if(bankManager.getNewAccountRequests().size() > 0){
                    approveNewAccountSemiManually();
                } else{ System.out.println("There is no request to approve.");
                }
                break;
            case "C":
                createNewAccountManually();
                break;
            default:
                break;
        }
    }

    private void approveAllNewAccountRequests() {
        for (String[] request : bankManager.getNewAccountRequests()) {
            bankManager.createNewAccount(request[0], request[1], request[2]);
        }
        System.out.println("All requests are approved.");
        System.out.println("We now have " + bankManager.getNewAccountRequests().size() + " more accounts.");
        bankManager.getNewAccountRequests().clear();
    }

    private void approveNewAccountSemiManually() {
        longNewAccountRequestNotification();

        if (bankManager.getNewAccountRequests().size() != 0) {

            System.out.println("\nPlease enter the number of the account request you want to approve: ");

            String option = userInput.nextLine();

            while (notAllNumeric(option) || Integer.valueOf(option) >= bankManager.getNewAccountRequests().size()) {
                option = rePrompt();
            }

            int index = Integer.valueOf(option);
            String[] request = bankManager.getNewAccountRequests().get(index);
            bankManager.createNewAccount(request[0], request[1], request[2]);

            System.out.println("A new " + request[1] + " account is created for " + request[0] + ".");
            bankManager.getNewAccountRequests().remove(index);

            String option1 = exitQuestion();

            if (option1.equals("A")) {
                approveNewAccountSemiManually();
            }
        }
    }

    private void createNewAccountManually() {

        // Get Username
        System.out.println("Please type in the username for whom you want to create a new account today: ");

        String userName = getUserName();

        String accountSubType;

        if (!userName.equals("E")) {
            String accountType = getAccountType(userName);

            if (!accountType.equals("E")) {
                if (accountType.equals("A")) {
                    accountSubType = getAssetType(userName);
                } else {
                    accountSubType = getDebtType(userName);
                }

                String accountId = getAccountId(userName);

                bankManager.createNewAccount(userName, accountSubType, accountId);
                System.out.println(userName + " now has one more " + accountSubType + " account, Id: " + accountId);

                String option = exitQuestion();
                if (option.equals("A")) {
                    createNewAccountManually();
                }
            }
        }
    }

    private String exitQuestion() {

        System.out.println("Enter 'A' to create another account.");
        System.out.println("Enter anything else to go back to main menu.");
        System.out.println("Enter here: ");
        return userInput.nextLine();
    }

    private String getAccountId(String userName) {
        System.out.print("Please enter the id # that you would like to be associated with this account: ");
        String id = userInput.nextLine();

        while (bank.getUserManager().getClient(userName).alreadyExists(id)){
            System.out.println("This id already exists.");
            id = rePrompt();
        }

        return id;
    }

    private String getAccountType(String userName) {
        // Get Account Type

        System.out.println("What type of account would you like to create for " + userName + " ?");
        System.out.println("Enter 'A' for Asset.");
        System.out.println("Enter 'B' for Debt.");
        System.out.println("Enter 'E' to exit to main menu.");
        System.out.println("Enter here: ");
        String option = userInput.nextLine();

        while (!(option.equals("A") || option.equals("B") || option.equals("E"))) {
            option = rePrompt();
        }

        return option;
    }

    private String getDebtType(String userName) {
        // Get Debt Account Type
        System.out.println("What type of Debt account would you like to create for " + userName + " ?");
        System.out.println("Enter 'A' for Credit Card Account.");
        System.out.println("Enter 'B' for Line of Credit Account.");
        System.out.println("Enter here: ");

        String accountType = userInput.nextLine();

        while (!(accountType.equals("A") || accountType.equals("B"))) {
            accountType = rePrompt();
        }

        if (accountType.equals("A")) {
            accountType = "CreditCard";
        } else {
            accountType = "LineOfCredit";
        }
        return accountType;
    }

    private String getAssetType(String userName) {
        // Get Asset Account Type

        System.out.println("What type of Asset account would you like to create for " + userName + " ?");
        System.out.println("Enter 'A' for Chequing Account.");
        System.out.println("Enter 'B' for Savings Account.");
        System.out.println("Enter here: ");

        String accountType = userInput.nextLine();

        while (!(accountType.equals("A") || accountType.equals("B"))) {
            accountType = rePrompt();
        }

        if (accountType.equals("A")) {
            accountType = "Chequing";

        } else {
            accountType = "Savings";
        }
        return accountType;
    }

    /**
     * UNDO TRANSACTION
     */

    private void undoTransaction() {
        // Get Username

        System.out.println("Which client would you like to undo a transaction for today?");
        System.out.println("Please enter their username.");

        String userName = getUserName();

        if (!userName.equals("E")) {
            Client currentClient = bank.getUserManager().getClient(userName);

            // Get accountId
            String id = getUndoId(userName, currentClient);

            if (!id.equals("E")) {
                currentClient.getTransactionManager().undoLatestTransaction(id);

                System.out.println(userName + "'s" + " latest transaction on their " + id + " account is undone.");

                String option = exitQuestionForUndo();
                if (option.equals("A")) {
                    createNewAccountManually();
                }
            }
        }
    }

    private String exitQuestionForUndo() {
        System.out.println("Enter 'A' to undo another transaction.");
        System.out.println("Enter anything else to go back to main menu.");
        System.out.println("Enter here: ");
        return userInput.nextLine();
    }

    private String getUndoId(String userName, Client currentClient) {
        System.out.println("You can undo " + userName + "'s latest transaction on one of their following accounts; \n");

        List<String> idList = displayTransactions(currentClient);

        System.out.println("Please enter the desired account's Id: ");
        System.out.println("Enter 'E' to exit to main menu.");
        System.out.println("Enter here: ");
        String id = userInput.nextLine();

        while (!idList.contains(id) && !id.equals("E")) {
            id = rePrompt();
        }

        return id;
    }

    private List<String> displayTransactions(Client currentClient) {
        List<String> idList = new ArrayList<>();
        List<Account> accountsList = currentClient.getAccountList();
        int i = 0;
        for (Account account : accountsList) {
            System.out.println(account.getClass().getSimpleName() + " Account, Id: " + account.getId());
            if (currentClient.getTransactionManager().getLatestTransaction(account.getId()).getSenderUsername() != null) {
                System.out.println("Latest transaction: " + "\n" +
                        currentClient.getTransactionManager().getLatestTransaction(account.getId()).toString());
                idList.add(i, account.getId());
                i++;
                System.out.println("\n");
            } else {
                System.out.println("No transactions. \n");
            }
        }
        return idList;
    }

    /**
     * MANAGE MANAGER INFO
     */

    private void displayChangePassword() {
        System.out.print("\n-----------------------------------------------\n");
        System.out.println("Current password: " + bankManager.getPassword());
        System.out.print("Please enter a new password: ");
        String newPassword = userInput.nextLine();
        while (newPassword.equals(bankManager.getPassword())) {
            System.out.println("Same as old password. Try again.");
            System.out.print("Please enter a new password: ");
            newPassword = userInput.nextLine();
        }
        bankManager.setPassword(newPassword);
    }

    /**
     * HELPERS
     */

    private boolean notAllNumeric(String option) {
        boolean result = false;
        for (int i = 0; i < option.length(); i++) {
            if (!Character.isDigit(option.charAt(i))) {
                result = true;
            }
        }
        return result;
    }

    private String indexToDollars(int i) {
        if (i == 0) {
            return "$50";
        } else if (i == 1) {
            return "$20";
        } else if (i == 2) {
            return "$10";
        } else {
            return "$5";
        }
    }

    private String rePrompt() {
        System.out.println("Invalid option. Please try again.");
        System.out.print("Enter here: ");
        return userInput.nextLine();
    }

    private String getUserName() {
        System.out.println("Enter 'E' to exit to main menu.");
        System.out.println("Enter here: ");
        String userName = userInput.nextLine();

        while (!bank.getUserManager().hasClient(userName) && !userName.equals("E")) {
            userName = rePrompt();
        }
        return userName;
    }
}
