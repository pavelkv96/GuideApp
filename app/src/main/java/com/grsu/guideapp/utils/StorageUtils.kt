package com.grsu.guideapp.utils

import android.content.Context
import java.io.File

@Suppress("unused")
class StorageUtils(private val context: Context) {

//File(Environment.getExternalStoragePublicDirectory("Android/data"), PACKAGE_NAME + "/files/content")
///storage/emulated/0/Android/data/com.grsu.guideapp/files/content

//App.getInstance().getExternalFilesDir(Settings.CONTENT_FOLDER)
///storage/emulated/0/Android/data/com.grsu.guideapp/files/content

//App.getInstance().externalCacheDir
///storage/emulated/0/Android/data/com.grsu.guideapp/cache

    fun getStorageFolder(): File? = context.getExternalFilesDir("")
}