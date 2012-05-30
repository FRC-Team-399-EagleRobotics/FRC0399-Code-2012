/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.controllers;

import edu.wpi.first.wpilibj.Joystick;
import org.team399.y2012.HumanInterfaceDevices.DriverStationUserInterface;
import org.team399.y2012.robot.Robot;
import org.team399.y2012.robot.mechanisms.Dropper;
import org.team399.y2012.robot.mechanisms.IntakeBelt;
import org.team399.y2012.robot.mechanisms.Shooter;
import org.team399.y2012.robot.mechanisms.Turret;
import org.team399.y2012.util.EagleMath;

/**
 *
 * @author Robotics
 */
public class TeleopController {

    private String driver = null, operator = null;
    public Joystick leftJoy = new Joystick(1);      //Added left and right joysticks 3/7/12
    public Joystick rightJoy = new Joystick(2);
    public Joystick driver_g, operator_g;
    
    public DriverStationUserInterface funbox;

    public TeleopController(String driver, String operator) {
        System.out.println("[TELE-Controller] Driver: " + driver);
        System.out.println("[TELE-Controller] Operator: " + operator);
        this.driver = driver;
        this.operator = operator;
        initDriver(driver);
        initOperator(operator);

        funbox = new DriverStationUserInterface();
    }

    public void setControllers(Joystick driver_g, Joystick operator_g) {
        this.driver_g = driver_g;
        this.operator_g = operator_g;
    }

    private void initDriver(String who) {
        if (driver.equals("Jeremy")) {
        } else if (driver.equals("Gabe")) {
        } else {
            System.out.println("Driver not recognized");
        }
        int delaycounter = 0;
    }

    private void initOperator(String who) {
        if (operator.equals("Jackie")) {
        } else {
            System.out.println("Operator not recognized");
        }
    }

    public void run() {
        drive(true);
        operate(true);
    }

    public void stop() {
        drive(false);
        operate(false);
    }

    private void drive(boolean enable) {
       if (driver.equals("Jeremy")) {
            Robot.drive.shift(driver_g.getRawButton(6));
            //Robot.drive.tankDrive(driver_g.getRawAxis(2), -driver_g.getRawAxis(4));
            Robot.drive.cheesyDrive(driver_g.getRawAxis(2), driver_g.getRawAxis(4), driver_g.getRawButton(8));
            Robot.dropper.setState((driver_g.getRawButton(5) ? Dropper.DROPPER_STATES.OUT : Dropper.DROPPER_STATES.IN));
           /* if (getDPad(DPadStates.UP)) {                                     //Commented out everything that uses DPAD because they are giving stupid errors and we dont even use them
                Robot.intake.setStates(IntakeBelt.INTAKE_STATES.COLLECT);
            }
            if (getDPad(DPadStates.DOWN)) {
                Robot.intake.setStates(IntakeBelt.INTAKE_STATES.RELEASE);
            }*/
          }
         
         if (driver.equals("Gabe")) {
           /* 
            Robot.drive.shift(driver_g.getRawButton(6));
            Robot.drive.tankDrive(driver_g.getRawAxis(2), -driver_g.getRawAxis(4));
            
            Robot.dropper.drop(driver_g.getRawButton(5));
           */ 
             
           //  NEW JOYSTICK Controls. NEED TO TEST. Written by the awesome Jackie and not Gabe
              
    Robot.drive.shift(rightJoy.getRawButton(1));              //Shift gears with Right TRIGGER    
    Robot.dropper.drop(leftJoy.getRawButton(1));               // Drop DROPPER with left TRIGGER
               
            if(leftJoy.getRawButton(5))                             //AUTOBALANCE NEED TO TEST
            {
                Robot.drive.autoBalance();
    
            }
            else{Robot.drive.tankDrive(leftJoy.getRawAxis(2), -rightJoy.getRawAxis(2));}     //Tank Drive
            
            
            if(leftJoy.getRawButton(3))                                     //Button 3 picks up balls on CONVEYOR
            {   
                Robot.intake.setStates(IntakeBelt.INTAKE_STATES.COLLECT);
            }
            else if(leftJoy.getRawButton(2))
            {
                Robot.intake.setStates(IntakeBelt.INTAKE_STATES.RELEASE);   //Button 2 releases balls on CONVEYOR
            }
            
            /*
            if (driver_g.getRawButton(1)) {
                Robot.drive.autoBalance();
            }

            if (getDPad(DPadStates.UP)) {
                Robot.intake.setStates(IntakeBelt.INTAKE_STATES.COLLECT);
            } 
            else if (getDPad(DPadStates.DOWN)) {
                Robot.intake.setStates(IntakeBelt.INTAKE_STATES.RELEASE);
            }*/
        }
    }

