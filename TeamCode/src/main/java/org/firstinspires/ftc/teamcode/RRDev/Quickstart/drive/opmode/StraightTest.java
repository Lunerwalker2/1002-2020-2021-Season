package org.firstinspires.ftc.teamcode.RRDev.Quickstart.drive.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Util.Alliance;

/*
 * This is a simple routine to test translational drive capabilities.
 */
@Config
@Autonomous(group = "drive")
public class StraightTest extends LinearOpMode {
    public static double DISTANCE = 60;

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(this, Alliance.OTHER);

        Trajectory trajectory = robot.roadRunnerBase.trajectoryBuilder(new Pose2d())
                .forward(DISTANCE)
                .build();

        waitForStart();

        if (isStopRequested()) return;

        robot.roadRunnerBase.followTrajectoryAsync(trajectory);

        while(opModeIsActive()){
            robot.roadRunnerBase.update();
            robot.update();
        }
    }
}
