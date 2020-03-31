package com.grsu.guideapp.adapters

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.annotation.VisibleForTesting
import com.grsu.guideapp.App
import com.grsu.guideapp.R
import com.grsu.guideapp.utils.extensions.createIfNotExists
import com.grsu.guideapp.utils.extensions.toByteArray
import com.grsu.guideapp.utils.extensions.toMD5
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Okio
import java.io.File
import java.io.FileOutputStream

class SaveAdapter(private val client: OkHttpClient, basePath: File) {

    private val folder: File = File(basePath, "content")

    @VisibleForTesting
    fun downloadImageTest(url: String) {
        if (url.isEmpty()) return /*null*/

        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        response.body()?.also { body ->
            folder.createIfNotExists()
            val file = File(folder, url.toMD5() + ".png")
            val sink = Okio.buffer(Okio.sink(file))
            body.source().use { buffer -> sink.use { output -> output.writeAll(buffer) } }
        }
    }

    @VisibleForTesting
    fun downloadImageTest1(url: String, picasso: Picasso) {
        if (url.isEmpty()) return /*null*/
        val file = File(folder, url.toMD5())
        val bitmap: Bitmap = picasso.load(url).get()
        file.createIfNotExists()
        FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.PNG, 0, it) }
    }

    @VisibleForTesting
    fun saveMarker(url: String): ByteArray {
        return try {
            val bitmap = Picasso.get().load(url).resize(59, 75).get()
            bitmap.toByteArray()
        } catch (e: Exception) {
            val drawable = App.getInstance().getDrawable(R.drawable.noicon)
            (drawable as BitmapDrawable).bitmap.toByteArray()
        }
    }
}