package com.grsu.guideapp.data.local.database

import androidx.room.TypeConverter
import com.grsu.guideapp.map.Provider
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
    fun fromProviderToString(provider: Provider): String = provider.name

    @TypeConverter
    fun fromStringToProvider(provider: String): Provider = Provider.valueOf(provider)

    @TypeConverter
    fun fromStatus(status: Status): Int = status.value

    @TypeConverter
    fun toStatus(status: Int): Status = Status.search(status)

    @TypeConverter
    fun fromTypeResource(resource: TypeResource): Int = resource.value

    @TypeConverter
    fun toTypeResource(resource: Int): TypeResource = TypeResource.search(resource)
}