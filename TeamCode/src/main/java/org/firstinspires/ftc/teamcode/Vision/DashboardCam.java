package org.firstinspires.ftc.teamcode.Vision;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraBase;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.Locale;

/**
 * In this class we will show an example on how to stream live feed from your pipeline to the viewport
 * on {@link FtcDashboard}. This will use the {@link FtcDashboard#startCameraStream(CameraStreamSource, double)}
 * function.
 */
@TeleOp(name="Easy OpenCv Dashboard Stream")
public class DashboardCam extends LinearOpMode {

    private OpenCvInternalCamera phoneCam;

    /*
     * Acquire the FtcDashboard instance
     */
    private FtcDashboard dashboard = FtcDashboard.getInstance();

    private HandlerThread cameraHardwareHandlerThread;
    private Handler cameraHardwareHandler;

    @Override
    public void runOpMode()
    {




        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);



        phoneCam.setPipeline(new SamplePipeline());



        cameraHardwareHandlerThread = new HandlerThread("CameraHardwareHandlerThread");
        cameraHardwareHandlerThread.start();
        cameraHardwareHandler = new Handler(cameraHardwareHandlerThread.getLooper());

        cameraHardwareHandler.post(() -> {
            phoneCam.openCameraDevice();
            phoneCam.setHardwareFrameTimingRange(new OpenCvInternalCamera.FrameTimingRange(30, 30));
            phoneCam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
        });





        /*
         * Change the dashboard image quality to full, since we are already using a relatively low
         * resolution in this example
         */
        dashboard.setImageQuality(100);

        /*
         * Wait for the user to press start on the Driver Station
         */
        waitForStart();



        /*
         * Set the transmission rate to 25ms. This is not required, however it's usually better to
         * limit this since it can take up space during transmission if you aren't careful
         */
        dashboard.setTelemetryTransmissionInterval(25);


        /*
         * Start the dashboard stream using frames from this camera
         */
        dashboard.startCameraStream((OpenCvCameraBase) phoneCam, 30);

        while (opModeIsActive())
        {
            TelemetryPacket packet = new TelemetryPacket();
            /*
             * Send some stats to the telemetry
             *
             * Make use of dashboard's telemetry packets, which can allow the graphing of data.
             * In reality, it doesn't matter what you use, but this is nice
             */
            packet.put("Frame Count", phoneCam.getFrameCount());
            packet.put("FPS", String.format(Locale.US, "%.2f", phoneCam.getFps()));
            packet.put("Total frame time ms", phoneCam.getTotalFrameTimeMs());
            packet.put("Pipeline time ms", phoneCam.getPipelineTimeMs());
            packet.put("Overhead time ms", phoneCam.getOverheadTimeMs());
            packet.put("Theoretical max FPS", phoneCam.getCurrentPipelineMaxFps());

            /*
             * Send the packet to dashboard
             */
            dashboard.sendTelemetryPacket(packet);
        }

        /*
         * Stop the stream when the opMode finishes
         */
        dashboard.stopCameraStream();
    }


    class SamplePipeline extends OpenCvPipeline
    {

        @Override
        public Mat processFrame(Mat input)
        {

            /*
             * Draw a simple box around the middle 1/2 of the entire frame
             */
            Imgproc.rectangle(
                    input,
                    new Point(
                            input.cols()/4,
                            input.rows()/4),
                    new Point(
                            input.cols()*(3f/4f),
                            input.rows()*(3f/4f)),
                    new Scalar(0, 255, 0), 4);

            return input;
        }
    }


}
