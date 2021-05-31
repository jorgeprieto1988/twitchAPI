package edu.uoc.pac4.data.di

import androidx.room.Dao
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.uoc.pac4.data.authentication.datasource.SessionManager
import edu.uoc.pac4.data.util.Network
import edu.uoc.pac4.data.util.OAuthConstants
import edu.uoc.pac4.data.authentication.datasource.TwitchAuthenticationService
import edu.uoc.pac4.data.authentication.repository.AuthenticationRepository
import edu.uoc.pac4.data.authentication.repository.OAuthAuthenticationRepository
import edu.uoc.pac4.data.streams.StreamsRepository
import edu.uoc.pac4.data.streams.TwitchStreamsRepository
import edu.uoc.pac4.data.streams.datasource.*
import edu.uoc.pac4.data.user.TwitchUserRepository
import edu.uoc.pac4.data.user.UserRepository
import edu.uoc.pac4.data.user.datasource.UserDataSource
import io.ktor.client.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.parameter.DefinitionParameters
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

    single< StreamDao> {
        Room.databaseBuilder(androidContext(),
                ApplicationDatabase::class.java, "app_database").build().streamDao()
    }

    // Data Sources
    single<SessionManager> { SessionManager(androidContext()) }
    single<TwitchAuthenticationService> { TwitchAuthenticationService(httpClient = get()) }
    single<UserDataSource> { UserDataSource(httpClient = get()) }
    single<StreamsRemote> { StreamsRemote(httpClient = get()) }
    single<StreamsLocal> { StreamsLocal(streamDao = get()) }

    // Repositories
    single<AuthenticationRepository> {
        OAuthAuthenticationRepository(
            sessionManager = get(),
            twitchAuthenticationService = get(),
        )
    }

    single<UserRepository> {
        TwitchUserRepository(
                userDataSource = get()
        )
    }

    single<StreamsRepository> {
        TwitchStreamsRepository(
                streamsRemote = get(),
                streamsLocal = get()
        )
    }
}