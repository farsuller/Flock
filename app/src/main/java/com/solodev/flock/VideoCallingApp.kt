package com.solodev.flock

import android.app.Application
import com.solodev.flock.di.appModule
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.model.User
import io.getstream.video.android.model.UserType
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class VideoCallingApp: Application() {

    // Holds the current user's name
    private var currentName: String? = null

    // Holds the StreamVideo client
    var client: StreamVideo? = null

    override fun onCreate() {
        super.onCreate()
        // Initialize Koin dependency injection
        startKoin{
            androidContext(this@VideoCallingApp)
            modules(appModule)
        }
    }

    // Initializes the video client with the given user name
    fun initVideoClient(name:String){

        // Check if the client is not initialized or if the name has changed
        if(client == null || name != currentName){
            StreamVideo.removeClient() // Remove the existing client if any
            currentName = name // Update the current name

            // Build a new StreamVideo client with the provided name
            client = StreamVideoBuilder(
                context = this,
                apiKey = "zbpvntqvvryq",
                user = User(
                    id = name,
                    name = name,
                    type = UserType.Guest
                )
            ).build()
        }
    }
}