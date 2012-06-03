/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Controls.Autonomous;

import org.team399.y2012.Utilities.StringUtils;
import org.team399.y2012.robot.Main;

/**
 *
 * @author Jeremy
 */
public class AutonInterpreter {
    private String[][] m_parsedFile;
    
    public AutonInterpreter(String[][] parsedFile) {
        this.m_parsedFile = parsedFile;
    }
    long commandStartTime = 0;
    
    public void run() {
        long commandStartTime = System.currentTimeMillis();
        for(int i = 0; i < m_parsedFile.length; i++) {  //Cycling through file lines
            System.out.println("Line: " + i);
            
            String currCommand = m_parsedFile[i][0].toUpperCase();
            System.out.println("Command: " + currCommand);
            double[] args = new double[m_parsedFile[i].length];
            for(int j = 0; j < m_parsedFile[i].length; j++) {
                args[j] = StringUtils.toDouble(m_parsedFile[i][j]);
            }
            
            while(!doLine(currCommand, args)){
                System.out.println("Doing command: " + currCommand);
                System.out.println("Time since new command: " + ((System.currentTimeMillis() - commandStartTime)/1000));
            }
        }
    }
    
    private boolean doLine(String command, double[] args) {
        
        if(command.equals("STOP")) {
            
            return true;
            
        } else if(command.equals("WAIT")) {
            
            if(System.currentTimeMillis() - commandStartTime < args[0]) {
                return false;
            }
            
            return true;
            
        } else if(command.equals("DRIVE_POWER")){
            Main.bot.drive.tankDrive(args[0], args[1]);
        } else if(command.equals("SHOOTER")) {
            Main.bot.shooter.setVelocity(args[0]);
        } else if(command.equals("BELT")) {
            Main.bot.intake.setIntake(args[0]);
        } else if(command.equals("DROPPER")) {
            Main.bot.intake.setDropper(args[0] == 1.0);
        } else if(command.equals("AUTOSHOOT")) {
            Main.bot.shootController.shoot(args[1], args[0]);
        } else if(command.equals("DRIVE_DISTANCE")) {
            //Autodrivetrain controller stuff here
        }
        
        return true;
    }
    
}
