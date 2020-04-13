package org.firstinspires.ftc.teamcode.Vision;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.openftc.easyopencv.OpenCvPipeline;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


@TeleOp(name = "OpenCv Stream")
public class OpenCVStream extends LinearOpMode {






    @Override
    public void runOpMode() {

        SubsystemVision vision = new SubsystemVision(this);


        vision.init();

        waitForStart();

        telemetry.addData("Ready for start", " > ");
        telemetry.update();

        vision.startVision();

        vision.streamLoop();
        while (opModeIsActive()) {
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