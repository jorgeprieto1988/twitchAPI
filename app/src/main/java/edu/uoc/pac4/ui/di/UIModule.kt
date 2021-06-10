package edu.uoc.pac4.ui.di

import edu.uoc.pac4.ui.LaunchViewModel
import edu.uoc.pac4.ui.login.oauth.OAuthViewModel
import edu.uoc.pac4.ui.profile.ProfileViewModel
import edu.uoc.pac4.ui.streams.StreamsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by alex on 11/21/20.
 */

val uiModule = module {
    // TODO: Init your UI Dependencies

    // LaunchViewModel
    viewModel { LaunchViewModel(repository = get()) }
    viewModel { ProfileViewModel(user_repository = get(), auth_repository = get()) }
    viewModel { StreamsViewModel(repository = get(), auth_repository = get())}
    viewModel { OAuthViewModel(auth_repository = get()) }
}