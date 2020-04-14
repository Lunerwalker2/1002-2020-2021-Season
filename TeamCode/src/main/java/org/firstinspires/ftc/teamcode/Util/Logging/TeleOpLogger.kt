package org.firstinspires.ftc.teamcode.Util.Logging

import android.util.ArrayMap
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonWriter
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson
import org.firstinspires.ftc.robotcore.internal.system.AppUtil
import org.firstinspires.ftc.teamcode.Util.Alliance
import org.firstinspires.ftc.teamcode.Util.Math.MathThings

import org.jdom2.Attribute
import org.jdom2.Document
import org.jdom2.Element
import org.jdom2.output.Format
import org.jdom2.output.XMLOutputter
import java.io.*
import java.lang.reflect.Type

data class JSONClass(val data: String, val messages: String)

class TeleOpLogger(fileName: String) {


    companion object {
        //We make our own GSON object here for pretty printing
        val gson = GsonBuilder().setPrettyPrinting().create()
    }

    private val fileWriter: OutputStream

    //Data
    var dataPairs = ArrayMap<String, Any>()
    //Messages
    var messagesList = mutableListOf<String>()

    init {
        //Get the OutputStream for the file
        fileWriter = AppUtil.getInstance().getSettingsFile(fileName).outputStream()
    }

    /**
     * Assign the log type
     */
    fun log(type: FileType) {
            when(type){
                FileType.XML -> logXML()
                FileType.TXT, FileType.CSV -> logTXT() //For now at least
                FileType.JSON -> logJSON()
            }
        fileWriter.flush()
        fileWriter.close()


    }

    /**
     * Log in the JSON format
     */
    //THIS IS VERY SCUFFED JSON!!!!!
    fun logJSON(){
        val data = gson.toJson(dataPairs, MathThings.getArrayMapType<String, Any>())
        val messages = gson.toJson(messagesList, TypeToken.get(messagesList.javaClass).type)
        val jsonClass = JSONClass(data, messages)
        fileWriter.write(FileType.byteArray(gson.toJson(jsonClass)))
    }

    /**
     * Log in the basic text format (also recommended behind XML)
     */
    fun logTXT() {
        fileWriter.write(FileType.byteArray("TeleOp Logs \n"))
        fileWriter.write(FileType.byteArray("\n"))
        fileWriter.write(FileType.byteArray("\n"))
        fileWriter.write(FileType.byteArray("Data: \n"))
        fileWriter.write(FileType.byteArray("\n"))
        dataPairs.iterator().forEach {
            fileWriter.write(FileType.byteArray(it.key + ": " + it.value.toString() + "\n"))
        }
        fileWriter.write(FileType.byteArray("\n"))
        fileWriter.write(FileType.byteArray("Messages: \n"))
        fileWriter.write(FileType.byteArray("\n"))
        messagesList.forEach {
            fileWriter.write(FileType.byteArray(it + "\n"))
        }
    }


    /**
     * Log in the XML format (recommended, most readable)
     */
    fun logXML() {
        try {
            val rootElement = Element("TeleOp")
            val doc = Document(rootElement)

            val logs = Element("logs")

            val data = Element("data")
            data.setAttribute(Attribute ("length", dataPairs.size.toString()))

            dataPairs.iterator().forEach {
                val dataEntry = Element("dataEntry")
                dataEntry.setAttribute("Name", it.key)
                dataEntry.text = it.value.toString()
                data.addContent(dataEntry)
            }

            //messages element
            val messages = Element("messages")
            messages.setAttribute(Attribute ("length", messagesList.size.toString()))

            messagesList.forEach {
                val messageEntry = Element("message")
                messageEntry.text = it
                messages.addContent(messageEntry)
            }

            logs.addContent(data)
            logs.addContent(messages)

            doc.rootElement.addContent(logs)

            val xmlOutput = XMLOutputter()
            xmlOutput.format = Format.getPrettyFormat()
            xmlOutput.output(doc, fileWriter)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}