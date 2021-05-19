package edu.uoc.pac4.data.streams.datasource

import android.util.Log
import edu.uoc.pac4.data.streams.StreamsResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class StreamsRemote(private val httpClient: HttpClient) {

    /// Gets Streams on Twitch
    suspend fun getStreamsTwitch(cursor: String? = null): StreamsResponse? {
        // TODO("Get Streams from Twitch")
        val response : StreamsResponse
        try{
            response = if(cursor == null) {
                httpClient.get<StreamsResponse>("https://api.twitch.tv/helix/streams")
            } else {
                httpClient.get<StreamsResponse>("https://api.twitch.tv/helix/streams"){
                    parameter("after", cursor)}
            }

            Log.v("Streams", "Data: ${response.data}")
            return response
        }
        catch(e: Exception){
            Log.v("Exception", "Getting exception! ${e.toString()}")


        }
        // TODO("Support Pagination")
        return null
    }
}