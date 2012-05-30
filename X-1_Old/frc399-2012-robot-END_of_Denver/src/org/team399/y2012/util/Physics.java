/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.util;


/**
 * Physics Utility class
 * @author Jeremy
 */
public class Physics {
    final static private double g = 9.81;
    
    /**
     * Calculates the range given the initial velocity and angle
     * @param Vi Initial velocity
     * @param theta angle in degrees
     * @return range in meters
     */
    public static double getRange(double Vi, double theta) {
        double answer;
        answer = (Vi*Vi)*(Math.sin(2*(Math.toRadians(theta))));
        answer /= g;
        return answer;
    }
    
    /**
     * Gets the angle given the range and the initial velocity
     * @param Vi initial velocity in M/s
     * @param range range in meters
     * @return 
     */
    public static double getAngle(double Vi, double range) {
        double answer;
        return 0.0;
        
    }
    
    public static double getInitialVelocity(double range, double angle) {
        double answer;
        answer = Math.sqrt((g*range)/Math.sin(2*angle));
        return answer;
    }
    
}
