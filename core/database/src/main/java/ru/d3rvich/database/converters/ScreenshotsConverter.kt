package ru.d3rvich.database.converters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class ScreenshotsConverter {

    @TypeConverter
    fun fromJson(value: String): List<String> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun toJson(screenshots: List<String>): String {
        return Json.encodeToString(screenshots)
    }
}