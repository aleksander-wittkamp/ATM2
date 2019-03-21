package ATM;

public abstract class User {
    String username;
    String password;

    public User(){}

    public User(String username, String password){
        setUsername(username);
        setPassword(password);
    }

    void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }
}
