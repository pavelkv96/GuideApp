package com.grsu.guideapp.data.local

import androidx.annotation.VisibleForTesting
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.grsu.guideapp.App
import com.grsu.guideapp.data.remote.pojo.AbstractObject
import com.grsu.guideapp.data.remote.pojo.Category

object AssetsManager {

    private val gson: Gson = GsonBuilder().setDateFormat("dd-MM-yyyy_HH:mm:ss").create()
    private val assets = App.getInstance().assets

    @VisibleForTesting
    fun parsedFromRoute(): AbstractObject {
        val json: String = assets.open("content/json/route.json").bufferedReader().use {
            it.readText()
        }

        return gson.fromJson(json, AbstractObject::class.java) ?: throw KotlinNullPointerException("JSON is null")
    }

    @VisibleForTesting
    fun parsedFromObjects(): List<AbstractObject> {
        val json: String = assets.open("content/json/objects.json").bufferedReader().use {
            it.readText()
        }

        val type = object : TypeToken<List<AbstractObject>>() {}.type
        val listPoi = gson.fromJson<List<AbstractObject>>(json, type)

        if (listPoi.isNullOrEmpty()) throw KotlinNullPointerException("JSON is null")

        return listPoi
    }

    @VisibleForTesting
    fun parsedFromTypes(): List<Category> {
        val json: String = assets.open("content/json/types.json").bufferedReader().use {
            it.readText()
        }

        val type = object : TypeToken<List<Category>>() {}.type
        val listPoi = gson.fromJson<List<Category>>(json, type)
        if (listPoi.isNullOrEmpty()) throw KotlinNullPointerException("JSON is null")
        return listPoi
    }

    @VisibleForTesting
    fun getIconFromAssets(hashUrl: String): ByteArray {
        val assets = App.getInstance().assets
        val path = "content/markers/$hashUrl"
        return assets.open(path).use { input -> ByteArray(input.available()).also { input.read(it) } }
    }

    @VisibleForTesting
    fun getImageFromAssets(type: Long): ByteArray {
        if (type == 0L) return ByteArray(0)
        val assets = App.getInstance().assets
        val path = "content/object/$type"
        return assets.open(path).use { input -> ByteArray(input.available()).also { input.read(it) } }
    }

    fun getListMarkers(): MutableSet<String>? {
        val assets = App.getInstance().assets
        val path = "content/markers"
        return assets.list(path)?.toMutableSet()
    }
}