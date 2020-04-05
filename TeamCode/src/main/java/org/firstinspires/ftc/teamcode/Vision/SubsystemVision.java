package org.firstinspires.ftc.teamcode.Vision;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Vision.pipeline.Init3BlockDetection;
import org.firstinspires.ftc.teamcode.Vision.pipeline.NaiveRectangleSamplingSkystoneDetectionPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SubsystemVision {

    public Init3BlockDetection pipeline;

    private LinearOpMode opMode;
    private OpenCvCamera camera;

    public SubsystemVision(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    /**
     * Initiates all the necessary hardware.
     */
    public void init(){

        pipeline = new NaiveRectangleSamplingSkystoneDetectionPipeline();
        int cameraMonitorViewId = opMode.hardwareMap.appContext.getResources()
                .getIdentifier(
                        "cameraMonitorViewId",
                        "id",
                        opMode.hardwareMap.appContext.getPackageName()
                );

        camera = OpenCvCameraFactory
                .getInstance()
                .createWebcam(opMode.hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        camera.openCameraDevice();
        camera.setPipeline(pipeline);

    }

    /**Starts the stream. Must be called after {@link SubsystemVision#init()}.
     *
     */
    public void startVision(){
        camera.startStreaming(pipeline.getWidth(), pipeline.getHeight(), OpenCvCameraRotation.UPRIGHT);
    }




    /**
     * Telemetry to use for the stream. Intended for the duration of the streaming
     */
    public void streamLoop() {
        opMode.telemetry.addData("Skystone", () -> pipeline.getDetectedSkystonePosition());
        opMode.telemetry.addData("Skystone Positions",
                () -> pipeline.getSkystonePositions(3)[0] + "" + pipeline.getSkystonePositions(3)[1]);
        opMode.telemetry.addData("Frame Count", () -> camera.getFrameCount());
        opMode.telemetry.addData("FPS", () -> String.format(Locale.US,"%.2f", camera.getFps()));
        opMode.telemetry.addData("Total frame time ms", () ->  camera.getTotalFrameTimeMs());
        opMode.telemetry.addData("Pipeline time ms", () -> camera.getPipelineTimeMs());
        opMode.telemetry.addData("Overhead time ms", () -> camera.getOverheadTimeMs());
        opMode.telemetry.addData("Theoretical max FPS", () -> camera.getCurrentPipelineMaxFps());

        opMode.telemetry.update();
        opMode.sleep(10);
    }

    public Map<String, Object> getStreamData() {
        Map<String, Object> data = new HashMap<>();

        data.put("Skystone", pipeline.getDetectedSkystonePosition());
        data.put("Skystone Positions",
                pipeline.getSkystonePositions(3)[0] + "" + pipeline.getSkystonePositions(3)[1]);
        data.put("Frame Count", camera.getFrameCount());
        data.put("FPS", String.format(Locale.US,"%.2f", camera.getFps()));
        data.put("Total frame time ms", camera.getTotalFrameTimeMs());
        data.put("Pipeline time ms", camera.getPipelineTimeMs());
        data.put("Overhead time ms", camera.getOverheadTimeMs());
        data.put("Theoretical max FPS", camera.getCurrentPipelineMaxFps());

        return data;
    }


    public void stopVision() {
        camera.stopStreaming();
        camera.closeCameraDevice();
    }
}