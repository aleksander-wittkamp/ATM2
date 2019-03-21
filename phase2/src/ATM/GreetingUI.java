package ATM;

class GreetingUI extends GeneralUI {

    GreetingUI(Bank b){
        super(b);
    }

    @Override
    void displayGeneralOptions(){
        User currUser;

        System.out.println("Welcome to TreeBank!");
        currUser = login();

        if (currUser instanceof Client) {
            ClientUI clientUI = new ClientUI(bank, currUser);
            clientUI.displayGeneralOptions();
        } else if (currUser instanceof BankManager) {
            BankManagerUI bankManagerUI = new BankManagerUI(bank, currUser);
            bankManagerUI.displayGeneralOptions();
        } else if (currUser instanceof Employee) {
            EmployeeUI employeeUI = new EmployeeUI(bank, currUser);
            employeeUI.displayGeneralOptions();
        }
    }

    private User login() {

        UserManager userManager = bank.getUserManager();

        System.out.println("\nPlease provide your username and password.");
        System.out.print("Enter username: ");
        String username = userInput.nextLine();

        while (!userManager.hasUser(username)) {
            System.out.println("We're sorry, that username isn't in our system. Please try again.");
            System.out.print("Enter username: ");
            username = userInput.nextLine();
        }

        System.out.print("Enter password: ");
        String password = userInput.nextLine();

        while (!userManager.isValidPassword(username, password)) {
            System.out.println("That password doesn't match the username. Please try again.");
            System.out.print("Enter password: ");
            password = userInput.nextLine();
        }

        return userManager.getUser(username);
    }
}
