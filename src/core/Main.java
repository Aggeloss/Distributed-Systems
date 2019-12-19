package core;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        readCsv rc = new readCsv();
        rc.readCsv();

        for( int i = 0; i<rc.getAllDataFromCsvFile().size(); i++ ) {
            System.out.println(rc.getAllDataFromCsvFile().get(i));
        }

		/*MasterThread mt1 = new MasterThread("thread 1");
		MasterThread mt2 = new MasterThread("thread 2");

		WorkerThread wt1 = new WorkerThread("thread 1");
		WorkerThread wt2 = new WorkerThread("thread 2");

		mt1.start();
		mt2.start();

		wt1.start();
		wt2.start();*/

		/*Thread t1 = new Thread(new ServerThread("first obj"));
		Thread t2 = new Thread(new ServerThread("second obj"));
		Thread t3 = new Thread(new ServerThread("third obj"));
		Thread t4 = new Thread(new ServerThread("forth obj"));

		t1.start();
		t2.start();
		t3.start();
		t4.start();*/
    }
}
