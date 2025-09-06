package com.practicum.playlistmaker.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlistmaker.PLAYLIST_MAKER_PREFERENCES
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.activity.base.BaseActivity
import com.practicum.playlistmaker.component.Page
import com.practicum.playlistmaker.track.adapter.TrackAdapter
import com.practicum.playlistmaker.track.model.Track
import com.practicum.playlistmaker.track.repository.HistoryTrackRepository
import com.practicum.playlistmaker.track.repository.HistoryTrackRepositoryImpl
import com.practicum.playlistmaker.track.repository.ItunesTrackRepository
import com.practicum.playlistmaker.track.repository.TrackRepository
import org.w3c.dom.Text


class SearchActivity : BaseActivity() {
    private lateinit var handler: Handler
    private val searchRunnable = Runnable { searchTracks() }
    private val trackRepository: TrackRepository = ItunesTrackRepository()
    private val historyTrackRepository: HistoryTrackRepository =
        HistoryTrackRepositoryImpl()
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
        handler = Handler(Looper.getMainLooper())
        sharedPrefs =  getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE);
        errorLayout = findViewById(R.id.errorLayout)
        errorImageView = findViewById(R.id.errorImageView)
        errorTextView = findViewById(R.id.errorTextView)
        errorRefreshBtn = findViewById(R.id.refreshBtn)
        recyclerView = findViewById(R.id.trackRecyclerView)
        searchEditText = findViewById<EditText>(R.id.search_edit_text)
        titleHistorySearch = findViewById<TextView>(R.id.titleHistorySearch)
        clearHistorySearchBtn = findViewById<Button>(R.id.clearHistorySearchBtn)
        clearBtn = findViewById<ImageView>(R.id.clear_icon)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        trackAdapter = TrackAdapter(emptyList()) { track ->
            openAudioPlayer(track)
        }

        initSearchActivity()
        addListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun initSearchActivity() {
        hideEmptyTrack()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_search)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBars.top)
            insets
        }

        initObjectViews(searchEditText, clearBtn)
        implTextWatcher(searchEditText, clearBtn)
    }

    private fun addListener() {

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchDebounce()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            hideHistorySearch(
                if (hasFocus&&searchEditText.text.toString().isEmpty())
                    View.VISIBLE else View.GONE)

        }

        errorRefreshBtn.setOnClickListener {
            searchTracks()
        }

        clearHistorySearchBtn.setOnClickListener {
            historyTrackRepository.cleanHistory(sharedPrefs)
            hideHistorySearch(View.GONE)
        }
    }

    private fun initObjectViews(editText: EditText, clearBtn: ImageView) {
        arrowBackButton(R.id.arrow_back)
        editText.setText("")
        clearBtn.setOnClickListener { cleanText(editText, clearBtn) }
    }

    private fun implTextWatcher(editText: EditText, clearBtn: ImageView){
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearBtn.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                hideHistorySearch (if (s.isNullOrEmpty()) View.VISIBLE else View.GONE)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun searchTracks() {
        val query = searchEditText.text.toString().trim()
        showProgressBar(View.VISIBLE)
        recyclerView.adapter = trackAdapter
        trackRepository.getTracks(query) { page ->
            runOnUiThread {
                updateTrackRecyclerView(query, page)
            }
        }

    }

    private fun getHistory() {
        recyclerView.adapter = trackAdapter
        historyTrackRepository.getHistory( sharedPrefs, { page ->
            trackAdapter.updatePage(page)
            if (page.meta.count == 0)
                hideHistorySearch(View.GONE)
        })
        errorRefreshBtn.visibility = View.GONE
        errorLayout.visibility = View.GONE
        errorRefreshBtn.visibility = View.GONE

    }

    private fun updateTrackRecyclerView(query: String, page: Page<Track>) {
        trackAdapter.updatePage(page)
        showProgressBar(View.GONE)
        if (page.meta.errors.isNotEmpty())
            showErrors(query)
        else {
            if (page.data.isEmpty())
                showEmptyTrack()
            else
                hideEmptyTrack()
        }
    }

    private fun showErrors(query: String) {
        errorImageView.setImageResource(R.drawable.no_connect)
        errorTextView.text = getString(R.string.no_connect)
        errorRefreshBtn.visibility = View.VISIBLE
        errorLayout.visibility = View.VISIBLE
        lastQuery = query
    }
    private fun showEmptyTrack() {
        errorImageView.setImageResource(R.drawable.track_not_found)
        errorTextView.text = getString(R.string.track_not_found)
        errorLayout.visibility = View.VISIBLE
        errorRefreshBtn.visibility = View.GONE
    }
    private fun hideEmptyTrack() {
        errorRefreshBtn.visibility = View.GONE
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STRING, "")
    }

    private fun cleanText(editText: EditText, clearBtn: ImageView) {
        clearBtn.setOnClickListener{
            editText.setText("")
            hideEmptyTrack()
            trackAdapter.clearTracks()
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            val currentView = currentFocus ?: View(this)
            inputMethodManager?.hideSoftInputFromWindow(currentView.windowToken, 0)
        }
    }

    private fun openAudioPlayer(track: Track) {
        if (clickDebounce()) {
            historyTrackRepository.setHistory(sharedPrefs, track)
            val audioPlayerIntent = Intent(this, AudioPlayerActivity::class.java)
                .apply { putExtra("TRACK", Gson().toJson(track)) }
            startActivity(audioPlayerIntent)
        }
    }

    private fun showProgressBar(isVisibility: Int) {
            progressBar.visibility = isVisibility
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}