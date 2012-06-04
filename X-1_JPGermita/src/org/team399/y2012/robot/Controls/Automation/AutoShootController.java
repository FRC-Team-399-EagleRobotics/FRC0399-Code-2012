/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Controls.Automation;

import org.team399.y2012.robot.Systems.Intake;
import org.team399.y2012.robot.Systems.Shooter;

/**
 * This controller automates a simple shoot command.
 * best used in conjunction with the AutoShooterSpeedController for completely automated shots
 * @author Jeremy
 */
public class AutoShootController {

    private Shooter m_shooter;
    private Intake m_intake;
    private AutoShooterSpeedController m_assc;

    /**
     * Constructor
     * @param shooter shooter instance to use
     * @param intake intake instance to use
     */
    public AutoShootController(Shooter shooter, Intake intake, AutoShooterSpeedController assc) {
        this.m_shooter = shooter;
        this.m_intake = intake;
        this.m_assc = assc;
    }
    
    
    /**
     * Auto shoot routine
     * @param shooterSpeed RPM to run shooter at
     * @param beltSpeed belt speed to run intake at
     */
    public void shoot(double shooterSpeed, double beltSpeed) {
        m_shooter.setVelocity(shooterSpeed);
        
        if((m_intake.hasBall() && m_shooter.isAtTargetSpeed())) {
            m_intake.setIntake(beltSpeed);
        } else {
            m_intake.setIntake(0);
        }
        
    }
    
    /**
     * Autoshoot with autospeed
     * @param distance distance to shoot to
     * @param beltSpeed belt speed to run intake at
     */
    public void shootDist(double distance, double beltSpeed) {
        shoot(m_assc.distanceToRPM(distance), beltSpeed);
    }
}
