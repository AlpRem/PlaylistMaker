package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.base.BaseActivity
import androidx.core.content.withStyledAttributes

class SettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_setting)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBars.top)
            insets
        }

        arrowBackButton(R.id.arrow_back)


        val switchTheme = findViewById<SwitchMaterial>(R.id.switch_themes)
        val sharedPref = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val isDarkTheme = sharedPref.getBoolean("dark_theme", false)
        switchTheme.isChecked = isDarkTheme
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            sharedPref.edit().putBoolean("dark_theme", isChecked).apply()
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            recreate()
        }
    }


    @SuppressLint("ResourceType")
    fun SwitchMaterial.setStyle(@StyleRes styleRes: Int) {
        val attrs = intArrayOf(
            com.google.android.material.R.attr.thumbTint,
            com.google.android.material.R.attr.trackTint
        )
        context.withStyledAttributes(styleRes, attrs) {
            thumbTintList = getColorStateList(0)
            trackTintList = getColorStateList(1)

        }
    }
}