/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.autonomous;

import org.team399.y2012.robot.Robot;
import org.team399.y2012.robot.mechanisms.Dropper;

/**
 *
 * @author robotics
 */
public class BridgeFirst {
    private static long startTime = 0;
    
    public static void start(long delayMillis) {
        startTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - startTime < delayMillis) {
            System.out.println("Delaying autonomous -- BridgeFirstDrive");
        }
        Robot.drive.shift(true);
        Robot.dropper.setState(Dropper.DROPPER_STATES.OUT);
    }
                
    public static void run() {
        
        double autondrivespeeds = .7;   //the speed of the robot in auton
        double time1 = 3;               //Time it takes to get to bridge
        double time2= 4.5;              //leave the bridge
    
        //Robot.shooter.setRollersV(.75); //Tweak this value for the shot
        Robot.dropper.setState(Dropper.DROPPER_STATES.OUT);
                
        if(System.currentTimeMillis() - startTime > 0 && System.currentTimeMillis() < time1 * 1000)     //Drive to Bridge
        {
            Robot.drive.tankDrive(-autondrivespeeds, autondrivespeeds);
            Robot.dropper.setState(Dropper.DROPPER_STATES.OUT);
        }
        
        if(System.currentTimeMillis() - startTime > time1 * 1000 && System.currentTimeMillis()- startTime < time2 * 1000 ){ // pause at bridge so balls fall
            Robot.drive.tankDrive(0, 0);//stop
        }
        
        if(System.currentTimeMillis() - startTime > time2 * 1000 && System.currentTimeMillis() - startTime < 15000){        //shoot currently possessed balls and gather more balls
            Robot.shooter.run();
            Robot.shooter.setVelocity(4300);                                                                            //ADJUST RPMS for shot
        
            Robot.intake.set(-1);         //conveyor belt  
        }                                                                                   
            
        if(System.currentTimeMillis() - startTime > time2 * 1000){                                        //drive backwards from the bridge so that the balls roll towards us
            Robot.drive.tankDrive(autondrivespeeds, -autondrivespeeds); 
        }
      
      
    }
}