/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Systems;

import edu.wpi.first.wpilibj.AnalogChannel;
import org.team399.y2012.robot.Config.RobotIOMap;
import org.team399.y2012.robot.LinearVictor;

/**
 * Class encapsulating the entire intake conveyor belt system, including dropper
 * @author Jeremy
 */
public class Intake {


    LinearVictor m_intake;
    AnalogChannel m_ballSensor;
    
    private final double hasBallThresh = 0.0;

    /**
     * Constructor
     */
    public Intake() {
        m_intake = new LinearVictor(RobotIOMap.INTAKE_CONVEYOR_PWM);
        //Add dropper pneumatics
        m_ballSensor = new AnalogChannel(RobotIOMap.BALL_SENSOR);
    }

    /**
     * Set the power for the intake motor
     * @param value 
     */
    public void setIntake(double value) {
        m_intake.set(value);
    }

    /**
     * Set the state of the dropper solenoid(s)
     * @param value 
     */
    public void setDropper(boolean value) {
    }
    
    public boolean hasBall() {
        return m_ballSensor.getVoltage() > hasBallThresh;
    }
}
