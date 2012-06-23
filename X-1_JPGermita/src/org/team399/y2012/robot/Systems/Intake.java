/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Systems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import org.team399.y2012.robot.Config.RobotIOMap;
import org.team399.y2012.Utilities.RateLimitFilter;

/**
 * Class encapsulating the entire intake conveyor belt system, including dropper
 * @author Jeremy
 */
public class Intake {

    LinearVictor m_intake;      //Linearized victor speed controller for intake motor
    AnalogChannel m_ballSensor; //Analog channel for VEX line sensor
    DoubleSolenoid m_dropper;         //Solenoid for bridge mechanism
    private final double hasBallThresh = .5;   //Threshold
    //TODO: Tweak this value for real ball
    RateLimitFilter vFil = new RateLimitFilter(.5);

    /**
     * Constructor
     */
    public Intake() {
        m_intake = new LinearVictor(RobotIOMap.INTAKE_CONVEYOR_PWM);
        m_ballSensor = new AnalogChannel(RobotIOMap.BALL_SENSOR);
        m_dropper = new DoubleSolenoid(RobotIOMap.DROPPER_PORTA, RobotIOMap.DROPPER_PORTB);
    }

    /**
     * Set the power for the intake motor
     * @param value 
     */
    public void setIntake(double value) {
        //vFil.update(value*.7);
        if(value == 0) vFil.reset();
        m_intake.set(value);
    }

    boolean flopped = false, value = false;


    /**
     * Move the dropper
     * @param down Button input
     */
    public void setDropper(boolean down) {
        if (down && !flopped) {
            value = !value;
            m_dropper.set((value) ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
            flopped = true;
        } else if (!down) {
            flopped = false;
        }
    }

    /**
     * returns true if the analog ball sensor's value appears to be that of a ball's
     * @return 
     */
    public boolean hasBall() {
        return m_ballSensor.getVoltage() > hasBallThresh;
    }
}
