package ATM;

public class LineOfCredit extends Debt {

    // initial set up
    public LineOfCredit(String accountId){
        super();
        this.id = "LineOfCredit" + accountId;
    }

    // config set up
    public LineOfCredit(String id, double balance, String creationDate) {
        super(id, balance, creationDate);
    }

    public void setBalance(double balance){ this.balance = balance; }

    @Override
    // increase and decrease in debt accounts mean different things.
    public void increase(double amount) { this.balance -= amount;}

    @Override
    public void decrease(double amount) { this.balance += amount;}

    @Override
    public boolean sufficientFundsToTransfer(double amount) {
        return true;
    }

    /*
    //TODO TESTING ONLY REMOVE LATER
    public static void main(String[] args) {
        LineOfCredit a = new LineOfCredit(accountId);
        System.out.println(a);
    }
     */

}
