package com.grsu.guideapp.utils

internal inline fun toRadians(angdeg: Double): Double = angdeg / 180.0 * Math.PI

enum class Provider(val value: String) {
    FORGE("mapsforge"),
    OSM("osmdroid"),
    DEFAULT("default")
}