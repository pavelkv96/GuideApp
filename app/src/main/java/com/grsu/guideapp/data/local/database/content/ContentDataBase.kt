package com.grsu.guideapp.data.local.database.content

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.grsu.guideapp.App
import com.grsu.guideapp.data.local.database.Converters
import com.grsu.guideapp.data.local.database.content.dao.*
import com.grsu.guideapp.data.local.database.content.entities.*

@Database(
    entities = [
        Lines::class, ListLines::class, ListPoi::class, Marker::class, Poi::class,
        PoiLanguage::class, References::class, ReferencesList::class, RouteLanguage::class,
        Routes::class, Types::class
    ],
    version = ContentDataBase.version,
    exportSchema = false
)
@TypeConverters(value = [Converters::class])
abstract class ContentDataBase : RoomDatabase() {

    companion object {
        const val version: Int = 1
        const val name: String = "content.db"

        @Volatile
        private var instance: ContentDataBase? = null

        fun getInstance() = instance ?: synchronized(this) { instance ?: buildDatabase().also { instance = it } }

        private fun buildDatabase(): ContentDataBase {
            return Room.databaseBuilder(App.getInstance(), ContentDataBase::class.java, name).build()
        }
    }

    abstract fun linesDao(): LinesDao
    abstract fun listLinesDao(): ListLinesDao
    abstract fun listPoiDao(): ListPoiDao
    abstract fun markerDao(): MarkerDao
    abstract fun poiDao(): PoiDao
    abstract fun poiLanguageDao(): PoiLanguageDao
    abstract fun referencesDao(): ReferencesDao
    abstract fun referencesListDao(): ReferencesListDao
    abstract fun routeLanguageDao(): RouteLanguageDao
    abstract fun routesDao(): RoutesDao
    abstract fun typesDao(): TypesDao
}