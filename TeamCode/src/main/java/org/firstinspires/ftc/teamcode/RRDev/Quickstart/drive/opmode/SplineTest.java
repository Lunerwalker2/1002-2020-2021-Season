package org.firstinspires.ftc.teamcode.RRDev.Quickstart.drive.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.util.Angle;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Util.Alliance;

/*
 * This is an example of a more complex path to really test the tuning.
 */
@Autonomous(group = "drive")
public class SplineTest extends LinearOpMode {

    ElapsedTime timer = new ElapsedTime();
    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(this, Alliance.OTHER);

        waitForStart();

        if (isStopRequested()) return;

        Trajectory traj = robot.roadRunnerBase.trajectoryBuilder(new Pose2d())
                .splineTo(new Pose2d(30, 30, 0))
                .build();

        robot.roadRunnerBase.followTrajectoryAsync(traj);

        timer.reset();
        while(opModeIsActive() && timer.milliseconds() < 2000){
            robot.roadRunnerBase.update();
            robot.update();
        }

        robot.roadRunnerBase.followTrajectoryAsync(
                robot.roadRunnerBase.trajectoryBuilder(traj.end(), Angle.norm(traj.end().getHeading() + Math.PI))
                        .splineTo(new Pose2d(0, 0, 0))
                        .build()
        );

        while (opModeIsActive()) {
            robot.roadRunnerBase.update();
            robot.update();
        }
    }
}
