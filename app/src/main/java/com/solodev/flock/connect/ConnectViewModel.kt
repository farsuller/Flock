package com.solodev.flock.connect

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.solodev.flock.VideoCallingApp


class ConnectViewModel(private val app : Application): AndroidViewModel(app) {

    var state by mutableStateOf(ConnectState())
        private set

    fun onAction(action:ConnectAction){
        when(action){
            ConnectAction.OnConnectClick -> { connectToRoom() }
            is ConnectAction.OnNameChange -> {
                state = state.copy(name = action.name)
            }
        }
    }

    // Attempt to connect to a room
    private fun connectToRoom(){
        // Reset any previous error message
        state = state.copy(errorMessage = null)

        // Validate the name is not blank
        if (state.name.isBlank()){
            state = state.copy(errorMessage = "The name can't be blank")
            return
        }

        // Initialize the video client with the provided name
        (app as VideoCallingApp).initVideoClient(state.name)

        // Update the state to indicate the user is connected
        state = state.copy(isConnected = true)
    }
}