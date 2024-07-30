package ru.d3rvich.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.d3rvich.database.dao.GamesDao
import ru.d3rvich.database.dao.GenresDao
import ru.d3rvich.database.dao.PlatformsDao
import ru.d3rvich.database.model.GameDBO
import ru.d3rvich.database.model.GenreDBO
import ru.d3rvich.database.model.PlatformDBO

/**
 * Created by Ilya Deryabin at 25.03.2024
 */
class JetGamesDatabase internal constructor(private val database: JetGamesRoomDatabase) {

    val platformsDao
        get() = database.platformsDao()

    val gamesDao
        get() = database.gamesDao()

    val genresDao
        get() = database.genresDao()
}

@Database(entities = [PlatformDBO::class, GameDBO::class, GenreDBO::class], version = 1)
internal abstract class JetGamesRoomDatabase : RoomDatabase() {
    abstract fun platformsDao(): PlatformsDao

    abstract fun gamesDao(): GamesDao

    abstract fun genresDao(): GenresDao
}

fun JetGamesDatabase(applicationContext: Context): JetGamesDatabase {
    val database = Room.databaseBuilder(
        context = checkNotNull(applicationContext.applicationContext),
        klass = JetGamesRoomDatabase::class.java,
        name = DatabaseName
    ).build()
    return JetGamesDatabase(database)
}

private const val DatabaseName = "jet_games"