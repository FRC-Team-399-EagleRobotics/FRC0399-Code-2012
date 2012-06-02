/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Systems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Solenoid;
import org.team399.y2012.robot.Config.RobotIOMap;

/**
 *
 * @author Jeremy
 */
public class DriveTrain {

    private CANJaguar m_leftA = null;
    private CANJaguar m_leftB = null;
    private CANJaguar m_rightA = null;
    private CANJaguar m_rightB = null;
    private Solenoid shifter = null;
    private Gyro yaw = new Gyro(RobotIOMap.GYRO_YAW);
    private Gyro pitch = new Gyro(RobotIOMap.GYRO_PITCH);

    /**
     * Constructor
     */
    public DriveTrain() {
        shifter = new Solenoid(RobotIOMap.SHIFTER_PORT);

        //Initialize drive motor controllers in their own try-catch statements to catch individual errors
        try {
            m_leftA = new CANJaguar(RobotIOMap.LEFT_DRIVE_A_ID);
            //m_leftA.changeControlMode(CANJaguar.ControlMode.kVoltage);
            m_leftA.setVoltageRampRate(56);
            m_leftA.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            m_leftA.configFaultTime(.5);
        } catch (Exception e) {
            System.err.println("[DRIVE-LEFT-A]Error initializing");
            e.printStackTrace();
        }
        try {
            m_leftB = new CANJaguar(RobotIOMap.LEFT_DRIVE_B_ID);
            //m_leftB.changeControlMode(CANJaguar.ControlMode.kVoltage);
            m_leftB.setVoltageRampRate(56);
            m_leftB.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            m_leftB.configFaultTime(.5);
        } catch (Exception e) {
            System.err.println("[DRIVE-LEFT-B]Error initializing");
            e.printStackTrace();
        }
        try {
            m_rightA = new CANJaguar(RobotIOMap.RIGHT_DRIVE_A_ID);
            //m_rightA.changeControlMode(CANJaguar.ControlMode.kVoltage);
            m_rightA.setVoltageRampRate(56);
            m_rightA.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            m_rightA.configFaultTime(.5);
        } catch (Exception e) {
            System.err.println("[DRIVE-RIGHT-A]Error initializing");
            e.printStackTrace();
        }
        try {
            m_rightB = new CANJaguar(RobotIOMap.RIGHT_DRIVE_B_ID);
            //m_rightB.changeControlMode(CANJaguar.ControlMode.kVoltage);
            m_rightB.setVoltageRampRate(56);
            m_rightB.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            m_rightB.configFaultTime(.5);
        } catch (Exception e) {
            System.err.println("[DRIVE-RIGHT-B]Error initializing");
            e.printStackTrace();
        }

        yaw.reset();
        pitch.reset();
    }

    /**
     * Basic tank drive method
     * @param leftPower left power to set
     * @param rightPower right power to set
     */
    public void tankDrive(double leftPower, double rightPower) {
        try {
            m_leftA.setX(leftPower);
        } catch (Exception e) {
            System.err.println("[DRIVE-LEFT-A]Error sending power");
            e.printStackTrace();
        }
        try {
            m_leftB.setX(leftPower);
        } catch (Exception e) {
            System.err.println("[DRIVE-LEFT-B]Error sending power");
            e.printStackTrace();
        }
        try {
            m_rightA.setX(rightPower);
        } catch (Exception e) {
            System.err.println("[DRIVE-RIGHT-A]Error sending power");
            e.printStackTrace();
        }
        try {
            m_rightB.setX(rightPower);

        } catch (Exception e) {
            System.err.println("[DRIVE-RIGHT-B]Error sending power");
            e.printStackTrace();
        }
    }

    /**
     * Shift gears
     * @param gear True for low gear, false for high 
     */
    public void shift(boolean gear) {
        shifter.set(gear);
    }
    double tSens = 1.5;                     //Cheesy drive turning sensitivity scalar
    final double TURBO_MODE_TSENS = 1.125;      //Turbo mode turning sensitivity
    final double NORMAL_MODE_TSENS = 1.25;   //Normal(slow) mode turning sensitivity
    final double NORMAL_MODE_SCALAR = .9;   //Scalar for making normal mode slower than turbo mode

