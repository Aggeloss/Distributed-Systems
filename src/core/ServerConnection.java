package core;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import datastructure.CFMaster;
import org.apache.commons.math3.linear.RealMatrix;

class ServerConnection extends Thread {

    public String[] connectedPC_features;

    private Socket hostThreadSocket;
    private Object object;
    private Buffer buffer;
    public ArrayList<RealMatrix> rm;
    private String check;
    CFMaster cfm = new CFMaster();
    private int cnt;

    ServerConnection(Socket socket, ArrayList rm, int cnt, String check){
        hostThreadSocket = socket;
        buffer = new BlockingBuffer();
        this.rm = rm;
        this.cnt = cnt;
        this.check = check;
    }

    @Override
    public void run() {

        int counter = 0;

        try {
            DataOutputStream dataOut;

            DataInputStream dataInput = new DataInputStream(hostThreadSocket.getInputStream());
            connectedPC_features = dataInput.readUTF().split("/");

            while (true) {

                /*if (counter == 1) {
                    dataOut = new DataOutputStream(hostThreadSocket.getOutputStream());
                    dataOut.writeUTF("exit");
                    break;
                }*/

                dataOut = new DataOutputStream(hostThreadSocket.getOutputStream());
                dataOut.writeUTF("keep going bro");

                ObjectOutputStream objectOutputStream;
                objectOutputStream = new ObjectOutputStream(hostThreadSocket.getOutputStream());

                if (counter == 0) {
                    objectOutputStream.writeObject(rm);
                } else {
                    object = getObject();
                    objectOutputStream.writeObject(object);
                }

                InputStream inputStream = hostThreadSocket.getInputStream();
                ObjectInputStream objInputStream = new ObjectInputStream(inputStream);
                Object object = objInputStream.readObject();

                setObject((ArrayList<RealMatrix>)object);
                System.out.println("Now I add a matrixxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                Server.real_m.add((ArrayList<RealMatrix>)this.getObject());
                //System.out.println("Size con " + Server.real_m.size());

                if (check.equalsIgnoreCase("exit")) {
                    dataOut = new DataOutputStream(hostThreadSocket.getOutputStream());
                    dataOut.writeUTF(check);
                    break;
                } else {
                    //System.out.println("test");

                    synchronized (this) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //counter++;
            }
        } catch (IOException ex) {
            System.out.println(ex.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                hostThreadSocket.close();
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    public void setObject(Object obj) {
        object = obj;
        try {
            buffer.set((ArrayList<RealMatrix>)object);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<RealMatrix> getObject() {
        try {
            object = buffer.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (ArrayList<RealMatrix>)object;
    }

    public void setRM(ArrayList<RealMatrix> realM) {
        this.rm = realM;
    }
}
