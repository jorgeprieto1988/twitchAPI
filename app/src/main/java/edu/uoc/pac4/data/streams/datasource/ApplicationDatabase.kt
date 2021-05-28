package edu.uoc.pac4.data.streams.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.uoc.pac4.data.streams.Stream

/**
 * Room Application Database
 */
@Database(entities = [Stream::class], version = 1)
abstract class ApplicationDatabase : RoomDatabase(), StreamDao {
    abstract fun streamDao(): StreamDao


}