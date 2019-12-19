package datastructure;

import org.apache.commons.math3.linear.RealMatrix;

import java.util.List;

public interface Master {

    //open socket
    void initialize();

    //Calculates a C array, using C[u][i] = 1 + a * RealMatrix[u][i], a = 40
    void calculateCMatrix(RealMatrix Matrix);

    //Gets each value of RealMatrix and where the value is greatest than 0, replace it with 1
    void calculatePMatrix(RealMatrix Matrix);

    //Sends X matrix to worker in order to get the new Y back
    void distributeXMatrixToWorkers();

    //Sends Y matrix to worker in order to get the new X back
    void distributeYMatrixToWorkers();

    //Returns calculate_x_u or calculate_y_u - not sure
    double calculateError();

    //inverse -> X[u] * Y[i]
    double calculateScore();

    void calculateBestPoisForUser(int value1, int value2);
}
