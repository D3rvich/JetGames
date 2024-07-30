package ru.d3rvich.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.d3rvich.database.model.PlatformDBO

/**
 * Created by Ilya Deryabin at 25.03.2024
 */
@Dao
interface PlatformsDao {

    @Query("SELECT * FROM platforms")
    suspend fun platforms(): List<PlatformDBO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(platforms: List<PlatformDBO>)

    @Delete
    suspend fun delete(platformDao: PlatformDBO)
}