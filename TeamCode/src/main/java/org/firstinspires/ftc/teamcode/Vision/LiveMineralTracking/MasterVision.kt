package org.firstinspires.ftc.teamcode.Vision.LiveMineralTracking

import com.qualcomm.robotcore.hardware.HardwareMap
import com.vuforia.CameraDevice
import org.firstinspires.ftc.robotcore.external.ClassFactory
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer

/**
 * Created by David Lukens on 10/31/2018. Edited by Ryan Driemeyer
 */

class MasterVision(private val parameters: VuforiaLocalizer.Parameters, val hMap: HardwareMap) : Thread() {
    var vuforiaLocalizer: VuforiaLocalizer? = null
    val tfLite = TFLite(this)
    var focalLength: Double? = null


    @Throws(InterruptedException::class)
    fun init() {
        if (vuforiaLocalizer == null)
            vuforiaLocalizer = ClassFactory.getInstance().createVuforia(parameters)
        focalLength = 5.0
        tfLite.init()
    }

    @Throws(InterruptedException::class)
    fun enable() {
        init()
        tfLite.enable()
    }

    @Throws(InterruptedException::class)
    fun disable() {
        tfLite.disable()
    }

    @Throws(InterruptedException::class)
    fun shutdown() {
        disable()
        tfLite.shutdown()
    }

    override fun run() {
        try {
            while (!currentThread().isInterrupted) {
                tfLite.updateTracking()
            }
        } catch (ex: InterruptedException) {
            currentThread().interrupt()
        }
    }

    init {
        start()
    }
}