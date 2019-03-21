package ATM;

public abstract class Asset extends Account {
    public Asset(){
        super();
    }

    /*
    I don't see the point of this constructor unless it is for the read method.
    public Asset(String id, double balance){
        super(id, balance);
    }

    */

    public Asset(String id, double balance, String creationDate){
        super(id, balance, creationDate);
    }

}
