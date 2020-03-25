package org.firstinspires.ftc.teamcode.Robot;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Abstract class that robot components extend which gives opMode-like syntax to access the
 * telemetry, opMode object, and gamepads.
 */
public abstract class Component {



    Robot robot;
    OpMode opMode;
    Telemetry telemetry;
    Gamepad gamepad1, gamepad2;
    HardwareMap hardwareMap;

    static FtcDashboard dashboard = FtcDashboard.getInstance();


     Component(Robot robot) {
        this.robot = robot;
        this.opMode = robot.opMode;
        this.telemetry = opMode.telemetry;
        this.gamepad1 = opMode.gamepad1;
        this.gamepad2 = opMode.gamepad2;
        this.hardwareMap = opMode.hardwareMap;
    }
}
