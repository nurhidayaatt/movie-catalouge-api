package com.example.moviecatalougeapi.ui.search

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecatalougeapi.R
import com.example.moviecatalougeapi.data.adapter.MovieAdapter
import com.example.moviecatalougeapi.data.adapter.TvAdapter
import com.example.moviecatalougeapi.data.model.movie.ResultMovie
import com.example.moviecatalougeapi.data.model.tv.ResultTv
import com.example.moviecatalougeapi.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*
import kotlin.collections.ArrayList

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_SPEECH_INPUT = 100
        const val EXTRA = "extra"
        const val MOVIE = "movie"
        const val TV = "tv"
        const val MOVIE_FAVORITE = "movie_favorite"
        const val TV_FAVORITE = "tv_favorite"
    }

    private lateinit var movieAdapter: MovieAdapter
    private lateinit var tvAdapter: TvAdapter
    private lateinit var viewModel: SearchViewModel

    private var extra: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        showLoading(false)

        setSupportActionBar(toolbar_search)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        extra = intent.getStringExtra(EXTRA)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        initSpeakToText()

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        input_search.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        input_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0 != null) {
                    closeKeyboard()
                    showLoading(true)
                    when {
                        extra.equals(MOVIE) -> {
                            getMovie(p0)
                        }
                        extra.equals(TV) -> {
                            getTv(p0)
                        }
                        extra.equals(MOVIE_FAVORITE) -> {
                            getFavoriteMovie(p0)
                        }
                        extra.equals(TV_FAVORITE) -> {
                            getFavoriteTv(p0)
                        }
                        else -> {
                            finish()
                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })
    }

    private fun initSpeakToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        if (intent.resolveActivity(this.packageManager) != null) {
            voice.visibility = View.VISIBLE
            voice.setOnClickListener{
                speakToText()
            }
        } else {
            voice.visibility = View.GONE
        }

    }

    private fun speakToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Coba Ucapkan Sesuatu")
        try {
            startActivityForResult(
                intent,
                REQUEST_CODE_SPEECH_INPUT
            )
        } catch (e: Exception) {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == Activity.RESULT_OK && data != null) {
            val text = data.getStringArrayListExtra(
                RecognizerIntent.EXTRA_RESULTS
            )

            if (text != null) {
                input_search.setQuery(text[0], true)
                closeKeyboard()
            }
        }
    }

    private fun getMovie(search: String) {
        if (Locale.getDefault().language.toString() == "in") {
            viewModel.getMovie("id", search)
        } else {
            viewModel.getMovie("en-US", search)
        }
        showMovie()
    }

    private fun showMovie() {
        movieAdapter = MovieAdapter()
        recycler_search.layoutManager = LinearLayoutManager(this)
        recycler_search.adapter = movieAdapter

        viewModel.response.observe(this, Observer { response ->
            if (response!=null) {
                showLoading(false)
                error.text = response
                movieAdapter.setData()
            }else {
                viewModel.movieItem.observe(this, Observer { movie ->
                    if (movie!=null) {
                        error.text = null
                        movieAdapter.setData(movie)
                        showLoading(false)
                    }else {
                        movieAdapter.setData()
                        error.text = getString(R.string.data_null)
                        showLoading(false)
                    }
                })
            }
        })

        movieAdapter.setOnItemClickCallback(object : MovieAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ResultMovie) {
                val intent = Intent(this@SearchActivity, DetailActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                intent.putExtra(DetailActivity.EXTRA_MOVIE, data)
                startActivity(intent)
            }
        })
    }

    private fun getTv(search: String) {
        if (Locale.getDefault().language.toString() == "in") {
            viewModel.getTv("id", search)
        } else {
            viewModel.getTv("en-US", search)
        }
        showTv()
    }

    private fun showTv() {
        tvAdapter = TvAdapter()
        recycler_search.layoutManager = LinearLayoutManager(this)
        recycler_search.adapter = tvAdapter

        viewModel.response.observe(this, Observer {response ->
            if (response!=null) {
                showLoading(false)
                error.text = response
                tvAdapter.setData()
            }else {
                viewModel.tvItem.observe(this, Observer { tvItem ->
                    if (tvItem!=null) {
                        error.text = null
                        tvAdapter.setData(tvItem)
                        showLoading(false)
                    }else {
                        tvAdapter.setData()
                        error.text = getString(R.string.data_null)
                        showLoading(false)
                    }
                })
            }
        })

        tvAdapter.setOnItemClickCallback(object : TvAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ResultTv) {
                val intent = Intent(this@SearchActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_TV, data)
                startActivity(intent)
            }
        })
    }

    private fun getFavoriteMovie(search: String) {
        viewModel.getMovieFavorite(search)
        movieAdapter = MovieAdapter()
        recycler_search.layoutManager = LinearLayoutManager(this)
        recycler_search.adapter = movieAdapter

        viewModel.favoriteMovie.observe(this, Observer { movieFavorite ->
            if (movieFavorite.isNotEmpty()) {
                movieAdapter.setData(movieFavorite)
                error.text = null
                showLoading(false)
            }else {
                movieAdapter.setData()
                error.text = getString(R.string.data_null)
                showLoading(false)
            }
        })

        movieAdapter.setOnItemClickCallback(object : MovieAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ResultMovie) {
                val intent = Intent(this@SearchActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_MOVIE, data)
                startActivity(intent)
            }
        })
    }

    private fun getFavoriteTv(search: String) {
        viewModel.getTvFavorite(search)
        tvAdapter = TvAdapter()
        recycler_search.layoutManager = LinearLayoutManager(this)
        recycler_search.adapter = tvAdapter

        viewModel.favoriteTv.observe(this, Observer { tvFavorite ->
            if (tvFavorite.isNotEmpty()) {
                tvAdapter.setData(tvFavorite as ArrayList<ResultTv>)
                error.text = null
                showLoading(false)
            }else {
                tvAdapter.setData()
                error.text = getString(R.string.data_null)
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
