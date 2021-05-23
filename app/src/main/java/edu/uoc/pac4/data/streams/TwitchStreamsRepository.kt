package edu.uoc.pac4.data.streams

import android.util.Log

import edu.uoc.pac4.data.streams.datasource.StreamsLocal
import edu.uoc.pac4.data.streams.datasource.StreamsRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by alex on 11/21/20.
 */

class TwitchStreamsRepository(
    // TODO: Add any datasources you may need
    private val streamsRemote: StreamsRemote,
    private val streamsLocal: StreamsLocal,
) : StreamsRepository {

    override suspend fun getStreams(cursor: String?): Flow<Pair<String?, List<Stream>>> {
        return flow {
            var listStream: List<Stream> = streamsLocal.getStreamsLocal()
            Log.w("Streams", "List of liststream is " + listStream.toString())
            var pair : Pair<String?, List<Stream>> = Pair("pair",listStream)
            emit(pair)
            val streams = streamsRemote.getStreamsTwitch(cursor)
            streams?.data?.let { streamsLocal.saveStreams(it) }
            listStream += streamsLocal.getStreamsLocal()
            Log.w("Streams", "List of liststream is $listStream")
            pair = Pair("pair",listStream)
            emit(pair)
        }


    }

}