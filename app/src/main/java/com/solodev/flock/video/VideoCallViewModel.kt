package com.solodev.flock.video

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.getstream.video.android.core.StreamVideo
import kotlinx.coroutines.launch

class VideoCallViewModel(
    private val videoClient: StreamVideo
): ViewModel() {

    // Holds the state of the video call
    var state by mutableStateOf(VideoCallState(
        call = videoClient.call("default" , "main-room")
    ))
        private set

    // Handles actions from the UI
    fun onAction(action : VideoCallAction){
        when(action){

            // Handle join call action
            VideoCallAction.JoinCall -> { joinCall() }

            // Handle disconnect action
            VideoCallAction.onDisconnectClick -> {
                state.call.leave() // Leave the current call
                videoClient.logOut() // Log out from the video client
                state = state.copy(callState = CallState.ENDED) // Update state to ended
            }
        }
    }

    // Attempt to join a call
    private fun joinCall(){

        // If already active, return early
        if (state.callState == CallState.ACTIVE){
            return
        }

        // Launch a coroutine to handle joining the call
        viewModelScope.launch {
            state = state.copy(callState = CallState.JOINING)

            // Determine if a new call should be created
            val shouldCreate = videoClient
                .queryCalls(filters = emptyMap())
                .getOrNull()
                ?.calls
                ?.isEmpty() == true


            // Attempt to join the call
            state.call.join(create = shouldCreate)
                .onSuccess {
                    state = state.copy(
                        callState = CallState.ACTIVE,
                        error = null)
                }// Update state to active and clear any error
                .onError {
                    state = state.copy(
                        error = it.message,
                        callState = null
                    ) // Update state with error message
                }
        }
    }
}