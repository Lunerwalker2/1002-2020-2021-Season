package org.firstinspires.ftc.teamcode.PPDev;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.ArrayList;

import static org.firstinspires.ftc.teamcode.PPDev.PPMovement.followCurve;

public class PPMain extends LinearOpMode {


    @Override
    public void runOpMode(){


        /*
         * Todo
         * When robot is near the last target point, just set the follow point to the last point in the list
         *
         * When it gets close to the target point, just set the follow point to the last point in the list",
         *  does he mean the target point as in the last point in the list? - yes
         *
         * Cut power near end?
         *
         * So basically when you are close to the last point of the path, reroute the follow point calculation to just go through the goToPosition(lastPointInPath).
         *
         *
         */


        waitForStart();


        ArrayList<CurvePoint> allPoints = new ArrayList<>();
        allPoints.add(new CurvePoint(0.0, 0.0, 1.0, 1.0, 50.0, Math.toRadians(50),1.0));
        allPoints.add(new CurvePoint(0.0, 0.0, 1.0, 1.0, 50.0, Math.toRadians(50),1.0));
        allPoints.add(new CurvePoint(0.0, 0.0, 1.0, 1.0, 50.0, Math.toRadians(50),1.0));
        allPoints.add(new CurvePoint(0.0, 0.0, 1.0, 1.0, 50.0, Math.toRadians(50),1.0));
        allPoints.add(new CurvePoint(0.0, 0.0, 1.0, 1.0, 50.0, Math.toRadians(50),1.0));
        allPoints.add(new CurvePoint(0.0, 0.0, 1.0, 1.0, 50.0, Math.toRadians(50),1.0));

        followCurve(allPoints, Math.toRadians(90));
    }
}
