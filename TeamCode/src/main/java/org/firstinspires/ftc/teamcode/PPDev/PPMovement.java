package org.firstinspires.ftc.teamcode.PPDev;

import com.qualcomm.robotcore.util.Range;

public class PPMovement {

    //Make these actuall things
    public static double world_x_position = -40;
    public static double world_y_position = 40;
    public static double world_angle_rad = 0.5;

    public static double movement_x = 0;
    public static double movement_y = 0;
    public static double movement_turn = 0;

    //Optimal direction is forward (even though it's holonomic)


    //Turn towards optimal angle
    /*
    Calculate the targetX - robotX and targetY - robotY

    Calculate the relative angle (difference to target angle)
     */

    public static void goToPosition(double x, double y, double movementSpeed, double prefferedAngle, double turnSpeed){

        double distanceToTarget = Math.hypot(x-world_x_position, y-world_y_position);

        double absoluteAngleToTarget = Math.atan2(y - world_y_position, x - world_x_position);

        double relativeAngleToPoint = PPMathFunctions.angleWrap(absoluteAngleToTarget - (world_angle_rad - Math.toRadians(90)));

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
