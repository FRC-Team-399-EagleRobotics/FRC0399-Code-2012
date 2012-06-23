/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Systems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Solenoid;
import org.team399.y2012.Utilities.PrintStream;
import org.team399.y2012.robot.Config.RobotIOMap;

/**
 * This class encapsulates the two motors and pneumatic actuator
 * involved in operating the shooter. Provides the bare essentials for running the shooter.
 * @author Jeremy Germita
 */
public class Shooter {

    private CANJaguar m_shooterA = null;
    private CANJaguar m_shooterB = null;
    private Solenoid m_hood = null;
    private PrintStream m_print = new PrintStream("[SHOOTER] ");
    /*************************************
     * SHOOTER PID CONSTANTS ARE HERE:
     * ***********************************
     */
    private double kP = 5, //Velocity PID Proportional gain
            kI = 5, //V-PID Integral gain
            kD = 8, //V-PID differential gain
            kF = .2;        //V-PID feed forward gain

    /**
     * Constructor
     * @param ROLLER_CAN_ID CAN ID for main roller
     * @param HOOD_CAN_ID CAN ID for hood
     */
    public Shooter() {

        try {

            //Encoder enabled shooter jag setup: MUST FOLLOW THIS SEQUENCE OR ENCODER OR MOTOR WILL NOT WORK
            m_shooterA = new CANJaguar(RobotIOMap.SHOOTER_A_ID);                //Initialize jaguar
            m_shooterA.setVoltageRampRate(56);                                  //Voltage ramp rate to prevent high current spikes
            m_shooterA.configNeutralMode(CANJaguar.NeutralMode.kCoast);         //Put motor into coast mode to lower amount of sudden force on mechanism
            m_shooterA.changeControlMode(CANJaguar.ControlMode.kPercentVbus);   //Change mode to percent vbus
            m_shooterA.changeControlMode(CANJaguar.ControlMode.kPosition);      //Change mode to position mode
            m_shooterA.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);//Change position reference to encoder
            m_shooterA.configEncoderCodesPerRev(360);                           //set Encoder type
            m_shooterA.changeControlMode(CANJaguar.ControlMode.kPercentVbus);   //Back to percent vbus mode
            m_shooterA.configFaultTime(.5);                                     //Half second fault time to minimize down time in case of fault
        } catch (Exception e) {
            System.err.println("[SHOOTER-A]Error initializing");
            e.printStackTrace();
        }

        try {
            m_shooterB = new CANJaguar(RobotIOMap.SHOOTER_B_ID);
            m_shooterB.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            m_shooterB.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            m_shooterB.setVoltageRampRate(56);
            m_shooterA.configFaultTime(.5);
        } catch (Exception e) {
            System.err.println("[SHOOTER-B]Error initializing");
            e.printStackTrace();
        }
        m_hood = new Solenoid(RobotIOMap.HOOD_PORT);
    }
    private double vel = 0;
    private final double a = .75;
    private double prevT = System.currentTimeMillis();

    public double getEncoderRate() {
        double scalar = -29051.864698566067815887346080672;//-((85423.972664328747414800827263735) / 250) * 360; //-542.63565891472*3.0;//-
        try {
            prevPos = pos;
            pos = m_shooterA.getPosition();

            double time = System.currentTimeMillis();

            double newVel = (pos - prevPos) / (time - prevT); //Velocity is change in position divided by change in unit time
            prevT = time;
            vel = vel * a + (1 - a) * newVel; // Filter algorithm. Tune a up for more filter

            if (Math.abs(vel * scalar) < 50) {
                return 0;
            }
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
     * Updates the PID controller using preconfigured values.
     */
    public void update() {
        update(kP, kI, kD, kF);    //Update shooter PID controller
    }

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
        try {
            if (Math.abs(err) > 1000) {

                m_shooterA.setVoltageRampRate(56);
                m_shooterB.setVoltageRampRate(56);
            } else {
                m_shooterA.setVoltageRampRate(24);
                m_shooterB.setVoltageRampRate(24);
            }
        } catch (Exception e) {
        }

        if (setPointV < 200) {   //Set some deadband on velocity control
            out = 0;            //If commanded a very low speed, coast to a stop
        } else {
            out += .000025 * (P * (err - prevErr) + I * err + D * (err - 2 * prevErr + prevPrevErr) + K * setPointV);  //PID + feedforward calculation
        }

        out = Math.abs(out);

        if (out > 1) {  //Clamping the output to +- 1
            out = 1;
        }

        shoot();
    }

    private void shoot() {
        if (enabled) {
            setWheelVoltage(out);
        } else {
            setWheelVoltage(0);
        }
    }

    /**
     * returns true if the shooter is within 100 rpm of the target speed
     * @return 
     */
    public boolean isAtTargetSpeed() {
        return (Math.abs(err) < 275); /*&& 
        Math.abs(prevErr) < 150 && 
        Math.abs(prevPrevErr) < 200 && 
        !(setPointV != 0));*/
    }

    /**
     * Sets the voltage of the roller wheels
     * @param voltage The voltage value from -1 to 1
     */
    public void setWheelVoltage(double voltage) {
        try {
            m_shooterA.setX(voltage, RobotIOMap.SHOOTER_SYNC_ID);
            m_shooterB.setX(-voltage, RobotIOMap.SHOOTER_SYNC_ID);
            CANJaguar.updateSyncGroup(RobotIOMap.SHOOTER_SYNC_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private boolean enabled = true;

    /**
     * Enable the shooter 
     * @param enabled 
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Move the hood
     * @param state true for up, false for down.
     */
    public void setHood(boolean state) {
        m_hood.set(state);
    }

    public void print() {
        try {
            m_print.println("Position: " + m_shooterA.getPosition());
            m_print.println("Velocity: " + getEncoderRate());
            m_print.println("AvgError: " + (err + prevErr + prevPrevErr) / 3);
        } catch (Exception e) {
        }
    }
}