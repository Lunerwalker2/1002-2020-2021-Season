package org.firstinspires.ftc.teamcode.Vision.pipeline;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * In this sample, we demonstrate how to use the {@link OpenCvPipeline#onViewportTapped()}
 * callback to switch which stage of a pipeline is rendered to the viewport for debugging
 * purposes. We also show how to get data from the pipeline to your OpMode.
 */
@TeleOp
public class LiveStoneTracking extends LinearOpMode
{
    private OpenCvCamera camera;
    private Pipeline pipeline;
    private double rotRectAngle = 0;

    private static int thresh = 80;

    int point = 0;

    double aboveMidlineLen = 0;
    double belowMidlineLen = 0;

    private double aboveMidLineDensity = 0;
    private double belowMidLineDensity = 0;

    @Override
    public void runOpMode()
    {
        /*
         * NOTE: Many comments have been omitted from this sample for the
         * sake of conciseness. If you're just starting out with EasyOpenCv,
         * you should take a look at {@link InternalCameraExample} or its
         * webcam counterpart, {@link WebcamExample} first.
         */

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createInternalCamera(
                OpenCvInternalCamera.CameraDirection.BACK,
                cameraMonitorViewId
        );
        camera.openCameraDevice();
        pipeline = new Pipeline();
        camera.setPipeline(pipeline);
        camera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);

        telemetry.setMsTransmissionInterval(20);

        waitForStart();

        while (opModeIsActive())
        {
            sleep(20);

            telemetry.addData("Rot rect angle", rotRectAngle);
            telemetry.addData("Above midline den", aboveMidLineDensity);
            telemetry.addData("Below midline den", belowMidLineDensity);
            telemetry.update();

            if(gamepad1.a)
            {
                System.out.println("hallo");
            }
        }
    }

    class Pipeline extends OpenCvPipeline
    {
        int numContoursFound;
        Mat yCbCrChan2Mat = new Mat();
        Mat thresholdMat = new Mat();
        Mat morphedThreshold = new Mat();
        Mat contoursOnFrameMat = new Mat();
        Mat boundingBoxesOnFrameMat = new Mat();
        List<MatOfPoint> contoursList = new ArrayList<>();

        double aboveMidLineHullArea;
        double aboveMidLineContourArea;

        double belowMidLineHullArea;
        double belowMidLineContourArea;

        DecimalFormat angleFormat = new DecimalFormat("#");

        String[] stages = new String[] {"YCrCb", "FINAL"};

        int stageNum = 0;

        @Override
        public void onViewportTapped()
        {
            /*
             * Note that this method is invoked from the UI thread
             * so whatever we do here, we must do quickly.
             */

            int nextStageNum = stageNum + 1;

            if(nextStageNum >= stages.length)
            {
                nextStageNum = 0;
            }

            stageNum = nextStageNum;
        }

        private void findContours(Mat input)
        {
            contoursList.clear();

            Imgproc.cvtColor(input, yCbCrChan2Mat, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(yCbCrChan2Mat, yCbCrChan2Mat, 2);
            Imgproc.threshold(yCbCrChan2Mat, thresholdMat, thresh, 255, Imgproc.THRESH_BINARY_INV);

            morphMask(thresholdMat, morphedThreshold);

            Imgproc.findContours(morphedThreshold, contoursList, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
            numContoursFound = contoursList.size();
            input.copyTo(contoursOnFrameMat);
            input.copyTo(boundingBoxesOnFrameMat);
            Imgproc.drawContours(contoursOnFrameMat, contoursList, -1, new Scalar(0, 0, 255), 3, 8);
        }

        @Override
        public Mat processFrame(Mat input)
        {
            findContours(input);

            for(MatOfPoint contour : contoursList)
            {
                Point[] points = contour.toArray();
                MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());

                RotatedRect rotatedRect = Imgproc.minAreaRect(contour2f);
                drawRotatedRect(rotatedRect, input);

                rotRectAngle = rotatedRect.angle;

                if (rotatedRect.size.width < rotatedRect.size.height) {
                    rotRectAngle += 90;
                }

                double midlineSlope = Math.tan(Math.toRadians(rotRectAngle));
                int midlineXDispl = (int) (200 * Math.cos(Math.toRadians(rotRectAngle)));
                int midlineYDispl = (int) (200 * Math.sin(Math.toRadians(rotRectAngle)));

                //Imgproc.line(input, new Point(rotatedRect.center.x+midlineXDispl, rotatedRect.center.y+midlineYDispl), new Point(rotatedRect.center.x-midlineXDispl, rotatedRect.center.y-midlineYDispl), new Scalar(255,0,0), 2);

                ArrayList<Point> aboveMidline = new ArrayList<>(points.length/2);
                ArrayList<Point> belowMidline = new ArrayList<>(points.length/2);

                for(Point p : points)
                {
                    if(rotatedRect.center.y - p.y > midlineSlope * (rotatedRect.center.x - p.x))
                    {
                        aboveMidline.add(p);
                    }
                    else
                    {
                        belowMidline.add(p);
                    }
                }

                //Above midline
                ArrayList<MatOfPoint> aboveMidlineContourMatList = new ArrayList<>();
                Point[] aboveMidlineArr = new Point[aboveMidline.size()];
                aboveMidline.toArray(aboveMidlineArr);
                MatOfPoint2f aboveMidlineContourSegment = new MatOfPoint2f(aboveMidlineArr);
                aboveMidlineContourMatList.add(new MatOfPoint(aboveMidlineArr));
                MatOfInt aboveMidlineHull = new MatOfInt();
                Imgproc.convexHull(new MatOfPoint(aboveMidlineArr), aboveMidlineHull);
                if(aboveMidlineHull.toArray().length > 0)
                {
                    Point[] hullPoints = new Point[aboveMidlineHull.rows()];
                    List<Integer> hullContourIdxList = aboveMidlineHull.toList();
                    for (int i = 0; i < hullContourIdxList.size(); i++) {
                        hullPoints[i] = aboveMidlineArr[hullContourIdxList.get(i)];
                    }
                    aboveMidLineHullArea = Imgproc.contourArea(new MatOfPoint(hullPoints));
                    aboveMidLineContourArea = Imgproc.contourArea(aboveMidlineContourMatList.get(0));
                    aboveMidLineDensity = aboveMidLineContourArea /aboveMidLineHullArea;
                }
                //Imgproc.drawContours(staticContourMat, aboveMidlineContourMatList, -1, new Scalar(0, 0, 255), 2, 8);

                //Below midline
                ArrayList<MatOfPoint> belowMidlineContourMatList = new ArrayList<>();
                Point[] belowMidlineArr = new Point[belowMidline.size()];
                belowMidline.toArray(belowMidlineArr);
                MatOfPoint2f belowMidlineContourSegment = new MatOfPoint2f(belowMidlineArr);
                belowMidlineContourMatList.add(new MatOfPoint(belowMidlineArr));
                MatOfInt belowMidlineHull = new MatOfInt();
                Imgproc.convexHull(belowMidlineContourMatList.get(0), belowMidlineHull);
                if(belowMidlineHull.toArray().length > 0)
                {
                    Point[] hullPoints = new Point[belowMidlineHull.rows()];
                    List<Integer> hullContourIdxList = belowMidlineHull.toList();
                    for (int i = 0; i < hullContourIdxList.size(); i++) {
                        hullPoints[i] = belowMidlineArr[hullContourIdxList.get(i)];
                    }
                    belowMidLineHullArea = Imgproc.contourArea(new MatOfPoint(hullPoints));
                    belowMidLineContourArea = Imgproc.contourArea(belowMidlineContourMatList.get(0));
                    belowMidLineDensity = belowMidLineContourArea / belowMidLineHullArea;
                }

                //Imgproc.drawContours(staticContourMat, belowMidlineContourMatList, -1, new Scalar(0, 255, 0), 2, 8);

                /*aboveMidlineLen = Imgproc.arcLength(aboveMidlineContourSegment,true);
                belowMidlineLen = Imgproc.arcLength(belowMidlineContourSegment, true);
                if(aboveMidlineLen > belowMidlineLen)
                {
                    Imgproc.drawContours(input, aboveMidlineContourMatList, -1, new Scalar(0, 0, 255), 2, 8);
                }
                else
                {
                    Imgproc.drawContours(input, belowMidlineContourMatList, -1, new Scalar(0, 255, 0), 2, 8);
                }*/

                int midlineXDispl2 = (int) (60 * Math.cos(Math.toRadians(rotRectAngle+90)));
                int midlineYDispl2 = (int) (60 * Math.sin(Math.toRadians(rotRectAngle+90)));

                if(aboveMidLineDensity < belowMidLineDensity -.03)
                {
                    Imgproc.line(input, new Point(rotatedRect.center.x, rotatedRect.center.y), new Point(rotatedRect.center.x-midlineXDispl2, rotatedRect.center.y-midlineYDispl2), new Scalar(158,52,235), 2);
                    Imgproc.drawContours(input, aboveMidlineContourMatList, -1, new Scalar(3, 148, 252), 2, 8);

                    double angle = -(Math.toDegrees(Math.atan2(midlineYDispl2, midlineXDispl2))-180);

                    drawTextOld(rotatedRect, mapAngleToOrientation(angle), input);
                }
                else if(belowMidLineDensity < aboveMidLineDensity -.03)
                {
                    Imgproc.line(input, new Point(rotatedRect.center.x+midlineXDispl2, rotatedRect.center.y+midlineYDispl2), new Point(rotatedRect.center.x, rotatedRect.center.y), new Scalar(158,52,235), 2);
                    Imgproc.drawContours(input, belowMidlineContourMatList, -1, new Scalar(3, 148, 252), 2, 8);

                    double angle = -(Math.toDegrees(Math.atan2(-midlineYDispl2, -midlineXDispl2))-180);

                    drawTextOld(rotatedRect, mapAngleToOrientation(angle), input);
                }
                else
                {
                    drawTextOld(rotatedRect, "UPRIGHT", input);
                }
            }

            //Imgproc.drawContours(staticContourMat, contours, -1, new Scalar(0, 0, 255), 1, 8);

            //sleep(100);

            switch (stageNum)
            {
                case 0:
                {
                    return yCbCrChan2Mat;
                }

                case 1:
                {
                    return input;
                }
            }

            return input;
        }

        private void morphMask(Mat input, Mat output)
        {
            Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
            Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(6, 6));


            Imgproc.erode(input, output, erodeElement);
            Imgproc.erode(output, output, erodeElement);

            Imgproc.dilate(output, output, dilateElement);
            Imgproc.dilate(output, output, dilateElement);
        }

        void drawTextOld(RotatedRect rect, String text, Mat mat)
        {
            Imgproc.putText(mat, text,
                    new Point(rect.center.x-50, rect.center.y+25),
                    Imgproc.FONT_HERSHEY_PLAIN, 1,
                    new Scalar(255, 0, 0), 1);
        }

        public int getNumContoursFound()
        {
            return numContoursFound;
        }

        void drawRotatedRect(RotatedRect rect, Mat drawOn)
        {
            Point[] points = new Point[4];
            rect.points(points);
            for(int i=0; i<4; ++i){
                Imgproc.line(drawOn, points[i], points[(i+1)%4], new Scalar(255,0,0), 2);
            }
        }

        String mapAngleToOrientation(double angle)
        {
            if((angle <= 45 && angle >= 0) || (angle >= 315 && angle <= 360))
            {
                return "RIGHT";
            }
            else if((angle >= 135 && angle <= 180) || (angle <= 225 && angle >= 180))
            {
                return "LEFT";
            }
            else
            {
                return "DUNNO";
            }
        }

        double getRotatedRectRatio(RotatedRect rect)
        {
            double lenA = rect.size.height;
            double lenB = rect.size.width;

            if(lenA > lenB)
            {
                return lenA / lenB;
            }
            else
            {
                return lenB / lenA;
            }
        }
    }
}