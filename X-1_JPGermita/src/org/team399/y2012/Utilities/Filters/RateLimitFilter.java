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

    public RateLimitFilter(double limit) {
        this.m_a = limit;
    }

    public void update(double data) {
        prevData = currData;
        currData = data;

        if (Math.abs((prevData - currData)) > m_a) {
            out += m_a;
        } else {
            out += data;
        }
    }

    public double get() {
        return out;
    }

    public void reset() {
        out = 0;
    }
}
