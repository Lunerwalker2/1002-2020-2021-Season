package org.firstinspires.ftc.teamcode.Robot

import com.qualcomm.hardware.lynx.LynxModule
import org.firstinspires.ftc.teamcode.Util.HardwareNames
import org.openftc.revextensions2.ExpansionHubEx
import java.util.*
import java.util.function.Consumer

class BulkData(robot: Robot?) : Component(robot) {
    //public access
    var HUB_1: LynxModule
    var HUB_10: LynxModule

    //internal access
    private val modules: MutableList<LynxModule> = ArrayList(2)

    init {
        HUB_1 = hardwareMap.get(ExpansionHubEx::class.java, HardwareNames.Hubs.HUB_1).standardModule
        HUB_10 = hardwareMap.get(ExpansionHubEx::class.java, HardwareNames.Hubs.HUB_10).standardModule
        modules.add(HUB_1)
        modules.add(HUB_10)
        setManual()
        clearCache()
    }


    fun update() {
        clearCache()
    }

    private fun clearCache() {
        modules.forEach(Consumer { obj: LynxModule -> obj.clearBulkCache() })
    }

    private fun setManual() {
        modules.forEach(Consumer { module: LynxModule -> module.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL })
    }

    fun setAuto() {
        modules.forEach(Consumer { module: LynxModule -> module.bulkCachingMode = LynxModule.BulkCachingMode.AUTO })
    }

    fun setOff() {
        modules.forEach(Consumer { module: LynxModule -> module.bulkCachingMode = LynxModule.BulkCachingMode.OFF })
    }


}