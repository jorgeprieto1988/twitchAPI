package edu.uoc.pac4.ui.login.oauth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.pac4.data.authentication.repository.AuthenticationRepository
import kotlinx.coroutines.launch

class OAuthViewModel(
        private val auth_repository: AuthenticationRepository
) : ViewModel() {

    // Live Data
    val isLoginSuccess = MutableLiveData<Boolean>()

    init {
        getIsLoginSuccess()
    }

    fun login(authorizationCode : String){
        viewModelScope.launch {
            Log.w("TAG", "starting login in viewmodel")
            auth_repository.login(authorizationCode)
            Log.w("TAG", "finishin login in viewmodel")
        }
    }

    private fun getIsLoginSuccess() {
        // Get Availability from Repository and post result
        viewModelScope.launch {
            isLoginSuccess.postValue(auth_repository.isLoginSuccess())
        }
    }
}