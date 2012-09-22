/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Systems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Solenoid;
import org.team399.y2012.Utilities.MovingAverage;
import org.team399.y2012.Utilities.PrintStream;
import org.team399.y2012.Utilities.RateLimitFilter;
import org.team399.y2012.robot.Config.RobotIOMap;

/**
 * Drivetrain class that encapsulates all basic drivetrain functions
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
    private MovingAverage m_pitchFilter = new MovingAverage(8);
    private RateLimitFilter m_yawFilter = new RateLimitFilter(.1);
    private PrintStream ps_drive = new PrintStream("[DRIVE] ");

    /**
     * Constructor
     */
    public DriveTrain() {
        ps_drive.println("Drivetrain Initialization Started...");
        shifter = new Solenoid(RobotIOMap.SHIFTER_PORT);

        //Initialize drive motor controllers in their own try-catch statements to catch individual errors
        ps_drive.println("LeftA Initialization started...");
        try {
            m_leftA = new CANJaguar(RobotIOMap.LEFT_DRIVE_A_ID);
            m_leftA.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            m_leftA.setVoltageRampRate(56);
            m_leftA.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            m_leftA.configFaultTime(.5);
        } catch (Exception e) {
            System.err.println("[DRIVE-LEFT-A]Error initializing");
            e.printStackTrace();
        }
        ps_drive.println("LeftA Initialization Complete!");
        ps_drive.println("LeftB Initialization started...");
        try {
            m_leftB = new CANJaguar(RobotIOMap.LEFT_DRIVE_B_ID);
            m_leftB.setVoltageRampRate(56);

            m_leftB.changeControlMode(CANJaguar.ControlMode.kPosition);
            m_leftB.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            m_leftB.configEncoderCodesPerRev(360);

            m_leftB.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            m_leftB.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            m_leftB.configFaultTime(.5);
        } catch (Exception e) {
            System.err.println("[DRIVE-LEFT-B]Error initializing");
            e.printStackTrace();
        }
        ps_drive.println("LeftB Initialization Complete!");
        ps_drive.println("RightA Initialization started...");
        try {
            m_rightA = new CANJaguar(RobotIOMap.RIGHT_DRIVE_A_ID);
            m_rightA.setVoltageRampRate(56);
            m_rightA.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            m_rightA.configFaultTime(.5);
        } catch (Exception e) {
            System.err.println("[DRIVE-RIGHT-A]Error initializing");
            e.printStackTrace();
        }
        ps_drive.println("RightA Initialization Complete!");
        ps_drive.println("RightB Initialization started...");
        try {
            m_rightB = new CANJaguar(RobotIOMap.RIGHT_DRIVE_B_ID);
            m_rightB.setVoltageRampRate(56);
            m_rightB.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            m_rightB.configFaultTime(.5);
        } catch (Exception e) {
            System.err.println("[DRIVE-RIGHT-B]Error initializing");
            e.printStackTrace();
        }
        ps_drive.println("RightB Initialization Complete!");
        ps_drive.println("CAN Jaguar initialization complete");
        yaw.reset();
        pitch.reset();

        ps_drive.println("Gyro initialization complete");
        ps_drive.println("Drivetrain initialization complete!");
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
     * Set drivetrain to coast
     * Used a combination of brake/coast to maintain handling ability
     */
    public void coast() {
        try {
            m_leftA.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            m_leftB.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            m_rightA.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            m_rightB.configNeutralMode(CANJaguar.NeutralMode.kBrake);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set drivetrain to brake
     */
    public void brake() {
        try {
            m_leftA.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            m_leftB.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            m_rightA.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            m_rightB.configNeutralMode(CANJaguar.NeutralMode.kBrake);
        } catch (Exception e) {
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

        //Convert inputs for processing
        double wheel = twoStickToTurning(left, right);
        double throttle = twoStickToThrottle(left, right);

        //Adjusts turning sensitivity value depending on high/low gear
        if (!shifter.get()) {
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
     * Drive algorithm written by Jeremy. Adjusts turning sensitivity with throttle.
     * Dynamic brake/coasting based on gear
     * @param left left joystick input
     * @param right right joystick input
     * @param gear shifting input, hold for high gear
     */
    public void iCantBelieveItsNotButterDrive(double left, double right, boolean gear) {
        //convert two stick commands to arcade throttle
        double throttle = twoStickToThrottle(left, right);

        //convert two stick commands to arcade turning
        double turning = twoStickToTurning(left, right);

        //scalar value for turning desensitivity
        double e_tSens = .60;
        //Turn limiting scalar, based on throttle
        double tLim = (1.2 - Math.abs(throttle)) * e_tSens;

        //High gear
        if (!gear) {
            //Apply turn scaling if in high gear
            turning *= tLim;
            //turning = (turning*turning*turning);
            highGear();
            //Put drivetrain into coast for high gear
            coast();
        } //Low gear
        else {
            lowGear();
            //Put drivetrain into brake for low gear
            brake();
        }

        tankDrive((throttle + turning), -(throttle - turning));	//Output
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
        double pitchVal = pitch.getAngle();

        return m_pitchFilter.calculate(pitchVal);
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
    private int pl_counter = 0; //PID lock run counter
    private double pl_pos = 0, pl_err = 0, pl_p = -1.00;
    private double pl_deadband = .1250;

    /**
     * PID positional lock for bridge balancing and defensive maneuvers
     * @param enabled 
     */
    public void PIDLock(boolean enabled) {
        if (enabled) {
            lowGear();
            if (pl_counter == 0) {
                try {
                    pl_pos = m_leftB.getPosition();
                } catch (Exception e) {
                }
            }
            try {
                pl_err = pl_pos - m_leftB.getPosition();

            } catch (Exception e) {
            }

            if (Math.abs(pl_err) < pl_deadband) {
                tankDrive(0, 0);
            } else {
                double power = pl_err * pl_p;
                tankDrive(power, -power);
            }

            pl_counter++;
        } else {
            pl_counter = 0;
        }
    }

    /**
     * Increment the pid loop setpoint for fine adjustment
     * @param counts the number of encoder counts to incrememnt the setpoint 
     */
    public void PIDLockIncrement(double counts) {
        pl_pos += counts;
    }
    
    
}
