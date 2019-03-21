package ATM;

import java.util.ArrayList;
import java.util.List;

public class BankManager extends User implements WorksHere {

    private List<String[]> newAccountRequests;
    UserManager userManager;
    MoneyManager moneyManager;

    /**
     * 1) Create a login and set the initial password for a user .
     * 2) Increase the number of $5, $10, $20, and/or $50 bills in the machine to simulate restocking the machine
     *      ->This is handled in the interface.
     * 3) Lastly, the manager has the ability to undo the most recent transaction on any asset or debt account,
     * except for paying bills.
     *      ->This is handled in the interface
     */

    BankManager(UserManager userManager, MoneyManager moneyManager){
        this.userManager = userManager;
        this.moneyManager = moneyManager;
        this.newAccountRequests = new ArrayList<>();
    }

    void createClient(String username, String password, BankManager bankManager) {
        userManager.createClient(username, password, bankManager);
    }

    /**
     * SET CLIENT PASSWORD METHOD
     */

    public void setClientPassword(String username) {
        userManager.getClient(username).setPassword(password);
    }

    /**
     * CREATE NEW ACCOUNT METHOD
     */

    void createNewAccount(String username, String accountType, String newAccountId){
        Client thisClient = userManager.getClient(username);
        Account newAccount = null;

        switch (accountType){
            case "chequing":
                newAccount = new Chequing(newAccountId);
                thisClient.addChequingList((Chequing) newAccount);
                break;
            case "savings":
                newAccount = new Savings(newAccountId);
                thisClient.addSavingsList((Savings) newAccount);
                break;
            case "creditcard":
                newAccount = new CreditCard(newAccountId);
                thisClient.addAccountCreditCardList((CreditCard) newAccount);
                break;
            case "lineofcredit":
                newAccount = new LineOfCredit(newAccountId);
                thisClient.addAccountLineOfCreditList((LineOfCredit) newAccount);
                break;
        }

        if (newAccount != null){
            thisClient.addAccountList(newAccount);
        }
    }

    /**
     * NEW ACCOUNT REQUESTS
     */

    List<String[]> getNewAccountRequests() {
        return this.newAccountRequests;
    }

    void addAccountRequest(String username, String accountType, int newAccountId){
        this.newAccountRequests.add(new String[]{username, accountType, Integer.toString(newAccountId)});
    }

}