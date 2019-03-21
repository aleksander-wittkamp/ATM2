package ATM;

/*
Transaction class represents a single transaction made by a client.
 */

public class Transaction {
    private String senderUsername, receiverUsername, nonUserReceiver;
    private String fromAccountId, toAccountId;
    private int type;

    /**
     * TYPE 1: Within User
     * TYPE 2: Between Users
     * TYPE 3: To Non-User
     */


    double amount;
    int id;

    private static int numTransactions = 0;

    /**
     * OVERLOADED CONSTRUCTOR FOR THREE DIFFERENT TYPES OF TRANSFER
     */


    Transaction(String senderAndReceiverUsername, String fromAccountId, String toAccountId, double amount){
        this.senderUsername = senderAndReceiverUsername;
        this.fromAccountId = fromAccountId;
        this.receiverUsername = senderAndReceiverUsername;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.type = 1;
        this.id = numTransactions;
        numTransactions ++;
    }

    Transaction(String senderUsername, String fromAccountId, String receiverUsername, String toAccountId, double amount){
        this.senderUsername = senderUsername;
        this.fromAccountId = fromAccountId;
        this.receiverUsername = receiverUsername;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.type = 2;
        this.id = numTransactions;
        numTransactions ++;
    }

    Transaction(double amount, String senderUsername, String fromAccountId, String nonUserReceiver){
        this.senderUsername = senderUsername;
        this.fromAccountId = fromAccountId;
        this.nonUserReceiver = nonUserReceiver;
        this.amount = amount;
        this.type = 3;
        this.id = numTransactions;
        numTransactions ++;
    }

    Transaction(){}

    /**
     * SETTERS AND GETTERS
     */

    String getFromAccountId() {
        return fromAccountId;
    }

    String getToAccountId() {
        return toAccountId;
    }

    public double getAmount() {
        return this.amount;
    }

    int getType() {
        return this.type;
    }

    String getSenderUsername() {
        return senderUsername;
    }

    String getReceiverUsername() {
        return receiverUsername;
    }

    String getNonUserReceiver() {
        return this.nonUserReceiver;
    }

    private int getId() {
        return this.id;
    }


    @Override
    public String toString() {
        return this.senderUsername + " sent $" + this.amount +
                " from account " + this.fromAccountId + " to " + this.receiverUsername;
//        this.senderUsername = senderUsername;
//        this.fromAccountId = fromAccountId;
//        this.receiverUsername = receiverUsername;
//        this.toAccountId = toAccountId;
//        this.amount = amount;
//        this.type = 2;
//        this.id = numTransactions;
    }

    /**
     * EQUALS & TO STRING
     */

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        else if (! (obj instanceof Transaction)){
            return false;
        }
        else{
            return this.getId() == ((Transaction)obj).getId();
        }
    }
}
