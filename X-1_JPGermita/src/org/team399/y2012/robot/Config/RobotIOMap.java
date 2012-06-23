/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Config;

/**
 * Contains a list of robot IO input and output ports adn their associated devices
 * @author Jeremy Germita
 */
public class RobotIOMap {
    //CAN Jaguar IDs

    /**
     * Left drive motor A CAN ID
     */
    public static final int LEFT_DRIVE_A_ID = 2;
    /**
     * Left drive motor B CAN ID
     */
    public static final int LEFT_DRIVE_B_ID = 3;
    /**
     * Right drive motor A CAN ID
     */
    public static final int RIGHT_DRIVE_A_ID = 4;
    /**
     * Right drive motor B CAN ID
     */
    public static final int RIGHT_DRIVE_B_ID = 5;
    /**
     * Shooter motor A CAN ID
     */
    public static final int SHOOTER_A_ID = 6;
    /**
     * Shooter motor B ID
     */
    public static final int SHOOTER_B_ID = 7;
    /**
     * Turret motor ID
     */
    public static final int TURRET_ID = 8;
    //CAN Sync Groups
    public static final byte SHOOTER_SYNC_ID = 1;
    public static final byte DRIVETRAIN_SYNC_ID = 2;
    //Analog Sensors:
    /**
     * Gyro - Yaw axis analog port
     */
    public static final int GYRO_YAW = 1;
    /**
     * Gyro - Pitch axis analog port
     */
    public static final int GYRO_PITCH = 2;
    /**
     * Conveyor belt ball sensor port
     */
    public static final int BALL_SENSOR = 3;
    //Digital outputs/Inputs
    /**
     * Light ring - Red channel DIO port
     */
    public static final int LIGHT_RING_R = 1;
    /**
     * Light ring - green channel DIO port
     */
    public static final int LIGHT_RING_G = 2;
    /**
     * Light ring - blue channel DIO port
     */
    public static final int LIGHT_RING_B = 3;
    /**
     * Pressure switch digital output
     */
    public static final int PRESSURE_SWITCH = 4;
    //PWM outputs
    public static final int CAM_TILT_SERVO_PWM = 3;
    /**
     * Intake conveyor belt PWM port
     */
    public static final int INTAKE_CONVEYOR_PWM = 2;
    //Relay outputs
    /**
     * Compressor relay port
     */
    public static final int COMPRESSOR_RELAY = 1;
    //Solenoid outputs
    /**
     * Dropper solenoid port
     */
    public static final int DROPPER_PORTA = 4;
    public static final int DROPPER_PORTB = 7;
    /**
     * hood solenoid port
     */
    public static final int HOOD_PORT = 1;
    /**
     * Shifter solenoid port
     */
    public static final int SHIFTER_PORT = 2;
}