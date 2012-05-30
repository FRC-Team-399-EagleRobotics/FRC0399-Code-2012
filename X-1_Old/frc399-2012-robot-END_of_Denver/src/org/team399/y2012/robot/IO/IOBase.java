/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.IO;

/**
 * Base IO class. provides functionality for IO channels such as DIO, AI, CAN, etc
 * @author Jeremy
 */
public class IOBase extends Thread {
    private static IOBase instance = null;
    
    /**
     * Constructor
     */
    public IOBase() {
        
    }
    
    /**
     * Default IOBase run
     */
    public void run() {
        System.out.println("Default IO thread running. Consider writing your own run method to add functionality :)");
    }
    
    /**
     * Gets current instance of IOBase
     * @return current instance
     */
    public static synchronized IOBase getInstance() {
        if(instance == null) {
            instance = new IOBase();
        }
        
        return instance;
    }
}
