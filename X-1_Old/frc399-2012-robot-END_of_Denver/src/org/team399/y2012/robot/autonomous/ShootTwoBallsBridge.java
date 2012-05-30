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
public class ShootTwoBallsBridge {
    private static long startTime = 0;
    
    public static void start(long delayMillis) 
    {
        startTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - startTime < delayMillis) {
            System.out.println("Delaying autonomous -- ShootTwoBallsDrive");
        }
        //Robot.shooter.setVelocity(4300);
        //Robot.dropper.setState(Dropper.DROPPER_STATES.OUT);
    }
                
    public static void run() 
    {
        double autondrivespeeds = .7;   //the speed of the robot in auton
        double delay = 1.5;             //delay so shooters get up to speed
        double conveystop = 2;          //stop the roller let the shooter catch up
        double conveystart = 2.5;       //start the roller to shoot
        double time1 = 4.5;             //start moving
        double time2 = 8;               //hit the bridge
        double time3 = 8.5;             //when to start backing away from the bridge
        double time4 = 11.5;            //stop time
        
        
        Robot.drive.shift(true);                            //shift to low
        Robot.dropper.setState(Dropper.DROPPER_STATES.OUT); //drop dat dropper
        Robot.shooter.run();                                //move that shooter
        Robot.shooter.setVelocity(4300);                    //set speed
        //Robot.intake.set(0);                              //don't start the conveyor
        
        //grab current turret postion and hold it!
        double turretposition = Robot.turret.getPosition();
        Robot.turret.setAngle(turretposition);
        
        //added to prevent conveyor from fighting itself!
        //stop conveyor for to delay to let the shooter stop
        if(System.currentTimeMillis() - startTime < delay * 1000)
            {Robot.intake.set(0);}  //conveyor on
        
        //start the roller after the shooter spins up
        if(System.currentTimeMillis() - startTime > delay * 1000 && System.currentTimeMillis() - startTime < conveystop * 1000)
            {Robot.intake.set(-1);}  //conveyor on
        
        //stop the roller for split second
        if(System.currentTimeMillis() - startTime > conveystop * 1000 && System.currentTimeMillis() - startTime < conveystart * 1000) 
            {Robot.intake.set(0);}//conveyor stop
        
        //start the roller again
        if(System.currentTimeMillis() - startTime > conveystart * 1000 && System.currentTimeMillis() - startTime < time1 * 1000) 
            {Robot.intake.set(-1);}//conveyor start
        
        //Drive towards the bridge
        if(System.currentTimeMillis() - startTime > time1 * 1000 && System.currentTimeMillis() - startTime < time2 * 1000) 
            {
            Robot.drive.tankDrive(-autondrivespeeds, autondrivespeeds);  //drive forward
            Robot.intake.set(-1); //conveyor on
            }
            
        //stop at the bridge for a few seconds and let the balls hit us
        if(System.currentTimeMillis() - startTime >time2 *1000 && System.currentTimeMillis() - startTime < time3 * 1000)
            {
            Robot.intake.set(-1);
            Robot.drive.tankDrive(0, 0);//stop
            }
        
        
        //drive backwards from the bridge so that the balls roll towards us
        if(System.currentTimeMillis() - startTime > time3 * 1000 && System.currentTimeMillis() - startTime < time4 * 1000)
            {
            Robot.drive.tankDrive(autondrivespeeds, -autondrivespeeds);  //drive backwards
            Robot.intake.set(-1);  //conveyor on
            }
        
        //drive backwards from the bridge so that the balls roll towards us
        if(System.currentTimeMillis() - startTime > time4 * 1000)
            {
            Robot.drive.tankDrive(0, 0);  //stop  
            Robot.shooter.setVelocity(4300); //shooter on
            Robot.intake.set(-1); //conveyor on
            }
    }
}