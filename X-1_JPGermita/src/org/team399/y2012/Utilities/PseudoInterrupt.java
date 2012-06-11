/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2012.Utilities;

/**
 * A simple class to monitor a boolean variable and return various values based on previous states
 * @author Jeremy Germita
 */
public class PseudoInterrupt {

    private static boolean raw;
    private static boolean prevRaw;

    public static void update(boolean input) {
        prevRaw = raw;
        raw = input;
    }

    public static boolean risingEdge() {
        return (raw != prevRaw) && (raw);
    }

    public static boolean fallingEdge() {
        return (raw != prevRaw) && (!raw);
    }
}
