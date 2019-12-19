package core;

import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;

public interface Buffer {

    public void set( ArrayList<RealMatrix> matrix ) throws InterruptedException;

    public ArrayList<RealMatrix> get() throws InterruptedException;
}
