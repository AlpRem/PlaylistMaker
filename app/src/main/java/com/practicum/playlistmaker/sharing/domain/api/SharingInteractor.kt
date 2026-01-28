package com.practicum.playlistmaker.sharing.domain.api

interface SharingInteractor {
    fun sharePlaylistApp(playlistInfo: String)
    fun shareApp()
    fun openTerms()
    fun openSupport()
}