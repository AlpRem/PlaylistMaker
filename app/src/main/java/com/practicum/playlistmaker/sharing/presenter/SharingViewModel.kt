package com.practicum.playlistmaker.sharing.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.model.SharingState

class SharingViewModel(private val sharingInteractor: SharingInteractor): ViewModel() {

    private val stateSharing = MutableLiveData<SharingState>()
    val observeStateSharing: LiveData<SharingState> = stateSharing

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