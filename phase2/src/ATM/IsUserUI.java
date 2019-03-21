package ATM;

abstract class IsUserUI extends GeneralUI {
    User currUser;
    boolean running = true;

    IsUserUI(Bank b, User u) {
        super(b);
        currUser = u;
    }

    void logout() {
        System.out.println("Have a great day!");
        System.out.print("\n-----------------------------------------------\n");
        this.running = false;
    }
}
