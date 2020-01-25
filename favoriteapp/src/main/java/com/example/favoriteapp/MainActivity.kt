package com.example.favoriteapp

import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        private const val AUTHORITY = "com.example.moviecatalougeapi.data.provider"
        private const val BASE_PATH = "movie_favorite"
    }

    private val movieData = mutableListOf<ResultMovie>()
    //private lateinit var adapter : FavoritesAdapter

    private val contentUri = Uri.Builder()
        .scheme("content")
        .authority(AUTHORITY)
        .appendPath(BASE_PATH)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportLoaderManager.initLoader(1, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(this, contentUri, null, null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        data?.let{
            val movies = CursorHelper.convertToListFavorite(data)
            if(movies.isEmpty()) tes.text = movies.toString()
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        tes.text = movieData.toString()
    }

    override fun onResume() {
        super.onResume()
        supportLoaderManager.restartLoader(1, null, this)
    }
}
