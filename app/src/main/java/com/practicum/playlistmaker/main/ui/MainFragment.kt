package com.practicum.playlistmaker.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentMainBinding
import com.practicum.playlistmaker.library.ui.LibraryActivity
import com.practicum.playlistmaker.search.ui.SearchActivity
import com.practicum.playlistmaker.setting.ui.SettingsActivity

class MainFragment: Fragment() {

    private lateinit var binding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBars.top)
            insets
        }

        binding.btnSearch.setOnClickListener {
            val searchIntent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(searchIntent)
        }

        binding.btnLibrary.setOnClickListener {
            val libraryIntent = Intent(requireContext(), LibraryActivity::class.java)
            startActivity(libraryIntent)
        }

        binding.btnSetting.setOnClickListener {
            val settingsIntent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}