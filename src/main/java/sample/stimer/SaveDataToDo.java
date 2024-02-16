package sample.stimer;

import javafx.scene.control.ListView;

import java.io.*;
import java.util.ArrayList;

public class SaveDataToDo {

    public static void newOpravilo(String predmet, String opravilo, ListView<String> toDdListView) {
        File f = new File("src/main/resources/"+predmet+".txt");

        try {
            // Create a new file if it doesn't exist


            BufferedReader file = new BufferedReader(new FileReader(f));
            StringBuffer inputBuffer = new StringBuffer();
            String line;

            while ((line = file.readLine()) != null) {
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }

            line = opravilo+"@0";
            inputBuffer.append(line);
            inputBuffer.append('\n');
            file.close();

            // write the new string with the replaced line OVER the same file
            FileOutputStream fileOut = new FileOutputStream(f);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();
            toDdListView.getItems().add(opravilo);

        } catch (IOException e) {
            System.out.println("Problem reading or creating file. " + f.getAbsolutePath());
            e.printStackTrace();
        }
    }

    public static void deleteOpravilo(String predmet, String opravilo, ListView<String> toDdListView) {
        File f = new File("src/main/resources/"+predmet+".txt");
        try {
            BufferedReader file = new BufferedReader(new FileReader(f));
            StringBuffer inputBuffer = new StringBuffer();
            String line;

            while ((line = file.readLine()) != null) {
                if (!line.contains(opravilo)) {
                    inputBuffer.append(line);
                    inputBuffer.append('\n');
                }
            }
            file.close();
            FileOutputStream fileOut = new FileOutputStream(f);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();
            toDdListView.getItems().remove(opravilo);

        } catch (Exception e) {
            System.out.println("Problem reading file. " + f.getAbsolutePath());
        }
    }

    public static String[] arrayOpravil(String predmet) {
        ArrayList<String> arrayListOpravil = new ArrayList<>();
        File f = new File("src/main/resources/"+predmet+".txt");
        try {
            BufferedReader file = new BufferedReader(new FileReader(f));
            String line;

            while ((line = file.readLine()) != null) {
                String[] newLineArray = line.split("@");
                arrayListOpravil.add(newLineArray[0]);

            }
            file.close();

        } catch (Exception e) {
            System.out.println("Problem reading file. " + f.getAbsolutePath());
            return null;
        }
        return arrayListOpravil.toArray(new String[0]);
    }

}
