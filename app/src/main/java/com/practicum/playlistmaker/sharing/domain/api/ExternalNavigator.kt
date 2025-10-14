package com.practicum.playlistmaker.sharing.domain.api

import com.practicum.playlistmaker.sharing.data.EmailData

interface ExternalNavigator {
    fun shareLink()
    fun openLink()
    fun openEmail()
}