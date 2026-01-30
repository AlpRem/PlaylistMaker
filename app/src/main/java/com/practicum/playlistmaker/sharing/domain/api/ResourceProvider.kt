package com.practicum.playlistmaker.sharing.domain.api

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

interface ResourceProvider {
    fun getString(@StringRes resId: Int): String
    fun getString(@StringRes resId: Int, vararg args: Any): String

    fun getString(@PluralsRes resId: Int, quantity: Int, vararg args: Any): String
}