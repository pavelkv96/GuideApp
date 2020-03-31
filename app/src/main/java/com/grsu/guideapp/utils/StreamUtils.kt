package com.grsu.guideapp.utils

import java.io.FileOutputStream
import java.io.InputStream

@Suppress("unused")
class StreamUtils {

    companion object {
        fun copyInputStreamToFile(inputStream: InputStream, outputStream: FileOutputStream) {
            val buffer = ByteArray(8192)
            inputStream.use { input ->
                outputStream.use { fileOut ->
                    while (true) {
                        val length = input.read(buffer)
                        if (length <= 0) break
                        fileOut.write(buffer, 0, length)
                    }
                }
            }
        }
    }
}