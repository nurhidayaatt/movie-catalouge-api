package com.example.moviecatalougeapi.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.moviecatalougeapi.R
import com.example.moviecatalougeapi.data.model.movie.ResultMovie
import com.example.moviecatalougeapi.data.model.tv.ResultTv
import com.example.moviecatalougeapi.util.widget.FavoriteWidget
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.layout_detail.*

class DetailActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_MOVIE = "extra_movie"
        const val EXTRA_TV = "extra_tv"
        const val BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w400"
        const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w154"
    }

    private lateinit var viewModel: DetailViewModel
    private var movie: ResultMovie? = null
    private var tv: ResultTv? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        showLoading(true)

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)

        setSupportActionBar(toolbar_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        movie = intent.getParcelableExtra(EXTRA_MOVIE)
        tv = intent.getParcelableExtra(EXTRA_TV)

        when {
            movie != null -> {
                viewModel.getFavoriteMovieId(movie!!.id)
                setMovie(movie!!)
            }
            tv != null -> {
                viewModel.getFavoriteTvId(tv!!.id)
                setTv(tv!!)
            }
            else -> {
                showLoading(false)
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

        viewModel.movieId.observe(this, Observer { movieid ->
            if (movieid==movie.id) {
                favorite.setImageResource(R.drawable.ic_favorite_pink_24dp)
                favorite.setOnClickListener {
                    showLoading(true)
                    viewModel.deleteFavorite(movie)
                    snackbar(getString(R.string.favorite_deleted))
                    viewModel.getFavoriteMovieId(movie.id)
                    sendUpdateFavoriteList(this)
                }
            }else {
                favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                favorite.setOnClickListener {
                    showLoading(true)
                    viewModel.addFavorite(movie)
                    snackbar(getString(R.string.favorite_added))
                    viewModel.getFavoriteMovieId(movie.id)
                    sendUpdateFavoriteList(this)
                }
            }
            showLoading(false)
        })
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

        viewModel.tvId.observe(this, Observer { tvid ->
            if (tvid==tv.id) {
                favorite.setImageResource(R.drawable.ic_favorite_pink_24dp)
                favorite.setOnClickListener{
                    showLoading(true)
                    viewModel.deleteFavorite(tv)
                    snackbar(getString(R.string.favorite_deleted))
                    viewModel.getFavoriteTvId(tv.id)
                    sendUpdateFavoriteList(this)
                }
            }else {
                favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                favorite.setOnClickListener{
                    showLoading(true)
                    viewModel.addFavorite(tv)
                    snackbar(getString(R.string.favorite_added))
                    viewModel.getFavoriteTvId(tv.id)
                    sendUpdateFavoriteList(this)
                }
            }
            showLoading(false)
        })
    }

    private fun sendUpdateFavoriteList(context: Context) {
        val intent = Intent(context, FavoriteWidget::class.java)
        intent.action = FavoriteWidget.UPDATE_WIDGET
        context.sendBroadcast(intent)
    }

    private fun snackbar(string: String) {
        Snackbar.make(coordinator, string, Snackbar.LENGTH_LONG).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}
