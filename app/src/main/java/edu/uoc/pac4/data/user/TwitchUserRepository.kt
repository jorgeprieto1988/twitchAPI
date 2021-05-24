package edu.uoc.pac4.data.user

import edu.uoc.pac4.data.user.datasource.UserDataSource

/**
 * Created by alex on 11/21/20.
 */

class TwitchUserRepository(
    private val userDataSource: UserDataSource

) : UserRepository {

    override suspend fun getUser(): User? {
       return userDataSource.getUserTwitch()
    }

    override suspend fun updateUser(description: String): User? {
        return userDataSource.updateUserDescription(description)
    }
}