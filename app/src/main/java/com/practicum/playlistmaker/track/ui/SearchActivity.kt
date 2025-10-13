package com.practicum.playlistmaker.track.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.ui.AudioPlayerActivity
import com.practicum.playlistmaker.common.ui.BaseActivity
import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.track.domain.model.Track
import com.practicum.playlistmaker.track.domain.model.TrackState
import com.practicum.playlistmaker.track.presenter.SearchViewModel
import kotlin.getValue

class SearchActivity : BaseActivity() {
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var errorLayout: LinearLayout
    private lateinit var errorImageView: ImageView
    private lateinit var errorTextView: TextView
    private lateinit var errorRefreshBtn: Button
    private lateinit var searchEditText: EditText
    private lateinit var titleHistorySearch: TextView
    private lateinit var clearHistorySearchBtn: Button
    private lateinit var clearBtn: ImageView
    private lateinit var progressBar: ProgressBar

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModel.getFactory(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
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
        errorLayout = findViewById(R.id.errorLayout)
        errorImageView = findViewById(R.id.errorImageView)
        errorTextView = findViewById(R.id.errorTextView)
        errorRefreshBtn = findViewById(R.id.refreshBtn)
        recyclerView = findViewById(R.id.trackRecyclerView)
        searchEditText = findViewById(R.id.search_edit_text)
        titleHistorySearch = findViewById(R.id.titleHistorySearch)
        clearHistorySearchBtn = findViewById(R.id.clearHistorySearchBtn)
        clearBtn = findViewById(R.id.clear_icon)
        progressBar = findViewById(R.id.progressBar)

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
        recyclerView.adapter = trackAdapter
    }


    private fun onInitListener() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearBtn.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                viewModel.searchDebounce(changedText = s?.toString() ?: "")
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            viewModel.onSearchFocusChange(hasFocus, searchEditText.text)
        }

        clearBtn.setOnClickListener { clearInput() }
        clearHistorySearchBtn.setOnClickListener {
            viewModel.clearHistory()
        }
    }
    private fun showLoading(state: TrackState) {
        showProgressBar(View.VISIBLE)
        errorLayout.visibility = View.GONE
        titleHistorySearch.visibility = View.GONE
        clearHistorySearchBtn.visibility = View.GONE
        trackAdapter.updatePage(state.page)
    }

    private fun showContent(page: Page<Track>, isHistory: Boolean) {
        trackAdapter.updatePage(page)
        showProgressBar(View.GONE)
        errorLayout.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        titleHistorySearch.visibility = if (isHistory) View.VISIBLE else View.GONE
        clearHistorySearchBtn.visibility = if (isHistory) View.VISIBLE else View.GONE
    }

    private fun showErrors() {
        showProgressBar(View.GONE)
        errorImageView.setImageResource(R.drawable.no_connect)
        errorTextView.text = getString(R.string.no_connect)
        errorRefreshBtn.visibility = View.VISIBLE
        errorLayout.visibility = View.VISIBLE
    }

    private fun showEmptyTrack() {
        showProgressBar(View.GONE)
        errorImageView.setImageResource(R.drawable.track_not_found)
        errorTextView.text = getString(R.string.track_not_found)
        errorLayout.visibility = View.VISIBLE
        errorRefreshBtn.visibility = View.GONE
    }

    private fun hideEmptyTrack() {
        errorRefreshBtn.visibility = View.GONE
        errorLayout.visibility = View.GONE
    }

    private fun clearInput() {
        viewModel.onSearchCleared()
        val ims = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        val currentView = currentFocus ?: View(this)
        ims?.hideSoftInputFromWindow(currentView.windowToken, 0)
    }
    private fun showProgressBar(isVisibility: Int) {
        progressBar.visibility = isVisibility
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