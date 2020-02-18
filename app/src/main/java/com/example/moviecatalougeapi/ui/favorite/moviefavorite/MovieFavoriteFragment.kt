package com.example.moviecatalougeapi.ui.favorite.moviefavorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviecatalougeapi.R
import com.example.moviecatalougeapi.data.adapter.MovieAdapter
import com.example.moviecatalougeapi.data.model.movie.ResultMovie
import com.example.moviecatalougeapi.ui.detail.DetailActivity
import com.example.moviecatalougeapi.ui.search.SearchActivity
import com.example.moviecatalougeapi.util.widget.FavoriteWidget
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

        viewModel = ViewModelProvider(this).get(MovieFavoriteViewModel::class.java)
        movieAdapter = MovieAdapter()
        recycler_movie_favorite.layoutManager = LinearLayoutManager(activity)
        recycler_movie_favorite.adapter = movieAdapter

        viewModel.movies.observe(this, Observer { data ->
            if (data.isNotEmpty()) {
                fab_search.show()
                error.text = null
                movieAdapter.setData(data)
            }else {
                fab_search.hide()
                error.text = getString(R.string.data_null)
                movieAdapter.setData()
            }
        })

        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteFavorite(movieAdapter.getData(viewHolder.adapterPosition))
                sendUpdateFavoriteList(requireContext())
            }

        }).attachToRecyclerView(recycler_movie_favorite)

        movieAdapter.setOnItemClickCallback(object : MovieAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ResultMovie) {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                intent.putExtra(DetailActivity.EXTRA_MOVIE, data)
                startActivity(intent)
            }
        })
    }

    private fun sendUpdateFavoriteList(context: Context) {
        val intent = Intent(context, FavoriteWidget::class.java)
        intent.action = FavoriteWidget.UPDATE_WIDGET
        context.sendBroadcast(intent)
    }
}
