package core;

import datastructure.CFMaster;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;

public class MatrixManagement implements MatrixM {

    private ArrayList<RealMatrix> rm;
    private int expectedClients;
    private int random_num;
    private int x_balader;
    private int y_balader;
    CFMaster cfm;

    MatrixManagement(int random_num, int expectedClients, CFMaster cfm, int x_balader, int y_balader) {
        this.cfm = cfm;
        this.random_num = random_num;
        this.expectedClients = expectedClients;
        this.x_balader = x_balader;
        this.y_balader = y_balader;
    }

    public ArrayList<RealMatrix> calcMatrix(ArrayList<RealMatrix> matrix, int x_value, int y_value) {

        this.x_balader = x_value;
        this.y_balader = y_value;

        if (matrix == null && Server.count == 1) {
            System.out.println("Initializing cfm..");
            rm = new ArrayList<RealMatrix>(4);
            cfm.initialize();
        } else {
            rm = new ArrayList<RealMatrix>(4);
        }

        if (random_num > 5 && x_balader == 0 || random_num <= 5 && y_balader == 1) {

            //System.out.println("random number: " + random_num);

            RealMatrix[] X_total = null;
            RealMatrix[] Y_total = null;
            RealMatrix[] C_total = null;
            RealMatrix[] P_total = null;

            if (matrix == null || matrix != null && matrix.get(1).getRowDimension() != (int)(cfm.getCMatrix().getColumnDimension())) {

                if (matrix == null)
                    cfm.distributeXMatrixToWorkers();

                //X_total = separateDistributedRealMatrixXY(expectedClients, cfm.getXMatrix());
                //C_total = separateDistributedRealMatrixPC_X(expectedClients, cfm.getCMatrix());
                //P_total = separateDistributedRealMatrixPC_X(expectedClients, cfm.getPMatrix());
                Y_total = separateDistributedRealMatrixXY(expectedClients, cfm.getYMatrix());

                System.out.println(Server.count + " X => " + cfm.getXMatrix().getData().length + ", " + Server.count + " X => " + cfm.getXMatrix().getData()[0].length);
                System.out.println(Server.count + " Y => " + Y_total[Server.count-1].getData().length + ", " + Server.count + " Y => " + Y_total[Server.count-1].getData()[0].length);
                System.out.println(Server.count + " C => " + cfm.getCMatrix().getData().length + ", " + Server.count + " C => " + cfm.getCMatrix().getData()[0].length);
                System.out.println(Server.count + " P => " + cfm.getPMatrix().getData().length + ", " + Server.count + " P => " + cfm.getPMatrix().getData()[0].length);

                rm.add(cfm.getXMatrix());
                rm.add(Y_total[Server.count-1]);
                rm.add(cfm.getCMatrix());
                rm.add(cfm.getPMatrix());
            } else {
                //X_total = separateDistributedRealMatrixXY(expectedClients, matrix.get(0));
                Y_total = separateDistributedRealMatrixXY(expectedClients, matrix.get(1));
                //C_total = separateDistributedRealMatrixPC_X(expectedClients, matrix.get(2));
                //P_total = separateDistributedRealMatrixPC_X(expectedClients, matrix.get(3));

                System.out.println(Server.count + " X => " + matrix.get(0).getData().length + ", " + Server.count + " X => " + matrix.get(0).getData()[0].length);
                System.out.println(Server.count + " Y => " + Y_total[Server.count-1].getData().length + ", " + Server.count + " Y => " + Y_total[Server.count-1].getData()[0].length);
                System.out.println(Server.count + " C => " + matrix.get(2).getData().length + ", " + Server.count + " C => " + matrix.get(2).getData()[0].length);
                System.out.println(Server.count + " P => " + matrix.get(3).getData().length + ", " + Server.count + " P => " + matrix.get(3).getData()[0].length);

                rm.add(matrix.get(0));
                rm.add(Y_total[Server.count-1]);
                rm.add(matrix.get(2));
                rm.add(matrix.get(3));
            }
        } else if (random_num <= 5 && y_balader == 0 || random_num > 5 && x_balader == 1) {
            //System.out.println("random number: " + random_num);

            RealMatrix[] X_total = null;
            RealMatrix[] Y_total = null;
            RealMatrix[] C_total = null;
            RealMatrix[] P_total = null;

            if (matrix == null || matrix != null && matrix.get(0).getRowDimension() != (int)(cfm.getCMatrix().getRowDimension())) {

                if (matrix == null)
                    cfm.distributeYMatrixToWorkers();

                X_total = separateDistributedRealMatrixXY(expectedClients,cfm.getXMatrix());
                //Y_total = separateDistributedRealMatrixXY(expectedClients,cfm.getYMatrix());
                //C_total = separateDistributedRealMatrixPC_Y(expectedClients,cfm.getCMatrix());
                //P_total = separateDistributedRealMatrixPC_Y(expectedClients,cfm.getPMatrix());

                System.out.println(Server.count + " X => " + X_total[Server.count-1].getData().length + ", " + Server.count + " X => " + X_total[Server.count-1].getData()[0].length);
                System.out.println(Server.count + " Y => " + cfm.getYMatrix().getData().length + ", " + Server.count + " Y => " + cfm.getYMatrix().getData()[0].length);
                System.out.println(Server.count + " C => " + cfm.getCMatrix().getData().length + ", " + Server.count + " C => " + cfm.getCMatrix().getData()[0].length);
                System.out.println(Server.count + " P => " + cfm.getPMatrix().getData().length + ", " + Server.count + " P => " + cfm.getPMatrix().getData()[0].length);

                rm.add(X_total[Server.count-1]);
                rm.add(cfm.getYMatrix());
                rm.add(cfm.getCMatrix());
                rm.add(cfm.getPMatrix());
            } else {
                X_total = separateDistributedRealMatrixXY(expectedClients, matrix.get(0));
                //Y_total = separateDistributedRealMatrixXY(expectedClients, matrix.get(1));
                //C_total = separateDistributedRealMatrixPC_Y(expectedClients, matrix.get(2));
                //P_total = separateDistributedRealMatrixPC_Y(expectedClients, matrix.get(3));

                System.out.println(Server.count + " X => " + X_total[Server.count-1].getData().length + ", " + Server.count + " X => " + X_total[Server.count-1].getData()[0].length);
                System.out.println(Server.count + " Y => " + matrix.get(1).getData().length + ", " + Server.count + " Y => " + matrix.get(1).getData()[0].length);
                System.out.println(Server.count + " C => " + matrix.get(2).getData().length + ", " + Server.count + " C => " + matrix.get(2).getData()[0].length);
                System.out.println(Server.count + " P => " + matrix.get(3).getData().length + ", " + Server.count + " P => " + matrix.get(3).getData()[0].length);

                rm.add(X_total[Server.count-1]);
                rm.add(matrix.get(1));
                rm.add(matrix.get(2));
                rm.add(matrix.get(3));
            }

            //System.out.println(Y_total[Server.count-1].getEntry(0,0) + ", " + Y_total[Server.count-1].getEntry(0,1) + ", " + Y_total[Server.count-1].getEntry(0,2) + ", " + Y_total[Server.count-1].getEntry(1,0) + ", " + Y_total[Server.count-1].getEntry(2,1) + ", " + Y_total[Server.count-1].getEntry(1,2));
            //System.out.println(C_total[Server.count-1].getEntry(0,0) + ", " + C_total[Server.count-1].getEntry(0,1) + ", " + C_total[Server.count-1].getEntry(0,2) + ", " + C_total[Server.count-1].getEntry(2,1) + ", " + C_total[Server.count-1].getEntry(0,1) + ", " + C_total[Server.count-1].getEntry(0,2));
            //System.out.println(P_total[Server.count-1].getEntry(0,0) + ", " + P_total[Server.count-1].getEntry(0,1) + ", " + P_total[Server.count-1].getEntry(0,2) + ", " + P_total[Server.count-1].getEntry(1,1) + ", " + P_total[Server.count-1].getEntry(4,1) + ", " + P_total[Server.count-1].getEntry(3,2));
        }
        return rm;
    }

