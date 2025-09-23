package com.practicum.playlistmaker.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.SettingsInteractor
import com.practicum.playlistmaker.ui.base.BaseActivity

class SettingsActivity : BaseActivity() {

    private lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        settingsInteractor = Creator.provideSettingsInteractor(applicationContext)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_setting)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBars.top)
            insets
        }

        initArrowBack()
        initThemeSwitch()
        initSharedButton()
        initSupportButton()
        initAgreementButton()
    }

    private fun initArrowBack() {
        arrowBackButton(R.id.arrow_back)
    }

    private fun initThemeSwitch() {
        findViewById<SwitchMaterial>(R.id.switch_themes).apply {
            isChecked = (application as App).getCurrentTheme()
            setOnCheckedChangeListener { _, isChecked ->
                (application as App).switchTheme(isChecked)
                recreate()
                settingsInteractor.setDarkThemeEnabled(isChecked)
            }
        }
    }
    private fun initSharedButton() {
            findViewById<MaterialTextView>(R.id.text_view__shared).setOnClickListener {
                val intent = Intent.createChooser(
                    Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.shared_text))
                flags = Intent.FLAG_ACTIVITY_NEW_TASK},
                getString(R.string.shared_title))
            startActivity(intent)
        }
    }

    private fun initSupportButton() {
        findViewById<MaterialTextView>(R.id.text_view__support).setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = "mailto:".toUri()
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_message))
            startActivity(intent)
        }
    }

    private fun initAgreementButton() {
        findViewById<MaterialTextView>(R.id.text_view__agreement).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, getString(R.string.agreement_url).toUri())
            startActivity(intent)
        }
    }
}