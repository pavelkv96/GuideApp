package com.grsu.guideapp.database.content.dao

import androidx.room.Dao
import androidx.room.Query
import com.grsu.guideapp.database.content.entities.Poi

@Dao
abstract class PoiDao : BaseDao<Poi>() {

    @Query("SELECT * FROM poi")
    abstract fun getAll(): List<Poi>
}