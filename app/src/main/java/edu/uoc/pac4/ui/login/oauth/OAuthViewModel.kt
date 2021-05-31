package edu.uoc.pac4.ui.login.oauth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.pac4.data.authentication.repository.AuthenticationRepository
import kotlinx.coroutines.launch

class OAuthViewModel(
        private val auth_repository: AuthenticationRepository
) : ViewModel() {

    fun login(authorizationCode : String){
        viewModelScope.launch {
            auth_repository.login(authorizationCode)
        }
    }
}