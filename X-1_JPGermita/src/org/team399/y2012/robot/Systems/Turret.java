/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Systems;

import edu.wpi.first.wpilibj.CANJaguar;
import org.team399.y2012.robot.Config.RobotIOMap;

/**
 *
 * @author Jeremy
 */
public class Turret {

    private CANJaguar m_turret;

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
            m_turret.configSoftPositionLimits(1.0, 8.5);                                            //TURRET LIMITS
            m_turret.setPID(-50, -.1, 35);                                                          //TURRET PID CONSTANTS
            m_turret.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            m_turret.enableControl();
        } catch (Exception e) {
            System.err.println("[TURRET]Error initializing");
            e.printStackTrace();
        }
    }

    /**
     * Set the angle of the turret, relative to the right side of the robto
     * @param angle 
     */
    public void setAngle(double angle) {
        try {
            m_turret.setX(angle);// - 3);

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

    /**
     * get the set position
     * @return 
     */
    public double getSetPosition() {
        try {
            return m_turret.getX();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Returns true if the turret is actually very close to the target position
     * @return 
     */
    public boolean isAtAngle() {
        return Math.abs(getSetPosition() - getActualPosition()) < .00005;
    }
}
