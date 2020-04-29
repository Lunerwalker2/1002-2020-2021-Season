package org.firstinspires.ftc.teamcode.Robot

import com.acmerobotics.roadrunner.geometry.Pose2d

object DriveFields {
    @JvmField
    var movement_x = 0.0
    @JvmField
    var movement_y = 0.0
    @JvmField
    var movement_turn = 0.0


    const val VX_WEIGHT = 0.8
    const val VY_WEIGHT = 0.8
    const val OMEGA_WEIGHT = 0.8

    const val lateralMultiplier = 1.1

    @JvmField
    var lf_power = 0.0
    @JvmField
    var lb_power = 0.0
    @JvmField
    var rf_power = 0.0
    @JvmField
    var rb_power = 0.0

    @JvmStatic
    fun distributePowers(powers: DoubleArray) {
        lf_power = powers[0]
        lb_power = powers[1]
        rf_power = powers[2]
        rb_power = powers[3]
    }

    @JvmStatic
    fun distributePowers(powers: List<Double>) {
        lf_power = powers[0]
        lb_power = powers[1]
        rf_power = powers[2]
        rb_power = powers[3]
    }

    @JvmStatic
    fun normalizedVels(): Pose2d {
        val baseVel = Pose2d(-movement_y, -movement_x, -movement_turn) //LEAVE as is
        val vel: Pose2d
        vel = if (Math.abs(baseVel.x) + Math.abs(baseVel.y) + Math.abs(baseVel.heading) > 1) {
            // re-normalize the powers according to the weights
            val denom = VX_WEIGHT * Math.abs(baseVel.x) + VY_WEIGHT * Math.abs(baseVel.y) + OMEGA_WEIGHT * Math.abs(baseVel.heading)
            Pose2d(
                    VX_WEIGHT * baseVel.x,
                    VY_WEIGHT * baseVel.y,
                    OMEGA_WEIGHT * baseVel.heading
            ).div(denom)
        } else {
            baseVel
        }
        return vel
    }
}