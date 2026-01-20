package com.practicum.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmaker.library.domain.model.FavoritesState
import com.practicum.playlistmaker.library.presenter.FavoritesViewModel
import com.practicum.playlistmaker.player.ui.AudioPlayerFragment
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.TrackAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment: Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private lateinit var trackAdapter: TrackAdapter
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by viewModel()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onInitAdapter()

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onInitAdapter() {
        trackAdapter = TrackAdapter(emptyList()) { track ->
            findNavController().navigate(
                R.id.action_libraryFragment_to_audioPlayerFragment,
                AudioPlayerFragment.createArgs(track)
            )
        }
        binding.favoritesRecyclerView.adapter = trackAdapter
    }

    private fun render(state: FavoritesState) {
        when {
            state.isLoading -> showLoading()
            state.isEmpty -> showEmpty()
            else -> showContent(state.page)
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.favoritesRecyclerView.visibility = View.GONE
        binding.emptyLayout.visibility = View.GONE
    }

    private fun showEmpty() {
        binding.progressBar.visibility = View.GONE
        binding.favoritesRecyclerView.visibility = View.GONE
        binding.emptyLayout.visibility = View.VISIBLE
    }

    private fun showContent(page: Page<Track>) {
        trackAdapter.updatePage(page)
        binding.progressBar.visibility = View.GONE
        binding.emptyLayout.visibility = View.GONE
        binding.favoritesRecyclerView.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }
}