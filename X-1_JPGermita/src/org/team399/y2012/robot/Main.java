/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.team399.y2012.robot;

import edu.wpi.first.wpilibj.*;

import edu.wpi.first.wpilibj.Joystick;
import org.team399.y2012.Utilities.EagleMath;
import org.team399.y2012.robot.Controls.HumanInterfaceDevices.DriverStationUserInterface;
import edu.wpi.first.wpilibj.Compressor;
import org.team399.y2012.Utilities.DsLcdStream;
import org.team399.y2012.Utilities.PulseTriggerBoolean;
import org.team399.y2012.robot.Controls.Autonomous.Feed;
import org.team399.y2012.robot.Controls.Autonomous.HPBlock;
import org.team399.y2012.robot.Controls.Autonomous.ShootTwoBalls;
import org.team399.y2012.robot.Controls.Autonomous.ShootTwoBallsBridge;
import org.team399.y2012.robot.Controls.Autonomous.HighGearBothBridges;

public class Main extends IterativeRobot {

    public static Robot bot;
    Compressor comp = new Compressor(4, 1);
    Joystick leftJoy = new Joystick(1);
    Joystick rightJoy = new Joystick(2);
    public static DriverStationUserInterface funbox = new DriverStationUserInterface();
    int auto = 0;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        bot = new Robot();
        comp.start();
        bot.shooter.setEnabled(true);

