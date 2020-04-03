package org.firstinspires.ftc.teamcode.RRDev.Quickstart.drive.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Util.Alliance;

/*
 * This is a simple routine to test turning capabilities.
 */
@Config
@Autonomous(group = "drive")
public class TurnTest extends LinearOpMode {
    public static double ANGLE = 90; // deg

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(this, Alliance.OTHER);

        waitForStart();

        if (isStopRequested()) return;

        robot.roadRunnerBase.turnAsync(Math.toRadians(ANGLE));

        while(opModeIsActive()){
            robot.roadRunnerBase.update();
            robot.update();;
        }
    }
}
