    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2011.HumanInterfaceDevices;

import edu.wpi.first.wpilibj.Joystick;
import java.lang.Math;

/**
 * A wrapper Class for use with the Logitech Attack 3 Joystick
 * @author Jeremy Germita
 */
public class SaitekST290Joystick {

    private Joystick m_Stick;   //Joystick object

    /**
     * Contructor.
     * @param port The USB port the Joystick is connected to
     */
    public SaitekST290Joystick(int port) {
        m_Stick = new Joystick(port);   //Instantiate joystick on desired port
    }

    public double getAngle() {
        return m_Stick.getDirectionDegrees();

    }

    /**
     * Get the Y axis
     * @return the Y axis
     */
    public double getY() {
        return m_Stick.getRawAxis(2);   //Return the Y axis value
    }

    /**
     * Get the X axis
     * @return the X axis
     */
    public double getX() {
        return m_Stick.getRawAxis(1);   //Return the X axis value
    }

    /**
     * Get the throttle
     * @return the throttle
     */
    public double getThrottle() {
        return m_Stick.getRawAxis(4);  //Return the throttle value
    }

    public double getTwist() {
        return m_Stick.getRawAxis(3);
    }

    /**
     * Get the state of the trigger
     * @return the trigger state
     */
    public boolean getTrigger() {
        return m_Stick.getRawButton(1); //Return the trigger state
    }

    /**
     * Get the state the button
     * @param button the button to poll
     * @return the state of the button
     */
    public boolean getButton(int button) {
        return m_Stick.getRawButton(button);    //Return the state of the desired button
    }

    /**
     * Get the state of any button on the stick
     * @return
     */
    public boolean getAny() {
        return getButton(1) || getButton(2) || getButton(3) || getButton(4) || getButton(5)
                || getButton(6);
    }

    /**
     * Get the direction of the POV Hat on the stick
     * @return
     */
    public int getPovDirection() {

        return 0;
    }
}
