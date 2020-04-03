package org.firstinspires.ftc.teamcode.RRDev.Quickstart.drive.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Util.Alliance;;

/*
 * Op mode for tuning follower PID coefficients (located in the drive base classes). The robot
 * drives in a DISTANCE-by-DISTANCE square indefinitely.
 */
@Config
@Autonomous(group = "drive")
public class FollowerPIDTuner extends LinearOpMode {
    public static double DISTANCE = 48;

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(this, Alliance.OTHER);

        Pose2d startPose = new Pose2d(-DISTANCE / 2, -DISTANCE / 2, 0);

        robot.roadRunnerBase.setPoseEstimate(startPose);

        waitForStart();

        if (isStopRequested()) return;

        while (!isStopRequested()) {
            Trajectory traj = robot.roadRunnerBase.trajectoryBuilder(startPose)
                    .forward(DISTANCE)
                    .build();
            while(opModeIsActive() && robot.roadRunnerBase.isBusy()) {
                robot.roadRunnerBase.followTrajectoryAsync(traj);
                robot.roadRunnerBase.update();
                robot.update();
            }
            while(opModeIsActive() && robot.roadRunnerBase.isBusy()) {
                robot.roadRunnerBase.turn(Math.toRadians(90));
                robot.roadRunnerBase.update();
                robot.update();
            }

            startPose = traj.end().plus(new Pose2d(0, 0, Math.toRadians(90)));
        }
    }
}
