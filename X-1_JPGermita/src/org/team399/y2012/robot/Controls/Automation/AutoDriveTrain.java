/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Controls.Automation;

import org.team399.y2012.robot.Systems.DriveTrain;

/**
 * A class to provide functions for automating the drivetrain
 * @author Jeremy
 */
public class AutoDriveTrain {

    private DriveTrain m_drive;

    /**
     * Constructor
     * @param drive instance of the drivetrain to use
     */
    public AutoDriveTrain(DriveTrain drive) {
        this.m_drive = drive;
    }

    /**
     * Autobalance routine
     * @param throttle power to try to move forward/back
     */
    public void autoBalance(double throttle) {
        m_drive.lowGear();                          //Lock in low gear for low backdrivability
        double pitch = m_drive.getPitch();          //Read the pitch value from gyro
        double yaw = m_drive.getAngleWraparound();
        
        if(Math.abs(pitch) < 15) pitch = 0;         //Threshold for pitch. Prevents tiny movements that spoil 
        
        double P = 0.05;                            //P constant for closed loop controller
        
        double out = P*pitch;                       //P controller
        
        m_drive.driveToAngle(out+throttle, 0);               //output
    }
    
    /**
     * Drive to a distance
     * @param distance distance in inches
     */
    public void driveToDistance(double distance) {
        //TODO
    }
}
