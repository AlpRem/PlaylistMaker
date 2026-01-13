package com.practicum.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.library.presenter.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue
import com.practicum.playlistmaker.R

class PlaylistFragment: Fragment() {

    companion object {
        fun newInstance(): PlaylistFragment {
            return PlaylistFragment()
        }
    }

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        viewModel.listPlaylist()

        binding.createPlaylistBtn.setOnClickListener {
            requireParentFragment()
                .findNavController()
                .navigate(R.id.action_libraryFragment_to_playlistAddFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            when {
                state.isLoading -> {
                    // пока ничего не делаем, позже добавите ProgressBar
                }

                state.isError -> {
                    // можно просто залогировать
                }

                state.isEmpty -> {
                    // пустое состояние (заглушка)
                }

                else -> {
                    // контент загружен
                    // сейчас ничего не делаем, данные уже логируются во ViewModel
                }
            }
        }
    }
}