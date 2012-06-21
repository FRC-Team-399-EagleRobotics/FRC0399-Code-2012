/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Systems.Imaging;

import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.image.ColorImage;

/**
 * Camera Class. encapsulates camera and tilt servo code.
 * @author Jeremy
 */
public class Camera {

    private AxisCamera camera = null;

    /**
     * Constructor
     */
    public Camera() {
        try {
            //camera = AxisCamera.getInstance();
        } catch (Exception e) {
            System.err.println("[CAMERA]Error initializing");
            e.printStackTrace();
        }
    }

    /**
     * Returns true if the camera has a new image
     * @return 
     */
    public boolean freshImage() {
        return camera.freshImage();
    }

    /**
     * Color image, ready for processing
     * @return 
     */
    public ColorImage getImage() {
        try {
            return camera.getImage();
        } catch (Exception e) {
            System.out.println("Error Getting Camera Image");
            e.printStackTrace();
        }
        return null;
    }
}
