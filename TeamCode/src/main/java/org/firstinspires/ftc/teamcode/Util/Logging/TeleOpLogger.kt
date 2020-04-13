package org.firstinspires.ftc.teamcode.Util.Logging

import android.util.ArrayMap
import org.firstinspires.ftc.robotcore.internal.system.AppUtil
import org.firstinspires.ftc.teamcode.Util.Alliance
import java.io.IOException

import org.jdom2.Attribute
import org.jdom2.Document
import org.jdom2.Element
import org.jdom2.output.Format
import org.jdom2.output.XMLOutputter
import java.io.File
import java.io.OutputStream
import java.io.Writer

class TeleOpLogger(fileName: String) {

    private val fileWriter: Writer

    open var dataPairs = ArrayMap<String, Any>()
    open var messagesList = mutableListOf<String>()

    init {
        fileWriter = AppUtil.getInstance().getSettingsFile(fileName).writer()
    }

    fun log() {

        try {
            //root element
            val rootElement = Element("TeleOp")
            val doc = Document(rootElement)

            //root element
            val logs = Element("logs")

            //data element
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

            // display ml
            xmlOutput.format = Format.getPrettyFormat()
            xmlOutput.output(doc, fileWriter)

        } catch (e: IOException) {
            e.printStackTrace()
        }
        finally {
            fileWriter.flush()
            fileWriter.close()
        }
    }
}