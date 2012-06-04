/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Systems.Imaging;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import java.util.Vector;

/**
 * ImageProcessor class. Provides methods to process images
 * @author Jeremy G., Justin S.
 */
public class ImageProcessor {

    public static final double areaThresh = 444;
    public static final double rectThresh = 70;
    public static final double aspectThresh = 0;
    public static final int cameraHeight = 240;
    public static final int cameraWidth = 320;
    public static final double cameraOnTurretHeight = 4.958;

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
            masked = image.thresholdHSL(thresh.HueLow, thresh.HueHigh,
                    thresh.SatLow, thresh.SatHigh,
                    thresh.LumLow, thresh.LumHigh);
            hulled = masked.convexHull(true);
            filtered = hulled.removeSmallObjects(true, 2);
            all = filtered.getOrderedParticleAnalysisReports(6);  //Get sorted particle report. sorted in order of size
            image.free();
            filtered.free();
            hulled.free();
            masked.free();
        } catch (NIVisionException e) {
            return null;
        }

        for (int i = 0; i < all.length; i++) {               //Store the targets that pass the size limit
            if (all[i].particleArea > areaThresh) {
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
                }
            } else {
            }

        }
        rects.trimToSize();
        Target[] targets = new Target[rects.size()];
        rects.copyInto(targets);
        if (targets.length == 0) {
            return null;
        }
        return targets;
    }

    /**
     * Target class. provides properties to store data on a target
     */
    public static class Target {

        public int x;
        public int y;
        public int width;
        public int height;
        public double distance;
        public double area;

        /**
         * Creates a target based on a particle analysis report
         * @param par 
         */
        public Target(ParticleAnalysisReport par) {
            x = par.center_mass_x;
            y = par.center_mass_y;
            width = par.boundingRectWidth;
            height = par.boundingRectHeight;
            area = par.particleArea;
            distance = calculateDistance();
        }

        /**
         * Creates a string representation of the target
         * @return a string with various bits of target info
         */
        public String toString() {
            int feet = (int) distance;
            int inches = (int) ((distance - feet) * 12);
            return " X: " + x + " Y: " + y + " H: " + height + " W: "
                    + width + " Distance: " + feet + " " + inches + " Time: " + System.currentTimeMillis();//" Area: " + area;
        }

        /**
         * returns the target's offset above the center
         * @return 
         */
        public int getPixelsAboveCenter() {
            return x - (120);
        }
        private final double cameraHalfTanAngle = Math.tan(48.0 * Math.PI / 360.0);

        /**
         * Attempts to calculate distance from the target
         * @return 
         */
        public double calculateDistance() {
            return cameraWidth / (width * cameraHalfTanAngle);
        }
    }
}