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
            //Get streams from local
            var listStream: List<Stream> = streamsLocal.getStreamsLocal()
            Log.w("Streams", "List of liststream is " + listStream.toString())
            //Emit local streams
            var pair : Pair<String?, List<Stream>> = Pair(null,listStream)
            emit(pair)
            //Get streams from Twitch
            val streams = streamsRemote.getStreamsTwitch(cursor)
            //Save streams from Twitch
            val pagination = streams?.pagination?.cursor
            streams?.data?.let { streamsLocal.saveStreams(it) }
            //Merge streams from local and twitch
            listStream = streamsLocal.getStreamsLocal()
            Log.w("Streams", "List of liststream is $listStream")
            //Emit streams again
            pair = Pair(pagination ,listStream)
            emit(pair)
        }


    }

}