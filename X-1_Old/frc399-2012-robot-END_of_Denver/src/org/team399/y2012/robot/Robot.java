package org.team399.y2012.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Preferences;
import org.team399.y2012.config.RobotIOMap;
import org.team399.y2012.robot.mechanisms.DriveTrain;
import org.team399.y2012.robot.mechanisms.Dropper;
import org.team399.y2012.robot.mechanisms.FeedingBelt;
import org.team399.y2012.robot.mechanisms.IntakeBelt;
import org.team399.y2012.robot.mechanisms.MechanismState;
import org.team399.y2012.robot.mechanisms.Shooter;
import org.team399.y2012.robot.mechanisms.Turret;
import org.team399.y2012.robot.sensors.Imaging.EagleEye;
import org.team399.y2012.util.DataLogger;

/**
 * Robot Class. Instantiate and start this to provide robot functionality.
 * @author Jeremy
 */
public class Robot {

    private static final int DRIVETRAIN_STATE = 0;
    private static final int TURRET_STATE = 1;
    private static final int SHOOTER_STATE = 2;
    private static final int INTAKE_STATE = 3;
    private static final int FEED_STATE = 4;
    private static final int DROPPER_STATE = 5;
    private MechanismState[] states;
    //Joystick gamepad = new Joystick(1);
    public static Turret turret;
    public static DriveTrain drive;
    public static IntakeBelt intake;
    public static Dropper dropper;
    public static Shooter shooter;
    //Preferences;
    public static DataLogger logger = DataLogger.getInstance("file:///shooter_log.csv");
    public static EagleEye eye = new EagleEye();
    private Compressor comp = new Compressor(RobotIOMap.PRESSURE_SWITCH,
            RobotIOMap.COMPRESSOR_RELAY);

    public static class ROBOT_STATES {

        public static MechanismState[] TELEOP_INIT = {DriveTrain.DRIVETRAIN_STATES.DRIVING,
            Turret.TURRET_STATES.PANNING,
            Shooter.SHOOTER_STATES.OFF,
            IntakeBelt.INTAKE_STATES.OFF,
            FeedingBelt.FEED_STATES.WAIT_FOR_BALL,
            Dropper.DROPPER_STATES.IN};
    }

    public Robot() {
        //turret = new Turret((short)1);
        //this.driverL = driverL;
        //this.driverR = driverR;
        drive = new DriveTrain();
        turret = new Turret();
        intake = new IntakeBelt();
        dropper = new Dropper();
        shooter = new Shooter();
//        setStates(ROBOT_STATES.TELEOP_INIT);
        comp.start();
    }

    public void setStates(MechanismState[] state) {
        states = state;
    }

    public void doStatesTele() {
        //setStates(ROBOT_STATES.TELEOP_INIT);
    }

    public void doStatesAuton() {
    }
}