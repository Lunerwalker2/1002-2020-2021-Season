package org.firstinspires.ftc.teamcode.Vision.pipeline

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.openftc.easyopencv.*
import java.util.*
import java.util.function.Consumer

@TeleOp
class FoundationDetection : LinearOpMode() {

    companion object {
        var redThresholdValue = 100.0
        var blueThresholdValue = 100.0
    }

    private var camera: OpenCvCamera? = null
    private var foundationDetector: FoundationDetector? = null

    @Throws(InterruptedException::class)
    override fun runOpMode() {
        val cameraMonitorViewId = hardwareMap.appContext.resources
                .getIdentifier(
                        "cameraMonitorViewId",
                        "id",
                        hardwareMap.appContext.packageName
                )
        camera = OpenCvCameraFactory
                .getInstance()
                .createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId)
        camera?.openCameraDevice()
        foundationDetector = FoundationDetector()
        camera?.setPipeline(foundationDetector)
        camera?.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT)
        waitForStart()
        while (opModeIsActive()) {
            val packet = TelemetryPacket()
            packet.put("Frame Count", camera?.getFrameCount())
            packet.put("Total frame time ms", camera?.getTotalFrameTimeMs())
            packet.put("Pipeline time ms", camera?.getPipelineTimeMs())
            packet.put("Overhead time ms", camera?.getOverheadTimeMs())
            packet.put("Theoretical max FPS", camera?.getCurrentPipelineMaxFps())
            FtcDashboard.getInstance().sendTelemetryPacket(packet)
            sleep(100)
        }
    }

    internal class FoundationDetector : OpenCvPipeline() {
        var blurred = Mat()
        var CbChannel = Mat()
        var CrChannel = Mat()
        var redThreshold = Mat()
        var blueThreshold = Mat()
        var boundingBoxesDrawn = Mat()
        var blueContoursList: MutableList<MatOfPoint> = ArrayList()
        var redContoursList: MutableList<MatOfPoint> = ArrayList()
        lateinit var redBoundingRects: Array<Rect?>
        lateinit var blueBoundingRects: Array<Rect?>
        lateinit var blueContoursPoly: Array<MatOfPoint2f?>
        lateinit var redContoursPoly: Array<MatOfPoint2f?>

        internal enum class Stage(var text: String) {
            RAW_IMAGE("Raw Image"), GAUSSIAN_BLUR("Gaussian Blur"), YCbCr_CHAN2("Extracted Cb Channel"), YCbCr_CHAN1("Extracted Cr Channel"), THRESHOLD_RED("Red Threshold"), THRESHOLD_BLUE("Blue Threshold"), BOUNDING_BOXES_DRAWN("Bounding Boxes");

            fun putText(input: Mat) {
                Imgproc.putText(
                        input,
                        text,
                        Point(70.0, (input.cols() + 20).toDouble()),
                        Imgproc.FONT_HERSHEY_SIMPLEX, 1.0,
                        Scalar(10.0, 255.0, 20.0),
                        5
                )
            }

        }

        private var stageToRenderToViewport = Stage.RAW_IMAGE
        private val stages = Stage.values()
        override fun onViewportTapped() {
            /*
             * Note that this method is invoked from the UI thread
             * so whatever we do here, we must do quickly.
             */
            val currentStageNum = stageToRenderToViewport.ordinal
            var nextStageNum = currentStageNum + 1
            if (nextStageNum >= stages.size) {
                nextStageNum = 0
            }
            stageToRenderToViewport = stages[nextStageNum]
        }

        private fun smoothImage(input: Mat) {
            Imgproc.GaussianBlur(
                    input,
                    blurred,
                    Size(5.0, 5.0), 0.0, 0.0)
        }

        private fun drawFoundationsBlue(i: Int) {
            if (checkSizes(blueBoundingRects[i])) {
                Imgproc.rectangle(boundingBoxesDrawn, blueBoundingRects[i]!!.tl(), blueBoundingRects[i]!!.br(), Scalar(10.0, 10.0, 255.0), 3)
            }
        }

        private fun drawFoundationsRed(i: Int) {
            if (checkSizes(redBoundingRects[i])) {
                Imgproc.rectangle(boundingBoxesDrawn, redBoundingRects[i]!!.tl(), redBoundingRects[i]!!.br(), Scalar(255.0, 10.0, 10.0), 3)
            }
        }

        private fun checkSizes(rect: Rect?): Boolean {
            return rect!!.width > 90 && rect.height > 10
        }

        override fun processFrame(input: Mat): Mat {
            redContoursList.clear()
            blueContoursList.clear()

            //remove the noise
            smoothImage(input)

            //Convert to YCbCr (although OpenCv holds it as YCrCb)
            Imgproc.cvtColor(blurred, blurred, Imgproc.COLOR_RGB2YCrCb)

            //Extract the Cb (blue-difference) channel
            Core.extractChannel(blurred, CbChannel, 2)

            //Extract the Cr (red-difference) channel
            Core.extractChannel(blurred, CrChannel, 2)

            //Apply a binary threshold to the blue channel
            Imgproc.threshold(CbChannel, blueThreshold, blueThresholdValue, 255.0, Imgproc.THRESH_BINARY)

            //Apply a binary threshold to the red channel
            Imgproc.threshold(CrChannel, redThreshold, redThresholdValue, 255.0, Imgproc.THRESH_BINARY)

            //Find the contours of the blue channel
            Imgproc.findContours(blueThreshold, blueContoursList, Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE)

            //Find the contours of the red channel
            Imgproc.findContours(blueThreshold, redContoursList, Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE)
            blueContoursPoly = arrayOfNulls(blueContoursList.size)
            redContoursPoly = arrayOfNulls(redContoursList.size)
            blueBoundingRects = arrayOfNulls(blueContoursList.size)
            redBoundingRects = arrayOfNulls(redContoursList.size)

            //Iterate through each item
            for (i in blueContoursList.indices) {
                blueContoursPoly[i] = MatOfPoint2f()
                Imgproc.approxPolyDP(MatOfPoint2f(*blueContoursList[i].toArray()), blueContoursPoly[i], 3.0, true)
                //Find the rotated rect of the contours
                blueBoundingRects[i] = Imgproc.boundingRect(MatOfPoint(*blueContoursPoly[i]!!.toArray()))
            }
            for (i in redContoursList.indices) {
                redContoursPoly[i] = MatOfPoint2f()
                Imgproc.approxPolyDP(MatOfPoint2f(*redContoursList[i].toArray()), redContoursPoly[i], 3.0, true)
                //Find the rotated rect of the contours
                redBoundingRects[i] = Imgproc.boundingRect(MatOfPoint(*blueContoursPoly[i]!!.toArray()))
            }

            //Add the contours to the list
            val blueContoursPolyList: MutableList<MatOfPoint> = ArrayList(blueContoursPoly.size)
            val redContoursPolyList: MutableList<MatOfPoint> = ArrayList(redContoursPoly.size)
            for (poly in blueContoursPoly) {
                blueContoursPolyList.add(MatOfPoint(*poly!!.toArray()))
            }
            for (poly in redContoursPoly) {
                redContoursPolyList.add(MatOfPoint(*poly!!.toArray()))
            }
            input.copyTo(boundingBoxesDrawn)
            for (i in blueContoursList.indices) {
                Imgproc.drawContours(boundingBoxesDrawn, blueContoursPolyList, i, Scalar(10.0, 10.0, 255.0), 2)
                drawFoundationsBlue(i)
            }
            for (i in redContoursList.indices) {
                Imgproc.drawContours(boundingBoxesDrawn, redContoursPolyList, i, Scalar(255.0, 10.0, 10.0), 2)
                drawFoundationsRed(i)
            }
            blueContoursPolyList.forEach(Consumer { obj: MatOfPoint -> obj.release() })
            redContoursPolyList.forEach(Consumer { obj: MatOfPoint -> obj.release() })
            for (matOfPoint2f in blueContoursPoly) {
                matOfPoint2f!!.release()
            }
            for (matOfPoint2f in redContoursPoly) {
                matOfPoint2f!!.release()
            }
            blueContoursList.forEach(Consumer { obj: MatOfPoint -> obj.release() })
            redContoursList.forEach(Consumer { obj: MatOfPoint -> obj.release() })
            return when (stageToRenderToViewport) {
                Stage.YCbCr_CHAN2 -> {
                    stageToRenderToViewport.putText(CbChannel)
                    CbChannel
                }
                Stage.YCbCr_CHAN1 -> {
                    stageToRenderToViewport.putText(CrChannel)
                    CrChannel
                }
                Stage.RAW_IMAGE -> {
                    stageToRenderToViewport.putText(input)
                    input
                }
                Stage.GAUSSIAN_BLUR -> {
                    stageToRenderToViewport.putText(blurred)
                    blurred
                }
                Stage.THRESHOLD_RED -> {
                    stageToRenderToViewport.putText(redThreshold)
                    redThreshold
                }
                Stage.THRESHOLD_BLUE -> {
                    stageToRenderToViewport.putText(blueThreshold)
                    blueThreshold
                }
                Stage.BOUNDING_BOXES_DRAWN -> {
                    stageToRenderToViewport.putText(boundingBoxesDrawn)
                    boundingBoxesDrawn
                }
            }
        }
    }


}