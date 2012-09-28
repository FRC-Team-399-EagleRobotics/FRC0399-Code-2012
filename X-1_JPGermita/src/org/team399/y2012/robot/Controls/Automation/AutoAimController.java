/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Controls.Automation;

import org.team399.y2012.Utilities.EagleMath;
import org.team399.y2012.Utilities.PulseTriggerBoolean;
import org.team399.y2012.robot.Systems.DriveTrain;
import org.team399.y2012.robot.Systems.Imaging.EagleEye;
import org.team399.y2012.robot.Systems.Turret;

/**
 * Auto aim controller automates most aiming related tasks.
 * @author Jeremy
 */
public class AutoAimController {

    private Turret m_turret;
    private EagleEye m_eye;
    private DriveTrain m_drive;

    /**
     * Constructor
     * @param turret
     */
    public AutoAimController(Turret turret, EagleEye eye, DriveTrain drive) {
        this.m_turret = turret;
        this.m_eye = eye;
        this.m_drive = drive;
    }
    public boolean enable = true;

    /**
     * Routine to lock on to the highest target in view.
     * Will not do anything if no target found
     */
    public void lockOn() {
        double trackingP = -.02525, trackingD = .04;

        if (enable) {
            m_eye.run();
            if (m_eye.getHighestTarget() != null) {
                System.out.println("Auto Aim Controller Running!");
                System.out.println("Number of targets found: " + m_eye.getNumberOfTargets());
//                double targetDistance = m_eye.getHighestTarget().distance;  //Get distance in inches
                double xErr = 160 - m_eye.getHighestTarget().x;             //Target's distance from the center of view
                double angleOffset = EagleMath.map((float) xErr, -160, 160, 45, -45);
                angleOffset = EagleMath.truncate(angleOffset, 2);
//                System.out.println("Target Distance:     " + targetDistance);
//                System.out.println("Target X Offset:     " + xErr);
//                System.out.println("Target Angle Offset: " + angleOffset);

                m_turret.setV((angleOffset * trackingP) - (trackingD * (angleOffset * trackingP)));
            } else {
                m_turret.setV(0);
                //autoTurn();
            }

        } else {
            m_turret.setV(0);
            m_eye.framerate = 0;
        }
    }

    private PulseTriggerBoolean autoTurnPulse_front = new PulseTriggerBoolean();
    private PulseTriggerBoolean autoTurnPulse_back = new PulseTriggerBoolean();
    private void autoTurn() {

        double botHeading = m_drive.getAngleWraparound();
        boolean botIsFacingForward = EagleMath.isInBand(botHeading, 0, 90) || EagleMath.isInBand(botHeading, 0, 90);
        
        autoTurnPulse_front.set(botIsFacingForward);
        autoTurnPulse_back.set(!botIsFacingForward);
        
        if(autoTurnPulse_front.get()) {
           //turn turret to front of robot
        } else if(autoTurnPulse_back.get()) {
            //turn turret to rear of robot
        } else {
            //Stop turret
        }

    }

    /**
     * Turns the turret to the front of the robot and attempts to lock on
     */
    public void turnToFront() {
        setAndLock(0);
    }

    /**
     * Turns the turret to the rear of the robot. attempts to lock on
     */
    public void turnToRear() {
        setAndLock(180);
    }

    /**
     * turns the turret to the left fender position and attempts to lock on
     */
    public void leftFender() {
        setAndLock(40);
    }

    /**
     * turns the turret to the right fender position and attempts to lock on
     */
    public void rightFender() {
        setAndLock(-40);
    }

    public void rLeftFender() {
    }

    public void rRightFender() {
    }

    /**
     * changes the turret angle as a function of the robot drivetrain's angle, attempting to keep the turret angle
     * constantly pointed in a direction
     * @param angle The robot drivetrain's angle
     */
    public void virtualFourBar(double angle) {
        double setAngle = (angle - 90) / 360;    //Some fancy scaling math to get the drivetrain angle to be  a usable angle for the turret
        setAndLock(angle);
    }

    private void setAndLock(double angle) {
        m_turret.setAngleDeg(angle);
    }
}