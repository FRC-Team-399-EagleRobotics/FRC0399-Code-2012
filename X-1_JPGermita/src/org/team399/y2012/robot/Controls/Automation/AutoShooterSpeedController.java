/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Controls.Automation;

import com.sun.squawk.util.MathUtils;
import org.team399.y2012.robot.Systems.Shooter;

/**
 *
 * @author Jeremy
 */
public class AutoShooterSpeedController {
    private Shooter m_shooter;

    /**
     * Constructor
     */
    public AutoShooterSpeedController() {
    }
    
    public double distanceToRPM(double distance) {
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
        
        double answer = MathUtils.pow((distance - midRange)*scale, 1/3)+midRPM;
        answer = (answer > maxRPM) ? maxRPM : answer;
        answer = (answer < 0) ? 0 : answer;
        
        return answer;
    }
    
}
