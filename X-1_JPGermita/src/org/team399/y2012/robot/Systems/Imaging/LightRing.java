/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Systems.Imaging;

import edu.wpi.first.wpilibj.DigitalOutput;

/**
 * Light ring class encapsulating 3 PWM inputs
 * @author Jeremy, Jackie, Justin
 */
public class LightRing {

    private DigitalOutput m_red = null;
    private DigitalOutput m_green = null;
    private DigitalOutput m_blue = null;
    private Color current = new Color(0, 0, 0);

    /**
     * Constructor
     * @param Red - red channel
     * @param Green - green channel
     * @param Blue  - blue channel
     */
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

    /**
     * updates the light ring
     */
    private void update() {
        m_red.updateDutyCycle(current.getRed());
        m_green.updateDutyCycle(current.getGreen());
        m_blue.updateDutyCycle(current.getBlue());
    }

    /**
     * Set the light ring a color in the HSV colorspace
     * @param hue
     * @param saturation
     * @param value 
     */
    public void setHSV(double hue, double saturation, double value) {
        current.setHSV(hue, saturation, value);
        update();
    }

    /**
     * Set the light ring a color in the HSL colorspace
     * @param hue
     * @param saturation
     * @param luminance 
     */
    public void setHSL(double hue, double saturation, double luminance) {
        current.setHSL(hue, saturation, luminance);
        update();
    }

    /**
     * Set the light ring a color in the RGB colorspace
     * @param red
     * @param green
     * @param blue 
     */
    public void setRGB(double red, double green, double blue) {
        current.setRGB(red, green, blue);
        update();
    }

    public void set(Color c) {
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

    public Color getColor() {
        return current;
    }
}
