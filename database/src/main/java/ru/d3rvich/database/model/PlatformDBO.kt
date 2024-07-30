package ru.d3rvich.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Ilya Deryabin at 25.03.2024
 */
@Entity(tableName = "platforms")
data class PlatformDBO(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "games_count") val gamesCount: Int,
)