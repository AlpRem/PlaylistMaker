package com.practicum.playlistmaker.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.activity.base.BaseActivity
import com.practicum.playlistmaker.track.adapter.TrackAdapter
import com.practicum.playlistmaker.track.repository.MockTrackRepository
import com.practicum.playlistmaker.track.repository.TrackRepository

class SearchActivity : BaseActivity() {
    private var searchString: String = ""
    private val trackRepository: TrackRepository = MockTrackRepository()

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

        searchString = savedInstanceState?.getString(SEARCH_STRING) ?: ""
        val searchEditText = findViewById<EditText>(R.id.search_edit_text)
        val clearBtn = findViewById<ImageView>(R.id.clear_icon)

        initObjectViews(searchEditText, clearBtn)
        implTextWatcher(searchEditText, clearBtn);

        recyclerView = findViewById(R.id.trackRecyclerView)
        trackAdapter = TrackAdapter(trackRepository.getTracks())
        recyclerView.adapter = trackAdapter

    }

    private fun initObjectViews(editText: EditText, clearBtn: ImageView) {
        arrowBackButton(R.id.arrow_back)
        editText.setText(searchString)
        clearBtn.setOnClickListener { cleanText(editText, clearBtn) }
    }


    private fun implTextWatcher(editText: EditText, clearBtn: ImageView){
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearBtn.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                searchString = if (s.isNullOrEmpty()) s.toString() else "";
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STRING, searchString)
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