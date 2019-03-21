package ATM;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Starter {

    public static void main(String[] args) {

        Bank bank = IOManager.readConfig();

        while (true) {
            GreetingUI session = new GreetingUI(bank);
            session.displayGeneralOptions();
            IOManager.writeConfig();
        }
    }
}

//    // Time should be handled here.
//        /*
//    Returns true if the current day is the first of the current month.
//     */
//    private boolean firstOfMonth(){
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
//        Date date = new Date();
//        String currDate = dateFormat.format(date).substring(0,9);
//        if (currDate.equals("01")){
//            return true;
//        }
//        else{
//            return false;
//        }
//    }
//
//    private void adjustAllSavings(){
//        userManager.adjustAllSavings();
//    }
//
//    /*
//    Return true if it is 5 seconds till midnight.
//    */
//    private boolean isMidnightNear(){
//        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//        Date date = new Date();
//        String currTime = dateFormat.format(date);
//        if (currTime.equals("23:59:55")){
//            return true;
//        }
//        else{
//            return false;
//        }
//    }
