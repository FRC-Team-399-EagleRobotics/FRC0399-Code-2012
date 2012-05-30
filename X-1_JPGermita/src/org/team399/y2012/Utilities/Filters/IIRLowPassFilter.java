/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.Utilities.Filters;

/**
 * An IIR Low pass filter.
 * @author Jeremy
 */
public class IIRLowPassFilter {

    private double m_a = 0.0;
    private double out = 0.0;
    private double currData = 0.0;

    public IIRLowPassFilter(double a) {
        this.m_a = a;
    }

    public void update(double data) {
        currData = data;

        out = out * m_a + (1 - m_a) * currData;
    }

    public double get() {
        return out;
    }

    public void reset() {
        out = 0;
    }
}
