package edu.uoc.pac4

import android.app.Application
import edu.uoc.pac4.data.di.dataModule
import edu.uoc.pac4.ui.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Created by alex on 4/24/21.
 */
class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        // Init Dependency Injection
        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(uiModule, dataModule)
        }
    }
}