/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.mechanisms;

import edu.wpi.first.wpilibj.Solenoid;
import org.team399.y2012.config.RobotIOMap;

/**
 *
 * @author Jeremy Germita
 */
public class Dropper {
    public static class DROPPER_STATES {
        public static MechanismState OUT = new MechanismState("OUT");   //Extends dropper
        public static MechanismState IN = new MechanismState("IN");     //Retracts dropper
    }
    
    private Solenoid m_dropper;
    
    public Dropper() {
        m_dropper = new Solenoid(RobotIOMap.DROPPER_PORT);
    }
    
    public void setState(MechanismState state) {
        m_dropper.set(state.equals(DROPPER_STATES.OUT));
    }
    
    boolean flopped = false, position = false;

    /**
     * Move the dropper
     * @param down Button input
     */
    public void drop(boolean down) {
        if (down && !flopped) {
            position = !position;
            m_dropper.set(position);
            flopped = true;
        } else if (!down) {
            flopped = false;
        }
    }
}