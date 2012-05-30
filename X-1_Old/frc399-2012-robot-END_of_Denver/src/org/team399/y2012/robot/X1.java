/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.team399.y2012.robot;

import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import org.team399.y2012.HumanInterfaceDevices.DriverStationUserInterface;
import org.team399.y2012.controllers.TeleopController;
import org.team399.y2012.robot.autonomous.KinectShootAuton;
import org.team399.y2012.robot.autonomous.ShootTwoBalls;
import org.team399.y2012.robot.autonomous.ShootTwoBallsBridge;
import org.team399.y2012.util.EagleMath;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class X1 extends IterativeRobot {
    public Robot bot = new Robot();
    TeleopController teleop = new TeleopController("Gabe", "Jackie");
    //public EagleEye eye = new EagleEye();

    //LightRing ring = new LightRing(RobotIOMap.LIGHT_RING_R, RobotIOMap.LIGHT_RING_G, RobotIOMap.LIGHT_RING_B); //init light ring 
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
//        Timer.delay(10);        //
        //ring.setRGB(0, 0, 0);
        teleop.setControllers(new Joystick(1), new Joystick(2));
    }

    public void disabledInit() {
    }
    //Relay LED = new Relay(1);
    double counter = 0;
    int auton = 0;  //Default to two ball

    public void disabledPeriodic() {
        System.out.println("TurretPot: " + Robot.turret.getPosition());
        //Robot.eye.demoMode();
        //System.out.println("autopot: " + teleop.funbox.getAnalog(DriverStationUserInterface.PORTS.AUTON_KNOB));
        if (EagleMath.isInBand(teleop.funbox.getAnalog(DriverStationUserInterface.PORTS.AUTON_KNOB), 0.1, 2.0)) {
            System.out.println("Two ball");
            //DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "Two Ball Autonomous");
            auton = 0;
        } else if (EagleMath.isInBand(teleop.funbox.getAnalog(DriverStationUserInterface.PORTS.AUTON_KNOB), 2.0, 2.9)) {
            System.out.println("kinect");
            //DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "KinectTwoBall Auton");
            auton = 1;
        } else if (EagleMath.isInBand(teleop.funbox.getAnalog(DriverStationUserInterface.PORTS.AUTON_KNOB), 2.9, 3.9)) {
            System.out.println("Bridge");
            //DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "TwoBallBridge Auton");
            auton = 2;
        } else if (EagleMath.isInBand(teleop.funbox.getAnalog(DriverStationUserInterface.PORTS.AUTON_KNOB), 3.9, 5.0)) {
            //DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "Do Nothing    Auton");
            auton = 99;
        }
        //DriverStationLCD.getInstance().updateLCD();
         
    }

    public void autonomousInit() {

        if (auton == 0) {
            System.out.println("TwoBallShoot");
            ShootTwoBalls.start(0);
        } else if (auton == 1) {
            System.out.println("Kinect TwoBall");
            KinectShootAuton.start(0);
        } else if (auton == 2) {
            ShootTwoBallsBridge.start(0);
        } else if (auton == 99) {
        }
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        if (auton == 0) {
            System.out.println("TwoBallShoot");
            ShootTwoBalls.run();
        } else if (auton == 1) {
            System.out.println("Bridge First");
            KinectShootAuton.run();
        } else if (auton == 2) {
            ShootTwoBallsBridge.run();
        } else if (auton == 99) {
        }
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        //bot.doStatesTele();
        teleop.run();
    }
}
