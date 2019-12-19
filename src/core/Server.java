package core;

import datastructure.CFMaster;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Server {

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(8080)) {

            System.out.println("Server is up and running on port: " + serverSocket.getLocalPort());

            Random random = new Random();
            CFMaster cfm = new CFMaster();
            ServerConnection[] myHostThread = new ServerConnection[expectedClients+1];
            int random_num = 2;//random.nextInt(10);
            MatrixManagement mm = new MatrixManagement(random_num, expectedClients, cfm, 0, 0);
            ArrayList<RealMatrix> rm = null;
            ArrayList<RealMatrix> cfmaster = null;
            double calculated_score = 0.0;
            Socket socket = null;
            boolean firstTime = true;

            while (true) {

                try {
                    if (firstTime){

                        socket = serverSocket.accept();

                        count++;
                        System.out.println("\n#" + count + " request | from client with IP => "
                                + socket.getInetAddress() + ":"
                                + socket.getPort());
                        if (count == 1)
                            rm = mm.calcMatrix(null, 0, 0);
                        else
                            rm = mm.calcMatrix(rm, 0, 0);

                        myHostThread[count] = new ServerConnection(socket, rm, count, "");
                        myHostThread[count].start();
                        TimeUnit.SECONDS.sleep(2);
                        System.out.println("Available cores of connected computer: " + myHostThread[count].connectedPC_features[0] + "\n" +
                                            "Available ram of connected computer: " + myHostThread[count].connectedPC_features[1] + " GB");
                        TimeUnit.SECONDS.sleep(4);
                    } else {
                        for (int i = 1; i < myHostThread.length; i++) {
                            count = i;
                            System.out.println("Starting again " + count + " client..");
                            /*System.out.println("X row size: " + cfmaster.get(0).getRowDimension() +
                                    ", X column size: " + cfmaster.get(0).getColumnDimension() +
                                    ", Y row size: " + cfmaster.get(1).getRowDimension() +
                                    ", Y row size: " + cfmaster.get(1).getColumnDimension());*/
                            if (random_num > 5)
                                rm = mm.calcMatrix(cfmaster, 0, 0);
                            else
                                rm = mm.calcMatrix(cfmaster, 0, 0);
                            myHostThread[i].setRM(rm);
                            synchronized (myHostThread[i]) {
                                myHostThread[i].notify();
                            }
                            System.out.println("Here size => " + myHostThread[i].rm.size());
                        }
                    }

                    //System.out.println(myHostThread[count].getState() == Thread.State.WAITING);

                    if (count == expectedClients) {
                        for (int i = 1; i < myHostThread.length; i++) {
                            while (myHostThread[i].getState() != Thread.State.WAITING) {
                                continue;
                            }
                        }
                        System.out.println("Size " + real_m.size());

                        cfmaster = mm.mergeMatrix(real_m);

                        count = 0;

                        real_m = new ArrayList<ArrayList<RealMatrix>>();

                        for (int i = 1; i < myHostThread.length; i++) {
                            count = i;
                            if (random_num > 5)
                                rm = mm.calcMatrix(cfmaster, 1, 0);
                            else
                                rm = mm.calcMatrix(cfmaster, 0, 1);
                            myHostThread[i].setRM(rm);
                            synchronized (myHostThread[i]) {
                                myHostThread[i].notify();
                            }
                            System.out.println("Here size => " + myHostThread[i].rm.size());
                        }

                        for (int i = 1; i < myHostThread.length; i++) {
                            while (myHostThread[i].getState() != Thread.State.WAITING) {
                                continue;
                            }
                        }

                        System.out.println("Size after calculating y: " + real_m.size());

                        cfmaster = mm.mergeMatrix(real_m);

                        System.out.println("cfmaster value => " + cfmaster.get(0).getRowDimension());
                        if (cfmaster != null) {
                            /*System.out.println("X row size: " + cfmaster.get(0).getRowDimension() +
                                    ", X column size: " + cfmaster.get(0).getColumnDimension() +
                                    ", Y row size: " + cfmaster.get(1).getRowDimension() +
                                    ", Y row size: " + cfmaster.get(1).getColumnDimension());*/
                            cfm.initialize(cfmaster);
                        }
                        calculated_score = cfm.calculateScore();
                        System.out.println(count + "Calculated score is: " + calculated_score);

                        if (calculated_score >= 10000.0 && calculated_score < 120000.0) {
                            cfm.calculateBestPoisForUser(10, 4);
                            break;
                        } else {
                            /*synchronized (myHostThread) {
                                System.out.println("Starting again all clients..");
                                myHostThread.notifyAll();
                            }
                            System.out.println("Clients r ready..");*/
                            real_m = new ArrayList<ArrayList<RealMatrix>>();
                            firstTime = false;
                            count = 0;
                            continue;
                        }
                    }

                } catch (IOException ex) {
                    System.out.println(ex.toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }

    public static int count = 0;
    public static ArrayList<ArrayList<RealMatrix>> real_m = new ArrayList<ArrayList<RealMatrix>>();
    public final static int expectedClients = 4;
}