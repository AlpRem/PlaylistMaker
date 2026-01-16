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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R

class PlaylistAddFragment: Fragment() {
    private lateinit var binding: FragmentPlaylistAddBinding
    private val viewModel: PlaylistAddViewModel by viewModel()
    private lateinit var confirmDialog: AlertDialog

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
        initConfirmDialog()
        initTextWatcher()
        toBackArrowButton()
        toHandleBack()
        savePlaylist()
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.addPhoto.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val imeBottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            val systemBottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            val rootPaddingBottom = binding.root.paddingBottom
            binding.bottomSpacer.layoutParams.height =
                if (imeVisible) imeBottom - rootPaddingBottom else systemBottom
            binding.bottomSpacer.requestLayout()
            insets
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
        if (state.isSaveSuccess) {
            Toast.makeText(
                requireContext(),
                "Плейлист ${state.namePlaylist} создан",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().previousBackStackEntry?.savedStateHandle?.set("tab", 1)
            findNavController().popBackStack(R.id.libraryFragment, false)
        }

        state.coverPlaylistUri?.let {
            binding.addPhoto.setImageURI(it.toUri())
            binding.iconAddPhotoIcon.isInvisible = true
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
            .setTitle("Завершить создание плейлиста?")
            .setNeutralButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("Завершить") { dialog, _ ->
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
}