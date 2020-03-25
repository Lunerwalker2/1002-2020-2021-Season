package org.firstinspires.ftc.teamcode.Vision.LiveMineralTracking

import org.firstinspires.ftc.robotcore.external.ClassFactory
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector

/**
 * Created by David Lukens on 10/31/2018. Edited by Ryan Driemeyer
 */

class TFLite(private val master: MasterVision) {
    companion object {
        private const val TFOD_MODEL_ASSET = "RoverRuckus.tflite"
        private const val LABEL_GOLD_MINERAL = "Gold Mineral"
        private const val LABEL_SILVER_MINERAL = "Silver Mineral"
    }


    var mineralRecognitions = ArrayList<Mineral>()




    val exampleField: Int = 4

    private var tfod: TFObjectDetector? = null
    private val tfodMoniterViewId = master.hMap.appContext.resources.getIdentifier("tfodMonitorViewId", "id", master.hMap.appContext.packageName)
    private val parameters = TFObjectDetector.Parameters(tfodMoniterViewId)

    @Throws(InterruptedException::class)
    fun init() {
        if (tfod == null) {
            parameters.minimumConfidence = 0.5
            tfod = ClassFactory.getInstance().createTFObjectDetector(parameters, master.vuforiaLocalizer)
            tfod?.loadModelFromAsset(TFOD_MODEL_ASSET, Mineral.MineralType.GOLD.label, Mineral.MineralType.SILVER.label)
        }
    }


    @Throws(InterruptedException::class)
    internal fun updateTracking() {
        mineralRecognitions.clear()
        if (tfod != null) {
            val updatedRecognitions = tfod?.updatedRecognitions
            if (updatedRecognitions != null) {
                updatedRecognitions.forEach {
                    when(it.label){
                        Mineral.MineralType.SILVER.label -> {
                            mineralRecognitions.add(Mineral(Mineral.MineralType.SILVER, it))
                        }
                        Mineral.MineralType.GOLD.label -> {
                            mineralRecognitions.add(Mineral(Mineral.MineralType.GOLD, it))
                        }
                    }
                }
            }
        }
    }


    @Throws(InterruptedException::class)
    fun enable() {
        tfod?.activate()
    }

    @Throws(InterruptedException::class)
    fun disable() {
        tfod?.deactivate()
    }

    @Throws(InterruptedException::class)
    fun shutdown() {
        tfod?.shutdown()
    }

}