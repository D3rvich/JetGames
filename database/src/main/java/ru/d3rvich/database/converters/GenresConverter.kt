package ru.d3rvich.database.converters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.d3rvich.database.model.GenreDBO

/**
 * Created by Ilya Deryabin at 26.03.2024
 */
internal class GenresConverter {

    @TypeConverter
    fun fromJson(json: String?): List<GenreDBO>? {
        return json?.let { Json.decodeFromString<List<GenreDBO>>(it) }
    }

    @TypeConverter
    fun genresToJson(genres: List<GenreDBO>?): String? {
        return genres?.let { Json.encodeToString(it) }
    }
}