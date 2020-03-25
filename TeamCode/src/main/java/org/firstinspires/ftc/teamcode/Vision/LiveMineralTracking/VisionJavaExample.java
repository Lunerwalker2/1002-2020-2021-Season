package org.firstinspires.ftc.teamcode.Vision.LiveMineralTracking;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

@TeleOp
@Disabled
public class VisionJavaExample extends LinearOpMode {
    MasterVision vision;

    @Override
    public void runOpMode() throws InterruptedException {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;// recommended camera direction
        parameters.vuforiaLicenseKey = getKey();



        vision = new MasterVision(parameters, hardwareMap);
        vision.init();// enables the camera overlay. this will take a couple of seconds
        vision.enable();// enables the tracking algorithms. this might also take a little time

        waitForStart();

        vision.disable();// disables tracking algorithms. this will free up your phone's processing power for other jobs.

        int goldPosition = vision.getTfLite().getExampleField();

        while (opModeIsActive()) {

            telemetry.update();
        }

        vision.shutdown();
    }

    private String getKey(){
        return new StringBuilder().append("AeCl8dv/////AAABmU0vhtooEkbCoy9D8hM/")
                .append("5Yh8AhufXZT4nSVVD16Vjh1o/rLFmV")
                .append("yKVPNW3S/lXY0UWmDBpSNPS5yMk6lZoMFh")
                .append("TMoq9BMbmXHJ9KU+uKvC+GVp5cuEo18HvMpLM")
                .append("PPNmIVoXgOv9CqDfnRCOLSCblZf5cRF+E/LNqkZU")
                .append("7dEnEe/rrDq76FjVXruSdMBmUefIhu853VEpgvJPJTNopNj")
                .append("E0yU5TJ3+Uprgldx7fdy//VPG8PfXc")
                .append("axLj4EJOzEKwJuCNdPS43bio37xbTbnLTz")
                .append("bKmfTqCI6BJpPaK5fXCk7o5xdVewJJbZC")
                .append("A8DDuNX6KUTT//OJ1UEWnMSYw5H1BrWMk")
                .append("ytK5Syws7gdsCpYUshsQX7VP51")
                .toString();
    }
}