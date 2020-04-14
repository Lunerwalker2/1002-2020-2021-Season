package org.firstinspires.ftc.teamcode.Util.Logging

enum class FileType(val suffix: String) {


    TXT(".txt"),
    CSV(".csv"),
    JSON(".json"),
    XML(".xml");

    companion object {
        fun byteArray(text: String): ByteArray {
            return text.toByteArray()
        }
    }

    fun toFullFileName(relativeFileName: String): String {
        return relativeFileName + this.suffix

    }


}