package com.grsu.guideapp.database.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.grsu.guideapp.database.cache.entities.CacheTile
import com.grsu.guideapp.map.Provider

@Dao
interface CacheDao {

    @Query("SELECT tile FROM tiles WHERE key_tile = :index AND style = :tileStyle limit 1")
    fun getTile(index: Long, tileStyle: Provider): ByteArray?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveTile(data: CacheTile)
}