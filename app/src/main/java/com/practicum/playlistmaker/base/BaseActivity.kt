package com.practicum.playlistmaker.base

import android.content.Intent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.MainActivity

abstract class BaseActivity: AppCompatActivity() {
    protected fun arrowBackButton(backButtonId: Int) {
        findViewById<ImageView>(backButtonId).setOnClickListener {
            val backIntent = Intent(this, MainActivity::class.java)
            startActivity(backIntent)
            finish()
        }
    }
}