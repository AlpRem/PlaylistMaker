package com.practicum.playlistmaker.player.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.model.AudioPlayerState

class LikeViewModel: ViewModel() {
    private var stateLike = MutableLiveData<Boolean>(false)
    val observeLike: LiveData<Boolean> = stateLike


    fun toggleLike() {
        this.stateLike.value = !(stateLike.value?: false)
    }
}