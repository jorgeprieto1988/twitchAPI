package edu.uoc.pac4.data.authentication.repository

/**
 * Created by alex on 12/09/2020.
 */

interface AuthenticationRepository {

    /// Returns true if a user exists. False otherwise
    suspend fun isUserAvailable(): Boolean

    /// Returns true if the user logged in successfully. False otherwise
    suspend fun login(authorizationCode: String)

    /// Log out the current user
    suspend fun logout()

}