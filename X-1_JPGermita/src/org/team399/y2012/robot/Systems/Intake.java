/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Systems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Solenoid;
import org.team399.y2012.robot.Config.RobotIOMap;

/**
 * Class encapsulating the entire intake conveyor belt system, including dropper
 * @author Jeremy
 */
public class Intake {

    LinearVictor m_intake;      //Linearized victor speed controller for intake motor
    AnalogChannel m_ballSensor; //Analog channel for VEX line sensor
    Solenoid m_dropper;         //Solenoid for bridge mechanism
    private final double hasBallThresh = .5;   //Threshold
    //TODO: Tweak this value for real ball

    /**
     * Constructor
     */
    public Intake() {
        m_intake = new LinearVictor(RobotIOMap.INTAKE_CONVEYOR_PWM);
        m_ballSensor = new AnalogChannel(RobotIOMap.BALL_SENSOR);
        m_dropper = new Solenoid(RobotIOMap.DROPPER_PORT);
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
        m_dropper.set(value);
    }

    /**
     * returns true if the analog ball sensor's value appears to be that of a ball's
     * @return 
     */
    public boolean hasBall() {
        return m_ballSensor.getVoltage() > hasBallThresh;
    }
}
