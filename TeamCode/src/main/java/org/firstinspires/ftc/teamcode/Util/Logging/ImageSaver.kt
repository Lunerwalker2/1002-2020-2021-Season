package org.firstinspires.ftc.teamcode.Util.Logging

import android.graphics.Bitmap
import org.firstinspires.ftc.robotcore.internal.system.AppUtil
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs
import java.io.File
import java.io.OutputStream


class ImageSaver(fileName: String, val image: Bitmap) {

    constructor(fileName: String, image: Mat) : this(fileName, toBitmap(image))

    companion object {
        fun toMat(bitmap: Bitmap): Mat {
            val mat = Mat()
            Utils.bitmapToMat(bitmap, mat)
            return mat
        }

        fun toBitmap(mat: Mat): Bitmap{
            val bitmap = Bitmap.createBitmap(mat.rows(), mat.cols(), Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(mat, bitmap)
            return bitmap
        }

        val imageDir = File(AppUtil.FIRST_FOLDER.toString() + "/Images")
    }

    private val outputStream: OutputStream


    init {
        if (!imageDir.exists()){
            imageDir.mkdir()
        }
        outputStream = File(imageDir, "/${fileName}.png").outputStream()
    }

    fun save(): Boolean{
        val successful = image.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
        outputStream.flush()
        outputStream.close()
        return successful
    }


}