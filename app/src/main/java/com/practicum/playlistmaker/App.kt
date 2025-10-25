package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.player.di.playerDataModule
import com.practicum.playlistmaker.player.di.playerInteractorModule
import com.practicum.playlistmaker.player.di.playerRepositoryModule
import com.practicum.playlistmaker.player.di.playerViewModelModule
import com.practicum.playlistmaker.search.di.searchDataModule
import com.practicum.playlistmaker.search.di.searchInteractorModule
import com.practicum.playlistmaker.search.di.searchRepositoryModule
import com.practicum.playlistmaker.search.di.searchViewModelModule
import com.practicum.playlistmaker.setting.di.settingInteractorModule
import com.practicum.playlistmaker.setting.di.settingViewModelModule
import com.practicum.playlistmaker.setting.di.settingsRepositoryModule
import com.practicum.playlistmaker.setting.domain.api.SettingsInteractor
import com.practicum.playlistmaker.sharing.di.sharingDataModule
import com.practicum.playlistmaker.sharing.di.sharingInteractorModule
import com.practicum.playlistmaker.sharing.di.sharingViewModelModule
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
                playerDataModule,
                playerInteractorModule,
                playerRepositoryModule,
                playerViewModelModule,

                searchDataModule,
                searchInteractorModule,
                searchRepositoryModule,
                searchViewModelModule,

                settingInteractorModule,
                settingsRepositoryModule,
                settingViewModelModule,

                sharingDataModule,
                sharingInteractorModule,
                sharingViewModelModule
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