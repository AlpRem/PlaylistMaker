package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactorModule
import com.practicum.playlistmaker.di.repositoryModule
import com.practicum.playlistmaker.di.viewModelModule
import com.practicum.playlistmaker.setting.domain.api.SettingsInteractor
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val IS_DARK_THEME = "key_is_dark_theme"
const val HISTORY_TRACKS = "key_history_TRACKS"
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                dataModule,
                repositoryModule,
                interactorModule,
                viewModelModule
            )
        }
        val settingsInteractor: SettingsInteractor = getKoin().get()
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