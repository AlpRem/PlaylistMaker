package com.practicum.playlistmaker.base

import android.content.Intent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.practicum.playlistmaker.MainActivity
import com.practicum.playlistmaker.R

abstract class BaseActivity: AppCompatActivity() {
    protected fun arrowBackButton(arrowBack: Int) {
//        findViewById<MaterialToolbar>(materialToolbar).setNavigationOnClickListener  {
//            finish()
//        }

        findViewById<ImageView>(arrowBack).setOnClickListener  {
            finish()
        }
    }
}