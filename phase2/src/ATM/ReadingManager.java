package ATM;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class ReadingManager {

    private static String directory = (
            System.getProperty("user.dir")
                    //Gives path up until the file containing phase1, this may differ for different computers
                    + "/phase2/src/IO_Files/");
    //Gives the rest of the path up until filename, this may not differ for any computer.
    int[] readDeposit() {
        int [] billsDeposited = new int[4];

        Path path = Paths.get(directory + "deposits.txt");
        try (BufferedReader fileInput = Files.newBufferedReader(path)) {
            String line = fileInput.readLine();
            int i = 0;
            while (line != null && i < 4) {
                int amountBills = extractAmount(line);
                billsDeposited[i] = amountBills;
                line = fileInput.readLine();
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return billsDeposited;
    }

    private int extractAmount(String line) {
        String[] content = line.split(":");
        return Integer.valueOf(content[1].substring(1, content[1].length() - 1));
    }

    private int extractBills(String line) {
        String[] content = line.split(" ");
        return Integer.valueOf(content[0].substring(1));
    }

    boolean [] getIsLow() {
        boolean [] isLow = new boolean[4];
        Path path = Paths.get(directory + "alerts.txt");
        try (BufferedReader fileInput = Files.newBufferedReader(path)) {
            String line = fileInput.readLine();
            while (line != null) {
                int value = extractBills(line);
                processValue(isLow, value);
                line = fileInput.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isLow;
    }

    private void processValue(boolean[] isLow, int value) {
        if (value == 50){
            isLow[0] = true;
        } else if (value == 20){
            isLow[1] = true;
        } else if (value == 10){
            isLow[2] = true;
        } else if (value == 5){
            isLow[3] = true;
        }
    }

    void deleteDeposits() {
        File file = new File(directory + "deposits.txt");
        file.delete();
    }
}
