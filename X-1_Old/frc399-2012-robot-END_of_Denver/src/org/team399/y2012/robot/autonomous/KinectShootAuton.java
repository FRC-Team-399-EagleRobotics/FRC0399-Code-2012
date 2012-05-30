/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.autonomous;

import edu.wpi.first.wpilibj.KinectStick;
import org.team399.y2012.robot.Robot;

/**
 *
 * @author Jeremy
 */
public class KinectShootAuton {
    
    private static long startTime = 0;
    
    public static void start(long delayMillis) {
        startTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - startTime < delayMillis) {
            System.out.println("Delaying autonomous -- ShootTwoBalls");
        }
        
    }
                
    public static void run() {
        double autondrivespeeds = .7;   //the speed of the robot in auton

        double time1 = 1;             //start moving
        double time2 = 4;             //hit the bridge
        double time3 = 6;             //when to start backing away from the bridge
        double time4 = 10;            //stop time
        
        
        Robot.drive.shift(true);                            //shift to low
        //Robot.dropper.setState(Dropper.DROPPER_STATES.OUT); //drop dat dropper
        Robot.shooter.run();                                //move that shooter
                      
        //grab current turret postion and hold it!
        double turretposition = Robot.turret.getPosition();
        Robot.turret.setAngle(turretposition);

        //Drive towards the fender
        if(System.currentTimeMillis() - startTime > time1 * 1000 && System.currentTimeMillis() - startTime < time2 * 1000) 
            {
            Robot.drive.tankDrive(autondrivespeeds, -autondrivespeeds);  //drive forward
            
            }
            
        //stop at the fender
        
        if(System.currentTimeMillis() - startTime >time2 *1000 && System.currentTimeMillis() - startTime < time3 * 1000)
            {
            
            Robot.drive.tankDrive(0, 0);//stop
            Robot.shooter.setVelocity(2000);
            }
        
        
        //drive backwards from the bridge so that the balls roll towards us
        if(System.currentTimeMillis() - startTime > time3 * 1000 && System.currentTimeMillis() - startTime < time4 * 1000)
            {Robot.shooter.setVelocity(2000);
            Robot.drive.tankDrive(0, 0);  //drive backwards
            Robot.intake.set(-1);  //conveyor on
            
            }
        
        //drive backwards from the bridge so that the balls roll towards us
        if(System.currentTimeMillis() - startTime > time4 * 1000)
            {
            Robot.drive.tankDrive(0, 0);  //stop  
            Robot.intake.set(0); //conveyor on
            }
    }
}
