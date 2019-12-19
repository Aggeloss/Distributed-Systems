package core;

import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;

public interface MatrixM {

    public ArrayList<RealMatrix> calcMatrix(ArrayList<RealMatrix> matrix, int x_value, int y_value);

    public ArrayList<RealMatrix> mergeMatrix(ArrayList<ArrayList<RealMatrix>> rel);

    public RealMatrix[] separateDistributedRealMatrixXY(int value, RealMatrix matrix );

    public RealMatrix[] separateDistributedRealMatrixPC_X( int value, RealMatrix matrix );

    public RealMatrix[] separateDistributedRealMatrixPC_Y( int value, RealMatrix matrix );

}
