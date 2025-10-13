package com.practicum.playlistmaker.sharing.domain.impl

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.data.EmailData
import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor

class SharingInteractorImpl(private val context: Context,
                            private val externalNavigator: ExternalNavigator) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink(), getShareTitle())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return context.getString(R.string.shared_text)
    }

    private fun getShareTitle(): String {
        return context.getString(R.string.shared_title)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(email = context.getString(R.string.support_email),
            subject = context.getString(R.string.support_subject),
            text = context.getString(R.string.support_message))
    }

    private fun getTermsLink(): String {
        return context.getString(R.string.agreement_url)
    }
}