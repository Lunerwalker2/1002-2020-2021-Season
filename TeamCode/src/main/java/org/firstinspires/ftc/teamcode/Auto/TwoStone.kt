package org.firstinspires.ftc.teamcode.Auto

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.Auto.base.RRAutoBase
import org.firstinspires.ftc.teamcode.Robot.Robot
import org.firstinspires.ftc.teamcode.Util.Alliance
import org.firstinspires.ftc.teamcode.Vision.SubsystemVision
import java.lang.Math.toRadians

@Autonomous(name = "Two Stone Red", group = "Two Stone")
class TwoStoneRed : TwoStone(Alliance.RED)

@Autonomous(name = "Two Stone Blue", group = "Two Stone")
class TwoStoneBlue : TwoStone(Alliance.BLUE)

abstract class TwoStone(alliance: Alliance): RRAutoBase(alliance) {

    val vision = SubsystemVision(this)

    var skystonePosition = 0

    @Throws(InterruptedException::class)
    override fun runOpMode() {

        robot = Robot(this, alliance)

        vision.init()

        vision.startVision()

        while (!isStarted){
            skystonePosition = vision.pipeline.detectedSkystonePosition
            telemetry.addData("Skystone Position", skystonePosition)
            telemetry.update()
        }

        vision.stopVision()

        when(skystonePosition){
            1 -> {
                followAsync(
                        trajectoryBuilder()
                                .splineToLinearHeading(changeSide(Pose2d(-44.0, -35.0, toRadians(90.0))), changeSide(toRadians(90.0)))
                                .build()
                )
                waitAndUpdate()
                followAsync(
                        trajectoryBuilder()
                                .forward(2.0)
                                .build()
                )
                waitAndUpdate()
                followAsync(
                        trajectoryBuilder()
                                .lineTo(changeSide(Vector2d(-28.0, -38.0)))
                                .build()
                )
                waitAndUpdate()
            }
            2 -> {
                followAsync(
                        trajectoryBuilder()
                                .splineToLinearHeading(changeSide(Pose2d(-36.0, -35.0, toRadians(90.0))), changeSide(toRadians(90.0)))
                                .build()
                )
                waitAndUpdate()
                followAsync(
                        trajectoryBuilder()
                                .forward(2.0)
                                .build()
                )
                waitAndUpdate()
                followAsync(
                        trajectoryBuilder()
                                .lineTo(changeSide(Vector2d(-28.0, -38.0)))
                                .build()
                )
                waitAndUpdate()
            }
            3 -> {
                followAsync(
                        trajectoryBuilder()
                                .splineToLinearHeading(changeSide(Pose2d(-29.0, -35.0,toRadians(90.0))), changeSide(toRadians(90.0)))
                                .build()
                )
                waitAndUpdate()
                followAsync(
                        trajectoryBuilder()
                                .forward(2.0)
                                .build()
                )
                waitAndUpdate()
                followAsync(
                        trajectoryBuilder()
                                .lineTo(changeSide(Vector2d(-28.0, -38.0)))
                                .build()
                )
                waitAndUpdate()
            }
        }

        //Go under bridge
        turnAsync(changeSide(toRadians(20.0)))
        waitAndUpdate()
        followAsync(
                trajectoryBuilderReversed(poseEstimate)
                        .lineToLinearHeading(changeSide(Vector2d(18.0, -36.0)), changeSide(toRadians(0.0)))
                        .build()
        )
        waitAndUpdate()
        followAsync(
                trajectoryBuilder()
                        .splineToLinearHeading(changeSide(Pose2d(36.0, -38.0, toRadians(55.0))), changeSide(toRadians(55.0)))
                        .build()
        )
        //Go to foundation
        waitAndUpdate()
        followAsync(
                trajectoryBuilder()
                        .lineToLinearHeading(changeSide(Vector2d(40.0, -33.0)), changeSide(toRadians(90.0)))
                        .build()
        )
        waitAndUpdate()
        followAsync(
                trajectoryBuilder()
                        .back(5.0)
                        .build()
        )
        waitAndUpdate()
        followAsync(
                trajectoryBuilder()
                        .lineToLinearHeading(changeSide(Vector2d(0.0, -36.0)), changeSide(toRadians(180.0)))
                        .build()
        )
        waitAndUpdate()
        followAsync(
                trajectoryBuilder()
                        .splineToLinearHeading(changeSide(Pose2d(-54.0, -40.0, toRadians(90.0 + 10))), changeSide(toRadians(90.0 + 10)))
                        .build()
        )
        waitAndUpdate()
        //Second stone. We can'y reach the first stone, so if it's that we must get a different one
        when(skystonePosition){
            1,2 -> {
                followAsync(
                        trajectoryBuilder()
                                .splineToLinearHeading(changeSide(Pose2d(-60.0, -34.0, toRadians(90.0))), changeSide(toRadians(90.0)))
                                .build()
                )
                waitAndUpdate()
            }
            3 -> {
                followAsync(
                        trajectoryBuilder()
                                .splineToLinearHeading(changeSide(Pose2d(-52.0, -34.0, toRadians(90.0))), changeSide(toRadians(90.0)))
                                .build()
                )
                waitAndUpdate()
            }
        }
        followAsync(
                trajectoryBuilder()
                        .forward(1.0)
                        .build()
        )
        waitAndUpdate()
        followAsync(
                trajectoryBuilder()
                        .back(7.0)
                        .build()
        )
        waitAndUpdate()
        followAsync(
                trajectoryBuilder()
                        .lineToLinearHeading(changeSide(Vector2d(18.0, -38.0)), changeSide(toRadians(55.0)))
                        .build()
        )
        waitAndUpdate()
        followAsync(
                trajectoryBuilder()
                        .lineToLinearHeading(changeSide(Vector2d(48.0, -36.0)), changeSide(toRadians(90.0)))
                        .build()
        )
        waitAndUpdate()
        followAsync(
                trajectoryBuilder()
                        .forward(1.0)
                        .build()
        )
        waitAndUpdate()
        followAsync(
                trajectoryBuilder()
                        .back(5.0)
                        .build()
        )
        waitAndUpdate()
        followAsync(
                trajectoryBuilder()
                        .lineToSplineHeading(changeSide( Vector2d(0.0, -40.0)), changeSide(toRadians(0.0)))
                        .build()
        )
        waitAndUpdate()

        //Normally called on stop(), but here, stop() is whatever we want baby
        robot.stop()
    }
}