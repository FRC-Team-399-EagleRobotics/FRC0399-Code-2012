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
public class ShootTwoBalls {
    private static long startTime = 0;
   
    
    public static void start(long delayMillis) {
       System.out.println (" In TWO Ball");
        startTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - startTime < delayMillis) {
            System.out.println("Delaying autonomous -- ShootTwoBalls");
        }
        
        
    }
                
    public static void run() {
            
        // Speed of shooter, need to test
        double RPM = 2750;              
    
        
        double delay = 1.5;             //delay so shooters get up to speed
        double conveystop = 2;          //stop the roller let the shooter catch up
        double conveystart = 2.5;       //start the roller to shoot
        double stopshoot = 4.5;         //stop shooting balls
        
        double turretposition = Main.bot.turret.getActualPosition();
        
        // Set shooter                                
        Main.bot.shooter.update();                                
        Main.bot.shooter.setVelocity(RPM);   
        
        //Hood up
        Main.bot.shooter.setHood(true);
     
        
        //grab current turret postion and hold it!
         Main.bot.turret.setAngle(turretposition);
        
        //added to prevent conveyor from fighting itself!
        //stop conveyor for to delay to let the shooter stop
        if(System.currentTimeMillis() - startTime < delay * 1000)
           {
                Main.bot.intake.setIntake(0);
           }  //conveyor off
        
        //start the conveyor after the shooter spins up
        if(System.currentTimeMillis() - startTime > delay * 1000 &&
           System.currentTimeMillis() - startTime < conveystop * 1000)
            {
                Main.bot.intake.setIntake(-1);
            }  
        
        //stop the conveyor for split second
        if(System.currentTimeMillis() - startTime > conveystop * 1000 &&
           System.currentTimeMillis() - startTime < conveystart * 1000) 
            {
                Main.bot.intake.setIntake(0);
            }
        
        //start the conveyor
        if(System.currentTimeMillis() - startTime > conveystart * 1000 &&
           System.currentTimeMillis() - startTime < stopshoot * 1000) 
            {
                Main.bot.intake.setIntake(-1);
            }
        
        //stop conveyor and shooter
         if(System.currentTimeMillis() - startTime > stopshoot * 1000) 
            {
                Main.bot.intake.setIntake(0);
                Main.bot.shooter.setVelocity(0);
            }
    }
}