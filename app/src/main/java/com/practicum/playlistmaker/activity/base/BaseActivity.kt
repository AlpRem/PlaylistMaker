package com.practicum.playlistmaker.activity.base

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

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