/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Controls.Autonomous;

import org.team399.y2012.Utilities.EagleMath;
import org.team399.y2012.Utilities.StringUtils;
import org.team399.y2012.robot.Main;

/**
 * Class to interpret data parsed from file
 * @author Jeremy
 */
public class AutonInterpreter {

    private String[][] m_parsedFile;
    long autonStartTime = 0;

    /**
     * Constructor
     * @param parsedFile string array containing the data from the parsed file
     */
    public AutonInterpreter(String[][] parsedFile) {
        this.m_parsedFile = parsedFile;
        autonStartTime = System.currentTimeMillis();
    }
    long commandStartTime = 0;
    
    /**
     * Call this to run the autonomous from file
     */
    public void run() {
        long commandStartTime = System.currentTimeMillis();
        //if ((15000 - (System.currentTimeMillis() - autonStartTime)) < 10000) {
            for (int i = 0; i < m_parsedFile.length; i++) {  //Cycling through file lines
                System.out.println("Line: " + i);

                String currCommand = m_parsedFile[i][0].toUpperCase();
                System.out.println("Command: " + currCommand);
                if (currCommand.startsWith("%") || currCommand.startsWith("#")) {
                    break;  //Encountered a comment line. ignore it
                }
                double[] args = new double[m_parsedFile[i].length];
                if (currCommand.equals("STOP")) {
                    break;
                }
                for (int j = 1; j < m_parsedFile[i].length; j++) {
                    args[j] = StringUtils.toDouble(m_parsedFile[i][j].trim());
                }

                while (!doLine(currCommand, args)) {
                    System.out.println("Doing command: " + currCommand);
                    System.out.println("Time since new command: " + ((System.currentTimeMillis() - commandStartTime) / 1000));
                }
            }
        //}else {
        //}

    }

    private boolean doLine(String command, double[] args) {

        if (command.equals("STOP")) {
            return true;
        } else if (command.equals("WAIT")) {
            if (System.currentTimeMillis() - commandStartTime < args[0]) {
                System.out.println("Waiting for " + args[0] + " ms");
                return false;
            }
            return true;
        } else if (command.equals("DRIVE_POWER")) {
            System.out.println("Driving");
            //Main.bot.drive.tankDrive(args[0]/100, args[1]/100);
            if (args[2] == 0) {
                Main.bot.drive.lowGear();
            } else {
                Main.bot.drive.highGear();
            }
        } else if (command.equals("SHOOTER")) {
            Main.bot.shooter.setVelocity(args[0]);
        } else if (command.equals("BELT")) {
            Main.bot.intake.setIntake(args[0]);
        } else if (command.equals("DROPPER")) {
            Main.bot.intake.setDropper(args[0] == 1.0);
        } else if (command.equals("AUTOSHOOT")) {
            if(System.currentTimeMillis() - commandStartTime < args[2]) {
                Main.bot.shootController.shoot(args[1], args[0]);
            } else {
                return true;
            }
        } else if (command.equals("DRIVE_DISTANCE")) {
            //Autodrivetrain controller stuff here
        } else if (command.endsWith("TURN_ANGLE")) {
            Main.bot.drive.driveToAngle(0, args[0]);
            if(System.currentTimeMillis() - commandStartTime < args[1]) {
                if (EagleMath.isInBand(Math.abs(args[0] - Main.bot.drive.getAngle()), 0, 10)) {
                    return true;
                }
            } else {
                return true;
            }
            return false;
        }
        return true;
    }
}