package sample.stimer;

import javafx.scene.control.ListView;

import java.io.*;
import java.util.ArrayList;

public class SaveDataToDo {

    public static void newOpravilo(String predmet, String opravilo, ListView<String> toDdListView) {
        File f = new File("src/main/resources/"+predmet+".txt");

        try {
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
                if (newLineArray[1].equals("1")) {
                    arrayListOpravil.add("1 " +newLineArray[0]);
                } else {
                    arrayListOpravil.add(newLineArray[0]);
                }

            }
            file.close();

        } catch (Exception e) {
            System.out.println("Problem reading file. " + f.getAbsolutePath());
            return null;
        }
        return arrayListOpravil.toArray(new String[0]);
    }


    public static void checkUncheck (String predmet, String currentOpravilo, ListView<String> toDdListView) {
        String opravilo = currentOpravilo.replace("1","").trim();
        System.out.println(opravilo);
        File f = new File("src/main/resources/"+predmet+".txt");

        try {
            BufferedReader file = new BufferedReader(new FileReader(f));
            StringBuffer inputBuffer = new StringBuffer();
            String line;

            while ((line = file.readLine()) != null) {
                if (line.contains(opravilo)) {
                    String[] newLineArray = line.split("@");
                    if (newLineArray[1].equals("1")) {
                        line = opravilo+"@0";
                    } else {
                        line = opravilo+"@1";
                    }
                }
                inputBuffer.append(line);
                inputBuffer.append('\n');

            }
            file.close();

            // write the new string with the replaced line OVER the same file
            FileOutputStream fileOut = new FileOutputStream(f);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();
            toDdListView.getItems().clear();
            toDdListView.getItems().addAll(SaveDataToDo.arrayOpravil(predmet));


        } catch (IOException e) {
            System.out.println("Problem reading or creating file. " + f.getAbsolutePath());
            e.printStackTrace();
        }
    }

}
