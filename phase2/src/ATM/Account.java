package ATM;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;

public abstract class Account extends Observable {
    private String creationDate;
    //TODO consider fixing:
    // As it stands, when you create 3 users, user 3 has id=3 for every first account
    //static int numCreditCardAccounts = 1;
    //static int numLineOfCreditAccounts = 1;
    //static int numChequingAccounts = 1;
    //static int numSavingsAccounts = 1;
    String id; // we should keep this as package private.
    double balance;

    // initial set up
    public Account(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        Date date = new Date();
        this.creationDate = dateFormat.format(date);
        this.balance = 0;
    }


    // config set up
    public Account(String id, double balance, String creationDate){
        this.id = id;
        this.balance = balance;
        this.creationDate = creationDate;
    }

    public void setBalance(double balance){ this.balance = balance; }
    public double getBalance(){return this.balance;}

    public void increase(double amount){this.balance += amount;} //Increase balance by the amount specified
    public void decrease(double amount){this.balance -= amount;} //Decrease balance by the amount specified

    public void setId(String id){ this.id = id; }
    public String getId(){ return this.id; }

    String getCreationDate(){
        return this.creationDate;
    }

    public boolean sufficientFundsToTransfer(double amount){ return (this.balance >= amount);}

    public String toString(){
        return this.id;
    }

}
