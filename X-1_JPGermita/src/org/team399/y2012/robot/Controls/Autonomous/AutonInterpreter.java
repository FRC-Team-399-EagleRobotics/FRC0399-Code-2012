/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Controls.Autonomous;

import org.team399.y2012.Utilities.StringUtils;

/**
 *
 * @author Jeremy
 */
public class AutonInterpreter {
    private String[][] m_parsedFile;
    
    public AutonInterpreter(String[][] parsedFile) {
        this.m_parsedFile = parsedFile;
    }
    
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
        
    }
    
    
}
