package com.grsu.guideapp.data.local.database.vo

import androidx.room.ColumnInfo
import com.grsu.guideapp.App
import com.grsu.guideapp.R
import com.grsu.guideapp.utils.Language

class TypeItemVO(
    @ColumnInfo(name = "id_type") val id: Long,
    @ColumnInfo(name = "language_ru") private val language_ru: String,
    @ColumnInfo(name = "language_en") private val language_en: String,
    @ColumnInfo(name = "language_cn") private val language_cn: String,
    @ColumnInfo(name = "language_pl") private val language_pl: String,
    @ColumnInfo(name = "language_lt") private val language_lt: String,
    @ColumnInfo(name = "picture") val image: ByteArray
) {
    val name: String get() = when (App.getInstance().getString(R.string.locale)) {
        Language.ru.name -> language_ru
        else -> language_en
    }
}