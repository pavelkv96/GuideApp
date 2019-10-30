@file:JvmName("Locale")
package com.grsu.guideapp.utils.extensions

import com.grsu.guideapp.project_settings.Constants.Language
import java.util.*

fun Locale.getCurrentLocale(): String {
    return when (this.language) {
        Language.ru.name -> Language.ru.name;
        else -> Language.en.name;
    }
}