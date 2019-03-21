package ATM;

public class Bank {
    private UserManager userManager;
    private MoneyManager moneyManager;

    Bank(UserManager um, MoneyManager mm) {
        userManager = um;
        moneyManager = mm;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public MoneyManager getMoneyManager() {
        return moneyManager;
    }
}