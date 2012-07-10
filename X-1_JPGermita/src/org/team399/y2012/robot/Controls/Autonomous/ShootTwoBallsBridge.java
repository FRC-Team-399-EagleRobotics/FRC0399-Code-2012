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
public class ShootTwoBallsBridge {
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
        double RPM =3000;
        
        double delay = 1.5;             //delay so shooters get up to speed
        double conveystop = 2;          //stop the roller let the shooter catch up
        double conveystart = 2.5;       //start the roller to shoot
        double startDrive = 4.5;        //start moving
        double bridge = 7.7;              //hit the bridge
        double backAway = 9.5;          //when to start backing away from the bridge
        double convStop = 11.75;
        double stop = 12.5;             //stop time
        
        boolean intake = false;
        //shift to low
        Main.bot.drive.lowGear();                            
        
        
        // Drop dat dropper
       Main.bot.intake.setDropperRaw(intake);
        
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
            }  
        
        //start the roller after the shooter spins up
        if(System.currentTimeMillis() - startTime > delay * 1000 &&
           System.currentTimeMillis() - startTime < conveystop * 1000)
            {
                Main.bot.intake.setIntake(-1);
            }  
        
        //stop the roller for split second
        if(System.currentTimeMillis() - startTime > conveystop * 1000 &&
           System.currentTimeMillis() - startTime < conveystart * 1000) 
            {
                Main.bot.intake.setIntake(0);
            }
        
        //start the roller again
        if(System.currentTimeMillis() - startTime > conveystart * 1000 &&
           System.currentTimeMillis() - startTime < startDrive * 1000) 
            {
                Main.bot.intake.setIntake(-1);
            }
        
        //Drive towards the bridge
        if(System.currentTimeMillis() - startTime > startDrive * 1000 &&
           System.currentTimeMillis() - startTime < bridge * 1000) 
            {
                Main.bot.drive.tankDrive(-autondrivespeeds, autondrivespeeds);  
                Main.bot.intake.setIntake(-1);               
            }
            
        //stop at the bridge for a few seconds and let the balls hit us
        if(System.currentTimeMillis() - startTime > bridge *1000 && 
           System.currentTimeMillis() - startTime < backAway * 1000)
            {
                
                Main.bot.intake.setIntake(-1);
                Main.bot.drive.tankDrive(0, 0);
            }
        if(System.currentTimeMillis() - startTime > backAway *1000 && 
           System.currentTimeMillis() - startTime < convStop * 1000)
            {
                Main.bot.intake.setIntake(0);
                Main.bot.drive.tankDrive(autondrivespeeds, -autondrivespeeds);
            }
        
        
        //drive backwards from the bridge so that the balls roll towards us
        if(System.currentTimeMillis() - startTime > convStop * 1000 &&
           System.currentTimeMillis() - startTime < stop * 1000)
            {
                Main.bot.drive.tankDrive(autondrivespeeds, -autondrivespeeds);  
                Main.bot.intake.setIntake(0);  
            }
        
        //drive backwards from the bridge so that the balls roll towards us
        if(System.currentTimeMillis() - startTime > stop * 1000)
            {
                Main.bot.drive.tankDrive(0, 0);   
                Main.bot.intake.setIntake(-1);
  
            }
    }
}