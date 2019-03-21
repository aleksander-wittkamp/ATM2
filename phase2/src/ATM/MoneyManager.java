package ATM;

class MoneyManager {

    private int[] money;
    private ReportManager reportManager;
    private ReadingManager readingManager;

    MoneyManager(){
        this.money = new int[4];
        this.reportManager = new ReportManager();
        this.readingManager = new ReadingManager();
    }

    /**
     * SETTERS AND GETTERS
     */

    void setFifties(int fifties){
        this.money[0] = fifties;
    }

    private int getFifties() {
        return this.money[0];
    }

    void setTwenties(int twenties){
        this.money[1] = twenties;
    }

    private int getTwenties() {
        return this.money[1];
    }

    void setTens(int tens){
        this.money[2] = tens;
    }

    private int getTens() {
        return this.money[2];
    }

    void setFives (int fives){
        this.money[3] = fives;
    }

    private int getFives() {
        return this.money[3];
    }

    int[] getMoney() {
        return money;
    }

    private int getTotalMoney(){
        return (getFifties() * 50 + getTwenties() * 20 + getTens() * 10 + getFives() * 5);
    }

    /**
     * DEPOSIT AND WITHDRAW METHODS
     */

    int deposit(){
        int[] toIncrease = readingManager.readDeposit();
        increase(toIncrease);
        readingManager.deleteDeposits();
        return toIncrease[0] * 50 + toIncrease[1] * 20 + toIncrease[2] * 10 + toIncrease[3] * 5;
    }

    boolean withdraw(Account fromAccount, int amount) {
        //Precondition, amount % 5 == 0.

        // This result says whether there are sufficient funds or no
        boolean result;

        if (sufficientMoney(amount)){
            if (fromAccount.sufficientFundsToTransfer(amount)){
                decrease(bestCombinationOfBills(amount, getMoney()));
                fromAccount.decrease(amount);
                result = true;}
            else{result = false;}
        } else{
            result = false;
        }
        if (lowOnMoney()){
            reportManager.sendAlert(money);
        }
        return result;
    }

    /**
     * HELPERS
     */

    private int[] bestCombinationOfBills(int amount, int[] money) {
        int fifties = 0;
        int twenties = 0;
        int tens = 0;
        int fives = 0;

        while (amount >= 50 && fifties < money[0]) {
            fifties ++;
            amount -= 50;
        }
        while (amount >= 20 && twenties < money[1]) {
            twenties ++;
            amount -= 20;
        }
        while (amount >= 10 && tens < money[2]) {
            tens ++;
            amount -= 10;
        }
        while (amount >= 5 && fives < money[3]) {
            fives ++;
            amount -= 5;
        }

        return new int[]{fifties, twenties, tens, fives};
    }

    void increase(int[] billsToIncrease) {
        int fiftiesToIncrease = billsToIncrease[0];
        int twentiesToIncrease = billsToIncrease[1];
        int tensToIncrease = billsToIncrease[2];
        int fivesToIncrease = billsToIncrease[3];

        setFifties(getFifties() + fiftiesToIncrease);
        setTwenties(getTwenties() + twentiesToIncrease);
        setTens(getTens() + tensToIncrease);
        setFives(getFives() + fivesToIncrease);
    }

    private void decrease(int[] billsToDecrease){
        int fiftiesToDecrease = billsToDecrease[0];
        int twentiesToDecrease = billsToDecrease[1];
        int tensToDecrease = billsToDecrease[2];
        int fivesToDecrease = billsToDecrease[3];

        setFifties(getFifties() - fiftiesToDecrease);
        setTwenties(getTwenties() - twentiesToDecrease);
        setTens(getTens() - tensToDecrease);
        setFives(getFives() - fivesToDecrease);
    }

    private boolean lowOnMoney(){
        for (int billNumber : money){
            if (billNumber < 20){
                return true;
            }
        }
        return false;
    }

    private boolean sufficientMoney(int amount){
        return amount <= getTotalMoney();
    }

    // Prints number of each bill after manager uploads bills.
    String representBills(){
        return "$50: " + getFifties() + "\n" +
                "$20: " + getTwenties() + "\n" +
                "$10: " + getTens() + "\n" +
                "$5: " + getFives();
    }

    void deleteAlerts() {
        reportManager.deleteAlerts();
    }
}