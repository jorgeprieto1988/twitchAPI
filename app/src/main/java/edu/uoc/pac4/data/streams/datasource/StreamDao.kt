package edu.uoc.pac4.data.streams.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.uoc.pac4.data.streams.Stream

@Dao
interface StreamDao {
    @Query("SELECT * FROM stream_entity")
    fun getAllStreams(): List<Stream>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveStream(stream: Stream): Long
}