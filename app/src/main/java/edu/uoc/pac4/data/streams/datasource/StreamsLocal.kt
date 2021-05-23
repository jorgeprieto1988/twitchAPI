package edu.uoc.pac4.data.streams.datasource

import edu.uoc.pac4.data.streams.Stream

class StreamsLocal(private val streamDao: StreamDao) {

    suspend fun getStreamsLocal(): List<Stream> {
        return streamDao.getAllStreams()
    }

    suspend fun saveStream(stream: Stream) {
        streamDao.saveStream(stream)
    }

    suspend fun saveStreams(streams: List<Stream>) {
        streams.forEach { saveStream(it) }
    }

}