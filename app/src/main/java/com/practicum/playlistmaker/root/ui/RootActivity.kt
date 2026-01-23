package com.practicum.playlistmaker.root.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.practicum.playlistmaker.databinding.ActivityRootBinding
import com.practicum.playlistmaker.R
import androidx.core.view.isGone

class RootActivity: AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding
    private var isBottomNavAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            isBottomNavAllowed =
                destination.id != R.id.audioPlayerFragment &&
                        destination.id != R.id.playlistAddFragment &&
                        destination.id != R.id.playlistDetailsFragment

            bottomNavigationView.visibility =
                if (isBottomNavAllowed) View.VISIBLE else View.GONE
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val isImeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            binding.bottomNavigationView.visibility =
                if (isImeVisible || !isBottomNavAllowed) View.GONE else View.VISIBLE
            insets
        }
    }
}