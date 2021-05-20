package edu.uoc.pac4.data.streams

import edu.uoc.pac4.data.authentication.datasource.SessionManager
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
        val streamsFlow: Flow<List<Stream>> = flow {
            val listStream: MutableList<Stream> = streamsLocal.getStreamsLocal().toMutableList()
            emit(listStream)
            val streams = streamsRemote.getStreamsTwitch(cursor)
            streams?.data?.let { streamsLocal.saveStreams(it) }
            listStream += streamsLocal.getStreamsLocal()
            emit(listStream)
        }
        TODO("Not yet implemented")
    }

}