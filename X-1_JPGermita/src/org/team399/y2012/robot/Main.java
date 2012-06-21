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
import edu.wpi.first.wpilibj.Joystick;
import org.team399.y2012.Utilities.EagleMath;
import org.team399.y2012.robot.Controls.HumanInterfaceDevices.DriverStationUserInterface;
import edu.wpi.first.wpilibj.Compressor;

/**
 * This is the main class. It provides the periodic and continuous methods for processing
 * and running robot code. Should only be used to call further methods. Try to keep as much
 * function in other classes as possible.
 */
public class Main extends IterativeRobot {

    public static Robot bot;
    AutonFile auton;
    AutonInterpreter ai;
    Compressor comp = new Compressor(4, 1);
    Joystick leftJoy = new Joystick(1);
    Joystick rightJoy = new Joystick(2);
    DriverStationUserInterface funbox = new DriverStationUserInterface();

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        bot = new Robot();
        comp.start();
        bot.shooter.setEnabled(true);
    }
    
    public void disabledPeriodic() {
        bot.eye.demoMode();
    }

    public void autonomousInit() {
        auton = new AutonFile("file:///testAuton.txt");
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
        double leftPower = leftJoy.getRawAxis(2), //Variables to customize controls easily
                rightPower = -leftJoy.getRawAxis(4);
        boolean shift = leftJoy.getRawButton(6),
                intake = leftJoy.getRawButton(1);

//        bot.drive.tankDrive(leftPower, rightPower);
//
//        if (!shift) {
//            bot.drive.highGear();
//        } else {
//            bot.drive.lowGear();
//        }

        bot.intake.setDropper(intake);
        if(!leftJoy.getRawButton(10)) {
            bot.drive.iCantBelieveItsNotButterDrive(leftPower, -rightPower, shift);
        } else {
            bot.drive.tankDrive(-.4, .4);
            bot.drive.lowGear();
        }
    }

    /**
     * Operator routine. edit operator controls here
     */
    public void operate() {

        boolean autoShoot = leftJoy.getRawButton(8),//funbox.getShooterSwitch().equals("AUTO")
                //&& funbox.getDigital(DriverStationUserInterface.PORTS.SHOOT_BUTTON),
                autoSpeed = false,
                autoAimLock = false,
                autoAimLFend = false,
                autoAimRFend = false,
                autoAimKey = false;

        double shooterSpeed = //4000
                EagleMath.map((float)funbox.getAnalog(DriverStationUserInterface.PORTS.SHOOTER_KNOB), 
                                           (float)1.75, (float)5.0, (float)500, (float)3000)
                ,
                intakeSpeed = funbox.getAnalog(DriverStationUserInterface.PORTS.BELT_KNOB),
                turretAngle = 0;
        boolean hood = (funbox.getHoodSwitch().equals("AUTO") || funbox.getHoodSwitch().equals("MANUAL")),
                shoot = funbox.getDigital(DriverStationUserInterface.PORTS.SHOOT_BUTTON) && !autoShoot;

        boolean manualAim = funbox.getTurretSwitch().equals("MANUAL");
        

//        if (!(autoAimLock || autoAimLFend || autoAimRFend || autoAimKey)) {
//            manualAim = false;
//        } else if (funbox.getTurretSwitch().equals("MANUAL")) {
//            manualAim = true;
//        }

        //System.out.println("Turret knob: " + funbox.getAnalog(DriverStationUserInterface.PORTS.TURRET_KNOB));
        if (manualAim) {
            bot.turret.setAngle(EagleMath.map((float) funbox.getAnalog(DriverStationUserInterface.PORTS.TURRET_KNOB), (float) 4.75, (float) 2, (float) 9.6, (float) 1.0));//TODO: scale to range
        } else if (autoAimLock) {
            bot.aic.lockOn();
        } else if (autoAimLFend) {
            bot.aic.leftFender();
        } else if (autoAimRFend) {
            bot.aic.rightFender();
        } else if (autoAimKey) {
            bot.aic.virtualFourBar(bot.drive.getAngleWraparound());
        }

        if (shoot) {
            bot.shooter.setVelocity(shooterSpeed);
            
            if (funbox.getDigital(DriverStationUserInterface.PORTS.INTAKE_BELT_BUTTON)) {
                bot.intake.setIntake(1.0);
            } else if (funbox.getDigital(DriverStationUserInterface.PORTS.RELEASE_BELT_BUTTON)) {
                bot.intake.setIntake(-1.0);
            } else {
                bot.intake.setIntake(0);
            }
        } else if (autoShoot) {
            if (!autoSpeed) {
                bot.shootController.shoot(shooterSpeed, 1);
            } else {
                bot.shootController.shootDist(bot.eye.getTallestTarget().distance, 1);
            }
        } else {
            bot.shooter.setVelocity(0);

            if (funbox.getDigital(DriverStationUserInterface.PORTS.INTAKE_BELT_BUTTON) || leftJoy.getRawButton(7)) {
                bot.intake.setIntake(1.0);
            } else if (funbox.getDigital(DriverStationUserInterface.PORTS.RELEASE_BELT_BUTTON)) {
                bot.intake.setIntake(-1.0);
            } else {
                bot.intake.setIntake(0);
            }
        }


        bot.shooter.setHood(hood);


    }
}
