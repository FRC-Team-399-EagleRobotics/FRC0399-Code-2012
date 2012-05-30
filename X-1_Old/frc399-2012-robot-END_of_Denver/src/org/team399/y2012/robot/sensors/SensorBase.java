/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.sensors;

/**
 * Sensor Base Class
 * @author Jeremy
 */
public class SensorBase extends Thread { 
    private SensorBase instance = null;
    
    /**
     * Constructor
     */
    public SensorBase() {
        
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
    public synchronized SensorBase getInstance() {
        if(instance == null) {
            instance = new SensorBase();
        }
        
        return instance;
    }
}
