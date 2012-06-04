/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.Utilities.Filters;

/**
 * Rate Limit Filter
 * Filters output by rate of change of input
 * @author Jeremy
 */
public class RateLimitFilter {

    private double m_a = 0.0;
    private double out = 0.0;
    private double currData = 0.0;
    private double prevData = 0.0;

    
    /**
     * Constructor
     * @param limit Maximum rate of change before getting filtered
     */
    public RateLimitFilter(double limit) {
        this.m_a = limit;
    }

    /**
     * Update the filter with data
     * @param data a new sample to filter
     */
    public void update(double data) {
        prevData = currData;
        currData = data;

        if (Math.abs((prevData - currData)) > m_a) {
            out += m_a;
        } else {
            out += data;
        }
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
