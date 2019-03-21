package ATM;

public class Employee extends User implements WorksHere, HasAccounts {

    public Employee(){}

    public Employee(String username, String password){
        super(username, password);
    }

}
