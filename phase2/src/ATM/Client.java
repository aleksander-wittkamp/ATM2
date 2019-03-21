package ATM;

import java.util.ArrayList;
import java.util.List;

public class Client extends User implements HasAccounts{
    private List<Account> accountList;
    private List<CreditCard> accountCreditCardList;
    private List<LineOfCredit> accountLineOfCreditList;
    private List<Chequing> accountChequingList;
    private List<Savings> accountSavingsList;
    private BankManager bankManager;
    private TransactionManager transactionManager;

    Client(String username, BankManager bankManager) {
        this.username = username;

        this.accountCreditCardList = new ArrayList<>();
        this.accountCreditCardList.add(new CreditCard("1"));

        this.accountLineOfCreditList = new ArrayList<>();
        this.accountLineOfCreditList.add(new LineOfCredit("1"));

        Chequing firstChequing = new Chequing("1");
        firstChequing.setPrimaryAccount();
        this.accountChequingList = new ArrayList<>();
        this.accountChequingList.add(firstChequing);

        this.accountSavingsList = new ArrayList<>();
        this.accountSavingsList.add(new Savings("1"));

        this.accountList = new ArrayList<>();
        this.accountList.addAll(this.accountChequingList);
        this.accountList.addAll(this.accountSavingsList);
        this.accountList.addAll(this.accountCreditCardList);
        this.accountList.addAll(this.accountLineOfCreditList);

        this.bankManager = bankManager;

        this.transactionManager = new TransactionManager(bankManager.userManager);

    }

    // config set up
    Client(String username, String password, List<Account> accountList,
           List<Transaction> transactionList, BankManager bankManager) {
        super(username, password);
        this.accountList = accountList;
        distributeAccounts(accountList);
        this.bankManager = bankManager;
        this.transactionManager = new TransactionManager(bankManager.userManager);
        transactionManager.setTransactionList(transactionList);
    }

    /**
     * SETTERS AND GETTERS
     */

    List<LineOfCredit> getLineOfCreditList(){
        return this.accountLineOfCreditList;
    }

    List<CreditCard> getCreditCardList(){
        return this.accountCreditCardList;
    }

    List<Savings> getSavingsList(){
        return this.accountSavingsList;
    }

    List<Chequing> getChequingList(){
        return this.accountChequingList;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String newPassword){
        this.password = newPassword;
    }

    /**
     * ADD ACCOUNT METHODS
     */


    void addAccountList(Account newAccount){
        this.accountList.add(newAccount);
    }

    void addAccountCreditCardList(CreditCard newCreditCard){
        this.accountCreditCardList.add(newCreditCard);
    }

    void addAccountLineOfCreditList(LineOfCredit newLineOfCredit){
        this.accountLineOfCreditList.add(newLineOfCredit);
    }

    void addChequingList(Chequing newChequing){
        this.accountChequingList.add(newChequing);
    }

    void addSavingsList(Savings newSavings){
        this.accountSavingsList.add(newSavings);
    }

    /**
     * NEW ACCOUNT CREATION REQUEST, GOES TO BANK MANAGERS REQUEST LIST
     */

    void requestCreditCardAccountCreation(int newAccountId){
        bankManager.addAccountRequest(this.username, "creditcard", newAccountId);
    }

    void requestLineOfCreditAccountCreation(int newAccountId){
        bankManager.addAccountRequest(this.username, "lineofcredit", newAccountId);
    }

    void requestSavingsAccountCreation(int newAccountId){
        bankManager.addAccountRequest(this.username, "savings", newAccountId);
    }

    void requestChequingAccountCreation(int newAccountId){
        // Olivia removed willBePrimary parameter :D don't kill me
        //Precondition willBePrimary is either "Yes" or "No".
        bankManager.addAccountRequest(this.username, "chequing", newAccountId);
    }

    /**
     * CALCULATE NET TOTAL METHOD
     */

    double calculateNetTotal() {
        return calculateTotalAssets() - calculateTotalDebt();
    }

    private double calculateTotalDebt(){
        double totalDebt = 0;

        List<Debt> combineDebtAccounts = new ArrayList<>();
        combineDebtAccounts.addAll(accountCreditCardList);
        combineDebtAccounts.addAll(accountLineOfCreditList);

        for (Debt currDebtAccount : combineDebtAccounts) {
            totalDebt = totalDebt + currDebtAccount.getBalance();
        }

        return totalDebt;
    }

