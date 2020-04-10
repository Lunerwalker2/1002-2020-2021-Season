package org.firstinspires.ftc.teamcode.Util

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.canvas.Canvas
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import org.firstinspires.ftc.robotcore.external.Func
import org.firstinspires.ftc.robotcore.external.Telemetry

class CombinedPacket(val telemetry: Telemetry, val packet: TelemetryPacket) {
    companion object{
        val dashboard: FtcDashboard = FtcDashboard.getInstance()
    }


    fun put(caption: String, data: Any) {
        telemetry.addData(caption, data)
        packet.put(caption, data)
    }

    fun addLine(line: String) {
        telemetry.addLine(line)
        packet.addLine(line)
    }

    fun<T> addTelemtryAction(caption:String, action: Func<T>) {
        telemetry.addData(caption, action)
    }

    fun getFieldOverlay(): Canvas {
        return packet.fieldOverlay()
    }

    fun telemetrySpeak(text: String){
        telemetry.speak(text)
    }

    fun update(){
        telemetry.update()
        dashboard.sendTelemetryPacket(packet)
    }

}