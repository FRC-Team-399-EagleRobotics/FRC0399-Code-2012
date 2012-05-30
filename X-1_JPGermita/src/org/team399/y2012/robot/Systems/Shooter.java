/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Systems;

import edu.wpi.first.wpilibj.CANJaguar;
import org.team399.y2012.robot.Config.RobotIOMap;

/**
 * This class encapsulates the two motors and pneumatic actuator
 * involved in operating the shooter. Provides the bare essentials for running the shooter.
 * @author Jeremy Germita
 */
public class Shooter {

    private CANJaguar m_shooterA = null;
    private CANJaguar m_shooterB = null;

    /**
     * Constructor
     * @param ROLLER_CAN_ID CAN ID for main roller
     * @param HOOD_CAN_ID CAN ID for hood
     */
    public Shooter() {
        try {

            //Encoder enabled shooter jag setup: MUST FOLLOW THIS SEQUENCE OR ENCODER OR MOTOR WILL NOT WORK
            m_shooterA = new CANJaguar(RobotIOMap.SHOOTER_A_ID);        //Initialize jaguar
            m_shooterA.setVoltageRampRate(48);                          //Voltage ramp rate to prevent high current spikes
            m_shooterA.configNeutralMode(CANJaguar.NeutralMode.kCoast); //Put motor into coast mode to lower amount of sudden force on mechanism
            m_shooterA.changeControlMode(CANJaguar.ControlMode.kPercentVbus);//Change mode to percent vbus
            m_shooterA.changeControlMode(CANJaguar.ControlMode.kPosition);//Change mode to position mode
            m_shooterA.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);//Change position reference to encoder
            m_shooterA.configEncoderCodesPerRev(250);                   //set Encoder type
            m_shooterA.changeControlMode(CANJaguar.ControlMode.kPercentVbus);//Back to percent vbus mode
            m_shooterA.configFaultTime(.5);                             //Half second fault time to minimize down time in case of fault
        } catch (Exception e) {
            System.err.println("[SHOOTER-A]Error initializing");
            e.printStackTrace();
        }
        try {
            m_shooterB = new CANJaguar(RobotIOMap.SHOOTER_B_ID);
            m_shooterB.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            m_shooterB.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            m_shooterB.setVoltageRampRate(48);
            m_shooterA.configFaultTime(.5);
        } catch (Exception e) {
            System.err.println("[SHOOTER-B]Error initializing");
            e.printStackTrace();
        }
    }
    private double vel = 0;
    private final double a = 0.5;
    private double prevT = System.currentTimeMillis();

    public double getEncoderRate() {
        double scalar = -85423.972664328747414800827263735; //-542.63565891472*3.0;
        try {
            prevPos = pos;
            pos = m_shooterA.getPosition();
            double time = System.currentTimeMillis();
            double newVel = (pos - prevPos) / (time - prevT); //Velocity is change in position divided by change in unit time
            prevT = time;
            vel = vel * a + (1 - a) * newVel; // Filter algorithm. Tune a up for more filter
            return vel * scalar;              //Scales value to reasonable values
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0.0; //Returns 0 if there was a fault in above code
    }
    private double setPointV = 0;
    private double err = 0, prevErr = err, prevPrevErr = err;
    private double pos = 0, prevPos = 0;

    /**
     * Set velocity for payload.
     * @param velocity Velocity in meters per second
     */
    public void setVelocity(double velocity) {
        setPointV = velocity;
    }
    double out = 0;

    /**
     * Speed control algorithm
     * @param P Proportional scalar value.
     * @param I Integral scalar value
     * @param D Derivative scalar value
     * @param K Feed forward scalar value. This value varies based on the setpoint. may not be linear. 
     */
    public void update(double P, double I, double D, double K) {
        prevPrevErr = prevErr;
        prevErr = err;
        err = setPointV - getEncoderRate();

        out += -.000025 * (P * (err - prevErr) + I * err + D * (err - 2 * prevErr + prevPrevErr) + K * setPointV);

        if (out > 1) {
            out = 1;
        }
        if (out < -1) {
            out = -1;
        }
        setWheelVoltage(out);
    }
    
    public boolean isAtTargetSpeed() {
        return (err < 100) && !(setPointV != 0);
    }

    /**
     * Sets the voltage of the roller wheels
     * @param voltage The voltage value from -1 to 1
     */
    private void setWheelVoltage(double voltage) {
        try {
            m_shooterA.setX(-voltage, RobotIOMap.SHOOTER_SYNC_ID);
            m_shooterB.setX(voltage, RobotIOMap.SHOOTER_SYNC_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Move the hood
     * @param state true for up, false for down.
     */
    public void setHood(boolean state) {
        //Do hood stuff here
    }
}
