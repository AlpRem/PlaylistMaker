package com.practicum.playlistmaker.ui.track

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.PLAYLIST_MAKER_PREFERENCES
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.audioPlayer.AudioPlayerActivity
import com.practicum.playlistmaker.ui.base.BaseActivity
import com.practicum.playlistmaker.component.Page
import com.practicum.playlistmaker.data.repository.ItunesTrackRepository
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.api.HistoryTrackRepository
import com.practicum.playlistmaker.data.repository.HistoryTrackRepositoryImpl
import com.practicum.playlistmaker.domain.api.HistoryTrackInteractor

class SearchActivity : BaseActivity() {


    private val tracksInteractor: TracksInteractor = Creator.provideTracksInteractor()
    private lateinit var historyTrackInteractor: HistoryTrackInteractor
    private lateinit var handler: Handler
    private val searchRunnable = Runnable { performSearch() }

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
    private lateinit var progressBar: ProgressBar;
    private lateinit var sharedPrefs: SharedPreferences;

    private var lastQuery: String = ""
    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        historyTrackInteractor = Creator.provideHistoryTrackInteractor(applicationContext)
        handler = Handler(Looper.getMainLooper())
        sharedPrefs =  getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE);

        onInitElement()
        onInitAdapter()
        onInitListener()
        hideEmptyTrack()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }


    // === code from presenter ===
    // === init element ===
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
        searchEditText.setText("")
    }

    private fun onInitAdapter() {
        trackAdapter = TrackAdapter(emptyList()) { track ->
            openAudioPlayer(track)
        }
        recyclerView.adapter = trackAdapter
    }


    private fun onInitListener() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearBtn.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                hideHistorySearch(if (s.isNullOrEmpty()&&searchEditText.hasFocus()) View.VISIBLE else View.GONE)
                searchDebounce()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            hideHistorySearch(if (hasFocus && searchEditText.text.toString().isEmpty()) View.VISIBLE else View.GONE)
        }

        clearBtn.setOnClickListener { clearInput() }
        errorRefreshBtn.setOnClickListener { performSearch() }
        clearHistorySearchBtn.setOnClickListener {
            historyTrackInteractor.clearHistory()
            hideHistorySearch(View.GONE)
        }
    }

    // === search track ===
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun performSearch() {
        val query = searchEditText.text.toString().trim()
        if (query.isEmpty()) {
            hideEmptyTrack()
            hideError()
            getHistory()
            return
        }
        showLoading()
        tracksInteractor.searchTracks(query, object : TracksInteractor.TracksConsumer {
            override fun consume(foundTracks: Page<Track>) {
                updateTrackRecyclerView(query, foundTracks)
            }
        })
    }

    private fun updateTrackRecyclerView(query: String, page: Page<Track>) {
        runOnUiThread {
            trackAdapter.updatePage(page)
            showProgressBar(View.GONE)
            if (page.meta.errors.isNotEmpty()) {
                showErrors(query)
            } else {
                if (page.data.isEmpty()) showEmptyTrack() else showContent()
            }
        }
    }


    // === history ===
    private fun getHistory() {
        historyTrackInteractor.getHistory { page ->
            runOnUiThread {
                trackAdapter.updatePage(page)
                if (page.meta.count == 0)
                    hideHistorySearch(View.GONE)
                else
                    showHistory(page)
            }
        }
        hideError()
    }


    // === status ===
    private fun showLoading() {
        showProgressBar(View.VISIBLE)
        errorLayout.visibility = View.GONE
    }

    private fun showContent() {
        showProgressBar(View.GONE)
        errorLayout.visibility = View.GONE
        lastQuery = ""
    }

    private fun showErrors(query: String) {
        showProgressBar(View.GONE)
        errorImageView.setImageResource(R.drawable.no_connect)
        errorTextView.text = getString(R.string.no_connect)
        errorRefreshBtn.visibility = View.VISIBLE
        errorLayout.visibility = View.VISIBLE
        lastQuery = query
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

    private fun hideError() {
        errorLayout.visibility = View.GONE
        errorRefreshBtn.visibility = View.GONE
    }


    private fun showHistory(page: Page<Track>) {
        trackAdapter.updatePage(page)
        titleHistorySearch.visibility = View.VISIBLE
        clearHistorySearchBtn.visibility = View.VISIBLE
        errorLayout.visibility = View.GONE
    }

    private fun hideHistorySearch(statusVisible: Int) {
        titleHistorySearch.visibility = statusVisible;
        clearHistorySearchBtn.visibility = statusVisible;
        if (statusVisible == View.VISIBLE)
            getHistory()
        else
            trackAdapter.clearTracks()
    }

    // === util ===
    private fun clearInput() {
        searchEditText.setText("")
        hideEmptyTrack()
        trackAdapter.clearTracks()
        val ims = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        val currentView = currentFocus ?: View(this)
        ims?.hideSoftInputFromWindow(currentView.windowToken, 0)
    }

    private fun openAudioPlayer(track: Track) {
        if (clickDebounce()) {
            historyTrackInteractor.saveTrack(track)
            val audioPlayerIntent = Intent(this, AudioPlayerActivity::class.java)
                .apply { putExtra("TRACK", Gson().toJson(track)) }
            startActivity(audioPlayerIntent)
        }
    }

    private fun showProgressBar(isVisibility: Int) {
        progressBar.visibility = isVisibility
    }

    private fun clickDebounce(): Boolean {
        if (!isClickAllowed) return false
        isClickAllowed = false
        handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        return true
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}