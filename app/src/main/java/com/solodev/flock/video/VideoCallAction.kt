package com.solodev.flock.video

sealed interface VideoCallAction {
    data object onDisconnectClick : VideoCallAction
    data object JoinCall : VideoCallAction
}