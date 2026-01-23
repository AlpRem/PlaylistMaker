package com.practicum.playlistmaker.library.ui

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.library.presenter.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.common.util.dpToPx
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.library.domain.model.PlaylistState

class PlaylistFragment: Fragment() {
    private var _binding: FragmentPlaylistBinding? = null
    private lateinit var playlistAdapter: PlaylistAdapter
    private val binding get() = _binding!!
    private val viewModel: PlaylistViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state)
            state.openPlaylistId?.let { playlistId ->
                findNavController().navigate(
                    R.id.action_libraryFragment_to_playlistDetailsFragment,
                    PlaylistDetailsFragment.createArgs(playlistId)
                )
                viewModel.clearOpenPlaylist()
            }
        }

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

    private fun initRecyclerView() {
        val recyclerView  = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.layoutManager = recyclerView
        playlistAdapter = PlaylistAdapter(emptyList(),
            onPlaylistClick = { playlist ->
                viewModel.onPlaylistClicked(playlist)
            })
        binding.recyclerView.adapter = playlistAdapter

        binding.recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val horizontalSpacing = 8.dpToPx()
                val verticalSpacing = 16.dpToPx()
                val position = parent.getChildAdapterPosition(view)
                val column = position % 2
                outRect.left = column * horizontalSpacing / 2
                outRect.right = horizontalSpacing - (column + 1) * horizontalSpacing / 2

                if (position >= 2) {
                    outRect.top = verticalSpacing
                }
                outRect.bottom = 0
            }
        })
    }

    private fun render(state: PlaylistState) {
        when {
            state.isLoading -> {}
            state.isError -> { showErrors() }
            state.isEmpty -> { showErrors() }
            else -> showContent(state.page)
        }
    }


    private fun showErrors() {
        binding.errorImageView.visibility = View.VISIBLE
        binding.errorTextView.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

    private fun showContent(playlists: Page<Playlist>) {
        binding.errorImageView.visibility = View.GONE
        binding.errorTextView.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        playlistAdapter.updatePage(playlists)
    }

    companion object {
        fun newInstance(): PlaylistFragment {
            return PlaylistFragment()
        }
    }
}