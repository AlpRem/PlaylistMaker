package com.practicum.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.common.util.dpToPx
import com.practicum.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.library.domain.model.PlaylistAddState
import com.practicum.playlistmaker.library.domain.model.PlaylistDetailsState
import com.practicum.playlistmaker.library.presenter.PlaylistDetailsViewModel
import com.practicum.playlistmaker.player.ui.AudioPlayerAdapter
import com.practicum.playlistmaker.player.ui.AudioPlayerFragment
import com.practicum.playlistmaker.search.domain.model.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PlaylistDetailsFragment: Fragment() {
    private lateinit var binding: FragmentPlaylistDetailsBinding
    private val viewModel: PlaylistDetailsViewModel by viewModel()
    private lateinit var playlistDetailsAdapter: PlaylistDetailsAdapter

    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var confirmDialog: AlertDialog


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        toBackArrowButton()
        initMenuBottomSheet()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBars.top)
            insets
        }

        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state)
            if (state.completerDelete) {
                findNavController().popBackStack()
                viewModel.changeFlagCompleterDelete()
            }
        }
        viewModel.loadPlaylist(requireArguments().getLong(PLAYLIST_ID))
        initBottomSheetPeekHeight()

        binding.playlistIconShared.setOnClickListener { viewModel.shareApp() }

        binding.playlistIconMenu.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.sharing.setOnClickListener {
            viewModel.shareApp()
        }

        binding.delete.setOnClickListener {
            viewModel.delete()
        }
    }

    private fun toBackArrowButton() {
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun render(state: PlaylistDetailsState) {
        state.playerTrack?.let { track ->
            findNavController().navigate(
                R.id.action_playlistDetailsFragment_to_audioPlayerFragment,
                AudioPlayerFragment.createArgs(track)
            )
            viewModel.resetOpenTrack()
        }
        when {
            state.isLoading -> showLoading()
            state.isEmpty -> showEmpty()
            state.playlist != null -> {
                showPlaylist(state.playlist, state.totalDurationMillis)
                playlistDetailsAdapter.updatePage(Page.of(state.tracks))
            }
        }
    }
    private fun showLoading() {
    }

    private fun showEmpty() {
    }

    private fun showPlaylist(playlist: Playlist, durationMillis: Long) {
        if (playlist.image.isBlank()) {
            binding.playlistImage.setImageResource(R.drawable.placeholder)
            binding.imagePlaylistSm.setImageResource(R.drawable.placeholder)
        }
        else {
            Glide.with(this)
                .load(File(playlist.image))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .centerCrop()
                .into(binding.playlistImage)
            Glide.with(this)
                .load(File(playlist.image))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .centerCrop()
                .into(binding.imagePlaylistSm)
        }
        binding.name.text = playlist.name
        binding.namePlaylistSm.text = playlist.name
        binding.description.text = playlist.description
        val minutes = durationMillis / 1000 / 60
        binding.time.text = this.resources.getQuantityString(
            R.plurals.time_count,
            minutes.toInt() ,
            minutes
        )

        binding.countTrack.text = this.resources.getQuantityString(
            R.plurals.tracks_count,
            playlist.countTracks,
            playlist.countTracks
        )
        binding.countTrackSm.text = this.resources.getQuantityString(
            R.plurals.tracks_count,
            playlist.countTracks,
            playlist.countTracks
        )
    }

    private fun initRecycler() {
        playlistDetailsAdapter = PlaylistDetailsAdapter(
            tracks = emptyList(),
            onTrackClick = { track ->
                viewModel.onOpenAudioPlayer(track)
            },
            onTrackLongClick = { track ->
                showDialog(track)
            }
        )
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = playlistDetailsAdapter
        }
    }

    private fun showDialog(track: Track) {
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_track_dialog)
            .setNeutralButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton(R.string.yes) { dialog, _ ->
                dialog.dismiss()
                viewModel.delete(track)
            }
            .create()

        confirmDialog.show()
    }

    private fun initBottomSheetPeekHeight() {
        val bottomSheet = binding.standardBottomSheet
        val content = binding.content
        bottomSheet.post {
            val behavior = BottomSheetBehavior.from(bottomSheet)
            val rootHeight = binding.root.height
            val contentBottom = content.bottom
            val peekHeight = rootHeight - contentBottom
            behavior.peekHeight = peekHeight
            behavior.isHideable = false
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun initMenuBottomSheet() {
        menuBottomSheetBehavior =
            BottomSheetBehavior.from(binding.menuBottomSheet).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

        menuBottomSheetBehavior.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    binding.overlay.visibility =
                        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                            View.GONE
                        } else {
                            View.VISIBLE
                        }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
            }
        )

        binding.overlay.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    companion object {
        private const val PLAYLIST_ID = "PLAYLIST_ID"
        fun createArgs(id: Long): Bundle =
            bundleOf(PLAYLIST_ID to id)
    }
}