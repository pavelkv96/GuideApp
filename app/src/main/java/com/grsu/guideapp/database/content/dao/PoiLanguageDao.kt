package com.grsu.guideapp.database.content.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.grsu.guideapp.database.content.entities.PoiLanguage

@Dao
interface PoiLanguageDao : BaseDao<PoiLanguage> {

    @Query("SELECT * FROM poi_language")
    fun getAll(): List<PoiLanguage>

    @Transaction
    fun insertOrUpdate(obj: PoiLanguage) {
        if (insert(obj) == -1L) update(obj)
    }

    @Transaction
    fun insertOrUpdate(objList: List<PoiLanguage>) {
        insert(objList)
            .mapIndexed { index, l -> if (l == -1L) objList[index] else null }
            .filterNotNull()
            .also { if (it.isNotEmpty()) update(it) }
    }
}