package org.firstinspires.ftc.teamcode.Util

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.PIDFCoefficients
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType
import org.openftc.revextensions2.ExpansionHubMotor
import kotlin.reflect.KClass

class DriveMotor(name: String, hardwareMap: HardwareMap) {
    val motor = hardwareMap.get(ExpansionHubMotor::class.java, name)

    var direction: DcMotorSimple.Direction = DcMotorSimple.Direction.FORWARD
        set(value) {
            if (value != field) {
                motor.direction = value
                field = value
            }
        }
    var mode: DcMotor.RunMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        set(value) {
            if (field != value) {
                motor.mode = value
                field = value
            }
        }

    var zeroPowerBehavior = DcMotor.ZeroPowerBehavior.UNKNOWN
        set(value) {
            if (value != field) {
                if (value != DcMotor.ZeroPowerBehavior.UNKNOWN)
                    motor.zeroPowerBehavior = value
                field = value
            }
        }

    val reverse: DriveMotor
        get() {
            direction = DcMotorSimple.Direction.REVERSE
            return this
        }

    val forward: DriveMotor
        get() {
            direction = DcMotorSimple.Direction.FORWARD
            return this
        }

    val float: DriveMotor
        get() {
            zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
            return this
        }

    val brake: DriveMotor
        get() {
            zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            return this
        }

    val noEncoders: DriveMotor
        get(){
            mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            return this
        }

    val useEncoders: DriveMotor
        get() {
            mode = DcMotor.RunMode.RUN_USING_ENCODER
            return this
        }

}