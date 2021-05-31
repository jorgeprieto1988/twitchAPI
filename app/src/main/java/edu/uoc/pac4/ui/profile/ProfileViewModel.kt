package edu.uoc.pac4.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.pac4.data.authentication.repository.AuthenticationRepository
import edu.uoc.pac4.data.user.TwitchUserRepository
import edu.uoc.pac4.data.user.User
import edu.uoc.pac4.data.user.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
        private val user_repository: UserRepository,
        private val auth_repository: AuthenticationRepository
) : ViewModel() {

    // Live Data
    private val user = MutableLiveData<User>()

    init{
        getUserTwitch()
        //viewModelScope.launch {
        //    repository.getUser()?.description?.let { updateUserTwitch(it) }
       // }
    }

    fun getSavedUser() = user

    private fun getUserTwitch(){

        viewModelScope.launch{
            user.postValue(user_repository.getUser())
        }
    }

    fun updateUserTwitch(description: String){
        viewModelScope.launch{
            user.postValue(user_repository.updateUser(description))
        }
    }

    fun userLogOut(){
        viewModelScope.launch {
            auth_repository.logout()
        }
    }

}