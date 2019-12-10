package com.grsu.guideapp.database.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.grsu.guideapp.database.Converters
import com.grsu.guideapp.database.cache.dao.CacheDao
import com.grsu.guideapp.database.cache.entities.CacheTile

@Database(entities = [CacheTile::class], version = 1, exportSchema = false)
@TypeConverters(value = [Converters::class])
abstract class CacheDataBase : RoomDatabase() {
    abstract fun cache(): CacheDao
}