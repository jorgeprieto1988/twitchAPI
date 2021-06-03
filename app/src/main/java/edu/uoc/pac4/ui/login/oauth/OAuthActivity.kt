package edu.uoc.pac4.ui.login.oauth

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import edu.uoc.pac4.ui.LaunchActivity
import edu.uoc.pac4.R
import edu.uoc.pac4.data.authentication.datasource.SessionManager
import edu.uoc.pac4.data.TwitchApiService
import edu.uoc.pac4.data.authentication.datasource.TwitchAuthenticationService
import edu.uoc.pac4.data.authentication.repository.AuthenticationRepository
import edu.uoc.pac4.data.authentication.repository.OAuthAuthenticationRepository
import edu.uoc.pac4.data.util.Endpoints
import edu.uoc.pac4.data.util.Network
import edu.uoc.pac4.data.util.OAuthConstants
import edu.uoc.pac4.ui.login.LoginActivity
import edu.uoc.pac4.ui.profile.ProfileViewModel
import edu.uoc.pac4.ui.streams.StreamsActivity
import kotlinx.android.synthetic.main.activity_oauth.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class OAuthActivity : AppCompatActivity() {

    private val TAG = "OauthActivity"

    // Temporary repository before creating the OAuthViewModel
    //val repository: AuthenticationRepository by inject()
    private val viewModel  by viewModel<OAuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oauth)

        launchOAuthAuthorization()
    }

    private fun initObservers() {
        // Observe `isUserAvailable` LiveData
        viewModel.isLoginSuccess.observe(this) {
            // Call a method when a new value is emitted
            onLoginSuccess(it)
        }
    }

    private fun onLoginSuccess(isLoginSuccess: Boolean) {
        if (isLoginSuccess) {
            Log.v("Token", "Login ok")
            // User is available, open Streams Activity
            progressBar.visibility = View.GONE
            startActivity(Intent(this@OAuthActivity, LaunchActivity::class.java))
        } else {
            Log.v("Token", "Login failed")
            // User not available, request Login
            startActivity(Intent(this@OAuthActivity, OAuthActivity::class.java))
        }
        finish()
    }


    fun buildOAuthUri(): Uri {
        return Uri.parse(Endpoints.authorizationUrl)
            .buildUpon()
            .appendQueryParameter("client_id", OAuthConstants.clientID)
            .appendQueryParameter("redirect_uri", OAuthConstants.redirectUri)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("scope", OAuthConstants.scopes.joinToString(separator = " "))
            .appendQueryParameter("state", OAuthConstants.uniqueState)
            .build()
    }

    private fun launchOAuthAuthorization() {
        //  Create URI
        val uri = buildOAuthUri()

        // Set webView Redirect Listener
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                request?.let {
                    // Check if this url is our OAuth redirect, otherwise ignore it
                    if (request.url.toString().startsWith(OAuthConstants.redirectUri)) {
                        // To prevent CSRF attacks, check that we got the same state value we sent, otherwise ignore it
                        val responseState = request.url.getQueryParameter("state")
                        if (responseState == OAuthConstants.uniqueState) {
                            // This is our request, obtain the code!
                            request.url.getQueryParameter("code")?.let { code ->
                                // Got it!
                                Log.d("OAuth", "Here is the authorization code! $code")
                                onAuthorizationCodeRetrieved(code)
                                // Hide WebView
                                webView.visibility = View.GONE
                            } ?: run {
                                // User cancelled the login flow
                                // Handle error
                                Toast.makeText(
                                    this@OAuthActivity,
                                    getString(R.string.error_oauth),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        // Load OAuth Uri
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(uri.toString())
    }

    // Call this method after obtaining the authorization code
    // on the WebView to obtain the tokens
    private fun onAuthorizationCodeRetrieved(authorizationCode: String) {

        // Show Loading Indicator
        progressBar.visibility = View.VISIBLE

        // If the Activity is closed, this Thread will be cancelled
        lifecycleScope.launch {

            try {
                Log.w(TAG, "entering logins")
                viewModel.login(authorizationCode)
                initObservers()
                Log.w(TAG, "out of  logins")
                // Hide Loading Indicator
               // progressBar.visibility = View.GONE
                //Log.w(TAG, "starting activity launch")
                // Restart app to navigate to StreamsActivity
                //startActivity(Intent(this@OAuthActivity, LaunchActivity::class.java))
                //finish()
            }
            catch(e :Error){
                Log.w(TAG, "catching errors")
                Toast.makeText(
                    this@OAuthActivity,
                    getString(R.string.error_oauth),
                    Toast.LENGTH_LONG
                ).show()
                // Restart Activity
                //finish()
                //startActivity(Intent(this@OAuthActivity, OAuthActivity::class.java))
            }
       
        }


    }
}