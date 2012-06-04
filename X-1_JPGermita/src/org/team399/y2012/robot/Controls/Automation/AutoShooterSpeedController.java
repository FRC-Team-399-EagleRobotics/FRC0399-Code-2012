/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Controls.Automation;

import com.sun.squawk.util.MathUtils;
import org.team399.y2012.robot.Systems.Shooter;

/**
 * A class to provide some functions for automatic shooter speed control
 * @author Jeremy
 */
public class AutoShooterSpeedController {

    private static double[][] setDistRanges_LUT = {
        {0, 0, 0}, //Fender front shot distance range
        {0, 0, 0} //Key Shot distance range
    };

    /**
     * Applies inverse of distance function to get a speed for the shooter to use
     * @param distance distance in inches
     * @return an RPM for the shooter
     */
    public static double distanceToRPM(double distance) {
//        return distance;

        /*double answer = MathUtils.pow(distance-3000, 3);
        answer *= .000000003;
        answer += 60;
        return answer;*/


        double midRange = 60;
        double maxRange = 120;
        double midRPM = 3000;
        double maxRPM = 4100;
        double scale = .000000003;
        double answer = 0;

        if (distance >= setDistRanges_LUT[0][0] && distance <= setDistRanges_LUT[0][1]) {
            //Fender Shot
            answer = setDistRanges_LUT[0][2];
        } else if (distance >= setDistRanges_LUT[1][0] && distance <= setDistRanges_LUT[1][1]) {
            //Key Shot
            answer = setDistRanges_LUT[1][2];
        } else {
            answer = Math.sqrt((distance - midRange) * scale) + midRPM;
            answer = (answer > maxRPM) ? maxRPM : answer;
            answer = (answer < 0) ? 0 : answer;
        }

        return answer;
    }
}
