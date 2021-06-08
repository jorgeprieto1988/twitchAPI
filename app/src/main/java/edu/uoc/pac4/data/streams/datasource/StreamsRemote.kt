package edu.uoc.pac4.data.streams.datasource

import android.util.Log
import edu.uoc.pac4.data.streams.StreamsResponse
import edu.uoc.pac4.data.util.Endpoints
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class StreamsRemote(private val httpClient: HttpClient) {

    /// Gets Streams on Twitch
    suspend fun getStreamsTwitch(cursor: String? = null): StreamsResponse? {
        Log.w("Remote", "getting streams remote")
        Log.w("Remote", "Cursor in is "+ cursor)
        // TODO("Get Streams from Twitch")
        //val response : StreamsResponse
        try{
            //response = if(cursor == null) {
                //httpClient.get<StreamsResponse>("https://api.twitch.tv/helix/streams")
            //} else {
            //    httpClient.get<StreamsResponse>("https://api.twitch.tv/helix/streams"){
             //       parameter("after", cursor)}
           // }
            val response = httpClient
                    .get<StreamsResponse>(Endpoints.streamsUrl) {
                        cursor?.let { parameter("after", it) }
                    }

            Log.v("Streams", "Data: ${response.data}")
            Log.w("Remote", "Data: ${response.data}")
            Log.v("Streams", "Cursor: ${response.pagination?.cursor}")
            Log.w("Remote", "Cursor: ${response.pagination?.cursor}")
            return response
        }
        catch(e: Exception){
            Log.v("Exception", "Getting exception! ${e.toString()}")
            Log.w("Remote", "Getting exception! ${e.toString()}")


        }
        // TODO("Support Pagination")
        return null
    }
}