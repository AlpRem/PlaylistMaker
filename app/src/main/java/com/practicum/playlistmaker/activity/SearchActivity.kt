package com.practicum.playlistmaker.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.activity.base.BaseActivity
import com.practicum.playlistmaker.component.Error
import com.practicum.playlistmaker.component.Page
import com.practicum.playlistmaker.itunes.ITunesResponse
import com.practicum.playlistmaker.itunes.ItunesClient
import com.practicum.playlistmaker.itunes.ItunesService
import com.practicum.playlistmaker.track.adapter.TrackAdapter
import com.practicum.playlistmaker.track.model.Track
import com.practicum.playlistmaker.track.repository.ItunesTrackRepository
import com.practicum.playlistmaker.track.repository.MockTrackRepository
import com.practicum.playlistmaker.track.repository.TrackRepository
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class SearchActivity : BaseActivity() {
    private val trackRepository: TrackRepository = ItunesTrackRepository()
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_search)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBars.top)
            insets
        }

        val searchEditText = findViewById<EditText>(R.id.search_edit_text)
        val clearBtn = findViewById<ImageView>(R.id.clear_icon)
        initObjectViews(searchEditText, clearBtn)
        implTextWatcher(searchEditText, clearBtn);
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTracks(searchEditText.text.toString());
                true
            }
            false
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
        recyclerView = findViewById(R.id.trackRecyclerView)
        trackAdapter = TrackAdapter(emptyList())
        recyclerView.adapter = trackAdapter
        trackRepository.getTracks(query) { page ->
            runOnUiThread {
                updateTrackRecyclerView(page)
            }
        }
    }

    private fun updateTrackRecyclerView(page: Page<Track>) {
        trackAdapter.updatePage(page)
        if (page.meta.errors.isNotEmpty())
            showErrors(page.meta.errors)
        else {
            if (page.data.isEmpty())
                showEmptyTrack()
            else
                hideEmptyTrack()
        }
    }

    private fun showErrors(errors: List<Error>) {}
    private fun showEmptyTrack() {}
    private fun hideEmptyTrack() {}


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STRING, "")
    }

    private fun cleanText(editText: EditText, clearBtn: ImageView) {
        clearBtn.setOnClickListener{
            editText.setText("");
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            val currentView = currentFocus ?: View(this)
            inputMethodManager?.hideSoftInputFromWindow(currentView.windowToken, 0)
        }
    }

    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
    }


}