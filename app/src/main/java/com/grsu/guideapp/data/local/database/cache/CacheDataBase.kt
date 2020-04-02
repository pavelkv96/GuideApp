package com.grsu.guideapp.data.local.database.cache

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.grsu.guideapp.App
import com.grsu.guideapp.data.local.database.Converters
import com.grsu.guideapp.data.local.database.cache.dao.CacheDao
import com.grsu.guideapp.data.local.database.cache.entities.CacheTile

@Database(entities = [CacheTile::class], version = CacheDataBase.version, exportSchema = false)
@TypeConverters(value = [Converters::class])
abstract class CacheDataBase : RoomDatabase() {
    abstract fun cache(): CacheDao

    companion object {
        const val version: Int = 1
        const val name: String = "map cache.db"

        @Volatile
        private var instance: CacheDataBase? = null

        fun getInstance() = instance ?: synchronized(this) { instance ?: buildDatabase().also { instance = it } }

        private fun buildDatabase(): CacheDataBase {
            return Room.databaseBuilder(App.getInstance(), CacheDataBase::class.java, name)
                .setJournalMode(JournalMode.WRITE_AHEAD_LOGGING)
                .build()
        }
    }
}