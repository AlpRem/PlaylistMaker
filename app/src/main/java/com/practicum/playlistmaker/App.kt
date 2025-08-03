package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate


const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val IS_DARK_THEME = "key_is_dark_theme"
class App: Application() {
    private lateinit var sharedPrefs: SharedPreferences;
    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        switchTheme(getCurrentTheme());
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate
            .setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun getCurrentTheme(): Boolean {
        return sharedPrefs.getBoolean(IS_DARK_THEME,
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> true
                else -> false })
    }
}