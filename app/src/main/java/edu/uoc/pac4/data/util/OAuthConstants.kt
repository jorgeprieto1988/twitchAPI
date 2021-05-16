package edu.uoc.pac4.data.util

import java.util.*

/**
 * Created by alex on 07/09/2020.
 */
object OAuthConstants {

    // OAuth2 Variables
    const val clientID = "12345" // TODO: Replace with your Client ID
    const val redirectUri = "http://localhost" // TODO: Replace with your redirect uri
    val scopes = listOf("user:read:email user:edit")
    val uniqueState = UUID.randomUUID().toString()
    const val clientSecret = "12345" // TODO: Replace with your Client Secret

}