    public ArrayList<RealMatrix> mergeMatrix(ArrayList<ArrayList<RealMatrix>> rel) {

        RealMatrix[] dataMatrix = new RealMatrix[4];

        dataMatrix[0] = null;
        dataMatrix[1] = null;
        dataMatrix[2] = null;
        dataMatrix[3] = null;

        if (Server.count == Server.expectedClients) {
            ArrayList<ArrayList<RealMatrix>> arr = rel;
            double[][] x_matrix_double = new double[cfm.getPMatrix().getRowDimension()][arr.get(0).get(0).getColumnDimension()];
            double[][] y_matrix_double = new double[cfm.getPMatrix().getColumnDimension()][arr.get(0).get(0).getColumnDimension()];
            RealMatrix x_matrix = MatrixUtils.createRealMatrix(x_matrix_double);
            RealMatrix y_matrix = MatrixUtils.createRealMatrix(y_matrix_double);
            RealMatrix X_temp1 = null;
            RealMatrix X_temp1_sec = null;
            RealMatrix Y_temp1 = null;
            RealMatrix Y_temp1_sec = null;

            int counter_x = 0;
            int counter_y = 0;

            for (int i = 0; i < Server.count; i++) {
                //System.out.println("I'm inn");
                if (i == 0) {
                    /*System.out.println("1st");
                    System.out.println("==================================");*/
                    X_temp1 = arr.get(i).get(0);
                    Y_temp1 = arr.get(i).get(1);
                    dataMatrix[2] = cfm.getCMatrix();
                    dataMatrix[3] = cfm.getPMatrix();
                    //System.out.println("haaa " + X_temp1.getRowDimension());
                    //System.out.println("SIZEE => " + X_temp1.getRowDimension());
                    for (int j = 0; j < X_temp1.getRowDimension(); j++) {
                        for (int h = 0; h < X_temp1.getColumnDimension(); h++) {
                            x_matrix.setEntry(counter_x, h, X_temp1.getEntry(j, h));
                            //System.out.println("x_matrix => " + x_matrix.getEntry(j, h));
                        }
                        counter_x++;
                    }
                    for (int j = 0; j < Y_temp1.getRowDimension(); j++) {
                        for (int h = 0; h < Y_temp1.getColumnDimension(); h++) {
                            y_matrix.setEntry(counter_y, h, Y_temp1.getEntry(j, h));
                            //System.out.println(j + " y_matrix => " + y_matrix.getEntry(j, h));
                        }
                        counter_y++;
                    }
                    dataMatrix[0] = x_matrix.copy();
                    dataMatrix[1] = y_matrix.copy();
                } else {
                    X_temp1_sec = arr.get(i).get(0);
                    Y_temp1_sec = arr.get(i).get(1);
                    /*System.out.println("2nd");
                    System.out.println("==================================");*/
                    if (random_num > 5 && this.x_balader == 1 || random_num <= 5 && this.y_balader == 0) {
                        //System.out.println("haaa " + counter_x);
                        for (int j = 0; j < X_temp1_sec.getRowDimension(); j++) {
                            for (int h = 0; h < X_temp1_sec.getColumnDimension(); h++) {
                                x_matrix.setEntry(counter_x, h, X_temp1_sec.getEntry(j, h));
                                //System.out.println(counter_x + " x_matrix => " + x_matrix.getEntry(j, h));
                            }
                            counter_x++;
                        }
                    } else if (random_num <= 5 && this.y_balader == 1 || random_num > 5 && this.x_balader == 0){
                        for (int j = 0; j < Y_temp1_sec.getRowDimension(); j++) {
                            for (int h = 0; h < Y_temp1_sec.getColumnDimension(); h++) {
                                //System.out.println(counter_y);
                                y_matrix.setEntry(counter_y, h, Y_temp1_sec.getEntry(j, h));
                                //System.out.println(counter_y + " y_matrix => " + y_matrix.getEntry(j, h));
                            }
                            counter_y++;
                        }
                    }
                }
            }
            dataMatrix[0] = x_matrix.copy();
            dataMatrix[1] = y_matrix.copy();

            rm.set(0, dataMatrix[0]);
            rm.set(1, dataMatrix[1]);
            rm.set(2, dataMatrix[2]);
            rm.set(3, dataMatrix[3]);

            if (dataMatrix[0] == null)
                System.out.println("X is null");
            if (dataMatrix[1] == null)
                System.out.println("Y is null");
            if (dataMatrix[2] == null)
                System.out.println("C is null");
            if (dataMatrix[3] == null)
                System.out.println("P is null");
            if (dataMatrix[0] != null && dataMatrix[1] != null && dataMatrix[2] != null && dataMatrix[3] != null) {
                System.out.println("X: " + dataMatrix[0].getData().length + ", Y: " + dataMatrix[0].getData()[0].length +
                        " | X: " + dataMatrix[1].getData().length + ", Y: " + dataMatrix[1].getData()[0].length + " | X: " +
                        dataMatrix[2].getData().length + ", Y: " + dataMatrix[2].getData()[0].length + " | X: " + dataMatrix[3].getData().length +
                        ", Y: " + dataMatrix[3].getData()[0].length);
                System.out.println(dataMatrix[0].getEntry(0,0) + ", " + dataMatrix[0].getEntry(0,1) + ", " + dataMatrix[0].getEntry(0,2) + ", " + dataMatrix[0].getEntry(1,0) + ", " + dataMatrix[0].getEntry(2,1) + ", " + dataMatrix[0].getEntry(1,2));
                System.out.println(dataMatrix[1].getEntry(0,0) + ", " + dataMatrix[1].getEntry(0,1) + ", " + dataMatrix[1].getEntry(0,2) + ", " + dataMatrix[1].getEntry(2,1) + ", " + dataMatrix[1].getEntry(0,1) + ", " + dataMatrix[1].getEntry(0,2));
                System.out.println(dataMatrix[2].getEntry(0,0) + ", " + dataMatrix[2].getEntry(0,1) + ", " + dataMatrix[2].getEntry(0,2) + ", " + dataMatrix[2].getEntry(1,1) + ", " + dataMatrix[2].getEntry(4,1) + ", " + dataMatrix[2].getEntry(3,2));
                System.out.println(dataMatrix[3].getEntry(0,0) + ", " + dataMatrix[3].getEntry(0,1) + ", " + dataMatrix[3].getEntry(1,1) + ", " + dataMatrix[3].getEntry(4,8) + ", " + dataMatrix[3].getEntry(24,1) + ", " + dataMatrix[3].getEntry(100,2));
                ArrayList<RealMatrix> cfmMatrix = new ArrayList<RealMatrix>(4);
                cfmMatrix.add(dataMatrix[0]);
                cfmMatrix.add(dataMatrix[1]);
                cfmMatrix.add(dataMatrix[2]);
                cfmMatrix.add(dataMatrix[3]);
                return cfmMatrix;
            } else {
                System.out.println("NULL..");
                return null;
            }
        }
        return null;
    }

