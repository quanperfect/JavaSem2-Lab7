package Client.Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileController {
    private String TXTfilepath = "C:\\Users\\H-PC\\IdeaProjects\\Prog_Lab7\\src\\main\\java\\Client\\";
    private String HELPfilepath = "C:\\Users\\H-PC\\IdeaProjects\\Prog_Lab7\\src\\main\\java\\Client\\ApplicationData\\help.txt";

    public ArrayList<String> readScriptFileAsCommandsList(String fileName) {
        ArrayList<String> Commands = new ArrayList<>();
        Commands.add("Wrong fileName");
        String lastFourSymbols = "";
        if (fileName.length() > 4) {
            lastFourSymbols = fileName.substring(fileName.length() - 4);
            System.out.println(lastFourSymbols);
        }
        if (!lastFourSymbols.equals(".txt")) {
            fileName = fileName + ".txt";
        }
        String thisTXTfilepath = TXTfilepath+fileName;

        try {
            Scanner input = new Scanner(System.in);
            File scriptTxt = new File(thisTXTfilepath);
            //System.out.println(thisTXTfilepath);
            input = new Scanner(scriptTxt);

            while (input.hasNextLine()) {
                String line = input.nextLine();
                Commands.add(line);
            }
            input.close();

            if (Commands.size() > 1) {
                Commands.set(0, "OK");
            } else {
                Commands.set(0, "File empty");
            }

        } catch (FileNotFoundException e) {
            Commands.set(0, "File not found");
            System.out.println("File not found!");
        } catch (SecurityException e2) {
            Commands.set(0, "No access to file");
            System.out.println(e2.getMessage());
        } catch (Exception e3) {
            Commands.set(0, "Unknown exception");
            System.out.println(e3.getMessage());
        }

        return Commands;
    }

    public ArrayList<String> readHelpFileAsList() {
        ArrayList<String> Lines = new ArrayList<>();

        try {
            Scanner input = new Scanner(System.in);
            File helpTxt = new File(HELPfilepath);
            input = new Scanner(helpTxt);

            while (input.hasNextLine()) {
                String line = input.nextLine();
                Lines.add(line);
            }
            input.close();

            if (Lines.size() < 1) {
                System.out.println("Help file is empty. Check your application integrity.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Help file not found. Check your application integrity.");
        } catch (SecurityException e2) {
//            System.out.println(e2.getMessage());
        } catch (Exception e3) {
//            System.out.println(e3.getMessage());
        }

        return Lines;
    }
}
