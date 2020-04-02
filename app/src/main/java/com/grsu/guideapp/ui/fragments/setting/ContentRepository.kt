package com.grsu.guideapp.ui.fragments.setting

import com.grsu.guideapp.data.local.database.content.ContentDataBase

object ContentRepository {

    private val dataBase = ContentDataBase.getInstance()
    fun clearAllTables() {
        dataBase.clearAllTables()
    }
}