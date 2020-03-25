package org.firstinspires.ftc.teamcode.Vision;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;


@TeleOp(name = "OpenCv Stream")
public class OpenCVStream extends LinearOpMode {






    @Override
    public void runOpMode(){

        SubsystemVision vision = new SubsystemVision(hardwareMap, this);


        vision.initHardware();

        waitForStart();

        telemetry.addData("Ready for start", " > ");
        telemetry.update();

        vision.startVision();

        vision.streamLoop();
        while(opModeIsActive()){
            telemetry.update();

        }

        vision.stopVision();

    }

    static class UselessTesting extends OpenCvPipeline {


        @Override
        public Mat processFrame(Mat input){
            return input;
        }
    }


}