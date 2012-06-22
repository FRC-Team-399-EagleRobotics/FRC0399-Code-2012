/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Controls.Automation;

import com.sun.squawk.util.MathUtils;
import org.team399.y2012.robot.Systems.Imaging.EagleEye;
import org.team399.y2012.robot.Systems.Turret;

/**
 * Auto aim controller automates most aiming related tasks.
 * @author Jeremy
 */
public class AutoAimController {

    private Turret m_turret;
    private EagleEye m_eye;

    /**
     * Constructor
     * @param turret
     */
    public AutoAimController(Turret turret, EagleEye eye) {
        this.m_turret = turret;
        this.m_eye = eye;
    }

    /**
     * Routine to lock on to the highest target in view.
     * Will not do anything if no target found
     */
    public void lockOn() {
        //DO WORK HERE
        if (System.currentTimeMillis() % 100 < 5) {      //Put on a .1 second timer to reduce noise in tracking
//            if (m_turret.isAtAngle() && m_eye.foundTarget()) {    //Check to see if turret is still trying to go to a set position
//                double targetDistance = m_eye.getTallestTarget().distance;  //Get distance in inches
//                double xErr = 240 - m_eye.getTallestTarget().x;             //Target's distance from the center of view
//
//                double angle = MathUtils.asin(xErr / targetDistance);
//                m_turret.setAngle(m_turret.getSetPosition()-angle);
//            }
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
    public void virtualFourBar(double angle)  {
        double setAngle = (angle - 90)/360;    //Some fancy scaling math to get the drivetrain angle to be  a usable angle for the turret
        setAndLock(angle);
    }
    
    private void setAndLock(double angle) {
//        if (m_turret.isAtAngle() && Math.abs(m_turret.getSetPosition() - angle) > .5) {
//            lockOn();
//        } else {
//            m_turret.setAngle(angle);
//        }
        m_turret.setAngleDeg(angle);
    }
    
   
}