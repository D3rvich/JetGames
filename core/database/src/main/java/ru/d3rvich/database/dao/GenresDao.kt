package ru.d3rvich.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.d3rvich.database.model.GenreDBO

/**
 * Created by Ilya Deryabin at 26.03.2024
 */
@Dao
interface GenresDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(genres: List<GenreDBO>)

    @Query("SELECT * FROM genres")
    fun genres(): List<GenreDBO>

    @Delete
    suspend fun delete(genre: GenreDBO)
}