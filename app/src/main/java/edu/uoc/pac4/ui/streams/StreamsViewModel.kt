package edu.uoc.pac4.ui.streams

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.pac4.data.authentication.datasource.SessionManager
import edu.uoc.pac4.data.authentication.repository.AuthenticationRepository
import edu.uoc.pac4.data.streams.Stream
import edu.uoc.pac4.data.streams.StreamsRepository
import edu.uoc.pac4.data.util.OAuthException
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.jvm.Throws


class StreamsViewModel(
        private val repository: StreamsRepository,
        private val auth_repository : AuthenticationRepository) : ViewModel() {

    // Live Data
    private val streams = MutableLiveData<List<Stream>>()
    private val nextCursor = MutableLiveData<String?>()
    private val isLoading = MutableLiveData<Boolean>()
    private val error_oauth = MutableLiveData<Boolean>()
    private val error_throw = MutableLiveData<Boolean>()

    fun getSavedStreams() = streams
    fun getCursor() = nextCursor
    fun getIsLoading() = isLoading
    fun getErrorThrow() = error_throw
    fun getErrorOauth() = error_oauth

    init{
        viewModelScope.launch {
            getStreams()
        }
        error_throw.postValue(false)
        error_oauth.postValue(false)

    }

    fun getStreams(cursor: String? = null){

        viewModelScope.launch{
            isLoading.postValue(true)
            val listStreams = repository.getStreams(cursor)
            listStreams.collect(){collectedStreams ->
                if (cursor != null) {
                    streams.postValue(streams.value?.plus(collectedStreams.second))
                    nextCursor.postValue(collectedStreams.first)
                    isLoading.postValue(false)
                } else {

                    // It's the first n items, no pagination yet
                    streams.postValue(collectedStreams.second)
                    nextCursor.postValue(collectedStreams.first)
                    isLoading.postValue(false)
                }
            }
            listStreams.catch { e ->
                if( e is OAuthException) {
                    Log.w("Viewmodel", "Unauthorized Error getting streams")
                    // Clear local access token

                    auth_repository.clearAccessToken()
                    error_oauth.postValue(true)
                }

                if(e is Throwable)
                {
                    Log.w("Viewmodel", "Unknown error getting streams")
                    error_throw.postValue(true)
                }
            }

        }
    }

}