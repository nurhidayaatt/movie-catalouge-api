package com.example.favoriteapp.ui


import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteapp.util.CursorHelper.convertToMovieFavorite
import com.example.favoriteapp.R
import com.example.favoriteapp.model.ResultMovie
import com.example.favoriteapp.adapter.MovieAdapter
import kotlinx.android.synthetic.main.fragment_movie_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MovieFavoriteFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        private const val AUTHORITY = "com.example.moviecatalougeapi"
        private const val BASE_PATH = "movie_favorite"
    }

    private val contentUri = Uri.Builder().scheme("content").authority(AUTHORITY).appendPath(BASE_PATH).build()
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LoaderManager.getInstance(this).initLoader(1, null, this)
        movieAdapter = MovieAdapter()
        recycler_movie_favorite.layoutManager = LinearLayoutManager(activity)
        recycler_movie_favorite.adapter = movieAdapter
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(requireContext(), contentUri, null, null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        GlobalScope.launch(Dispatchers.Main) {
            if (data != null) {
                movieAdapter.setData(convertToMovieFavorite(data))
                movieAdapter.setOnItemClickCallback(object : MovieAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: ResultMovie) {
                        val intent = Intent(activity, DetailActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                        intent.putExtra(DetailActivity.EXTRA_MOVIE, data)
                        startActivity(intent)
                    }
                })
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        movieAdapter.setData()
    }

    override fun onResume() {
        super.onResume()
        LoaderManager.getInstance(this).restartLoader(1, null, this)
    }
}
