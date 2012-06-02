/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Systems.Imaging;

import edu.wpi.first.wpilibj.DriverStation;
import org.team399.y2012.Utilities.EagleMath;
import org.team399.y2012.robot.Config.RobotIOMap;
import org.team399.y2012.robot.Systems.Imaging.ImageProcessor.Target;

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
    Color[] colors = {
        Color.fromRGB(0, 1, 0), //Green color
        Color.fromRGB(1, 0, 0), //Red color
        Color.fromRGB(0, 0, 1) //blue color
    }; //Colors
    Threshold[] thresholds = {
        new Threshold(110, 130, 80, 255, 120, 255), //Green threshold
        new Threshold(0, 45, 80, 255, 120, 255), //Red threshold
        new Threshold(200, 240, 80, 255, 120, 255) //Blue threshold
    };

    /**
     * Constructor
     */
    public EagleEye() {
        ring = new LightRing(RobotIOMap.LIGHT_RING_R, RobotIOMap.LIGHT_RING_G, RobotIOMap.LIGHT_RING_B); //init light ring
        ring.setRGB(0, 0, 0);
        cam = new Camera(); //Init camera
        if (DriverStation.getInstance().getAlliance().equals(DriverStation.Alliance.kBlue)) {
            ring.setRGB(0, 0, 1);
        } else if (DriverStation.getInstance().getAlliance().equals(DriverStation.Alliance.kRed)) {
            ring.setRGB(1, 0, 0);
        } else if (DriverStation.getInstance().getAlliance().equals(DriverStation.Alliance.kInvalid)) {
            ring.setRGB(0, 1, 0);
        }
        //ring.setRGB(0, 1, 0);
        System.out.println("[EAGLE-EYE] Eagle Eye initialized");
    }
    int colorCount = 0;
    int loopCount = 0;
    boolean targetsFound = false;
    int colorIndex = 0;
    long timeLastTarget = 0;

    /**
     * Main run method. Call this for functionality.
     */
    public void run() {
        //ring.setRGB(0, 1, 0);
        ring.set(colors[colorIndex]);

        loopCount++;
        if (cam.freshImage()) {

            targets = ImageProcessor.processImage(cam.getImage(), thresholds[colorIndex]);    //Process image from camera using given thresholds

            if (targets != null && targets.length > 0) {    //If targets are detected

                targetsFound = true;                        //Set flag to true
                System.out.println("[EAGLE-EYE] Targets Found!!! " + targets.length);   //Print number of targets
                timeLastTarget = System.currentTimeMillis();
            } else {
                targetsFound = false;
                if ((System.currentTimeMillis()- timeLastTarget) % 5000 < 4950) {
                    colorIndex++;
                    colorIndex = (colorIndex > colors.length) ? 0 : colorIndex;
                }
            }
        }
    }

    /**
     * Return whether or not any target is found.
     * @return 
     */
    public boolean foundTarget() {
        return targetsFound;
    }

    public Target[] getTargets() {
        return targets;
    }

    public Target getTallestTarget() {
        if (targets == null) {
            return null;
        }
        Target tallest = targets[0];
        for (int i = 1; i < targets.length; i++) {
            if (targets[i].y > tallest.y) {
                tallest = targets[i];
            }
        }
        return tallest;
    }

    public void demoMode() {
        long timer = System.currentTimeMillis();
        timer = timer % 16500;

        Color[] patriotic = {
            Color.fromRGB(1, 0, 0),
            Color.fromRGB(.9, 1, 1),
            Color.fromRGB(0, 1, 0),};

        Color[] teamSpirit = {
            Color.fromHSL(20, 255, 50),
            Color.fromHSL(120, 255, 50),
            Color.fromHSL(275, 255, 50),};

        Color[] demoColors = teamSpirit;


        if (EagleMath.isInBand(timer, 0, 5000)) {
            ring.set(demoColors[0]);
        } else if (EagleMath.isInBand(timer, 5500, 10500)) {
            ring.set(demoColors[1]);
        } else if (EagleMath.isInBand(timer, 11000, 15000)) {
            ring.set(demoColors[2]);
        } else {
            long fadeTimer = (timer % 500);
            double rStep = ring.getColor().getRed() / 500;
            double gStep = ring.getColor().getGreen() / 500;
            double bStep = ring.getColor().getBlue() / 500;
            ring.setRGB(ring.getColor().getRed() - (fadeTimer * rStep),
                    ring.getColor().getGreen() - (fadeTimer * gStep),
                    ring.getColor().getBlue() - (fadeTimer * bStep));
        }
    }
}