package com.practicum.playlistmaker.sharing.presenter

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.model.SharingState

class SharingViewModel(context: Context): ViewModel() {

    private var sharingInteractor: SharingInteractor = Creator.providerSharing(context)
    private val stateSharing = MutableLiveData<SharingState>()
    val observeStateSharing: LiveData<SharingState> = stateSharing

    companion object {
        fun getFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer { SharingViewModel(context) }
        }
    }

    fun shareApp() {
        sharingInteractor.shareApp()
        stateSharing.value = SharingState(SharingState.SharingCommand.ShareApp)
    }

    fun openSupport() {
        sharingInteractor.openSupport()
        stateSharing.value = SharingState(SharingState.SharingCommand.OpenSupport)
    }

    fun openAgreement() {
        sharingInteractor.openTerms()
        stateSharing.value = SharingState(SharingState.SharingCommand.OpenAgreement)
    }
}