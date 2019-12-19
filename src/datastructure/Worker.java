package datastructure;

import org.apache.commons.math3.linear.RealMatrix;

public interface Worker {

    //open socket
    void initialize();

    //Calculates a diagonal C_u array, where C_u[i][i] = RealMatrix[value][i];
    void calculateCuMatrix(int value);

    //Calculates a diagonal C_i array, where C_i[i][i] = RealMatrix[i][value];
    void calculateCiMatrix(int value);

    //Calculates a n * 1 matrix, where P_u[i][0] = P[value][i];
    void calculatePuMatrix(int value);

    //Calculates a m * 1 matrix, where P_u[i][0] = P[i][value];
    void calculatePiMatrix(int value);

    //Calculates Y anastrofo * Y
    RealMatrix preCalculateYY();

    //Calculates X anastrofo * X
    RealMatrix preCalculateXX();

    //Uses the first formula (knowing Y array) to calculate user's values
    void calculate_x_u(int value1, double value2);

    //Uses the second formula (knowing X array) to calculate user's values
    void calculate_y_i(int value1, double value2);

    //send socket to Master
    void sendResultsToMaster();

    //Hard copy of Matrix to C
    void setC(RealMatrix Matrix);

    //Hard copy of Matrix to P
    void setP(RealMatrix Matrix);

    //Hard copy of Matrix to X
    void setX(RealMatrix Matrix);

    //Hard copy of Matrix to Y
    void setY(RealMatrix Matrix);
}
