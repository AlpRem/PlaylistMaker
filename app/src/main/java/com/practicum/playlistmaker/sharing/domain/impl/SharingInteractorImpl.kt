package com.practicum.playlistmaker.sharing.domain.impl


import com.practicum.playlistmaker.sharing.data.EmailData
import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor

class SharingInteractorImpl(private val externalNavigator: ExternalNavigator) : SharingInteractor {
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
        return "https://practicum.yandex.ru/android-developer/"
    }

    private fun getShareTitle(): String {
        return "Share APK"
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(email = "alprem2016@yandex.ru",
            subject = "Сообщение разработчикам и разработчицам приложения Playlist Maker",
            text = "Спасибо разработчикам и разработчицам за крутое приложение!")
    }

    private fun getTermsLink(): String {
        return "https://yandex.ru/legal/practicum_offer/"
    }
}