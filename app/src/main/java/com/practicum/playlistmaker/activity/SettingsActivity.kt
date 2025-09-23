package com.practicum.playlistmaker.activity

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.ui.base.BaseActivity
import com.google.android.material.textview.MaterialTextView
import androidx.core.net.toUri
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.IS_DARK_THEME
import com.practicum.playlistmaker.PLAYLIST_MAKER_PREFERENCES
import com.practicum.playlistmaker.R


class SettingsActivity : BaseActivity() {

    private lateinit var sharedPrefs: SharedPreferences;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_setting)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBars.top)
            insets
        }
        arrowBackButton(R.id.arrow_back)
        changeTheme();
        shared();
        support();
        agreement();
    }

    private fun shared() {
        val btnShared = findViewById<MaterialTextView>(R.id.text_view__shared)
        btnShared.setOnClickListener {
            val intent = Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.shared_text))
                flags = Intent.FLAG_ACTIVITY_NEW_TASK},
                getString(R.string.shared_title))
            startActivity(intent)
        }
    }

    private fun support() {
        val btnSupport = findViewById<MaterialTextView>(R.id.text_view__support)
        btnSupport.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = "mailto:".toUri()
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_message))
            startActivity(intent)
        }
    }

    private fun agreement() {
        val btnAgreement = findViewById<MaterialTextView>(R.id.text_view__agreement)
        btnAgreement.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, getString(R.string.agreement_url).toUri())
            startActivity(intent)
        }
    }

    private fun changeTheme() {
        findViewById<SwitchMaterial>(R.id.switch_themes).apply {
            isChecked = (application as App).getCurrentTheme()
            setOnCheckedChangeListener { _, isChecked ->
                (application as App).switchTheme(isChecked)
                recreate()
                sharedPrefs.edit { putBoolean(IS_DARK_THEME, isChecked) }

            }
        }
    }

    private fun isDarkTheme(): Boolean {
        return sharedPrefs.getBoolean(IS_DARK_THEME,
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false })
    }
}