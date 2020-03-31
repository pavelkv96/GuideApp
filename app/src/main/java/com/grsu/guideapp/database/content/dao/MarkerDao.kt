package com.grsu.guideapp.database.content.dao

import androidx.room.Dao
import androidx.room.Query
import com.grsu.guideapp.database.content.entities.Marker

@Dao
abstract class MarkerDao : BaseDao<Marker> {

    @Query("SELECT * FROM marker")
    abstract fun getAll():List<Marker>
}