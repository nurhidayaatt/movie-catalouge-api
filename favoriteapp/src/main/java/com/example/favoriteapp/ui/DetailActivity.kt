package com.example.favoriteapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.favoriteapp.R
import com.example.favoriteapp.model.ResultMovie
import com.example.favoriteapp.model.ResultTv
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.layout_detail.*

class DetailActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_MOVIE = "extra_movie"
        const val EXTRA_TV = "extra_tv"
        const val BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w400"
        const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w154"
    }

    private var movie: ResultMovie? = null
    private var tv: ResultTv? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(toolbar_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        movie = intent.getParcelableExtra(EXTRA_MOVIE)
        tv = intent.getParcelableExtra(EXTRA_TV)

        when {
            movie != null -> {
                setMovie(movie!!)
            }
            tv != null -> {
                setTv(tv!!)
            }
        }
    }

    private fun setMovie(movie: ResultMovie) {
        collapsing_toolbar_detail.title = movie.title
        Glide.with(this).load(String.format(resources.getString(R.string.format_url), BACKDROP_BASE_URL, movie.backdropPath)).placeholder(R.drawable.placeholder).into(iv_toolbar)
        Glide.with(this).load(String.format(resources.getString(R.string.format_url), POSTER_BASE_URL, movie.posterPath)).placeholder(R.drawable.placeholder).into(iv_poster)
        iv_toolbar.contentDescription = String.format(getString(R.string.format_content_description), getString(R.string.photo), movie.title)
        iv_poster.contentDescription = String.format(getString(R.string.format_content_description), getString(R.string.photo), movie.title)
        tv_title.text = movie.title
        tv_release_date.text = movie.releaseDate

        vote_average.text = movie.voteAverage.toString()
        vote_count.text = movie.voteCount.toString()

        when {
            movie.overview==null -> {
                tv_overview.text = getString(R.string.empty_string)
            }
            movie.overview!!.trim().isEmpty() -> {
                tv_overview.text = getString(R.string.empty_string)
            }
            else -> {
                tv_overview.text = movie.overview
            }
        }
    }

    private fun setTv(tv: ResultTv) {
        collapsing_toolbar_detail.title = tv.name
        Glide.with(this).load(String.format(resources.getString(R.string.format_url), BACKDROP_BASE_URL, tv.backdropPath)).placeholder(R.drawable.placeholder).into(iv_toolbar)
        Glide.with(this).load(String.format(resources.getString(R.string.format_url), POSTER_BASE_URL, tv.posterPath)).placeholder(R.drawable.placeholder).into(iv_poster)
        iv_toolbar.contentDescription = String.format(getString(R.string.format_content_description), getString(R.string.photo), tv.name)
        iv_poster.contentDescription = String.format(getString(R.string.format_content_description), getString(R.string.photo), tv.name)
        tv_title.text = tv.name
        tv_release_date.text = tv.firstAirDate

        vote_average.text = tv.voteAverage.toString()
        vote_count.text = tv.voteCount.toString()

        when {
            tv.overview==null -> {
                tv_overview.text = getString(R.string.empty_string)
            }
            tv.overview!!.trim().isEmpty() -> {
                tv_overview.text = getString(R.string.empty_string)
            }
            else -> {
                tv_overview.text = tv.overview
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
