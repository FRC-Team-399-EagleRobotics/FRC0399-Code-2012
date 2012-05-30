/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.util;

/**
 *
 * @author robotics
 */
public class EagleMath {

    public static float map(float value, float istart, float istop, float ostart, float ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
    }
    
    public static boolean isInBand(double input, double low, double high) {
        return input > low && input < high;
    }
}
