package ru.d3rvich.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by Ilya Deryabin at 26.03.2024
 */
@Serializable
@Entity(tableName = "genres")
data class GenreDBO(
    @PrimaryKey @ColumnInfo(name = "id") @SerialName("id") val id: Int,
    @ColumnInfo(name = "name") @SerialName("name") val name: String,
    @ColumnInfo("image_background") @SerialName("image_url") val imageUrl: String?,
    @ColumnInfo("games_count") @SerialName("games_count") val gamesCount: Int,
)
