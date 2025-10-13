package com.practicum.playlistmaker.track.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.ui.AudioPlayerActivity
import com.practicum.playlistmaker.common.ui.BaseActivity
import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.track.domain.model.Track
import com.practicum.playlistmaker.track.domain.model.TrackState
import com.practicum.playlistmaker.track.presenter.SearchViewModel
import kotlin.getValue

class SearchActivity : BaseActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var trackAdapter: TrackAdapter
    private val viewModel: SearchViewModel by viewModels {
        SearchViewModel.getFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onInitElement()
        onInitAdapter()
        onInitListener()
        hideEmptyTrack()

        viewModel.observeState().observe(this) {
            render(it)
        }

        viewModel.observeStateOpenTrack.observe(this) { track ->
            track?.let {
                val intent = Intent(this, AudioPlayerActivity::class.java)
                    .apply { putExtra("TRACK", Gson().toJson(it)) }
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    private fun onInitElement() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_search)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBars.top)
            insets
        }
        arrowBackButton(R.id.arrow_back)
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
        viewModel.onSearchCleared()
        val ims = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        val currentView = currentFocus ?: View(this)
        ims?.hideSoftInputFromWindow(currentView.windowToken, 0)
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
}