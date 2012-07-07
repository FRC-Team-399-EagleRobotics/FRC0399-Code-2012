/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Controls.Autonomous;

import org.team399.y2012.robot.Main;

/**
 *
 * @author robotics
 */
public class HPBlock {
    private static long startTime = 0;
    
  
    
    public static void start(long delayMillis) 
    {
        startTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - startTime < delayMillis) {
            System.out.println("Delaying autonomous -- ShootTwoBallsDrive");
        }
    }
                
    public static void run() 
    {
        //the speed of the robot in auton
        double autondrivespeeds = .7;   
        
        // Shooter speed
        double RPM = 1600;
        
        double delay = 1.5;             //delay so shooters get up to speed
        double drivetofender = 4;          //stop the roller let the shooter catch up
        double conveystart = 5.5;       //start the roller to shoot
        double conveystop = 6.5;        //start moving
        double backaway = 8;          //when to start backing away from the bridge
        double turntoHP = 9;
        
        double forward = 11.5; 
        double stop = 13.5;
        
       
        //move that shooter
        Main.bot.shooter.update();                                
        Main.bot.shooter.setVelocity(RPM);  
        
        //Hood up
        Main.bot.shooter.setHood(true);
        
        //LOW GEAR
        Main.bot.drive.lowGear();
        
        //grab current turret postion and hold it!
        Main.bot.turret.setV(0);
        
        //added to prevent conveyor from fighting itself!
        //stop conveyor for to delay to let the shooter stop
        if(System.currentTimeMillis() - startTime < delay * 1000)
            {
                Main.bot.intake.setIntake(0);
            }  
        
        //approaches fender flush left
        if(System.currentTimeMillis() - startTime > delay * 1000 &&
           System.currentTimeMillis() - startTime < drivetofender * 1000)
            {
                Main.bot.drive.tankDrive(-autondrivespeeds, autondrivespeeds);
            }  
        
        //feeds ball one to the shooter
        if(System.currentTimeMillis() - startTime > drivetofender * 1000 &&
           System.currentTimeMillis() - startTime < conveystart * 1000) 
            {
                Main.bot.intake.setIntake(-1.0);
            }
        
        //pause belt to let shooter speed up
        if(System.currentTimeMillis() - startTime > conveystart * 1000 &&
           System.currentTimeMillis() - startTime < conveystop * 1000) 
            {
                Main.bot.intake.setIntake(0);
            }
    
            
        //backs away from fender to give room to turn
        if(System.currentTimeMillis() - startTime > conveystop *1000 && 
           System.currentTimeMillis() - startTime < backaway * 1000)
            {
                Main.bot.intake.setIntake(0);
                Main.bot.shooter.setVelocity(0);
                Main.bot.drive.tankDrive(autondrivespeeds, -autondrivespeeds);
            }
        
        
        //turns left towards inbounder station
        if(System.currentTimeMillis() - startTime > backaway *1000 && 
           System.currentTimeMillis() - startTime < turntoHP * 1000)
            {
                Main.bot.drive.tankDrive(autondrivespeeds, autondrivespeeds);
                Main.bot.shooter.setVelocity(0);
            }
        
        
        
        //drives in front of inbounder
        if(System.currentTimeMillis() - startTime > turntoHP *1000 && 
           System.currentTimeMillis() - startTime < forward * 1000)
            {
                Main.bot.drive.tankDrive(-autondrivespeeds, autondrivespeeds);
                Main.bot.shooter.setVelocity(0);
            }
        
       //stops everything
        if(System.currentTimeMillis() - startTime > forward *1000)
            {
                Main.bot.shooter.setVelocity(0);
                Main.bot.drive.tankDrive(0,0);
            }
    }
}