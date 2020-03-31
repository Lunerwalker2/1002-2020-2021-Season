package org.firstinspires.ftc.teamcode.Util

import com.acmerobotics.dashboard.canvas.Canvas
import org.firstinspires.ftc.teamcode.Robot.DriveFields
import kotlin.math.abs

object WheelPowerCanvas {
    private const val xArrowMaxLength = 20.0
    private const val yArrowMaxLength = 20.0
    private const val robotRadius = 10.0
    private var robotColor = "#0ceb2d"
    private var lineColor = "#0c6deb"
    private const val zeroThreshold = 0.5

    @JvmStatic
    fun drawRobotMovement(canvas: Canvas) {
        //Draw the robot
        canvas.setStroke(robotColor)
        canvas.strokeCircle(0.0, 0.0, robotRadius)

        //Next decide the length of the lines
        val x_length = xArrowMaxLength * DriveFields.movement_x
        val y_length = yArrowMaxLength * DriveFields.movement_y

        //Change the color
        canvas.setStroke(lineColor)
        //Now draw the lines. If a line is positive
        if (abs(x_length) >= zeroThreshold) canvas.strokeLine(0.0, 0.0, 0.0, x_length)
        if (abs(y_length) >= zeroThreshold) canvas.strokeLine(0.0, 0.0, y_length, 0.0)
    }
}