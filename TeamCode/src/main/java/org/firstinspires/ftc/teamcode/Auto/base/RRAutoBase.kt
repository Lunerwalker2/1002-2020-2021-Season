package org.firstinspires.ftc.teamcode.Auto.base

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.Robot.Robot
import org.firstinspires.ftc.teamcode.Util.Alliance

abstract class RRAutoBase(val alliance: Alliance): LinearOpMode() {

    lateinit var robot: Robot

    fun changeSide(pose: Pose2d): Pose2d {
        return if(alliance == Alliance.BLUE) {
            Pose2d(pose.x, pose.y * -1, pose.heading * -1)
        } else{
            Pose2d(pose.x, pose.y, pose.heading)
        }
    }

    fun changeSide(vec: Vector2d): Vector2d {
        return if(alliance == Alliance.BLUE) {
            Vector2d(vec.x, vec.y * -1)
        } else{
            Vector2d(vec.x, vec.y)
        }
    }

    fun changeSide(angle: Double): Double{
        return if (alliance == Alliance.BLUE){
            angle * -1
        } else{
            angle
        }
    }


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

    fun turnAsync(angle: Double) = robot.roadRunnerBase.turnAsync(angle)

    fun trajectoryBuilder(poseEstimate: Pose2d): TrajectoryBuilder = robot.roadRunnerBase.trajectoryBuilder(poseEstimate)

    fun trajectoryBuilder():TrajectoryBuilder {
        return trajectoryBuilder(poseEstimate)
    }

    fun trajectoryBuilderReversed(poseEstimate: Pose2d): TrajectoryBuilder {
        return TrajectoryBuilder(poseEstimate, true, robot.roadRunnerBase.constraints)
    }

    val poseEstimate: Pose2d
        get() {
            return robot.roadRunnerBase.poseEstimate
        }
}