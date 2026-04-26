package ru.d3rvich.database.converters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.d3rvich.database.model.ParentPlatformDBO

/**
 * Created by Ilya Deryabin at 23.04.2024
 */
internal class ParentPlatformsConverter {
    @TypeConverter
    fun fromJson(json: String?): List<ParentPlatformDBO>? {
        return json?.let { Json.decodeFromString<List<ParentPlatformDBO>>(it) }
    }

    @TypeConverter
    fun toJson(parentPlatforms: List<ParentPlatformDBO>?): String? {
        return parentPlatforms?.let { Json.encodeToString(it) }
    }
}