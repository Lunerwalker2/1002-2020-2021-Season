package org.firstinspires.ftc.teamcode.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.RRDev.Quickstart.util.DashboardUtil;
import org.firstinspires.ftc.teamcode.Robot.Odometry;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Util.Alliance;
import org.firstinspires.ftc.teamcode.Util.HardwareNames;

import static java.lang.Math.addExact;
import static java.lang.Math.toDegrees;

public class OdometryTest extends LinearOpMode {


    private FtcDashboard dashboard = FtcDashboard.getInstance();

    private Pose2d startingPosition = new Pose2d(0, 0, Rotation2d.fromDegrees(0));


    private Robot robot;

    private BNO055IMU imu;

    private double addOffsetDeg(double heading, double offset){
        return AngleUnit.normalizeDegrees(heading + offset);
    }

    private double addOffsetRad(double heading, double offset){
        return AngleUnit.normalizeRadians(heading + offset);
    }


    @Override
    public void runOpMode() throws InterruptedException {


        imu = hardwareMap.get(BNO055IMU.class, HardwareNames.Sensors.LEFT_HUB_IMU);
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);

        //Set up the robot
        Robot.userStartingPosition = startingPosition;
        robot = new Robot(this, Alliance.OTHER);


        waitForStart();


        while (opModeIsActive()){

            double rawExternalHeadingRad = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS).firstAngle;

            Canvas fieldOverlay = robot.packet.fieldOverlay();

            robot.packet.put("Robot Heading IMU (deg)", addOffsetDeg(toDegrees(rawExternalHeadingRad), -90));
            robot.packet.put("Robot Heading IMU (rad)", addOffsetRad(rawExternalHeadingRad, -90));



            fieldOverlay.setStroke("##18cf15");
            DashboardUtil.drawRobot(fieldOverlay, new com.acmerobotics.roadrunner.geometry.Pose2d(Odometry.world_x_position, Odometry.world_y_position, Odometry.world_angle_rad));

            robot.update();
        }


    }
}
