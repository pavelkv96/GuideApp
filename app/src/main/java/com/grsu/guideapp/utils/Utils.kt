package com.grsu.guideapp.utils

internal fun toRadians(angdeg: Double): Double = angdeg / 180.0 * Math.PI

enum class Provider(val value: String) {
    FORGE("mapsforge"),
    OSM("osmdroid"),
    DEFAULT("default")
}

enum class TypeResource(val value: Int) {
    UNDEFINED(0),
    PHOTO(1),
    AUDIO(2);

    companion object {
        fun search(value: Int): TypeResource {
            return values().find { it.value == value } ?: UNDEFINED
        }
    }

}

enum class Status(val value: Int) {
    UNDEFINED(0),
    NOT_DOWNLOAD(1),
    HAVE_UPDATE(2),
    DOWNLOAD(3);

    companion object {
        fun search(value: Int): Status {
            return values().find { it.value == value } ?: UNDEFINED
        }
    }
}

enum class Type(val value: Int) {
    UNDEFINED(0),
    ONLY_TEXT(1),
    ONLY_TEXT_AND_IMAGES(2),
    ALL_DATA(3);

    companion object {
        fun search(value: Int): Type {
            return values().find { it.value == value } ?: UNDEFINED
        }
    }
}

enum class Language {
    ru, en, zh, pl, lt
}