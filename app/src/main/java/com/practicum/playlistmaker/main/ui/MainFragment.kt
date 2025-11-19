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
import androidx.fragment.app.commit
import com.practicum.playlistmaker.databinding.FragmentMainBinding
import com.practicum.playlistmaker.setting.ui.SettingsActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.library.ui.LibraryFragment
import com.practicum.playlistmaker.search.ui.SearchFragment

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
            parentFragmentManager.commit {
                replace(
                R.id.rootFragmentContainerView,
                    SearchFragment(),
                    SearchFragment.TAG
                )
                addToBackStack(SearchFragment.TAG)
            }
        }

        binding.btnLibrary.setOnClickListener {
            parentFragmentManager.commit {
                replace(
                    R.id.rootFragmentContainerView,
                    LibraryFragment(),
                    LibraryFragment.TAG
                )
                addToBackStack(SearchFragment.TAG)
            }
        }

        binding.btnSetting.setOnClickListener {
            val settingsIntent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}