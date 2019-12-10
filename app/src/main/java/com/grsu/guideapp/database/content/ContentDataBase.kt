package com.grsu.guideapp.database.content

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.grsu.guideapp.database.Converters
import com.grsu.guideapp.database.content.dao.RoutesDao
import com.grsu.guideapp.database.content.entities.*

@Database(
    entities = [
        Lines::class, ListLines::class, ListPoi::class, Marker::class, Poi::class,
        PoiLanguage::class, References::class, ReferencesList::class, RouteLanguage::class,
        Routes::class, Types::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(value = [Converters::class])
abstract class ContentDataBase : RoomDatabase() {

    abstract fun routesDao(): RoutesDao
}