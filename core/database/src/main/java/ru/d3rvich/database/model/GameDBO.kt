package ru.d3rvich.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.datetime.LocalDate
import ru.d3rvich.database.converters.LocalDateConverter
import ru.d3rvich.database.converters.GenresConverter
import ru.d3rvich.database.converters.ParentPlatformsConverter
import ru.d3rvich.database.converters.RatingsConverter
import ru.d3rvich.database.converters.ScreenshotsConverter
import ru.d3rvich.database.converters.StoresConverter

/**
 * Created by Ilya Deryabin at 26.03.2024
 */
@Entity(tableName = "games")
@TypeConverters(
    LocalDateConverter::class,
    GenresConverter::class,
    ParentPlatformsConverter::class,
    ScreenshotsConverter::class,
    RatingsConverter::class,
    StoresConverter::class,
)
data class GameDBO(
    @PrimaryKey @ColumnInfo("id") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "screenshot_count") val screenshotCount: Int,
    @ColumnInfo(name = "screenshots") val screenshots: List<String>,
    @ColumnInfo(name = "released") val released: LocalDate?,
    @ColumnInfo(name = "adding_time") val addingTime: Long,
    @ColumnInfo(name = "metacritic") val metacritic: Int?,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "genres") val genres: List<GenreDBO>?,
    @ColumnInfo(name = "rating") val rating: Float?,
    @ColumnInfo(name = "ratings") val ratings: List<RatingDBO>?,
    @ColumnInfo(name = "parent_platforms") val parentPlatforms: List<ParentPlatformDBO>?,
    @ColumnInfo(name = "stores") val stores: List<StoreDBO>,
)