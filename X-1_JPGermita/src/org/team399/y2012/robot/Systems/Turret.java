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
    }
}
