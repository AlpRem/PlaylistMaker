package com.practicum.playlistmaker.library.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.databinding.FragmentPlaylistAddBinding
import com.practicum.playlistmaker.library.domain.model.PlaylistAddState
import com.practicum.playlistmaker.library.presenter.PlaylistAddViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class PlaylistAddFragment: Fragment() {
    private lateinit var binding: FragmentPlaylistAddBinding
    private val viewModel: PlaylistAddViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPlaylistAddBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toBackArrowButton()
        initTextWatcher()
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
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
    private fun toBackArrowButton() {
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun render(state: PlaylistAddState) {
        binding.addPlaylistBtn.isEnabled = state.isAddPlaylistBtnEnabled
    }

    private fun initTextWatcher() {
        binding.namePlaylist.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onChangedNamePlaylist(s?.toString())
            }

            override fun afterTextChanged(s: Editable?) = Unit
        })
    }
}