package edu.uoc.pac4.data.authentication.datasource

import android.util.Log
import edu.uoc.pac4.data.util.Endpoints
import edu.uoc.pac4.data.util.OAuthConstants
import edu.uoc.pac4.data.authentication.model.OAuthTokensResponse
import io.ktor.client.*
import io.ktor.client.request.*

/**
 * Created by alex on 4/24/21.
 */
class TwitchAuthenticationService(
    private val httpClient: HttpClient,
) {
    private val TAG = "TwitchAuthenticationSer"

    /// Gets Access and Refresh Tokens on Twitch
    suspend fun getTokens(authorizationCode: String): OAuthTokensResponse? {
        // Get Tokens from Twitch
        try {
            val response = httpClient.post<OAuthTokensResponse>(Endpoints.tokenUrl) {
                parameter("client_id", OAuthConstants.clientID)
                parameter("client_secret", OAuthConstants.clientSecret)
                parameter("code", authorizationCode)
                parameter("grant_type", "authorization_code")
                parameter("redirect_uri", OAuthConstants.redirectUri)
            }

            Log.w("Token", "Getting tokens Access:" + response.accessToken + " and Refresh: " + response.refreshToken)
            return response
        }  catch (t: Throwable) {
            Log.w(TAG, "Error Getting Access token", t)
            return null
        }
    }
}