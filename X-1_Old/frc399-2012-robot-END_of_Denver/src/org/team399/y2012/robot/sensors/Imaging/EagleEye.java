/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.sensors.Imaging;

import edu.wpi.first.wpilibj.DriverStation;
import org.team399.y2012.config.RobotIOMap;
import org.team399.y2012.robot.sensors.Imaging.ImageTracker.Target;

/**
 * Eagle Eye class. Encapsulates camera, ImageTracker, and the RGB light ring into one class.
 * @author Jeremy Germita, Justin S. Jackie P.
 * 
 */
public class EagleEye {
    
    public LightRing ring;
    public Camera cam;
    
    private Target[] targets;
   
    //Color Thresholds
    Color[] colors = {Color.fromRGB(1, 1, 1)}; //Colors
    Threshold[] thresholds = { new Threshold(110, 130, 80, 255, 120, 255)};
    /**
     * Constructor
     */
    public EagleEye() {
        cam = new Camera(); //Init camera
        ring = new LightRing(RobotIOMap.LIGHT_RING_R, RobotIOMap.LIGHT_RING_G, RobotIOMap.LIGHT_RING_B); //init light ring
        if(DriverStation.getInstance().getAlliance().equals(DriverStation.Alliance.kBlue)) ring.setRGB(0,0,1);
        else if(DriverStation.getInstance().getAlliance().equals(DriverStation.Alliance.kRed)) ring.setRGB(1,0,0);
        else if(DriverStation.getInstance().getAlliance().equals(DriverStation.Alliance.kInvalid)) ring.setRGB(0,1,0);
        ring.setRGB(0,1,0);
        System.out.println("[EAGLE-EYE] Eagle Eye initialized");
    }

    int colorCount = 0;
    int loopCount = 0;
    
    boolean targetsFound = false;
     
    /**
     * Main run method. Call this for functionality.
     */
    public void run() {
        //ring.setRGB(colors[colorCount].getRed(),
        //            colors[colorCount].getGreen(),
        //            colors[colorCount].getBlue());
        loopCount++;
        if(cam.freshImage())
        {
            targets = ImageTracker.processImage(cam.getImage(), thresholds[0]);
            if(targets != null && targets.length > 0) 
                {
                targetsFound = true;
                System.out.println("[EAGLE-EYE] Targets Found!!! " +  targets.length);
                } 
            else 
                {
                targetsFound = false;
                }
        }
        
        
        if(loopCount % 50 == 0) {
            colorCount = (colorCount + 1) %  colors.length;//Math.min(colors.length, thresholds.length);
        }
    }

    /**
     * Return whether or not any target is found.
     * @return 
     */
    public boolean foundTarget() {
        return targetsFound;
    }
    
    /**
     * Returns a target based on its size
     * @param index lower index == smaller size
     * @return 
     */
    public Target getTarget(int index) {
        return targets[index];
    }
    
    
    public Target getTarget(String which) {
        Target buffer = (targets[0] == null) ? null : targets[0];
        if(which.equals("HIGH")) {
            for(int i = 0; i < targets.length; i++) {
                if(buffer.y < targets[i].y) buffer = targets[i];
            }
        } else if(which.equals("LEFT")) {
            for(int i = 0; i < targets.length; i++) {
                if(buffer.x < targets[i].x) buffer = targets[i];
            }
        } else if(which.equals("RIGHT")) {
            for(int i = 0; i < targets.length; i++) {
                if(buffer.x > targets[i].x) buffer = targets[i];
            }
        } else if(which.equals("LOW")) {
            for(int i = 0; i < targets.length; i++) {
                if(buffer.y > targets[i].y) buffer = targets[i];
            }
        }
        
        return buffer;
    }
    
    public Target[] getTargets() {
        return targets;
    }
    
    private String whichTarget = "";
    
    public void setTarget(String which) {
        whichTarget = which;
    }
    
    public Target getSetTarget() {
        return getTarget(whichTarget);
    }
    
    
    int demoCounter = 0;
    int prevDemoCtr = 0;
    public void demoMode() {
//        double[] ORANGE = {1.000, .2705, 0.000};
//        double[] PURPLE = {.5764, .4392, .8588};
//        double[] GREEN =  {0.000, 1.000, .0200};
//        double[] WHITE =  {.9019, 1.000, 1.000};
//        double[][] colors = {ORANGE, PURPLE, GREEN, WHITE};
//        
//        if(demoCounter > colors.length) demoCounter = 0;
//        if(prevDemoCtr > colors.length) prevDemoCtr = 0;
//        
//        double stepRate = 50;
//        
//        double rStep = colors[prevDemoCtr][0] - colors[demoCounter][0];
//        double gStep = colors[prevDemoCtr][1] - colors[demoCounter][1];
//        double bStep = colors[prevDemoCtr][2] - colors[demoCounter][2];
//        
//        rStep /= stepRate;
//        gStep /= stepRate;
//        bStep /= stepRate;
//        
//        for(int i = 0; i < 100; i++) {
//            ring.setRGB(colors[prevDemoCtr][0] - (rStep*i), colors[prevDemoCtr][1] - (gStep*i), colors[prevDemoCtr][1] - (bStep*i));
//        }
//        
//        prevDemoCtr = demoCounter;
//        demoCounter++;
        demoCounter++;
        
        if(demoCounter < 250) {
            ring.setRGB(1.0, 0.0, 0.0);
        } else if(demoCounter > 250 && demoCounter < 500) {
            ring.setRGB(.9019, 1.000, 1.000);
        } else if(demoCounter > 500 && demoCounter < 750) {
        } else if(demoCounter > 750) {
            demoCounter = 0;
        }
    }
}