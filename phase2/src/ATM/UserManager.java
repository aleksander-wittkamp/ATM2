package ATM;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;


public class UserManager {
    // One master map to track all Users.
    // Keys are usernames. Values are Users.
    private HashMap<String, User> map;

    //TODO Reading from AND writing to file when this is a new program, or when we have saved data already

    public UserManager(){
        map = new HashMap<>();
        // This is creation from scratch. TODO Need a way to read from file too/ load the values into these maps
        //clientMap = new HashMap<String, String>();
        //employeeMap = new HashMap<String, String>();
    }


    void createClient(String username, String password, BankManager bankManager){
        //TODO below may not be good practice if BankManager is the one who is supposed to set the password.
        Client c = new Client(username, bankManager);
        map.put(username, c);
        getClient(username).setPassword(password);
        /*
        HashMap<String, Object> temp = new HashMap<String, Object>();
        temp.put("password", password);  I'm leaving this code in place for now.
        temp.put("type", "client");      It's what Stephen had before I changed map.
        temp.put("user", c);
        Creates a new Client Account;
        So this actually has to call the Client() constructor then no?
         */
    }

    //TODO after implementation of Employee
    /*
    public void createEmployee(String username, String password){
        employeeMap.put(username, password);
    }
     */

    //Returns whether a given username is included in container for users; for Transaction purposes
    boolean hasUser(String username){
        return (hasClient(username) || hasEmployee(username));
    }

    boolean hasClient(String username){
        if (map.containsKey(username)) {
            return map.get(username) instanceof Client;
        }
        return false;


        /*
        if (map.containsKey(username)){
            if (map.get(username).get("type") == "client") {
                return true;
            }
        }
        return false;
        return checkValidity(username,"Client");
         */

        // Sees if the given username is in the container for clients
    }

    User getUser(String username){
        return map.get(username);
    }


    private boolean hasEmployee(String username){
        //Sees if the given username is in the container for employees
//        return checkValidity(username,"Employee");
        if (map.containsKey(username)) {
            return map.get(username) instanceof Employee;
        }
        return false;
    }

    private boolean checkValidity(String username, Object whatToCheck){
        // Helper method to avoid duplicate code
        if (map.containsKey(username)){
            if (map.get(username).getClass() == whatToCheck) {
                return true;
            }
        }
        return false;
    }

    // Function for user/password valid combination checking. Ie useful for checking login information when the
    // main method is in state 0. Otherwise main method would have to reach into the fields in this class
    public boolean isValidLogin(String username, String password){
//        return checkValidity(username, "password", password);
        if (map.containsKey(username)) {
            return map.get(username).getPassword().equals(password);
        }
        return false;
    }


    Account getPrimaryAccount(String username){
        Client thisClient = getClient(username);
        return thisClient.getPrimaryAccount();
    }



    //Returns User given a username
    Client getClient(String username){
        //TODO where should the below safety check go?
//        if (checkValidity(username, "type", "client")) {
        Client thisClient = (Client) map.get(username);
        return (Client) map.get(username);
//        }

    }

    //TODO Implement once employee done.
    public Employee getEmployee(String username){
        return new Employee(); // placeholder for now.
    }

    /*
    Return true if a given password is valid given a particular username.
     */
    boolean isValidPassword(String username, String password){
        User thisUser = getUser(username);
        if (thisUser.getPassword().equals(password)){
            return true;
        }
        return false;
    }


    /*
    Adjusts the savings accounts for all users.
     */
    void adjustAllSavings(){
        for (User thisUser: map.values()){
            if (thisUser instanceof Client){
                Client thisClient = (Client) thisUser;
                thisClient.adjustSavingsAccounts();
            }
        }
    }


    void addUser(User user){
        map.put(user.getUsername(), user);
    }

    Collection<User> getUsers() {
        return map.values();
    }

}
