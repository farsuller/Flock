package com.solodev.flock.di



import com.solodev.flock.VideoCallingApp
import com.solodev.flock.connect.ConnectViewModel
import com.solodev.flock.video.VideoCallViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    // Provides the StreamVideo client from the VideoCallingApp
    factory {
        val app = androidContext().applicationContext as VideoCallingApp
        app.client
    }

    // Provides instances of ConnectViewModel
    viewModelOf(::ConnectViewModel)

    // Provides instances of VideoCallViewModel
    viewModelOf(::VideoCallViewModel)
}