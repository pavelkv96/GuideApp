package com.grsu.guideapp.database.content.dao

import androidx.room.Dao
import androidx.room.Query
import com.grsu.guideapp.database.content.entities.Routes

@Dao
abstract class RoutesDao : BaseDao<Routes>(){

    @Query("SELECT * FROM routes")
    abstract fun getRoutes(): List<Routes>
}