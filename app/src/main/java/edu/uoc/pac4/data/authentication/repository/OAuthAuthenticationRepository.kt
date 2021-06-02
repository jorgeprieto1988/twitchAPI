package edu.uoc.pac4.data.authentication.repository

import android.util.Log
import edu.uoc.pac4.data.authentication.datasource.SessionManager
import edu.uoc.pac4.data.authentication.datasource.TwitchAuthenticationService

/**
 * Created by alex on 11/21/20.
 */
class OAuthAuthenticationRepository(
    // DataSources are always injected in the constructors
    private val sessionManager: SessionManager,
    private val twitchAuthenticationService: TwitchAuthenticationService,
) : AuthenticationRepository {

    private val TAG = "OAuthAuthenticationRepository"

    override suspend fun isUserAvailable(): Boolean {
        // Check user is available locally
        return sessionManager.isUserAvailable()
    }

    override suspend fun login(authorizationCode: String) {
        // Authenticate using *TwitchAuthenticationService*
        val tokensResponse = twitchAuthenticationService.getTokens(authorizationCode)
        // Save tokens using *SessionManager*
        if (tokensResponse != null) {
            sessionManager.saveAccessToken(tokensResponse.accessToken)
        }
        if (tokensResponse != null) {
            tokensResponse.refreshToken?.let {
                sessionManager.saveRefreshToken(it)
            } ?: Log.w("Token", "Refresh token after login")
        }
    }

    override suspend fun logout() {
        // Clear local tokens
        sessionManager.clearAccessToken()
        sessionManager.clearRefreshToken()
    }
}