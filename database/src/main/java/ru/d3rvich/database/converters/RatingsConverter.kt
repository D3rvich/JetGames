package ru.d3rvich.database.converters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.d3rvich.database.model.RatingDBO

/**
 * Created by Ilya Deryabin at 27.06.2024
 */
internal class RatingsConverter {
    @TypeConverter
    fun fromJson(json: String?): List<RatingDBO>? {
        return json?.let { Json.decodeFromString<List<RatingDBO>>(it) }
    }

    @TypeConverter
    fun toJson(ratings: List<RatingDBO>?): String? {
        return ratings?.let { Json.encodeToString(it) }
    }
}