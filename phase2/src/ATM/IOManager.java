package ATM;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// There's an IO method in BankManagerUI that needs to be moved here.

class IOManager {

    private static File directory = new File("phase2/src/IO_Files/directory.txt");


    /**
     * Reads directory.txt to set up Users, Accounts, Transactions, and the MoneyManager.
     */
    static Bank readConfig() {

        UserManager userManager = new UserManager();
        MoneyManager moneyManager = new MoneyManager();

        if (checkForEmptyConfig()) {
            setLoginAndAddBankManager("Manager", "@");

        } else {

            try {

                // Setting up the reader.
                String line;
                FileReader fileReader = new FileReader(directory);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                while ((line = bufferedReader.readLine()) != null) {

                    switch (line) {
                        // Setting up the BankManager.
                        case "BANKMANAGER START":
                            setUpBankManager(bufferedReader.readLine());
                            bufferedReader.readLine();
                            break;

                        // Setting up a User, their Accounts, and their Transactions.
                        case "USER START":
                            ArrayList<String[]> objectData = new ArrayList<>();
                            while (!((line = bufferedReader.readLine()).equals("USER END"))) {
                                objectData.add(line.split(" "));
                            }
                            makeUser(objectData);
                            break;

                        // Setting up the MoneyManager.
                        case "MONEYMANAGER START":
                            inputBillCounts(bufferedReader.readLine());
                            bufferedReader.readLine();
                            break;
                    }
                }
                bufferedReader.close();
            }

            // Error handling.
            catch (FileNotFoundException ex) {
                System.out.println("Unable to open directory file.");
            } catch (IOException ex) {
                System.out.println("Error reading directory file.");
            }
        }
        return new Bank(userManager, moneyManager);
    }


    /**
     * Overwrites directory.txt with updated info for Users, Accounts, and the MoneyManager.
     */
    static void writeConfig() {

        try {

            // Setting up the writer.
            FileWriter fileWriter = new FileWriter(directory);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            //Writing BankManager data.
            for (String s : getBankManagerData()) {
                bufferedWriter.write(s);
            }

            // Gathering Users.
            Collection<User> users = userManager.getUsers();

            for (User u : users) {

                if (u instanceof BankManager) {
                    continue;
                }

                // Writing general User data.
                for (String s : getUserData(u)) {
                    bufferedWriter.write(s);
                }

                if (u instanceof Client) {
                    Client client = (Client) u;

                    // Writing the Client's Accounts.
                    bufferedWriter.write("ACCOUNTS START" + "\n");
                    List<Account> accounts = client.getAccountList();
                    for (Account a : accounts) {
                        for (String s : getAccountData(a)) {
                            bufferedWriter.write(s);
                        }
                    }
                    bufferedWriter.write("ACCOUNTS END" + "\n");

                    // Writing the Client's Transfers.
                    bufferedWriter.write("TRANSACTIONS START" + "\n");
                    List<Transaction> transactions = client.getTransactions();
                    for (Transaction t : transactions) {
                        for (String s : getTransactionData(t)) {
                            bufferedWriter.write(s);
                        }
                    }
                    bufferedWriter.write("TRANSACTIONS END" + "\n");

                    bufferedWriter.write("USER END" + "\n");
                }
            }

            // Writing the Money Manager.
            for (String s : getMoneyManagerData()) {
                bufferedWriter.write(s);
            }

            bufferedWriter.close();
        }

        // Error handling.
        catch (IOException ex) {
            System.out.println("Error writing to directory.");
        }
    }