    private double calculateTotalAssets(){
        double totalAssets = 0;
        List<Asset> combineAssetAccounts = new ArrayList<>();
        combineAssetAccounts.addAll(accountChequingList);
        combineAssetAccounts.addAll(accountSavingsList);

        for (Asset currAssetAccount : combineAssetAccounts) {
            totalAssets = totalAssets + currAssetAccount.getBalance();
        }

        return totalAssets;
    }

    /*
    Adjust all the savings accounts for this client.
     */
    void adjustSavingsAccounts(){
        for (Savings thisSavings: accountSavingsList){
            thisSavings.balance = thisSavings.balance + thisSavings.balance * 0.001;
        }
    }


    List<Transaction> getTransactions() {
        return this.transactionManager.getTransactionList();
    }

    TransactionManager getTransactionManager() {
        return transactionManager;
    }

    /**
     * TRANSFER CALLS TO TRANSFER MANAGER
     */

    boolean withinUserTransfer(User user, String AccountId, Account toAccount, double amount) {
        Account fromAccount;
        int i;
        for (i = 0; i < accountList.size(); i++) {
            if (AccountId.equals(accountList.get(i).getId())) {
                fromAccount = accountList.get(i);
                return getTransactionManager().withinUserTransfer(user, fromAccount, toAccount, amount);
            }
        }
        return false;
    }

    boolean betweenUsersTransfer(User user, Account fromAccount,
                                 String receiverUsername, double amount) {
        if (bankManager.userManager.hasUser(receiverUsername)) {
            return getTransactionManager().betweenUsersTransfer(user, fromAccount, receiverUsername, amount);
        } else {
            return false;
        }
    }

    boolean toNonUserTransfer(User user, Account fromAccount, String receiver, double amount) {
        return getTransactionManager().toNonUserTransfer(user, fromAccount, receiver, amount);
    }

    /**
     * ATM RELATED CALLS TO MONEY MANAGER
     */

    void deposit(Account account) {
        // The moneyManager.deposit() method returns the $ value equivalent of the bills specified
        // in the deposits.txt file.
        int amount = bankManager.moneyManager.deposit();
        account.increase(amount);
    }

    boolean hasAccount(String AccountId){
        for (Account account : accountList) {
            if (AccountId.equals(account.getId())) {
                return true;
            }
        }
        return false;
    }

    boolean withdraw(int amount, Account targetAccount) {
        return bankManager.moneyManager.withdraw(targetAccount, amount);
    }

    /**
     * HELPERS
     */

    Account findAccount(String accountId) {
        for (Account account : accountList) {
            if (account.getId().equals(accountId)) {
                return account;
            }
        }
        //this wont be evaluated under normal conditions
        return getPrimaryAccount();
    }

    Chequing getPrimaryAccount() {
        Chequing primaryChequing = new Chequing("0"); // instantiating for placeholder purposes.

        for (Chequing thisChequing : accountChequingList) {
            if (thisChequing.isPrimary()) {
                primaryChequing = thisChequing;
                break;
            }
        }
        return primaryChequing;
    }

    List<Account> getAccountList() {
        return accountList;
    }

    private void distributeAccounts(List<Account> accounts) {
        accountCreditCardList = new ArrayList<>();
        accountLineOfCreditList = new ArrayList<>();
        accountChequingList = new ArrayList<>();
        accountSavingsList = new ArrayList<>();

        for (Account a : accounts) {
            if (a instanceof Chequing){
                accountChequingList.add((Chequing) a);
            } else if (a instanceof Savings){
                accountSavingsList.add((Savings) a);
            } else if (a instanceof LineOfCredit){
                accountLineOfCreditList.add((LineOfCredit) a);
            } else if (a instanceof CreditCard) {
                accountCreditCardList.add((CreditCard) a);
            }
        }
    }

    public boolean alreadyExists(String id) {
        boolean result = false;

        for(Account account : accountList){
            if (account.getId().equals(id)){
                result = true;
            }
        }
        return result;
    }

    /**
     * TO STRING
     */

    @Override
    public String toString() {
        return this.username;
    }
}

