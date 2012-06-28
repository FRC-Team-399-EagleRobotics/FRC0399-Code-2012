/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.Utilities;

import edu.wpi.first.wpilibj.DriverStationLCD;

/**
 * Class used to print to the DriverStationLCD display
 * @author Jeremy
 */
public class DsLcdStream {
    private static DriverStationLCD lcd = DriverStationLCD.getInstance();
    
    public static void println1(String data) {
        lcd.println(DriverStationLCD.Line.kUser2, 1, data);
        lcd.updateLCD();
    }
    public static void println2(String data) {
        lcd.println(DriverStationLCD.Line.kUser3, 1, data);
        lcd.updateLCD();
    }
    public static void println3(String data) {
        lcd.println(DriverStationLCD.Line.kUser4, 1, data);
        lcd.updateLCD();
    }
    public static void println4(String data) {
        lcd.println(DriverStationLCD.Line.kUser5, 1, data);
        lcd.updateLCD();
    }
    public static void println5(String data) {
        lcd.println(DriverStationLCD.Line.kUser6, 1, data);
        lcd.updateLCD();
    }
    public static void printlnMain(String data) {
        
        lcd.println(DriverStationLCD.Line.kMain6, 1, data);
        lcd.updateLCD();
    }
    
    
    
    
}
