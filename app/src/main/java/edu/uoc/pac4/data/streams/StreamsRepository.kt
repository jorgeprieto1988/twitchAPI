package edu.uoc.pac4.data.streams

import edu.uoc.pac4.data.util.OAuthException.Unauthorized
import kotlinx.coroutines.flow.Flow

/**
 * Created by alex on 12/09/2020.
 */

interface StreamsRepository {
    /// Returns a Pair object containing
    /// first: (nullable) Pagination cursor
    /// second: List of Streams
    @Throws(Unauthorized::class)
    suspend fun getStreams(cursor: String? = null): Flow<Pair<String?, List<Stream>>>
}