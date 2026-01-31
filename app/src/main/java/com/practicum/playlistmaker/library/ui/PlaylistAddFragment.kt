package com.practicum.playlistmaker.library.ui

import androidx.appcompat.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.databinding.FragmentPlaylistAddBinding
import com.practicum.playlistmaker.library.domain.model.PlaylistAddState
import com.practicum.playlistmaker.library.presenter.PlaylistAddViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.updatePadding
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import java.io.File

class PlaylistAddFragment: Fragment() {
    private lateinit var binding: FragmentPlaylistAddBinding
    private val viewModel: PlaylistAddViewModel by viewModel()
    private lateinit var confirmDialog: AlertDialog

    private val playlistId: Long?
        get() = arguments
            ?.takeIf { it.containsKey(PLAYLIST_ID) }
            ?.getLong(PLAYLIST_ID)

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            viewModel.onSelectCoverPlaylist(it.toString())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPlaylistAddBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toBackArrowButton()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBars.top)
            insets
        }
        initConfirmDialog()
        initTextWatcher()
        toHandleBack()
        savePlaylist()

        if (playlistId == null) {
            binding.titlePage.text = getString(R.string.btn_playlist_add)
            binding.addPlaylistBtn.text = getString(R.string.playlist_btn_add)
        } else {
            binding.titlePage.text = getString(R.string.btn_playlist_edit)
            binding.addPlaylistBtn.text = getString(R.string.playlist_btn_edit)
        }

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.addPhoto.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        playlistId?.let {
            viewModel.loadPlaylist(it)
        }
    }
    private fun toHandleBack() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.observeState().value?.let { state ->
                        handleBack(state)
                    }
                }
            }
        )
    }

    private fun toBackArrowButton() {
        binding.arrowBack.setOnClickListener {
            viewModel.observeState().value?.let { state ->
                handleBack(state)
            }
        }
    }
    private fun render(state: PlaylistAddState) {
        binding.addPlaylistBtn.isEnabled = state.isAddPlaylistBtnEnabled
        if (binding.namePlaylist.text.toString() != state.namePlaylist)
            binding.namePlaylist.setText(state.namePlaylist)
        if (binding.descriptionPlaylist.text.toString() != state.descriptionPlaylist.orEmpty())
            binding.descriptionPlaylist.setText(state.descriptionPlaylist)
        val cover = state.coverPlaylistUri

        when {
            cover.isNullOrBlank() -> {
                binding.addPhoto.setImageDrawable(null)
                binding.iconAddPhotoIcon.isInvisible = false
            }

            cover.startsWith("content://") -> {
                binding.addPhoto.setImageURI(cover.toUri())
                binding.iconAddPhotoIcon.isInvisible = true
            }

            else -> {
                Glide.with(this)
                    .load(File(cover))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .centerCrop()
                    .into(binding.addPhoto)
                binding.iconAddPhotoIcon.isInvisible = true
            }
        }
        if (state.isSaveSuccess) {
            if (state.isEditPlaylist) {
                Toast.makeText(
                    requireContext(),
                    "Плейлист обновлён",
                    Toast.LENGTH_SHORT
                ).show()

                findNavController()
                    .getBackStackEntry(R.id.libraryFragment)
                    .savedStateHandle
                    .set("tab", 1)
                findNavController().popBackStack(
                    R.id.libraryFragment,
                    false
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    "Плейлист ${state.namePlaylist} создан",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().popBackStack()
            }
        }
    }

    private fun initTextWatcher() {
        binding.namePlaylist.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onChangedNamePlaylist(s?.toString())
            }

            override fun afterTextChanged(s: Editable?) = Unit
        })

        binding.descriptionPlaylist.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onChangedDescriptionPlaylist(s?.toString())
            }

            override fun afterTextChanged(s: Editable?) = Unit
        })
    }

    private fun initConfirmDialog() {
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.end_add_playlist)
            .setNeutralButton(R.string.cansel) { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton(R.string.ending) { dialog, _ ->
                dialog.dismiss()
                findNavController().popBackStack()
            }
            .create()
    }

    private fun handleBack(state: PlaylistAddState) {
        if (state.isChangeData)
            confirmDialog.show()
        else
            findNavController().popBackStack()
    }

    private fun savePlaylist() {
        binding.addPlaylistBtn.setOnClickListener {
            viewModel.savePlaylist()
        }
    }

    companion object {
        private const val PLAYLIST_ID = "PLAYLIST_ID"
        fun createArgs(id: Long?): Bundle =
            bundleOf(PLAYLIST_ID to id)
    }
}