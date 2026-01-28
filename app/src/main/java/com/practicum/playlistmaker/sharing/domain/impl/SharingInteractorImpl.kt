package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor

class SharingInteractorImpl(private val externalNavigator: ExternalNavigator) : SharingInteractor {
    override fun sharePlaylistApp(playlistInfo: String) {
        externalNavigator.sharePlaylist(playlistInfo)
    }

    override fun shareApp() {
        externalNavigator.shareLink()
    }

    override fun openTerms() {
        externalNavigator.openLink()
    }

    override fun openSupport() {
        externalNavigator.openEmail()
    }
}