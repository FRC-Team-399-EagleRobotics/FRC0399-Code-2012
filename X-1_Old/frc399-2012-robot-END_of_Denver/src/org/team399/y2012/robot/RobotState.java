/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot;

import org.team399.y2012.robot.mechanisms.MechanismState;
import org.team399.y2012.robot.mechanisms.Turret;

/**
 * Robot
 * @author robotics
 */
public class RobotState {

    private MechanismState drivetrain = null;
    private MechanismState turret = null;
    private MechanismState belt = null;
    private MechanismState dropper = null;
    

    public RobotState(MechanismState drivetrain, MechanismState turret) {
        this.drivetrain = drivetrain;
        this.turret = turret;
    }
    
}