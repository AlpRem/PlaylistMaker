package com.practicum.playlistmaker.sharing.domain.model


data class SharingState(
    val command: SharingCommand? = null
) {
    sealed class SharingCommand {
        data object ShareApp : SharingCommand()
        data object OpenSupport : SharingCommand()
        data object OpenAgreement : SharingCommand()
    }
}