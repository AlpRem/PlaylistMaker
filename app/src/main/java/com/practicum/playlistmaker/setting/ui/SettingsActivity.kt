package com.practicum.playlistmaker.setting.ui

import android.content.Intent
import android.os.Bundle
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.setting.domain.api.SettingsInteractor
import com.practicum.playlistmaker.common.ui.BaseActivity
import com.practicum.playlistmaker.databinding.ActivityLibraryBinding
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        arrowBackButton(R.id.arrow_back)

        settingsInteractor = Creator.provideSettingsInteractor(applicationContext)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_setting)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBars.top)
            insets
        }
        initThemeSwitch()
        initSharedButton()
        initSupportButton()
        initAgreementButton()
    }

    private fun initThemeSwitch() {
        binding.switchThemes.apply {
            isChecked = (application as App).getCurrentTheme()
            setOnCheckedChangeListener { _, isChecked ->
                (application as App).switchTheme(isChecked)
                recreate()
                settingsInteractor.setDarkThemeEnabled(isChecked)
            }
        }
    }
    private fun initSharedButton() {
            binding.textViewShared.setOnClickListener {
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
        binding.textViewSupport.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = "mailto:".toUri()
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_message))
            startActivity(intent)
        }
    }

    private fun initAgreementButton() {
        binding.textViewAgreement.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, getString(R.string.agreement_url).toUri())
            startActivity(intent)
        }
    }
}