    /**
     * Cheesy drive method from 125's code, derived from 254's code, modified by me to take tank inputs
     * @param throttle Throttle input
     * @param wheel Turning input
     * @param quickTurn quick turn mode boolean input
     */
    public void cheesyDrive(double left, double right, boolean quickTurn) {

        double wheel = twoStickToTurning(left, right);      //Convert inputs for processing
        double throttle = twoStickToThrottle(left, right);

        if (!shifter.get()) {           //Adjuts turning sensitivity value depending on high/low gear
            tSens = TURBO_MODE_TSENS;
        } else {
            tSens = NORMAL_MODE_TSENS;
        }
        
        double angular_power = 0.0;
        double overPower = 0.0;
        double sensitivity = tSens;
        double rPower = 0.0;
        double lPower = 0.0;

        if (quickTurn) {
            overPower = 1.0;
            sensitivity = 1.0;
            angular_power = wheel;
        } else {
            overPower = 0.0;
            angular_power = Math.abs(throttle) * wheel * sensitivity;
        }

        rPower = lPower = throttle;
        lPower += angular_power;
        rPower -= angular_power;

        if (lPower > 1.0) {
            rPower -= overPower * (lPower - 1.0);
            lPower = 1.0;
        } else if (rPower > 1.0) {
            lPower -= overPower * (rPower - 1.0);
            rPower = 1.0;
        } else if (lPower < -1.0) {
            rPower += overPower * (-1.0 - lPower);
            lPower = -1.0;
        } else if (rPower < -1.0) {
            lPower += overPower * (-1.0 - rPower);
            rPower = -1.0;
        }

        tankDrive(lPower, -rPower);
    }

    /**
     * Converts tank Y axis values into a throttle value
     * @param leftIn Left Y axis
     * @param rightIn Right Y axis
     * @return Throttle output
     */
    private double twoStickToThrottle(double leftIn, double rightIn) {
        double answer = leftIn + rightIn;
        answer /= 2;
        return answer;
    }

    /**
     * Converts tank Y axis values into a turning value
     * @param leftIn left Y axis
     * @param rightIn right Y axis
     * @return Turning output
     */
    private double twoStickToTurning(double leftIn, double rightIn) {
        double answer = leftIn - rightIn;
        answer /= 2;
        return answer;
    }

    /**
     * Get the angle from the yaw gyro. Uses some logic to normalize angle to 0-360 range
     * @return
     */
    public double getAngleWraparound() {
        double angle = yaw.getAngle();
        if (angle < 0.0) {
            angle += 360;
        } else if (angle > 360.0) {
            angle -= 360;
        }
        return angle;
    }

    /**
     * Get gyro yaw angle
     * @return
     */
    public double getAngle() {
        return yaw.getAngle();
    }

    /**
     * Get gyro pitch angle
     * @return 
     */
    public double getPitch() {
        return pitch.getAngle();
    }

    /**
     * Reset gyros
     */
    public void resetGyros() {
        yaw.reset();
        pitch.reset();
    }
    
    double error = 0;
    double prevError = 0;

    /**
     * Drive to a specified angle. 
     * @param throttle Throttle to drive forward
     * @param angle angle to drive to
     */
    public void driveToAngle(double throttle, double angle) {
        double P = -.3,
                D = 0;
        error = angle - getAngle();
        double proportional = P * error;
        double derivative = error - prevError;
        double PID_Out = proportional - D * derivative;
        prevError = error;
//        System.out.println("Angle: " + getAngle());
        tankDrive((throttle) - PID_Out, (throttle) + PID_Out);
    }

    
    /**
     * Puts the drivetrain into low gear
     */
    public void lowGear() {
        shifter.set(true);
    }
    
    /**
     * Puts the drivetrain into high gear
     */
    public void highGear() {
        shifter.set(false);
    }
}
