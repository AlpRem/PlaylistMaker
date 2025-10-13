package com.practicum.playlistmaker.sharing.domain.api

import com.practicum.playlistmaker.sharing.data.EmailData

interface ExternalNavigator {
    fun shareLink(shareAppLink: String, shareTitle: String)
    fun openLink(termsLink: String)
    fun openEmail(supportEmailData: EmailData)
}