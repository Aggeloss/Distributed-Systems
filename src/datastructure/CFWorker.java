package datastructure;

import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class CFWorker implements Worker {

    private Socket s;

    private RealMatrix C = null;
    private RealMatrix P = null;
    private RealMatrix X = null;
    private RealMatrix Y = null;
    private RealMatrix I = null;
    private RealMatrix C_u = null;
    private RealMatrix C_i = null;
    private RealMatrix P_u = null;
    private RealMatrix P_i = null;
    private int K = 20;
    private double L = 0.1;

    private static int counter = 0;

    private ArrayList<RealMatrix> rm;

    //open socket
    public void initialize(Socket socket) {

        s = socket;

        InputStream inputStream;

        try {
            inputStream = s.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            Object object = objectInputStream.readObject();

            rm = (ArrayList<RealMatrix>)object;

            X = rm.get(0);
            Y = rm.get(1);
            C = rm.get(2);
            P = rm.get(3);

            System.out.println("Response from server:");
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

            if(I == null) {
                I = MatrixUtils.createRealMatrix(K, K);
                for(int i=0; i<K; i++) {
                    for(int j=0; j<K; j++) {
                        if(i==j) {
                            I.setEntry(i,j, 1);
                        }
                    }
                }
            }
            if(C_u == null) {
                C_u = MatrixUtils.createRealMatrix(C.getColumnDimension(), C.getColumnDimension());
            }
            if(C_i == null) {
                C_i = MatrixUtils.createRealMatrix(C.getRowDimension(), C.getRowDimension());
            }
            if(P_u == null) {
                P_u = MatrixUtils.createRealMatrix(C.getColumnDimension(), 1);
            }
            if(P_i == null) {
                P_i = MatrixUtils.createRealMatrix(C.getRowDimension(), 1);
            }
            if (X == null && Y == null) {
                System.out.println("X & Y => null");
            }
            //X = MatrixUtils.createRealMatrix(C.getRowDimension(), K);
            //Y = MatrixUtils.createRealMatrix(C.getColumnDimension(), K);
            System.out.println("My fucking counter....." + counter);
            if (X.getData().length != C.getData().length && Y.getData().length != 1) {
                //X = MatrixUtils.createRealMatrix(C.getRowDimension(), K);
                System.out.println("Starting calculating x_u... ");
                for (int i = 0; i<X.getRowDimension(); i++) {
                    //System.out.println(i + " loop");
                   calculate_x_u(i, L);
                }
                System.out.println("Finish calculating x_u... ");
                /*System.out.println("Starting calculating y_i... ");
                for (int i = 0; i<Y.getRowDimension(); i++) {
                   //calculate_y_i(i, L);
                }
                System.out.println("Finish calculating y_i... ");*/
            }
            if (Y.getData().length != C.getData()[0].length && X.getData().length != 1) {
                //Y = MatrixUtils.createRealMatrix(C.getColumnDimension(), K);
                System.out.println("Starting calculating y_i... ");
                for (int i = 0; i<Y.getRowDimension(); i++) {
                    calculate_y_i(i, L);
                }
                System.out.println("Finish calculating y_i... ");
                /*System.out.println("Starting calculating x_u... ");
                for (int i = 0; i<X.getRowDimension(); i++) {
                    //System.out.println(i + " loop");
                    //calculate_x_u(i, L);
                }
                System.out.println("Finish calculating x_u... ");*/
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize() {
        try {
            s = new Socket(InetAddress.getLocalHost(), 8550);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Calculates a diagonal C_u array, where C_u[i][i] = RealMatrix[value][i];
    public void calculateCuMatrix(int value) {
        try {
            for(int i=0; i<C.getColumnDimension(); i++) {
                C_u.setEntry(i, i, C.getEntry(value,i));
            }
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Calculates a diagonal C_i array, where C_i[i][i] = RealMatrix[i][value];
    public void calculateCiMatrix(int value) {
        try {
            for(int i=0; i<C.getRowDimension(); i++) {
                C_i.setEntry(i, i, C.getEntry(i,value));
            }
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Calculates a n * 1 matrix, where P_u[i][0] = P[value][i];
    public void calculatePuMatrix(int value) {
        try {
            for(int i=0; i<P.getColumnDimension(); i++) {
                P_u.setEntry(i, 0, P.getEntry(value,i));
            }
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    //Calculates a m * 1 matrix, where P_[i][0] = P[i][value];
    public void calculatePiMatrix(int value) {
        try {
            for(int i=0; i<P.getRowDimension(); i++) {
                P_i.setEntry(i, 0, P.getEntry(i,value));
            }
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Calculates Y anastrofo * Y
    public RealMatrix preCalculateYY() {

        RealMatrix RealMatrixYY = MatrixUtils.createRealMatrix(Y.getColumnDimension(), Y.getColumnDimension());
        RealMatrixYY = (Y.transpose()).multiply(Y);

        return RealMatrixYY;
    }

    //Calculates X anastrofo * X
    public RealMatrix preCalculateXX() {

        RealMatrix RealMatrixXX = MatrixUtils.createRealMatrix(X.getRowDimension(), X.getRowDimension());
        RealMatrixXX = (X.transpose()).multiply(X);

        return RealMatrixXX;
    }

    //Calculates X_u and places it on X
    public void calculate_x_u(int value1, double value2) {

        calculateCuMatrix(value1);
        calculatePuMatrix(value1);

        RealMatrix temp1 = MatrixUtils.createRealMatrix(C_u.getRowDimension(), C_u.getRowDimension());

        for(int i=0; i<C_u.getRowDimension(); i++) {
            for(int j=0; j<C_u.getColumnDimension(); j++) {
                if(i==j) {
                    temp1.setEntry(i, j, C_u.getEntry(i,j)-1);
                }
            }
        }

        RealMatrix temp2 = MatrixUtils.createRealMatrix(I.getRowDimension(), I.getRowDimension());

        for(int i=0; i<I.getRowDimension(); i++) {
            for(int j=0; j<I.getColumnDimension(); j++) {
                if(i==j) {
                    temp2.setEntry(i, j, value2);
                }
            }
        }

        //RealMatrix temp3 = new LUDecomposition(preCalculateYY().add(Y.transpose().multiply(temp1).multiply(Y)).add(temp2)).getSolver().getInverse();
        RealMatrix temp3 = new QRDecomposition(preCalculateYY().add(Y.transpose().multiply(temp1).multiply(Y)).add(temp2)).getSolver().getInverse();
        RealMatrix X_u = temp3.multiply(Y.transpose()).multiply(C_u).multiply(P_u);

        for(int i=0; i<X_u.getRowDimension(); i++) {
            X.setEntry(value1, i, X_u.getEntry(i,0));
        }

    }

    //Uses the second formula (knowing X array) to calculate user's values
    public void calculate_y_i(int value1, double value2) {

        calculateCiMatrix(value1);
        calculatePiMatrix(value1);

        RealMatrix temp1 = MatrixUtils.createRealMatrix(C_i.getRowDimension(), C_i.getRowDimension());

        for(int i=0; i<C_i.getRowDimension(); i++) {
            for(int j=0; j<C_i.getColumnDimension(); j++) {
                if(i==j) {
                    temp1.setEntry(i, j, C_i.getEntry(i,j)-1);
                }
            }
        }

        RealMatrix temp2 = MatrixUtils.createRealMatrix(I.getRowDimension(), I.getRowDimension());

        for(int i=0; i<I.getRowDimension(); i++) {
            for(int j=0; j<I.getColumnDimension(); j++) {
                if(i==j) {
                    temp2.setEntry(i, j, value2);
                }
            }
        }
        //RealMatrix temp3 = new LUDecomposition(preCalculateXX().add(X.transpose().multiply(temp1).multiply(X)).add(temp2)).getSolver().getInverse();
        RealMatrix temp3 = new QRDecomposition(preCalculateXX().add(X.transpose().multiply(temp1).multiply(X)).add(temp2)).getSolver().getInverse();
        RealMatrix Y_i = temp3.multiply(X.transpose()).multiply(C_i).multiply(P_i);

        for(int i=0; i<Y_i.getRowDimension(); i++) {
            Y.setEntry(value1, i, Y_i.getEntry(i,0));
        }
    }

    //send socket to Master
    public void sendResultsToMaster() {

        OutputStream outputStream;

        System.out.println(X.getData().length);

        rm.set(0, X);
        rm.set(1, Y);
        rm.set(2, C);
        rm.set(3, P);

        counter++;

        try {
            outputStream = s.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(rm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setC(RealMatrix Matrix) {
        C = Matrix.copy();
    }

    public void setP(RealMatrix Matrix) {
        P = Matrix.copy();
    }

    public void setX(RealMatrix Matrix) {
        X = Matrix.copy();
    }

    public void setY(RealMatrix Matrix) {
        Y = Matrix.copy();
    }
}
