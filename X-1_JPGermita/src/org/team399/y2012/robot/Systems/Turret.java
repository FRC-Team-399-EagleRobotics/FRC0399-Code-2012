/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Systems;

import edu.wpi.first.wpilibj.CANJaguar;
import org.team399.y2012.Utilities.EagleMath;
import org.team399.y2012.Utilities.PrintStream;
import org.team399.y2012.robot.Config.RobotIOMap;
import org.team399.y2012.Utilities.MovingAverage;

/**
 *
 * @author Jeremy
 */
public class Turret {

    private CANJaguar m_turret;
    //private double p = -100.0, i = 0.0, d = -1.7, deadband = .25;
    public double positionRaw = 5.0;
    private double errorTolerance = .03;
    private PrintStream m_print = new PrintStream("[Turret] ");
    private MovingAverage actualFilter = new MovingAverage(4);
    private MovingAverage setFilter = new MovingAverage(4);

    /**
     * Constructor.
     * @param CAN_ID Motor's CAN ID
     */
    public Turret() {
        m_print.println("Turret Initialization started...");
        try {
            m_turret = new CANJaguar(RobotIOMap.TURRET_ID);
            m_turret.changeControlMode(CANJaguar.ControlMode.kPosition);
            m_turret.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
            m_turret.configPotentiometerTurns(10);
            
            //TURRET LIMITS
            m_turret.configSoftPositionLimits(1.0, 9.6);                                            
            
            m_turret.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
           
            //TURRET PID CONSTANTS
            //m_turret.setPID(p, i, d); 
            
            m_turret.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            m_turret.enableControl();
        } catch (Exception e) {
            System.err.println("[TURRET]Error initializing");
            e.printStackTrace();
        }
        m_print.println("Turret Initialization complete!");
        for (int i = 0; i < 9; i++) {
            setFilter.calculate(positionRaw);
        }
    }

    /**
     * Set the angle of the turret, relative to the right side of the robot
     * @param angle in pot rotations
     */
    public void setAngle(double angle) {
        try{
        
            positionRaw = setFilter.calculate(angle);
        positionRaw *= 100;
        positionRaw = Math.floor(positionRaw);
        positionRaw /= 100;
        System.out.println("Turret setAngle: " + angle);
        bangBangController();
        // m_turret.setX(angle);
    }
    catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAngleDeg(double angle) {
        double rotationsPerDegrees = 4.3 / 180;
        setAngle(angle * rotationsPerDegrees + 5.0);
    }

    public boolean isAtAngle() {
        return Math.abs(getActualPosition() - positionRaw) > errorTolerance;
    }
    double prevError = 0, error = 0;

    private void bangBangController() {
        double speed = .85;
        double kP = 1.3, kD = 0.1;

        prevError = error;
        error = getActualPosition() - positionRaw;
        try {
            if (Math.abs(error) > errorTolerance) {
                if (Math.abs(error) < .5) {
                    //speed = .15;
                    speed = (kP*error) - (kD * (error - prevError));

                    m_turret.setX(speed);
                } else {
//                    if (positionRaw > getActualPosition()) {
//                        m_turret.setX(-speed);
//                    } else if (positionRaw < getActualPosition()) {
//                        m_turret.setX(speed);
//                    } else {
//                        m_turret.setX(0);
//                    }
                    kP = 2.0;
                    kD = .2;
                    
                    speed = (kP*error) - (kD * (error - prevError));
                    m_turret.setX(speed);
                }
                //System.out.println("Turret Speed: " + speed);
                //System.out.println("Turret error: " + error);
            } else {
                m_turret.setX(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setV(double value) {
        try {
            m_turret.setX(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the actual position of the turret
     * @return 
     */
    public double getActualPosition() {
        try {
            double actual = actualFilter.calculate(m_turret.getPosition());
            actual *= 100;
            actual = Math.floor(actual);
            actual /= 100;
            return actual;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
     public double getRawPosition() {
        try {
      
            double actual = m_turret.getPosition();
            
            return actual;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
     }
}
