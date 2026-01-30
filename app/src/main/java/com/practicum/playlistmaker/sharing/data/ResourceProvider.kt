package com.practicum.playlistmaker.sharing.data

import android.content.Context
import com.practicum.playlistmaker.sharing.domain.api.ResourceProvider

class ResourceProvider(
    private val context: Context
) : ResourceProvider {

    override fun getString(resId: Int): String {
        return context.getString(resId)
    }

    override fun getString(resId: Int, vararg args: Any): String {
        return context.getString(resId, *args)
    }

    override fun getString(resId: Int, quantity: Int, vararg args: Any): String {
        return context.resources.getQuantityString(resId, quantity, *args)
    }
}