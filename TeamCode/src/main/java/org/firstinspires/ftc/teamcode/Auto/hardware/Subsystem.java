package org.firstinspires.ftc.teamcode.Auto.hardware;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

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
