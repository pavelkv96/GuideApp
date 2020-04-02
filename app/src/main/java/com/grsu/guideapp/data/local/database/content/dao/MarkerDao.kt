package com.grsu.guideapp.data.local.database.content.dao

import androidx.room.Dao
import androidx.room.Query
import com.grsu.guideapp.data.local.database.content.entities.Marker

@Dao
abstract class MarkerDao : BaseDao<Marker> {

    @Query("SELECT * FROM marker")
    abstract fun getAll():List<Marker>
}