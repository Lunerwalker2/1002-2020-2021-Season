package org.firstinspires.ftc.teamcode.PPDev;

import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Auto.Subsystems.Drive;

import java.util.ArrayList;

import static org.firstinspires.ftc.teamcode.PPDev.PPMovement.followCurve;
import static org.firstinspires.ftc.teamcode.PPDev.PPMovement.withinRangeOfEnd;

public class PPMain extends LinearOpMode {

    //The odometry class
    private PPOdo odo;

    //The drive class
    private Drive drive;


    //The starting position of the robot (use inches and the origin at the red loading zone and measured in inches. heading in radians
    private Pose2d starting_position = new Pose2d(0, 0, new Rotation2d(0.5));


    @Override
    public void runOpMode(){

        odo = new PPOdo(hardwareMap, starting_position, PPOdo.HeadingMode.HEADING_FROM_ENCODERS);
        drive = new Drive(this);

        drive.initHardware();
        drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        drive.reverseRightSide();
        drive.stop();

        /*
         * Todo
         * Find units for distance (odo/coords) and heading (deg/rad)
         *
         * heading = rad
         * distance = whatever we want, but please use inches
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


        while(opModeIsActive()) {
            odo.update();
            followCurve(allPoints, Math.toRadians(90));
            if(!withinRangeOfEnd){
                drive.update();
            } else {
                drive.stopDrive();
            }
        }

        drive.stop();
    }
}
