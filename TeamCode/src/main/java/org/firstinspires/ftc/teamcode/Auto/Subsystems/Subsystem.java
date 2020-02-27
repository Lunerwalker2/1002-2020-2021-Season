package org.firstinspires.ftc.teamcode.Auto.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class Subsystem {


        HardwareMap hardwareMap;


        Subsystem(LinearOpMode opMode){

            hardwareMap = opMode.hardwareMap;

        }

        Subsystem(HardwareMap hardwareMap){
            this.hardwareMap = hardwareMap;
        }

        public abstract void initHardware();

        public abstract void stop();
}
