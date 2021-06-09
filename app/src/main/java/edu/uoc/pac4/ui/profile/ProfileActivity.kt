package edu.uoc.pac4.ui.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import edu.uoc.pac4.R
import edu.uoc.pac4.data.util.Network
import edu.uoc.pac4.ui.login.LoginActivity
import edu.uoc.pac4.data.authentication.datasource.SessionManager
import edu.uoc.pac4.data.user.TwitchUserRepository
import edu.uoc.pac4.data.user.User
import edu.uoc.pac4.data.user.datasource.UserDataSource
import edu.uoc.pac4.data.util.OAuthConstants
import edu.uoc.pac4.data.util.OAuthException
import edu.uoc.pac4.ui.LaunchViewModel
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileActivity : AppCompatActivity() {
    private val viewModel  by viewModel<ProfileViewModel>()

    private val TAG = "ProfileActivity"

    //private val twitchApiService = TwitchApiService(Network.createHttpClient(this, "", ""))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initObservers()
        // Get User Profile
        //lifecycleScope.launch {

          //  getUserProfile()
       // }
        // Update Description Button Listener
        updateDescriptionButton.setOnClickListener {
            // Hide Keyboard
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(it.windowToken, 0)
            // Update User Description
            //lifecycleScope.launch {
                viewModel.getSavedUser().observe(this, Observer {
                    lifecycleScope.launch {
                    updateUserDescription(userDescriptionEditText.text?.toString() ?: "")}
                })
               // updateUserDescription(userDescriptionEditText.text?.toString() ?: "")
           // }
        }
        // Logout Button Listener
        logoutButton.setOnClickListener {
            // Logout

            logout()
        }
    }

    private fun initObservers() {
        getUserProfile()
    }

    private  fun getUserProfile() {
        progressBar.visibility = VISIBLE
        // Retrieve the Twitch User Profile using the API
        try {
            viewModel.getSavedUser().observe(this, Observer { user ->
                user?.let { setUserInfo(it) }
            })

            // Hide Loading
            progressBar.visibility = GONE
        } catch (t: OAuthException.Unauthorized) {
            onUnauthorized()
        }
    }


    private suspend fun updateUserDescription(description: String) {
        progressBar.visibility = VISIBLE
        // Update the Twitch User Description using the API
        try {
            viewModel.updateUserTwitch(description)
            // Hide Loading
            progressBar.visibility = GONE
        } catch (t: OAuthException.Unauthorized) {
            onUnauthorized()
        }
    }

    private fun setUserInfo(user: User) {
        // Set Texts
        userNameTextView.text = user.userName
        userDescriptionEditText.setText(user.description ?: "")
        // Avatar Image
        user.profileImageUrl?.let {
            Glide.with(this)
                .load(user.getSizedImage(it, 128, 128))
                .centerCrop()
                .transform(CircleCrop())
                .into(imageView)
        }
        // Views
        viewsText.text = getString(R.string.views_text, user.viewCount)
    }

    private fun logout() {
        viewModel.userLogOut()
        // Close this and all parent activities
        finishAffinity()
        // Open Login
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun onUnauthorized() {
        // Clear local access token
        //SessionManager(this).clearAccessToken()
        viewModel.clearAccessToken()
        // User was logged out, close screen and all parent screens and open login
        finishAffinity()
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun showError(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    // Override Action Bar Home button to just finish the Activity
    // not to re-launch the parent Activity (StreamsActivity)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            false
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}