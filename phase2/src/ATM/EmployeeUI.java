package ATM;

class EmployeeUI extends IsUserUI {
    private Employee currEmployee;

    EmployeeUI(Bank b, User u) {
        super(b, u);
        currEmployee = (Employee) currUser;
    }

    @Override
    void displayGeneralOptions() {

    }
}
