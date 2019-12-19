package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class readCsv {

    ArrayList<ArrayList<Integer>> csv_data = new ArrayList<ArrayList<Integer>>();

    public void readCsv() {

        Scanner scanner = null;

        //ArrayList<Integer> collection = new ArrayList<Integer>();

        try {
            //Πρεπει να οριστει το path του csv
            scanner = new Scanner(new File("Resources/input_matrix_non_zeros.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int i = 0;

        ArrayList<String> line = new ArrayList<String>();

        while( scanner.hasNextLine() ){
            line.add(scanner.nextLine().trim());
        }

        for( i = 0; i < line.size(); i++ ) {

            String triplets[] = line.get(i).split(",");
            ArrayList<Integer> collection = new ArrayList<Integer>();

            for(int j = 0; j < triplets.length; j++) {
                //System.out.print(Integer.parseInt(triplets[j].trim()));
                collection.add(Integer.parseInt(triplets[j].trim()));
            }

            addTripletsToArray(collection);
        }
        scanner.close();
    }

    public ArrayList<ArrayList<Integer>> addTripletsToArray( ArrayList<Integer> array ) {
        csv_data.add(array);
        return csv_data;
    }

    public ArrayList<ArrayList<Integer>> getAllDataFromCsvFile() {
        return csv_data;
    }
}
