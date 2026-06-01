package ru.d3rvich.core.database.converters

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import ru.d3rvich.core.database.model.StoreDBO

internal class StoresConverter {
    @TypeConverter
    fun fromJson(json: String?): List<StoreDBO> {
        return json?.let { Json.decodeFromString<List<StoreDBO>>(it) }!!
    }

    @TypeConverter
    fun toJson(parentPlatforms: List<StoreDBO>?): String? {
        return parentPlatforms?.let { Json.encodeToString(it) }
    }
}
