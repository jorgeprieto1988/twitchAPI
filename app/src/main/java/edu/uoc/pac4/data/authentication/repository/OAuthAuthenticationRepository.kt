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
        Log.w("TAG", "saving tokens")
        sessionManager.saveAccessToken(tokensResponse.accessToken)
        Log.w("TAG", "tokens are " + sessionManager.getAccessToken())
        tokensResponse.refreshToken?.let {
            sessionManager.saveRefreshToken(it)
        } ?: Log.w("TAG", "Refresh token after login")
    }

    override suspend fun logout() {
        // Clear local tokens
        sessionManager.clearAccessToken()
        sessionManager.clearRefreshToken()
    }

    override suspend fun isLoginSuccess(): Boolean {
        // Check user is available locally
        return (sessionManager.getAccessToken() != null && sessionManager.getRefreshToken() != null)
    }
}