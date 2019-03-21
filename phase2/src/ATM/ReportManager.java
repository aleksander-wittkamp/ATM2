package ATM;

import java.io.*;

/*
ReportManager class creates alerts.txt, and outgoing.txt files
 */
class ReportManager {

    private static String directory = (
            System.getProperty("user.dir")
            //Gives path up until the file containing phase1, this may differ for different computers
            + "/phase2/src/IO_Files/");
    //Gives the rest of the path up until filename, this may not differ for any computer.

    private static int outgoingId;

    void sendAlert(int[] money){
        File file = new File(directory + "alerts.txt");
        try {file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
            String toWrite = prepareAlertContent(money);
            out.print(toWrite);} catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String prepareAlertContent(int[] money) {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < money.length; i++){
            if (money[i] < 20){
                if (i == 0){
                    s.append("$50 Bills are low: ");
                } else if (i == 1){
                    s.append("$20 Bills are low: ");
                } else if (i == 2){
                    s.append("$10 Bills are low: ");
                } else if (i == 3) {
                    s.append("$5 Bills are low: ");
                }
                s.append(money[i]);
                s.append("\n");
            }
        }

        return s.toString();
    }

    void sendOutgoing(String sender, String receiver, double amount){
         String fileName = "outgoing" + outgoingId + ".txt";
        File file = new File(directory + fileName);
        try {
            if(file.createNewFile()){
                System.out.println(fileName + " created in IO_Files");
            }else System.out.println("File " + fileName + " already exists in IO_Files");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
            String toWrite = sender + " has sent $" + amount + " to: " + receiver;
            out.print(toWrite);
        } catch (IOException e) {
            e.printStackTrace();
        }
        outgoingId ++;
    }

    void deleteAlerts() {
        File file = new File(directory + "alerts.txt");
        file.delete();
    }
}
