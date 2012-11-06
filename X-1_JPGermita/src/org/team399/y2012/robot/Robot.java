/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot;

import org.team399.y2012.Utilities.DsLcdStream;
import org.team399.y2012.Utilities.PrintStream;
import org.team399.y2012.robot.Controls.Automation.AutoAimController;
import org.team399.y2012.robot.Controls.Automation.AutoDriveTrain;
import org.team399.y2012.robot.Controls.Automation.AutoShootController;
import org.team399.y2012.robot.Controls.Automation.AutoShooterSpeedController;
import org.team399.y2012.robot.Systems.*;
import org.team399.y2012.robot.Systems.Imaging.*;
import edu.wpi.first.wpilibj.Dashboard;
import edu.wpi.first.wpilibj.DriverStation;

/**
 * Class to encapsulate all mechanisms into one class
 * @author Jeremy
 */
public class Robot {
    public Dashboard comp_dash = DriverStation.getInstance().getDashboardPackerLow();
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
        eye = new EagleEye();        
        //eye.start();
        ps_bot.println("Vision initialized!");
         
        //Eagle Eye Instantiation
        ps_bot.println("Systems Initialized");
        
        shooterSpeedController = new AutoShooterSpeedController();
        shootController = new AutoShootController(shooter, intake, shooterSpeedController);
        adt = new AutoDriveTrain(drive);
        aic = new AutoAimController(turret, eye, drive);
        ps_bot.println("Auto controllers initialized");
        ps_bot.println("Initialization complete!");
        ps_bot.println("Initialization took " + ((double) initStartTime) / 1000 + " seconds!");
        DsLcdStream.printlnMain("Init done! Took " + ((double) initStartTime) / 1000 + " seconds!");
        
        
    }

    /**
     * Basic run method. Insert universal functionality here. That is, functionality that 
     * will be common between autonomous and teleoperated modes. 
     */
    public void run() {
        //shooter.update();
        
//        comp_dash.addInt(eye.getNumberOfTargets());
//        comp_dash.addBoolean(false);
//        try {
//            comp_dash.addDouble((double)eye.getHighestTarget().x);
//            comp_dash.addDouble((double)eye.getHighestTarget().y);
//            comp_dash.addDouble(eye.getHighestTarget().calculateDistance());
//        } catch(NullPointerException e) {
//            comp_dash.addDouble(0);
//            comp_dash.addDouble(0);
//            comp_dash.addDouble(0);
//        }
//        comp_dash.addDouble(shooter.setPointV);
//        comp_dash.addDouble(shooter.getEncoderRate());
//        comp_dash.commit();
    }
}
