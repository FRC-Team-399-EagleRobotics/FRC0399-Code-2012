/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Controls.HumanInterfaceDevices;

import edu.wpi.first.wpilibj.DriverStationEnhancedIO;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO.EnhancedIOException;

/**
 * Driver Station User Interface Class. A wrapper class for the Cypress PSoC
 * @author Jeremy Germita and Brad Hall
 */
public class DriverStationUserInterface {

    private DriverStationEnhancedIO m_io;   //IO board object
    private boolean fault = false;

    public static class PORTS {

        public static final int SHOOT_BUTTON = 4;           //Red button
        public static final int INTAKE_BELT_BUTTON = 2;     //BLUE
        public static final int RELEASE_BELT_BUTTON = 6;    //WHITE
        public static final int SHOOTER_KNOB = 2;           
        public static final int HOOD_KNOB = 6;
        public static final int TURRET_KNOB = 7;
        public static final int BELT_KNOB = 4;
        public static final int SHOOTER_SWITCH_AUTO = 10;
        public static final int SHOOTER_SWITCH_OFF = 12;
        public static final int BELT_SWITCH_FWD = 8;
        public static final int BELT_SWITCH_REV = 6;
        public static final int HOOD_SWITCH_AUTO = 9;
        public static final int HOOD_SWITCH_OFF = 11;
        public static final int TURRET_SWITCH_AUTO = 5;
        public static final int TURRET_SWITCH_OFF = 7;
        public static final int CAMERA_HIGH = 1;
        public static final int CAMERA_LOW = 3;
        public static final int AUTON_KNOB = 5;
    }

    /**
     * Constructor.
     */
    public DriverStationUserInterface() {
        m_io = DriverStation.getInstance().getEnhancedIO(); //Instantiating the object
    }

    public String getCameraPosition(){
       if (!getDigital(PORTS.CAMERA_HIGH) && !getDigital(PORTS.CAMERA_LOW)) {
            return "MIDDLE";
        } else if (getDigital(PORTS.CAMERA_HIGH)) {
            return "HIGH";
        } else if (getDigital(PORTS.CAMERA_LOW)) {
            return "LOW";
        }
        return null;
    }
    
    public String getTurretSwitch() {
        if (!getDigital(PORTS.TURRET_SWITCH_AUTO) && !getDigital(PORTS.TURRET_SWITCH_OFF)) {
            return "MANUAL";
        } else if (getDigital(PORTS.TURRET_SWITCH_AUTO)) {
            return "AUTO";
        } else if (getDigital(PORTS.TURRET_SWITCH_OFF)) {
            return "OFF";
        }
        return null;
    }

    public String getHoodSwitch() {
        if (!getDigital(PORTS.HOOD_SWITCH_AUTO) && !getDigital(PORTS.HOOD_SWITCH_OFF)) {
            return "MANUAL";
        } else if (getDigital(PORTS.HOOD_SWITCH_AUTO)) {
            return "AUTO";
        } else if (getDigital(PORTS.HOOD_SWITCH_OFF)) {
            return "OFF";
        }
        return null;
    }

    public String getShooterSwitch() {
        if (!getDigital(PORTS.SHOOTER_SWITCH_AUTO) && !getDigital(PORTS.SHOOTER_SWITCH_OFF)) {
            return "MANUAL";
        } else if (getDigital(PORTS.SHOOTER_SWITCH_AUTO)) {
            return "AUTO";
        } else if (getDigital(PORTS.SHOOTER_SWITCH_OFF)) {
            return "OFF";
        }
        return null;
    }

    public String getBeltSwitch() {
        if (!getDigital(PORTS.BELT_SWITCH_FWD) && !getDigital(PORTS.BELT_SWITCH_REV)) {
            return "OFF";
        } else if (getDigital(PORTS.BELT_SWITCH_REV)) {
            return "FWD";
        } else if (getDigital(PORTS.BELT_SWITCH_FWD)) {
            return "REV";
        }
        return null;
    }

    /**
     * Get the state of a digital input
     * @param which The channel
     * @return The state of the digital input
     */
    public boolean getDigital(int which) {
        try {
            return !m_io.getDigital(which);
        } catch (Exception e) {
            e.printStackTrace();
            fault = true;
            return false;
        }
    }

    /**
     * Get the value of the analog channel specified
     * @param which Which channel
     * @return the value of that channel, usually between 0 and 3.3
     */
    public double getAnalog(int which) {
        try {
            return 5 - m_io.getAnalogIn(which);
        } catch (Exception e) {
            e.printStackTrace();
            fault = true;
            return 0.0;
        }
    }

    /**
     * Return false during normal activity, true if the board is not plugged in
     * @return false during normal activity, true if the board is not plugged in
     */
    public boolean getFault() {
        try {
            fault = false;
            if (m_io.getFirmwareVersion() == 0) {
                fault = true;
            }
        } catch (EnhancedIOException eioe) {
            eioe.printStackTrace();
            fault = true;
        }
        return fault;
    }

    /**
     * Turn on the leds
     * @param state 
     */
    public void setIndicators(boolean state) {
        if (!fault) {
            try {
                m_io.setLED(1, state);
                m_io.setLED(2, state);
                m_io.setLED(3, state);
                m_io.setLED(4, state);
                m_io.setLED(5, state);
                m_io.setLED(6, state);
                m_io.setLED(7, state);
                m_io.setLED(8, state);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
