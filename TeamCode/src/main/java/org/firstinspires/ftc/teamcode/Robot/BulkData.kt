package org.firstinspires.ftc.teamcode.Robot

import com.qualcomm.hardware.lynx.LynxModule
import org.firstinspires.ftc.teamcode.Util.HardwareNames
import org.openftc.revextensions2.ExpansionHubEx
import java.util.*
import java.util.function.Consumer

class BulkData(robot: Robot?) : Component(robot) {
    //public access
     val HUB_1: LynxModule
        get() {
            return HUB_EX_1.standardModule
        }

    val HUB_10: LynxModule
        get() {
            return HUB_EX_10.standardModule
        }


    private val HUB_EX_10: ExpansionHubEx
    private val HUB_EX_1: ExpansionHubEx

    //internal access
    private val modules: MutableList<LynxModule> = ArrayList(2)

    init {
        HUB_EX_1 = hardwareMap.get(ExpansionHubEx::class.java, HardwareNames.Hubs.HUB_1) //find the hubs
        HUB_EX_10 = hardwareMap.get(ExpansionHubEx::class.java, HardwareNames.Hubs.HUB_10)

        //Add to list for easy access
        modules.add(HUB_1)
        modules.add(HUB_10)
        setManual()
        clearCache()
    }


    /**
     *     Update "everything" lol. Basically just clear the cache
     */
    fun update() {
        clearCache()
    }

    /**
     * Clear the cache
     */
    private fun clearCache() {
        modules.forEach(Consumer { obj: LynxModule -> obj.clearBulkCache() })

    }

    /**
     * Set the mode as MANUAL
     */
    private fun setManual() {
        modules.forEach(Consumer { module: LynxModule -> module.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL })
    }

    /**
     * Set the mode as AUTO
     */
    fun setAuto() {
        modules.forEach(Consumer { module: LynxModule -> module.bulkCachingMode = LynxModule.BulkCachingMode.AUTO })
    }

    /**
     * Set the mode as OFF
     */
    fun setOff() {
        modules.forEach(Consumer { module: LynxModule -> module.bulkCachingMode = LynxModule.BulkCachingMode.OFF })
    }


}