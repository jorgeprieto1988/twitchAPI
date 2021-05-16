package edu.uoc.pac4.data.streams

import kotlinx.coroutines.flow.Flow

/**
 * Created by alex on 11/21/20.
 */

class TwitchStreamsRepository(
    // TODO: Add any datasources you may need
) : StreamsRepository {

    override suspend fun getStreams(cursor: String?): Flow<Pair<String?, List<Stream>>> {
        TODO("Not yet implemented")
    }

}