package datastructure;

import core.readCsv;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.Random;

public class CFMaster implements Master {

    private Socket s;

    private RealMatrix C = null;
    private RealMatrix P = null;
    private RealMatrix R = null;
    private RealMatrix X = null;
    private RealMatrix Y = null;
    private double prevscore = 0;
    private double score = 0;
    private int K = 20;
    private int A = 40;
    private double L = 0.1;

    //open socket
    public void initialize(ArrayList<RealMatrix> rm) {

        X = rm.get(0);
        Y = rm.get(1);
        C = rm.get(2);
        P = rm.get(3);

        System.out.println("Response from client:");
        System.out.println("---------------------");
        System.out.println(" ---");

        int counter1 = 0;
        int counter2 = 0;
        int counter3 = 0;
        int counter4 = 0;

        for(double[] i : X.getData()) {
            for (double j : i) {
                //System.out.println("|" + j + "|");
                //System.out.println(" ---");
                counter1++;
            }
        }
        System.out.println("Matrix X contains: " + counter1 + " values");
        for(double[] i : Y.getData()) {
            for(double j : i) {
                //System.out.println("|" + j + "|");
                //System.out.println(" ---");
                counter2++;
            }
        }
        System.out.println("Matrix Y contains: " + counter2 + " values");
        for(double[] i : C.getData()) {
            for(double j : i) {
                //System.out.println("|" + j + "|");
                //System.out.println(" ---");
                counter3++;
            }
        }
        System.out.println("Matrix C contains: " + counter3 + " values");
        for(double[] i : P.getData()) {
            for(double j : i) {
                //System.out.println("|" + j + "|");
                //System.out.println(" ---");
                //System.out.println(" ---");
                counter4++;
            }
        }
        System.out.println("Matrix P contains: " + counter4 + " values");

        /*try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        if( R.getData() == null ) {
            System.out.println("Something went wrong: R => null");
        } else {
            //System.out.println("@R row => " + R.getData().length + " / @R column => " + R.getData()[0].length);
            //System.out.println("Calculated Error: " + calculateError());
            //calculateBestPoisForUser(10, 4);
        }

        if( X.getData() == null ) {
            System.out.println("Something went wrong: X => null");
        }

        if( Y.getData() == null ) {
            System.out.println("Something went wrong: Y => null");
        }

        if( C.getData() == null ) {
            System.out.println("Something went wrong: C => null");
        }

        if( P.getData() == null ) {
            System.out.println("Something went wrong: P => null");
        }
    }

    public void initialize() {

        s = null;

        if( R == null ) {
            //Ru_i initialize reading each value from matrix.csv
            readCsv rc = new readCsv();
            rc.readCsv();

            ArrayList<ArrayList<Integer>> arr = rc.getAllDataFromCsvFile();

            int max = 0;
            //int counter = 0;

            for( int i = 0; i<rc.getAllDataFromCsvFile().size(); i++ ) {
                if( max < rc.getAllDataFromCsvFile().get(i).get(1) ) max = rc.getAllDataFromCsvFile().get(i).get(1);
            }

            int lastUserId = rc.getAllDataFromCsvFile().get(arr.size()-1).get(0) + 1;

            max = max + 1;

            //System.out.print("Max Index Poi: " + max + ", Max Index User: " + lastUserId + ", arrSize" + arr.size());
            double[][] Ru_i = new double[lastUserId][max];

            int x = 0;

            for( int i = 0; i<arr.size(); i++ ) {
                Ru_i[arr.get(i).get(x)][arr.get(i).get(x+1)] = arr.get(i).get(x+2);
            }

            R = MatrixUtils.createRealMatrix(Ru_i);

        } else {
            System.out.println("Rui error..");
        }

        if( X == null ) {
            X = MatrixUtils.createRealMatrix(R.getRowDimension(), K);
        } else {
            System.out.println("P error..");
        }

        if( Y == null ) {
            Y = MatrixUtils.createRealMatrix(R.getColumnDimension(), K);
        } else {
            System.out.println("P error..");
        }

        if( C == null ) {
            //call calculateCMatrix method to calculate C according to the R
            C = MatrixUtils.createRealMatrix(R.getRowDimension(), R.getColumnDimension());
            calculateCMatrix(R);
        } else {
            System.out.println("C error..");
        }

        if( P == null ) {
            //call calculatePMatrix method to calculate P according to the R
            P = MatrixUtils.createRealMatrix(R.getRowDimension(), R.getColumnDimension());
            calculatePMatrix(R);
        } else {
            System.out.println("P error..");
        }
    }


    //Calculates a C array, using C[u][i] = 1 + a * RealMatrix[u][i], a = 40
    public void calculateCMatrix(RealMatrix Matrix) {
        try {
            for( int i = 0; i < Matrix.getRowDimension(); i++ ) {
                for( int j = 0; j < Matrix.getColumnDimension(); j++ ) {
                    Matrix.multiplyEntry(i,j,A);
                    C.setEntry(i, j, 1 + Matrix.getEntry(i,j));
                }
            }
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Gets each value of RealMatrix and where the value is greatest than 0, replace it with 1
    public void calculatePMatrix(RealMatrix Matrix) {
        try {
            for( int i = 0; i < Matrix.getRowDimension(); i++ ) {
                for( int j = 0; j < Matrix.getColumnDimension(); j++ ) {
                    if(Matrix.getEntry(i,j) > 0) {
                        P.setEntry(i,j, 1);
                    }
                }
            }
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Creates first array X or L inverse matrix
    public void distributeXMatrixToWorkers() {
        //to-do..
        Random rnd = new Random();
        X = MatrixUtils.createRealMatrix(R.getRowDimension(), K);
        for(int i=0; i< X.getRowDimension(); i++) {
            for(int j=0; j< X.getColumnDimension(); j++) {
                X.setEntry(i, j, (double)rnd.nextInt(10));
            }
        }
    }

    //Creates second array Y or R
    public void distributeYMatrixToWorkers() {
        //to-do..
        Random rnd = new Random();
        Y = MatrixUtils.createRealMatrix(R.getColumnDimension(), K);
        for(int i=0; i< Y.getRowDimension(); i++) {
            for(int j=0; j< Y.getColumnDimension(); j++) {
                Y.setEntry(i, j, (double)rnd.nextInt(10));
            }
        }
    }

    //Calculate Cost Function
    public double calculateError() {

        double finalerror = 0;
        double error = 0;
        double norms = 0;

        for(int i=0; i<X.getRowDimension(); i++) {
            for(int j=0; j<X.getColumnDimension(); j++) {
                norms += Math.pow(X.getEntry(i,j),2);
            }
        }

        for(int i=0; i<Y.getRowDimension(); i++) {
            for(int j=0; j<Y.getColumnDimension(); j++) {
                norms += Math.pow(Y.getEntry(i,j),2);
            }
        }

        norms = norms * L;

        RealMatrix X_u = MatrixUtils.createRealMatrix(K, 1);
        RealMatrix Y_i = MatrixUtils.createRealMatrix(K, 1);
        RealMatrix temp = MatrixUtils.createRealMatrix(1,1);

        for(int i=0; i<R.getRowDimension(); i++) {

            for(int k=0; k<X.getColumnDimension(); k++) {
                X_u.setEntry(k, 0, X.getEntry(i,k));
            }

            for(int j=0; j<R.getColumnDimension(); j++) {

                for(int l=0; l<Y.getColumnDimension(); l++) {
                    Y_i.setEntry(l, 0, Y.getEntry(i,l));
                }

                temp = X_u.transpose().multiply(Y_i);
                error += C.getEntry(i,j) * Math.pow(P.getEntry(i,j) - temp.getEntry(0,0), 2);

            }
        }

        finalerror = error + norms;

        return (finalerror/10);
    }

    //Calculate Threshold
    public double calculateScore() {

        score = calculateError();
        double difference = 0.0;

        if (prevscore == 0.0)
            prevscore = score;
        else {
            difference = Math.abs(prevscore - score);
            score = prevscore - difference;
            prevscore = score;
        }

        return score;
    }

    public void calculateBestPoisForUser(int value1, int value2) {

        RealMatrix Rui = MatrixUtils.createRealMatrix(R.getRowDimension(),R.getColumnDimension());

        RealMatrix X_u = MatrixUtils.createRealMatrix(X.getColumnDimension(),1);

        RealMatrix Y_i = MatrixUtils.createRealMatrix(Y.getColumnDimension(),1);

        RealMatrix temp ;
        RealMatrix temp2 = MatrixUtils.createRealMatrix(1,value2);


        for(int i=0; i<R.getRowDimension(); i++) {

            for(int k=0; k<X.getColumnDimension(); k++) {
                X_u.setEntry(k, 0, X.getEntry(i,k));
            }
            for(int j=0; j<R.getColumnDimension(); j++) {

                for(int l=0; l<Y.getColumnDimension(); l++) {
                    Y_i.setEntry(l, 0, Y.getEntry(j,l));
                }

                temp = X_u.transpose().multiply(Y_i);
                //System.out.println(temp.getEntry(0,0));
                Rui.setEntry(i, j, temp.getEntry(0,0));
            }
        }

        double[] temp3 = Rui.getRow(value1);

        double max = 0;
        int pointer = 0;

        for(int i=0; i<value2; i++) {
            for(int j=0; j<Rui.getColumnDimension(); j++) {
                if (max < temp3[j]) {
                    max = temp3[j];
                    pointer = j;
                    System.out.println(pointer);
                }
            }
            System.out.println("End of this loop");
            temp2.setEntry(0, i, pointer);
            temp3[pointer] = 0;
            max = 0 ;
            pointer = 0;
        }

        System.out.println("The best " + value2 + " POIs for User: " + value1 + " are: ");

        for(int i=0; i<value2-1; i++) {
            System.out.println("POI with ID: " + temp2.getEntry(0,i) + ", ");
        }
        System.out.println("POI with ID: " + temp2.getEntry(0,value2-1));
    }

    public RealMatrix getXMatrix() {
        return X;
    }

    public RealMatrix getYMatrix() {
        return Y;
    }

    public RealMatrix getCMatrix() {
        return C;
    }

    public RealMatrix getPMatrix() {
        return P;
    }
}
