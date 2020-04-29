package org.firstinspires.ftc.teamcode.Auto.base

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.Robot.Robot
import org.firstinspires.ftc.teamcode.Util.Alliance


abstract class RRAutoBase(val alliance: Alliance): LinearOpMode() {


    //My LOOONG list of helpful extension functions (they are sooooo cool)


    //angle stuff
    val Double.toRadians get() = (Math.toRadians(this))
    val Int.toRadians get() = (Math.toRadians(this.toDouble()))
    val Float.toRadians get() = (Math.toRadians(this.toDouble())) //probably unnecessary

    //position stuff
    val Pose2d.toSide get() = (changeSide(this))
    val Vector2d.toSide get() = (changeSide(this))
    val Double.toSide get() = (changeSide(this))
    val Int.toSide get() = (changeSide(this.toDouble()))

    val Pose2d.flip get() = (Pose2d(this.x, -this.y, -this.heading))
    val Vector2d.flip get() = (Vector2d(this.x, -this.y))

    //trajectory stuff
    fun Trajectory.beginAsync() = followAsync(this)
    fun Trajectory.begin() = follow(this)


    lateinit var robot: Robot

    fun changeSide(pose: Pose2d): Pose2d {
        return if(alliance == Alliance.BLUE) {
            pose.flip
        } else{
            pose
        }
    }

    fun changeSide(vec: Vector2d): Vector2d {
        return if(alliance == Alliance.BLUE) {
            vec.flip
        } else{
            vec
        }
    }

    fun changeSide(angle: Double): Double = angle * alliance.sign

    fun trajBusy() = robot.roadRunnerBase.isBusy

    fun isActiveAndBusy(): Boolean{
        return opModeIsActive() && trajBusy()
    }

    fun waitAndUpdate(){
        while (isActiveAndBusy()){
            robot.update()
        }
    }

    fun followAsync(trajectory: Trajectory) = robot.roadRunnerBase.followTrajectoryAsync(trajectory)

    fun follow(trajectory: Trajectory) = robot.roadRunnerBase.followTrajectory(trajectory)

    fun turnAsync(angle: Double) = robot.roadRunnerBase.turnAsync(angle)

    fun turn(angle: Double) = robot.roadRunnerBase.turn(angle)

    fun trajectoryBuilder(poseEstimate: Pose2d): TrajectoryBuilder = robot.roadRunnerBase.trajectoryBuilder(poseEstimate)

    fun trajectoryBuilder() = trajectoryBuilder(poseEstimate)

    fun trajectoryBuilderReversed(poseEstimate: Pose2d): TrajectoryBuilder {
        return TrajectoryBuilder(poseEstimate, true, robot.roadRunnerBase.constraints)
    }

    fun trajectoryBuilderReversed() = trajectoryBuilderReversed(poseEstimate)

    val poseEstimate: Pose2d
        get() {
            return robot.roadRunnerBase.poseEstimate
        }
}