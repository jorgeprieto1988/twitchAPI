package edu.uoc.pac4.ui.streams

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.pac4.data.streams.Stream
import edu.uoc.pac4.data.streams.StreamsRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class StreamsViewModel(private val repository: StreamsRepository) : ViewModel() {

    // Live Data
    private val streams = MutableLiveData<List<Stream>>()
    private val nextCursor = MutableLiveData<String?>()

    fun getSavedStreams() = streams
    fun getCursor() = nextCursor

    init{
        viewModelScope.launch {
            getStreams()
        }


    }

    fun getStreams(cursor: String? = null){

        viewModelScope.launch{
            val listStreams = repository.getStreams(cursor)
            listStreams.collect(){collectedStreams ->
                if (cursor != null) {
                    streams.postValue(streams.value?.plus(collectedStreams.second))

                } else {

                    // It's the first n items, no pagination yet
                    streams.postValue(collectedStreams.second)
                    nextCursor.postValue(collectedStreams.first)
                }
            }
        }
    }

}