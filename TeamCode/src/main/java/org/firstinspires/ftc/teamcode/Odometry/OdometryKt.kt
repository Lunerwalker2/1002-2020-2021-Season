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


    @NonNull
    override fun getWheelPositions(): List<Double> {
        return listOf(
                ticksToInches(getLeftPosition.value()),
                ticksToInches(getRightPosition.value()),
                ticksToInches(getCenterPosition.value()) //returns in radians
        )
    }

}


/**
 * TRACK_WIDTH = the trackwidth of the two y encoders
 * FORWARD_OFFSET = the offset of the x encoder
 */
data class DesignCharacteristics(val TRACK_WIDTH: Double, val FORWARD_OFFSET: Double)
data class OdometerCharacteristics(val TICKS_PER_REV: Double, val WHEEL_RADIUS: Double, val GEAR_RATIO: Double)