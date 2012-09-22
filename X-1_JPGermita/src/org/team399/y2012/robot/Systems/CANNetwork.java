/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Systems;

import edu.wpi.first.wpilibj.CANJaguar;
import java.util.Vector;

/**
 * Thread safe CAN Network wrapper
 * @author Jeremy
 */
public class CANNetwork {

    private static CANNetwork m_instance = null;
    private static short MAX_DEVICE_COUNT = 10;
    private Vector devices;

    public CANNetwork() {
        devices = new Vector(MAX_DEVICE_COUNT);
        devices.ensureCapacity(MAX_DEVICE_COUNT);
        devices.addElement(null); //Null first object so first jaguar in network doesn't occupy ID1
    }

    public synchronized static CANNetwork getInstance() {
        if (m_instance == null) {
            m_instance = new CANNetwork();
        }

        return m_instance;
    }

    public synchronized void add(String name, int id) {
        boolean success = false;
        int attempts = 0;
        while (!success || attempts > 10) {
            try {
                devices.addElement(new CANJaguar(id));
                success = true;
            } catch (Exception e) {
                success = false;
                System.out.println("Error adding device to CAN Network");
                System.out.println("Attempt number: " + attempts);
                e.printStackTrace();
            }
            attempts++;
        }
    }
    
    public synchronized void setX(int id, double value) {
        
    }

    /*
     * TODO: 
     * -initialization configuaration methods
     * -get/set methods
     * 
     */
}
