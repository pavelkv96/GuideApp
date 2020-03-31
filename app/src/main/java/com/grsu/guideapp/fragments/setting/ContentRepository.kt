package com.grsu.guideapp.fragments.setting

import com.grsu.guideapp.database.content.ContentDataBase

object ContentRepository {

    private val dataBase = ContentDataBase.getInstance()
    fun clearAllTables() {
        dataBase.clearAllTables()
    }
}