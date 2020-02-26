package org.firstinspires.ftc.teamcode.PPDev;

import com.qualcomm.robotcore.util.Range;

import org.opencv.core.Point;
import java.util.ArrayList;

import static org.firstinspires.ftc.teamcode.PPDev.PPMathFunctions.lineCircleIntersection;
import static org.firstinspires.ftc.teamcode.PPDev.PPMathFunctions.AngleWrap;

public class PPMovement {

    //Make these actuall things
    public static double world_x_position = -40;
    public static double world_y_position = 40;
    public static double world_angle_rad = 0.5;

    public static double movement_x = 0;
    public static double movement_y = 0;
    public static double movement_turn = 0;

    //Optimal direction is forward (even though it's holonomic)

    public static void followCurve(ArrayList<CurvePoint> allPoints, double followingAngle){
        for(int i = 0; i < allPoints.size()-1; i++){

        }

        CurvePoint followMe = getFollowPointPath(allPoints, new Point(world_x_position, world_y_position), allPoints.get(0).followDistance);

        //I SKIPPED DEBUGGING FROM THE TUTORIAL!!

        goToPosition(followMe.x, followMe.y, followMe.moveSpeed, followingAngle, followMe.turnSpeed);
    }
    
    
    public static CurvePoint getFollowPointPath(ArrayList<CurvePoint> pathPoints, Point robotLocation, double followRadius){
        CurvePoint followMe = new CurvePoint(pathPoints.get(0));
        
        for(int i = 0; i < pathPoints.size();i++){
            CurvePoint startLine = pathPoints.get(i);
            CurvePoint endLine = pathPoints.get(i + 1);


            ArrayList<Point> intersections = lineCircleIntersection(robotLocation, followRadius, startLine.toPoint(),
                    endLine.toPoint());

            double closestAngle = 10000000;

            for(Point thisIntersection : intersections){
                double angle = Math.atan2(thisIntersection.y-world_y_position, thisIntersection.x-world_x_position);
                double deltaAngle = Math.abs(PPMathFunctions.AngleWrap(angle - world_angle_rad));

                if(deltaAngle < closestAngle){
                    closestAngle = deltaAngle;
                    followMe.setPoint(thisIntersection);
                }
            }
        }
        return followMe;
    }


    //Turn towards optimal angle
    /*
    Calculate the targetX - robotX and targetY - robotY

    Calculate the relative angle (difference to target angle)
     */

    public static void goToPosition(double x, double y, double movementSpeed, double prefferedAngle, double turnSpeed){

        double distanceToTarget = Math.hypot(x-world_x_position, y-world_y_position);

        double absoluteAngleToTarget = Math.atan2(y - world_y_position, x - world_x_position);

        double relativeAngleToPoint = AngleWrap(absoluteAngleToTarget - (world_angle_rad - Math.toRadians(90)));

        double relativeXToPoint = Math.cos(relativeAngleToPoint) * distanceToTarget;
        double relativeYToPoint = Math.sin(relativeAngleToPoint) * distanceToTarget;

        double movementXPower = relativeXToPoint / (Math.abs(relativeXToPoint) + Math.abs(relativeYToPoint));
        double movementYPower = relativeYToPoint / (Math.abs(relativeXToPoint) + Math.abs(relativeYToPoint));

        movement_x = movementXPower * movementSpeed;
        movement_y = movementYPower * movementSpeed;

        double relativeTurnAngle =  relativeAngleToPoint - Math.toRadians(180) + prefferedAngle;

        movement_turn = Range.clip(relativeTurnAngle/Math.toRadians(30), -1, 1) * turnSpeed;

        if(distanceToTarget < 10){
            movement_turn = 0;
        }

    }
}
