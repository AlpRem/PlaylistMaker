package com.practicum.playlistmaker.sharing.domain.api

import com.practicum.playlistmaker.sharing.data.EmailData

interface ExternalNavigator {
    fun sharePlaylist(playlistInfo: String)
    fun shareLink()
    fun openLink()
    fun openEmail()
}