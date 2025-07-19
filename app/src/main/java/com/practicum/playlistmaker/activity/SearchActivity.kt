package com.practicum.playlistmaker.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.activity.base.BaseActivity
import com.practicum.playlistmaker.component.Page
import com.practicum.playlistmaker.track.adapter.TrackAdapter
import com.practicum.playlistmaker.track.model.Track
import com.practicum.playlistmaker.track.repository.ItunesTrackRepository
import com.practicum.playlistmaker.track.repository.TrackRepository


class SearchActivity : BaseActivity() {
    private val trackRepository: TrackRepository = ItunesTrackRepository()
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var errorLayout: LinearLayout
    private lateinit var errorImageView: ImageView
    private lateinit var errorTextView: TextView
    private lateinit var errorRefreshBtn: Button
    private var lastQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        errorLayout = findViewById(R.id.errorLayout)
        errorImageView = findViewById(R.id.errorImageView)
        errorTextView = findViewById(R.id.errorTextView)
        errorRefreshBtn = findViewById(R.id.refreshBtn)
        recyclerView = findViewById(R.id.trackRecyclerView)
        trackAdapter = TrackAdapter(emptyList())
        hideEmptyTrack()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_search)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBars.top)
            insets
        }

        val searchEditText = findViewById<EditText>(R.id.search_edit_text)
        val clearBtn = findViewById<ImageView>(R.id.clear_icon)
        initObjectViews(searchEditText, clearBtn)
        implTextWatcher(searchEditText, clearBtn)

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE&&
                searchEditText.text.toString().isNotEmpty()) {
                searchTracks(searchEditText.text.toString())
                true
            }
            false
        }

        errorRefreshBtn.setOnClickListener {
            searchTracks(searchEditText.text.toString())
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
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun searchTracks(query: String) {
        recyclerView.adapter = trackAdapter
        trackRepository.getTracks(query) { page ->
            runOnUiThread {
                updateTrackRecyclerView(query, page)
            }
        }
    }

    private fun updateTrackRecyclerView(query: String, page: Page<Track>) {
        trackAdapter.updatePage(page)
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
    }
    private fun hideEmptyTrack() {
        errorRefreshBtn.visibility = View.GONE
        errorLayout.visibility = View.GONE
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

    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
    }


}