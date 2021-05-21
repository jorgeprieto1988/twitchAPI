package edu.uoc.pac4.ui.streams

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import edu.uoc.pac4.R
import edu.uoc.pac4.data.authentication.datasource.SessionManager
import edu.uoc.pac4.data.TwitchApiService
import edu.uoc.pac4.data.streams.Stream
import edu.uoc.pac4.data.streams.TwitchStreamsRepository
import edu.uoc.pac4.data.streams.datasource.ApplicationDatabase
import edu.uoc.pac4.data.streams.datasource.StreamsLocal
import edu.uoc.pac4.data.streams.datasource.StreamsRemote
import edu.uoc.pac4.data.util.Network
import edu.uoc.pac4.data.util.OAuthException
import edu.uoc.pac4.ui.login.LoginActivity
import edu.uoc.pac4.ui.profile.ProfileActivity
import kotlinx.android.synthetic.main.activity_streams.*
import kotlinx.coroutines.launch



class StreamsActivity : AppCompatActivity() {

    private val TAG = "StreamsActivity"

    private val adapter = StreamsAdapter()
    private val layoutManager = LinearLayoutManager(this)

    private val twitchApiService = TwitchApiService(Network.createHttpClient(this, "", ""))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streams)
        // Init RecyclerView
        initRecyclerView()
        // Swipe to Refresh Listener
        swipeRefreshLayout.setOnRefreshListener {
            getStreams()
        }
        // Get Streams
        getStreams()
    }

    private fun initRecyclerView() {
        // Set Layout Manager
        recyclerView.layoutManager = layoutManager
        // Set Adapter
        recyclerView.adapter = adapter
        // Set Pagination Listener
        recyclerView.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                getStreams(nextCursor)
            }

            override fun isLastPage(): Boolean {
                return nextCursor == null
            }

            override fun isLoading(): Boolean {
                return swipeRefreshLayout.isRefreshing
            }
        })
    }

    private var nextCursor: String? = null
    private fun getStreams(cursor: String? = null) {
        Log.d(TAG, "Requesting streams with cursor $cursor")

        // Show Loading
        swipeRefreshLayout.isRefreshing = true

        // Get Twitch Streams
        lifecycleScope.launch {
            try {
                val database = Room.databaseBuilder(applicationContext,
                    ApplicationDatabase::class.java, "app_database").build()
                val streamslocal = StreamsLocal(database.streamDao())
                val streamsremote = StreamsRemote(Network.createHttpClient(this@StreamsActivity, "", ""))
                val streams = TwitchStreamsRepository(streamsremote,streamslocal)
                val listStreams = streams.getStreams(cursor)
                Log.w(TAG, "streams are "+ listStreams.toString())
                init {
                    viewModelScope.launch {
                        // Trigger the flow and consume its elements using collect
                        newsRepository.favoriteLatestNews.collect { favoriteNews ->
                            // Update View with the latest favorite news
                        }
                    }
                }
                if (cursor != null) {
                    // We are adding more items to the list
                    adapter.submitList(adapter.currentList.plus(listStreams.collect(List<Stream>)))
                } else {
                    // It's the first n items, no pagination yet
                    adapter.submitList(listStreams)
                }
                // Hide Loading
                swipeRefreshLayout.isRefreshing = false

            } catch (t: OAuthException.Unauthorized) {
                Log.w(TAG, "Unauthorized Error getting streams", t)
                // Clear local access token
                SessionManager(this@StreamsActivity).clearAccessToken()
                // User was logged out, close screen and open login
                finish()
                startActivity(Intent(this@StreamsActivity, LoginActivity::class.java))
            } catch (t: Throwable) {
                // We don't know why this happen, just show an error message
                Log.w(TAG, "Unknown error getting streams", t)
                Toast.makeText(
                    this@StreamsActivity,
                    getString(R.string.error_streams), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // region Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate Menu
        menuInflater.inflate(R.menu.menu_streams, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.menu_user -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    // endregion
}