package org.firstinspires.ftc.teamcode.Vision.pipeline;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

/**
 * In this sample, we demonstrate how to use the {@link OpenCvPipeline#onViewportTapped()}
 * callback to switch which stage of a pipeline is rendered to the viewport for debugging
 * purposes. We also show how to get data from the pipeline to your OpMode.
 */
@Config
@TeleOp
public class MineralTracking extends LinearOpMode
{
    private OpenCvCamera camera;
    private StageSwitchingPipeline stageSwitchingPipeline;

    private enum Colors {
        LABEL(new Scalar(0, 255, 0)),
        CONTOUR_LINES(new Scalar(10, 220, 20)),
        BOUNDING_CIRCLE(new Scalar(10, 240, 20));

        final Scalar scalar;

        Colors(Scalar scalar) {
            this.scalar = scalar;
        }
    }


    public static double thresholdValue = 99;

    public static double brightnessAlpha = 1.45;

    public static double minimumRadius = 90;



    @Override
    public void runOpMode()
    {




        /**
         * NOTE: Many comments have been omitted from this sample for the
         * sake of conciseness. If you're just starting out with EasyOpenCv,
         * you should take a look at {@link InternalCameraExample} or its
         * webcam counterpart, {@link WebcamExample} first.
         */

        int cameraMonitorViewId = hardwareMap.appContext.getResources()
                .getIdentifier(
                        "cameraMonitorViewId",
                        "id",
                        hardwareMap.appContext.getPackageName()
                );

        camera = OpenCvCameraFactory
                .getInstance()
                .createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        camera.openCameraDevice();
        stageSwitchingPipeline = new StageSwitchingPipeline();
        camera.setPipeline(stageSwitchingPipeline);
        camera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);

        waitForStart();

        telemetry.addData("Num contours found", 0);

        while (opModeIsActive())
        {
            telemetry.addData("Num contours found", stageSwitchingPipeline.getNumContoursFound());
            telemetry.update();
            sleep(100);
        }
    }

    /*
     * With this pipeline, we demonstrate how to change which stage of
     * is rendered to the viewport when the viewport is tapped. This is
     * particularly useful during pipeline development. We also show how
     * to get data from the pipeline to your OpMode.
     */
    static class StageSwitchingPipeline extends OpenCvPipeline
    {
        Mat yCbCrChan2Mat = new Mat();
        List<MatOfPoint> contoursList = new ArrayList<>();
        int numContoursFound;
        Mat blurred = new Mat();
        Mat boundingBoxesDrawn = new Mat();
        Mat cannyOutput = new Mat();

        float[][] radius;
        Point[] centers;



        enum Stage
        {
            RAW_IMAGE("Raw Image"),
            GAUSSIAN_BLUR("Gaussian Blur"),
            YCbCr_CHAN2("Extracted Cb Channel"),
            CANNY_EDGE_DETECTION("Canny Edge Detection"),
            BOUNDING_BOXES_DRAWN("Bounding Boxes");

            String text;

            public void putText(Mat input){
                Imgproc.putText(
                        input,
                        this.text,
                        new Point(70, input.cols()+20),
                        Imgproc.FONT_HERSHEY_SIMPLEX,
                        1,
                        Colors.LABEL.scalar,
                        5
                );
            }

            Stage(String text){
                this.text = text;
            }

        }

        private Stage stageToRenderToViewport = Stage.YCbCr_CHAN2;
        private Stage[] stages = Stage.values();

        @Override
        public void onViewportTapped()
        {
            /*
             * Note that this method is invoked from the UI thread
             * so whatever we do here, we must do quickly.
             */

            int currentStageNum = stageToRenderToViewport.ordinal();

            int nextStageNum = currentStageNum + 1;

            if(nextStageNum >= stages.length)
            {
                nextStageNum = 0;
            }

            stageToRenderToViewport = stages[nextStageNum];
        }

        private void smoothImage(Mat input){
            Imgproc.GaussianBlur(
                    input,
                    blurred,
                    new Size(5, 5),
                    0,
                    0
            );
        }

        private void drawCircles(int i){
            if(isCircleLargeEnough(radius[i][0])) {
                Imgproc.circle(boundingBoxesDrawn, centers[i], (int) radius[i][0], Colors.BOUNDING_CIRCLE.scalar, 3);
            }
        }

        private boolean isCircleLargeEnough(double radius){
            double diameter = radius*radius;
            return (diameter > minimumRadius);
        }


        @Override
        public Mat processFrame(Mat input) {

            contoursList.clear();

            smoothImage(input);

            //Convert to YCrCb color space
            Imgproc.cvtColor(blurred, yCbCrChan2Mat, Imgproc.COLOR_RGB2YCrCb);

            yCbCrChan2Mat.convertTo(yCbCrChan2Mat, -1, brightnessAlpha);

            //Extract the Cb channel
            Core.extractChannel(yCbCrChan2Mat, yCbCrChan2Mat, 2);

            Imgproc.Canny(yCbCrChan2Mat, cannyOutput, thresholdValue, thresholdValue * 2);

            Imgproc.findContours(cannyOutput, contoursList, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

            MatOfPoint2f[] contoursPoly = new MatOfPoint2f[contoursList.size()];
            centers = new Point[contoursList.size()];
            radius = new float[contoursList.size()][1];

            for (int i = 0; i < contoursList.size(); i++) {
                contoursPoly[i] = new MatOfPoint2f();
                Imgproc.approxPolyDP(new MatOfPoint2f(contoursList.get(i).toArray()), contoursPoly[i], 5, true);
                centers[i] = new Point();
                Imgproc.minEnclosingCircle(contoursPoly[i], centers[i], radius[i]);
            }

            List<MatOfPoint> contoursPolyList = new ArrayList<>(contoursPoly.length);
            for (MatOfPoint2f poly : contoursPoly) {
                contoursPolyList.add(new MatOfPoint(poly.toArray()));
            }

            input.copyTo(boundingBoxesDrawn);

            for (int i = 0; i < contoursList.size(); i++) {
                Imgproc.drawContours(boundingBoxesDrawn, contoursPolyList, i, Colors.CONTOUR_LINES.scalar, 2);
                drawCircles(i);
            }
            for (MatOfPoint2f mat : contoursPoly) {
                mat.release();
            }
            contoursPolyList.forEach(MatOfPoint::release);


            switch (stageToRenderToViewport) {
                case YCbCr_CHAN2: {
                    stageToRenderToViewport.putText(yCbCrChan2Mat);
                    return yCbCrChan2Mat;
                }
                case RAW_IMAGE: {
                    stageToRenderToViewport.putText(input);
                    return input;
                }

                case GAUSSIAN_BLUR: {
                    stageToRenderToViewport.putText(blurred);
                    return blurred;
                }

                case CANNY_EDGE_DETECTION:
                    stageToRenderToViewport.putText(cannyOutput);
                    return cannyOutput;

                case BOUNDING_BOXES_DRAWN:
                    stageToRenderToViewport.putText(boundingBoxesDrawn);
                    return boundingBoxesDrawn;

                default: {
                    return input;
                }
            }
        }

        public int getNumContoursFound()
        {
            return numContoursFound;
        }
    }
}
