package com.practicum.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSearch = findViewById<Button>(R.id.btn_search)
        val btnLibrary = findViewById<Button>(R.id.btn_library)
        val btnSetting = findViewById<Button>(R.id.btn_setting)

        val btnSearchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Нажали на кнопку поиска!", Toast.LENGTH_SHORT).show()
            }
        }
        btnSearch.setOnClickListener(btnSearchClickListener)

        btnLibrary.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на кнопку медиатеки !", Toast.LENGTH_SHORT).show()
        }

        btnSetting.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на кнопку настроек !", Toast.LENGTH_SHORT).show()
        }
    }
}