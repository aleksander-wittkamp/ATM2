package ATM;

import java.util.ArrayList;
import java.util.List;

/*
TransactionManager class keeps track of all the transactions made by a client.
 */

class TransactionManager {

    private List<Transaction> transactionList;
    private UserManager userManager;


    TransactionManager(UserManager userManager) {
        this.transactionList = new ArrayList<>();
        this.userManager = userManager;
    }

    /**
     * SETTERS & GETTERS
     */


    void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    List<Transaction> getTransactionList() {
        return this.transactionList;
    }


    /**
     * TRANSFER METHODS
     */

    boolean withinUserTransfer(User user, Account fromAccount, Account toAccount, double amount) {
        if (fromAccount.sufficientFundsToTransfer(amount)) {
            Transaction t = new Transaction(user.getUsername(), fromAccount.getId(), toAccount.getId(), amount);
            fromAccount.decrease(amount);
            toAccount.increase(amount);
            transactionList.add(t);
            return true;
        } else {
            return false;
        }
    }

    boolean betweenUsersTransfer(User user, Account fromAccount,
                                 String receiverUsername, double amount) {
        Client receiver = userManager.getClient(receiverUsername);
        Account toAccount = userManager.getPrimaryAccount(receiverUsername);
        if (fromAccount.sufficientFundsToTransfer(amount)) {
            Transaction t = new Transaction(user.getUsername(), fromAccount.getId(),
                    receiver.getUsername(), toAccount.getId(), amount);
            fromAccount.decrease(amount);
            toAccount.increase(amount);
            transactionList.add(t);
            receiver.getTransactionManager().transactionList.add(t);
            return true;
        } else {
            return false;
        }
    }

    boolean toNonUserTransfer(User user, Account fromAccount, String receiver, double amount) {
        ReportManager reportManager = new ReportManager();
        if (fromAccount.sufficientFundsToTransfer(amount)) {
            Transaction t = new Transaction(amount, user.getUsername(), fromAccount.getId(), receiver);
            fromAccount.decrease(amount);
            transactionList.add(t);
            reportManager.sendOutgoing(user.toString(), receiver, amount);
            return true;
        } else { return false;
        }
    }

    /**
     * UNDO TRANSACTION METHODS FOR BANK MANAGER
     */


    void undoLatestTransaction(String accountId) {
        Transaction latestTransaction = getLatestTransaction(accountId);
        undoTransaction(latestTransaction);
    }

    Transaction getLatestTransaction(String accountId) {
        for (Transaction currentTransaction : transactionList) {
            if (currentTransaction.getFromAccountId().equals(accountId)) {
                return currentTransaction;
            }
        }
        //Never gets reached
        return new Transaction();
    }

    private void undoTransaction(Transaction latestTransaction) {
        Client fromClient = userManager.getClient(latestTransaction.getSenderUsername());
        Account fromAccount = fromClient.findAccount(latestTransaction.getFromAccountId());
        Client toClient = userManager.getClient(latestTransaction.getReceiverUsername());
        Account toAccount = toClient.findAccount(latestTransaction.getToAccountId());

        double amount = latestTransaction.getAmount();

        fromAccount.increase(amount);
        toAccount.decrease(amount);
        deleteTransaction(latestTransaction);
    }

    private void deleteTransaction(Transaction latestTransaction) {
        int latestTransactionIndex = 0;
        for (int i = 0; i < transactionList.size(); i++) {
            if (transactionList.get(i).equals(latestTransaction)) {
                latestTransactionIndex = i;
            }
        }
        transactionList.remove(latestTransactionIndex);
    }
}