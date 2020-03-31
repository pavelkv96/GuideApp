package com.grsu.guideapp.project_settings

import com.grsu.guideapp.App

object Settings {

    private val DEFAULT_PATH: String = App.getInstance().getStorage().toString()
    const val CONTENT = "some/path/cache/content/"
    const val CACHE_FOLDER: String = "cache/"
    private const val THEMES_FOLDER: String = "${CACHE_FOLDER}themes/"
    const val MAPSFORGE_FOLDER: String = "${THEMES_FOLDER}mapsforge/"

    const val MAP_FILE = "KA.map"
    const val ZOOM_TABLE = "ZoomTables.data"

    private const val CONTENT_FOLDER = "content/"
    const val JSON_FOLDER = "${CONTENT_FOLDER}json/"
    const val MARKERS_FOLDER = "${CONTENT_FOLDER}markers/"
    const val OBJECT_FOLDER = "${CONTENT_FOLDER}object/"
    const val PHOTO_FOLDER = "${CONTENT_FOLDER}photo/"
}