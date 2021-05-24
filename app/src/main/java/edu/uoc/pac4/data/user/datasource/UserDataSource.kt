package edu.uoc.pac4.data.user.datasource

import android.util.Log
import edu.uoc.pac4.data.streams.StreamsResponse
import edu.uoc.pac4.data.user.User
import edu.uoc.pac4.data.user.UsersResponse
import edu.uoc.pac4.data.util.OAuthException
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import kotlin.jvm.Throws

class UserDataSource (private val httpClient: HttpClient){
    private val TAG = "UserDataSource"
    @Throws(OAuthException.Unauthorized::class)
    suspend fun getUserTwitch(): User? {
        try {
            val response = httpClient.get<UsersResponse>("https://api.twitch.tv/helix/users")

            return response.data?.firstOrNull()
        } catch (t: Throwable) {
            Log.w(TAG, "Error getting user", t)
            // Try to handle error
            return when (t) {
                is ClientRequestException -> {
                    // Check if it's a 401 Unauthorized
                    if (t.response?.status?.value == 401) {
                        throw OAuthException.Unauthorized
                    }
                    null
                }
                else -> null
            }
        }
    }

    @Throws(OAuthException.Unauthorized::class)
    suspend fun updateUserDescription(description: String): User? {
        try {
            val response = httpClient.put<UsersResponse>("https://api.twitch.tv/helix/users") {
                        parameter("description", description)
                    }

            return response.data?.firstOrNull()
        } catch (t: Throwable) {
            Log.w(TAG, "Error updating user description", t)
            // Try to handle error
            return when (t) {
                is ClientRequestException -> {
                    // Check if it's a 401 Unauthorized
                    if (t.response?.status?.value == 401) {
                        throw OAuthException.Unauthorized
                    }
                    null
                }
                else -> null
            }
        }
    }

}