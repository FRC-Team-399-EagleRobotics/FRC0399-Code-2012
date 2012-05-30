/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.sensors.Imaging;

//import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.image.ColorImage;
//import org.team399.y2012.config.RobotIOMap;
import org.team399.y2012.robot.sensors.SensorBase;

/**
 * Camera Class. encapsulates camera and tilt servo code.
 * @author Jeremy
 */
public class Camera extends SensorBase{
    private AxisCamera camera = null;
    //private Servo tilt = null;
    
    
    public Camera() {
      //  tilt = new Servo(RobotIOMap.CAM_TILT_SERVO_PWM);
        try {
            camera = AxisCamera.getInstance();
        } catch (Exception e) {
            System.err.println("[CAMERA]Error initializing");
            e.printStackTrace();
        }
    }
    
    /*public double getTiltAngle() {
        return tilt.getAngle();
    }*/
    
    public boolean freshImage()
    {
        return camera.freshImage();
    }
    
    public ColorImage getImage() {
        try {
            return camera.getImage();
        } catch(Exception e) {
            System.out.println("Error Getting Camera Image");
            e.printStackTrace();
        }
        return null;
    }
    
   /* public void setTiltAngle(double angle) {
        tilt.setAngle(angle);
    }*/
}
