/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot;

import org.team399.y2012.Utilities.DsLcdStream;
import org.team399.y2012.Utilities.EagleMath;
import org.team399.y2012.Utilities.PrintStream;
import org.team399.y2012.robot.Controls.Automation.AutoAimController;
import org.team399.y2012.robot.Controls.Automation.AutoDriveTrain;
import org.team399.y2012.robot.Controls.Automation.AutoShootController;
import org.team399.y2012.robot.Controls.Automation.AutoShooterSpeedController;
import org.team399.y2012.robot.Controls.HumanInterfaceDevices.DriverStationUserInterface;
import org.team399.y2012.robot.Systems.*;
import org.team399.y2012.robot.Systems.Imaging.*;

/**
 * Class to encapsulate all mechanisms into one class
 * @author Jeremy
 */
public class Robot {

    public DriveTrain drive;     //Drivetrain Instance
    public Shooter shooter;         //Shooter instance
    public Intake intake;            //Intake instance
    public Turret turret;            //Turret instance
    public EagleEye eye;           //Eagle Eye Instance
    private PrintStream ps_bot = new PrintStream("[ROBOT] ");   //Printstream for deubgging
    public AutoShootController shootController;
    public AutoShooterSpeedController shooterSpeedController;
    public AutoAimController aic;
    public AutoDriveTrain adt;

    /**
     * Constructor. Insert any other initialization commands here
     */
    public Robot() {
        DsLcdStream.printlnMain("Init Started...");
        long initStartTime = System.currentTimeMillis();
        ps_bot.println("Initialization started...");

        //Drivetrain Instantiation
        drive = new DriveTrain();     
        ps_bot.println("Drivetrain initialized!");
        
        //Shooter instantiation
        shooter = new Shooter();     
        ps_bot.println("Shooter initialized!");
        
        //Intake instantiation
        intake = new Intake();        
        ps_bot.println("Intake initialized!");
        
        //Turret instantiation
        turret = new Turret();        
        ps_bot.println("Turret initialized!");
         
        //Eagle Eye Instantiation
        eye = new EagleEye();        
        ps_bot.println("Vision initialized!");
        ps_bot.println("Systems Initialized");
        
        shooterSpeedController = new AutoShooterSpeedController();
        shootController = new AutoShootController(shooter, intake, shooterSpeedController);
        adt = new AutoDriveTrain(drive);
        aic = new AutoAimController(turret, eye);
        ps_bot.println("Auto controllers initialized");
//        ps_bot.println("Excecuting garbage collection...");
//        System.gc();
        ps_bot.println("Initialization complete!");
        ps_bot.println("Initialization took " + ((double) initStartTime) / 1000 + " seconds!");
        DsLcdStream.printlnMain("Init done! Took " + ((double) initStartTime) / 1000 + " seconds!");
        //eye.start();
        
    }

    /**
     * Basic run method. Insert universal functionality here. That is, functionality that 
     * will be common between autonomous and teleoperated modes. 
     */
    public void run() {
        shooter.update();
        //eye.run();
       
//         DsLcdStream.println1("Shoot Err: " + ((EagleMath.map((float) Main.funbox.getAnalog(DriverStationUserInterface.PORTS.SHOOTER_KNOB),
//                (float) 1.75, (float) 5.0, (float) 500, (float) 3500)) + shooter.getEncoderRate()) + "          ");
//         DsLcdStream.println2("Shooter Act: " + shooter.getEncoderRate() + "          ");
        
    }
}
