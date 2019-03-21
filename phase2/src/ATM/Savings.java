package ATM;

public class Savings extends Asset {

    // initial set up
    public Savings(String accountId) {
        super();
        this.id = "Savings" + accountId;
    }

    // config set up
    public Savings(String id, double balance, String creationDate){
        super(id, balance, creationDate);
    }

}
