package com.practicum.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator

class ExternalNavigatorImpl(private val context: Context): ExternalNavigator {

    override fun shareLink() {
        val intent = Intent.createChooser(
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getShareAppLink())
                flags = Intent.FLAG_ACTIVITY_NEW_TASK},
            getShareTitle())
        context.startActivity(intent)
    }

    override fun openLink() {
        val intent = Intent(Intent.ACTION_VIEW, getTermLink().toUri())
        context.startActivity(intent)
    }

    override fun openEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getSupportEmailData().email))
            putExtra(Intent.EXTRA_SUBJECT, getSupportEmailData().subject)
            putExtra(Intent.EXTRA_TEXT, getSupportEmailData().text)
        }
        context.startActivity(intent)
    }

    private fun getShareAppLink(): String {
        return context.getString(R.string.shared_text)
    }
    private fun getShareTitle(): String {
        return context.getString(R.string.shared_title)
    }

    private fun getTermLink(): String {
        return context.getString(R.string.agreement_url)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(email = context.getString(R.string.support_email),
            subject=context.getString(R.string.support_subject),
            text=context.getString(R.string.support_message))
    }
}