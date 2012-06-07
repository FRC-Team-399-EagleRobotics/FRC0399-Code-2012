/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.team399.y2012.robot;

import edu.wpi.first.wpilibj.*;
import org.team399.y2012.robot.Controls.Autonomous.AutonFile;
import org.team399.y2012.robot.Controls.Autonomous.AutonInterpreter;

/**
 * This is the main class. It provides the periodic and continuous methods for processing
 * and running robot code. Should only be used to call further methods. Try to keep as much
 * function in other classes as possible.
 */
public class Main extends IterativeRobot {

    public static Robot bot;
    AutonFile auton;
    AutonInterpreter ai;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        bot = new Robot();
    }

    public void autonomousInit() {
        auton = new AutonFile("Autonomous.txt");
        ai = new AutonInterpreter(auton.getParsedFile());
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        bot.run();
        ai.run();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        bot.run();  //Basic robot functions that run continuously
        drive();    //Driver routine
        operate();  //operator routine
    }

    /**
     * Driver's routine. Edit driver controls here
     */
    public void drive() {
        double leftPower = 0, //Variables to customize controls easily
                rightPower = 0;
        boolean shift = false,
                intake = false;

        bot.drive.tankDrive(leftPower, rightPower);

        if (shift) {
            bot.drive.highGear();
        } else {
            bot.drive.lowGear();
        }

        bot.intake.setDropper(intake);
    }

    /**
     * Operator routine. edit operator controls here
     */
    public void operate() {
        double shooterSpeed = 0,
                intakeSpeed = 0,
                turretAngle = 0;
        boolean hood = false,
                shoot = false;
        boolean autoShoot = false,
                autoSpeed = false,
                autoAimLock = false,
                autoAimLFend = false,
                autoAimRFend = false,
                autoAimKey = false;

        boolean manualAim = !(autoAimLock || autoAimLFend || autoAimRFend || autoAimKey);

        if (manualAim) { //If manual aiming is used
            //Todo: rethink implementation here. If momentary buttons are used, turret will snap back to the manual position after manual
            //Essentially, only switch back to manual if operator explicitly commands a transition into manual
        } else if (autoAimLock) {
            
        } else if (autoAimLFend) {
        } else if (autoAimRFend) {
        } else if (autoAimKey) {
        }

        if (shoot) {
            bot.shooter.setVelocity(shooterSpeed);
        } else if (autoShoot) {
            if (!autoSpeed) {
                bot.shootController.shoot(shooterSpeed, intakeSpeed);
            } else {
                bot.shootController.shootDist(bot.eye.getTallestTarget().distance, intakeSpeed);
            }
        } else {
            bot.shooter.setVelocity(shooterSpeed);
        }

    }
}
