package edu.uoc.pac4.data.di

import edu.uoc.pac4.data.authentication.datasource.SessionManager
import edu.uoc.pac4.data.util.Network
import edu.uoc.pac4.data.util.OAuthConstants
import edu.uoc.pac4.data.authentication.datasource.TwitchAuthenticationService
import edu.uoc.pac4.data.authentication.repository.AuthenticationRepository
import edu.uoc.pac4.data.authentication.repository.OAuthAuthenticationRepository
import io.ktor.client.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Created by alex on 11/21/20.
 */

val dataModule = module {

    // TODO: Init your other Data Dependencies

    // Utils
    single<HttpClient> {
        Network.createHttpClient(
            androidContext(),
            OAuthConstants.clientID,
            OAuthConstants.clientSecret
        )
    }

    // Data Sources
    single<SessionManager> { SessionManager(androidContext()) }
    single<TwitchAuthenticationService> { TwitchAuthenticationService(httpClient = get()) }

    // Repositories
    single<AuthenticationRepository> {
        OAuthAuthenticationRepository(
            sessionManager = get(),
            twitchAuthenticationService = get(),
        )
    }
}