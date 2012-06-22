/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Systems.Imaging;

/**
 * A class representing a color in RGB, HSL, or HSI
 * @author robotics
 */
public class Color {

    private double red, green, blue;

    /**
     * Constructor
     * @param R 
     * @param G
     * @param B 
     */
    public Color(double R, double G, double B) {
        red = R;
        green = G;
        blue = B;
    }

    public double getRed() {
        return red;
    }

    public double getGreen() {
        return green;
    }

    public double getBlue() {
        return blue;
    }

    public void setRed(double red) {
        this.red = red;
    }

    public void setGreen(double green) {
        this.green = green;
    }

    public void setBlue(double blue) {
        this.blue = blue;
    }

    public void setRGB(double red, double green, double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public void set(Color c) {
        this.red = c.red;
        this.green = c.green;
        this.blue = c.blue;
    }

    /**
     * Sets the color in the HSL colorspace
     * @param hue
     * @param sat
     * @param lum 
     */
    public void setHSL(double hue, double sat, double lum) {
        hue = fMod(hue, 360.0);

        double Hprime = hue / 60.0;
        double C = (1 - Math.abs(2 * lum - 1)) * sat;
        double X = C * (1 - Math.abs(fMod(Hprime, 2) - 1));

        switch ((int) Hprime) {
            case 0:
                red = C;
                green = X;
                blue = 0;
                break;
            case 1:
                red = X;
                green = C;
                blue = 0;
                break;
            case 2:
                red = 0;
                green = C;
                blue = X;
                break;
            case 3:
                red = 0;
                green = X;
                blue = C;
                break;
            case 4:
                red = X;
                green = 0;
                blue = C;
                break;
            case 5:
                red = C;
                green = 0;
                blue = X;
                break;
            default:
                red = 0;
                green = 0;
                blue = 0;
        }

        double m = lum - 0.5 * C;
        red += m;
        green += m;
        blue += m;
    }

    /**
     * Sets a color in the HSV colorspace
     * @param hue
     * @param sat
     * @param val 
     */
    public void setHSV(double hue, double sat, double val) {
        hue = fMod(hue, 360.0);

        double Hprime = hue / 60.0;
        double C = sat * (1 - Math.abs(fMod(Hprime, 2) - 1));
        double X = C * val;

        switch ((int) Hprime) {
            case 0:
                red = C;
                green = X;
                blue = 0;
                break;
            case 1:
                red = X;
                green = C;
                blue = 0;
                break;
            case 2:
                red = 0;
                green = C;
                blue = X;
                break;
            case 3:
                red = 0;
                green = X;
                blue = C;
                break;
            case 4:
                red = X;
                green = 0;
                blue = C;
                break;
            case 5:
                red = C;
                green = 0;
                blue = X;
                break;
            default:
                red = 0;
                green = 0;
                blue = 0;
        }
    }

    /**
     * Floating point modulus 
     * @param input
     * @param mod
     * @return 
     */
    private static double fMod(double input, double mod) {
        double output = input / mod;
        output -= (int) output;
        output *= mod;

        if (output < 0) {
            output += mod;
        }
        return output;
    }

    public static Color fromRGB(double R, double G, double B) {
        return new Color(R, G, B);
    }

    public static Color fromHSV(double H, double S, double V) {
        double red, green, blue;

        H = fMod(H, 360.0);

        double Hprime = H / 60.0;
        double C = V * S;
        double X = C * (1 - Math.abs(fMod(Hprime, 2) - 1));

        switch ((int) Hprime) {
            case 0:
                red = C;
                green = X;
                blue = 0;
                break;
            case 1:
                red = X;
                green = C;
                blue = 0;
                break;
            case 2:
                red = 0;
                green = C;
                blue = X;
                break;
            case 3:
                red = 0;
                green = X;
                blue = C;
                break;
            case 4:
                red = X;
                green = 0;
                blue = C;
                break;
            case 5:
                red = C;
                green = 0;
                blue = X;
                break;
            default:
                red = 0;
                green = 0;
                blue = 0;
        }

        return new Color(red, green, blue);
    }

    public static Color fromHSL(double H, double S, double L) {
        double red, green, blue;

        H = fMod(H, 360.0);

        double Hprime = H / 60.0;
        double C = (1 - Math.abs(2 * L - 1)) * S;
        double X = C * (1 - Math.abs(fMod(Hprime, 2) - 1));

        switch ((int) Hprime) {
            case 0:
                red = C;
                green = X;
                blue = 0;
                break;
            case 1:
                red = X;
                green = C;
                blue = 0;
                break;
            case 2:
                red = 0;
                green = C;
                blue = X;
                break;
            case 3:
                red = 0;
                green = X;
                blue = C;
                break;
            case 4:
                red = X;
                green = 0;
                blue = C;
                break;
            case 5:
                red = C;
                green = 0;
                blue = X;
                break;
            default:
                red = 0;
                green = 0;
                blue = 0;
        }

        double m = L - 0.5 * C;
        red += m;
        green += m;
        blue += m;

        return new Color(red, green, blue);
    }
}