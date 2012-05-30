/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.util;

/**
 * An object representing a vector quantity.
 * @author Jeremy
 */
public class PhysicsVector {

    public double mag = 0;
    public double dir = 0;

    /**
     * Constructor
     * @param mag magnitude of vector
     * @param dir angle of vector
     */
    public PhysicsVector(double mag, double dir) {
        this.mag = mag;
        this.dir = dir;
    }
}