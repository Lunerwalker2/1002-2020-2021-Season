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

@Autonomous(name = "One Stone Red", group = "Autonomous")
class OneStoneRed : OneStone(Alliance.RED)

@Autonomous
class OneStoneBlue : OneStone(Alliance.BLUE)




abstract class OneStone(alliance: Alliance): RRAutoBase(alliance) {


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
                        trajectoryBuilder(poseEstimate)
                                .splineToLinearHeading(changeSide(Pose2d(-15.0, -34.0, toRadians(90.0 + 10.0))), changeSide(toRadians(90.0 + 15.0)))
                                .build()
                )
                waitAndUpdate()
                turnAsync(changeSide(25.0))
                waitAndUpdate()
                followAsync(
                        trajectoryBuilder(poseEstimate)
                                .lineToLinearHeading(changeSide(Vector2d(-20.0, -28.0)), changeSide(toRadians(140.0)))
                                .build()
                )
                waitAndUpdate()
                followAsync(
                        trajectoryBuilder(poseEstimate)
                                .forward(3.0)
                                .build()
                )
                waitAndUpdate()
                followAsync(
                        trajectoryBuilder(poseEstimate)
                                .lineTo(changeSide(Vector2d(-28.0, -36.0)))
                                .build()
                )
                waitAndUpdate()
            }
            2 -> {

            }
            3 -> {

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
                trajectoryBuilder(poseEstimate)
                        .splineToLinearHeading(changeSide(Pose2d(36.0, -38.0, toRadians(55.0))), changeSide(toRadians(55.0)))
                        .build()
        )
        //Go to foundation
        waitAndUpdate()
        followAsync(
                trajectoryBuilder(poseEstimate)
                        .lineToLinearHeading(changeSide(Vector2d(40.0, -33.0)), changeSide(toRadians(90.0)))
                        .build()
        )
        //Park
        waitAndUpdate()
        followAsync(
                trajectoryBuilder(poseEstimate)
                        .back(5.0)
                        .build()
        )
        waitAndUpdate()
        followAsync(
                trajectoryBuilder(poseEstimate)
                        .lineToLinearHeading(changeSide(Vector2d(0.0, -36.0)), changeSide(toRadians(0.0)))
                        .build()
        )
        waitAndUpdate()
      
      //Normally called on stop(), but here, stop() is whatever we want baby
      robot.stop()
    }
}
