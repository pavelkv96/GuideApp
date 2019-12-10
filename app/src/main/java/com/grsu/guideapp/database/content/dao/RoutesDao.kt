package com.grsu.guideapp.database.content.dao

import androidx.room.Dao
import androidx.room.Query
import com.grsu.guideapp.database.content.entities.Routes

@Dao
interface RoutesDao {

    @Query("SELECT * FROM routes")
    fun getRoutes(): List<Routes>
}