package core;

import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class BlockingBuffer implements Buffer {

    private final ArrayBlockingQueue<ArrayList<RealMatrix>> buffer;
    private final ArrayBlockingQueue<ArrayList<ArrayList<RealMatrix>>> bufferR;

    public BlockingBuffer() {
        buffer = new ArrayBlockingQueue<ArrayList<RealMatrix>>(1);
        bufferR = new ArrayBlockingQueue<ArrayList<ArrayList<RealMatrix>>>(1);
    }

    public void set(ArrayList<RealMatrix> matrix) throws InterruptedException {
        buffer.put( matrix );
    }

    public ArrayList<RealMatrix> get() throws InterruptedException {
        ArrayList<RealMatrix> readMatrix = buffer.take();
        return readMatrix;
    }
}
