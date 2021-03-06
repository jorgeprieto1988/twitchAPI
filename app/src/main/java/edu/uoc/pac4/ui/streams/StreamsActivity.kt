package edu.uoc.pac4.ui.streams

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import edu.uoc.pac4.R
import edu.uoc.pac4.data.authentication.datasource.SessionManager
import edu.uoc.pac4.data.streams.TwitchStreamsRepository
import edu.uoc.pac4.data.streams.datasource.ApplicationDatabase
import edu.uoc.pac4.data.streams.datasource.StreamsLocal
import edu.uoc.pac4.data.streams.datasource.StreamsRemote
import edu.uoc.pac4.data.util.Network
import edu.uoc.pac4.data.util.OAuthConstants
import edu.uoc.pac4.data.util.OAuthException
import edu.uoc.pac4.ui.login.LoginActivity
import edu.uoc.pac4.ui.profile.ProfileActivity
import kotlinx.android.synthetic.main.activity_streams.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel


class StreamsActivity : AppCompatActivity() {

    private val TAG = "StreamsActivity"

    private val adapter = StreamsAdapter()
    private val layoutManager = LinearLayoutManager(this)

    private val viewModel  by viewModel<StreamsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streams)
        // Init RecyclerView
        initRecyclerView()
        initObservers()
    }

    private fun initRecyclerView() {
        // Set Layout Manager
        recyclerView.layoutManager = layoutManager
        // Set Adapter
        recyclerView.adapter = adapter
        // Set Pagination Listener
        recyclerView.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                Log.w(TAG, "Calling from recyclerView!!! viewModel.getStreams....")
                getStreams(nextCursor)
            }

            override fun isLastPage(): Boolean {
                return nextCursor == null
            }

            override fun isLoading(): Boolean {
                Log.w("Refreshing", "Value of refreshing is..."  + swipeRefreshLayout.isRefreshing.toString())
                return swipeRefreshLayout.isRefreshing
            }
        })
    }

    private fun initObservers() {
        viewModel.getCursor().observe(this) {
            // Call a method when a new value is emitted
            onNewCursor(it)
        }
        viewModel.getSavedStreams().observe(this, Observer { streams ->
            streams?.let { adapter.submitList(it) }
        })

        viewModel.getIsLoading().observe(this, Observer { loading ->
            swipeRefreshLayout.isRefreshing = loading
        })

        viewModel.getErrorOauth().observe(this, Observer { error ->
            if(error) {
                finish()
                startActivity(Intent(this@StreamsActivity, LoginActivity::class.java))
            }
        })

        viewModel.getErrorThrow().observe(this, Observer { error ->
            if(error){
                Toast.makeText(
                        this@StreamsActivity,
                        getString(R.string.error_streams), Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun onNewCursor(newCursor: String?) {
        nextCursor = newCursor
    }

    private var nextCursor: String? = null
    private fun getStreams(cursor: String? = null) {
                viewModel.getStreams(nextCursor)
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