package com.practicum.playlistmaker.search.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.ui.AudioPlayerActivity
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.domain.model.TrackState
import com.practicum.playlistmaker.search.presenter.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SearchFragment: Fragment() {
    companion object {
        const val TAG = "SearchFragment"
    }

    private lateinit var binding: FragmentSearchBinding
    private lateinit var trackAdapter: TrackAdapter
    private val viewModel: SearchViewModel by viewModel()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onInitElement()
        onInitAdapter()
        onInitListener()
        hideEmptyTrack()

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observeStateOpenTrack.observe(viewLifecycleOwner) { track ->
            track?.let {
                val intent = Intent(requireContext(), AudioPlayerActivity::class.java)
                    .apply { putExtra("TRACK", Gson().toJson(it)) }
                startActivity(intent)
            }
        }
    }


    private fun onInitElement() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBars.top)
            insets
        }
        arrowBackButton()
    }

    private fun onInitAdapter() {
        trackAdapter = TrackAdapter(emptyList()) { track ->
            viewModel.onOpenAudioPlayer(track)
        }
        binding.trackRecyclerView.adapter = trackAdapter
    }


    private fun onInitListener() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.clearIcon.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                viewModel.searchDebounce(changedText = s?.toString() ?: "")
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            viewModel.onSearchFocusChange(hasFocus, binding.searchEditText.text)
        }

        binding.clearIcon.setOnClickListener { clearInput() }
        binding.clearHistorySearchBtn.setOnClickListener {
            viewModel.clearHistory()
        }
    }
    private fun showLoading(state: TrackState) {
        showProgressBar(View.VISIBLE)
        binding.errorLayout.visibility = View.GONE
        binding.titleHistorySearch.visibility = View.GONE
        binding.clearHistorySearchBtn.visibility = View.GONE
        trackAdapter.updatePage(state.page)
    }

    private fun showContent(page: Page<Track>, isHistory: Boolean) {
        trackAdapter.updatePage(page)
        showProgressBar(View.GONE)
        binding.errorLayout.visibility = View.GONE
        binding.trackRecyclerView.visibility = View.VISIBLE
        binding.titleHistorySearch.visibility = if (isHistory) View.VISIBLE else View.GONE
        binding.clearHistorySearchBtn.visibility = if (isHistory) View.VISIBLE else View.GONE
    }

    private fun showErrors() {
        showProgressBar(View.GONE)
        binding.errorImageView.setImageResource(R.drawable.no_connect)
        binding.errorTextView.text = getString(R.string.no_connect)
        binding.refreshBtn.visibility = View.VISIBLE
        binding.errorLayout.visibility = View.VISIBLE
    }

    private fun showEmptyTrack() {
        showProgressBar(View.GONE)
        binding.errorImageView.setImageResource(R.drawable.track_not_found)
        binding.errorTextView.text = getString(R.string.track_not_found)
        binding.errorLayout.visibility = View.VISIBLE
        binding.refreshBtn.visibility = View.GONE
    }

    private fun hideEmptyTrack() {
        binding.refreshBtn.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
    }

    private fun clearInput() {
        binding.searchEditText.text?.clear()
        viewModel.onSearchCleared()
        val ims = requireContext().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        ims?.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
    }
    private fun showProgressBar(isVisibility: Int) {
        binding.progressBar.visibility = isVisibility
    }

    private fun render(state: TrackState) {
        when {
            state.isLoading -> showLoading(state)
            state.isError -> showErrors()
            state.isEmpty -> showEmptyTrack()
            else -> showContent(state.page, state.isHistory)
        }
    }

    private fun arrowBackButton() {
        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}