    private void operate(boolean enable) {
        if (operator.equals("Test")) {
            if (operator_g.getRawButton(1)) {
                Robot.logger.setEnabled(true);
                Robot.shooter.setRollersV(-1.00);
                //Robot.shooter
            } else if (operator_g.getRawButton(2)) {
                Robot.logger.setEnabled(true);
                Robot.shooter.setRollersV(-.90);
            } else if (operator_g.getRawButton(3)) {
                Robot.logger.setEnabled(true);
                Robot.shooter.setRollersV(-.8);
            } else if (operator_g.getRawButton(4)) {
                Robot.logger.setEnabled(true);
                Robot.shooter.setRollersV(-.55);
            } else if (operator_g.getRawButton(10)) {
                Robot.logger.setEnabled(true);
                Robot.shooter.setRollersV(0);
            }

            /*if(operator_g.getRawButton(5)) {
            Robot.shooter.setAngle(40);
            }  else {
            Robot.shooter.setAngle(80);
            }*/

            //Robot.shooter.print();//
            //Robot.shooter.printToFile();


            if (operator_g.getRawButton(6)) {
                Robot.intake.set(-1.0);
            } else if (operator_g.getRawButton(8)) {
                Robot.intake.set(1.0);
            } else {
                Robot.intake.set(0);
            }

            //Robot.turret.setAngle(1.4);

            //Robot.turret.setAngle(((operator_g.getRawAxis(1)+1)/2)*(.970-.0));
            //Robot.turret.setAngle(3);
            //Robot.turret.run();
            //Robot.turret.setHood(operator_g.getRawAxis(4));
            Robot.turret.setV(((-operator_g.getRawAxis(1))));

            Robot.shooter.setHood(-operator_g.getRawAxis(4));
            //
            //Robot.shooter.setVelocity(3800);
        } else if (operator.equals("Jackie")) {
            Robot.eye.run();
 
            // NEW Operator Controls. NEED TO TEST. Written by Jackie
            if(funbox.getDigital(DriverStationUserInterface.PORTS.INTAKE_BELT_BUTTON))       //Added new Conveyor controls, got rid of switch 3/7/12
            {
                 Robot.intake.setStates(IntakeBelt.INTAKE_STATES.COLLECT);
            }
            else if(funbox.getDigital(DriverStationUserInterface.PORTS.RELEASE_BELT_BUTTON))
            {
                 Robot.intake.setStates(IntakeBelt.INTAKE_STATES.RELEASE);
            }
            else{Robot.intake.setStates(IntakeBelt.INTAKE_STATES.OFF);
            }
            
            Robot.intake.setMult(EagleMath.map((float) funbox.getAnalog(DriverStationUserInterface.PORTS.BELT_KNOB), (float) 1.3, (float) 5.0, (float) 0.0, (float) 1.0)); 
          
            if (funbox.getTurretSwitch().equals("AUTO")) {
                //Robot.turret.setState(Turret.TURRET_STATES.AUTO);
                Robot.turret.tracking();
                Robot.turret.setState(Turret.TURRET_STATES.TRACKING);
            } else if (funbox.getTurretSwitch().equals("MANUAL")) {
                Robot.turret.setState(Turret.TURRET_STATES.MANUAL);
                Robot.turret.setAngle(EagleMath.map((float) funbox.getAnalog(DriverStationUserInterface.PORTS.TURRET_KNOB), (float) 1.7, (float) 5.0, (float) 8.5, (float) 1.0));//TODO: scale to range
                //System.out.println("turret driver input" + funbox.getAnalog(DriverStationUserInterface.PORTS.TURRET_KNOB));
                //System.out.println("turret mappint output" + EagleMath.map((float) funbox.getAnalog(DriverStationUserInterface.PORTS.TURRET_KNOB), (float) 0, (float) 5.0, (float) 8.5, (float) 1.0));
            } else {
                Robot.turret.setState(Turret.TURRET_STATES.OFF);
            }
         
            if (funbox.getDigital(DriverStationUserInterface.PORTS.SHOOT_BUTTON)) {
                Robot.shooter.setState(Shooter.SHOOTER_STATES.SHOOT);
                if (funbox.getShooterSwitch().equals("AUTO")) {
                    Robot.shooter.setShooterManual(false);
                    Robot.shooter.run();
                } else if (funbox.getShooterSwitch().equals("MANUAL")) {
                    Robot.shooter.setShooterManual(true);
                    System.out.println("Manual shooter");
                    Robot.shooter.setVelocity(EagleMath.map((float) funbox.getAnalog(DriverStationUserInterface.PORTS.SHOOTER_KNOB), (float) 0, (float) 5.0, (float) 0.0, (float) 5000.0));
                    //Robot.shooter.setRollersV(-EagleMath.map((float) funbox.getAnalog(DriverStationUserInterface.PORTS.SHOOTER_KNOB), (float) 0, (float) 5.0, (float) 0.0, (float) 1.0));
                    Robot.shooter.run();
                } 
            } else {
                Robot.shooter.setState(Shooter.SHOOTER_STATES.OFF);
            }

            if (funbox.getHoodSwitch().equals("AUTO")) {
                Robot.shooter.setHoodManual(false);
                //Todo: add auto hood stuff
            } else if (funbox.getHoodSwitch().equals("MANUAL")) {
                Robot.shooter.setHoodManual(true);
                float potVal = (float) funbox.getAnalog(DriverStationUserInterface.PORTS.HOOD_KNOB);
                potVal = EagleMath.map(potVal, (float) 0, (float) 5.0, (float) -1.0, (float) 1.0);
                potVal = (Math.abs(potVal) < .25) ? 0 : potVal;
                
                //COMMENTED HOOD STUFF SWITCH IT
                //Robot.shooter.setHood(EagleMath.map((float) funbox.getAnalog(DriverStationUserInterface.PORTS.HOOD_KNOB), (float) 0, (float) 5.0, (float) .78, (float) .95));
                Robot.shooter.setHood(potVal * .9);  //TODO: Scale to range of hood
                
                //System.out.println("Hood Driver Input =" + EagleMath.map((float) funbox.getAnalog(DriverStationUserInterface.PORTS.HOOD_KNOB), (float) 1.0, (float) 5.0, (float) .95, (float) .78));
                //System.out.println("Hood Jag SetX" + Robot.shooter.getHoodJagX());
                //System.out.println("Hood Jag Volts Out:"+ Robot.shooter.getHoodVoltage());
                //System.out.println("Hood Jag Pot Value:"+ Robot.shooter.getHoodPot());
                //Robot.shooter.getHoodControlStyle();
                //System.out.println("HOOD:" + Robot.shooter.getHood());
                
                //System.out.println("potinput:" + potVal);
            } else {
                //Robot.shooter.setState(Shooter.SHOOTER_STATES.OFF);
            }

            //Robot.intake.run();
        }
    }
}

    /**
     * Get the state of the specified D pad direction
     * @param direction
     * @return
     */
   /* private boolean getDPad(int direction) {
        switch (direction) {
            case DPadStates.UP:
                return (operator_g.getRawAxis(6) < -.7);
            case DPadStates.DOWN:
                return (operator_g.getRawAxis(6) > .7);
            case DPadStates.LEFT:
                return (operator_g.getRawAxis(5) > .7);
            case DPadStates.RIGHT:
                return (operator_g.getRawAxis(5) < -.7);
            default:
                return false;

        }
    }

    /**
     * Directional pad states.
     
    private static interface DPadStates {

        final int UP = 0;
        final int DOWN = 1;
        final int LEFT = 2;
        final int RIGHT = 3;
  }}*/

