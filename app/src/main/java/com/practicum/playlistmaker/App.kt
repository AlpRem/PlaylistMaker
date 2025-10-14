package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator


const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val IS_DARK_THEME = "key_is_dark_theme"
const val HISTORY_TRACKS = "key_history_TRACKS"
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        val settingsInteractor = Creator.provideSettingsInteractor(applicationContext)
        val isDark = settingsInteractor.isDarkThemeEnabled()

        switchTheme(isDark)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}