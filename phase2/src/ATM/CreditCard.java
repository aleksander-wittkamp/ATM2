package ATM;

public class CreditCard extends Debt {

    // initial set up
    public CreditCard(String accountId){
        super();
        this.id = "CreditCard" + accountId;
        //numCreditCardAccounts++;
    }

    // config set up
    public CreditCard(String id, double balance, String creationDate) {
        super(id, balance, creationDate);
    }

    @Override
    // increase and decrease in debt accounts mean different things.
    public void increase(double amount) { this.balance -= amount;}

    @Override
    public void decrease(double amount) { this.balance += amount;}


    @Override
    public boolean sufficientFundsToTransfer(double amount) { return false; }



}
