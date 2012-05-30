/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.mechanisms;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import org.team399.y2012.config.RobotIOMap;
import org.team399.y2012.robot.Robot;
import org.team399.y2012.util.EagleMath;

/**
 *
 * @author Jeremy
 */
public class Shooter extends MechanismBase {

    public void setState(MechanismState state) {
        if (state.equals(SHOOTER_STATES.OFF)) {
            setRollersV(0);
        }
    }

    public static class SHOOTER_STATES {
        public static MechanismState SHOOT = new MechanismState("SHOOT");   //Shoots a ball;
        public static MechanismState OFF = new MechanismState("OFF");
    }
    
    public double getHoodJagX() {
        double answerX = 0;
        try {
            answerX = m_shooterHood.getX();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return answerX;
    }
    
    
    
    
    public double getHoodPot() {
        double answer = 0;
        try {
            answer = m_shooterHood.getPosition();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return answer;
    }
    
        public void getHoodControlStyle() {
        
        try {
            System.out.println("Hood Jag control"+ m_shooterHood.getControlMode()); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    
        public double getHoodVoltage() {
        double voltageanswer = 0;
        try {
            voltageanswer = m_shooterHood.getOutputVoltage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return voltageanswer;
    }
    
    private boolean isShooterManual = false;
    private boolean isHoodManual = false;

    public void setShooterManual(boolean state) {
        isShooterManual = state;
    }

    public void setHoodManual(boolean state) {
        isHoodManual = state;
    }

    private static class SHOOTER_VALUES {
        public static long MAX_ROLLER_SPEED = 4800;
        public static double MAX_RANGE_INCHES = 622;
    }
    
    private double[][] RPM_TO_RANGE_LUT = {
        {},
        {}
    };

    private double rpmToRangeSmooth(double RPM) {
        return RPM;
    }
    private CANJaguar m_shooterA = null;
    private CANJaguar m_shooterB = null;
    private CANJaguar m_shooterHood = null;
    private double currentVelocity = 0.0;
    private double currentAngle = 0.0;

    /**
     * Constructor
     * @param ROLLER_CAN_ID CAN ID for main roller
     * @param HOOD_CAN_ID CAN ID for hood
     */
    public Shooter() {
        try {
            m_shooterA = new CANJaguar(RobotIOMap.SHOOTER_A_ID);
            m_shooterA.setVoltageRampRate(48);
            m_shooterA.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            m_shooterA.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            m_shooterA.changeControlMode(CANJaguar.ControlMode.kPosition);
            m_shooterA.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            m_shooterA.configEncoderCodesPerRev(250);
            m_shooterA.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            m_shooterA.configFaultTime(.5);
            //m_shooterA.changeControlMode(CANJaguar.ControlMode.kVoltage);   //Voltage mode 
        } catch (Exception e) {
            System.err.println("[SHOOTER-A]Error initializing");
            e.printStackTrace();
        }
        try {
            m_shooterB = new CANJaguar(RobotIOMap.SHOOTER_B_ID);
            m_shooterB.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            m_shooterB.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            //m_shooterB.changeControlMode(CANJaguar.ControlMode.kVoltage);   //Voltage mode 
            m_shooterB.setVoltageRampRate(48);
            m_shooterA.configFaultTime(.5);
        } catch (Exception e) {
            System.err.println("[SHOOTER-B]Error initializing");
            e.printStackTrace();
        }
        try {
            m_shooterHood = new CANJaguar(RobotIOMap.HOOD_ID);

            m_shooterHood.changeControlMode(CANJaguar.ControlMode.kPosition);
            m_shooterHood.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
            m_shooterHood.configPotentiometerTurns(1);
            m_shooterHood.configSoftPositionLimits(.777,.960);
            
            //COMMENT THIS STUFF IF IT DOESNT WORK
            //m_shooterHood.setPID(-20, 0, 0);
            m_shooterHood.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            //m_shooterHood.changeControlMode(CANJaguar.ControlMode.kPosition);
        } catch (Exception e) {
            System.err.println("[SHOOTER-HOOD]Error initializing");
            e.printStackTrace();
        }
    }

    /**
     * Set angle of exit for payload
     * @param angle Angle in degrees relative to the ground
     */
    public void setAngle(double angle) {
        try {
            m_shooterHood.setX((double) EagleMath.map((float) angle, (float) 40, (float) 80, (float) .785, (float) .970));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setV() {
    }
    private double setPointV = 0;
    private double err = 0, prevErr = err, prevPrevErr = err;
    private double pos = 0, prevPos = 0;

    /**
     * Set velocity for payload.
     * @param velocity Velocity in meters per second
     */
    public void setVelocity(double velocity) {
        setPointV = velocity;
    }
    double out = 0;

    /**
     * 
     * @param P
     * @param I
     * @param D
     * @param K 
     */
    public void update(double P, double I, double D, double K) {
        prevPrevErr = prevErr;
        prevErr = err;
        err = setPointV - getEncoderRate();

        out += -.000025 * (P * (err - prevErr) + I * err + D * (err - 2 * prevErr + prevPrevErr) + K * setPointV);

        if (out > 1) {
            out = 1;
        }
        if (out < -1) {
            out = -1;
        }
        setRollersV(out);
        System.out.println("[SHOOTER] PIDK Output: " + out);
        System.out.println("[SHOOTER] %Actual: " + getEncoderRate() / setPointV);
        //System.out.println("[SHOOTER] Error: " + err);
        System.out.println("");
    }

    /**
     * 
     */
    public void disable() {
        out = 0;
    }
    private double vel = 0;
    private final double a = 0.5;
    private double prevT = System.currentTimeMillis();

    public double getEncoderRate() {
        double scalar = -85423.972664328747414800827263735; //-542.63565891472*3.0;
        try {
            prevPos = pos;
            pos = m_shooterA.getPosition();
            double time = System.currentTimeMillis();
            double newVel = (pos - prevPos) / (time - prevT);
            prevT = time;
            vel = vel * a + (1 - a) * newVel; // This is code
            return vel * scalar;          // also code
        } catch (Exception e) {
            e.printStackTrace();        //Code that shouldn't run.
        }

        return 0.0;
    }

    public double inchesToRPM(double inches, double angle) {
        inches = Math.abs(inches);

        double inchesPerRPM = .05134;
        if (inches > SHOOTER_VALUES.MAX_RANGE_INCHES) {
            inches = SHOOTER_VALUES.MAX_RANGE_INCHES;
        }
        double scalar = 10.0;

        return (inches / inchesPerRPM) * scalar;
    }

    public void run() {
        update(5, 4, 3, .2);

        if (!isShooterManual) {
            double distance;
            if (Robot.eye.foundTarget()) {
                distance = Robot.eye.getTarget(0).distance;
            } else {
                distance = 0;
            }
            setVelocity(inchesToRPM(distance, 40));
        }


    }

    /**
     * Set roller speed
     * @param speed speed in percent of battery voltage
     */
    public void setRollersV(double speed) {
        try {
//            m_shooterA.changeControlMode(CANJaguar.ControlMode.kVoltage);
//            m_shooterB.changeControlMode(CANJaguar.ControlMode.kVoltage);
//            
            System.out.println("speed:" + speed);
            m_shooterA.setX(-speed);//, RobotIOMap.SHOOTER_SYNC_ID);
            m_shooterB.setX(speed);//, RobotIOMap.SHOOTER_SYNC_ID);
            //CANJaguar.updateSyncGroup(RobotIOMap.SHOOTER_SYNC_ID);

            //        print();
            //CANJaguar.updateSyncGroup(RobotIOMap.SHOOTER_SYNC_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set roller speed
     * @param speed speed in RPM
     */
    /*public void setRollers(float speed) {
    speed = (Math.abs(speed) > SHOOTER_VALUES.MAX_ROLLER_SPEED) ? SHOOTER_VALUES.MAX_ROLLER_SPEED : speed;  //Clamp speed to max roller speed
    
    double voltage = 0;
    try {
    m_shooterA.changeControlMode(CANJaguar.ControlMode.kVoltage);
    m_shooterB.changeControlMode(CANJaguar.ControlMode.kVoltage);
    m_shooterA.setX(-voltage, RobotIOMap.SHOOTER_SYNC_ID);
    m_shooterB.setX(voltage, RobotIOMap.SHOOTER_SYNC_ID);
    CANJaguar.updateSyncGroup(RobotIOMap.SHOOTER_SYNC_ID);
    } catch (Exception e) {
    e.printStackTrace();
    }
    }*/
    public void setHood(double speed) {
        try {
            //m_shooterHood.setX(speed * .5);
            //m_shooterHood.changeControlMode(CANJaguar.ControlMode.kPosition);
            
            m_shooterHood.setX(speed);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void print() {
        //System.out.println(toString());
        //System.out.println("[SHOOTER] Cout " + getCurrentOut());
    }

    public void printToFile() {
        String out = toString();
        Robot.logger.print(out);
    }

    public String toString() {
        String newline = "\r\n";
        //System.out.println("[SHOOTER] Vout " + getVoltageOut());
        String out = "";
        out += "Vout," + getVoltageOut() + ", ";
        out += "Cout," + getCurrentOut() + ", ";
        out += "RPM," + getEncoderRate() + ", ";
        out += "Vbat," + getVoltageBattery() + ", ";
        out += "Time," + System.currentTimeMillis() + newline;
        return out;
    }

    public double getVelocity() {
        return setPointV;
    }

    public double getVoltageOut() {
        double answer = 0;
        try {
            answer += m_shooterA.getOutputVoltage();
            //answer += m_shooterB.getOutputVoltage();
            //answer /= 2;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return answer;
    }

    public double getVoltageBattery() {
        double answer = 0;
        try {
            answer += m_shooterA.getBusVoltage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return answer;
    }

    public double getCurrentOut() {
        double answer = 0;
        try {
            answer += m_shooterA.getOutputCurrent();
            answer += m_shooterB.getOutputCurrent();
            answer /= 2;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return answer;
    }
}