package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.base.BaseActivity
import androidx.core.content.withStyledAttributes
import com.google.android.material.textview.MaterialTextView
import androidx.core.net.toUri

class SettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
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
            isChecked = isDarkTheme()
            setOnCheckedChangeListener { _, isChecked ->
                AppCompatDelegate.setDefaultNightMode(
                    if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                )
                recreate()
            }
        }
    }

    private fun isDarkTheme(): Boolean {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }
}