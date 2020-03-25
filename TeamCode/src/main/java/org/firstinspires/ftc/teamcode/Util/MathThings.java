package org.firstinspires.ftc.teamcode.Util;


public class MathThings {

    public static double[] m_v_mult(double[][] m, double[] v) {
        double[] out = new double[4];
        out[0] = v[0] * m[0][0] + v[1] * m[1][0] + v[2] * m[2][0];
        out[1] = v[0] * m[0][1] + v[1] * m[1][1] + v[2] * m[2][1];
        out[2] = v[0] * m[0][2] + v[1] * m[1][2] + v[2] * m[2][2];
        out[3] = v[0] * m[0][3] + v[1] * m[1][3] + v[2] * m[2][3];
        return out;
    }

    /**
     * Cubes an input
     * @param input The value
     * @param factor The factor (-1<x<1)
     * @return The altered input
     */
    public static double cubeInput(double input, double factor){
        double t = factor * Math.pow(input, 3);
        double r = input * (1-factor);
        return t+r;
    }
    
    /**
    * Normalize to -180 to 180
    */
    public static double normalizeDeg(double angle){
        while (angle >= 180.0) angle -= 360.0;
        while (angle < -180.0) angle += 360.0;
        return angle;  
    }
    
    /**
    * Normalize to -2pi to 2pi
    */
    public static double normalizeRad(double angle){
        while (angle >= 1.0) angle -= 2.0;
        while (angle < -1.0) angle += 2.0;
        return angle;
    }

    /**
     * Focal length = (perceived (px) width * distance (in)) / width (in)
     * @param focalLength focalLength
     * @param knownWidthOfObject The known width of the object in inches
     * @param seenPixelWidth The observed pixel width at the unknown distance
     * @return The distance in inches
     */
    public static double distanceToObject(double knownWidthOfObject, double focalLength, double seenPixelWidth){
        return (knownWidthOfObject * focalLength) / seenPixelWidth;
    }

    public static double focalLength(double seenPixelWidth, double knownDistance, double knownWidth){
        return (seenPixelWidth * knownDistance) / knownWidth;
    }


}
