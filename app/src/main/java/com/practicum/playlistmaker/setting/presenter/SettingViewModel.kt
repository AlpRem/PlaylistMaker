package com.practicum.playlistmaker.setting.presenter

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.setting.domain.api.SettingsInteractor
import com.practicum.playlistmaker.setting.domain.model.SettingsState

class SettingViewModel(context: Context): ViewModel() {

    companion object {
        fun getFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingViewModel(context)
            }
        }
    }

    private var settingsInteractor: SettingsInteractor = Creator.provideSettingsInteractor(context)
    private val stateTheme = MutableLiveData(SettingsState())
    val observeStateTheme: LiveData<SettingsState> = stateTheme

    private val stateApplyTheme = MutableLiveData<Boolean>()
    val observeStateApplyTheme: LiveData<Boolean> = stateApplyTheme

    init {
        stateTheme.value = SettingsState(isDarkTheme = settingsInteractor.isDarkThemeEnabled())
    }

    fun onThemeSwitched(enabled: Boolean) {
        settingsInteractor.setDarkThemeEnabled(enabled)
        stateTheme.value = SettingsState(isDarkTheme = enabled)
        stateApplyTheme.value = enabled
    }

}