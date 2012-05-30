/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Systems.Imaging;

/**
 *
 * @author robotics
 */
public class Threshold {

    public int HueHigh;
    public int HueLow;
    public int SatHigh;
    public int SatLow;
    public int LumHigh;
    public int LumLow;

    public Threshold(int hueL, int hueH, int satL, int satH, int lumL, int lumH) {
        HueHigh = hueH;
        HueLow = hueL;
        SatHigh = satH;
        SatLow = satL;
        LumHigh = lumH;
        LumLow = lumL;
    }
}
