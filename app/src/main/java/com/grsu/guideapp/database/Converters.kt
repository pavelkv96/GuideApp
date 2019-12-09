package com.grsu.guideapp.database

import androidx.room.TypeConverter
import com.grsu.guideapp.utils.Provider
import java.util.*

@Suppress("unused")
class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(value: Date): Long {
        return value.time
    }

    @TypeConverter
    fun fromProviderToString(provider: Provider): String {
        return provider.value
    }

    @TypeConverter
    fun fromStringToProvider(provider: String): Provider {
        return Provider.valueOf(provider)
    }
}