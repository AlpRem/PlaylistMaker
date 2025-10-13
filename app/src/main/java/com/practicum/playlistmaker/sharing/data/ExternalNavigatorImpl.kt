package com.practicum.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator

class ExternalNavigatorImpl(private val context: Context): ExternalNavigator {

    override fun shareLink(shareAppLink: String, shareTitle: String) {
        val intent = Intent.createChooser(
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareAppLink)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK},
            shareTitle)
        context.startActivity(intent)
    }

    override fun openLink(termsLink: String) {
        val intent = Intent(Intent.ACTION_VIEW, termsLink.toUri())
        context.startActivity(intent)
    }

    override fun openEmail(supportEmailData: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmailData.email))
            putExtra(Intent.EXTRA_SUBJECT, supportEmailData.subject)
            putExtra(Intent.EXTRA_TEXT, supportEmailData.text)
        }
        context.startActivity(intent)
    }
}