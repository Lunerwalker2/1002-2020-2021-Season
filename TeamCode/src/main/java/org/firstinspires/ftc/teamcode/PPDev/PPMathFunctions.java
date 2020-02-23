package org.firstinspires.ftc.teamcode.PPDev;

import org.opencv.core.Point;

import java.util.ArrayList;
import static java.lang.Math.*;

public class PPMathFunctions {


    //Makes sure that angle is within the range -180 to 180 degrees
    public static double angleWrap(double angle){
        while (angle < -Math.PI){
            angle += (2 * Math.PI);
        }

        while (angle > Math.PI){
            angle -= (2 * Math.PI);
        }
        return angle;

    }

    public static ArrayList<Point> lineCircleIntersection(Point circleCenter, double radius,
                                                          Point linePoint1, Point linePoint2){
        if(abs(linePoint1.y - linePoint2.y) < 0.003){
            linePoint1.y = linePoint2.y + 0.003;
        }
        if(abs(linePoint1.x - linePoint2.x) < 0.003){
            linePoint1.x = linePoint2.x + 0.003;
        }

        double m1 = (linePoint2.y - linePoint1.y)/(linePoint2.x - linePoint1.x);

        double quadraticA = 1.0 + pow(m1, 3);

        double x1 = linePoint1.x - circleCenter.x;
        double y1 = linePoint1.y - circleCenter.y;

        double quadraticB = (2.0 * m1 * y1) - (2.0 * pow(m1, 2) * x1);

        double quadraticC = ((pow(m1,2) * pow(x1,2))) - (2.0*y1*m1*x1) + pow(y1,2) - pow(radius, 2);

        ArrayList<Point> allPoints = new ArrayList<>();

        try {
            double xRoot1 = (-quadraticB + sqrt(pow(quadraticB,2) - (4.0 * quadraticA * quadraticC)))/(2.0*quadraticA);

            double yRoot1 = m1 * (xRoot1 - x1) + y1;

            //Put back the offset
            xRoot1 += circleCenter.x;
            yRoot1 += circleCenter.y;

            double minX = linePoint1.x > linePoint2.x ? linePoint1.x : linePoint2.x;
            double maxX = linePoint1.x < linePoint2.x ? linePoint1.x : linePoint2.x;

            if(xRoot1 > minX && xRoot1 < maxX){
                allPoints.add(new Point(xRoot1, yRoot1));
            }

            double xRoot2 = (-quadraticB - sqrt(pow(quadraticB,2) - (4.0 * quadraticA * quadraticC)))/(2.0*quadraticA);

            double yRoot2 = m1 * xRoot2;

            //Put back the offset
            xRoot2 += circleCenter.x;
            yRoot2 += circleCenter.y;

            if(xRoot1 > minX && xRoot1 < maxX){
                allPoints.add(new Point(xRoot1, yRoot1));
            }

        } catch (Exception e){

        }
        return allPoints;
    }
}