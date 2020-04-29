package org.firstinspires.ftc.teamcode.Auto

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.Auto.base.RRAutoBase
import org.firstinspires.ftc.teamcode.RRDev.Quickstart.drive.opmode.SplineTest
import org.firstinspires.ftc.teamcode.Robot.Robot
import org.firstinspires.ftc.teamcode.Util.Alliance
import org.firstinspires.ftc.teamcode.Vision.SubsystemVision
import java.lang.Math.toRadians

/*
Red side full trajectory to stone 3:
                                        /.splineToLinearHeading(new Pose2d(-15, -34, toRadians(90 + 10)), toRadians(90 + 15))
                                        /.turn(Math.toRadians(25))
                                        /.lineToLinearHeading(new Vector2d(-20, -28), toRadians(140))
                                        /.forward(3)
                                        /.lineTo(new Vector2d(-28, -36))
                                        /.setReversed(true)
                                        /.turn(toRadians(20))
                                        /.lineToLinearHeading(new Vector2d(18, -36), toRadians(0))
                                        /.setReversed(false)
                                        /.splineToLinearHeading(new Pose2d(36, -38, toRadians(55)), toRadians(55))
                                        /.lineToLinearHeading(new Vector2d(40, -33), toRadians(90))
                                        /.back(5)
                                        .lineToLinearHeading(new Vector2d(0, -36), 0)
 */

@Autonomous(name = "One Stone Red", group = "One Stone")
class OneStoneRed : OneStone(Alliance.RED)

@Autonomous(name = "One Stone Blue", group = "One Stone")
class OneStoneBlue : OneStone(Alliance.BLUE)




abstract class OneStone(alliance: Alliance): RRAutoBase(alliance) {


    val vision = SubsystemVision(this)

    var skystonePosition = 0


    @Throws(InterruptedException::class)
    override fun runOpMode() {
        robot = Robot(this, alliance)

        vision.init()

        vision.startVision()

        while (!isStarted) {
            skystonePosition = vision.pipeline.detectedSkystonePosition
            telemetry.addData("Skystone Position", skystonePosition)
            telemetry.update()
        }

        vision.stopVision()



        when (skystonePosition) {
            1 -> {
                trajectoryBuilder()
                        .splineToLinearHeading(Pose2d(-44.0, -35.0, 90.toRadians).toSide, 90.toRadians.toSide)
                        .build()
                        .beginAsync()
                waitAndUpdate()
                trajectoryBuilder()
                        .forward(2.0)
                        .build()
                        .beginAsync()
                waitAndUpdate()
                trajectoryBuilder()
                        .lineTo(Vector2d(-28.0, -38.0).toSide)
                        .build()
                        .beginAsync()
                waitAndUpdate()
            }
            2 -> {
                trajectoryBuilder()
                        .splineToLinearHeading(Pose2d(-36.0, -35.0, 90.toRadians).toSide, 90.toRadians.toSide)
                        .build()
                        .beginAsync()
                waitAndUpdate()
                trajectoryBuilder()
                        .forward(2.0)
                        .build()
                        .beginAsync()
                waitAndUpdate()
                trajectoryBuilder()
                        .lineTo(Vector2d(-28.0, -38.0).toSide)
                        .build()
                        .beginAsync()
                waitAndUpdate()
            }
            3 -> {
                trajectoryBuilder()
                        .splineToLinearHeading(Pose2d(-29.0, -35.0, 90.toRadians).toSide, 90.toRadians.toSide)
                        .build()
                        .beginAsync()

                waitAndUpdate()
                trajectoryBuilder()
                        .forward(2.0)
                        .build()
                        .beginAsync()
                waitAndUpdate()
                trajectoryBuilder()
                        .lineTo(Vector2d(-28.0, -38.0).toSide)
                        .build()
                        .beginAsync()
                waitAndUpdate()
            }
        }

        //Go under bridge
        turnAsync(20.toRadians.toSide)
        waitAndUpdate()
        trajectoryBuilderReversed()
                .lineToLinearHeading(Vector2d(18.0, -36.0).toSide, 0.toRadians.toSide)
                .build()
                .beginAsync()
        waitAndUpdate()
        trajectoryBuilder()
                .splineToLinearHeading(Pose2d(36.0, -38.0, 55.toRadians).toSide, 55.toRadians.toSide)
                .build()
                .beginAsync()
        //Go to foundation
        waitAndUpdate()
        trajectoryBuilder()
                .lineToLinearHeading(Vector2d(40.0, -33.0).toSide, 90.toRadians.toSide)
                .build()
                .beginAsync()
        //Park
        waitAndUpdate()
        trajectoryBuilder()
                .back(5.0)
                .build()
                .beginAsync()
        waitAndUpdate()
        trajectoryBuilder()
                .lineToLinearHeading(Vector2d(0.0, -36.0).toSide, 0.toRadians.toSide)
                .build()
                .beginAsync()
        waitAndUpdate()

        //Normally called on stop(), but here, stop() is whatever we want baby
        robot.stop()
    }
}
