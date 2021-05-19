package edu.uoc.pac4.data.streams

import edu.uoc.pac4.data.authentication.datasource.SessionManager
import edu.uoc.pac4.data.streams.datasource.StreamsLocal
import edu.uoc.pac4.data.streams.datasource.StreamsRemote
import kotlinx.coroutines.flow.Flow

/**
 * Created by alex on 11/21/20.
 */

class TwitchStreamsRepository(
    // TODO: Add any datasources you may need
    private val sessionManager: SessionManager,
    private val streamsRemote: StreamsRemote,
    private val streamsLocal: StreamsLocal,
) : StreamsRepository {

    override suspend fun getStreams(cursor: String?): Flow<Pair<String?, List<Stream>>> {
        streamsLocal.getStreamsLocal()
        streamsRemote.getStreamsTwitch(cursor)
        streamsLocal.saveStreams()
        TODO("Not yet implemented")
    }

}