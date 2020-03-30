package org.firstinspires.ftc.teamcode.RRDev.Quickstart.drive.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.util.Angle;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.RRDev.Quickstart.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Util.ALLIANCE;

/*
 * This is an example of a more complex path to really test the tuning.
 */
@Autonomous(group = "drive")
public class SplineTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(this, ALLIANCE.OTHER, true);

        waitForStart();

        if (isStopRequested()) return;

        Trajectory traj = robot.roadRunnerBase.trajectoryBuilder(new Pose2d())
                .splineTo(new Pose2d(30, 30, 0))
                .build();

        robot.roadRunnerBase.followTrajectory(traj);

        sleep(2000);

        robot.roadRunnerBase.followTrajectory(
                robot.roadRunnerBase.trajectoryBuilder(traj.end(), Angle.norm(traj.end().getHeading() + Math.PI))
                        .splineTo(new Pose2d(0, 0, 0))
                        .build()
        );
    }
}
