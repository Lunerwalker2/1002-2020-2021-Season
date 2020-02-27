package org.firstinspires.ftc.teamcode.TeleOp.TeleOpSystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;


import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class TeleOpSystem {

    Gamepad gamepad1;
    Gamepad gamepad2;

    HardwareMap hardwareMap;

    Telemetry telemetry;

    TeleOpSystem(OpMode opMode){
        gamepad1 = opMode.gamepad1;
        gamepad2 = opMode.gamepad2;

        hardwareMap = opMode.hardwareMap;

        telemetry = opMode.telemetry;
    }

    public void onStart(){}

    public abstract void initHardware();

    public abstract void update();

    public abstract void stop();
}