    public RealMatrix[] separateDistributedRealMatrixXY(int value, RealMatrix matrix ) {

        RealMatrix[] totalMatrix = new RealMatrix[value];

        int [] start = new int [2];
        int end = matrix.getColumnDimension() - 1;

        for (int i = 0; i < value; i++) {

            if (i == 0) {
                start[0] = 0;
                start[1] = 0;
            }

            start[0] = start[1];
            if (i == 0)
                start[1] = (matrix.getData().length % value != 0 ? matrix.getData().length/value + 1 : matrix.getData().length/value) + start[0];
            else
                start[1] = matrix.getData().length/value + start[0];

            totalMatrix[i] = matrix.getSubMatrix(start[0], start[1] - 1, 0, end);
        }

        return totalMatrix;
    }

    public RealMatrix[] separateDistributedRealMatrixPC_X( int value, RealMatrix matrix ) {

        RealMatrix[] totalMatrix = new RealMatrix[value];

        int end_new = matrix.getColumnDimension() - 1;

        int [] start = new int [2];
        int [] end = new int [2];

        for (int i = 0; i < value; i++) {

            if (i == 0) {
                start[0] = 0;
                start[1] = 0;
                end[0]   = 0;
                end[0]   = 0;
            }
            System.out.println("Length " + matrix.getData().length + " divided " + (matrix.getData().length/value + start[0]));
            start[0] = start[1];
            if (i == 0)
                start[1] = (matrix.getData().length % value != 0 ? matrix.getData().length/value + 1 : matrix.getData().length/value) + start[0];
            else
                start[1] = matrix.getData().length/value + start[0];

            /*end[0] = end[1];
            if (i == 0)
                end[1] = (matrix.getData()[0].length % value != 0 ? matrix.getData()[0].length/value + 1 : matrix.getData()[0].length/value) + end[0];
            else
                end[1] = matrix.getData()[0].length/value + end[0];*/

            totalMatrix[i] = matrix.getSubMatrix(start[0], start[1] - 1, 0, end_new);
        }

        return totalMatrix;
    }

    public RealMatrix[] separateDistributedRealMatrixPC_Y( int value, RealMatrix matrix ) {

        RealMatrix[] totalMatrix = new RealMatrix[value];

        int end_new = matrix.getRowDimension() - 1;

        int [] start = new int [2];
        int [] end = new int [2];

        for (int i = 0; i < value; i++) {

            if (i == 0) {
                start[0] = 0;
                start[1] = 0;
                end[0]   = 0;
                end[0]   = 0;
            }
            System.out.println("Length " + matrix.getData().length + " divided " + (matrix.getData().length/value + start[0]));
            start[0] = start[1];
            if (i == 0)
                start[1] = (matrix.getData().length % value != 0 ? matrix.getData().length/value + 1 : matrix.getData().length/value) + start[0];
            else
                start[1] = matrix.getData().length/value + start[0];

            /*end[0] = end[1];
            if (i == 0)
                end[1] = (matrix.getData()[0].length % value != 0 ? matrix.getData()[0].length/value + 1 : matrix.getData()[0].length/value) + end[0];
            else
                end[1] = matrix.getData()[0].length/value + end[0];*/

            totalMatrix[i] = matrix.getSubMatrix(start[0], start[1] - 1, 0, end_new);
        }

        return totalMatrix;
    }
}
