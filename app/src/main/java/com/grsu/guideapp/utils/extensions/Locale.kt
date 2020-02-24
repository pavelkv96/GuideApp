//@file:JvmName("Locale")
package com.grsu.guideapp.utils.extensions

import com.grsu.guideapp.utils.Language
import java.util.*

fun Locale.getCurrentLocale(): String {
    return when (this.language) {
        Language.ru.name -> Language.ru.name;
        else -> Language.en.name;
    }
}