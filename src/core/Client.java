package core;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import datastructure.CFWorker;

class Client {

    public static void main(String args[])
    {

        /*if (args.length == 0) {
            System.out.println("usage: java client <unkown>");
            System.exit(1);
        }

        int port = isParseInt(args[0]);

        if (port == -1) {
            System.out.println("usage: java client <" + port + ">");
            System.out.println("<port>: integer");
            System.exit(1);
        }*/

        int port = 8080;
        int cores = Runtime.getRuntime().availableProcessors();
        long memory = (Runtime.getRuntime().maxMemory() / 1024) / 230784;

        //System.out.println("Available Cores: " + cores + ", Memory: " + memory + " GB");

        DataOutputStream dataOut;

        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), port);
            DataInputStream dataInput = new DataInputStream(socket.getInputStream());

            dataOut = new DataOutputStream(socket.getOutputStream());
            dataOut.writeUTF(cores + "/" + memory);

            while (true) {

                String so = dataInput.readUTF();

                if (so.equalsIgnoreCase("exit")) {
                    break;
                }

                CFWorker cfw = new CFWorker();
                System.out.println("Initializing socket..");
                cfw.initialize(socket);
                System.out.println("Sending results..");


                cfw.sendResultsToMaster();
            }

            socket.close();

        } catch(UnknownHostException e){
            System.out.println(e.toString());
        } catch(IOException e){
            System.out.println(e.toString());
        }

    }

    private static int isParseInt(String str){

        int num = -1;
        try{
            num = Integer.parseInt(str);
        } catch (NumberFormatException e) {
        }

        return num;
    }

}