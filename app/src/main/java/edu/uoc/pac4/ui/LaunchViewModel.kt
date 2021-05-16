package edu.uoc.pac4.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.pac4.data.authentication.repository.AuthenticationRepository
import kotlinx.coroutines.launch

/**
 * Created by alex on 11/21/20.
 */

// This is a ViewModel, used to expose data to the UI
// It can interact with the different repositories
class LaunchViewModel(
    private val repository: AuthenticationRepository
) : ViewModel() {

    // Live Data
    val isUserAvailable = MutableLiveData<Boolean>()

    init {
        getUserAvailability()
    }


    private fun getUserAvailability() {
        // Get Availability from Repository and post result
        viewModelScope.launch {
            isUserAvailable.postValue(repository.isUserAvailable())
        }
    }
}