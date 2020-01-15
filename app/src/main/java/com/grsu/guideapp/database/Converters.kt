package com.grsu.guideapp.database

import androidx.room.TypeConverter
import com.grsu.guideapp.utils.Provider
import com.grsu.guideapp.utils.Status
import com.grsu.guideapp.utils.TypeResource
import java.util.*

@Suppress("unused")
class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long): Date = Date(value)

    @TypeConverter
    fun dateToTimestamp(value: Date): Long = value.time / 1000

    @TypeConverter
    fun fromProviderToString(provider: Provider): String = provider.value

    @TypeConverter
    fun fromStringToProvider(provider: String): Provider {
        //TODO fix bug with getting provider
        return Provider.valueOf(provider)
    }

    @TypeConverter
    fun fromStatus(status: Status): Int = status.value

    @TypeConverter
    fun toStatus(status: Int): Status = Status.search(status)

    @TypeConverter
    fun fromTypeResource(resource: TypeResource): Int = resource.value

    @TypeConverter
    fun toTypeResource(resource: Int): TypeResource = TypeResource.search(resource)
}