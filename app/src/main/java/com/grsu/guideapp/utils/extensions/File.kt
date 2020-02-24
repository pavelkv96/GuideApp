package com.grsu.guideapp.utils.extensions

import java.io.File

fun File.createIfNotExists() {
    if (!exists()) { mkdirs() }
}