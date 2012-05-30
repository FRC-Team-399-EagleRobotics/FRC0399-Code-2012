/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.mechanisms;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import org.team399.y2012.config.RobotIOMap;
import org.team399.y2012.robot.Robot;
import org.team399.y2012.robot.sensors.Imaging.ImageTracker.Target;

/**
 * Turret class. 
 * @author Jeremy
 */
public class Turret extends MechanismBase {

    /**
     * Class containing turret states
     */
    public static class TURRET_STATES {

        public static MechanismState TRACKING = new MechanismState("TRACKING");     //Tracking a target
        public static MechanismState PANNING = new MechanismState("PANNING");       //Panning mode
        public static MechanismState DRIVING = new MechanismState("DRIVING");       //Driving mode, lock turret forward
        public static MechanismState MANUAL = new MechanismState("MANUAL");         //Manual turret control mode
        public static MechanismState AUTO = new MechanismState("AUTO");
        public static MechanismState OFF = new MechanismState("OFF");
    }
    private AxisCamera camera;
    private MechanismState currState = TURRET_STATES.PANNING, prevState;

    private interface HEIGHTS {

        double TURRET = 59.5;
    }
    private CANJaguar m_turret;

    /**
     * Constructor.
     * @param CAN_ID Motor's CAN ID
     */
    public Turret() {
        //setState(TURRET_STATES.PANNING);
        try {
            m_turret = new CANJaguar(RobotIOMap.TURRET_ID);
            m_turret.changeControlMode(CANJaguar.ControlMode.kPosition);
            m_turret.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
            m_turret.configPotentiometerTurns(10);
            m_turret.configSoftPositionLimits(1.0, 8.5);                                            //TURRET LIMITS
            //m_turret.configMaxOutputVoltage(5);
            //m_turret.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            m_turret.setPID(-50, -.1, 35);                                                          //TURRET PID CONSTANTS
            m_turret.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            m_turret.enableControl();
        } catch (Exception e) {
            System.err.println("[TURRET]Error initializing");
            e.printStackTrace();
        }
    }

    /**
     * Method for setting the angle of the turret.
     * This is angle relative to the far (looking at the front) right limit
     */
    public void setAngle(double angle) {
        try {
            System.out.println("in manual");
            //m_turret.changeControlMode(CANJaguar.ControlMode.kPosition);
            //m_turret.setX(angle*.25);
            m_turret.setX(angle);// - 3);
            //System.out.println("TURRET: " + m_turret.getPosition());//(m_turret.getForwardLimitOK() && m_turret.getReverseLimitOK()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private int run_ctr = 0;

    /**
     * Run method. provides turret state changing logic and functionality
     */
    public void run() {
        /*if (run_ctr == 0) {     //Initialization code
        try {
        m_turret.setX(5.0);
        
        } catch (Exception e) {
        e.printStackTrace();
        }
        }*/
        run_ctr++;
//
//        //setState(TURRET_STATES.PANNING);
//        if (currState.equals(TURRET_STATES.PANNING)) {
//            //System.out.println("Panning!");
//            panning();
//        } else if (currState.equals(TURRET_STATES.TRACKING)) {
////            System.out.println("Tracking!");
            tracking();
//        } else if (currState.equals(TURRET_STATES.MANUAL)) {
//            //manual();
//        } else if (currState.equals(TURRET_STATES.OFF)) {
//            disable();
//        }

    }

    
    /**
     * Manually set turret state
     * @param state State from the TURRET_STATES class
     */
    public void setState(MechanismState state) {
        prevState = currState;
        currState = state;
    }
    private double prevX = 0;

    /**
     * Panning mode. Provides target searching and acquisition logic
     */
    private void panning() {


        //Move us into the tracking mode
        if (Robot.eye.getTargets() != null) {
            setState(TURRET_STATES.TRACKING);
        } else {
            //make it pan here if you want
            setVBus(0);
        }
    }

    /**
     * Set the turret motor to a specific percent voltage
     * @param val 
     */
    public void setV(double val) {
        try {
            m_turret.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            m_turret.setX(val);

        } catch (Exception e) {
        }
    }

    /**
     * Tracking. Provides target tracking functionality
     */
    
    public void tracking() {        
        try {
       
        double turreterror = 0;
        double trackingP = .004358;
        double currentTurretPosition = 0;
        
        // do we see targets
            if (Robot.eye.getTargets() != null) 
            {
            //grab the high target location in the camera
            Target focus = Robot.eye.getTarget("HIGH");///Robot.eye.getTargets()[0];
            
            turreterror = 160 - focus.x;//160 is video frame width
            
            //current turret postion
            currentTurretPosition = m_turret.getPosition();
            
            //if the targets are on the left of the screen move to the right
            if(turreterror>= 80 && turreterror <159)
                {m_turret.setX(currentTurretPosition+.15);}
            if(turreterror>= 45 && turreterror <80)
                {m_turret.setX(currentTurretPosition+.075);}
            if(turreterror>= 20 && turreterror <45)
                {m_turret.setX(currentTurretPosition+.015);}
            if(turreterror>= 3 && turreterror <20)
                {m_turret.setX(currentTurretPosition+.008);}
          
            //if the targets are on the right of the screen
            if(turreterror<= -3 && turreterror >-20)
                {m_turret.setX(currentTurretPosition-.008);}
            if(turreterror<= -20 && turreterror >-45)
                {m_turret.setX(currentTurretPosition-.015);}
            if(turreterror<= -45 && turreterror >-80)
                {m_turret.setX(currentTurretPosition-.075);}
            if(turreterror<= -80 && turreterror >-159)
                {m_turret.setX(currentTurretPosition-.15);}
                        
            System.out.println("[TURRET] Tracking TGT Location: " + focus.x);
            System.out.println("[TURRET] Tracking. Center offset: " + Math.abs(160 - focus.x));
            System.out.println("[TURRET] Current Location" + m_turret.getPosition());
            System.out.println("[TURRET] Commanded Location" + m_turret.getX());
            
            } 
            else 
            {
                setState(TURRET_STATES.PANNING);
            }

        }
        catch (Exception e) {}
         
    }

    /**
     * Manual mode.
     */
    private void manual() {
        try {
            //m_turret.setX(5.0);
            //setState(TURET_STATES.PANNING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Disable mode
     */
    public void disable() {
        run_ctr = 0;
        setV(0);
        try {
            m_turret.disableControl();
        } catch(Exception e) {
            
        }
    }
    
    public void enable() {
//        try {
//            m_turret.enableControl();
//        } catch(Exception e) {
//            
//        }
    }

    /**
     * Get the position from the turret jag
     * @return 
     */
    public double getPosition() {
        double answer = 0;
        try {
            answer = m_turret.getPosition();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return answer;
    }

    /**
     * Convert turret pot value to degrees
     * TODO: Fix for new turret
     * @return 
     */
    public double getAngle() {
        double scalingConstant = 58.33333;
        double offset = -116.6666;
        return scalingConstant * getPosition() + offset;
    }

    /**
     * 
     * @param angle 
     */
    public void setAngleDeg(double angle) {
        double scalingConstant = 116.6666;
        double offset = -58.3333;
        setAngle((angle - offset) / scalingConstant);
    }

    
    public void setVBus(double val) {
        try {
            // m_turret.changeControlMode(CANJaguar.ControlMode.kPosition);
            m_turret.setX(getPosition() + val);

        } catch (Exception e) {
        }
    }
}