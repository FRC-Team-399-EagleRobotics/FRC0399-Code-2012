/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2012.robot;
import edu.wpi.first.wpilibj.Victor;

/**
 * Drive code wrapper class
 * @author Jeremy Germita, et al
 */
public class SPOilerDrive {
    //NOTES:
    //Thanks to Austin Schuh, Tom Bottiglieri teams 254, 971, and 125 for the cheesy drive code and victor linearization class

    double tSens = 1.5;                     //Cheesy drive turning sensitivity scalar
    final double TURBO_MODE_TSENS = 2;      //Turbo mode turning sensitivity
    final double NORMAL_MODE_TSENS = 1.25;   //Normal(slow) mode turning sensitivity
    final double NORMAL_MODE_SCALAR = .9;   //Scalar for making normal mode slower than turbo mode

    boolean isTurboMode = true;             //Defaults to turbo mode. Turbo mode is full speed

    private LinearVictor LEFT_MOTOR_A, LEFT_MOTOR_B, RIGHT_MOTOR_A, RIGHT_MOTOR_B;    //Drive victor objects

    /**
     * Constructor
     * @param leftA Left motor port
     * @param leftB Left secondary motor port. If there is no secondary motor, make this the same as leftA
     * @param rightA right motor port
     * @param rightB Right secondary motor. If there is no secondary, make this the same as rightA
     */
    public SPOilerDrive(int leftA, int leftB, int rightA, int rightB) {
        LEFT_MOTOR_A = new LinearVictor(leftA);     //Creates a Linear Victor
        if(leftB != leftA) {                        //If there is a second motor
            LEFT_MOTOR_B = new LinearVictor(leftB); //Construct one
        }

        RIGHT_MOTOR_A = new LinearVictor(rightA);
        if(rightB != rightA) {
            RIGHT_MOTOR_B = new LinearVictor(rightB);
        }
    }

    /**
     * Raw Tank drive method
     * @param leftP Left Motor power
     * @param rightP right motor power
     */
    public void tankDrive(double leftP, double rightP) {
        LEFT_MOTOR_A.set(leftP);
        RIGHT_MOTOR_A.set(-rightP);

        if(LEFT_MOTOR_B != null) {
            LEFT_MOTOR_B.set(leftP);
        }

        if(RIGHT_MOTOR_B != null) {
            RIGHT_MOTOR_B.set(-rightP);
        }
    }

    /**
     * Tank drive method with turbo mode
     * @param leftP
     * @param rightP
     * @param turboMode
     */
    public void tankDrive(double leftP, double rightP, boolean turboMode) {
        double scale_factor = (!turboMode) ? NORMAL_MODE_SCALAR : 1;
        isTurboMode = turboMode;
        tankDrive(leftP*scale_factor, rightP*scale_factor);
    }
    
    /**
     * Tank drive method with turbo mode
     * @param leftP
     * @param rightP
     * @param turboMode
     */
    public void tankDrive(double leftP, double rightP, boolean turboMode, boolean linear) {
        double scale_factor = (!turboMode) ? NORMAL_MODE_SCALAR : 1;
        isTurboMode = turboMode;
        if(!linear) {
            tankDrive(leftP*scale_factor, rightP*scale_factor);
         } else {
                LEFT_MOTOR_A.setNonLin(leftP);
            RIGHT_MOTOR_A.setNonLin(-rightP);
 
            if(LEFT_MOTOR_B != null) {
                LEFT_MOTOR_B.setNonLin(leftP);
            }

            if(RIGHT_MOTOR_B != null) {
                RIGHT_MOTOR_B.setNonLin(-rightP);
            }
        }
        
    }

    /**
     * Converts tank Y axis values into a throttle value
     * @param leftIn Left Y axis
     * @param rightIn Right Y axis
     * @return Throttle output
     */
    public double twoStickToThrottle(double leftIn, double rightIn) {
        double answer = leftIn + rightIn;
        answer /=2;
        return answer;
    }

    /**
     * Converts tank Y axis values into a turning value
     * @param leftIn left Y axis
     * @param rightIn right Y axis
     * @return Turning output
     */
    public double twoStickToTurning(double leftIn, double rightIn) {
        double answer = leftIn - rightIn;
        answer /=2;
        return answer;
    }


    /**
     * Cheesy drive method from 125's code, derived from 254's code
     * @param throttle Throttle input
     * @param wheel Turning input
     * @param quickTurn quick turn mode boolean input
     */
    
     public void cheesyDrive(double throttle, double wheel, boolean quickTurn, boolean turbo_button) {

        if(turbo_button) {
            tSens = TURBO_MODE_TSENS;
        } else {
            tSens = NORMAL_MODE_TSENS;
        }
        double angular_power = 0.0;
        double overPower = 0.0;
        double sensitivity = tSens;
        double rPower = 0.0;
        double lPower = 0.0;

        if(quickTurn) {
            overPower = 1.0;
            sensitivity = 1.0;
            angular_power = wheel;
        }
        else {
            overPower = 0.0;
            angular_power = Math.abs(throttle) * wheel * sensitivity;
        }

        rPower = lPower = throttle;
        lPower += angular_power;
        rPower -= angular_power;

        if(lPower > 1.0) {
            rPower -= overPower * (lPower - 1.0);
            lPower = 1.0;
        }
        else if(rPower > 1.0) {
            lPower -= overPower * (rPower - 1.0);
            rPower = 1.0;
        }
        else if(lPower < -1.0) {
            rPower += overPower * (-1.0 - lPower);
            lPower = -1.0;
        }
        else if(rPower < -1.0) {
            lPower += overPower * (-1.0 - rPower);
            rPower = -1.0;
        }

        tankDrive(lPower, rPower, turbo_button);
    }
}
