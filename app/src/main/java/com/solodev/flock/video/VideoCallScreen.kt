package com.solodev.flock.video

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.getstream.video.android.compose.permission.rememberCallPermissionsState
import io.getstream.video.android.compose.ui.components.call.activecall.CallContent
import io.getstream.video.android.compose.ui.components.call.controls.actions.DefaultOnCallActionHandler
import io.getstream.video.android.core.call.state.LeaveCall

@Composable
fun VideoCallScreen(
    state: VideoCallState,
    onAction: (VideoCallAction) -> Unit
){
    when {
        // Display an error message if there is an error in the state
        state.error != null ->{
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center){

                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error)
            }
        }

        // Show a loading indicator and message when joining a call
        state.callState == CallState.JOINING ->{
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Joining...")
            }
        }
        else ->{
            // Define required permissions based on the SDK version
            val basePermissions = listOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)

            val bluetoothConnectPermission = if(Build.VERSION.SDK_INT >= 31){
                listOf(Manifest.permission.BLUETOOTH_CONNECT)
            } else{ emptyList() }

            val notificationPermission = if(Build.VERSION.SDK_INT >= 31){
                listOf(Manifest.permission.POST_NOTIFICATIONS)
            } else{ emptyList() }

            val context = LocalContext.current

            // Define required permissions based on the SDK version
            CallContent(call = state.call,
                modifier = Modifier.fillMaxSize(),
                permissions = rememberCallPermissionsState(
                    call = state.call,
                    permissions = basePermissions + bluetoothConnectPermission + notificationPermission,
                    onPermissionsResult = { permissions->
                        if(permissions.values.contains(false)){
                            Toast.makeText(context, "Accept grant all permissions to use the app's feature.", Toast.LENGTH_LONG).show()
                        }
                        else{
                            onAction(VideoCallAction.JoinCall)
                        }
                    },
                ),
                // Handle call actions
                onCallAction = { callAction ->
                    if(callAction == LeaveCall){
                        onAction(VideoCallAction.onDisconnectClick)
                    }

                    DefaultOnCallActionHandler.onCallAction(state.call, callAction)
                },
                // Handle back press event
                onBackPressed = {
                    onAction(VideoCallAction.onDisconnectClick)
                }
            )
        }
    }
}