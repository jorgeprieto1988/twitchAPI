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
import edu.uoc.pac4.ui.profile.ProfileViewModel
import kotlinx.android.synthetic.main.activity_oauth.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class OAuthActivity : AppCompatActivity() {

    private val TAG = "StreamsActivity"

    // Temporary repository before creating the OAuthViewModel
    //val repository: AuthenticationRepository by inject()
    private val viewModel  by viewModel<OAuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oauth)
        launchOAuthAuthorization()
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

        // Create Twitch Service

        //val service = TwitchApiService(Network.createHttpClient(this, "", ""))
        // Launch new thread attached to this Activity.
        // If the Activity is closed, this Thread will be cancelled
        lifecycleScope.launch {
            //val sessionManager = SessionManager(this@OAuthActivity)
            //val twitchauth = TwitchAuthenticationService(Network.createHttpClient(this@OAuthActivity, "", ""))
            //val auth = OAuthAuthenticationRepository(sessionManager, twitchauth)

            try {
                viewModel.login(authorizationCode)
                //auth.login(authorizationCode)
                // Hide Loading Indicator
                progressBar.visibility = View.GONE

                // Restart app to navigate to StreamsActivity
                startActivity(Intent(this@OAuthActivity, LaunchActivity::class.java))
                finish()
            }
            catch(e :Error){
                Toast.makeText(
                    this@OAuthActivity,
                    getString(R.string.error_oauth),
                    Toast.LENGTH_LONG
                ).show()
                // Restart Activity
                finish()
                startActivity(Intent(this@OAuthActivity, OAuthActivity::class.java))
            }
       
        }
    }
}