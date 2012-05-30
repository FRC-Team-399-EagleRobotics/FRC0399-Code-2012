/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.mechanisms;

/**
 *
 * @author robotics
 */
public class FeedingBelt {
    public static class FEED_STATES {
        public static MechanismState ONE_BALL_SHOOT = new MechanismState("ONE_BALL_SHOOT");     //Shoot one ball
        public static MechanismState TWO_BALL_SHOOT = new MechanismState("TWO_BALL_SHOOT");     //Shoot two balls
        public static MechanismState THREE_BALL_SHOOT = new MechanismState("THREE_BALL_SHOOT"); //Shoot three balls
        public static MechanismState INCREMENT = new MechanismState("INCREMENT");               //Adds moves all balls up to make room for another
        public static MechanismState WAIT_FOR_BALL = new MechanismState("WAIT_FOR_BALL");       //Adds moves all balls up to make room for another
    }
    
}
