package org.team399.y2012.robot.mechanisms;

import org.team399.y2012.robot.LinearVictor;
import org.team399.y2012.config.RobotIOMap;

/**
 *
 * @author robotics
 */
public class IntakeBelt {
    public static class INTAKE_STATES {
        public static MechanismState COLLECT = new MechanismState("COLLECT");   //Collection mode
        public static MechanismState RELEASE = new MechanismState("RELEASE");   //Reverse mode, just in case of jam
        public static MechanismState OFF = new MechanismState("OFF");           //Off mode
    }
    
    double mult = 1;
    
    public void setMult(double val) {
        this.mult = Math.abs(val);
    }
    
    LinearVictor m_intake;
    
    public IntakeBelt() {
        m_intake = new LinearVictor(RobotIOMap.INTAKE_CONVEYOR_PWM);
    }
    
    
    public void setStates(MechanismState state) {
        if(state.equals(INTAKE_STATES.OFF)) {
            set(0);
        } else if(state.equals(INTAKE_STATES.COLLECT)) {
            set(-1);
        } else if(state.equals(INTAKE_STATES.RELEASE)) {
            set(.9);
        }
    }
    
    public void set(double val) {
        m_intake.set(val*mult);
    }
}
