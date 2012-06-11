/*
 * Moving Average Filter
 */
package org.team399.y2012.Utilities;

/**
 * Moving average class from FRC125's code
 * @author NUTRONS_PROGRAMMING
 */
public class MovingAverage {

    double[] dataPts;
    int length;
    int i = 0;

    public MovingAverage(int length) {
        dataPts = new double[length];
        this.length = length;
    }

    public double calculate(double in) {
        dataPts[i] = in;
        i++;
        if(i >= length) {
            i = 0;
        }

        // get average
        double avg = 0;
        for(int j = 0; j < length; j++) {
            avg += dataPts[j];
        }
        avg /= length;
        return avg;
    }
}
