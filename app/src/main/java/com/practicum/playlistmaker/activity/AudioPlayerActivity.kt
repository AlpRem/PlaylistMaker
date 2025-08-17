package com.practicum.playlistmaker.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.activity.base.BaseActivity
import com.practicum.playlistmaker.track.adapter.TrackAdapter
import com.practicum.playlistmaker.track.model.Track
import com.practicum.playlistmaker.util.dpToPx

class AudioPlayerActivity  : BaseActivity() {

    private lateinit var cover: ImageView
    private lateinit var trackName: TextView
    private lateinit var trackAuthor: TextView
    private lateinit var trackTimeTitle: TextView
    private lateinit var trackTimeValue: TextView
    private lateinit var collectionNameTitle: TextView
    private lateinit var collectionNameValue: TextView
    private lateinit var releaseDateTitle: TextView
    private lateinit var releaseDateValue: TextView
    private lateinit var primaryGenreNameTitle: TextView
    private lateinit var primaryGenreNameValue: TextView
    private lateinit var countryTitle: TextView
    private lateinit var countryValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        val trackGson = intent.getStringExtra("TRACK") ?: ""
        val track =  Gson().fromJson(trackGson ,Track::class.java)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        cover = findViewById(R.id.cover)
        trackName = findViewById(R.id.trackName)
        trackAuthor = findViewById(R.id.trackAuthor)
        trackTimeTitle = findViewById(R.id.track_time_title)
        trackTimeValue = findViewById(R.id.track_time_value)
        collectionNameTitle = findViewById(R.id.collection_name_title)
        collectionNameValue = findViewById(R.id.collection_name_value)
        releaseDateTitle = findViewById(R.id.release_date_title)
        releaseDateValue = findViewById(R.id.release_date_value)
        primaryGenreNameTitle = findViewById(R.id.primary_genre_name_title)
        primaryGenreNameValue = findViewById(R.id.primary_genre_name_value)
        countryTitle = findViewById(R.id.country_title)
        countryValue = findViewById(R.id.country_value)

        onInitData(track)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_audio_player)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBars.top)
            insets
        }
        arrowBackButton(R.id.arrow_back)
    }

    private fun onInitData(track: Track) {
        if (!track.artworkUrl100.isEmpty())
            Glide.with(this)
                .load(getHeightArtworkUrl(track.artworkUrl100))
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .transform(
                    MultiTransformation(
                        CenterCrop(),
                        RoundedCorners(8.dpToPx())
                    )
                )
                .into(cover)
        trackName.text = track.trackName
        trackAuthor.text = track.artistName
        gerContentInLineTextViews(track.trackTime, trackTimeTitle, trackTimeValue)
        gerContentInLineTextViews(track.collectionName, collectionNameTitle, collectionNameValue)
        gerContentInLineTextViews(track.releaseDate, releaseDateTitle, releaseDateValue)
        gerContentInLineTextViews(track.primaryGenreName, primaryGenreNameTitle, primaryGenreNameValue)
        gerContentInLineTextViews(track.country, countryTitle, countryValue)
    }

    fun getHeightArtworkUrl(artworkUrl100: String): String {
        return if (artworkUrl100.endsWith("100x100bb.jpg")) {
            artworkUrl100.substring(0, artworkUrl100.length - "100x100bb.jpg".length) + "512x512bb.jpg"
        } else {
            artworkUrl100
        }
    }

    private fun gerContentInLineTextViews(content: String, title: TextView, value: TextView) {
        if (content.isEmpty()) {
            title.visibility = View.GONE
            value.visibility = View.GONE
        } else {
            title.visibility = View.VISIBLE
            value.visibility = View.VISIBLE
            value.text = content;
        }
    }
}