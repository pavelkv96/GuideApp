package com.grsu.guideapp.database

import androidx.room.TypeConverter
import com.grsu.guideapp.utils.Provider
import com.grsu.guideapp.utils.Status
import com.grsu.guideapp.utils.TypeResource
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
        //TODO fix bug with getting provider
        return Provider.valueOf(provider)
    }

    @TypeConverter
    fun fromStatus(status: Status): Int {
        return status.value
    }

    @TypeConverter
    fun toStatus(status: Int): Status {
        return Status.search(status)
    }

    @TypeConverter
    fun fromTypeResource(resource: TypeResource): Int {
        return resource.value
    }

    @TypeConverter
    fun toTypeResource(resource: Int): TypeResource {
        return TypeResource.search(resource)
    }
}