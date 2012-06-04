/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.Utilities;

/**
 * Class of various math utilities.
 * @author robotics
 */
public class EagleMath {

    /**
     * Scales an input of a range between istart and istop to a range between ostart and ostop
     * @param value input value
     * @param istart input value's lower limit
     * @param istop input value's upper limit
     * @param ostart output value's lower limit
     * @param ostop output value's upper limit
     * @return the scaled value
     */
    public static float map(float value, float istart, float istop, float ostart, float ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
    }

    /**
     * Checks to see if input is between low and high
     * @param input input value
     * @param low lower threshold
     * @param high upper threshold
     * @return true if low < input < high
     */
    public static boolean isInBand(double input, double low, double high) {
        return input > low && input < high;
    }
}
