package ATM;

public abstract class Debt extends Account {

    // initial set up
    public Debt(){
        super();
    }

    // config file set up
    public Debt(String id, double balance, String creationDate){
        super(id, balance, creationDate);
    }



    //public abstract void setBalance(double balance);
}
