package ru.d3rvich.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.d3rvich.database.model.GameDBO

/**
 * Created by Ilya Deryabin at 26.03.2024
 */
@Dao
interface GamesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(game: GameDBO)

    @Query("SELECT * FROM games ORDER BY adding_time DESC")
    fun games(): PagingSource<Int, GameDBO>

    @Query("SELECT * FROM games WHERE name LIKE :query")
    fun search(query: String): PagingSource<Int, GameDBO>

    @Query("SELECT * FROM games WHERE id LIKE :gameId")
    suspend fun gameDetail(gameId: Int): GameDBO?

    @Query("SELECT EXISTS(SELECT * FROM games WHERE id LIKE :gameId)")
    suspend fun isGameExist(gameId: Int): Boolean

    @Delete
    suspend fun delete(game: GameDBO)
}