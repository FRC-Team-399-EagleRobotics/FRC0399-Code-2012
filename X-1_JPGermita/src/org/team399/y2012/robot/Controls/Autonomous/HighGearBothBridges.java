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
public class HighGearBothBridges {
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
        double RPM = 3000;
        
        double delay = 1;             //delay so shooters get up to speed
        double conveyStop = 2;          //stop the roller let the shooter catch up
        double conveyStart = 2.5;       //start the roller to shoot
        double startDrive = 4.5;        //start moving
        double stop = 5.65;              //drop the bridge\
        double wait = 8.3;                //when to start backing away from the bridge to give room to turn
        double backAway = 8.6; 
        double turnLeft = 9.1;          //turn towards alliance bridge
        double forward = 10.65;             //approach alliance bridge
        double turnRight = 11.25;          //set up for bridge drop
        
        boolean intake = false;
        //shift to low
        Main.bot.drive.highGear();                            
       
       
        
        //move that shooter
        Main.bot.shooter.update();                                
        Main.bot.shooter.setVelocity(RPM);  
        
        //Hood up
        Main.bot.shooter.setHood(true);
        
        //grab current turret postion and hold it!
        Main.bot.turret.setV(0);
        
        //added to prevent conveyor from fighting itself!
        //stop conveyor for to delay to let the shooter stop
        if(System.currentTimeMillis() - startTime < delay * 1000)
            {
                Main.bot.intake.setIntake(0);
                 // Drop dat dropper
                
            }  
        
        //start the roller after the shooter spins up
        if(System.currentTimeMillis() - startTime > delay * 1000 &&
           System.currentTimeMillis() - startTime < conveyStop * 1000)
            {
                Main.bot.intake.setIntake(-1);
                Main.bot.intake.setDropper(intake);
            }  
        
        //stop the roller for split second
        if(System.currentTimeMillis() - startTime > conveyStop * 1000 &&
           System.currentTimeMillis() - startTime < conveyStart * 1000) 
            {
                Main.bot.intake.setIntake(0);
                Main.bot.intake.setDropperRaw(intake);
            }
        
        //start the roller again
        if(System.currentTimeMillis() - startTime > conveyStart * 1000 &&
           System.currentTimeMillis() - startTime < startDrive * 1000) 
            {
                Main.bot.intake.setIntake(-1);
                Main.bot.intake.setDropperRaw(intake);
            }
        
        //Drive towards the bridge
        if(System.currentTimeMillis() - startTime > startDrive * 1000 &&
           System.currentTimeMillis() - startTime < stop * 1000) 
            {
                Main.bot.shooter.setVelocity(0);
                Main.bot.drive.tankDrive(-autondrivespeeds, autondrivespeeds);  
                Main.bot.intake.setIntake(-1);
                
            }
            
        //stop at the bridge for a few seconds and let the balls hit us
        if(System.currentTimeMillis() - startTime > stop *1000 && 
           System.currentTimeMillis() - startTime < wait * 1000)
            {
                Main.bot.shooter.setVelocity(0);
                Main.bot.intake.setIntake(-1);
                Main.bot.drive.tankDrive(0, 0);
            }
        // B
        if(System.currentTimeMillis() - startTime > wait *1000 && 
           System.currentTimeMillis() - startTime < backAway * 1000)
            {
                Main.bot.shooter.setVelocity(0);
                Main.bot.intake.setDropper(false);
                Main.bot.intake.setIntake(0);
                Main.bot.drive.tankDrive(autondrivespeeds, -autondrivespeeds);
            }
        
        //brings up dropper, backs away to create room to turn
        if(System.currentTimeMillis() - startTime > backAway *1000 && 
           System.currentTimeMillis() - startTime < turnLeft * 1000)
            {
                Main.bot.shooter.setVelocity(0);
                Main.bot.intake.setDropper(false);
                Main.bot.intake.setIntake(0);
                Main.bot.drive.tankDrive(autondrivespeeds, autondrivespeeds);
            }
        
        //approach alliance bridge
        if(System.currentTimeMillis() - startTime > turnLeft * 1000 &&
           System.currentTimeMillis() - startTime < forward * 1000)
            {
                Main.bot.drive.tankDrive(-autondrivespeeds, autondrivespeeds);  
                Main.bot.shooter.setVelocity(0);
                Main.bot.intake.setIntake(0);  
                Main.bot.intake.setDropper(true);
            }
        
        //align with alliance bridge in prep for teleop start
                if(System.currentTimeMillis() - startTime > forward * 1000 &&
           System.currentTimeMillis() - startTime < turnRight * 1000)
            {
                Main.bot.drive.tankDrive(-autondrivespeeds, -autondrivespeeds);  
                Main.bot.intake.setIntake(0);  
                Main.bot.shooter.setVelocity(0);
                //Main.bot.intake.setDropper(false);
            }
                
                
                if(System.currentTimeMillis() - startTime > turnRight * 1000)
            {
                Main.bot.drive.tankDrive(0,0);  
                Main.bot.intake.setIntake(0);  
                Main.bot.shooter.setVelocity(0);
                //Main.bot.intake.setDropper(false);
            }
                

    }
}