package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
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
        switchTheme.setOnCheckedChangeListener {
                _, isChecked ->
                    val styleRes = if (isChecked) R.style.switchStyleDark else R.style.switchStyleLight
                    switchTheme.setStyle(styleRes)
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