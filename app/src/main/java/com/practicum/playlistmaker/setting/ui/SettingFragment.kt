package com.practicum.playlistmaker.setting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.setting.presenter.SettingViewModel
import com.practicum.playlistmaker.sharing.presenter.SharingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SettingFragment: Fragment() {

    companion object {
        const val TAG = "SettingFragment"
    }

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingViewModel by viewModel()

    private val sharingViewModel: SharingViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBars.top)
            insets
        }

        initSettingViewModel()
        initSharingViewModel()
    }

    private fun initSettingViewModel() {
        viewModel.observeStateTheme.observe(viewLifecycleOwner) { state ->
            binding.switchThemes.isChecked = state.isDarkTheme
        }

        viewModel.observeStateApplyTheme.observe(viewLifecycleOwner) { isDark ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDark) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        binding.switchThemes.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onThemeSwitched(isChecked)
        }
    }

    private fun initSharingViewModel() {
        binding.textViewShared.setOnClickListener { sharingViewModel.shareApp() }
        binding.textViewSupport.setOnClickListener { sharingViewModel.openSupport() }
        binding.textViewAgreement.setOnClickListener { sharingViewModel.openAgreement() }
    }
}