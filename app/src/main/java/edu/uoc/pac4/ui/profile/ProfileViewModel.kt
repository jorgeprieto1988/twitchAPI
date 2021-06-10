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
    private val isLogOut = MutableLiveData<Boolean>()
    private val isTokenCleared = MutableLiveData<Boolean>()

    init{
        getUserTwitch(null)
        //viewModelScope.launch {
        //    repository.getUser()?.description?.let { updateUserTwitch(it) }
       // }
        isLogOut.postValue(false)
        isTokenCleared.postValue(false)
    }

    fun getSavedUser() = user
    fun getIsLogOut() = isLogOut
    fun getIsTokenCleared() = isTokenCleared

    private fun getUserTwitch(description: String?){
        if(description == null) {
            viewModelScope.launch {
                user.postValue(user_repository.getUser())
            }
        }
        else
        {
            viewModelScope.launch{
                user.postValue(user_repository.updateUser(description))
            }
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
            isLogOut.postValue(true)
        }
    }

    fun clearAccessToken(){
        viewModelScope.launch {
            auth_repository.clearAccessToken()
            isTokenCleared.postValue(true)
        }
    }

}