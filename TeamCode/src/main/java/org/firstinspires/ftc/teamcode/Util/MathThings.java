package org.firstinspires.ftc.teamcode.Util;

import com.arcrobotics.ftclib.geometry.Vector2d;

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


}
