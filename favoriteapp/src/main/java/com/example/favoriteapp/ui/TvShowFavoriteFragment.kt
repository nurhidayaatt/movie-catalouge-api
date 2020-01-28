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
import com.example.favoriteapp.util.CursorHelper.convertToTvFavorite

import com.example.favoriteapp.R
import com.example.favoriteapp.model.ResultTv
import com.example.favoriteapp.adapter.TvAdapter
import kotlinx.android.synthetic.main.fragment_tv_show_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TvShowFavoriteFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        private const val AUTHORITY = "com.example.moviecatalougeapi"
        private const val BASE_PATH = "tv_favorite"
    }

    private val contentUri = Uri.Builder().scheme("content").authority(AUTHORITY).appendPath(BASE_PATH).build()
    private lateinit var tvAdapter: TvAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tv_show_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LoaderManager.getInstance(this).initLoader(1, null, this)
        tvAdapter = TvAdapter()
        tvAdapter.notifyDataSetChanged()
        recycler_tv_favorite.layoutManager = LinearLayoutManager(activity)
        recycler_tv_favorite.adapter = tvAdapter
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(requireContext(), contentUri, null, null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        GlobalScope.launch(Dispatchers.Main) {
            if (data != null) {
                tvAdapter.setData(convertToTvFavorite(data) as ArrayList<ResultTv>)
                tvAdapter.setOnItemClickCallback(object : TvAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: ResultTv) {
                        val intent = Intent(activity, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.EXTRA_TV, data)
                        startActivity(intent)
                    }
                })
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        tvAdapter.setData(null)
    }

    override fun onResume() {
        super.onResume()
        LoaderManager.getInstance(this).restartLoader(1, null, this)
    }
}
