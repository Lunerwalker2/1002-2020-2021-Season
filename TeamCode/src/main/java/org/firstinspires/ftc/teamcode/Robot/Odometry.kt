package org.firstinspires.ftc.teamcode.Robot

import com.arcrobotics.ftclib.geometry.Pose2d
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Func
import org.firstinspires.ftc.teamcode.Odometry.OdometryKt
import org.firstinspires.ftc.teamcode.Util.HardwareNames
import org.openftc.revextensions2.ExpansionHubMotor

class Odometry(robot: Robot, startingPosition: Pose2d) : Component(robot) {

    companion object {
        var world_x_position = 0.0
        var world_y_position = 0.0
        var world_angle_rad = 0.0
        var world_angle_deg = 0.0
    }

    ///The odometers
    private lateinit var left_y_encoder: ExpansionHubMotor ///The odometers
    private lateinit var right_y_encoder: ExpansionHubMotor  ///The odometers
    private lateinit var x_encoder: ExpansionHubMotor


    //The odometry object
    val odometry: OdometryKt

    init {
        setUp(hardwareMap)

        odometry = OdometryKt(Func { this.getLeftYPos() }, Func { this.getRightYPos() }, Func { this.getXPos() })
        odometry.poseEstimate = com.acmerobotics.roadrunner.geometry.Pose2d(
                startingPosition.translation.x,
                startingPosition.translation.y,
                startingPosition.heading
        )
    }

    private fun setUp(hardwareMap: HardwareMap) {
        left_y_encoder = hardwareMap.get(ExpansionHubMotor::class.java, HardwareNames.Odometry.LEFT_Y_ENCODER)
        right_y_encoder = hardwareMap.get(ExpansionHubMotor::class.java, HardwareNames.Odometry.RIGHT_Y_ENCODER)
        x_encoder = hardwareMap.get(ExpansionHubMotor::class.java, HardwareNames.Odometry.X_ENCODER)


        //Reset everything
        left_y_encoder.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        right_y_encoder.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        x_encoder.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        left_y_encoder.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        right_y_encoder.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        x_encoder.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    fun update() {
        odometry.update()
    }

    private fun getLeftYPos() = left_y_encoder.currentPosition

    private fun getRightYPos() = right_y_encoder.currentPosition

    private fun getXPos() = x_encoder.currentPosition
}