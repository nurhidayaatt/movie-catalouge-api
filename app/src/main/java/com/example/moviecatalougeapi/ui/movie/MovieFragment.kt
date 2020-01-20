package com.example.moviecatalougeapi.ui.movie

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecatalougeapi.R
import com.example.moviecatalougeapi.data.adapter.MovieAdapter
import com.example.moviecatalougeapi.data.model.movie.ResultMovie
import com.example.moviecatalougeapi.ui.detail.DetailActivity
import com.example.moviecatalougeapi.ui.search.SearchActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.movie_fragment.*
import java.util.*

class MovieFragment : Fragment() {

    private lateinit var movieAdapter: MovieAdapter
    private lateinit var viewModel: MovieViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.movie_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getMovie()

        fab_search.setOnClickListener {
            val intent = Intent(activity, SearchActivity::class.java)
            intent.putExtra(SearchActivity.EXTRA, SearchActivity.MOVIE)
            startActivity(intent)
        }
    }

    private fun getMovie() {
        showLoading(true)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MovieViewModel::class.java)
        if (Locale.getDefault().language.toString() == "in") {
            viewModel.getMovie("id")
        } else {
            viewModel.getMovie("en-US")
        }
        showMovie()
    }

    private fun showMovie() {
        movieAdapter = MovieAdapter()
        movieAdapter.notifyDataSetChanged()

        recycler_movie.layoutManager = LinearLayoutManager(activity)
        recycler_movie.adapter = movieAdapter

        viewModel.response.observe(this, Observer {response ->
            if (response != null) {
                error.text = response
                Snackbar.make(coordinator, getString(R.string.error), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.try_again)) { getMovie() }.show()
                showLoading(false)
            } else {
                error.text = null
                viewModel.movieItem.observe(this, Observer { movieItem ->
                    if (movieItem != null) {
                        movieAdapter.setData(movieItem)
                        showLoading(false)
                    }
                })
            }
        })

        movieAdapter.setOnItemClickCallback(object : MovieAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ResultMovie) {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_MOVIE, data)
                startActivity(intent)
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
}
