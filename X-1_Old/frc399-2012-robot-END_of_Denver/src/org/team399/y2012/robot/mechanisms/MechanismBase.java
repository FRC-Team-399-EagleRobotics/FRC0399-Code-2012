/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.mechanisms;

/**
 * Base interface for mechanisms
 * @author Jeremy
 */
public class MechanismBase extends Thread {
    private static MechanismBase instance = null;
    private MechanismState prevState, currState;
    
    /**
     * Constructor.
     */
    public MechanismBase() {
        
    }
    
    /**
     * Default run method. Override in subclass to provide functionality
     */
    public void run() {
        System.out.println("Default Mechanism thread running. Consider writing your own run method to add functionality :)");
    }
    
    /**
     * Get instance
     * @return current instance of MechanismBase
     */
    public static MechanismBase getInstance() {
        if(instance == null) {
            instance = new MechanismBase();
        }
        
        return instance;
    }
    
    public void setState(MechanismState newState) { 
        prevState = currState;
        currState = newState;
    }
    
    
    
}
