package ru.d3rvich.database.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

/**
 * Created by Ilya Deryabin at 26.03.2024
 */
internal class LocalDateConverter {
    @TypeConverter
    fun fromString(value: String?): LocalDate? = value?.run {
        LocalDate.parse(value)
    }

    @TypeConverter
    fun toString(date: LocalDate?): String? = date?.toString()
}