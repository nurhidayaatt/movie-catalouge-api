package com.example.moviecatalougeapi.ui.favorite.moviefavorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecatalougeapi.R
import com.example.moviecatalougeapi.data.adapter.MovieAdapter
import com.example.moviecatalougeapi.data.model.movie.ResultMovie
import com.example.moviecatalougeapi.ui.detail.DetailActivity
import com.example.moviecatalougeapi.ui.search.SearchActivity
import kotlinx.android.synthetic.main.movie_favorite_fragment.*

class MovieFavoriteFragment : Fragment() {

    private lateinit var viewModel: MovieFavoriteViewModel
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.movie_favorite_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fab_search.setOnClickListener {
            val intent = Intent(activity, SearchActivity::class.java)
            intent.putExtra(SearchActivity.EXTRA, SearchActivity.MOVIE_FAVORITE)
            startActivity(intent)
        }

        viewModel = ViewModelProviders.of(this).get(MovieFavoriteViewModel::class.java)

        movieAdapter = MovieAdapter()
        movieAdapter.notifyDataSetChanged()

        recycler_movie_favorite.layoutManager = LinearLayoutManager(activity)
        recycler_movie_favorite.adapter = movieAdapter

        viewModel.movies.observe(this, Observer { data ->
            if (data.size != 0) {
                fab_search.show()
                error.text = null
                movieAdapter.setData(data as ArrayList<ResultMovie>)
            }else {
                fab_search.hide()
                error.text = getString(R.string.data_null)
                movieAdapter.setData()
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

}
