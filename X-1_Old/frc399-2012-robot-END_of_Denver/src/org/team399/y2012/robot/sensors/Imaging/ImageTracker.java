/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.sensors.Imaging;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import java.util.Vector;

/**
 *
 * @author JustinLaptop
 */
public class ImageTracker {

    public static final double areaThresh = 444;
    public static final double rectThresh = 70;
    public static final double aspectThresh = 0;
    public static final int cameraHeight = 240;
    public static final int cameraWidth = 320;
    public static final double cameraOnTurretHeight = 4.958;
    //private static Servo camServ = new Servo(3);

    public static double rectangularityScore(double area, double width, double height) {
        // A perfect rectangle will have a score of 100
        double rectangularityScore = area;
        rectangularityScore /= width;
        rectangularityScore /= height;
        rectangularityScore *= 100;
        return rectangularityScore;
    }

    public static double aspectRatioScore(double width, double height) {
        double aspectRatioScore = width / height;
        aspectRatioScore /= 1.33; // normalize
        aspectRatioScore = Math.abs(1.0 - aspectRatioScore); //Teepee at 1
        aspectRatioScore = 100.0 * (1.0 - aspectRatioScore);
        if (aspectRatioScore > 100) {
            aspectRatioScore = 100;
        } else if (aspectRatioScore < 0) {
            aspectRatioScore = 0;
        }
        return aspectRatioScore;
    }

    public static Target[] processImage(ColorImage image, Threshold thresh) {
        //System.out.println("[EAGLE-EYE] Image processor started...");
        if (image == null) {
            //System.out.println("[EAGLE-EYE] Image null, processor ended");
            return null;   //If the image is null, return a null target
        }
        BinaryImage masked;
        BinaryImage hulled;
        BinaryImage filtered;
        Vector rects = new Vector();
        rects.ensureCapacity(4);
        ParticleAnalysisReport[] all;

        try {
            //masked = image.thresholdHSL(80, 130, 80, 255, 120, 255);
            //thresholdHSL(int hueLow, int hueHigh, int saturationLow, int saturationHigh, int luminenceLow, int luminenceHigh)
            masked = image.thresholdHSL(thresh.HueLow, thresh.HueHigh,
                                        thresh.SatLow, thresh.SatHigh,
                                        thresh.LumLow, thresh.LumHigh);
            //System.out.println("[EAGLE-EYE] HSL thresholds applied. Number of particles found: " + masked.getNumberParticles());
            //NIVision399.convexHull(masked.image, masked.image, 1);   //Convex hull(close any particles) on input image
            hulled = masked.convexHull(true);
            //System.out.println("[EAGLE-EYE] Convex hulled. Number of particles found: " + hulled.getNumberParticles());
            filtered = hulled.removeSmallObjects(true, 2);
            //System.out.println("[EAGLE-EYE] Small objects removed. Number of particles found: " + filtered.getNumberParticles());
            all = filtered.getOrderedParticleAnalysisReports(6);  //Get sorted particle report. sorted in order of size
            //System.out.println("[EAGLE-EYE] Image processing complete.");
            //NetworkTable.getTable(null)
            image.free();
            filtered.free();
            hulled.free();
            masked.free();
        } catch (NIVisionException e) {
            return null;
        }

        //System.out.println("[EAGLE-EYE] Starting target tests.");
        
        for (int i = 0; i < all.length; i++) {               //Store the targets that pass the size limit
            //System.out.println("[EAGLE-EYE] Particle: " + i);
            //System.out.println("[EAGLE-EYE] Starting size test.");
            if (all[i].particleArea > areaThresh) {
                //System.out.println("[EAGLE-EYE] Passed size test. Beginning rectangularity and aspect ratio tests");
                double rectangularityScore =
                        rectangularityScore(all[i].particleArea,
                        all[i].boundingRectWidth,
                        all[i].boundingRectHeight);
                double aspectRatioScore =
                        aspectRatioScore(all[i].boundingRectWidth,
                        all[i].boundingRectHeight);

                if (rectangularityScore > rectThresh
                        && aspectRatioScore > aspectThresh) {
                    Target current = new Target(all[i]);
                    rects.addElement(current);          //Target found, return it 
                } else {
              //      System.out.println("[EAGLE-EYE] Failed rectangularity and aspect ratio tests.");
                }
            } else {
                //System.out.println("[EAGLE-EYE] Failed size test.");
            }
            
        }
        rects.trimToSize();
        Target[] targets = new Target[rects.size()];
        rects.copyInto(targets);
        if (targets.length == 0) {
            //System.out.println("[EAGLE-EYE] No targets found");
            return null;
        }
        //System.out.println("[EAGLE-EYE] Targets found. Target processing complete.");
        return targets;
    }

    /**
     * @deprecated 
     * Get a certain target
     * @param index 0 for the lowest target. 1 for the left target. 2 for the right target. 3 for the top target
     * @return a target object if that target was found. null if no target found
     */
    public static Target getTarget(Target[] targets, int index) {
        
        if (targets != null) {
            for (int i = 0; i < targets.length; i++) {
                //System.out.println("Target #" + i + ":");
                //System.out.println(targets[i].toString());
            }
            ImageTracker.Target focus = targets[0];
            if (index == 0) {
                for (int i = 0; i < targets.length; i++) {
                    if (focus.y < targets[i].y) {
                        focus = targets[i];
                    }
                }
            } else if (index == 1) {
                for (int i = 0; i < targets.length; i++) {
                    if (focus.x < targets[i].x) {
                        focus = targets[i];
                    }
                }
            } else if (index == 2) {
                for (int i = 0; i < targets.length; i++) {
                    if (focus.x > targets[i].x) {
                        focus = targets[i];
                    }
                }
            } else if (index == 3) {
                for (int i = 0; i < targets.length; i++) {
                    if (focus.y > targets[i].y) {
                        focus = targets[i];
                    }
                }
            }
            return focus;

        }

        return null;

    }

    public static class Target {

        public int x;
        public int y;
        public int width;
        public int height;
        public double distance;
        public double area;

        public Target(ParticleAnalysisReport par) {
            x = par.center_mass_x;
            y = par.center_mass_y;
            width = par.boundingRectWidth;
            height = par.boundingRectHeight;
            area = par.particleArea;
            distance = calculateDistance();
        }

        public String toString() {
            int feet = (int) distance;
            int inches = (int) ((distance - feet) * 12);
            return " X: " + x + " Y: " + y + " H: " + height + " W: "
                    + width + " Distance: " + feet + " " + inches + " Time: " + System.currentTimeMillis();//" Area: " + area;
        }

        public int getPixelsAboveCenter() {
            return x - (120);
        }

        public double getTargetHeight(double cameraAngle) {
            double scalingA = -1 / 50.0;    //1 foot per 42 pixels
            double yVal = y;            //Y pixel value
            double scalingB = 1.1;
            double range = distance;    //
            double theta = cameraAngle + MathUtils.asin(scalingA * (yVal - cameraHeight / 2.0) / range);// + Math.toRadians(7);
            return range * Math.sin(theta) + cameraOnTurretHeight;
        }
        private final double cameraHalfTanAngle = Math.tan(48.0 * Math.PI / 360.0);

        public double calculateDistance() {
            return cameraWidth / (width * cameraHalfTanAngle);
        }
    }
}