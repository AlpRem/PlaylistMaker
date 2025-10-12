package com.practicum.playlistmaker.common.ui

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity: AppCompatActivity() {
    protected fun arrowBackButton(arrowBack: Int) {
        findViewById<ImageView>(arrowBack).setOnClickListener  {
            finish()
        }
    }
}