package ATM;

public class Chequing extends Asset {

    private boolean primary;

    // initial set up
    public Chequing(String accountId){
        super();
        this.id = "Chequing" + accountId;
        //numChequingAccounts++;
        this.primary = false;
    }

    // config set up
    public Chequing(String id, double balance, String creationDate, boolean primary){
        super(id, balance, creationDate);
        this.primary = primary;
    }

    boolean isPrimary(){return this.primary;}

    void setPrimaryAccount(){this.primary = true;}

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    @Override
    public boolean sufficientFundsToTransfer(double amount){
        if ((this.balance >= 0) && ((this.balance - amount) >= -100)){
            return true;
        } else{
            return false;
        }
    }


}