        //auton = new AutonFile("file:///testAuton.txt");


    }

    public void disabledPeriodic() {
//        bot.eye.demoMode();

        //System.out.println("FB Scaled:" + EagleMath.map((float) funbox.getAnalog(DriverStationUserInterface.PORTS.TURRET_KNOB), (float) 5.0, (float) 1.75, (float) 9.6, (float) 1.0)); 
        //System.out.println("FB Knob:" + funbox.getAnalog(DriverStationUserInterface.PORTS.TURRET_KNOB));
        //System.out.println("Robot Pot: " + bot.turret.getActualPosition());

        if (EagleMath.isInBand(funbox.getAnalog(DriverStationUserInterface.PORTS.AUTON_KNOB), 0.1, 2.0)) {
            //System.out.println("Two ball");
            auto = 0;
        } else if (EagleMath.isInBand(funbox.getAnalog(DriverStationUserInterface.PORTS.AUTON_KNOB), 2.0, 2.9)) {
            //System.out.println("Feeding");
            auto = 1;
        } else if (EagleMath.isInBand(funbox.getAnalog(DriverStationUserInterface.PORTS.AUTON_KNOB), 2.9, 3.9) && !funbox.getDigital(DriverStationUserInterface.PORTS.CAMERA_LOW)) {
            //System.out.println("Bridge");
            auto = 2;
        } else if (EagleMath.isInBand(funbox.getAnalog(DriverStationUserInterface.PORTS.AUTON_KNOB), 3.9, 4.5)) {
            auto = 3;
            //System.out.println("HP Block");
        } else if (EagleMath.isInBand(funbox.getAnalog(DriverStationUserInterface.PORTS.AUTON_KNOB), 4.5, 5.0)) {
            auto = 99;
            //System.out.println("DO NOTHING!");
        } else if (EagleMath.isInBand(funbox.getAnalog(DriverStationUserInterface.PORTS.AUTON_KNOB), 2.9, 3.9) && funbox.getDigital(DriverStationUserInterface.PORTS.CAMERA_LOW)) {
            auto = 42;
            //System.out.println("High Gear Bridge");
        }


    }

    public void autonomousInit() {
        //ai = new AutonInterpreter(auton.getParsedFile());

        if (auto == 0) {
            System.out.println("TwoBallShoot");
            ShootTwoBalls.start(0);
        } else if (auto == 1) {
            System.out.println("Feed");
            Feed.start(0);
        } else if (auto == 2) {
            System.out.println("Bridge");
            ShootTwoBallsBridge.start(0);
        } else if (auto == 3) {
            System.out.println("HP Block");
            HPBlock.start(0);
        } else if (auto == 99) {
            System.out.println("DO Nothing!");
        } else if (auto == 42) {
            System.out.println(" High Gear 2 Ball Bridge ");
            HighGearBothBridges.start(0);
        }
        //bot.eye.start();

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

        bot.run();
        // ai.run();

        if (auto == 0) {
            System.out.println("TwoBallShoot");
            ShootTwoBalls.run();
        } else if (auto == 1) {
            System.out.println("Feed");
            Feed.run();
        } else if (auto == 2) {
            System.out.println("Bridge");
            ShootTwoBallsBridge.run();
        } else if (auto == 3) {
            System.out.println("HP Block");
            HPBlock.run();
        } else if (auto == 99) {
            System.out.println("DOING NOTHING!");
        } else if (auto == 42) {
            System.out.println(" High Gear ");
            HighGearBothBridges.run();
        }
    }

    public void teleopInit() {
        //bot.eye.start();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        
        drive();    //Driver routine
        operate();  //operator routine
        //bot.eye.run();

//        if (bot.eye.getHighestTarget() != null) {
//            System.out.println("Image Data: ");
//            System.out.println("Area: " + bot.eye.getHighestTarget().area);
//            System.out.println(bot.eye.getHighestTarget().x + ", " + bot.eye.getHighestTarget().y);
//        }
    }
    
    public void teleopContinuous() {
        bot.run();  //Basic robot functions that run continuously    
    }

    /**
     * Driver's routine. Edit driver controls here
     */
    public void drive() {
        double leftPower = leftJoy.getRawAxis(2), //Variables to customize controls easily
                rightPower = -leftJoy.getRawAxis(4);
        boolean shift = leftJoy.getRawButton(8),
                intake = leftJoy.getRawButton(7),
                pidBrake = leftJoy.getRawButton(10);
//DROPPER
        bot.intake.setDropper(intake);
//LOW Gear        
        if (leftJoy.getRawButton(2)) {
            bot.drive.tankDrive(-.4, .4);
            bot.drive.lowGear();
//PID Brakes           
        } else if (pidBrake) {
            bot.drive.PIDLock(true);
            System.out.println("BaseLocked!");
            bot.drive.PIDLockIncrement(-.25* leftJoy.getRawAxis(2));


        } else {
            bot.drive.iCantBelieveItsNotButterDrive(leftPower, -rightPower, shift);
            bot.drive.PIDLock(false);
        }

    }
    
    double turretAngle = 0;

    /**
     * Operator routine. edit operator controls here
     */
    public void operate() {

        boolean autoShoot = funbox.getShooterSwitch().equals("AUTO")
                && funbox.getDigital(DriverStationUserInterface.PORTS.SHOOT_BUTTON),
                autoSpeed = false,
                autoAimLock = false,//funbox.getDigital(DriverStationUserInterface.PORTS.TURRET_SWITCH_AUTO),
                autoAimLFend = false,
                autoAimRFend = false,
                autoAim = funbox.getDigital(DriverStationUserInterface.PORTS.TURRET_SWITCH_AUTO);

        


        double shooterSpeed = //4000
                EagleMath.map((float) funbox.getAnalog(DriverStationUserInterface.PORTS.SHOOTER_KNOB),
                (float) 1.75, (float) 5.0, (float) 500, (float) 3500),
                intakeSpeed = funbox.getAnalog(DriverStationUserInterface.PORTS.BELT_KNOB);

        boolean hood = (funbox.getHoodSwitch().equals("AUTO") || funbox.getHoodSwitch().equals("MANUAL")),
                shoot = funbox.getDigital(DriverStationUserInterface.PORTS.SHOOT_BUTTON) && !autoShoot;

        boolean manualAim = funbox.getTurretSwitch().equals("MANUAL");

        //System.out.println("FB Raw:" + funbox.getAnalog(DriverStationUserInterface.PORTS.TURRET_KNOB));
        //System.out.println("FB Scaled:" + EagleMath.map((float) funbox.getAnalog(DriverStationUserInterface.PORTS.TURRET_KNOB), (float) 5.0, (float) 1.75, (float) 9.6, (float) 1.0));
        //System.out.println("Turret Actual:" + Main.bot.turret.getActualPosition());

        //        if (!(autoAimLock || autoAimLFend || autoAimRFend || autoAimKey)) {
//            manualAim = false;
//        } else if (funbox.getTurretSwitch().equals("MANUAL")) {
//            manualAim = true;
//        }

        //System.out.println("Turret knob: " + funbox.getAnalog(DriverStationUserInterface.PORTS.TURRET_KNOB));
        if (manualAim) {
            turretAngle = 
                    EagleMath.map((float) funbox.getAnalog(DriverStationUserInterface.PORTS.TURRET_KNOB), (float) 5.0, (float) 1.75, (float) 9.6, (float) 1.0);
            bot.turret.setAngle(turretAngle);//TODO: scale to range
        } else if (autoAimLock) {
            bot.aic.lockOn();
        } else if (autoAimLFend) {
            bot.aic.leftFender();
        } else if (autoAimRFend) {
            bot.aic.rightFender();
        } else if (autoAim) {
            bot.aic.lockOn();

        } else if (funbox.getDigital(DriverStationUserInterface.PORTS.TURRET_SWITCH_OFF)) {
            bot.turret.setV(0);
        }

        if (shoot) {
            bot.shooter.setVelocity(shooterSpeed);


            if (funbox.getDigital(DriverStationUserInterface.PORTS.INTAKE_BELT_BUTTON)) {
                bot.intake.setIntake(-intakeSpeed);
            } else if (funbox.getDigital(DriverStationUserInterface.PORTS.RELEASE_BELT_BUTTON)) {
                bot.intake.setIntake(intakeSpeed);
            } else {
                bot.intake.setIntake(0);
            }
        } else if (autoShoot) {
            if (!autoSpeed) {
                bot.shootController.shoot(shooterSpeed, -1);
            } else {
                bot.shootController.shootDist(bot.eye.getHighestTarget().distance, 1);
            }
        } else {
            bot.shooter.setVelocity(0);

            if (funbox.getDigital(DriverStationUserInterface.PORTS.INTAKE_BELT_BUTTON) || leftJoy.getRawButton(6)) {
                bot.intake.setIntake(-intakeSpeed);
            } else if (funbox.getDigital(DriverStationUserInterface.PORTS.RELEASE_BELT_BUTTON) || leftJoy.getRawButton(5)) {
                bot.intake.setIntake(intakeSpeed);
            } else {
                bot.intake.setIntake(0);
            }
        }
        
        bot.aic.enable = !shoot;


        bot.shooter.setHood(hood);

    }
}