    static boolean [] getIsLow() {
        boolean [] isLow = new boolean[4];
        Path path = Paths.get(directory + "alerts.txt");
        try (BufferedReader fileInput = Files.newBufferedReader(path)) {
            String line = fileInput.readLine();
            while (line != null) {
                int value = extractBills(line);
                processValue(isLow, value);
                line = fileInput.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isLow;
    }

    private static int extractAmount(String line) {
        String[] content = line.split(":");
        return Integer.valueOf(content[1].substring(1, content[1].length() - 1));
    }

    private static int extractBills(String line) {
        String[] content = line.split(" ");
        return Integer.valueOf(content[0].substring(1));
    }

    private static void setUpBankManager(String data) {
        String[] managerData = data.split(" ");
        setLoginAndAddBankManager(managerData[1], managerData[2]);
    }

    private static void processValue(boolean[] isLow, int value) {
        if (value == 50){
            isLow[0] = true;
        } else if (value == 20){
            isLow[1] = true;
        } else if (value == 10){
            isLow[2] = true;
        } else if (value == 5){
            isLow[3] = true;
        }
    }

    private static void setLoginAndAddBankManager(String username, String password){
        bankManager.setUsername(username);
        bankManager.setPassword(password);
        userManager.addUser(bankManager);
    }

    private static List<String> getBankManagerData() {
        ArrayList<String> toReturn = new ArrayList<>();

        toReturn.add("BANKMANAGER START" + "\n");
        String userClass = String.valueOf(bankManager.getClass()).substring(10);
        toReturn.add(userClass + " " + bankManager.getUsername() + " " + bankManager.getPassword() + "\n");
        toReturn.add("BANKMANAGER END" + "\n");

        return toReturn;
    }

    private static List<String> getUserData(User u) {
        ArrayList<String> toReturn = new ArrayList<>();

        toReturn.add("USER START" + "\n");
        String userClass = String.valueOf(u.getClass()).substring(10);
        toReturn.add(userClass + " " + u.getUsername() + " " + u.getPassword() + "\n");

        return toReturn;
    }

    private static List<String> getTransactionData(Transaction t) {
        ArrayList<String> toReturn = new ArrayList<>();

        if (t.getType() == 1) {
            toReturn.add(t.getType() + " " + t.getSenderUsername() + " "
                    + t.getFromAccountId() + " " + t.getToAccountId() + " " + t.getAmount() + "\n");
        } else if (t.getType() == 2) {
            toReturn.add(t.getType() + " " + t.getSenderUsername() + " " + t.getFromAccountId() + " "
                    + t.getReceiverUsername() + " " + t.getToAccountId() + " " + t.getAmount() + "\n");
        } else {
            toReturn.add(t.getType() + " " + t.getSenderUsername() + " "
                    + t.getFromAccountId() + " " + t.getNonUserReceiver() + " " + t.getAmount() + "\n");
        }

        return toReturn;
    }

    private static List<String> getAccountData(Account a) {
        ArrayList<String> toReturn = new ArrayList<>();
        String accountClass = String.valueOf(a.getClass()).substring(10);

        // For Chequing Accounts.
        if (a instanceof Chequing) {
            toReturn.add(accountClass + " " + a.getId() + " " + a.getCreationDate()
                    + " " + a.getBalance() + " " + ((Chequing) a).isPrimary() + "\n");

        // For all other Accounts.
        } else {
            toReturn.add(accountClass + " " + a.getId() + " " + a.getCreationDate()
                    + " " + a.getBalance() + "\n");
        }

        return toReturn;
    }

    /**
     * Gets the MoneyManager data.
     * @return List of strings representing bill counts:
     *         [fifties, twenties, tens, fives]
     */
    private static List<String> getMoneyManagerData() {
        int[] money = moneyManager.getMoney();
        ArrayList<String> toReturn = new ArrayList<>();

        toReturn.add("MONEYMANAGER START" + "\n");
        toReturn.add(money[0] + " " + money[1] + " " + money[2] + " " + money[3] + "\n");
        toReturn.add("MONEYMANAGER END" + "\n");

        return toReturn;
    }

    /**
     * Checks to see if directory.txt is empty.
     * @return true if empty
     */
    private static boolean checkForEmptyConfig() {
        return directory.length() == 0;
    }

    /**
     * Makes a User, its accounts, and its transfers, then adds the User to UserManager.
     * @param userData This ArrayList is arranged in the following way:
     *                 [User type, username, password]
     *                 [ACCOUNTS START]
     *                 ...account info, one account per line...
     *                 [ACCOUNTS END]
     */
    private static void makeUser(ArrayList<String[]> userData) {
        if (userData.get(0)[0].equals("Client")) {
            String username = userData.get(0)[1];
            String password = userData.get(0)[2];
            ArrayList<Account> accounts = new ArrayList<>();
            ArrayList<Transaction> transactions = new ArrayList<>();

            int i = 2;
            while (!(userData.get(i)[0].equals("ACCOUNTS"))) {
                accounts.add(makeAccount(userData.get(i)));
                i++;
            }

            i = i + 2;
            while (!(userData.get(i)[0].equals("TRANSACTIONS"))) {
                transactions.add(makeTransaction(userData.get(i)));
                i++;
            }

            Client newClient = new Client(username, password, accounts, transactions, bankManager);

            userManager.addUser(newClient);
        }
    }

    /**
     * Makes an Account.
     *
     * @param accountData This is a String[] of the following form:
     *                    [Account type, id, creationDate, balance, (primary)]
     * @return an Account
     */
    private static Account makeAccount(String[] accountData) {
        String type = accountData[0];
        String id = accountData[1];
        String creationDate = accountData[2];
        double balance = Double.parseDouble(accountData[3]);
        Account toReturn;

        switch (type) {
            case "Chequing":
                boolean primary = Boolean.parseBoolean(accountData[4]);
                toReturn = new Chequing(id, balance, creationDate, primary);
                break;
            case "Savings":
                toReturn = new Savings(id, balance, creationDate);
                break;
            case "CreditCard":
                toReturn = new CreditCard(id, balance, creationDate);
                break;
            default:
                toReturn = new LineOfCredit(id, balance, creationDate);
                break;
        }
        return toReturn;
    }

    /**
     * Makes a Transaction.
     *
     * @param transactionData This is a String[] of one of three forms:
     *                        Type 1 is [type, senderUsername, fromAccountId,
     *                        toAccountId, amount]
     *                        Type 2 is [type, senderUsername, fromAccountId,
     *                        receiverUsername, toAccountId, amount]
     *                        Type 3 is [type, senderUsername, fromAccountId,
     *                        nonUserReceiver, amount]
     * @return a Transaction
     */
    private static Transaction makeTransaction(String[] transactionData) {
        int type = Integer.parseInt(transactionData[0]);
        String senderUsername = transactionData[1];
        String fromAccountId = transactionData[2];
        String toAccountId;
        double amount;
        Transaction toReturn;

        switch (type) {
            case 1:
                toAccountId = transactionData[3];
                amount = Double.parseDouble(transactionData[4]);
                toReturn = new Transaction(senderUsername, fromAccountId, toAccountId, amount);
                break;
            case 2:
                String receiverUsername = transactionData[3];
                toAccountId = transactionData[4];
                amount = Double.parseDouble(transactionData[5]);
                toReturn = new Transaction(senderUsername, fromAccountId, receiverUsername, toAccountId, amount);
                break;
            default:
                String nonUserReceiver = transactionData[3];
                amount = Double.parseDouble(transactionData[4]);
                toReturn = new Transaction(amount, senderUsername, fromAccountId, nonUserReceiver);
                break;
        }

        return toReturn;
    }

    /**
     * Sets initial bill count for the MoneyManager.
     *
     * @param data "fifties twenties tens fives"
     */
    private static void inputBillCounts(String data) {
        String[] billTotals = data.split(" ");
        moneyManager.setFifties(Integer.parseInt(billTotals[0]));
        moneyManager.setTwenties(Integer.parseInt(billTotals[1]));
        moneyManager.setTens(Integer.parseInt(billTotals[2]));
        moneyManager.setFives(Integer.parseInt(billTotals[3]));
    }

//    /**
//     * FOR TESTING readConfig. NOT PERMANENT.
//     */
//     public static void main(String[] args) {
//         UserManager uM = new UserManager();
//
//         MoneyManager mM = new MoneyManager();
//
//         BankManager bM = new BankManager(uM, mM);
//
//         // Make a client c1 who has two accounts and two transactions.
//         ArrayList<Account> accs1 = new ArrayList<>();
//         Chequing cheq1 = new Chequing("cheq1", 400, "date11", true);
//         Savings sav1 = new Savings("sav1", 600, "date12");
//         accs1.add(cheq1);
//         accs1.add(sav1);
//         ArrayList<Transaction> transactions1 = new ArrayList<>();
//         Transaction trans11 = new Transaction("BobSmith", "cheq1",
//                 "sav1", 100);
//         Transaction trans12 = new Transaction("BobSmith", "cheq1",
//                 "AlicePots", "cheq1", 100);
//         transactions1.add(trans11);
//         transactions1.add(trans12);
//         Client c1 = new Client("BobSmith", "123", accs1, transactions1, bM);
//         uM.addUser(c1);
//
//         // Make a client c2 who has two accounts and two transactions.
//         ArrayList<Account> accs2 = new ArrayList<>();
//         Chequing cheq2 = new Chequing("cheq1", 300, "date21", true);
//         Savings sav2 = new Savings("sav1", 800, "date22");
//         accs2.add(cheq2);
//         accs2.add(sav2);
//         ArrayList<Transaction> transactions2 = new ArrayList<>();
//         Transaction trans21 = new Transaction("AlicePots", "sav1",
//                 "cheq1", 200);
//         Transaction trans22 = new Transaction("AlicePots", "cheq1",
//                 "BobSmith", "cheq1", 300);
//         transactions2.add(trans21);
//         transactions2.add(trans22);
//         Client c2 = new Client("AlicePots", "456", accs2, transactions2, bM);
//         uM.addUser(c2);
//
//
//         bM.setUsername("JoeNesbo");
//         bM.setPassword("apples");
//
//         IOManager cM = new IOManager(uM, mM, bM);
//
//         cM.inputBillCounts("40 40 40 40");
//
//         cM.writeConfig();
//     }

//    /**
//     * FOR TESTING writeConfig. NOT PERMANENT.
//     */
//
//
//        public static void main(String[] args) {
//        UserManager uM = new UserManager();
//
//        MoneyManager mM = new MoneyManager();
//
//        BankManager bM = new BankManager(uM, mM);
//
//        IOManager cM = new IOManager(uM, mM, bM);
//        cM.readConfig();
//
//        cM.writeConfig();
//    }
}