/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.Utilities.Filters;

/**
 * An Infinite Impulse Response Low pass filter.
 * @author Jeremy
 */
public class IIRLowPassFilter {

    private double m_a = 0.0;
    private double out = 0.0;
    private double currData = 0.0;

    /**
     * Constructor
     * @param a Filter Constant. Tune this higher to filter out higher frequency events
     */
    public IIRLowPassFilter(double a) {
        this.m_a = a;
    }

    /**
     * Update the filter with data
     * @param data a new sample to filter
     */
    public void update(double data) {
        currData = data;
        out = out * m_a + (1 - m_a) * currData;
    }

    /**
     * Get the value of the filter
     * @return 
     */
    public double get() {
        return out;
    }

    /**
     * Zeros the filter output
     */
    public void reset() {
        out = 0;
    }
}
