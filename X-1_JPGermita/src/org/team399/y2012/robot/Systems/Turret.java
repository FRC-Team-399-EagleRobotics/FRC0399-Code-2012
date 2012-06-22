/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Systems;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.CANJaguar;
import org.team399.y2012.robot.Config.RobotIOMap;

/**
 *
 * @author Jeremy
 */
public class Turret {

    private CANJaguar m_turret;
    //private double p = -100, i = 0, d = -190, deadband = .25;
    private double positionRaw = 5.0;
    private double errorTolerance = .1;

    /**
     * Constructor.
     * @param CAN_ID Motor's CAN ID
     */
    public Turret() {
        try {
            m_turret = new CANJaguar(RobotIOMap.TURRET_ID);
            m_turret.changeControlMode(CANJaguar.ControlMode.kPosition);
            m_turret.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
            m_turret.configPotentiometerTurns(10);
            m_turret.configSoftPositionLimits(1.0, 9.6);                                            //TURRET LIMITS
            m_turret.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            //m_turret.setPID(p, i, d);                                                          //TURRET PID CONSTANTS
            m_turret.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            //m_turret.enableControl();
        } catch (Exception e) {
            System.err.println("[TURRET]Error initializing");
            e.printStackTrace();
        }
    }

    /**
     * Set the angle of the turret, relative to the right side of the robot
     * @param angle in pot rotations
     */
    public void setAngle(double angle) {
        positionRaw = angle;
        System.out.println("Turret setAngle: " + angle);
        bangBangController();
    }
    
    public void setAngleDeg(double angle) {
        double rotationsPerDegrees = .023888888888888888888888888888888;
        setAngle(angle*rotationsPerDegrees+5.0);
    }

    private void bangBangController() {
        double speed = .5;
        try {
            if (Math.abs(getActualPosition() - positionRaw) > errorTolerance) {
                if(Math.abs(getActualPosition() - positionRaw) > .5) {
                    speed  = 1.0;
                }
                
                if (positionRaw > getActualPosition() ) {
                    m_turret.setX(-speed);
                } else if (positionRaw < getActualPosition()) {
                    m_turret.setX(speed);
                } else {
                    m_turret.setX(0);
                }
            } else {
                m_turret.setX(0);
            }
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
            return m_turret.getPosition();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
