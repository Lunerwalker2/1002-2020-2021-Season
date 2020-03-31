package org.firstinspires.ftc.teamcode.Odometry

import android.support.annotation.NonNull
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.localization.ThreeTrackingWheelLocalizer
import org.firstinspires.ftc.robotcore.external.Func


class OdometryKt(val getLeftPosition: Func<Int>, val getRightPosition: Func<Int>, val getCenterPosition: Func<Int>):
        ThreeTrackingWheelLocalizer(listOf(
        Pose2d(0.0, design.TRACK_WIDTH / 2, 0.0),  // left
        Pose2d(0.0, -design.TRACK_WIDTH / 2, 0.0),  // right
        Pose2d(design.FORWARD_OFFSET, 0.0, Math.toRadians(90.0)) // front
        )) {

    companion object {
        private val design = DesignCharacteristics(14.5138, 3.55598425197)

        private val odometers = OdometerCharacteristics(1440.0, 1.41732283465, 1.0)


        fun ticksToInches(ticks: Int): Double {
            return odometers.WHEEL_RADIUS * 2 * Math.PI * odometers.GEAR_RATIO * ticks / odometers.TICKS_PER_REV
        }
    }

    private var last_left_y = 0
    private var last_right_y = 0
    private var last_x = 0


    @NonNull
    override fun getWheelPositions(): List<Double> {
        val positions = listOf(
                ticksToInches(getLeftPosition.value() - last_left_y),
                ticksToInches(getRightPosition.value() - last_right_y),
                ticksToInches(getCenterPosition.value() - last_x) //returns in radians
        )
        last_left_y = getLeftPosition.value()
        last_right_y = getRightPosition.value()
        last_x = getCenterPosition.value()
        return positions;
    }

}


/**
 * TRACK_WIDTH = the trackwidth of the two y encoders
 * FORWARD_OFFSET = the offset of the x encoder
 */
data class DesignCharacteristics(val TRACK_WIDTH: Double, val FORWARD_OFFSET: Double)
data class OdometerCharacteristics(val TICKS_PER_REV: Double, val WHEEL_RADIUS: Double, val GEAR_RATIO: Double)