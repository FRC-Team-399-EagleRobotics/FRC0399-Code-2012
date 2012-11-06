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
import org.team399.y2012.robot.Controls.Autonomous.Feed;
import org.team399.y2012.robot.Controls.Autonomous.HPBlock;
import org.team399.y2012.robot.Controls.Autonomous.ShootTwoBalls;
import org.team399.y2012.robot.Controls.Autonomous.ShootTwoBallsBridge;
import org.team399.y2012.robot.Controls.Autonomous.HighGearBothBridges;

public class Main extends IterativeRobot {
    
    public static Robot bot;
    Compressor comp = new Compressor(4, 1);     //Compressor on digital in port 4 and relay port 1
    Joystick gamepad = new Joystick(1);         //Gamepad
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
    }
    
    public void disabledPeriodic() {
        
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
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        
        bot.run();
        
        if (auto == 0) {
            //System.out.println("TwoBallShoot");
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
        multiplier = 0.6;
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        
        drive();    //Driver routine
        operate();  //operator routine
    }
    
    public void teleopContinuous() {
        bot.run();  //Basic robot functions that run continuously    
    }
    double multiplier = 1.0;

    /**
     * Driver's routine. Edit driver controls here
     */
    public void drive() {
        
        double leftPower = 0, //Variables to customize controls easily
                rightPower = 0,
                pidBrakeCrawl = 0;;
        boolean shift = false,
                intake = false,
                pidBrake = false;
        if (!funbox.getDigital(3)) {

//            leftPower = gamepad.getRawAxis(2);
//            rightPower = -gamepad.getRawAxis(4);
//            shift = gamepad.getRawButton(8);
//            intake = gamepad.getRawButton(7);
//            pidBrake = gamepad.getRawButton(10);
//            pidBrakeCrawl = leftPower;
            leftPower = leftJoy.getRawAxis(2);
            rightPower = -rightJoy.getRawAxis(2);
            shift = rightJoy.getRawButton(1);
            intake = leftJoy.getRawButton(1);
            pidBrake = rightJoy.getRawButton(2);
            pidBrakeCrawl = leftPower;
            
            if(leftJoy.getRawButton(4) || rightJoy.getRawButton(4)) {
                leftPower = .3;
                rightPower = .3;
            }
            if(leftJoy.getRawButton(5) || rightJoy.getRawButton(5)) {
                leftPower = -.3;
                rightPower = -.3;
            }
            
            if(shift) {
               leftPower = EagleMath.deadband(leftPower, .25);
               rightPower = EagleMath.deadband(rightPower, .25);
            }
            
        } else if (!funbox.getDigital(1)) {
            
            leftPower = leftJoy.getRawAxis(2);
            rightPower = -rightJoy.getRawAxis(2);
            shift = rightJoy.getRawButton(1);
            intake = leftJoy.getRawButton(1);
            pidBrake = rightJoy.getRawButton(2);
            pidBrakeCrawl = leftPower;
        }
        if (funbox.getDigital(1) && funbox.getDigital(3)) {
            
            leftPower = gamepad.getRawAxis(2);
            rightPower = -gamepad.getRawAxis(4);
            shift = gamepad.getRawButton(8);
            intake = gamepad.getRawButton(7);
            pidBrake = gamepad.getRawButton(10);
            pidBrakeCrawl = leftPower;
            
        }
//DROPPER
        bot.intake.setDropper(intake);
//LOW Gear        
        if (pidBrake) {
            bot.drive.PIDLock(true);
            System.out.println("BaseLocked!");
            if(Math.abs(pidBrakeCrawl) < .25) {
                pidBrakeCrawl = 0;
            }
            bot.drive.PIDLockIncrement(-.125 * pidBrakeCrawl);
        } else {
            if (!funbox.getDigital(3)) {
                bot.drive.iCantBelieveItsNotButterDrive(leftPower, -rightPower, shift);
//                bot.drive.tankDrive((rightJoy.getRawAxis(2) + -leftJoy.getRawAxis(1)*.75), 
//                        -(rightJoy.getRawAxis(2) - -leftJoy.getRawAxis(1)*.75));
                //bot.drive.shift(shift);
            } else {
                bot.drive.tankDrive(leftPower, rightPower);
                bot.drive.shift(shift);
            }
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
        
        if (manualAim) {
            turretAngle = EagleMath.map((float) funbox.getAnalog(DriverStationUserInterface.PORTS.TURRET_KNOB), (float) 5.0, (float) 1.75, (float) 10, (float) 0.0);
            turretAngle = Math.abs(turretAngle);
            //System.out.println("TurretAngle: " + bot.turret.getActualPosition());
            //System.out.println("KnobValue: " + turretAngle);
            if (EagleMath.isInBand(turretAngle, 0.0, 2.5)) {
                //System.out.println("Turret Left");
                bot.turret.setAngle(4.3);
            } else if (EagleMath.isInBand(turretAngle, 2.5, 5.0)) {
                //System.out.println("Turret Front");
                bot.turret.setAngle(3.6);
            } else if (EagleMath.isInBand(turretAngle, 5.0, 7.5)) {
                //System.out.println("Turret Right");
                bot.turret.setAngle(2.9);
            } else if (EagleMath.isInBand(turretAngle, 7.5, 10.0)) {
                //System.out.println("Turret Rear");
                bot.turret.setAngle(8.2);
            }
            //bot.turret.setAngle(turretAngle);//TODO: scale to range
            bot.eye.enable(false);
        } else if (autoAimLock) {
            //bot.aic.lockOn();
        } else if (autoAimLFend) {
            //bot.aic.leftFender();
        } else if (autoAimRFend) {
            //bot.aic.rightFender();
        } else if (autoAim) {
            //bot.aic.lockOn();

//            bot.eye.enable(true);
        } else if (funbox.getDigital(DriverStationUserInterface.PORTS.TURRET_SWITCH_OFF)) {
            bot.turret.setV(0);
            bot.eye.enable(false);
        }
        
        
        if (shoot) {
            comp.stop();
            bot.shooter.setVelocity(shooterSpeed);
            //bot.shooter.voltageControl(shooterSpeed);
            bot.shooter.update();
            
            
            if (funbox.getDigital(DriverStationUserInterface.PORTS.INTAKE_BELT_BUTTON)) {
                bot.intake.setIntake(-intakeSpeed);
            } else if (funbox.getDigital(DriverStationUserInterface.PORTS.RELEASE_BELT_BUTTON)) {
                bot.intake.setIntake(intakeSpeed);
            } else {
                bot.intake.setIntake(0);
            }
            
            bot.eye.enable(false);
            bot.aic.enable = false;
            bot.shooter.setEnabled(true);
        } else if (autoShoot) {
            //bot.shooter.voltageControl(shooterSpeed);
            bot.shooter.setWheelVoltage(.85);
            bot.shooter.printCsv();
            bot.shooter.setEnabled(false);
            
        } else {
            bot.shooter.setVelocity(0);
            bot.shooter.voltageControl(0);
            
            if (funbox.getDigital(DriverStationUserInterface.PORTS.INTAKE_BELT_BUTTON) || leftJoy.getRawButton(2)) {
                bot.intake.setIntake(-intakeSpeed);
            } else if (funbox.getDigital(DriverStationUserInterface.PORTS.RELEASE_BELT_BUTTON) || leftJoy.getRawButton(3)) {
                bot.intake.setIntake(intakeSpeed);
            } else {
                bot.intake.setIntake(0);
            }
            //bot.eye.enable(true);
            comp.start();
            //bot.eye.setIdle(false);
            //bot.aic.enable = true;
        }
        //bot.eye.setIdle(shoot);
        bot.shooter.setHood(hood);
        
    }
}
