/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.sensors.Imaging;

import edu.wpi.first.wpilibj.DigitalOutput;

/**
 *
 * @author robotics
 */
public class LightRing {

        private DigitalOutput m_red = null;
        private DigitalOutput m_green = null;
        private DigitalOutput m_blue = null;
        
        private Color current = new Color(0,0,0);

        public LightRing(int Red, int Green, int Blue) {
            m_red = new DigitalOutput(Red);
            m_green = new DigitalOutput(Green);
            m_blue = new DigitalOutput(Blue);

            m_red.setPWMRate(12000);
            m_red.enablePWM(0);

            m_green.setPWMRate(12000);
            m_green.enablePWM(0);

            m_blue.setPWMRate(12000);
            m_blue.enablePWM(0);

        }
        
        private void update()
        {
             m_red.updateDutyCycle(current.getRed());
             m_green.updateDutyCycle(current.getGreen());
             m_blue.updateDutyCycle(current.getBlue());
        }
        
        public void setHSV(double hue, double saturation, double value)
        {
            current.setHSV(hue, saturation, value);
            update();
        }
        
        public void setHSL(double hue, double saturation, double luminance)
        {
            current.setHSL(hue, saturation, luminance);
            update();
        }
        
        public void setRGB(double red, double green, double blue)
        {
            current.setRGB(red, green, blue);
            update();
        }
        
        public void set(Color c)
        {
            current.set(c);
        }

        public void setRed(double power) {
           current.setRed(power);
        }

        public void setGreen(double power) {
            current.setGreen(power);
        }

        public void setBlue(double power) {
            current.setBlue(power);
        }
    }
